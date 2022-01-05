package com.mitchelltford.discordbot;

import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import org.springframework.stereotype.Component;

// This class is used for generating a player to play track
@Component
public class LavaPlayerAudioProvider extends LavaPlayer {
  private final MutableAudioFrame frame = new MutableAudioFrame();

  public LavaPlayerAudioProvider() {

    frame.setBuffer(getBuffer());
  }

  @Override
  public boolean provide() {
    // AudioPlayer writes audio data to its AudioFrame
    final boolean didProvide = getPlayer().provide(frame);
    // If audio was provided, flip from write-mode to read-mode
    if (didProvide) {
      getBuffer().flip();
    }
    return didProvide;
  }
}
