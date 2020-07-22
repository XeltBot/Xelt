package me.theditor.xelt.commands.miscellaneous;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PurgeCommand extends BaseCommand {
	
	private CommandCategory category;
	private List<Guild> cooldown;
	private Timer timer;

	public PurgeCommand(CommandCategory category) {
		this.category = category;
		cooldown = new ArrayList<>();
		timer = new Timer();
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		if (args.size() < 1) {
			MessageUtils.errorMessage("Invalid command usage!", e.getChannel());
			return;
		}
		
		if(!e.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
			MessageUtils.errorMessage("You do not have enough permissions to use this command!",
					e.getChannel());
			return;
		}

		int limit = 0;

		try {
			limit = Integer.parseInt(args.get(0));
		} catch (Exception e1) {
		}

		if (limit == 0) {
			MessageUtils.errorMessage("Invalid command usage!", e.getChannel());
			return;
		}
		
		if(limit > 100) {
			MessageUtils.errorMessage("Invalid command usage!", e.getChannel());
			return;
		}
		
		if (cooldown.contains(e.getGuild())) {
			MessageUtils.errorMessage("Command on cooldown, please wait 3 seconds!", e.getChannel());
		}

		Message delete = e.getMessage();

		List<Message> messages = e.getChannel().getHistoryBefore(delete, limit).complete().getRetrievedHistory();
		
		if (messages.size() == 1) {
			messages.get(0).delete().complete();
		} else {
			e.getChannel().deleteMessages(messages).complete();
		}
		
		delete.delete().queue();
		
		cooldown.add(e.getGuild());
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				cooldown.remove(e.getGuild());
			}
			
		}, 1000 * 3);
		
		List<String> msgs = new ArrayList<>();
		msgs.add("Purged " + limit + " messages from channel " + e.getChannel().getAsMention());
		msgs.add("This message will self-destruct in 3 seconds!");
		e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Purge Complete", msgs, Color.green, e.getChannel()).build()).complete().delete().queueAfter(3, TimeUnit.SECONDS);

	}

	@Override
	public String getName() {
		return "Purge";
	}

	@Override
	public String getUsage(Guild guild) {
		return getName().toLowerCase() + " <limit>";
	}

	@Override
	public String getDescription() {
		return "Purge/delete messages upto the limit (<=100)";
	}

	@Override
	public String[] getAliases() {
		String[] aliases = {"delete","clean"};
		return aliases;
	}

	@Override
	public CommandCategory getParentCategory() {
		return category;
	}

}
