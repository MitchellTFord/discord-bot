package com.mitchelltford.discordbot;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import discord4j.core.object.entity.channel.TextChannel;


public class LavaPlayer {

  private AudioPlayerManager playerManager;
  private AudioPlayer player;
  private LavaPlayerEventHandler eventHandler;

  public LavaPlayer() {
    playerManager = new DefaultAudioPlayerManager();
    AudioSourceManagers.registerRemoteSources(playerManager);
    player = playerManager.createPlayer();
    eventHandler = new LavaPlayerEventHandler(player);
    player.addListener(eventHandler);
  }

  public AudioPlayer getPlayer() { return player; }

  public void loadAndPlay(String identifier, TextChannel channel) {
    playerManager.loadItem(identifier, new AudioLoadResultHandler() {
      @Override
      public void trackLoaded(AudioTrack track) {
        eventHandler.queue(track);
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
      }

      @Override
      public void loadFailed(FriendlyException throwable) {
        // Notify the user that everything exploded
      }
    });
  }


}
