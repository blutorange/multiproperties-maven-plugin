package com.github.blutorange.multiproperties_maven_plugin.generator;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import org.apache.maven.plugin.logging.Log;

import com.github.blutorange.multiproperties_maven_plugin.common.FileHelper;
import com.github.blutorange.multiproperties_maven_plugin.handler.Handler;
import com.github.blutorange.multiproperties_maven_plugin.handler.HandlerImplementorContext;
import com.github.blutorange.multiproperties_maven_plugin.handler.HandlerImplementorContextBuilder;
import com.github.blutorange.multiproperties_maven_plugin.mojo.SkipOutputMode;
import com.github.blutorange.multiproperties_maven_plugin.parser.Item;

final class DefaultHandlerImplementorContext<C extends Handler> implements HandlerImplementorContext<C> {
  private final String columnKey;
  private final C configuration;
  private final String defaultHandlerName;
  private final String fileDescription;
  private final String handlerConfigurationString;
  private final Path inputFile;
  private final List<Item> items;
  private final Log logger;
  private final boolean removeFirstPathSegment;
  private final SkipOutputMode skipMode;
  private final Path sourceDir;
  private final Path targetDir;

  DefaultHandlerImplementorContext(DefaultHandlerImplementorContextBuilder<C> builder) {
    this.targetDir = builder.targetDir;
    this.sourceDir = builder.sourceDir;
    this.columnKey = builder.columnKey;
    this.configuration = builder.configuration;
    this.defaultHandlerName = builder.defaultHandlerName;
    this.fileDescription = builder.fileDescription;
    this.handlerConfigurationString = builder.handlerConfigurationString;
    this.items = builder.items;
    this.logger = builder.logger;
    this.removeFirstPathSegment = builder.removeFirstPathSegment;
    this.skipMode = builder.skipMode;
    this.inputFile = builder.inputFile;
  }

  @Override
  public <T extends Handler> HandlerImplementorContextBuilder<T> derive(T configuration) {
    return new DefaultHandlerImplementorContextBuilder<T>() //
        .withTargetDir(targetDir) //
        .withSourceDir(sourceDir) //
        .withColumnKey(columnKey) //
        .withDefaultHandlerName(defaultHandlerName) //
        .withDefaultHandlerName(defaultHandlerName) //
        .withFileDescription(fileDescription) //
        .withHandlerConfigurationString(handlerConfigurationString) //
        .withItems(items) //
        .withLogger(logger) //
        .withRemoveFirstPathSegment(removeFirstPathSegment) //
        .withSkipMode(skipMode) //
        .withInputFile(inputFile) //
        .withConfiguration(configuration);
  }

  @Override
  public String getColumnKey() {
    return columnKey;
  }

  @Override
  public C getConfiguration() {
    return configuration;
  }

  @Override
  public String getDefaultHandlerConfigurationString() {
    return handlerConfigurationString;
  }

  @Override
  public String getDefaultHandlerName() {
    return defaultHandlerName;
  }

  @Override
  public String getFileDescription() {
    return fileDescription;
  }

  @Override
  public Path getInputFile() {
    return inputFile;
  }

  @Override
  public List<Item> getItems() {
    return items;
  }

  @Override
  public Log getLogger() {
    return logger;
  }

  @Override
  public SkipOutputMode getSkipMode() {
    return skipMode;
  }

  @Override
  public Path getSourceDir() {
    return sourceDir;
  }

  @Override
  public Path getTargetDir() {
    return targetDir;
  }

  @Override
  public Path interpolateFilename(Path outputPath) throws IOException {
    final var interpolator = new FilenameInterpolator(outputPath.toString());
    final var data = new HashMap<String, String>();
    data.put("key", columnKey);
    return interpolator.interpolate(inputFile, sourceDir, targetDir, data);
  }

  @Override
  public Path interpolateFilename(String path) throws IOException {
    return interpolateFilename(Paths.get(path));
  }

  @Override
  public boolean isRemoveFirstPathSegment() {
    return removeFirstPathSegment;
  }

  @Override
  public boolean shouldSkipOutput(Path outputFile) throws IOException {
    return FileHelper.shouldSkipOutput(inputFile, outputFile, skipMode);
  }

  public static <C extends Handler> HandlerImplementorContextBuilder<C> builder() {
    return new DefaultHandlerImplementorContextBuilder<C>();
  }
}
