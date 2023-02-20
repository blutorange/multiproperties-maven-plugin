package com.github.blutorange.multiproperties_maven_plugin.mojo;

import java.util.ArrayList;
import java.util.List;

/**
 * A set of files with multiple include and exclude glob patterns that control which files will match. Should behave the
 * same as e.g. the Maven POM resources section.
 */
public class FileSet {
  private List<String> excludes;
  private List<String> includes;

  /**
   * @return Glob pattern for files to exclude.
   */
  public List<String> getExcludes() {
    return excludes != null ? excludes : new ArrayList<>();
  }

  /**
   * @return Glob pattern for files to include.
   */
  public List<String> getIncludes() {
    return includes != null ? includes : new ArrayList<>();
  }

  /**
   * @param excludes Glob pattern for files to exclude.
   */
  public void setExcludes(List<String> excludes) {
    this.excludes = excludes;
  }

  /**
   * @param includes Glob pattern for files to include.
   */
  public void setIncludes(List<String> includes) {
    this.includes = includes;
  }
}