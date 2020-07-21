package me.theditor.xelt.commands.moderation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import me.theditor.xelt.Xelt;
import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class BanCommand extends BaseCommand {
	
	private CommandCategory category;

	public BanCommand(CommandCategory category) {
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
		Member member = null;
		List<Member> mentioned = e.getMessage().getMentionedMembers();
		if (mentioned.size() == 0) {
//			try {
//				e.getChannel().sendMessage(Long.parseLong(args.get(0)) + " IS THE SPECIFIED USER!").queue();
//				for(Member member1 : e.getGuild().getMembers()) {
//					e.getChannel().sendMessage(member1.getIdLong() + " " + member1.getAsMention()).queue();
//					if(member1.getIdLong() == Long.parseLong(args.get(0))) {
//						member = member1;
//						break;
//					}
//				}
//				member = e.getGuild().getMemberById(args.get(0));
//			} catch (Exception e1) {
//			}
			MessageUtils.errorMessage("Invalid member specified!", e.getChannel());
			return;
		} else {
			member = mentioned.get(0);
		}

		if (member.equals(e.getMember())) {
			MessageUtils.errorMessage("You cannot ban yourself!", e.getChannel());
			return;
		}

		// Calculating who is more powerful
		int sender = 0;
		int victim = 0;
		int bot = 0;
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
		for(Role role : e.getGuild().getMember(Xelt.jda.getSelfUser()).getRoles()) {
			bot = bot < role.getPosition() ? role.getPosition() : bot;
		}
		if (bot <= victim) {
			MessageUtils.errorMessage("Unable to ban " + member.getAsMention()
					+ "!\n" + Xelt.getInstance().getName()+ " should have role higher than him!", e.getChannel());
			return;
		}

		String reason = args.get(1);
		for (int i = 2; i < args.size(); i++) {
			reason = reason + " " + args.get(i);
		}
		
		member.ban(1, reason).queue();

		ArrayList<String> msgs = new ArrayList<>();
		msgs.add(member.getAsMention() + " has been banned!");
		e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Ban Successful", msgs, Color.green, e.getChannel()).build()).queue();
	}

	@Override
	public String getName() {
		return "Ban";
	}

	@Override
	public String getDescription() {
		return "Ban a member";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public String getUsage(Guild guild) {
		return this.getName().toLowerCase() + " <mention> [reason]";
	}

	@Override
	public CommandCategory getParentCategory() {
		return category;
	}

}
