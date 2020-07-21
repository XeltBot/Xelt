package me.theditor.xelt.commands.miscellaneous;

import java.util.ArrayList;
import java.util.List;

import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class AvatarCommand extends BaseCommand {
	
	private CommandCategory category;

	public AvatarCommand(CommandCategory category) {
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
		
		if(member.getUser().getAvatarUrl() == null) {
			MessageUtils.errorMessage(member.getAsMention() + " does not have a avatar!", e.getChannel());
			return;
		}
		
		EmbedBuilder builder = MessageUtils.noFieldMessageEmbed(null , new ArrayList<>(), e.getChannel());
		builder.setAuthor("Avatar • " + member.getUser().getAsTag());
		builder.setImage(member.getUser().getAvatarUrl());
		
		e.getChannel().sendMessage(builder.build()).queue();
	}

	@Override
	public String getName() {
		return "Avatar";
	}

	@Override
	public String getUsage(Guild guild) {
		return this.getName().toLowerCase() + " <mention>";
	}

	@Override
	public String getDescription() {
		return "Get user avatar!";
	}

	@Override
	public String[] getAliases() {
		String[] aliases = {"av"};
		return aliases;
	}

	@Override
	public CommandCategory getParentCategory() {
		return category;
	}

}
