package com.github.blutorange.multiproperties_maven_plugin.handler;

/**
 * The {@link HandlerImplementor output handler} for {@code Java Properties File}. Writes to a Java properties file.
 */
public final class JavaPropertiesHandler extends AbstractHandler {
  private boolean disableDefaultValues;

  private String encoding;

  private boolean insertColumnDescriptionAsComment;

  private boolean insertFileDescriptionAsComment;

  private String outputPath;

  private boolean writeDisabledPropertiesAsComments;

  /**
   * Creates a new empty Java properties handler with the default settings.
   */
  public JavaPropertiesHandler() {
    super(JavaPropertiesImplementor.class);
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
   * @return Whether to insert the description of each property file as a comment above the property in the properties
   * file.
   */
  public boolean isInsertColumnDescriptionAsComment() {
    return insertColumnDescriptionAsComment;
  }

  /**
   * @return Whether to insert the description of the multiproperties file as a comment at the top of the properties
   * file.
   */
  public boolean isInsertFileDescriptionAsComment() {
    return insertFileDescriptionAsComment;
  }

  /**
   * @return Whether to include disabled properties in the properties file as a comment.
   */
  public boolean isWriteDisabledPropertiesAsComments() {
    return writeDisabledPropertiesAsComments;
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
   * @param insertColumnDescriptionAsComment Whether to insert the description of each property file as a comment above
   * the property in the properties file.
   */
  public void setInsertColumnDescriptionAsComment(boolean insertColumnDescriptionAsComment) {
    this.insertColumnDescriptionAsComment = insertColumnDescriptionAsComment;
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

  /**
   * @param writeDisabledPropertiesAsComments Whether to include disabled properties in the properties file as a
   * comment.
   */
  public void setWriteDisabledPropertiesAsComments(boolean writeDisabledPropertiesAsComments) {
    this.writeDisabledPropertiesAsComments = writeDisabledPropertiesAsComments;
  }
}