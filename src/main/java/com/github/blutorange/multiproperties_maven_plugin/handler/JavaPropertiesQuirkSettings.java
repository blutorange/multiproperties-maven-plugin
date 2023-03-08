package com.github.blutorange.multiproperties_maven_plugin.handler;

final class JavaPropertiesQuirkSettings {
  private boolean skipCommentingMultiLines;
  private boolean skipEscapingBackslash;
  private boolean writeQuestionMarksInsteadOfProperlyEscapingChars;

  private JavaPropertiesQuirkSettings(Builder builder) {
    this.skipCommentingMultiLines = builder.skipCommentingMultiLines;
    this.skipEscapingBackslash = builder.skipEscapingBackslash;
    this.writeQuestionMarksInsteadOfProperlyEscapingChars = builder.writeQuestionMarksInsteadOfProperlyEscapingChars;
  }

  public boolean skipCommentingMultiLines() {
    return skipCommentingMultiLines;
  }

  public boolean skipEscapingBackslash() {
    return skipEscapingBackslash;
  }

  public boolean writeQuestionMarksInsteadOfProperlyEscapingChars() {
    return writeQuestionMarksInsteadOfProperlyEscapingChars;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private boolean skipCommentingMultiLines;
    private boolean skipEscapingBackslash;
    private boolean writeQuestionMarksInsteadOfProperlyEscapingChars;

    private Builder() {}

    public Builder skipCommentingMultiLines(boolean skipCommentingMultiLines) {
      this.skipCommentingMultiLines = skipCommentingMultiLines;
      return this;
    }

    public Builder skipEscapingBackslash(boolean skipEscapingBackslash) {
      this.skipEscapingBackslash = skipEscapingBackslash;
      return this;
    }

    public Builder writeQuestionMarksInsteadOfProperlyEscapingChars(boolean writeQuestionMarksInsteadOfProperlyEscapingChars) {
      this.writeQuestionMarksInsteadOfProperlyEscapingChars = writeQuestionMarksInsteadOfProperlyEscapingChars;
      return this;
    }

    public JavaPropertiesQuirkSettings build() {
      return new JavaPropertiesQuirkSettings(this);
    }
  }
}
