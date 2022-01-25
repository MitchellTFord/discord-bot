package com.mitchelltford.discordbot.commands;

import com.mitchelltford.discordbot.DefaultCommand;
import discord4j.core.object.entity.Message;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class Ping extends DefaultCommand {

  public Ping() {
    name = "ping";
    aliases = List.of("p");
    description = "Bot responds with \"pong\"";
  }

  @Override
  public Mono<Void> execute(Message message, String args) {
    return message.getChannel().flatMap(channel -> channel.createMessage("Pong!").then());
  }
}
