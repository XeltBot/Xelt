package me.theditor.xelt.commands.adminonly;

import java.util.ArrayList;
import java.util.List;

import me.theditor.xelt.Xelt;
import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class StatsCommand extends BaseCommand {
	
private CommandCategory category;
	
	public StatsCommand(CommandCategory category) {
		this.category = category;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		List<String> title = new ArrayList<>();
		List<String> msgs = new ArrayList<>();
		
		title.add("Guilds");
		msgs.add("" +Xelt.jda.getGuilds().size());
		
		title.add("Users");
		msgs.add("" + Xelt.jda.getUsers().size());
		
		title.add("CPU");
		msgs.add(Runtime.getRuntime().availableProcessors() + " Cores");
		
		title.add("Memory");
		long maxMemory = Runtime.getRuntime().maxMemory();
		msgs.add((Runtime.getRuntime().freeMemory() * Math.pow(10,-9)) + " GB Free\n" +
		(Runtime.getRuntime().totalMemory() * Math.pow(10,-9)) + " GB Allocated\n" +
		((maxMemory == Long.MAX_VALUE) ? "No Limit" : (maxMemory * Math.pow(10,-9) + " GB Max")));
		
		e.getChannel().sendMessage(MessageUtils.multiFieldMessageEmbed(Xelt.getInstance().getName() + " Stats", title, msgs, e.getChannel()).build()).queue();
	}

	@Override
	public String getName() {
		return "Stats";
	}

	@Override
	public String getUsage(Guild guild) {
		return getName().toLowerCase();
	}

	@Override
	public String getDescription() {
		return "Get the bot's stats (Admin only)";
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
