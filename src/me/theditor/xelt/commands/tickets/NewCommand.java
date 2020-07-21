package me.theditor.xelt.commands.tickets;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import me.theditor.xelt.Xelt;
import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class NewCommand extends BaseCommand {
	
	private CommandCategory category;

	public NewCommand(CommandCategory category) {
		this.category = category;
	}

	private Role everyone;

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		if (!e.getGuild().getMember(Xelt.jda.getSelfUser()).hasPermission(Permission.MANAGE_CHANNEL)) {
			MessageUtils.errorMessage(
					Xelt.getInstance().getName() + " does not have enough permissions to perform this action!",
					e.getChannel());
			return;
		}

		List<TextChannel> channel = e.getGuild().getTextChannelsByName("ticket-" + e.getMember().getId(), false);

		if (channel != null && !channel.isEmpty()) {
			MessageUtils.errorMessage("You already have an existing ticket!", e.getChannel());
			return;
		}

		List<Category> categories = e.getGuild().getCategoriesByName("Tickets", false);
		if (categories == null || categories.isEmpty()) {
			e.getGuild().createCategory("Tickets").complete();
		}
		for (Role role : e.getGuild().getRoles()) {
			if (role.isPublicRole()) {
				everyone = role;
				break;
			}
		}

		categories = e.getGuild().getCategoriesByName("Tickets", false);
		TextChannel chan = categories.get(0).createTextChannel("ticket-" + e.getMember().getId()).complete();
		List<String> msgs = new ArrayList<>();
		chan.putPermissionOverride(everyone).setDeny(Permission.MESSAGE_READ).queue();
		
		chan.putPermissionOverride(e.getMember()).setAllow(Permission.MESSAGE_WRITE, Permission.MESSAGE_READ, Permission.MESSAGE_HISTORY).queue();

		List<String> list = new ArrayList<>();
		list.add("React with \uD83D\uDEAB to close this ticket!");
		Message ticket = chan.sendMessage(MessageUtils.noFieldMessageEmbed("Ticket - " + e.getMember().getId(), list, chan).build()).complete();
		ticket.addReaction("\uD83D\uDEAB").queue();
		
		msgs.add("A new ticket " + chan.getAsMention() + " has been created for " + e.getMember().getAsMention());
		e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Ticket Created", msgs, Color.green, e.getChannel()).build()).queue();
	}

	@Override
	public String getName() {
		return "New";
	}

	@Override
	public String getUsage(Guild guild) {
		return this.getName().toLowerCase();
	}

	@Override
	public String getDescription() {
		return "Create a new ticket";
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
