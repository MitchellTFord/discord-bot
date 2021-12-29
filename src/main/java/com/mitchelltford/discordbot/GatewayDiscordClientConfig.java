package com.mitchelltford.discordbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.DiscordClientBuilder;


@Configuration
public class GatewayDiscordClientConfig {

  @Bean
  public GatewayDiscordClient gatewayDiscordClient(@Value("${bot.token}") String token) {
    return DiscordClientBuilder.create(token).build().login().block();
  }

}
