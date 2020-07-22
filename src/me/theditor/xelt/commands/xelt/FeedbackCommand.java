package me.theditor.xelt.commands.xelt;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.theditor.xelt.Xelt;
import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.listeners.XeltListener;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

public class FeedbackCommand extends BaseCommand {
	
	private CommandCategory category;

	public FeedbackCommand(CommandCategory category) {
		this.category = category;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		List<String> msgs = new ArrayList<>();
		msgs.add("Type your feedback about " + Xelt.getInstance().getName() + " and send it after this message");
		msgs.add(MarkdownUtil.bold("Note: ") + "This will timeout in 10 seconds!");
		e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Feedback", msgs, e.getChannel()).build()).queue(mess -> {
			XeltListener.addMessageListener(e.getAuthor(), message -> {
				XeltListener.removeMessageListener(e.getAuthor());
				msgs.clear();
				msgs.add(message.getContentDisplay());
				EmbedBuilder builder = MessageUtils.noFieldMessageEmbed("Feedback", msgs, e.getChannel());
				String url = message.getAuthor().getAvatarUrl();
				builder.setAuthor(message.getAuthor().getAsTag(), null, (url == null ) ? message.getAuthor().getDefaultAvatarUrl() : url);
				SimpleDateFormat format = new SimpleDateFormat("E, MMMM d, yyyy HH:mm a");
				builder.addField("Info", format.format(Date.from(message.getTimeCreated().toInstant())), false);
				Xelt.jda.getGuildById("730775873480949830").getTextChannelById("735406286459633675")
					.sendMessage(builder.build()).queue(message1 -> {
						msgs.clear();
						msgs.add("Your feedback has been sent to " + Xelt.getInstance().getName() + "'s Support Guild!");
						e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Feedback Sent", msgs, Color.green, e.getChannel()).build()).queue();
					});
			}, () -> {
				mess.delete().queue();
				MessageUtils.errorMessage(e.getMember().getAsMention() + " took too long to respond!", e.getChannel());
			});
		});
	}

	@Override
	public String getName() {
		return "Feedback";
	}

	@Override
	public String getUsage(Guild guild) {
		return this.getName().toLowerCase();
	}

	@Override
	public String getDescription() {
		return "Provid feedback for " + Xelt.getInstance().getName();
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public CommandCategory getParentCategory() {
		return category;
	}

}
