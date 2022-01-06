package com.mitchelltford.discordbot;

import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import org.springframework.stereotype.Component;

/**
 * This class is used for generating a player to play track
 * referenced from sedmelluq
 * @author Liang
 */
@Component
public class LavaPlayerAudioProvider extends LavaPlayer {
  private final MutableAudioFrame frame = new MutableAudioFrame();
  /**
   * Default constructor to initialize the buffer size
   */
  public LavaPlayerAudioProvider() {
    frame.setBuffer(getBuffer());
  }

  @Override
  /**
   * AudioPlayer writes audio data to its AudioFrame
   */
  public boolean provide() {
    final boolean didProvide = getPlayer().provide(frame);
    // If audio was provided, flip from write-mode to read-mode
    if (didProvide) {
      getBuffer().flip();
    }
    return didProvide;
  }
}
