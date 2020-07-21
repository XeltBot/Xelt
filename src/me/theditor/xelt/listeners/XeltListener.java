package me.theditor.xelt.listeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import me.theditor.xelt.Xelt;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class XeltListener extends ListenerAdapter{
	
	private Xelt xelt;
	private static Timer timer;
	private static HashMap<String, BiConsumer<MessageReaction, Member>> reactionListeners;
	private static HashMap<User, Consumer<Message>> messageListeners;
	
	public XeltListener() {
		xelt = Xelt.getInstance();
		timer = new Timer();
		reactionListeners = new HashMap<>();
		messageListeners = new HashMap<>();
	}
	
	@Override
	public void onGuildJoin(GuildJoinEvent e) {
		xelt.updateGuilds();
	}
	
	@Override
	public void onGuildLeave(GuildLeaveEvent e) {
		xelt.updateGuilds();
	}
	
	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent e) {
		if(reactionListeners.containsKey(e.getMessageId()))
			reactionListeners.get(e.getMessageId()).accept(e.getReaction(), e.getMember());
	}
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		if(messageListeners.containsKey(e.getAuthor())) {
			messageListeners.get(e.getAuthor()).accept(e.getMessage());
		}
	}
	
	public XeltListener ready() {
		xelt.getLogger().info("Setting up Activity");
		List<String> activities = new ArrayList<>();
		activities.add("Chillin'");
		activities.add("Serving " + Xelt.jda.getGuilds().size() + " Guilds");
		activities.add("Serving " + Xelt.jda.getGuilds().size() + " Guilds");
		activities.add("Upvoting Myself");
		activities.add("Playing Music");
		
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Collections.shuffle(activities);
				String activity = xelt.getDefaultPrefix() + "help | " + activities.get(0);
				Xelt.jda.getPresence().setPresence(Activity.watching(activity), false);
			}
		}, 0, 1000 * 10);
		xelt.getLogger().info("Activity Setup Complete");
		return this;
	}
	
	public static void addReactionListener(String messageId, BiConsumer<MessageReaction, Member> consumer) {
		reactionListeners.put(messageId, consumer);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				reactionListeners.remove(messageId);
			}
		}, 1000 * 10);
	}
	
	public static void removeReactionListener(String messageId) {
		reactionListeners.remove(messageId);
	}
	
	public static void addMessageListener(User user, Consumer<Message> callback, Runnable timeout) {
		messageListeners.put(user, callback);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(messageListeners.remove(user) != null)
					timeout.run();
			}
		}, 1000 * 10);
	}
	
	public static void removeMessageListener(User user) {
		messageListeners.remove(user);
	}

}
