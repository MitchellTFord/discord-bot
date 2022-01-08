package com.mitchelltford.discordbot;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.voice.AudioProvider;

/**
 * Represents the AudioPlayerManager for LavaPlayer
 *
 * @author Liang & Nick
 */
public abstract class LavaPlayer extends AudioProvider {

  /** AudioPlayerManager is used for configuration settings modification */
  private final AudioPlayerManager playerManager;
  /** AudioPlayer is used for playing the music */
  private final AudioPlayer player;
  /** LavaPlayerEventHandler is used for handing different cases */
  private final LavaPlayerEventHandler eventHandler;

  /** Default constructor to initialize the AudioPlayer & EventHandler */
  public LavaPlayer() {
    playerManager = new DefaultAudioPlayerManager();
    AudioSourceManagers.registerRemoteSources(playerManager);
    player = playerManager.createPlayer();
    eventHandler = new LavaPlayerEventHandler(player);
    player.addListener(eventHandler);
  }

  /** Return the AudioPlayer object */
  public AudioPlayer getPlayer() {
    return player;
  }

  /** To stop the song completely */
  public void stopPlayBack() {
    this.player.stopTrack();
  }

  /** To skip a song and play the next song in queue */
  public void skipTrack() {
    this.eventHandler.nextTrack();
  }

  /** To load a track and play it */
  public void loadAndPlay(String identifier, MessageChannel channel) {
    playerManager.loadItem(
        identifier,
        new AudioLoadResultHandler() {
          MessageChannel channel;

          /** Add a song to the queue */
          @Override
          public void trackLoaded(AudioTrack track) {
            eventHandler.queue(track);
            channel.createMessage("Added to queue:").subscribe();
          }

          /** Add a play list to the queue */
          @Override
          public void playlistLoaded(AudioPlaylist playlist) {
            for (AudioTrack track : playlist.getTracks()) {
              eventHandler.queue(track);
            }
          }

          /** Notify the user that we've got nothing */
          @Override
          public void noMatches() {
            this.channel.createMessage("No Matches For :" + identifier).subscribe();
          }


          /** Notify the user that everything exploded */
          @Override
          public void loadFailed(FriendlyException throwable) {
            this.channel.createMessage("Load Failed For :" + identifier).subscribe();
          }
        });
  }

  public abstract boolean provide();
}
