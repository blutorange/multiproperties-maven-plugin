package com.github.blutorange.multiproperties_maven_plugin.generator;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.apache.maven.plugin.logging.Log;

import com.github.blutorange.multiproperties_maven_plugin.handler.Handler;
import com.github.blutorange.multiproperties_maven_plugin.handler.HandlerImplementorContextBuilder;
import com.github.blutorange.multiproperties_maven_plugin.mojo.SkipOutputMode;
import com.github.blutorange.multiproperties_maven_plugin.parser.HandlerConfiguration;
import com.github.blutorange.multiproperties_maven_plugin.parser.IMultiproperties;
import com.github.blutorange.multiproperties_maven_plugin.parser.Item;

final class DefaultHandlerImplementorContextBuilder<C extends Handler> implements HandlerImplementorContextBuilder<C> {
  String columnKey;
  C configuration;
  String defaultHandlerName;
  String fileDescription;
  String handlerConfigurationString;
  Path inputFile;
  List<Item> items = Collections.emptyList();
  Log logger;
  boolean removeFirstPathSegment;
  SkipOutputMode skipMode;
  Path sourceDir;
  Path targetDir;

  DefaultHandlerImplementorContextBuilder() {}
  
  @Override
  public DefaultHandlerImplementorContext<C> build() {
    return new DefaultHandlerImplementorContext<>(this);
  }

  @Override
  public HandlerImplementorContextBuilder<C> withColumnKey(String columnKey) {
    this.columnKey = columnKey;
    return this;
  }

  @Override
  public HandlerImplementorContextBuilder<C> withConfiguration(C configuration) {
    this.configuration = configuration;
    return this;
  }

  @Override
  public HandlerImplementorContextBuilder<C> withDefaultHandlerName(String defaultHandlerName) {
    this.defaultHandlerName = defaultHandlerName;
    return this;
  }

  @Override
  public HandlerImplementorContextBuilder<C> withFileDescription(String fileDescription) {
    this.fileDescription = fileDescription;
    return this;
  }

  @Override
  public HandlerImplementorContextBuilder<C> withHandlerConfiguration(HandlerConfiguration handlerConfiguration) {
    withColumnKey(handlerConfiguration.getColumnKey());
    withHandlerConfigurationString(handlerConfiguration.getConfigurationString());
    return this;
  }

  @Override
  public HandlerImplementorContextBuilder<C> withHandlerConfigurationString(String handlerConfigurationString) {
    this.handlerConfigurationString = handlerConfigurationString;
    return this;
  }

  @Override
  public HandlerImplementorContextBuilder<C> withInputFile(Path inputFile) {
    this.inputFile = inputFile;
    return this;
  }

  @Override
  public HandlerImplementorContextBuilder<C> withItems(List<Item> items) {
    this.items = items;
    return this;
  }

  @Override
  public HandlerImplementorContextBuilder<C> withLogger(Log logger) {
    this.logger = logger;
    return this;
  }

  @Override
  public HandlerImplementorContextBuilder<C> withMultiproperties(IMultiproperties parsed) {
    withDefaultHandlerName(parsed.getHandler());
    withFileDescription(parsed.getFileDescription());
    withItems(parsed.getItems());
    return this;
  }

  @Override
  public HandlerImplementorContextBuilder<C> withRemoveFirstPathSegment(boolean removeFirstPathSegment) {
    this.removeFirstPathSegment = removeFirstPathSegment;
    return this;
  }

  @Override
  public HandlerImplementorContextBuilder<C> withSkipMode(SkipOutputMode skipMode) {
    this.skipMode = skipMode;
    return this;
  }

  @Override
  public HandlerImplementorContextBuilder<C> withSourceDir(Path sourceDir) {
    this.sourceDir = sourceDir;
    return this;
  }

  @Override
  public HandlerImplementorContextBuilder<C> withTargetDir(Path targetDir) {
    this.targetDir = targetDir;
    return this;
  }
}