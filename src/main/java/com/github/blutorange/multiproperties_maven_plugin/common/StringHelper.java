package com.github.blutorange.multiproperties_maven_plugin.common;

/**
 * Utility methods for strings.
 */
public class StringHelper {
  /**
   * @param value Value to check.
   * @param defaultValue Default value to use.
   * @return The value if the string is neither <code>null</code> nor empty, the default value otherwise.
   */
  public static String defaultIfEmpty(String value, String defaultValue) {
    return isNotEmpty(value) ? value : defaultValue;
  }

  /**
   * @param value Value to check.
   * @return <code>true</code> if the value is <code>null</code> or empty.
   */
  public static boolean isEmpty(String value) {
    return value == null || value.isEmpty();
  }

  /**
   * @param value Value to check.
   * @return <code>true</code> if the value is neither <code>null</code> nor empty.
   */
  public static boolean isNotEmpty(String value) {
    return value != null && !value.isEmpty();
  }

  /**
   * Removes the prefix from the beginning of the string, if it has that prefix.
   * @param value String to process.
   * @param prefix Prefix to remove.
   * @return The given string with the given prefix removed
   */
  public static String removeStart(String value, String prefix) {
    if (value == null) {
      return "";
    }
    if (prefix == null) {
      return prefix;
    }
    return value.startsWith(prefix) ? value.substring(prefix.length()) : value;
  }
}
