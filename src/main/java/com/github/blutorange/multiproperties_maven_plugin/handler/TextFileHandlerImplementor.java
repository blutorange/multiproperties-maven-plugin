package com.github.blutorange.multiproperties_maven_plugin.handler;

/**
 * Implementor for the {@link TextFileHandler}.
 */
public final class TextFileHandlerImplementor implements HandlerImplementor {
  @Override
  public String getName() {
    return "Text File Handler";
  }

  @Override
  public void handleProperties(HandlerImplementorContext params) throws Exception {
    // TODO What is the output format for the text file handler -- it appears to be broken in the Eclipse plugin...
    throw new UnsupportedOperationException("Text File Handler is not yet implemented. What is the output format for the text file handler -- it appears to be broken in the Eclipse plugin.");
  }
}
