package me.theditor.xelt.commands.miscellaneous;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ServerInfoCommand extends BaseCommand {

	private CommandCategory category;

	public ServerInfoCommand(CommandCategory category) {
		this.category = category;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		SimpleDateFormat format = new SimpleDateFormat("E, MMMM d, yyyy HH:mm a");

		List<Role> roles = e.getGuild().getRoles();

		EmbedBuilder embed = MessageUtils.noFieldMessageEmbed(null, new ArrayList<>(), e.getChannel());

		Member owner = e.getGuild().retrieveOwner().complete();

		if (owner != null) {
			embed.addField("Owner", owner.getUser().getAsTag(), true);
		}
		embed.addField("Region", e.getGuild().getRegionRaw(), true);
		embed.addField("Channel Categories", "" + e.getGuild().getCategories().size(), true);
		embed.addField("Text Channels", "" + e.getGuild().getTextChannels().size(), true);
		embed.addField("Voice Channels", "" + e.getGuild().getVoiceChannels().size(), true);
		embed.addField("Members", "" + e.getGuild().getMembers().size(), true);

		String title = "Roles [" + roles.size() + "]";
		if (roles.size() > 0) {
			String msg = roles.get(0).getAsMention();
			for (int i = 1; i < roles.size(); i++) {
				msg = msg + " " + roles.get(i).getAsMention();
			}
			embed.addField(title, msg, false);
		}

		embed.setAuthor(e.getGuild().getName(), null, e.getGuild().getIconUrl());
		embed.setDescription("ID: " + e.getGuild().getId() + " | Created: "
				+ format.format(Date.from(e.getGuild().getTimeCreated().toInstant())));
		e.getChannel().sendMessage(embed.build()).queue();
	}

	@Override
	public String getName() {
		return "ServerInfo";
	}

	@Override
	public String getUsage(Guild guild) {
		return this.getName().toLowerCase();
	}

	@Override
	public String getDescription() {
		return "Get server information!";
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
