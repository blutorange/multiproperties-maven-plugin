package com.github.blutorange.multiproperties_maven_plugin.handler;

import java.nio.file.Path;
import java.util.List;

import org.apache.maven.plugin.logging.Log;

import com.github.blutorange.multiproperties_maven_plugin.mojo.SkipOutputMode;
import com.github.blutorange.multiproperties_maven_plugin.parser.Comment;
import com.github.blutorange.multiproperties_maven_plugin.parser.Empty;
import com.github.blutorange.multiproperties_maven_plugin.parser.Item;
import com.github.blutorange.multiproperties_maven_plugin.parser.Property;

/**
 * A builder for configuring a handler implementation context passed to each handler when it is time to generate the
 * output file.
 * @param <C> Type of the handler configuration.
 */
public interface HandlerImplementorContextBuilder<C extends Handler> {
  /**
   * @return A new context with the currently configured settings.
   */
  HandlerImplementorContext<C> build();

  /**
   * @param columnKey The name of the column for which to generate an output file.
   * @return This builder instance for chaining method calls.
   */
  HandlerImplementorContextBuilder<C> withColumnKey(String columnKey);

  /**
   * @param configuration The configuration for the handler.
   * @return This builder instance for chaining method calls.
   */
  HandlerImplementorContextBuilder<C> withConfiguration(C configuration);

  /**
   * @param handlerConfigurationString The configuration string for the handler from the multiproperties file, if any.
   * @return This builder instance for chaining method calls.
   */
  HandlerImplementorContextBuilder<C> withDefaultHandlerConfigurationString(String handlerConfigurationString);

  /**
   * @param defaultHandlerName The {@link HandlerImplementor#getName()} of the output handler as specified in the
   * multiproperties file.
   * @return This builder instance for chaining method calls.
   */
  HandlerImplementorContextBuilder<C> withDefaultHandlerName(String defaultHandlerName);

  /**
   * @param fileDescription The description from the multiproperties file.
   * @return This builder instance for chaining method calls.
   */
  HandlerImplementorContextBuilder<C> withFileDescription(String fileDescription);

  /**
   * @param inputFile The input file for which from which to generate an output file.
   * @return This builder instance for chaining method calls.
   */
  HandlerImplementorContextBuilder<C> withInputFile(Path inputFile);

  /**
   * @param items Items to process, either a {@link Comment}, a {@link Property}, or {@link Empty}.
   * @return This builder instance for chaining method calls.
   */
  HandlerImplementorContextBuilder<C> withItems(List<Item> items);

  /**
   * @param logger Logger for logging progress, warnings or errors.
   * @return This builder instance for chaining method calls.
   */
  HandlerImplementorContextBuilder<C> withLogger(Log logger);

  /**
   * @param removeFirstPathSegment <code>true</code> if the first path segment of the output file path should be
   * removed.
   * @return This builder instance for chaining method calls.
   */
  HandlerImplementorContextBuilder<C> withRemoveFirstPathSegment(boolean removeFirstPathSegment);

  /**
   * @param skipMode Whether to skip recreating an output file.
   * @return This builder instance for chaining method calls.
   */
  HandlerImplementorContextBuilder<C> withSkipMode(SkipOutputMode skipMode);

  /**
   * @param sourceDir Source directory from which multiproperties files are read.
   * @return This builder instance for chaining method calls.
   */
  HandlerImplementorContextBuilder<C> withSourceDir(Path sourceDir);

  /**
   * @param targetDir Base directory against which relative output file paths should be resolved.
   * @return This builder instance for chaining method calls.
   */
  HandlerImplementorContextBuilder<C> withTargetDir(Path targetDir);
}