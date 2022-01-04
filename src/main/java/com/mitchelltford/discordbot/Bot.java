package com.mitchelltford.discordbot;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
        // Get the message associated with this event
        .map(MessageCreateEvent::getMessage)
        // Filter out messages sent by bots
        .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
        // Filter out messages that don't start with the command prefix
        .filter(message -> message.getContent().startsWith(PREFIX))
        // Handle commands
        .flatMap(this::handleCommand)
        .onErrorResume(
            throwable -> {
              log.error("An unhandled error occurred", throwable);
              return Mono.empty();
            })
        .subscribe();

    //    client
    //        .on(MessageCreateEvent.class)
    //        .subscribe(
    //            event -> {
    //
    //              // Upon a message event in Discord the string will be set to the Discord message
    //              String userMsg = event.getMessage().getContent();
    //              System.out.println("Message Read: " + userMsg);
    //
    //              if (userMsg.startsWith(PREFIX)) {
    //                String text =
    //                    userMsg.substring(
    //                        PREFIX.length(), userMsg.length()); // String of message without
    // prefix
    //                String[] splitText =
    //                    text.split("\\s"); // Splitting text with blank spaces as delimiter
    //                if (splitText.length == 0) {
    //                  return;
    //                }
    //                String commandKey = splitText[0];
    //                System.out.println("Command Key = " + commandKey);
    //                String[] args =
    //                    Arrays.copyOfRange(
    //                        splitText,
    //                        1,
    //                        splitText.length); // String array that hold args after commandKey
    //                System.out.println("Args = " + Arrays.toString(args));
    //
    //                switch (commandKey.toLowerCase()) {
    //                  case "play":
    //                    if (args.length < 1) {
    //                      System.out.println("Resuming Music");
    //                      event
    //                          .getMessage()
    //                          .getChannel()
    //                          .flatMap(channel -> channel.createMessage("Resuming Music!"))
    //                          .subscribe();
    //                    } else {
    //                      String songToPlay = "";
    //                      for (String i : args) {
    //                        songToPlay = songToPlay.concat(i + " ");
    //                      }
    //                      String finalSongToPlay = songToPlay;
    //                      System.out.println("Playing: " + finalSongToPlay);
    //                      event
    //                          .getMessage()
    //                          .getChannel()
    //                          .flatMap(channel -> channel.createMessage("Playing: " +
    // finalSongToPlay))
    //                          .subscribe();
    //                    }
    //                    break;
    //
    //                  case "ping":
    //                    System.out.println("Pong?");
    //                    event
    //                        .getMessage()
    //                        .getChannel()
    //                        .flatMap(channel -> channel.createMessage("Pong!"))
    //                        .subscribe();
    //                    break;
    //                }
    //              }
    //            });
  }

  private Mono<Void> handleCommand(Message message) {
    String contentWithoutPrefix = message.getContent().substring(PREFIX.length());
    Matcher matcher = Pattern.compile("^(\\w+)(?:\\s(.*))?$").matcher(contentWithoutPrefix);
    if (!matcher.find()) {
      // No command key in message, do nothing
      return Mono.empty();
    }
    // Command key, non-null
    String key = matcher.group(1);
    // Command arguments, may be null
    String args = matcher.group(2);

    // Get the command corresponding to the key, may be null
    Command command = commands.get(key);

    if (command == null) {
      // Command not found
      return message
          .getChannel()
          .flatMap(
              channel ->
                  channel.createMessage(String.format("Unrecognized command \"%s\"", key)).then());
    }

    // Execute the command
    return command
        .execute(message, args)
        .doOnSubscribe(
            unused ->
                log.debug(
                    "Executing command \"{}\" in response to this message: \"{}\"",
                    command.getName(),
                    message.getContent()))
        .doOnError(
            throwable -> log.error("Error executing command \"{}\"", command.getName(), throwable))
        .doOnSuccess(unused -> log.info("Successfully executed command \"{}\"", command.getName()));
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
