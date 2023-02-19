package com.github.blutorange.multiproperties_maven_plugin.handler;

import static com.github.blutorange.multiproperties_maven_plugin.common.StringHelper.removeStart;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

final class JavaPropertiesHandler implements IOutputHandler {
  @Override
  public String getName() {
    return "Java Properties Handler";
  }

  @Override
  public void handleProperties(String configurationString, Map<String, String> properties, Path baseDir) throws Exception {
    final var configuration = new JavaPropertiesConfiguration(configurationString);
    final var javaProperties = new Properties();
    for (final var entry : properties.entrySet()) {
      javaProperties.put(entry.getKey(), entry.getValue());
    }
    final var outputFile = baseDir.resolve(removeStart(configuration.getOutputPath(), "/"));
    final var encoding = configuration.getEncoding();
    try (final var out = Files.newOutputStream(outputFile, CREATE, WRITE, TRUNCATE_EXISTING)) {
      try (final var writer = new OutputStreamWriter(out, encoding)) {
        javaProperties.store(writer, "");
      }
    }
  }
}
