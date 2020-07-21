package me.theditor.xelt.commands.currency;

import java.util.ArrayList;
import java.util.List;

import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;

public class Currency extends CommandCategory {
	
	private List<BaseCommand> commands;
	
	public Currency() {
		commands = new ArrayList<>();
		commands.add(new CreateCommand(this));
		commands.add(new BalanceCommand(this));
		commands.add(new BegCommand(this));
		commands.add(new SearchCommand(this));
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
		return "Currency";
	}

}
