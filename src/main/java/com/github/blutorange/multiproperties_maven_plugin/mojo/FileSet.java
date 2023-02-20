package com.github.blutorange.multiproperties_maven_plugin.mojo;

import java.util.ArrayList;
import java.util.List;

/**
 * A set of files with multiple include and exclude glob patterns that control which files will match. Should behave the
 * same as e.g. the Maven POM resources section.
 */
public class FileSet {
  private boolean caseInsensitive;
  private String directory;
  private List<String> excludes;
  private List<String> extensions;
  private List<String> includes;
  private boolean skipDefaultExcludes;

  /**
   * @return Directory in which to look for files. Relative paths are interpreted depending on the context where the
   * file set is used.
   */
  public String getDirectory() {
    return directory;
  }

  /**
   * @return Glob pattern for files to exclude.
   */
  public List<String> getExcludes() {
    return excludes != null ? excludes : new ArrayList<>();
  }

  /**
   * @return When not empty, limit the matching files to files with one of these extensions. May or may not contain a
   * leading dot.
   */
  public List<String> getExtensions() {
    return extensions;
  }

  /**
   * @return Glob pattern for files to include.
   */
  public List<String> getIncludes() {
    return includes != null ? includes : new ArrayList<>();
  }

  /**
   * @return When <code>true</code>, ignore case when searching for matching files. Defaults to <code>false</code>.
   */
  public boolean isCaseInsensitive() {
    return caseInsensitive;
  }

  /**
   * @return When <code>true</code>, do not add the default exclude pattern for system files.
   */
  public boolean isSkipDefaultExcludes() {
    return skipDefaultExcludes;
  }

  /**
   * @param caseInsensitive When <code>true</code>, ignore case when searching for matching files. Defaults to
   * <code>false</code>.
   */
  public void setCaseInsensitive(boolean caseInsensitive) {
    this.caseInsensitive = caseInsensitive;
  }

  /**
   * @param directory Directory in which to look for files. Relative paths are interpreted depending on the context
   * where the file set is used.
   */
  public void setDirectory(String directory) {
    this.directory = directory;
  }

  /**
   * @param excludes Glob pattern for files to exclude.
   */
  public void setExcludes(List<String> excludes) {
    this.excludes = excludes;
  }

  /**
   * @param extensions When not empty, limit the matching files to files with one of these extensions. May or may not
   * contain a leading dot.
   */
  public void setExtensions(List<String> extensions) {
    this.extensions = extensions;
  }

  /**
   * @param includes Glob pattern for files to include.
   */
  public void setIncludes(List<String> includes) {
    this.includes = includes;
  }

  /**
   * @param skipDefaultExcludes When <code>true</code>, do not add the default exclude pattern for system files.
   */
  public void setSkipDefaultExcludes(boolean skipDefaultExcludes) {
    this.skipDefaultExcludes = skipDefaultExcludes;
  }

  @Override
  public String toString() {
    return String.format( //
        "FileSet[directory=%s,includes=%s,excludes=%s,extensions=%s,%s]", //
        directory, includes, excludes, extensions, caseInsensitive ? "caseInsensitive" : "caseSensitive" //
    );
  }
}