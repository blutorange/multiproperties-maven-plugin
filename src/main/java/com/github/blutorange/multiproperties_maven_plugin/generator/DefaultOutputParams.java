package com.github.blutorange.multiproperties_maven_plugin.generator;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.apache.maven.plugin.logging.Log;

import com.github.blutorange.multiproperties_maven_plugin.handler.HandlerImplementorContext;
import com.github.blutorange.multiproperties_maven_plugin.mojo.SkipOutputMode;
import com.github.blutorange.multiproperties_maven_plugin.parser.HandlerConfiguration;
import com.github.blutorange.multiproperties_maven_plugin.parser.IMultiproperties;
import com.github.blutorange.multiproperties_maven_plugin.parser.Item;

final class DefaultOutputParams implements HandlerImplementorContext {
  private final Path baseDir;
  private final String columnKey;
  private final String defaultHandlerName;
  private final String fileDescription;
  private final String handlerConfigurationString;
  private final Path inputFile;
  private final List<Item> items;
  private final Log logger;
  private final boolean removeFirstPathSegment;
  private final SkipOutputMode skipMode;

  private DefaultOutputParams(Builder builder) {
    this.baseDir = builder.baseDir;
    this.columnKey = builder.columnKey;
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
  public Path getBaseDir() {
    return baseDir;
  }

  @Override
  public String getColumnKey() {
    return columnKey;
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
  public String getHandlerConfigurationString() {
    return handlerConfigurationString;
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
  public boolean isRemoveFirstPathSegment() {
    return removeFirstPathSegment;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    public String defaultHandlerName;
    private Path baseDir;
    private String columnKey;
    private String fileDescription;
    private String handlerConfigurationString;
    private Path inputFile;
    private List<Item> items = Collections.emptyList();
    private Log logger;
    private boolean removeFirstPathSegment;
    private SkipOutputMode skipMode;

    private Builder() {}

    public DefaultOutputParams build() {
      return new DefaultOutputParams(this);
    }

    public Builder withBaseDir(Path baseDir) {
      this.baseDir = baseDir;
      return this;
    }

    public Builder withColumnKey(String columnKey) {
      this.columnKey = columnKey;
      return this;
    }

    public Builder withDefaultHandlerName(String defaultHandlerName) {
      this.defaultHandlerName = defaultHandlerName;
      return this;
    }

    public Builder withFileDescription(String fileDescription) {
      this.fileDescription = fileDescription;
      return this;
    }

    public Builder withHandlerConfiguration(HandlerConfiguration handlerConfiguration) {
      withColumnKey(handlerConfiguration.getColumnKey());
      withHandlerConfigurationString(handlerConfiguration.getConfigurationString());
      return this;
    }

    public Builder withHandlerConfigurationString(String handlerConfigurationString) {
      this.handlerConfigurationString = handlerConfigurationString;
      return this;
    }

    public Builder withInputFile(Path inputFile) {
      this.inputFile = inputFile;
      return this;
    }

    public Builder withItems(List<Item> items) {
      this.items = items;
      return this;
    }

    public Builder withLogger(Log logger) {
      this.logger = logger;
      return this;
    }

    public Builder withMultiproperties(IMultiproperties parsed) {
      withDefaultHandlerName(parsed.getHandler());
      withFileDescription(parsed.getFileDescription());
      withItems(parsed.getItems());
      return this;
    }

    public Builder withRemoveFirstPathSegment(boolean removeFirstPathSegment) {
      this.removeFirstPathSegment = removeFirstPathSegment;
      return this;
    }

    public Builder withSkipMode(SkipOutputMode skipMode) {
      this.skipMode = skipMode;
      return this;
    }
  }
}
