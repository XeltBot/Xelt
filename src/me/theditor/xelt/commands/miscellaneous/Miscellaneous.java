package me.theditor.xelt.commands.miscellaneous;

import java.util.ArrayList;
import java.util.List;

import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;

public class Miscellaneous extends CommandCategory{
	
	private List<BaseCommand> commands;
	
	public Miscellaneous() {
		commands = new ArrayList<>();
		commands.add(new HelpCommand(this));
		commands.add(new InviteCommand(this));
		commands.add(new PrefixCommand(this));
		commands.add(new PurgeCommand(this));
		commands.add(new WhoIsCommand(this));
		commands.add(new AvatarCommand(this));
		commands.add(new ServerInfoCommand(this));
		commands.add(new DonateCommand(this));
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
		return "Miscellaneous";
	}

}
