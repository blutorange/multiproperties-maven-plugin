package com.github.blutorange.multiproperties_maven_plugin.handler;

/**
 * Factory for obtaining {@link HandlerImplementor} instances for the default handlers supported by a multiproperties file.
 */
final class DefaultHandlerFactory {
  /**
   * Finds the handler implementor for a given handler name.
   * @param handlerName Name of the handler.
   * @return The handler instance.
   * @throws IllegalArgumentException When no handler exists with the given name.
   */
  public static HandlerImplementor<?> implementorForName(String handlerName) {
    if (handlerName == null) {
      throw new IllegalArgumentException("Invalid handler name: <null>");
    }
    switch (handlerName) {
      case "Java Properties Handler":
        return new JavaPropertiesImplementor();
      case "Text File Handler":
        return new TextFileHandlerImplementor();
      case "":
        return new NoneHandlerImplementor();
      default:
        throw new IllegalArgumentException("Invalid handler name: " + handlerName);
    }
  }
}
