package me.theditor.xelt.handlers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.theditor.xelt.Xelt;
import net.dv8tion.jda.api.entities.Guild;

public class GuildHandler {
	
	private static Xelt xelt = Xelt.getInstance();
	
	public static String getGuildPrefix(Guild guild) {
		String prefix = xelt.getDefaultPrefix();

		try {
			PreparedStatement statement = xelt.getMySQL().getConnection().prepareStatement("SELECT * FROM Guilds WHERE ID=?");
			statement.setString(1, guild.getId());
			ResultSet rs = xelt.getMySQL().query(statement);
			if (rs.next()) {
				String tempPrefix = rs.getString("Prefix");
				prefix = tempPrefix == null || tempPrefix.equals("") ? prefix : tempPrefix;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return prefix;
	}
	

}
