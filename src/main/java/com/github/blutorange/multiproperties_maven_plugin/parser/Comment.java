package com.github.blutorange.multiproperties_maven_plugin.parser;

/**
 * Model for a parsed comment from the multiproperties XML.
 */
public final class Comment extends Item {
  private final String value;

  /**
   * @param value Text of the comment.
   */
  public Comment(String value) {
    this.value = value;
  }

  /**
   * @return Text of the comment.
   */
  public String getValue() {
    return value;
  }
}
