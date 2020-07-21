package me.theditor.xelt.commands.music;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import me.theditor.xelt.Xelt;
import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.handlers.TracksHandler;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

public class NowPlayingCommand extends BaseCommand{

	private CommandCategory category;

	public NowPlayingCommand(CommandCategory category) {
		this.category = category;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		if(!e.getGuild().getAudioManager().isConnected()) {
			MessageUtils.errorMessage(Xelt.getInstance().getName() + " is not connected to any voice channel!", e.getChannel());
			return;
		}
		
		if(e.getMember().getVoiceState().getChannel() == null) {
			MessageUtils.errorMessage("Join the same voice channel as " + Xelt.getInstance().getName() + " before executing this command!", e.getChannel());
			return;
		}
		if(!e.getGuild().getAudioManager().getConnectedChannel().equals(e.getMember().getVoiceState().getChannel())) {
			MessageUtils.errorMessage("Join the same voice channel as " + Xelt.getInstance().getName() + " before executing this command!", e.getChannel());
			return;
		}
		
		AudioPlayer player = Xelt.getInstance().getAudioPlayerHandler().getAudioPlayer(e.getGuild());
		AudioTrack track = player.getPlayingTrack();
		AudioTrack next = TracksHandler.getTrackListener(player).getNextTrack();
		if(track == null) {
			List<String> msgs = new ArrayList<>();
			msgs.add("Not playing any tracks!");
			e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Now Playing", msgs, e.getChannel()).build()).queue();
			return;
		}
		
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		
		Date position = new Date(track.getPosition());
		Date duration = new Date(track.getDuration());
		
		List<String> msgs = new ArrayList<>();
		msgs.add(MarkdownUtil.maskedLink(track.getInfo().title, track.getInfo().uri));
		msgs.add("By " + track.getInfo().author);
		msgs.add(format.format(position) + "/" + format.format(duration));
		if(next != null)
			msgs.add("Up next - " + MarkdownUtil.maskedLink(next.getInfo().title, next.getInfo().uri));
		
		e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Now Playing", msgs, e.getChannel()).build()).queue();
	}

	@Override
	public String getName() {
		return "NowPlaying";
	}

	@Override
	public String getUsage(Guild guild) {
		return getName().toLowerCase();
	}

	@Override
	public String getDescription() {
		return "View the current playing track";
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
