package com.github.blutorange.multiproperties_maven_plugin.common;

/**
 * Utility methods for working with general objects.
 */
public final class ObjectHelper {
  private ObjectHelper() {}

  /**
   * Returns the value when it is not <code>null</code> or the default value otherwise.
   * @param <T> Type of the value.
   * @param value Value to check.
   * @param defaultValue Default to use when value is <code>null</code>.
   * @return The value if it not <code>null</code>, or the default otherwise.
   */
  public static <T> T defaultIfNull(T value, T defaultValue) {
    return value != null ? value : defaultValue;
  }
}
