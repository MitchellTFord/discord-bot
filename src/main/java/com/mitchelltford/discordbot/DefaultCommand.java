package com.mitchelltford.discordbot;

import java.util.List;

public abstract class DefaultCommand implements Command {

  protected String name;
  protected List<String> aliases;
  protected String description;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public List<String> getAliases() {
    return aliases;
  }

  @Override
  public String getDescription() {
    return description;
  }
}
