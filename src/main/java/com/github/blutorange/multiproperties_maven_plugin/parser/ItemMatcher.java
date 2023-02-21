package com.github.blutorange.multiproperties_maven_plugin.parser;

/**
 * A matcher for an {@link Item}, i.e. a visitor on the constituents of a sum type.
 * @param <R> Type of the return value.
 * @param <T> Type of the exception thrown by matcher methods.
 */
public interface ItemMatcher<R, T extends Throwable> {
  /**
   * Matcher method invoked when a comment is encountered.
   * @param comment Comment to process.
   * @return Value to return to the caller.
   * @throws T Exception to propagate to the caller.
   */
  default R comment(Comment comment) throws T {
    return null;
  }

  /**
   * Matcher method invoked when an empty item is encountered.
   * @param empty Empty item to process.
   * @return Value to return to the caller.
   * @throws T Exception to propagate to the caller.
   */
  default R empty(Empty empty) throws T {
    return null;
  }

  /**
   * Matcher method invoked when a property is encountered.
   * @param property Property to process.
   * @return Value to return to the caller.
   * @throws T Exception to propagate to the caller.
   */
  default R property(Property property) throws T {
    return null;
  }

  /**
   * Matcher method invoked when an unknown item type is encountered.
   * @param item Item to process.
   * @return Value to return to the caller.
   * @throws T Exception to propagate to the caller.
   */
  default R unknown(Item item) throws T {
    return null;
  }
}
