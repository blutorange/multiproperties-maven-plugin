package com.github.blutorange.multiproperties_maven_plugin.handler;

import static com.github.blutorange.multiproperties_maven_plugin.common.StringHelper.defaultIfEmpty;

final class HandlerConfigurationParser {
  private HandlerConfigurationParser() {}

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
    config.setEncoding(getString(parts, 5));

    return config;
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
