package com.mitchelltford.discordbot;

import discord4j.core.object.entity.Message;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class Ping implements Command {

  @Override
  public String getName() {
    return "ping";
  }

  @Override
  public List<String> getAliases() {
    return List.of("p");
  }

  @Override
  public Mono<Void> execute(Message message, String args) {
    return Mono.fromRunnable(() -> log.info("Pong!")).then();
  }
}
