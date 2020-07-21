package me.theditor.xelt.commands.music;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import me.theditor.xelt.Xelt;
import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.handlers.TracksHandler;
import me.theditor.xelt.utils.MessageUtils;
import me.theditor.xelt.utils.music.AudioPlayerSendHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand extends BaseCommand {
	
	private CommandCategory category;

	public JoinCommand(CommandCategory category) {
		this.category = category;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		if(e.getGuild().getAudioManager().isConnected()) {
			MessageUtils.errorMessage("Already connected to a voice channel!", e.getChannel());
			return;
		}
		
		if(e.getMember().getVoiceState().getChannel() == null) {
			MessageUtils.errorMessage("Join a voice channel before executing this command!", e.getChannel());
			return;
		}
		
		AudioManager manager = e.getGuild().getAudioManager();
		AudioPlayer player = Xelt.getInstance().getAudioPlayerManager().createPlayer();
		
		manager.setSendingHandler(new AudioPlayerSendHandler(player));
		Xelt.getInstance().getAudioPlayerHandler().registerPlayer(player, e.getGuild());
		player.addListener(new TracksHandler(player));
		
		manager.openAudioConnection(e.getMember().getVoiceState().getChannel());
		
		List<String> msgs = new ArrayList<>();
		msgs.add( Xelt.getInstance().getName() + " has joined voice channel " + e.getMember().getVoiceState().getChannel().getName());
		e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Joined Voice Channel", msgs, Color.green, e.getChannel()).build()).queue();
	}

	@Override
	public String getName() {
		return "Join";
	}

	@Override
	public String getUsage(Guild guild) {
		return getName().toLowerCase();
	}

	@Override
	public String getDescription() {
		return "Join a voice channel";
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
