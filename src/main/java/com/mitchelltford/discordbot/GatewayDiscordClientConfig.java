package com.mitchelltford.discordbot;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.DiscordClientBuilder;


@Configuration
public class GatewayDiscordClientConfig {

  private GatewayDiscordClient client;

  @Bean
  public GatewayDiscordClient gatewayDiscordConfig() {
//    client = DiscordClientBuilder.create("***Token Goes Here***").build().login().block();
//    client.onDisconnect().block();
    return client;
  }

// Still testing the functionality of logout(), it appears that it doesn't log the bot out right away.
//  public void logout() {
//    client.logout();
//  }

  public GatewayDiscordClient getClient() {
    return client;
  }

  public void setClient(GatewayDiscordClient client) {
    this.client = client;
  }

}
