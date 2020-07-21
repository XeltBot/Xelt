package me.theditor.xelt.commands.adminonly;

import java.util.List;

import me.theditor.xelt.Xelt;
import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ShutdownCommand extends BaseCommand {

	private CommandCategory category;
	
	public ShutdownCommand(CommandCategory category) {
		this.category = category;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		Xelt.getInstance().shutdown();
	}

	@Override
	public String getName() {
		return "Shutdown";
	}

	@Override
	public String getUsage(Guild guild) {
		return getName().toLowerCase();
	}

	@Override
	public String getDescription() {
		return "Shutdown the bot (Admin only)";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public CommandCategory getParentCategory() {
		return this.category;
	}

}
