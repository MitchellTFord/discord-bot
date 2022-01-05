package com.mitchelltford.discordbot;

import java.util.List;

public abstract class DefaultCommand implements Command {

  protected String name;
  protected List<String> aliases;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public List<String> getAliases() {
    return aliases;
  }
}
