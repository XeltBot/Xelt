package me.theditor.xelt.commands.moderation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Guild.Ban;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class UnBanCommand extends BaseCommand{
	
	private CommandCategory category;

	public UnBanCommand(CommandCategory category) {
		this.category = category;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		if (args.size() < 1) {
			MessageUtils.errorMessage("Invalid command usage!", e.getChannel());
			return;
		}
		if(args.size() == 1) {
			args.add("No reason specified");
		}
		
		if (!e.getMember().hasPermission(Permission.BAN_MEMBERS)) {
			MessageUtils.errorMessage("You do not have enough permissions to use this command!", e.getChannel());
			return;
		}
		
		long id = 0;
		try {
			id = Long.parseLong(args.get(0));
		} catch (Exception e1){
		}
		
		if(id == 0) {
			if(!args.get(0).contains("#")) {
				MessageUtils.errorMessage("Invalid user specified!", e.getChannel());
				return;
			}
		}
		
		User user = null;
		
		List<Ban> users = e.getGuild().retrieveBanList().complete();	
		for(Ban ban : users) {
			if(id != 0) {
				if(args.get(0).equals(ban.getUser().getName() + "#" + ban.getUser().getDiscriminator())) {
					user = ban.getUser();
					break;
				}
			} else {
				if(ban.getUser().getIdLong() == id) {
					user = ban.getUser();
					break;
				}
			}
		}
		
		if(user == null) {
			MessageUtils.errorMessage("Invalid user specified!", e.getChannel());
			return;
		}
		
		e.getGuild().unban(user).queue();

		ArrayList<String> msgs = new ArrayList<>();
		msgs.add(user.getAsMention() + " has been unbanned!");
		e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("UnBan Successful", msgs, Color.green, e.getChannel()).build()).queue();
	}

	@Override
	public String getName() {
		return "UnBan";
	}

	@Override
	public String getDescription() {
		return "UnBan a member";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public String getUsage(Guild guild) {
		return this.getName().toLowerCase() + " <id/tag>";
	}

	@Override
	public CommandCategory getParentCategory() {
		return category;
	}

}
