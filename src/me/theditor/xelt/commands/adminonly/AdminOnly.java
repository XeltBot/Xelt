package me.theditor.xelt.commands.adminonly;

import java.util.ArrayList;
import java.util.List;

import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;

public class AdminOnly extends CommandCategory {
	
	private List<BaseCommand> commands;
	
	public AdminOnly() {
		commands = new ArrayList<>();
		commands.add(new ShutdownCommand(this));
		commands.add(new ListGuildsCommand(this));
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
		return "Admin";
	}

}