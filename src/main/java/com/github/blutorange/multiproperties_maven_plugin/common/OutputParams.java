package com.github.blutorange.multiproperties_maven_plugin.common;

import java.nio.file.Path;
import java.util.Map;

import org.apache.maven.plugin.logging.Log;

import com.github.blutorange.multiproperties_maven_plugin.handler.IOutputParams;

final class OutputParams implements IOutputParams {
  private final Log logger;
  private final Path baseDir;
  private final String configuration;
  private final Map<String, String> properties;

  public OutputParams(Log logger, Path baseDir, String configuration, Map<String, String> properties) {
    this.logger = logger;
    this.baseDir = baseDir;
    this.configuration = configuration;
    this.properties = properties;
  }

  @Override
  public Path getBaseDir() {
    return baseDir;
  }

  @Override
  public String getConfiguration() {
    return configuration;
  }

  @Override
  public Log getLogger() {
    return logger;
  }

  @Override
  public Map<String, String> getProperties() {
    return properties;
  }
}
