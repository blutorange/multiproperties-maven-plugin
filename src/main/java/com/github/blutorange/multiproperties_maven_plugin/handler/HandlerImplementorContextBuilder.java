package com.github.blutorange.multiproperties_maven_plugin.handler;

import java.nio.file.Path;
import java.util.List;

import org.apache.maven.plugin.logging.Log;

import com.github.blutorange.multiproperties_maven_plugin.mojo.SkipOutputMode;
import com.github.blutorange.multiproperties_maven_plugin.parser.HandlerConfiguration;
import com.github.blutorange.multiproperties_maven_plugin.parser.IMultiproperties;
import com.github.blutorange.multiproperties_maven_plugin.parser.Item;

public interface HandlerImplementorContextBuilder<C extends Handler> {
  HandlerImplementorContext<C> build();

  HandlerImplementorContextBuilder<C> withColumnKey(String columnKey);

  HandlerImplementorContextBuilder<C> withConfiguration(C configuration);

  HandlerImplementorContextBuilder<C> withDefaultHandlerName(String defaultHandlerName);

  HandlerImplementorContextBuilder<C> withFileDescription(String fileDescription);

  HandlerImplementorContextBuilder<C> withHandlerConfiguration(HandlerConfiguration handlerConfiguration);

  HandlerImplementorContextBuilder<C> withHandlerConfigurationString(String handlerConfigurationString);

  HandlerImplementorContextBuilder<C> withInputFile(Path inputFile);

  HandlerImplementorContextBuilder<C> withItems(List<Item> items);

  HandlerImplementorContextBuilder<C> withLogger(Log logger);

  HandlerImplementorContextBuilder<C> withMultiproperties(IMultiproperties parsed);

  HandlerImplementorContextBuilder<C> withRemoveFirstPathSegment(boolean removeFirstPathSegment);

  HandlerImplementorContextBuilder<C> withSkipMode(SkipOutputMode skipMode);

  HandlerImplementorContextBuilder<C> withSourceDir(Path sourceDir);

  HandlerImplementorContextBuilder<C> withTargetDir(Path targetDir);
}