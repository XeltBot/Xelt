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

public class DonateCommand extends BaseCommand {
	
	private CommandCategory category;

	public DonateCommand(CommandCategory category) {
		this.category = category;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		List<String> msgs = new ArrayList<>();
		msgs.add("You can donate for " + Xelt.getInstance().getName() + "'s development " + MarkdownUtil.maskedLink("here", "https://www.paypal.me/akash8338"));
		msgs.add(MarkdownUtil.bold("Note: ") + "Donations cannot be refunded and can only be done through Paypal");
		msgs.add("For any kind of help with donations contact ThEditor#4626");
		e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Donation", msgs, e.getChannel()).build()).queue();
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public String getName() {
		return "Donate";
	}

	@Override
	public String getDescription() {
		return "Donate for " + Xelt.getInstance().getName();
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
