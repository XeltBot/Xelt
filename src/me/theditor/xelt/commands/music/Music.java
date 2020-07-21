package me.theditor.xelt.commands.music;

import java.util.ArrayList;
import java.util.List;

import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.commands.api.BaseCommand;

public class Music extends CommandCategory{

private List<BaseCommand> commands;
	
	public Music() {
		commands = new ArrayList<>();
		commands.add(new JoinCommand(this));
		commands.add(new LeaveCommand(this));
		commands.add(new PlayCommand(this));
		commands.add(new NowPlayingCommand(this));
		commands.add(new SkipCommand(this));
		commands.add(new PauseCommand(this));
		commands.add(new ResumeCommand(this));
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
		return "Music";
	}

}
