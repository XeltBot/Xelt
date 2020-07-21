package me.theditor.xelt.commands.voice;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;

import com.cloudburst.lame.lowlevel.LameEncoder;
import com.cloudburst.lame.mp3.Lame;
import com.cloudburst.lame.mp3.MPEGMode;
import com.google.common.primitives.Bytes;

import me.theditor.xelt.Xelt;
import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.commands.voice.handlers.RecordingHandler;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class StopCommand extends BaseCommand {

	private CommandCategory category;

	public StopCommand(CommandCategory category) {
		this.category = category;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		if (!e.getGuild().getAudioManager().isConnected()) {
			MessageUtils.errorMessage(Xelt.getInstance().getName() + " is not connected to any voice channel!",
					e.getChannel());
			return;
		}

		if (e.getMember().getVoiceState().getChannel() == null) {
			MessageUtils.errorMessage("Join the same voice channel as " + Xelt.getInstance().getName()
					+ " before executing this command!", e.getChannel());
			return;
		}

		if (!e.getGuild().getAudioManager().getConnectedChannel().equals(e.getMember().getVoiceState().getChannel())) {
			MessageUtils.errorMessage("Join the same voice channel as " + Xelt.getInstance().getName()
					+ " before executing this command!", e.getChannel());
			return;
		}
		
		if(!RecordingHandler.isRecording(e.getGuild())) {
			MessageUtils.errorMessage("No recording are going on in this guild!", e.getChannel());
			return;
		}
		
		List<Byte> oldBytes = RecordingHandler.stop(e.getGuild());
		e.getGuild().getAudioManager().closeAudioConnection();
		try {
			byte[] data = encodePcmToMp3(Bytes.toArray(oldBytes));
			List<String> msgs = new ArrayList<>();
			if(getFileSizeMegaBytes(data) > 8.0) {
				msgs.add("The output file size is bigger than 8 MBs (Discord limit)");
				msgs.add("And therefore, cannot be sent!");
				msgs.add("Premium coming soon with recordings greater than 8 MBs");
				e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Unable to Send Recording", msgs, e.getChannel()).build()).queue();
				return;
			}
			msgs.add("A new audio recording has finished for " + e.getGuild().getId());
			msgs.add("A file will shortly be sent to this channel");
			e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Recording Created", msgs, e.getChannel()).build()).queue(message -> {
				e.getChannel().sendFile(data, "unknown.mp3").queue();
			});
		} catch (Exception e1) {
			MessageUtils.errorMessage("An error occured while saving to file!", e.getChannel());
			return;
		}
	}
	
	private double getFileSizeMegaBytes(byte[] bytes) {
		return (double) bytes.length / (1024 * 1024);
	}

	public byte[] encodePcmToMp3(byte[] pcm) throws Exception {
		LameEncoder encoder = new LameEncoder(new AudioFormat(48000.0f, 16, 2, true, true), 128,
				MPEGMode.STEREO, Lame.QUALITY_HIGHEST, false);

		ByteArrayOutputStream mp3 = new ByteArrayOutputStream();
		byte[] buffer = new byte[encoder.getPCMBufferSize()];

		int bytesToTransfer = Math.min(buffer.length, pcm.length);
		int bytesWritten;
		int currentPcmPosition = 0;

		while (0 < (bytesWritten = encoder.encodeBuffer(pcm, currentPcmPosition, bytesToTransfer, buffer))) {
			currentPcmPosition += bytesToTransfer;
			bytesToTransfer = Math.min(buffer.length, pcm.length - currentPcmPosition);
			mp3.write(buffer, 0, bytesWritten);
		}
		encoder.close();
		
		return mp3.toByteArray();
	}

	@Override
	public String getName() {
		return "Stop";
	}

	@Override
	public String getUsage(Guild guild) {
		return getName().toLowerCase();
	}

	@Override
	public String getDescription() {
		return "Stop recording a voice channel!";
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
