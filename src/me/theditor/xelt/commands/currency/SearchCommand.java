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
import me.theditor.xelt.listeners.XeltListener;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class SearchCommand extends BaseCommand {

	private int COOLDOWN;
	private CommandCategory category;
	private HashMap<User, Integer> cooldown;
	private Timer timer;

	public SearchCommand(CommandCategory category) {
		this.category = category;
		cooldown = new HashMap<>();
		timer = new Timer();
		COOLDOWN = 40;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		if (cooldown.containsKey(e.getAuthor())) {
			MessageUtils.errorMessage("You are on a cooldown! " + cooldown.get(e.getAuthor()) + "s left!",
					e.getChannel());
			return;
		}

		long val = 0;

		try {
			PreparedStatement statement = Xelt.getInstance().getMySQL().getConnection()
					.prepareStatement("SELECT * FROM Currency WHERE ID=?");
			statement.setString(1, e.getAuthor().getId());
			ResultSet rs = Xelt.getInstance().getMySQL().query(statement);
			if (rs.next()) {
				val = rs.getLong("Balance");
			} else {
				MessageUtils.errorMessage("You don't have an account on " + Xelt.getInstance().getName() + "!",
						e.getChannel());
				return;
			}
		} catch (Exception e1) {
			MessageUtils.errorMessage("An error occured while performing that action!", e.getChannel());
			return;
		}
		
		final long bal = val;

		List<String> places = new ArrayList<>();
		places.add("Grass");
		places.add("Couch");
		places.add("Street");
		places.add("Car");
		places.add("Purse");
		places.add("Laundromat");
		places.add("Bed");
		places.add("Discord");
		places.add("Coat");
		places.add("Pocket");

		Collections.shuffle(places);

		List<String> selectable = new ArrayList<>();
		List<String> msgs = new ArrayList<>();
		selectable.add(places.get(0).toLowerCase());
		selectable.add(places.get(1).toLowerCase());
		selectable.add(places.get(2).toLowerCase());
		for (String selected : selectable) {
			msgs.add("`" + selected + "`");
		}

		e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Searchable Areas", msgs, e.getChannel()).build())
				.queue(message -> {
					XeltListener.addMessageListener(e.getAuthor(), msg -> {
						XeltListener.removeMessageListener(e.getAuthor());
						if (!selectable.contains(msg.getContentRaw().toLowerCase())) {
							MessageUtils.errorMessage(
									"Invalid place specified!\nTry that again but this time select an actual listed place.",
									e.getChannel());
							return;
						}
						
						int donated = (new Random()).nextInt(200);
						
						try {
							PreparedStatement statement1 = Xelt.getInstance().getMySQL().getConnection()
									.prepareStatement("UPDATE Currency SET Balance=? WHERE ID=?");
							statement1.setLong(1, bal + donated);
							statement1.setString(2, e.getAuthor().getId());
							Xelt.getInstance().getMySQL().update(statement1);
						} catch (Exception e1) {
							MessageUtils.errorMessage("An error occured while performing that action!", e.getChannel());
							return;
						}
						msgs.clear();
						msgs.add("You found " + donated + " on/in the "
								+ MessageUtils.capitalize(msg.getContentRaw()) + "!");

						e.getChannel().sendMessage(MessageUtils
								.noFieldMessageEmbed(e.getMember().getEffectiveName() + " Searched", msgs, e.getChannel())
								.build()).queue();

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
					}, () -> {
						msgs.clear();
						msgs.add("You took too long to respond!");
						msgs.add("Rude.");
						e.getChannel()
								.sendMessage(MessageUtils.noFieldMessageEmbed("Timeout", msgs, e.getChannel()).build())
								.queue();
					});
				});
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public String getName() {
		return "Search";
	}

	@Override
	public String getDescription() {
		return "Search places to get some cash!";
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
