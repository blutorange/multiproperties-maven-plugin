package com.github.blutorange.multiproperties_maven_plugin.handler;

final class NoneHandler implements IOutputHandler {
  @Override
  public void handleProperties(IOutputParams params) throws Exception {
    // no-op
  }

  @Override
  public String getName() {
    return "None Handler";
  }
}
