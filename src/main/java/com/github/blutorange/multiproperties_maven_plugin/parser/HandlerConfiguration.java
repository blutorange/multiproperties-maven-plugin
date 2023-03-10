package com.github.blutorange.multiproperties_maven_plugin.parser;

/**
 * Model for a parsed handler configuration from the multiproperties XML.
 */
public final class HandlerConfiguration {
  private final String columnKey;
  private final String configurationString;

  /**
   * @param columnKey Name of the handler.
   * @param configurationString Configuration string for the handler.
   */
  public HandlerConfiguration(String columnKey, String configurationString) {
    this.columnKey = columnKey;
    this.configurationString = configurationString;
  }

  /**
   * @return Name of the handler.
   */
  public String getColumnKey() {
    return columnKey;
  }

  /**
   * @return Configuration string for the handler.
   */
  public String getConfigurationString() {
    return configurationString;
  }
}
