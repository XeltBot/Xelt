package me.theditor.xelt.commands.music;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import me.theditor.xelt.Xelt;
import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ResumeCommand extends BaseCommand {
	
	private CommandCategory category;

	public ResumeCommand(CommandCategory category) {
		this.category = category;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		if (!e.getGuild().getAudioManager().isConnected()) {
			MessageUtils.errorMessage(Xelt.getInstance().getName() + " is not connected to any voice channel!",
					e.getChannel());
			return;
		}

		if (e.getMember().getVoiceState().getChannel() == null ) {
			MessageUtils.errorMessage("Join the same voice channel as " + Xelt.getInstance().getName()
					+ " before executing this command!", e.getChannel());
			return;
		}

		if (!e.getGuild().getAudioManager().getConnectedChannel().equals(e.getMember().getVoiceState().getChannel())) {
			MessageUtils.errorMessage("Join the same voice channel as " + Xelt.getInstance().getName()
					+ " before executing this command!", e.getChannel());
			return;
		}
		
		this.resume(e.getChannel(), e.getGuild());

	}

	private void resume(TextChannel channel, Guild guild) {
		AudioPlayer player = Xelt.getInstance().getAudioPlayerHandler().getAudioPlayer(guild);
		
		if(player.getPlayingTrack() == null) {
			MessageUtils.errorMessage(Xelt.getInstance().getName() + " is not playing any tracks!", channel);
			return;
		}
		
		if (!player.isPaused()) {
			MessageUtils.errorMessage("The current track is not paused!", channel);
			return;
		}
		
		player.setPaused(false);
		
		List<String> msgs = new ArrayList<>();
		msgs.add("Resumed current track!");

		channel.sendMessage(MessageUtils.noFieldMessageEmbed("Track Resumed", msgs, Color.green, channel).build()).queue();
	}
	
	@Override
	public String getName() {
		return "Resume";
	}

	@Override
	public String getUsage(Guild guild) {
		return getName().toLowerCase();
	}

	@Override
	public String getDescription() {
		return "Resume the current track";
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
