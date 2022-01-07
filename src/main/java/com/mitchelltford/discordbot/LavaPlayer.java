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
 * This class is used to set up AudioPlayerManager and AudioPlayer, and basic functionalities for
 * the Music bot referenced from sedmelluq
 *
 * @author Liang & Nick
 */
public abstract class LavaPlayer extends AudioProvider {

  /** AudioPlayerManager is used for configuration settings modification */
  private final AudioPlayerManager playerManager;

  private final AudioPlayer player;
  private final LavaPlayerEventHandler eventHandler;

  /** Default constructor to initialize the AudioPlayer & EventHandler */
  public LavaPlayer() {
    playerManager = new DefaultAudioPlayerManager();
    AudioSourceManagers.registerRemoteSources(playerManager);
    player = playerManager.createPlayer();
    eventHandler = new LavaPlayerEventHandler(player);
    player.addListener(eventHandler);
  }

  /** This is a function to access the AudioPlayer object */
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

          @Override
          /** Add a song to the queue */
          public void trackLoaded(AudioTrack track) {
            eventHandler.queue(track);
            channel.createMessage("Added to queue:").subscribe();
          }

          @Override
          /** Add a play list to the queue */
          public void playlistLoaded(AudioPlaylist playlist) {
            for (AudioTrack track : playlist.getTracks()) {
              eventHandler.queue(track);
            }
          }

          @Override
          /** Notify the user that we've got nothing */
          public void noMatches() {
            this.channel.createMessage("No Matches For :" + identifier).subscribe();
          }

          @Override
          /** Notify the user that everything exploded */
          public void loadFailed(FriendlyException throwable) {
            this.channel.createMessage("Load Failed For :" + identifier).subscribe();
          }
        });
  }

  public abstract boolean provide();
}
