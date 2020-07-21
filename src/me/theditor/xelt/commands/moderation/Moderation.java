package me.theditor.xelt.commands.moderation;

import java.util.ArrayList;
import java.util.List;

import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.commands.api.BaseCommand;

public class Moderation extends CommandCategory{

	private List<BaseCommand> commands;
	
	public Moderation() {
		commands = new ArrayList<>();
		commands.add(new BanCommand(this));
		commands.add(new UnBanCommand(this));
		commands.add(new KickCommand(this));
		commands.add(new WarnCommand(this));
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
		return "Moderation";
	}
}
