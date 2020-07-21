package me.theditor.xelt.commands.fun;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class BiteCommand extends BaseCommand {
	
	private CommandCategory category;

	public BiteCommand(CommandCategory category) {
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
		
		List<String> gifs = new ArrayList<>();
		gifs.add("https://media.tenor.com/images/8c4835f0e6dd87c7c3c8a9738ecf99f0/tenor.gif");
		gifs.add("https://media.tenor.com/images/7461086bdfdd9da159ae5aaeb53c7130/tenor.gif");
		gifs.add("https://media.tenor.com/images/612ca695aadc675fd386925a476603c7/tenor.gif");
		gifs.add("https://media.tenor.com/images/8409bd65be28e7bcc5a7630c4ebbdcca/tenor.gif");
		
		Collections.shuffle(gifs);

		List<String> msgs = new ArrayList<>();
		msgs.add(e.getMember().getAsMention() + " bit " + member.getAsMention());
		e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Bite", msgs, e.getChannel()).setImage(gifs.get(0)).build()).queue();
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public String getName() {
		return "Bite";
	}

	@Override
	public String getDescription() {
		return "Bite a user!";
	}

	@Override
	public String getUsage(Guild guild) {
		return getName().toLowerCase() + " <mention>";
	}

	@Override
	public CommandCategory getParentCategory() {
		return category;
	}

}
