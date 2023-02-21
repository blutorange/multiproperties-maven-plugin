package com.github.blutorange.multiproperties_maven_plugin.handler;

import static com.github.blutorange.multiproperties_maven_plugin.handler.HandlerConfigurationParser.textFile;

/**
 * Implementor for the {@link TextFileHandler}.
 */
public final class TextFileHandlerImplementor implements HandlerImplementor<TextFileHandler> {
  @Override
  public String getName() {
    return "Text File Handler";
  }

  @Override
  public void handleProperties(HandlerImplementorContext<TextFileHandler> context) throws Exception {
    // TODO What is the output format for the text file handler -- it appears to be broken in the Eclipse plugin...
    throw new UnsupportedOperationException("Text File Handler is not yet implemented. What is the output format for the text file handler -- it appears to be broken in the Eclipse plugin.");
  }

  @Override
  public TextFileHandler parseConfig(String configuration) {
    return textFile(configuration);
  }
}
