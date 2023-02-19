package com.github.blutorange.multiproperties_maven_plugin.handler;

import static com.github.blutorange.multiproperties_maven_plugin.common.StringHelper.removeStart;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.Properties;

final class JavaPropertiesHandler implements IOutputHandler {
  @Override
  public String getName() {
    return "Java Properties Handler";
  }

  @Override
  public void handleProperties(IOutputParams params) throws Exception {
    final var configuration = new JavaPropertiesConfiguration(params.getConfiguration());
    final var javaProperties = new Properties();
    for (final var entry : params.getProperties().entrySet()) {
      javaProperties.put(entry.getKey(), entry.getValue());
    }
    final var outputFile = params.getBaseDir().resolve(removeStart(configuration.getOutputPath(), "/"));
    final var encoding = configuration.getEncoding();
    params.getLogger().info(String.format("Writing file <%s> with encoding <%s>", outputFile, encoding));
    try (final var out = Files.newOutputStream(outputFile, CREATE, WRITE, TRUNCATE_EXISTING)) {
      try (final var writer = new OutputStreamWriter(out, encoding)) {
        javaProperties.store(writer, "");
      }
    }
  }
}
