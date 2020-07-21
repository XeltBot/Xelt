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

public class CreateCommand extends BaseCommand {
	
	private CommandCategory category;

	public CreateCommand(CommandCategory category) {
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
				MessageUtils.errorMessage("You already have an account on " + Xelt.getInstance().getName() + "!", e.getChannel());
			} else {
				PreparedStatement statement1 = Xelt.getInstance().getMySQL().getConnection()
						.prepareStatement("INSERT INTO Currency VALUES (?,?)");
				statement1.setString(1, e.getAuthor().getId());
				statement1.setLong(2, 0);
				Xelt.getInstance().getMySQL().update(statement1);
				List<String> msgs = new ArrayList<>();
				msgs.add("Balance: " + 0);
				e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed(e.getMember().getEffectiveName() + "'s Account Created", msgs, e.getChannel()).build()).queue();
			}
		} catch (Exception e1) {
			MessageUtils.errorMessage("An error occured while performing that action!", e.getChannel());
		}
	}

	@Override
	public String[] getAliases() {
		String[] aliases = { "createAccount", "register" };
		return aliases;
	}

	@Override
	public String getName() {
		return "Create";
	}

	@Override
	public String getDescription() {
		return "Create an account on " + Xelt.getInstance().getName()  + "!";
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
