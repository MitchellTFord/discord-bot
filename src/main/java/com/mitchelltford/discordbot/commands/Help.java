package com.mitchelltford.discordbot.commands;

import com.mitchelltford.discordbot.Command;
import com.mitchelltford.discordbot.DefaultCommand;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import java.util.List;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class Help extends DefaultCommand {

  List<? extends Command> commandList;
  EmbedCreateSpec embed;

  public Help(List<? extends Command> commands) {
    name = "help";
    aliases = List.of("h");
    description = "Help gives a list of all available commands, but gives a description of a"
            + "command if used in conjunction with another command.";
    commandList = commands;
  }

  @Override
  public Mono<Void> execute(Message message, String args) {
    if (args == null) {
      String argList = "";
      for (Command command : commandList) {
        argList = argList.concat(command.getName() + "\n");
      }
      embed = EmbedCreateSpec.builder()
          .addField("Commands", argList, false)
          .build();
      return message.getChannel()
          .flatMap(channel -> channel.createMessage(embed)).then();
    } else {
      for (Command command : commandList) {
        if (command.getName().equals(args)) {
          embed =
              EmbedCreateSpec.builder()
                  .addField(command.getName(), command.getDescription(), false)
                  .build();
          return message.getChannel()
              .flatMap(channel -> channel.createMessage(embed)).then();
        }
      }
      return message.getChannel()
          .flatMap(channel -> channel.createMessage("\"" + args + "\" isn't a valid command.").then());
    }
  }
}
