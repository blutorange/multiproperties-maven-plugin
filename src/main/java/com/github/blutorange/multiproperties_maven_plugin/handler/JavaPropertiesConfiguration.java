package com.github.blutorange.multiproperties_maven_plugin.handler;

import static com.github.blutorange.multiproperties_maven_plugin.common.StringHelper.defaultIfEmpty;
import static com.github.blutorange.multiproperties_maven_plugin.common.StringHelper.removeStart;

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
  private final boolean disableDefaultValues;

  private final String encoding;

  private final boolean insertColumnDescriptionAsComment;

  private final boolean insertFileDescriptionAsComment;

  private final String outputPath;

  private final boolean writeDisabledPropertiesAsComments;

  public JavaPropertiesConfiguration(String value) {
    value = defaultIfEmpty(value, "");
    final var parts = value.split("\\|");
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

  public String getOutputPath() {
    return removeStart(outputPath, "/");
  }

  public boolean isDisableDefaultValues() {
    return disableDefaultValues;
  }

  public boolean isInsertColumnDescriptionAsComment() {
    return insertColumnDescriptionAsComment;
  }

  public boolean isInsertFileDescriptionAsComment() {
    return insertFileDescriptionAsComment;
  }

  public boolean isWriteDisabledPropertiesAsComments() {
    return writeDisabledPropertiesAsComments;
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