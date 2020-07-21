package me.theditor.xelt.commands.currency;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import me.theditor.xelt.Xelt;
import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class BegCommand extends BaseCommand {

	private int COOLDOWN;
	private CommandCategory category;
	private HashMap<User, Integer> cooldown;
	private Timer timer;

	public BegCommand(CommandCategory category) {
		this.category = category;
		cooldown = new HashMap<>();
		timer = new Timer();
		COOLDOWN = 40;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		try {
			if (cooldown.containsKey(e.getAuthor())) {
				MessageUtils.errorMessage("You are on a cooldown! " + cooldown.get(e.getAuthor()) + "s left!",
						e.getChannel());
				return;
			}

			long bal = 0;

			PreparedStatement statement = Xelt.getInstance().getMySQL().getConnection()
					.prepareStatement("SELECT * FROM Currency WHERE ID=?");
			statement.setString(1, e.getAuthor().getId());
			ResultSet rs = Xelt.getInstance().getMySQL().query(statement);
			if (rs.next()) {
				bal = rs.getLong("Balance");
			} else {
				MessageUtils.errorMessage("You don't have an account on " + Xelt.getInstance().getName() + "!",
						e.getChannel());
				return;
			}

			List<String> people = new ArrayList<>();
			people.add("Howard Stern");
			people.add("Courteney Cox");
			people.add("Ashton Kutcher");
			people.add("Leonardo DiCaprio");
			people.add("Elon Musk");
			people.add("Tom Cruise");
			people.add("Someone");
			people.add("Alien");
			people.add("Poor Man");

			Collections.shuffle(people);

			int donated = (new Random()).nextInt(500);
			String side = "";
			if (donated < 50) {
				side = " Better luck next time!";
			} else if (donated < 200) {
				side = " You lucky duck";
			} else if (donated < 400) {
				side = " Such a generous being!";
			} else if (donated < 501) {
				side = " You hit the jackpot!";
			}

			PreparedStatement statement1 = Xelt.getInstance().getMySQL().getConnection()
					.prepareStatement("UPDATE Currency SET Balance=? WHERE ID=?");
			statement1.setLong(1, bal + donated);
			statement1.setString(2, e.getAuthor().getId());
			Xelt.getInstance().getMySQL().update(statement1);

			List<String> msgs = new ArrayList<>();
			msgs.add(people.get(0) + " donated " + donated + "!" + side);

			e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed(e.getMember().getEffectiveName() + " Begged", msgs, e.getChannel()).build()).queue();

			cooldown.putIfAbsent(e.getAuthor(), COOLDOWN);
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					int val = cooldown.get(e.getAuthor());
					cooldown.put(e.getAuthor(), val - 2);
					if (cooldown.get(e.getAuthor()) <= 0) {
						cooldown.remove(e.getAuthor());
						this.cancel();
					}
				}
			}, 2, 1000 * 2);
		} catch (Exception e1) {
			MessageUtils.errorMessage("An error occured while performing that action!", e.getChannel());
		}
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public String getName() {
		return "Beg";
	}

	@Override
	public String getDescription() {
		return "Beg to get some cash!";
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
