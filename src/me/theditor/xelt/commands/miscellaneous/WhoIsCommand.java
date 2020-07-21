package me.theditor.xelt.commands.miscellaneous;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class WhoIsCommand extends BaseCommand{
	private CommandCategory category;

	public WhoIsCommand(CommandCategory category) {
		this.category = category;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		if (args.size() < 1) {
			MessageUtils.errorMessage("Invalid command usage!", e.getChannel());
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
		
		SimpleDateFormat format = new SimpleDateFormat("E, MMMM d, yyyy HH:mm a");
		
		List<Role> roles = member.getRoles();
		EnumSet<Permission> perms = member.getPermissions();
		List<String> title = new ArrayList<>();
		List<String> msgs = new ArrayList<>();
		
		title.add("Joined");
		msgs.add(format.format(Date.from(member.getTimeJoined().toInstant())));
		title.add("Registered");
		msgs.add(format.format(Date.from(member.getTimeCreated().toInstant())));
		
		if(roles.size() > 0) {
			title.add("Roles [" + roles.size() + "]");
			String msg = roles.get(0).getAsMention();
			for(int i = 1; i < roles.size(); i++) {
				msg = msg + " " + roles.get(i).getAsMention();
			}
			msgs.add(msg);
		}
		
		if(perms.size() > 0) {
			title.add("Permissions");
			String msg = "";
			for(Permission perm : perms) {
				if(perm.isGuild()) {
					if(msg.equals("")) {
						msg = perm.getName() + ", ";
					} else {
						msg = msg + perm.getName() + ", ";
					}
				}
			}
			msg = msg.substring(0, msg.length() - (", ").length());
			msgs.add(msg);
		}
		
		title.add("Acknowledgements");
		msgs.add(member.isOwner()? "Server Owner" : member.hasPermission(Permission.ADMINISTRATOR) ? "Server Admin" : "Normal Member");
		
		EmbedBuilder embed = MessageUtils.multiFieldMessageEmbed(null, title, msgs, e.getChannel());
		embed.setAuthor(member.getUser().getAsTag(), null, member.getUser().getAvatarUrl());
		embed.setThumbnail(member.getUser().getAvatarUrl());
		embed.setDescription(member.getAsMention() + "\nID: " + member.getId());
		e.getChannel().sendMessage(embed.build()).queue();
	}

	@Override
	public String getName() {
		return "WhoIs";
	}

	@Override
	public String getUsage(Guild guild) {
		return this.getName().toLowerCase() + " <mention>";
	}

	@Override
	public String getDescription() {
		return "Get user information!";
	}

	@Override
	public String[] getAliases() {
		String[] aliases = {"userinfo"};
		return aliases;
	}

	@Override
	public CommandCategory getParentCategory() {
		return category;
	}
}
