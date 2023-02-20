package com.github.blutorange.multiproperties_maven_plugin.common;

import java.nio.file.Path;

import org.apache.maven.plugin.logging.Log;

import com.github.blutorange.multiproperties_maven_plugin.handler.OutputHandlerFactory;
import com.github.blutorange.multiproperties_maven_plugin.mojo.SkipOutputMode;
import com.github.blutorange.multiproperties_maven_plugin.parser.MultipropertiesParser;

/**
 * Processes multiproperties files and generates the derived files. Each instance has an immutable configuration.
 */
public final class MultipropertiesGenerator {
  private final Log logger;
  private final boolean removeFirstPathSegment;
  private final SkipOutputMode skipMode;
  @SuppressWarnings("unused")
  private final Path sourceDir;
  private final Path targetDir;

  private MultipropertiesGenerator(Builder builder) {
    this.logger = builder.logger;
    this.skipMode = builder.skipMode;
    this.sourceDir = builder.sourceDir;
    this.targetDir = builder.targetDir;
    this.removeFirstPathSegment = builder.removeFirstPathSegment;
  }

  /**
   * Processes a multiproperties file by invoking the configured output handler on all defined columns.
   * @param file Multiproperties file to process.
   * @throws Exception When the file could not be processed, e.g. when the file does not exist, is invalid or the output
   * could not be written.
   */
  public void process(Path file) throws Exception {
    final var parsed = MultipropertiesParser.parse(file);
    final var handler = OutputHandlerFactory.forName(parsed.getHandler());
    final var handlerConfigurations = parsed.getHandlerConfigurations();
    logger.info(String.format("Using output handler <%s>", handler.getName()));
    for (final var handlerConfiguration : handlerConfigurations) {
      final var columnKey = handlerConfiguration.getColumnKey();
      logger.info(String.format("Processing column <%s>", columnKey));
      final var params = outputParamsBuilder() //
          .withInputFile(file) //
          .withMultiproperties(parsed) //
          .withHandlerConfiguration(handlerConfiguration) //
          .build();
      handler.handleProperties(params);
    }
  }

  private OutputParams.Builder outputParamsBuilder() {
    return OutputParams.builder() //
        .withLogger(logger) //
        .withBaseDir(targetDir) //
        .withSkipMode(skipMode) //
        .withRemoveFirstPathSegment(removeFirstPathSegment);
  }

  /**
   * @return A builder for configuring a multiproperties processor.
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * A builder for configuring a multiproperties processor.
   */
  public static final class Builder {
    private Log logger;
    private boolean removeFirstPathSegment;
    private SkipOutputMode skipMode;
    private Path sourceDir;
    private Path targetDir;

    private Builder() {}

    /**
     * @return A new generator with the current configuration.
     */
    public MultipropertiesGenerator build() {
      return new MultipropertiesGenerator(this);
    }

    /**
     * @param logger Maven logger to use for logging messages.
     * @return This builder instance for chaining method calls.
     */
    public Builder withLogger(Log logger) {
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
    public Builder withSkipMode(SkipOutputMode skipMode) {
      this.skipMode = skipMode;
      return this;
    }

    /**
     * @param sourceDir Source directory for input multiproperties files.
     * @return This builder instance for chaining method calls.
     */
    public Builder withSourceDir(Path sourceDir) {
      this.sourceDir = sourceDir;
      return this;
    }

    /**
     * @param targetDir Target directory for generated output files.
     * @return This builder instance for chaining method calls.
     */
    public Builder withTargetDir(Path targetDir) {
      this.targetDir = targetDir;
      return this;
    }
  }
}
