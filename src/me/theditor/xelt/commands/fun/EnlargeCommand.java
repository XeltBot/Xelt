package me.theditor.xelt.commands.fun;

import java.util.ArrayList;
import java.util.List;

import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.utils.MessageUtils;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class EnlargeCommand extends BaseCommand{
	
	private CommandCategory category;

	public EnlargeCommand(CommandCategory category) {
		this.category = category;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		List<Emote> emote = e.getMessage().getEmotes();
		if (args.size() < 1 || emote.size() < 1) {
			MessageUtils.errorMessage("Invalid command usage!", e.getChannel());
			return;
		}

		List<String> msgs = new ArrayList<>();
		msgs.add("");
		e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed("Enlarged Emote", msgs, e.getChannel()).setImage(emote.get(0).getImageUrl()).build()).queue();
	}

	@Override
	public String[] getAliases() {
		String[] aliases = {"bigemote","bigemoji"};
		return aliases;
	}

	@Override
	public String getName() {
		return "Enlarge";
	}

	@Override
	public String getDescription() {
		return "Enlarge an emote!";
	}

	@Override
	public String getUsage(Guild guild) {
		return getName().toLowerCase() + " <server emote>";
	}

	@Override
	public CommandCategory getParentCategory() {
		return category;
	}

}
