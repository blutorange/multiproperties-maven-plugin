package com.github.blutorange.multiproperties_maven_plugin.mojo;

/**
 * Options for when to skip recreating an output file.
 */
public enum SkipOutputMode {
  /** Do not recreate an output file when it exists already, irrespective of the modification date. */
  EXISTS("File exists already"),

  /** Never skip recreating an output file. */
  NEVER("Internal error"),

  /** Do not recreate an output file when it is newer (more recently modified) that all input files. */
  NEWER("File is newer than input file"),;

  private final String reason;

  private SkipOutputMode(String reason) {
    this.reason = reason;
  }

  /**
   * @return Technical reason for why a file is skipped under this mode.
   */
  public String getReason() {
    return reason;
  }
}
