package me.theditor.xelt.commands.fun;

import java.util.ArrayList;
import java.util.List;

import me.theditor.xelt.commands.api.BaseCommand;
import me.theditor.xelt.commands.api.CommandCategory;
import me.theditor.xelt.utils.MessageUtils;
import me.theditor.xelt.utils.RedditUtils;
import net.dean.jraw.references.SubredditReference;
import net.dean.jraw.tree.RootCommentNode;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownUtil;

public class MemeCommand extends BaseCommand{
	
	private CommandCategory category;

	public MemeCommand(CommandCategory category) {
		this.category = category;
	}

	@Override
	public void run(GuildMessageReceivedEvent e, List<String> args) {
		String subredditname = "dankmemes";
		if(args.size() >= 1) {
			subredditname = args.get(0);
			for (int i = 1; i < args.size(); i++) {
				subredditname = subredditname + " " + args.get(i);
			}
		}
		
		SubredditReference subreddit = RedditUtils.getReddit()
				.subreddit(subredditname);
		
		if(subreddit == null) {
			MessageUtils.errorMessage("Invalid subreddit specified!", e.getChannel());
			return;
		}
		
		boolean nsfw = true;
		try {
			nsfw = subreddit.about().isNsfw();
		} catch (Exception e1) {
			MessageUtils.errorMessage("That subreddit is blacklisted!", e.getChannel());
			return;
		}
		
		if(nsfw) {
			MessageUtils.errorMessage("That subreddit is blacklisted!", e.getChannel());
			return;
		}
		
		RootCommentNode submission = null;
		try {
		 submission = subreddit.randomSubmission();
		}catch (Exception e1) {
			MessageUtils.errorMessage("Invalid subreddit specified!", e.getChannel());
			return;
		}
		String url = submission.getSubject().getUrl();
		
		List<String> msgs = new ArrayList<>();
		msgs.add(MarkdownUtil.maskedLink("Submission", "https://www.reddit.com" +submission.getSubject().getPermalink()) + " | " +submission.getSubject().getScore() + " Upvotes");
		e.getChannel().sendMessage(MessageUtils.noFieldMessageEmbed(submission.getSubject().getTitle(), msgs, e.getChannel())
				.setImage(url)
				.setFooter(subredditname + " • " + submission.getSubject().getAuthor()).build()).queue();
	}

	@Override
	public String[] getAliases() {
		String[] aliases = {"Maymay, meemee"};
		return aliases;
	}

	@Override
	public String getName() {
		return "Meme";
	}	

	@Override
	public String getDescription() {
		return "Get some memes from subreddits!";
	}

	@Override
	public String getUsage(Guild guild) {
		return getName().toLowerCase() + " [subreddit]";
	}

	@Override
	public CommandCategory getParentCategory() {
		return category;
	}

}
