package com.mitchelltford.discordbot;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class Bot {

  private static final String PREFIX = "!";

  @Autowired
  Bot(GatewayDiscordClient client) {
    client.on(MessageCreateEvent.class)
        .subscribe(event -> {

          //Upon a message event in Discord the string will be set to the Discord message
          String userMsg = event.getMessage().getContent();
          System.out.println("Message Read: " + userMsg);

          if (userMsg.startsWith(PREFIX)) {
            String text = userMsg.substring(PREFIX.length(),
                userMsg.length()); //String of message without prefix
            String[] splitText = text.split("\\s"); //Splitting text with blank spaces as delimiter
            if (splitText.length == 0) {
              return;
            }
            String commandKey = splitText[0];
            System.out.println("Command Key = " + commandKey);
            String[] args = Arrays.copyOfRange(splitText, 1,
                splitText.length); //String array that hold args after commandKey
            System.out.println("Args = " + Arrays.toString(args));

            switch (commandKey.toLowerCase()) {

              case "play":
                if (args.length < 1) {
                  System.out.println("Resuming Music");
                  event.getMessage().getChannel()
                      .flatMap(channel -> channel.createMessage("Resuming Music!")).subscribe();
                } else {
                  String songToPlay = "";
                  for (String i : args) {
                    songToPlay = songToPlay.concat(i + " ");
                  }
                  String finalSongToPlay = songToPlay;
                  System.out.println("Playing: " + finalSongToPlay);
                  event.getMessage().getChannel()
                      .flatMap(channel -> channel.createMessage("Playing: " + finalSongToPlay))
                      .subscribe();
                }
                break;

              case "ping":
                System.out.println("Pong?");
                event.getMessage().getChannel()
                    .flatMap(channel -> channel.createMessage("Pong!")).subscribe();
                break;
            }
          }
        });

  }

}
