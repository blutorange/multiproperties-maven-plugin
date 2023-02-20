package com.github.blutorange.multiproperties_maven_plugin.common;

import java.nio.file.Path;
import java.util.List;

import org.apache.maven.plugin.logging.Log;

import com.github.blutorange.multiproperties_maven_plugin.handler.IOutputParams;
import com.github.blutorange.multiproperties_maven_plugin.parser.HandlerConfiguration;
import com.github.blutorange.multiproperties_maven_plugin.parser.IMultiproperties;
import com.github.blutorange.multiproperties_maven_plugin.parser.Item;

final class OutputParams implements IOutputParams {
  private final Path baseDir;
  private final String columnKey;
  private final String fileDescription;
  private final String handlerConfigurationString;
  private final List<Item> items;
  private final Log logger;
  private final boolean removeFirstPathSegment;

  public OutputParams(Log logger, Path baseDir, boolean removeFirstPathSegment, IMultiproperties parsed, HandlerConfiguration handlerConfiguration) {
    this.logger = logger;
    this.baseDir = baseDir;
    this.removeFirstPathSegment = removeFirstPathSegment;
    this.handlerConfigurationString = handlerConfiguration.getConfigurationString();
    this.columnKey = handlerConfiguration.getColumnKey();
    this.fileDescription = parsed.getFileDescription();
    this.items = parsed.getItems();
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
  public String getFileDescription() {
    return fileDescription;
  }

  @Override
  public String getHandlerConfigurationString() {
    return handlerConfigurationString;
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
  public boolean isRemoveFirstPathSegment() {
    return removeFirstPathSegment;
  }
}
