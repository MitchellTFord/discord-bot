package com.mitchelltford.discordbot;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class GatewayDiscordClientConfig {
  private GatewayDiscordClient client;

  public GatewayDiscordClientConfig() {}

  @Bean
  public GatewayDiscordClient gatewayDiscordConfig() {
    return client;
  }

  public void setClient(GatewayDiscordClient client) {
    this.client = client;
  }

  public GatewayDiscordClient getClient() {
    return client;
  }

}
