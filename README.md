# Xelt

## Overview
Xelt is my Discord bot which I coded from scratch in 3 days code-time.
I made this bot to refresh my knowledge on the topic of discord bots. 
I chose to use [Discord JDA](https://github.com/DV8FromTheWorld/JDA)
since I am more familiar with Java than any other programming language
and have used it before.

## Features
- Music (Added 7th July 2020)
- Tickets (Added 5th July 2020)
- Moderation (Added 3rd July 2020)
- Miscellaneous (Added 3rd July 2020)
- Fun (Added 10th July 2020)
- Voice (Added 19th July 2020)

## Invite
You can invite Xelt [here](https://discord.com/api/oauth2/authorize?client_id=726763157195849728&permissions=8&scope=bot)

## Dependencies
- [Discord JDA](https://github.com/DV8FromTheWorld/JDA)
- [Dotenv](https://github.com/cdimascio/java-dotenv)
- [MySQL-Connector-Java](https://mvnrepository.com/artifact/mysql/mysql-connector-java)
- [Pastebin-Java-API](https://github.com/marcoacierno/pastebin-java-api)
- [LavaPlayer](https://github.com/sedmelluq/lavaplayer/)
- [Youtube API](https://mvnrepository.com/artifact/com.google.apis/google-api-services-youtube)
- [JRAW](https://github.com/mattbdean/JRAW)
- [Java-Lame](https://github.com/nwaldispuehl/java-lame)

## Feature Descriptions

###### Music

Xelt uses [LavaPlayer](https://github.com/sedmelluq/lavaplayer/) to play music. Music's coding started on morning 7th July, 2020 and finished by evening 7th July, 2020.
The commands associated to music are play, join, leave, skip. It has a voting system for commands - skip, leave if 2 or more
people are listening to Xelt.
Command Categories were implemented along with Music.

###### Tickets
Xelt has a ticket system which works with the user's ID. It uses [Pastebin-Java-API](https://github.com/marcoacierno/pastebin-java-api) to create transcripts at the end
of a ticket and sends it to all the participants of the ticket. Unfortunately it does not have a feature like "Support Team" and therefore you will have to give read/write
permission to the "Tickets" category to the people you want to let handle tickets.

###### Moderation
Xelt has a basis moderation system which includes ban, kick and warn. Nothing really special here. This, too, was a starter to learn the Discord API.

###### Miscellaneous
Xelt has a set of miscellaneous commands which includes purge, prefix, invite, help. These commands are self-explanatory and are widely used by Discord bots.
This was a starter to learn the Discord API.

###### Fun
Xelt has a bunch of fun commands that you can use 8Ball, Roll, Hug, Bite, Enlarge, F, Meme. All are self-explanatory but in the meme one you can specify a
subreddit, so you can see memes from your favourite subreddit.

###### Voice
Xelt supports recording audio in channels and then sending them to you. There are to commands - record, stop. You can use these commands to record audio. Currently, Xelt 
has a limitation of recording not more than 8 MB (Discord limitation), however, this limitation will be removed soon.
