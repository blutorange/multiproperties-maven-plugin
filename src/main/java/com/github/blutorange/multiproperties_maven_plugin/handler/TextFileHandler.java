package com.github.blutorange.multiproperties_maven_plugin.handler;

final class TextFileHandler implements IOutputHandler {
  @Override
  public void handleProperties(IOutputParams params) throws Exception {
    // TODO What is the output format for the text file handler -- it appears to be broken in the Eclipse plugin...
    throw new UnsupportedOperationException("Text File Handler is not yet implemented. What is the output format for the text file handler -- it appears to be broken in the Eclipse plugin.");
  }

  @Override
  public String getName() {
    return "Text File Handler";
  }
}
