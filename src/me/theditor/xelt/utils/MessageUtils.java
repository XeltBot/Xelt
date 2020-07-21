package me.theditor.xelt.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import me.theditor.xelt.Xelt;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

public class MessageUtils {

	public static EmbedBuilder messageEmbed(String name, String msg, MessageChannel channel) {
		return messageEmbed(name, "", msg, channel);
	}

	public static EmbedBuilder messageEmbed(String name, String title, String msg, MessageChannel channel) {
		List<String> msgs = new ArrayList<>();
		msgs.add(msg);
		return singleFieldMessageEmbed(name, title, msgs, channel);
	}

	public static EmbedBuilder singleFieldMessageEmbed(String name, String title, List<String> msgs,
			MessageChannel channel) {
		return singleFieldMessageEmbed(name, title, msgs, new Color(52, 128, 235), channel);
	}

	public static EmbedBuilder singleFieldMessageEmbed(String name, String title, List<String> msgs, Color color,
			MessageChannel channel) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(color);
		builder.setTitle(name);
		if(msgs.size() != 0) {
		String finalMsg = msgs.get(0);
		for (int i = 1; i < msgs.size(); i++) {
			finalMsg = finalMsg + "\n" + msgs.get(i);
		}
		builder.addField(title, finalMsg, false);
		}
		// builder.setThumbnail(Xelt.thumbnailLink);
		if (channel instanceof TextChannel) {
			builder.setFooter(((TextChannel) channel).getGuild().getName() + " - " + Xelt.getInstance().getName(),
					((TextChannel) channel).getGuild().getIconUrl());
		}
		return builder;
	}

	public static EmbedBuilder noFieldMessageEmbed(String name, List<String> msgs, MessageChannel channel) {
		return noFieldMessageEmbed(name, msgs, new Color(52, 128, 235), channel);
	}

	public static EmbedBuilder noFieldMessageEmbed(String name, List<String> msgs, Color color, MessageChannel channel) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(color);
		if(msgs.size() != 0) {
		String finalMsg = msgs.get(0);
		for (int i = 1; i < msgs.size(); i++) {
			finalMsg = finalMsg + "\n" + msgs.get(i);
		}
		builder.addField(name, finalMsg, false);
		}
		// builder.setThumbnail(Xelt.thumbnailLink);
		if (channel instanceof TextChannel) {
			builder.setFooter(((TextChannel) channel).getGuild().getName() + " - " + Xelt.getInstance().getName(),
					((TextChannel) channel).getGuild().getIconUrl());
		}
		return builder;
	}

	public static EmbedBuilder multiFieldMessageEmbed(String name, List<String> title, List<String> msgs,
			MessageChannel channel) {
		return multiFieldMessageEmbed(name, title, msgs, new Color(52, 128, 235), channel);
	}

	public static EmbedBuilder multiFieldMessageEmbed(String name, List<String> title, List<String> msgs, Color color,
			MessageChannel channel) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(color);
		builder.setTitle(name);
		for (int i = 0; i < msgs.size(); i++) {
			builder.addField(title.get(i), msgs.get(i), false);
		}
		// builder.setThumbnail(Xelt.thumbnailLink);
		if (channel instanceof TextChannel) {
			builder.setFooter(((TextChannel) channel).getGuild().getName() + " - " + Xelt.getInstance().getName(),
					((TextChannel) channel).getGuild().getIconUrl());
		}
		return builder;
	}

	public static EmbedBuilder errorMessage(String error, MessageChannel channel) {
		ArrayList<String> list = new ArrayList<>();
		list.add(error);
		EmbedBuilder embed = noFieldMessageEmbed("Error", list, Color.red, channel);
		channel.sendMessage(embed.build()).queue();
		return embed;
	}

	public static String capitalize(String s) {
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}

}
