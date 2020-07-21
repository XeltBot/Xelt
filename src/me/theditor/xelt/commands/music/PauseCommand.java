package me.theditor.xelt.commands.music;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import me.theditor.xelt.Xelt;
import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PauseCommand extends BaseCommand {
	
	private CommandCategory category;

	public PauseCommand(CommandCategory category) {
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
		
		VoiceChannel chan = e.getMember().getVoiceState().getChannel();
		
		int size = 0;
		for(Member member : chan.getMembers()) {
			if(!member.getUser().isBot()) {
				size++;
			}
		}
		
		if (size > 2) {
			List<String> msgs = new ArrayList<String>();
			msgs.add(e.getMember().getAsMention() + " has requested to pause the current track!");
			msgs.add("React with \u2705 in 5 seconds to pause!");
			Message message = e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Pause Request", msgs, e.getChannel()).build()).complete();
			message.addReaction("U+2705").complete();
			List<User> users = message.retrieveReactionUsers("✅").completeAfter(5, TimeUnit.SECONDS);
			int i = 0;
			for(User user : users) {
				if(user.isBot())
					continue;
				Member member = e.getGuild().getMember(user);
				if(member != null && chan.getMembers().contains(member)) {
					i++;
				}
			}
			if(i >= (chan.getMembers().size() - 2)) {
				this.pause(e.getChannel(), e.getGuild());
			}
			message.delete().queue();
			return;
		}
		
		this.pause(e.getChannel(), e.getGuild());

	}

	private void pause(TextChannel channel, Guild guild) {
		AudioPlayer player = Xelt.getInstance().getAudioPlayerHandler().getAudioPlayer(guild);

		if (player.getPlayingTrack() == null) {
			MessageUtils.errorMessage(Xelt.getInstance().getName() + " is not playing any tracks!", channel);
			return;
		}
		
		if (player.isPaused()) {
			MessageUtils.errorMessage("The current track is already paused!", channel);
			return;
		}
		
		player.setPaused(true);
		
		List<String> msgs = new ArrayList<>();
		msgs.add("Paused current track!");

		channel.sendMessage(MessageUtils.noFieldMessageEmbed("Track Paused", msgs, Color.green, channel).build()).queue();
	}
	
	@Override
	public String getName() {
		return "Pause";
	}

	@Override
	public String getUsage(Guild guild) {
		return getName().toLowerCase();
	}

	@Override
	public String getDescription() {
		return "Pause the current track";
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
