package me.theditor.xelt.commands.fun;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class EightBallCommand extends BaseCommand {

	private CommandCategory category;

	public EightBallCommand(CommandCategory category) {
		this.category = category;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		if(args.size() < 1) {
			MessageUtils.errorMessage("Invalid command usage!", e.getChannel());
			return;
		}
		
		String question = args.get(0);
		for (int i = 1; i < args.size(); i++) {
			question = question + " " + args.get(i);
		}
		
		List<String> balls = new ArrayList<>();
		balls.add("Yes, surely");
		balls.add("Yes, surely");
		balls.add("No way");
		balls.add("No way");
		balls.add("Maybe");
		balls.add("I don't know");
		balls.add("Try again");
		balls.add("Probably");
		balls.add("Probably not");
		
		Collections.shuffle(balls);
		
		
		List<String> title = new ArrayList<>();
		List<String> msgs = new ArrayList<>();
		title.add("Question");
		msgs.add(question);
		title.add("8Ball Predicts");
		msgs.add(balls.get(0));
		e.getChannel().sendMessage(MessageUtils.multiFieldMessageEmbed(null, title, msgs, e.getChannel()).build()).queue();
	}

	@Override
	public String[] getAliases() {
		String[] aliases = {"Predict"};
		return aliases;
	}

	@Override
	public String getName() {
		return "8Ball";
	}	

	@Override
	public String getDescription() {
		return "Predicts the future!";
	}

	@Override
	public String getUsage(Guild guild) {
		return getName().toLowerCase() + " <question>";
	}

	@Override
	public CommandCategory getParentCategory() {
		return category;
	}
	
}
