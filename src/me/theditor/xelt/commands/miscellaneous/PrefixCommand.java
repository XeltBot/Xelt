package me.theditor.xelt.commands.miscellaneous;

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.theditor.xelt.Xelt;
import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.handlers.GuildHandler;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PrefixCommand extends BaseCommand {

	private CommandCategory category;

	public PrefixCommand(CommandCategory category) {
		this.category = category;
	}
	
	private Xelt xelt = Xelt.getInstance();

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		List<String> msgs = new ArrayList<>();
		if (args.size() == 0) {
			msgs.add("The prefix of guild " + e.getGuild().getName() + " is "
					+ GuildHandler.getGuildPrefix(e.getGuild()));
			e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Guild Prefix", msgs, e.getChannel()).build()).queue();
		} else if (args.size() == 1) {
			if (!e.getMember().hasPermission(Permission.MANAGE_SERVER)) {
				MessageUtils.errorMessage("You do not have enough permissions to use this command!",
						e.getChannel());
				return;
			}

			try {
				PreparedStatement statement = xelt.getMySQL().getConnection()
						.prepareStatement("UPDATE Guilds SET Prefix=? WHERE ID=?");
				statement.setString(1, args.get(0));
				statement.setString(2, e.getGuild().getId());
				xelt.getMySQL().update(statement);
				msgs.add("Successfully changed prefix to " + args.get(0) + " for Guild " + e.getGuild().getName());
				e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Prefix Changed", msgs, Color.green, e.getChannel()).build()).queue();
			} catch (SQLException e1) {
				e1.printStackTrace();
				MessageUtils.errorMessage("An error occured while performing that action!", e.getChannel());
			}

		} else {
			MessageUtils.errorMessage("Invalid Command Usage!", e.getChannel());
			return;
		}
	}

	@Override
	public String getName() {
		return "Prefix";
	}

	@Override
	public String getUsage(Guild guild) {
		return getName().toLowerCase() + " [prefix]";
	}

	@Override
	public String getDescription() {
		return "View or set the prefix of the guild";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public CommandCategory getParentCategory() {
		return category;
	}

}
