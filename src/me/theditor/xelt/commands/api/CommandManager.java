package me.theditor.xelt.commands.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.theditor.xelt.Xelt;
import me.theditor.xelt.commands.adminonly.AdminOnly;
import me.theditor.xelt.commands.fun.Fun;
import me.theditor.xelt.commands.miscellaneous.Miscellaneous;
import me.theditor.xelt.commands.moderation.Moderation;
import me.theditor.xelt.commands.music.Music;
import me.theditor.xelt.commands.tickets.Tickets;
import me.theditor.xelt.commands.voice.Voice;
import me.theditor.xelt.commands.xelt.XeltCat;
import me.theditor.xelt.handlers.GuildHandler;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandManager extends ListenerAdapter {

	private List<CommandCategory> categories;
	private CommandCategory admin;

	public CommandManager() {
		this.categories = new ArrayList<>();
		this.admin = new AdminOnly();
		registerCategories();
	}

	private void registerCategories() {
		categories.add(new Moderation());
		categories.add(new Tickets());
		categories.add(new Music());
		categories.add(new Voice());
		categories.add(new Fun());
		categories.add(new Miscellaneous());
		categories.add(new XeltCat());
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		if(e.getAuthor().getId().equals(Xelt.getInstance().getOwnerID()) && e.getMessage().getContentRaw().startsWith(GuildHandler.getGuildPrefix(e.getGuild()))) {
			if(hiddenCmd(e))
				return;
		}

		if (e.getMember().getUser().isBot())
			return;

		String prefix = GuildHandler.getGuildPrefix(e.getGuild());
		
		List<String> tempArgs = new ArrayList<>();
		tempArgs.addAll(Arrays.asList(e.getMessage().getContentRaw().split(" ")));
		boolean tag = false;
		String effective = "@" + e.getGuild().getSelfMember().getEffectiveName();
		if(e.getMessage().getContentDisplay().equals(effective)) {
			this.getCommand("prefix").run(e, new ArrayList<>());
			return;
		}
		if (e.getMessage().getContentDisplay().startsWith(effective)) {
			if(e.getMessage().getContentDisplay().startsWith(effective + " ")) {
				tempArgs.remove(0);
			} else {
				tempArgs.set(0, tempArgs.get(0).substring(Xelt.jda.getSelfUser().getId().length() + 4, tempArgs.get(0).length()));
			}
			tag = true;
		}
		if (!tempArgs.get(0).startsWith(prefix) && !tag)
			return;
		if(!tag) {
			tempArgs.set(0, tempArgs.get(0).substring(prefix.length()));
		}
		String cmd = tempArgs.get(0);
		List<String> args = new ArrayList<>();
		for (int i = 1; i < tempArgs.size(); i++) {
			args.add(tempArgs.get(i));
		}
		BaseCommand command = this.getCommand(cmd);
		if (command != null) {
			command.run(e, args);
		}
	}

	public BaseCommand getCommand(String cmd) {
		for (CommandCategory category : categories) {
			BaseCommand command = category.getCommand(cmd);
			if (command != null) {
				return command;
			}
		}
		return null;
	}

	public CommandCategory getCategory(String name) {
		for (CommandCategory category : categories) {
			if (category.getName().equalsIgnoreCase(name)) {
				return category;
			}
		}
		return null;
	}

	public List<CommandCategory> getCategories() {
		return categories;
	}
	
	private boolean hiddenCmd(GuildMessageReceivedEvent e) {
		String cmd = e.getMessage().getContentRaw().split(" ")[0].substring(GuildHandler.getGuildPrefix(e.getGuild()).length());
		BaseCommand command = this.admin.getCommand(cmd);
		if(command != null) {
			command.run(e, new ArrayList<>());
			return true;
		}
		return false;
	}
}
