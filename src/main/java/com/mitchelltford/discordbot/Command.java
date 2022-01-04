package com.mitchelltford.discordbot;

import discord4j.core.object.entity.Message;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public interface Command {
  String getName();

  default List<String> getAliases() {
    return Collections.emptyList();
  }

  Mono<Void> execute(Message message, String args);
}
