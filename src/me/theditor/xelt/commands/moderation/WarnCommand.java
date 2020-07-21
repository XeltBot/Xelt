package me.theditor.xelt.commands.moderation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class WarnCommand extends BaseCommand{
	
	private CommandCategory category;

	public WarnCommand(CommandCategory category) {
		this.category = category;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		if (args.size() < 2) {
			MessageUtils.errorMessage("Invalid command usage!", e.getChannel());
			return;
		}
		
		if (!e.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
			MessageUtils.errorMessage("You do not have enough permissions to use this command!", e.getChannel());
			return;
		}
		Member member = null;
		List<Member> mentioned = e.getMessage().getMentionedMembers();
		if (mentioned.size() == 0) {
			MessageUtils.errorMessage("Invalid member specified!", e.getChannel());
			return;
		} else {
			member = mentioned.get(0);
		}

		if (member.equals(e.getMember())) {
			MessageUtils.errorMessage("You cannot warn yourself!", e.getChannel());
			return;
		}

		// Calculating who is more powerful
		int sender = 0;
		int victim = 0;
		if (e.getMember().getRoles() != null && member.getRoles() != null) {
			for (Role role : e.getMember().getRoles()) {
				sender = sender < role.getPosition() ? role.getPosition() : sender;
			}
			for (Role role : member.getRoles()) {
				victim = victim < role.getPosition() ? role.getPosition() : victim;
			}
		}
		if (victim >= sender && (!e.getMember().isOwner())) {
			MessageUtils.errorMessage(
					"You do not have enough permissions to perform this action on " + member.getAsMention(),
					e.getChannel());
			return;
		}

		String reason = args.get(1);
		for (int i = 2; i < args.size(); i++) {
			reason = reason + " " + args.get(i);
		}
		
		List<String> msgs = new ArrayList<>();
		msgs.add("You have been warned in guild " + e.getGuild().getName());
		msgs.add("Warning: " + reason);
		
		member.getUser().openPrivateChannel().queue(channel -> {
			channel.sendMessage(MessageUtils.noFieldMessageEmbed("Warning", msgs, channel).build()).queue();
		});

		List<String> msgs1 = new ArrayList<>();
		msgs1.add(member.getAsMention() + " has been warned!");
		e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Warn Successful", msgs1, Color.green, e.getChannel()).build()).queue();
	}

	@Override
	public String getName() {
		return "Warn";
	}

	@Override
	public String getUsage(Guild guild) {
		return this.getName().toLowerCase() + " <mention> <warning>";
	}

	@Override
	public String getDescription() {
		return "Warn a member";
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
