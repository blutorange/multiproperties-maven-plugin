package com.github.blutorange.multiproperties_maven_plugin.handler;

import java.nio.file.Path;
import java.util.Map;

final class NoneHandler implements IOutputHandler {
  @Override
  public void handleProperties(String configuration, Map<String, String> properties, Path baseDir) throws Exception {
    // no-op
  }

  @Override
  public String getName() {
    return "None Handler";
  }
}
