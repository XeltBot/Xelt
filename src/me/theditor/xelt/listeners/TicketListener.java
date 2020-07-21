package me.theditor.xelt.listeners;

import java.util.ArrayList;
import java.util.List;

import me.theditor.xelt.Xelt;
import me.theditor.xelt.utils.MessageUtils;
import me.theditor.xelt.utils.PastebinUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.MarkdownUtil;

public class TicketListener extends ListenerAdapter {

	private User starter = null;

	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e) {
		if (!e.getChannel().getName().contains("ticket-") || !e.getReactionEmote().getEmoji().equals("\uD83D\uDEAB")
				|| e.getMember().getUser().isBot()) {
			return;
		}

		starter = null;

		e.getReaction().removeReaction(e.getMember().getUser()).complete();

		String id = e.getChannel().getName().split("-")[1];
		
		String link = "";
		String raw = "";
		List<User> users = new ArrayList<>();
		
		boolean transcript = hasMessages(e.getChannel());

		while(hasMessages(e.getChannel())) {
		List<Message> messages = e.getChannel().getHistoryFromBeginning(100).complete().getRetrievedHistory();
		if (messages.size() >= 1) {
			raw = messages.get(messages.size() - 1).getAuthor().getAsTag() + ": "
					+ messages.get(messages.size() - 1).getContentDisplay();
			if (!messages.get(messages.size() - 1).getAuthor().isBot()
					&& !users.contains(messages.get(messages.size() - 1).getAuthor())) {
				users.add(messages.get(messages.size() - 1).getAuthor());
			}
			messages.get(messages.size() - 1).delete().complete();
			for (int i = messages.size() - 2; i >= 0; i--) {
				if (!messages.get(i).getAuthor().isBot()) {
					raw = raw + "\n" + messages.get(i).getAuthor().getAsTag() + ": "
							+ messages.get(i).getContentDisplay();
					if (!users.contains(messages.get(i).getAuthor())) {
						users.add(messages.get(i).getAuthor());
					}
				}
			}
			e.getChannel().deleteMessages(messages).complete();
			link = PastebinUtils.newPaste(e.getChannel().getName(), raw);
		}
		}

		e.getChannel().delete().queue();

		try {
			starter = Xelt.jda.getUserById(id);
		} catch (Exception e1) {
		}

		for (User user : users) {
			PrivateChannel channel = user.openPrivateChannel().complete();
			List<String> msgs = new ArrayList<>();
			msgs.add("The ticket you participated in Guild " + e.getGuild().getName() + " with Guild ID "
					+ e.getGuild().getId() + " has been closed!");
			if (starter != null)
				msgs.add("This ticket was started by a user " + starter.getAsTag() + " with ID " + id);
			if (transcript)
				msgs.add("Ticket Transcript can be found " + MarkdownUtil.maskedLink("here", link) + "!");
			msgs.add(MarkdownUtil.bold("Note:") + " This link is only available for one week!");
			msgs.add("You can now open a new ticket!");
			channel.sendMessage(MessageUtils.noFieldMessageEmbed("Ticket Closed", msgs, channel).build()).queue();
		}
	}
	
	private boolean hasMessages(TextChannel channel) {
		return !channel.getHistoryFromBeginning(2).complete().getRetrievedHistory().isEmpty();
	}

}
