package com.github.blutorange.multiproperties_maven_plugin.parser;

import java.util.Map;

/**
 * Model for a parsed property from the multiproperties XML.
 */
public final class Property extends Item {
  private final String defaultValue;
  private final boolean disabled;
  private final boolean multiline;
  private final String name;
  private final Map<String, String> valueMap;

  /**
   * @param name The name of the property.
   * @param disabled Whether the property is disabled.
   * @param defaultValue Default value to use when no value exists for a column.
   * @param valueMap Map with the values for each column. The key is the column key, the value the value for that
   * column.
   * @param multiline Whether the property value may span multiple lines.
   */
  public Property(String name, boolean disabled, String defaultValue, Map<String, String> valueMap, boolean multiline) {
    this.name = name;
    this.disabled = disabled;
    this.defaultValue = defaultValue;
    this.valueMap = valueMap;
    this.multiline = multiline;
  }

  /**
   * @return Default value to use when no value exists for a column.
   */
  public String getDefaultValue() {
    return defaultValue;
  }

  /**
   * @return The name of the property.
   */
  public String getName() {
    return name;
  }

  /**
   * @param columnKey Key of a column.
   * @return The resolved value for the column, either the value if present or the default value. <code>null</code> if
   * neither a value nor a default exists.
   */
  public String getResolvedValue(String columnKey) {
    return valueMap.getOrDefault(columnKey, defaultValue);
  }

  /**
   * @param columnKey Key of a column.
   * @return The value for the column, may be <code>null</code>.
   */
  public String getValue(String columnKey) {
    return valueMap.get(columnKey);
  }
  
  /**
   * @return Map with the values for each column. The key is the column key, the value the value for that column.
   */
  public Map<String, String> getValueMap() {
    return valueMap;
  }

  /**
   * @return Whether the property is disabled.
   */
  public boolean isDisabled() {
    return disabled;
  }

  /**
   * @return Whether the property value may span multiple lines.
   */
  public boolean isMultiline() {
    return multiline;
  }
}