package com.github.blutorange.multiproperties_maven_plugin.mojo;

import java.util.ArrayList;
import java.util.List;

public class FileSet {
  private List<String> includes;
  private List<String> excludes;

  public List<String> getIncludes() {
    return includes != null ? includes : new ArrayList<>();
  }

  public List<String> getExcludes() {
    return excludes != null ? excludes : new ArrayList<>();
  }

  public void setIncludes(List<String> includes) {
    this.includes = includes;
  }

  public void setExcludes(List<String> excludes) {
    this.excludes = excludes;
  }
}