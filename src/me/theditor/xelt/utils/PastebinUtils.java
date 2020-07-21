package me.theditor.xelt.utils;

import com.besaba.revonline.pastebinapi.Pastebin;
import com.besaba.revonline.pastebinapi.impl.factory.PastebinFactory;
import com.besaba.revonline.pastebinapi.paste.Paste;
import com.besaba.revonline.pastebinapi.paste.PasteBuilder;
import com.besaba.revonline.pastebinapi.paste.PasteExpire;
import com.besaba.revonline.pastebinapi.paste.PasteVisiblity;
import com.besaba.revonline.pastebinapi.response.Response;

import me.theditor.xelt.Config;

public class PastebinUtils {

	private static PastebinFactory factory = new PastebinFactory();
	private static Pastebin pastebin = factory.createPastebin(Config.get("PASTEBIN_DEV_KEY"));

	public static String newPaste(String title, String raw) {
		final PasteBuilder pasteBuilder = factory.createPaste();
		pasteBuilder.setTitle(title);
		pasteBuilder.setRaw(raw);
		pasteBuilder.setMachineFriendlyLanguage("text");
		pasteBuilder.setVisiblity(PasteVisiblity.Private);
		pasteBuilder.setExpire(PasteExpire.OneWeek);

		final Paste paste = pasteBuilder.build();
		final Response<String> postResult = pastebin.post(paste);

		if (postResult.hasError()) {
			return null;
		}
		
		return postResult.get();
	}

}
