package me.theditor.xelt.commands.voice.handlers;

import java.util.HashMap;
import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class RecordingHandler {
	
	private static HashMap<Guild, List<Byte>> recording = new HashMap<>();
	
	public static void record(Guild guild, List<Byte> bytes) {
		recording.put(guild, bytes);
	}
	
	/**
	 * Takes new bytes and adds them to the original byte list
	 * @param guild
	 * @param newBytes
	 */
	public static void updateBytes(Guild guild, List<Byte> newBytes) {
		recording.get(guild).addAll(newBytes);
	}
	
	public static boolean isRecording(VoiceChannel channel) {
		return isRecording(channel.getGuild());
	}
	
	public static boolean isRecording(Guild guild) {
		return recording.containsKey(guild);
	}
	
	public static List<Byte> stop(Guild guild) {
		return recording.remove(guild);
	}
}
