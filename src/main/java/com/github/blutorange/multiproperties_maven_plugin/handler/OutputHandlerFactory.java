package com.github.blutorange.multiproperties_maven_plugin.handler;

/**
 * Factory for obtaining {@link IOutputHandler} instances.
 */
public final class OutputHandlerFactory {
  /**
   * Finds the handler instance for a given handler name.
   * @param handlerName Name of the handler.
   * @return The handler instance.
   * @throws IllegalArgumentException When no handler exists with the given name.
   */
  public static IOutputHandler forName(String handlerName) {
    if (handlerName == null) {
      throw new IllegalArgumentException("Invalid handler name: <null>");
    }
    switch (handlerName) {
      case "Java Properties Handler":
        return new JavaPropertiesHandler();
      case "Text File Handler":
        return new TextFileHandler();
      case "":
        return new NoneHandler();
      default:
        throw new IllegalArgumentException("Invalid handler name: " + handlerName);
    }
  }
}
