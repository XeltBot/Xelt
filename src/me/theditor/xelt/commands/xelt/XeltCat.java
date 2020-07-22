package me.theditor.xelt.commands.xelt;

import java.util.ArrayList;
import java.util.List;

import me.theditor.xelt.Xelt;
import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;

public class XeltCat extends CommandCategory {
	
private List<BaseCommand> commands;
	
	public XeltCat() {
		commands = new ArrayList<>();
		commands.add(new DonateCommand(this));
		commands.add(new StatsCommand(this));
		commands.add(new InviteCommand(this));
		commands.add(new UpvoteCommand(this));
		commands.add(new FeedbackCommand(this));
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
		return Xelt.getInstance().getName();
	}

}
