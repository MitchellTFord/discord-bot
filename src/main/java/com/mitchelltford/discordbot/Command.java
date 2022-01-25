package com.mitchelltford.discordbot;

import discord4j.core.object.entity.Message;
import java.util.Collections;
import java.util.List;
import reactor.core.publisher.Mono;

public interface Command {
  String getName();

  default List<String> getAliases() {
    return Collections.emptyList();
  }

  String getDescription();

  Mono<Void> execute(Message message, String args);
}
