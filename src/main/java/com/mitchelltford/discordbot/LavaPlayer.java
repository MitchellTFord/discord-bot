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

// This class is used to set up AudioPlayerManager and AudioPlayer, and basic functionalities for
// the Music bot
public abstract class LavaPlayer extends AudioProvider {
  // AudioPlayerManager is used for configuration settings modification
  private final AudioPlayerManager playerManager;
  private final AudioPlayer player;
  private final LavaPlayerEventHandler eventHandler;

  public LavaPlayer() {
    playerManager = new DefaultAudioPlayerManager();
    AudioSourceManagers.registerRemoteSources(playerManager);
    player = playerManager.createPlayer();
    eventHandler = new LavaPlayerEventHandler(player);
    player.addListener(eventHandler);
  }

  public AudioPlayer getPlayer() {
    return player;
  }

  public void stopPlayBack() {
    this.player.stopTrack();
  }

  public void skipTrack() {
    this.eventHandler.nextTrack();
  }
  // To load a track and play it
  public void loadAndPlay(String identifier, MessageChannel channel) {
    playerManager.loadItem(
        identifier,
        new AudioLoadResultHandler() {
          MessageChannel channel;

          @Override
          public void trackLoaded(AudioTrack track) {
            eventHandler.queue(track);
            channel.createMessage("Added to queue:").subscribe();
          }

          @Override
          public void playlistLoaded(AudioPlaylist playlist) {
            for (AudioTrack track : playlist.getTracks()) {
              eventHandler.queue(track);
            }
          }

          @Override
          public void noMatches() {
            // Notify the user that we've got nothing
            this.channel.createMessage("No Matches For :" + identifier).subscribe();
          }

          @Override
          public void loadFailed(FriendlyException throwable) {
            // Notify the user that everything exploded
            this.channel.createMessage("Load Failed For :" + identifier).subscribe();
          }
        });
  }

  public abstract boolean provide();
}
