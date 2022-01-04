package com.mitchelltford.discordbot;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!test")
@Slf4j
public class Bot {

  private static final String PREFIX = "!";

  private final Map<String, Command> commands;

  @Autowired
  public Bot(GatewayDiscordClient client, List<? extends Command> commands) {

    // Register all the found commands
    this.commands = new HashMap<>();
    registerCommands(commands);

    client
        .on(MessageCreateEvent.class)
        .subscribe(
            event -> {

              // Upon a message event in Discord the string will be set to the Discord message
              String userMsg = event.getMessage().getContent();
              System.out.println("Message Read: " + userMsg);

              if (userMsg.startsWith(PREFIX)) {
                String text =
                    userMsg.substring(
                        PREFIX.length(), userMsg.length()); // String of message without prefix
                String[] splitText =
                    text.split("\\s"); // Splitting text with blank spaces as delimiter
                if (splitText.length == 0) {
                  return;
                }
                String commandKey = splitText[0];
                System.out.println("Command Key = " + commandKey);
                String[] args =
                    Arrays.copyOfRange(
                        splitText,
                        1,
                        splitText.length); // String array that hold args after commandKey
                System.out.println("Args = " + Arrays.toString(args));

                switch (commandKey.toLowerCase()) {
                  case "play":
                    if (args.length < 1) {
                      System.out.println("Resuming Music");
                      event
                          .getMessage()
                          .getChannel()
                          .flatMap(channel -> channel.createMessage("Resuming Music!"))
                          .subscribe();
                    } else {
                      String songToPlay = "";
                      for (String i : args) {
                        songToPlay = songToPlay.concat(i + " ");
                      }
                      String finalSongToPlay = songToPlay;
                      System.out.println("Playing: " + finalSongToPlay);
                      event
                          .getMessage()
                          .getChannel()
                          .flatMap(channel -> channel.createMessage("Playing: " + finalSongToPlay))
                          .subscribe();
                    }
                    break;

                  case "ping":
                    System.out.println("Pong?");
                    event
                        .getMessage()
                        .getChannel()
                        .flatMap(channel -> channel.createMessage("Pong!"))
                        .subscribe();
                    break;
                }
              }
            });
  }

  private void registerCommand(String key, Command command) {
    if (this.commands.containsKey(key)) {
      throw new RuntimeException(
          String.format("Attempted to register command using duplicate key \"%s\"", key));
    }
    this.commands.put(key, command);
  }

  private void registerCommands(List<? extends Command> commands) {
    for (Command command : commands) {
      registerCommand(command.getName(), command);
      for (String alias : command.getAliases()) {
        registerCommand(alias, command);
      }
      log.debug(
          "Registered \"{}\" command using it's name and {} aliases",
          command.getName(),
          command.getAliases().size());
    }
    log.info(
        "Registered {} commands using a total of {} keys", commands.size(), this.commands.size());
  }
}
