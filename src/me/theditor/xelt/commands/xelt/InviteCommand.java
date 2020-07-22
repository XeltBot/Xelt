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

public class InviteCommand extends BaseCommand{
	private CommandCategory category;

	public InviteCommand(CommandCategory category) {
		this.category = category;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		ArrayList<String> msgs = new ArrayList<String>();
		msgs.add("You can invite " + Xelt.getInstance().getName() + " here -");
		msgs.add(MarkdownUtil.maskedLink("Link", "https://discord.com/api/oauth2/authorize?client_id=726763157195849728&permissions=8&scope=bot"));
		e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Invite " + Xelt.getInstance().getName(), msgs, e.getChannel()).build()).queue();
	}

	@Override
	public String getName() {
		return "Invite";
	}

	@Override
	public String getUsage(Guild guild) {
		return this.getName().toLowerCase();
	}

	@Override
	public String getDescription() {
		return "Get the invite link to this bot!";
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
