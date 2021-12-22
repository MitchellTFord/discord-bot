package com.mitchelltford.discordbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);

    //copied from https://docs.discord4j.com/quickstart/
    DiscordClient.create("TOKEN")
        .withGateway(client ->
            client.on(MessageCreateEvent.class, event -> {
              Message message = event.getMessage();

              if (message.getContent().equalsIgnoreCase("!ping")) {
                return message.getChannel()
                    .flatMap(channel -> channel.createMessage("Pong!"));
              }

              return Mono.empty();
            }))
        .block();
  }

}
