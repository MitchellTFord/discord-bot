package com.mitchelltford.discordbot.commands;

import com.mitchelltford.discordbot.DefaultCommand;
import com.mitchelltford.discordbot.LavaPlayerAudioProvider;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.VoiceChannelJoinSpec;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Represents the play command
 *
 * @author Liang & Nick
 */
@Component
@Slf4j
public class Play extends DefaultCommand {

  private final LavaPlayerAudioProvider player;

  /** Default constructor to initialize player object */
  public Play(LavaPlayerAudioProvider player) {
    name = "play";
    aliases = List.of();
    this.player = player;
  }

  /** Load the song into the queue and play it */
  @Override
  public Mono<Void> execute(Message message, String args) {

    String botMessage = "Playing: " + args;

    MessageChannel messageChannel = message.getChannel().block();
    player.loadAndPlay(args, messageChannel);

    return message
        .getAuthorAsMember()
        .flatMap(Member::getVoiceState)
        .flatMap(VoiceState::getChannel)
        .flatMap(channel -> channel.join(VoiceChannelJoinSpec.builder().provider(player).build()))
        .flatMap(
            unused -> message.getChannel().flatMap(channel -> channel.createMessage(botMessage)))
        .then();
  }
}
