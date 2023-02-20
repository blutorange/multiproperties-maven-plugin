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
   * @param value String to pad.
   * @param length Length to which to pad.
   * @param padChar Character with which to pad.
   * @return The string with the padding char added at the beginning if the string is smaller than the target length.
   */
  public static String padLeft(String value, int length, char padChar) {
    final var sb = new StringBuilder();
    for (var i = length - value.length(); i-- > 0;) {
      sb.append(padChar);
    }
    sb.append(value);
    return sb.toString();
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
