package me.theditor.xelt.commands.api;

import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public abstract class BaseCommand {
	
	/**
	 * Executes on command execution
	 */
	public abstract void run(GuildMessageReceivedEvent e, List<String> args);
	
	/**
	 * Returns command name
	 */
	public abstract String getName();
	
	/**
	 * Returns command usage
	 */
	public abstract String getUsage(Guild guild);
	
	/**
	 * Returns command description
	 */
	public abstract String getDescription();
	
	/**
	 * Returns aliases of the command
	 */
	public abstract String[] getAliases();

	/**
	 * 
	 */
	public abstract CommandCategory getParentCategory();
}
