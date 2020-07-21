package me.theditor.xelt.commands.music.handlers;

import java.util.HashMap;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import net.dv8tion.jda.api.entities.Guild;

public class AudioPlayerHandler {
	
	private HashMap<Guild, AudioPlayer> audioPlayers;
	
	public AudioPlayerHandler() {
		this.audioPlayers = new HashMap<Guild, AudioPlayer>();
	}
	
	public AudioPlayer registerPlayer(AudioPlayer player, Guild guild) {
		this.audioPlayers.put(guild, player);
		return player;
	}
	
	public AudioPlayer unregisterPlayer(Guild guild) {
		return this.audioPlayers.remove(guild);
	}
	
	public AudioPlayer getAudioPlayer(Guild guild) {
		return audioPlayers.get(guild);
	}
	
	@Deprecated
	public AudioPlayer unregisterPlayer(AudioPlayer player) {
		for(Guild guild : audioPlayers.keySet()) {
			if(audioPlayers.get(guild).equals(player)) {
				return this.audioPlayers.remove(guild);
			}
		}
		return null;
	}

}
