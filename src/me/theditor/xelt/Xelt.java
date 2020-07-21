package me.theditor.xelt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import me.theditor.xelt.commands.api.CommandManager;
import me.theditor.xelt.commands.music.handlers.AudioPlayerHandler;
import me.theditor.xelt.listeners.TicketListener;
import me.theditor.xelt.listeners.XeltListener;
import me.theditor.xelt.utils.MySQL;
import me.theditor.xelt.utils.RedditUtils;
import net.dean.jraw.http.UserAgent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;

public class Xelt {

	private String name;
	private String token;
	private String defaultPrefix;
	private String ownerID;
	private Logger logger;
	private String youtubeKey;
	private static Xelt instance;
	public static JDA jda;
	private MySQL mySQL;
	private String redditClientId;
	private String redditSecret;
	private UserAgent userAgent;
	
	private AudioPlayerManager playerManager;
	private AudioPlayerHandler audioPlayerHandler;

	private CommandManager commandManager;

	public Xelt() throws LoginException {
		setInstance(this);
		
		Config.setup();

		this.name = Config.get("BOT_NAME");
		this.token = Config.get("TOKEN");
		this.defaultPrefix = Config.get("DEFAULT_PREFIX");
		this.ownerID = Config.get("OWNER_ID");
		this.youtubeKey = Config.get("YOUTUBE_KEY");
		this.redditClientId = Config.get("REDDIT_CLIENT_ID");
		this.redditSecret = Config.get("REDDIT_SECRET");
		this.userAgent = new UserAgent("bot", "me.theditor.xelt", "v0.0.1", "theditor");
		
		this.logger = new Logger();

		logger.info("Booting JDA");

		jda = JDABuilder.createDefault(token).build();
		logger.info("JDA Starting");
		try {
			jda.awaitReady();
		} catch (InterruptedException e) {
			logger.fatal("JDA Start Interrupted!");
		}
		logger.info("JDA Started");

		setupMySQL();

		setupListeners();
		
		setupMusic();
		
		setupReddit();
	}
	
	private void setupReddit() {
		RedditUtils.setup();
	}
	
	private void setupMusic() {
		this.logger.info("Setting up music");
		playerManager  = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
		this.audioPlayerHandler = new AudioPlayerHandler();
		this.logger.info("Music setup complete");
		
	}

	private void setupListeners() {
		logger.info("Setting up Listeners");
		jda.addEventListener((commandManager = new CommandManager()), new XeltListener().ready(), new TicketListener());
		logger.info("Listeners Setup Complete");
	}

	private void setupMySQL() {
		mySQL = new MySQL();
		mySQL.connect(Config.get("DATABASE_HOST"), Config.get("DATABASE_USER"), Config.get("DATABASE_PASSWORD"),
				Config.get("DATABASE_NAME"), Config.get("DATABASE_PORT"));

		updateGuilds();
	}
	
	public void updateGuilds() {
		this.logger.info("Updating Guilds Table");
		this.logger.info("Adding New Guilds");
		for (Guild guild : jda.getGuilds()) {
			if (!mySQL.guildExists(guild.getId())) {
				try {
					PreparedStatement statement = mySQL.getConnection()
							.prepareStatement("INSERT INTO Guilds VALUES (?,?)");
					statement.setString(1, guild.getId());
					statement.setString(2, this.defaultPrefix);
					mySQL.update(statement);
					this.logger.info("New Guild Added - " + guild.getName() + " with ID - " + guild.getId());
				} catch (SQLException e) {
					this.logger.warn("An error occured while Adding Guilds");
					e.printStackTrace();
				}
			}
		}

		this.logger.info("Removing Left Guilds");
		try {
			PreparedStatement statement = mySQL.getConnection().prepareStatement("SELECT * FROM Guilds");
			ResultSet rs = mySQL.query(statement);
			while(rs.next()) {
				String id = rs.getString("ID");
				boolean remove = true;
				for(Guild guild : jda.getGuilds()) {
					if(guild.getId().equals(id)) {
						remove = false;
						break;
					}
				}
				if(remove) {
					PreparedStatement statement1 = mySQL.getConnection().prepareStatement("DELETE FROM Guilds WHERE ID=?");
					statement1.setString(1, id);
					mySQL.update(statement1);
					this.logger.info("Guild Removed with ID - " + id);
				}
			}
		} catch(SQLException e) {
			this.logger.warn("An error occured while Removing Guilds");
			e.printStackTrace();
		}
		this.logger.info("Updated Guilds Table");
	}
	
	public void shutdown() {
		this.logger.info("Shutting down");
		jda.shutdown();
		System.exit(0);
	}

	public static Xelt getInstance() {
		return instance;
	}

	public static void setInstance(Xelt instance) {
		Xelt.instance = instance;
	}

	public String getName() {
		return name;
	}

	public String getToken() {
		return token;
	}

	public String getDefaultPrefix() {
		return defaultPrefix;
	}

	public String getOwnerID() {
		return ownerID;
	}

	public Logger getLogger() {
		return logger;
	}

	public MySQL getMySQL() {
		return mySQL;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}
	
	public AudioPlayerManager getAudioPlayerManager() {
		return playerManager;
	}
	
	public AudioPlayerHandler getAudioPlayerHandler() {
		return audioPlayerHandler;
	}
	
	public String getYoutubeKey() {
		return youtubeKey;
	}

	public String getRedditClientId() {
		return redditClientId;
	}

	public String getRedditSecret() {
		return redditSecret;
	}
	
	public UserAgent getUserAgent() {
		return userAgent;
	}
}
