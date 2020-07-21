package me.theditor.xelt.commands.voice;

import java.util.ArrayList;
import java.util.List;

import com.google.common.primitives.Bytes;

import me.theditor.xelt.Xelt;
import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.commands.voice.handlers.RecordingHandler;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.CombinedAudio;
import net.dv8tion.jda.api.audio.UserAudio;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class RecordCommand extends BaseCommand {

	private CommandCategory category;

	public RecordCommand(CommandCategory category) {
		this.category = category;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		boolean joinedNow = false;

		if (!e.getGuild().getAudioManager().isConnected()) {
			if (e.getMember().getVoiceState().getChannel() == null) {
				MessageUtils.errorMessage("Join a voice channel before executing this command!", e.getChannel());
				return;
			}

			Xelt.getInstance().getCommandManager().getCommand("join").run(e, args);
			joinedNow = true;
		}

		if (e.getMember().getVoiceState().getChannel() == null) {
			MessageUtils.errorMessage("Join the same voice channel as " + Xelt.getInstance().getName()
					+ " before executing this command!", e.getChannel());
			return;
		}

		if (!joinedNow) {
			if (!e.getGuild().getAudioManager().getConnectedChannel()
					.equals(e.getMember().getVoiceState().getChannel())) {
				MessageUtils.errorMessage("Join the same voice channel as " + Xelt.getInstance().getName()
						+ " before executing this command!", e.getChannel());
				return;
			}
		}

		if (RecordingHandler.isRecording(e.getGuild())) {
			MessageUtils.errorMessage("Another recording is already taking place in "
					+ e.getGuild().getAudioManager().getConnectedChannel().getName(), e.getChannel());
			return;
		}
		
		RecordingHandler.record(e.getGuild(), new ArrayList<>());

		e.getGuild().getAudioManager().setReceivingHandler(new AudioReceiveHandler() {
			@Override
			public boolean canReceiveCombined() {
				return true;
			}

			@Override
			public boolean canReceiveUser() {
				return false;
			}

			@Override
			public void handleCombinedAudio(CombinedAudio combinedAudio) {
				if (combinedAudio.getUsers().size() > 0 && RecordingHandler.isRecording(e.getGuild())) {
					try {
						RecordingHandler.updateBytes(e.getGuild(), Bytes.asList(combinedAudio.getAudioData(1.0)));
					} catch (Exception e1) {
						e.getGuild().getAudioManager().closeAudioConnection();
						MessageUtils.errorMessage("An error occured while saving audio!", e.getChannel());
					}
				}
			}

			@Override
			public void handleUserAudio(UserAudio userAudio) {

			}
		});

	}

	@Override
	public String getName() {
		return "Record";
	}

	@Override
	public String getUsage(Guild guild) {
		return getName().toLowerCase();
	}

	@Override
	public String getDescription() {
		return "Record a voice channel!";
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
