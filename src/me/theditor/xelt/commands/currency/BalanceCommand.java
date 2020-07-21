package me.theditor.xelt.commands.currency;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import me.theditor.xelt.Xelt;
import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class BalanceCommand extends BaseCommand {

	private CommandCategory category;

	public BalanceCommand(CommandCategory category) {
		this.category = category;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		try {
			PreparedStatement statement = Xelt.getInstance().getMySQL().getConnection()
					.prepareStatement("SELECT * FROM Currency WHERE ID=?");
			statement.setString(1, e.getAuthor().getId());
			ResultSet rs = Xelt.getInstance().getMySQL().query(statement);
			if(rs.next()) {
				long bal = rs.getLong("Balance");
				List<String> msgs = new ArrayList<>();
				msgs.add("Balance: " + bal);
				e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed(e.getMember().getEffectiveName() + "'s Account", msgs, e.getChannel()).build()).queue();
			} else {
				MessageUtils.errorMessage("You don't have an account on " + Xelt.getInstance().getName() + "!", e.getChannel());
			}
		} catch (Exception e1) {
			MessageUtils.errorMessage("An error occured while performing that action!", e.getChannel());
		}
	}

	@Override
	public String[] getAliases() {
		String[] aliases = { "bal" };
		return aliases;
	}

	@Override
	public String getName() {
		return "Balance";
	}

	@Override
	public String getDescription() {
		return "Check your balance!";
	}

	@Override
	public String getUsage(Guild guild) {
		return getName().toLowerCase();
	}

	@Override
	public CommandCategory getParentCategory() {
		return category;
	}

}
