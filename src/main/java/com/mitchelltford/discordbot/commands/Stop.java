package com.mitchelltford.discordbot.commands;

import com.mitchelltford.discordbot.DefaultCommand;
import com.mitchelltford.discordbot.LavaPlayerAudioProvider;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.voice.VoiceConnection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class Stop extends DefaultCommand {

  private final GatewayDiscordClient client;
  private final LavaPlayerAudioProvider player;

  public Stop(GatewayDiscordClient client, LavaPlayerAudioProvider player) {
    name = "stop";
    aliases = List.of();
    this.client = client;
    this.player = player;
  }

  @Override
  public Mono<Void> execute(Message message, String args) {
    player.stopPlayBack();
    return message
        .getAuthorAsMember()
        .flatMap(Member::getVoiceState)
        .flatMap(
            vs ->
                client
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
