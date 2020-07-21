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

public class HugCommand extends BaseCommand {
	
	private CommandCategory category;

	public HugCommand(CommandCategory category) {
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
		gifs.add("https://media.tenor.com/images/fec973cdb301f5179dca6eef16499ab0/tenor.gif");
		gifs.add("https://media.tenor.com/images/78d3f21a608a4ff0c8a09ec12ffe763d/tenor.gif");
		gifs.add("https://media.tenor.com/images/d5c635dcb613a9732cfd997b6a048f80/tenor.gif");
		gifs.add("https://media.tenor.com/images/1069921ddcf38ff722125c8f65401c28/tenor.gif");
		gifs.add("https://media.tenor.com/images/78d3f21a608a4ff0c8a09ec12ffe763d/tenor.gif");
		
		Collections.shuffle(gifs);

		List<String> msgs = new ArrayList<>();
		msgs.add(e.getMember().getAsMention() + " hugged " + member.getAsMention());
		e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Hugs", msgs, e.getChannel()).setImage(gifs.get(0)).build()).queue();
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public String getName() {
		return "Hug";
	}

	@Override
	public String getDescription() {
		return "Hug a user!";
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
