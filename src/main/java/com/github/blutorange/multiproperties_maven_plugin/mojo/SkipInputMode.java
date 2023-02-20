package com.github.blutorange.multiproperties_maven_plugin.mojo;

/**
 * Options for when to skip processing an input file.
 */
public enum SkipInputMode {
  /** Never skip an input file. */
  NEVER("Internal error"),

  /** Skip an input file when it did not have any changes since the last build. */
  UNCHANGED("File does not have any changes"),
  ;

  private final String reason;

  private SkipInputMode(String reason) {
    this.reason = reason;
  }

  /**
   * @return Technical reason for why a file is skipped under this mode.
   */
  public String getReason() {
    return reason;
  }
}
