package me.theditor.xelt.commands.fun;

import java.util.ArrayList;
import java.util.List;

import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;

public class Fun extends CommandCategory{

private List<BaseCommand> commands;
	
	public Fun() {
		commands = new ArrayList<>();
		commands.add(new RollCommand(this));
		commands.add(new FCommand(this));
		commands.add(new BiteCommand(this));
		commands.add(new HugCommand(this));
		commands.add(new EightBallCommand(this));
		commands.add(new EnlargeCommand(this));
		commands.add(new MemeCommand(this));
	}

	@Override
	public BaseCommand getCommand(String cmd) {
		for(BaseCommand command : this.commands) {
			if(command.getName().equalsIgnoreCase(cmd)) {
				return command;
			}else if(command.getAliases() != null){
				for(String aliase : command.getAliases()) {
					if(aliase.equalsIgnoreCase(cmd)) {
						return command;
					}
				}
			}
		}
		return null;
	}

	@Override
	public List<BaseCommand> getCommands() {
		return commands;
	}

	@Override
	public String getName() {
		return "Fun";
	}

}
