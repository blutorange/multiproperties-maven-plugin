package com.github.blutorange.multiproperties_maven_plugin.handler;

import static com.github.blutorange.multiproperties_maven_plugin.common.StringHelper.removeStart;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

final class JavaPropertiesHandler implements IOutputHandler {
  @Override
  public String getName() {
    return "Java Properties Handler";
  }

  // Handler configuration:
  //
  // <FilePath>|<Flag1>|<Flag2>|<Flag3>|<Flag4>|<Encoding>
  //
  // where
  //
  // Flag1 = Insert description of MultiProperties in the beginning as comment
  // Flag2 = Insert description of column int the beginning as comment
  // Flag3 = Write disabled properties as comments
  // Flag4 = Disabled default values
  //
  // e.g.
  //
  // /fc-backend-common/src/main/resources/i18n/fc_de.properties|false|true|true|false|ISO-8859-1
  @Override
  public void handleProperties(String configurationString, Map<String, String> properties, Path baseDir) throws Exception {
    final var configuration = new Configuration(configurationString);
    final var javaProperties = new Properties();
    for (final var entry : properties.entrySet()) {
      javaProperties.put(entry.getKey(), entry.getValue());
    }
    final var outputFile = baseDir.resolve(removeStart(configuration.outputPath, "/"));
    final var encoding = Charset.forName(configuration.encoding);
    try (final var out = Files.newOutputStream(outputFile, CREATE, WRITE, TRUNCATE_EXISTING)) {
      try (final var writer = new OutputStreamWriter(out, encoding)) {
        javaProperties.store(writer, "");
      }
    }
  }

  private final static class Configuration {
    private final String encoding;
    private final String outputPath;
    @SuppressWarnings("unused") // this option is not yet supported
    private final boolean disableDefaultValues;
    @SuppressWarnings("unused") // this option is not yet supported
    private final boolean insertColumnDescriptionAsComment;
    @SuppressWarnings("unused") // this option is not yet supported
    private final boolean insertFileDescriptionAsComment;
    @SuppressWarnings("unused") // this option is not yet supported
    private final boolean writeDisabledPropertiesAsComments;

    public Configuration(String value) {
      value = value != null ? value : "";
      final var parts = value.split("|");
      this.outputPath = getString(parts, 0);
      this.insertFileDescriptionAsComment = getBoolean(parts, 1);
      this.insertColumnDescriptionAsComment = getBoolean(parts, 2);
      this.writeDisabledPropertiesAsComments = getBoolean(parts, 3);
      this.disableDefaultValues = getBoolean(parts, 4);
      this.encoding = getString(parts, 5);
    }

    private static boolean getBoolean(String[] parts, int index) {
      return Boolean.parseBoolean(getString(parts, index));
    }

    private static String getString(String[] parts, int index) {
      if (index >= parts.length) {
        return "";
      }
      return parts[index];
    }
  }
}
