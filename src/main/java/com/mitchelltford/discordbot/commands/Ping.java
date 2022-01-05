package com.mitchelltford.discordbot.commands;

import com.mitchelltford.discordbot.DefaultCommand;
import com.mitchelltford.discordbot.LavaPlayerAudioProvider;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.core.spec.VoiceChannelJoinSpec;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class Ping extends DefaultCommand {

  private final LavaPlayerAudioProvider player;

  public Ping(LavaPlayerAudioProvider player) {
    name = "ping";
    aliases = List.of("p");
    this.player = player;
  }

  @Override
  public Mono<Void> execute(Message message, String args) {

    Member member = message.getAuthorAsMember().block();
    if (member != null) {
      final VoiceState voiceState = member.getVoiceState().block();
      if (voiceState != null) {
        final VoiceChannel channel = voiceState.getChannel().block();
        if (channel != null) {
          // join returns a VoiceConnection which would be required if we were
          // adding disconnection features, but for now we are just ignoring it.
          channel.join(VoiceChannelJoinSpec.builder().provider(player).build()).block();
        }
      }
    }
    String url = "https://youtu.be/AULG4MoYxQk";
    MessageChannel messageChannel = message.getChannel().block();
    player.loadAndPlay(url, messageChannel);

    return message.getChannel().flatMap(channel -> channel.createMessage("Pong!").then());
  }
}
