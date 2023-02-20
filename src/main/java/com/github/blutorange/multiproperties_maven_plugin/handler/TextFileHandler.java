package com.github.blutorange.multiproperties_maven_plugin.handler;

/**
 * The {@link HandlerImplementor output handler} for {@code Text File Handler}. Outputs to a text file.
 */
public final class TextFileHandler extends AbstractHandler {
  protected TextFileHandler() {
    super(TextFileHandlerImplementor.class);
  }
}
