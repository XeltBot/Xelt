package me.theditor.xelt.commands.xelt;

import java.util.ArrayList;
import java.util.List;

import me.theditor.xelt.Xelt;
import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

public class UpvoteCommand extends BaseCommand{
	
	private CommandCategory category;

	public UpvoteCommand(CommandCategory category) {
		this.category = category;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		List<String> msgs = new ArrayList<>();
		msgs.add("You can upvote for " + Xelt.getInstance().getName() + " on the following links -");
		msgs.add(MarkdownUtil.maskedLink("Top.gg", "https://top.gg/bot/726763157195849728"));
		msgs.add(MarkdownUtil.maskedLink("Discord Bot List","https://discordbotlist.com/bots/xelt"));
		e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Upvote", msgs, e.getChannel()).build()).queue();
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public String getName() {
		return "Upvote";
	}

	@Override
	public String getDescription() {
		return "Upvote for " + Xelt.getInstance().getName();
	}

	@Override
	public String getUsage(Guild guild) {
		return getName().toLowerCase() + " [command]";
	}

	@Override
	public CommandCategory getParentCategory() {
		return category;
	}

}
