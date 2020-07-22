package me.theditor.xelt.commands.adminonly;

import java.util.ArrayList;
import java.util.List;

import com.besaba.revonline.pastebinapi.paste.PasteExpire;

import me.theditor.xelt.Xelt;
import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.utils.MessageUtils;
import me.theditor.xelt.utils.PastebinUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

public class ListGuildsCommand extends BaseCommand {
	
private CommandCategory category;
	
	public ListGuildsCommand(CommandCategory category) {
		this.category = category;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		String raw = "";
		for(Guild guild : Xelt.jda.getGuilds()) {
			raw = raw + guild.getName() + " | " + guild.getId() + "\n";
		}
		
		String link = PastebinUtils.newPaste("All Guilds of " + Xelt.getInstance().getName(), raw, PasteExpire.TenMinutes);

		List<String> msgs = new ArrayList<>();
		msgs.add("You can find all the guilds that " + Xelt.getInstance().getName() + MarkdownUtil.maskedLink(" here", link));
		msgs.add(MarkdownUtil.bold("Note: ") + " This link will expire in 10 minutes!");
		e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Guilds", msgs, e.getChannel()).build()).queue();
	}

	@Override
	public String getName() {
		return "ListGuilds";
	}

	@Override
	public String getUsage(Guild guild) {
		return getName().toLowerCase();
	}

	@Override
	public String getDescription() {
		return "Lists all guilds the bot is in (Admin only)";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public CommandCategory getParentCategory() {
		return this.category;
	}

}
