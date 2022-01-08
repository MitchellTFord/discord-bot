package com.mitchelltford.discordbot.commands;

import com.mitchelltford.discordbot.DefaultCommand;
import com.mitchelltford.discordbot.LavaPlayerAudioProvider;
import discord4j.core.object.entity.Message;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 *  Represents the skip command
 *
 * @author Liang
 */
@Component
@Slf4j
public class Skip extends DefaultCommand {

  private final LavaPlayerAudioProvider player;

  /** Default constructor to initialize player object */
  public Skip(LavaPlayerAudioProvider player) {
    name = "skip";
    aliases = List.of();
    this.player = player;
  }

  /** To skip the current song in the queue */
  @Override
  public Mono<Void> execute(Message message, String args) {
    player.skipTrack();
    return message.getChannel().flatMap(channel -> channel.createMessage("Song Skipped.").then());
  }
}
