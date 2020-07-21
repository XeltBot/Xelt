package me.theditor.xelt.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.entities.TextChannel;

public class TracksHandler extends AudioEventAdapter {

	public AudioPlayer player;
	private TextChannel defaultChannel;
	private static List<TracksHandler> listeners = new ArrayList<>();
	private final BlockingQueue<AudioTrack> queue;

	public TracksHandler(AudioPlayer player) {
		this.player = player;
		TracksHandler.listeners.add(this);
		this.queue = new LinkedBlockingQueue<>();
	}

	public void queue(AudioTrack track, TextChannel channel) {
		if (!player.startTrack(track, true)) {
			queue.offer(track);
		}
	}

	public void nextTrack(TextChannel channel) {
		if (player.startTrack(queue.poll(), false)) {
			List<String> msgs = new ArrayList<>();
			msgs.add("Playing next track! " + ((queue.size()==0) ? "No" : queue.size()) + " tracks to go!");
			channel.sendMessage(MessageUtils.noFieldMessageEmbed("Next Track", msgs, channel).build()).queue();
		}
	}
	
	public void nextTrack() {
		if (player.startTrack(queue.poll(), false) && defaultChannel != null) {
			List<String> msgs = new ArrayList<>();
			msgs.add("Playing next track! " + ((queue.size()==0) ? "No" : queue.size()) + " tracks to go!");
			defaultChannel.sendMessage(MessageUtils.noFieldMessageEmbed("Next Track", msgs, defaultChannel).build()).queue();
		}
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (endReason.mayStartNext) {
			nextTrack();
		}
	}
	
	public AudioTrack getNextTrack() {
		return queue.peek();
	}

	public static TracksHandler getTrackListener(AudioPlayer player) {
		for (TracksHandler listener : TracksHandler.listeners) {
			if (listener.player.equals(player)) {
				return listener;
			}
		}
		return null;
	}

}
