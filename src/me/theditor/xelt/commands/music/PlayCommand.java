package me.theditor.xelt.commands.music;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import me.theditor.xelt.Xelt;
import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.handlers.TracksHandler;
import me.theditor.xelt.listeners.XeltListener;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PlayCommand extends BaseCommand {

	private CommandCategory category;
	private YouTube youtube;

	public PlayCommand(CommandCategory category) {
		this.category = category;

		YouTube temp = null;

		try {
			temp = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(),
					JacksonFactory.getDefaultInstance(), null).setApplicationName("Xelt").build();
		} catch (Exception e) {
			e.printStackTrace();
		}

		youtube = temp;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		if (args.size() < 1) {
			MessageUtils.errorMessage("Invalid command usage!", e.getChannel());
			return;
		}
		
		String input = args.get(0);
		for(int i=1; i < args.size(); i++) {
			input = input + " " + args.get(i);
		}

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
		
		if(!isUrl(input)) {
			List<SearchResult> results = searchYoutube(input);
			List<String> msgs = new ArrayList<>();
			for(int i=0; i < results.size(); i++) {
				msgs.add((i+1) + ". " + results.get(i).getSnippet().getTitle());
			}
			Message message = e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Select Track", msgs, e.getChannel()).build()).complete();
			for(int i=1; i <= results.size(); i++) {
				message.addReaction(i+ "\uFE0F\u20E3").complete();
			}
			
			XeltListener.addReactionListener(message.getId(), (reaction, member) -> {
				if(member.getId().equals(e.getMember().getId())) {
					XeltListener.removeReactionListener(message.getId());
					int selection = 0;
					try {
						selection = Integer.parseInt(reaction.getReactionEmote().getEmoji().substring(0,1));
					} catch (Exception e1) {
					}
					message.delete().queue();
					if(selection != 0) {
						AudioPlayer player = Xelt.getInstance().getAudioPlayerHandler().getAudioPlayer(e.getGuild());
						TracksHandler listener = TracksHandler.getTrackListener(player);
						
						Xelt.getInstance().getAudioPlayerManager().loadItem(results.get(selection-1).getId().getVideoId(), new AudioLoadResultHandler() {
							@Override
							public void trackLoaded(AudioTrack track) {
								if (listener == null) {
									return;
								}
								listener.queue(track, e.getChannel());
								List<String> msgs = new ArrayList<>();
								msgs.add("Requested track has been added to the queue!");
								e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Added To Queue", msgs, Color.green, e.getChannel()).build()).queue();
							}

							@Override
							public void playlistLoaded(AudioPlaylist playlist) {
								if (listener == null) {
									return;
								}
								for (AudioTrack track : playlist.getTracks()) {
									listener.queue(track, e.getChannel());
								}
								List<String> msgs = new ArrayList<>();
								msgs.add("Requested playlist has been added to the queue!");
								e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Added To Queue", msgs, Color.green, e.getChannel()).build()).queue();
							}

							@Override
							public void noMatches() {
								MessageUtils.errorMessage("No match found for the requested track!", e.getChannel());
							}

							@Override
							public void loadFailed(FriendlyException throwable) {
								Xelt.getInstance().getLogger().warn("Failed to load a track in guild " + e.getGuild());
							}
						});
					} else {
						MessageUtils.errorMessage("Invalid track selected", e.getChannel());
					}
				}
			});
			return;
		}

		AudioPlayer player = Xelt.getInstance().getAudioPlayerHandler().getAudioPlayer(e.getGuild());
		TracksHandler listener = TracksHandler.getTrackListener(player);

		Xelt.getInstance().getAudioPlayerManager().loadItem(input, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				if (listener == null) {
					return;
				}
				listener.queue(track, e.getChannel());
				List<String> msgs = new ArrayList<>();
				msgs.add("Requested track has been added to the queue!");
				e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Added To Queue", msgs, Color.green, e.getChannel()).build()).queue();
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				if (listener == null) {
					return;
				}
				for (AudioTrack track : playlist.getTracks()) {
					listener.queue(track, e.getChannel());
				}
				List<String> msgs = new ArrayList<>();
				msgs.add("Requested playlist has been added to the queue!");
				e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Added To Queue", msgs, Color.green, e.getChannel()).build()).queue();
			}

			@Override
			public void noMatches() {
				MessageUtils.errorMessage("No match found for the requested track!", e.getChannel());
			}

			@Override
			public void loadFailed(FriendlyException throwable) {
				Xelt.getInstance().getLogger().warn("Failed to load a track in guild " + e.getGuild());
			}
		});

	}

	private boolean isUrl(String input) {
		try {
			new URL(input);

			return true;
		} catch (MalformedURLException ignored) {
			return false;
		}
	}
	
	private List<SearchResult> searchYoutube(String input) {
        try {
            List<SearchResult> results = youtube.search()
                    .list("id,snippet")
                    .setQ(input)
                    .setMaxResults(5L)
                    .setType("video")
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey(Xelt.getInstance().getYoutubeKey())
                    .execute()
                    .getItems();

            if (!results.isEmpty()) {
                return results;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

	@Override
	public String getName() {
		return "Play";
	}

	@Override
	public String getUsage(Guild guild) {
		return getName().toLowerCase() + " <url/keywords>";
	}

	@Override
	public String getDescription() {
		return "Play a track";
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
