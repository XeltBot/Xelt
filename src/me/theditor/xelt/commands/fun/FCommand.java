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

public class FCommand extends BaseCommand{
	
	private CommandCategory category;

	public FCommand(CommandCategory category) {
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
		gifs.add("https://media.tenor.com/images/dba59225f62211b83b702d306ef293e4/tenor.gif");
		gifs.add("https://media.tenor.com/images/7e20dadf1a4a3b3b9c14979efb1a94b9/tenor.gif");
		gifs.add("https://media.tenor.com/images/3439ae587477c0847c1ae161e0822788/tenor.gif");
		
		Collections.shuffle(gifs);

		List<String> msgs = new ArrayList<>();
		msgs.add("Press F to pay respect to " + member.getAsMention());
		e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Press F", msgs, e.getChannel()).setImage(gifs.get(0)).build()).queue(message ->{
			message.addReaction("\uD83C\uDDEB").queue();
		});
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public String getName() {
		return "F";
	}

	@Override
	public String getDescription() {
		return "Pay respect to a user!";
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
