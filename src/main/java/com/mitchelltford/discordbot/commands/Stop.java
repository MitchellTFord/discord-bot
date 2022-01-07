package com.mitchelltford.discordbot.commands;

import com.mitchelltford.discordbot.DefaultCommand;
import com.mitchelltford.discordbot.LavaPlayerAudioProvider;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.voice.VoiceConnection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
/**
 * This is a command class to stop playback and have the bot leave the voice channel
 *
 * @author Nick
 */
public class Stop extends DefaultCommand {

  private final LavaPlayerAudioProvider player;

  /** Default constructor to initialize player object */
  public Stop(LavaPlayerAudioProvider player) {
    name = "stop";
    aliases = List.of();
    this.player = player;
  }

  @Override
  /** stop the songs in the queue and disconnect the bot */
  public Mono<Void> execute(Message message, String args) {
    player.stopPlayBack();
    return message
        .getAuthorAsMember()
        .flatMap(Member::getVoiceState)
        .flatMap(
            vs ->
                message
                    .getClient()
                    .getVoiceConnectionRegistry()
                    .getVoiceConnection(vs.getGuildId())
                    .doOnSuccess(
                        vc -> {
                          if (vc == null) {
                            log.info("No voice connection to leave.");
                          }
                        })
                    .flatMap(VoiceConnection::disconnect));
  }
}
