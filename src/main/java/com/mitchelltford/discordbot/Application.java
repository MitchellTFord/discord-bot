package com.mitchelltford.discordbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {

    ApplicationContext apc = SpringApplication.run(Application.class, args);
    for(String s: apc.getBeanDefinitionNames()) {
      System.out.println(s);
    }

  }

}
