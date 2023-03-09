package com.github.blutorange.multiproperties_maven_plugin.handler;

/**
 * The {@link HandlerImplementor output handler} for {@code Simple Java Properties File}. Writes to a simple Java
 * properties file without comments and without all the quirks from the Eclipse add-on.
 */
public final class SimpleJavaPropertiesHandler extends AbstractHandler {
  private boolean disableDefaultValues;

  private String encoding;

  private boolean insertFileDescriptionAsComment;

  private String outputPath;

  /**
   * Creates a new empty Java properties handler with the default settings.
   */
  public SimpleJavaPropertiesHandler() {
    super(SimpleJavaPropertiesImplementor.class);
  }

  /**
   * @return Encoding for the output file.
   */
  public String getEncoding() {
    return encoding;
  }

  /**
   * @return Path where to write the output file.
   */
  public String getOutputPath() {
    return outputPath;
  }

  /**
   * @return Whether to treat the multiproperties file as if all default values were disabled.
   */
  public boolean isDisableDefaultValues() {
    return disableDefaultValues;
  }

  /**
   * @return Whether to insert the description of the multiproperties file as a comment at the top of the properties
   * file.
   */
  public boolean isInsertFileDescriptionAsComment() {
    return insertFileDescriptionAsComment;
  }

  /**
   * @param disableDefaultValues Whether to treat the multiproperties file as if all default values were disabled.
   */
  public void setDisableDefaultValues(boolean disableDefaultValues) {
    this.disableDefaultValues = disableDefaultValues;
  }

  /**
   * @param encoding Encoding for the output file.
   */
  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  /**
   * @param insertFileDescriptionAsComment Whether to insert the description of the multiproperties file as a comment at
   * the top of the properties file.
   */
  public void setInsertFileDescriptionAsComment(boolean insertFileDescriptionAsComment) {
    this.insertFileDescriptionAsComment = insertFileDescriptionAsComment;
  }

  /**
   * @param outputPath Path where to write the output file.
   */
  public void setOutputPath(String outputPath) {
    this.outputPath = outputPath;
  }
}