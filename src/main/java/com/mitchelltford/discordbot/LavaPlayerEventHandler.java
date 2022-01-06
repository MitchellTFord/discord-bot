package com.mitchelltford.discordbot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * This class is used for handling different event in the LavaPlayer
 * referenced from sedmelluq
 * @author Liang & Nick
 */
public class LavaPlayerEventHandler extends AudioEventAdapter {
  private final AudioPlayer player;

  /**
   * to store a queue of songs to be play
   */
  private final BlockingQueue<AudioTrack> queue;

  /**
   * default constructor to initialize the queue
   */
  public LavaPlayerEventHandler(AudioPlayer player) {
    this.player = player;
    this.queue = new LinkedBlockingQueue<>();
  }

  /**
   * append the song to the queue
   */
  public void queue(AudioTrack track) {
    if (!player.startTrack(track, true)) {
      queue.offer(track);
    }
  }

  /**
   * skip the current song and play the next song in queue
   */
  public void nextTrack() {
    player.startTrack(queue.poll(), false);
  }

  @Override
  /**
   * check if the queue is ended, if not, we can play the next song.
   */
  public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
    if (endReason.mayStartNext) {
      nextTrack();
    }
  }
}
