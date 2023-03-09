package com.github.blutorange.multiproperties_maven_plugin.handler;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

import java.nio.charset.StandardCharsets;

final class HandlerConfigurationParser {
  private HandlerConfigurationParser() {}

  public static DefaultHandler defaultHandler(String value) {
    value = defaultIfEmpty(value, "");
    final var config = new DefaultHandler();
    return config;
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
  /// fc-backend-common/src/main/resources/i18n/fc_de.properties|false|true|true|false|ISO-8859-1
  public static JavaPropertiesHandler javaProperties(String value) {
    value = defaultIfEmpty(value, "");

    final var config = new JavaPropertiesHandler();
    final var parts = value.split("\\|");
    config.setOutputPath(getString(parts, 0));
    config.setInsertFileDescriptionAsComment(getBoolean(parts, 1));
    config.setInsertColumnDescriptionAsComment(getBoolean(parts, 2));
    config.setWriteDisabledPropertiesAsComments(getBoolean(parts, 3));
    config.setDisableDefaultValues(getBoolean(parts, 4));
    config.setEncoding(getString(parts, 5, StandardCharsets.ISO_8859_1.name()));

    return config;
  }

  public static NoneHandler none(String value) {
    value = defaultIfEmpty(value, "");
    final var config = new NoneHandler();
    return config;
  }

  public static SimpleJavaPropertiesHandler simpleJavaProperties(String value) {
    value = defaultIfEmpty(value, "");

    final var config = new SimpleJavaPropertiesHandler();
    final var parts = value.split("\\|");
    config.setOutputPath(getString(parts, 0));
    config.setInsertFileDescriptionAsComment(getBoolean(parts, 1));
    config.setDisableDefaultValues(getBoolean(parts, 4));
    config.setEncoding(getString(parts, 5, StandardCharsets.ISO_8859_1.name()));

    return config;
  }

  public static TextFileHandler textFile(String value) {
    value = defaultIfEmpty(value, "");
    final var config = new TextFileHandler();
    return config;
  }

  private static boolean getBoolean(String[] parts, int index) {
    return getBoolean(parts, index, false);
  }

  private static boolean getBoolean(String[] parts, int index, boolean defaultValue) {
    return Boolean.parseBoolean(getString(parts, index, Boolean.toString(defaultValue)));
  }

  private static String getString(String[] parts, int index) {
    return getString(parts, index, "");
  }

  private static String getString(String[] parts, int index, String defaultValue) {
    if (index >= parts.length) {
      return defaultValue;
    }
    return defaultIfEmpty(parts[index], defaultValue);
  }
}
