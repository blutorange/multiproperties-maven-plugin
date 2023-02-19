package com.github.blutorange.multiproperties_maven_plugin.handler;

import static com.github.blutorange.multiproperties_maven_plugin.common.StringHelper.defaultIfEmpty;

import java.nio.charset.Charset;

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
final class JavaPropertiesConfiguration {
  private final String encoding;

  private final String outputPath;

  // TODO
  private final boolean disableDefaultValues;

  @SuppressWarnings("unused") // this option is not yet supported
  private final boolean insertColumnDescriptionAsComment;

  @SuppressWarnings("unused") // this option is not yet supported
  private final boolean insertFileDescriptionAsComment;

  @SuppressWarnings("unused") // this option is not yet supported
  private final boolean writeDisabledPropertiesAsComments;

  public JavaPropertiesConfiguration(String value) {
    value = defaultIfEmpty(value, "");
    final var parts = value.split("|");
    this.outputPath = getString(parts, 0);
    this.insertFileDescriptionAsComment = getBoolean(parts, 1);
    this.insertColumnDescriptionAsComment = getBoolean(parts, 2);
    this.writeDisabledPropertiesAsComments = getBoolean(parts, 3);
    this.disableDefaultValues = getBoolean(parts, 4);
    this.encoding = getString(parts, 5);
  }

  public Charset getEncoding() {
    return Charset.forName(encoding);
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

  public String getOutputPath() {
    return outputPath;
  }
}