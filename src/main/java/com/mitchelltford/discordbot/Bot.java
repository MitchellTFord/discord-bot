package com.mitchelltford.discordbot;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.logging.log4j.core.tools.picocli.CommandLine.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class Bot {
  private static final String PREFIX = "!";


  interface Command{
    Mono<Void> execute(MessageCreateEvent event);
  }

  @Autowired
  Bot(GatewayDiscordClient client) {
    client.on(MessageCreateEvent.class)
        .subscribe(event -> {
          // handle deez nutz

          Map<String, Command> commands = new HashMap<>();
          commands.put("ping", event -> event.getMessage().getChannel()
              .flatMap(channel -> channel.createMessage("Pong!")).then());
          //Upon a message event in Discord the string will be set to the Discord message
          String userMsg = event.getMessage().getContent();

          //Gets the first character of the message and continues if the first character is a '!'
          for(final Map.Entry<String, Command> entry : commands.entrySet()) {
            if (userMsg.startsWith(PREFIX + entry.getKey())) {
              entry.getValue().execute(event);
              break;
              //String[] temp = userMsg.split("(?<=!)\\w+/"); //String array that contains the command (i.e. whatever is after the '!')
//              String text = userMsg.substring(PREFIX.length(), userMsg.length());
//              String[] splitText = text.split("\\s");
//              if (splitText.length == 0)
//                return;
//              String commandKey = splitText[0];
//              String[] args = Arrays.copyOfRange(splitText, 1, splitText.length);
              //String[] args = userMsg.split("(?<=\\s)\\w+/g"); //String that contains everything after the command (i.e. words that has a space before it)
//              System.out.println("Command Key = " + commandKey);
//            switch (commandKey.toLowerCase()) {
//
//              case "play":
//                if (args.length > 1) {
//                  System.out.println("Playing Music");
//                }
//                else {
//                  String songToPlay = "";
//                  for (int i = 1; i < args.length; i++)
//                    songToPlay = songToPlay.concat(args[i] + " ");
//                  String finalSongToPlay = songToPlay;
//                  event.getMessage().getChannel().flatMap(channel -> channel.createMessage("Playing: "+ finalSongToPlay));
//                }
//                break;
//
//              case "ping":
//                System.out.println("Pong?");
//                event.getMessage().getChannel().flatMap(channel -> channel.createMessage("Pong!"));
//                break;
//            }
            }
          }
        });

  }

}
