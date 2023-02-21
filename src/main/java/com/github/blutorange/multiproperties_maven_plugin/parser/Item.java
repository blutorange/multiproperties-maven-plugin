package com.github.blutorange.multiproperties_maven_plugin.parser;

/**
 * Either a {@link Comment}, a {@link Property}, or {@link Empty}.
 */
public class Item {
  /**
   * Performs a pattern match on this item type. Invokes the corresponding method of the method and returns the result.
   * @param <R> Type of the return value.
   * @param matcher Matcher to apply to this item.
   * @return The value as returned by the matcher.
   * @throws T Type of the exception thrown by the matcher methods.
   */
  public final <R, T extends Throwable> R match(ItemMatcher<R, T> matcher) throws T {
    if (this instanceof Property) {
      return matcher.property((Property)this);
    }
    if (this instanceof Comment) {
      return matcher.comment((Comment)this);
    }
    if (this instanceof Empty) {
      return matcher.empty((Empty)this);
    }
    return matcher.unknown(this);
  }
}
