package me.theditor.xelt.commands.miscellaneous;

import java.util.ArrayList;
import java.util.List;

import me.theditor.xelt.Xelt;
import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.handlers.GuildHandler;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class HelpCommand extends BaseCommand {
	private CommandCategory category;

	public HelpCommand(CommandCategory category) {
		this.category = category;
	}

	private Xelt xelt = Xelt.getInstance();

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		if(args.size() > 0) {
			BaseCommand cmd = xelt.getCommandManager().getCommand(args.get(0));
			if(cmd == null) {
				MessageUtils.errorMessage("Invalid command specified!", e.getChannel());
				return;
			}
			
			List<String> title = new ArrayList<>();
			List<String> msgs = new ArrayList<>();
			
			title.add("Description:");
			msgs.add(cmd.getDescription());
			title.add("Usage:");
			msgs.add("`"+ GuildHandler.getGuildPrefix(e.getGuild()) + cmd.getUsage(e.getGuild()) + "`");
			String[] aliase = cmd.getAliases();
			if(aliase != null) {
				title.add("Aliases:");
				String aliases = aliase[0];
				for(int i = 1; i < aliase.length; i++) {
					aliases = aliases + ", " + aliase[i];
				}
				msgs.add(aliases);
			}
			e.getChannel().sendMessage(MessageUtils.multiFieldMessageEmbed(cmd.getName() + " Command", title, msgs, e.getChannel()).build()).queue();
			return;
		}
		List<CommandCategory> categories = xelt.getCommandManager().getCategories();
		List<String> title = new ArrayList<>();
		List<String> cmds = new ArrayList<>();
		String prefix = GuildHandler.getGuildPrefix(e.getGuild());
		
		for(CommandCategory category : categories) {
			if(category.getCommands().size() < 1) {
				continue;
			}
			title.add(category.getName());
			List<BaseCommand> commands = category.getCommands();
			String cmd = prefix + commands.get(0).getUsage(e.getGuild()) + " | " + commands.get(0).getDescription();
			for(int i = 1; i < commands.size(); i++) {
				BaseCommand command = commands.get(i);
				cmd = cmd + "\n" + prefix + command.getUsage(e.getGuild()) + " | " + command.getDescription();
			}
			cmds.add(cmd);
		}

		e.getChannel().sendMessage(MessageUtils.multiFieldMessageEmbed("Help", title, cmds, e.getChannel()).build()).queue();
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public String getName() {
		return "Help";
	}

	@Override
	public String getDescription() {
		return "Returns this help menu!";
	}

	@Override
	public String getUsage(Guild guild) {
		return getName().toLowerCase() + " [command]";
	}

	@Override
	public CommandCategory getParentCategory() {
		return category;
	}

}
