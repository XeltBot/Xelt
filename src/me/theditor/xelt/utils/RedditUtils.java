package me.theditor.xelt.utils;

import me.theditor.xelt.Xelt;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;

public class RedditUtils {

	private static Credentials credentials;
	private static NetworkAdapter adapter;
	private static RedditClient reddit;

	public static void setup() {
		try {
			credentials = Credentials.script(Xelt.getInstance().getRedditUsername(),
					Xelt.getInstance().getRedditPassword(), Xelt.getInstance().getRedditClientId(),
					Xelt.getInstance().getRedditSecret());
			adapter = new OkHttpNetworkAdapter(Xelt.getInstance().getUserAgent());
			reddit = OAuthHelper.automatic(adapter, credentials);
			if (reddit == null) {
				Xelt.getInstance().getLogger().fatal("Reddit is null");
			}
		} catch (Exception e) {
			Xelt.getInstance().getLogger().fatal("Reddit is null");
		}
	}

	public static RedditClient getReddit() {
		return reddit;
	}
}
