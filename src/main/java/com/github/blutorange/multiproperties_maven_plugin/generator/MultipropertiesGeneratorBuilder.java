package com.github.blutorange.multiproperties_maven_plugin.generator;

import java.nio.file.Path;
import java.util.List;

import org.apache.maven.plugin.logging.Log;

import com.github.blutorange.multiproperties_maven_plugin.handler.Handler;
import com.github.blutorange.multiproperties_maven_plugin.mojo.SkipOutputMode;

/**
 * A builder for configuring a multiproperties processor.
 */
public final class MultipropertiesGeneratorBuilder {
  List<Handler> handlers;
  Log logger;
  boolean removeFirstPathSegment;
  SkipOutputMode skipMode;
  Path sourceDir;
  Path targetDir;

  MultipropertiesGeneratorBuilder() {}

  /**
   * @return A new generator with the current configuration.
   */
  public MultipropertiesGenerator build() {
    return new MultipropertiesGenerator(this);
  }

  /**
   * @param handlers Handlers to use for creating derived output files.
   * @return This builder instance for chaining method calls.
   */
  public MultipropertiesGeneratorBuilder withHandlers(List<Handler> handlers) {
    this.handlers = handlers;
    return this;
  }

  /**
   * @param logger Maven logger to use for logging messages.
   * @return This builder instance for chaining method calls.
   */
  public MultipropertiesGeneratorBuilder withLogger(Log logger) {
    this.logger = logger;
    return this;
  }

  /**
   * @param removeFirstPathSegment When <code>true</code>, removes the first path segment of each output file path.
   */
  public void withRemoveFirstPathSegment(boolean removeFirstPathSegment) {
    this.removeFirstPathSegment = removeFirstPathSegment;
  }

  /**
   * @param skipMode Whether to skip recreating an output file.
   * @return This builder instance for chaining method calls.
   */
  public MultipropertiesGeneratorBuilder withSkipMode(SkipOutputMode skipMode) {
    this.skipMode = skipMode;
    return this;
  }

  /**
   * @param sourceDir Source directory for input multiproperties files.
   * @return This builder instance for chaining method calls.
   */
  public MultipropertiesGeneratorBuilder withSourceDir(Path sourceDir) {
    this.sourceDir = sourceDir;
    return this;
  }

  /**
   * @param targetDir Target directory for generated output files.
   * @return This builder instance for chaining method calls.
   */
  public MultipropertiesGeneratorBuilder withTargetDir(Path targetDir) {
    this.targetDir = targetDir;
    return this;
  }
}