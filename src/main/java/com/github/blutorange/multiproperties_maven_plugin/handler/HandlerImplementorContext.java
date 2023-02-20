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
 * Parameters passed to each {@link HandlerImplementor}.
 */
public interface HandlerImplementorContext {
  /**
   * @return Base directory against which relative file paths should be resolved.
   */
  Path getBaseDir();

  /**
   * @return The {@link HandlerImplementor#getName()} of the output handler as specified in the multiproperties file.
   */
  String getDefaultHandlerName();

  /**
   * @return Key of the column for which to write the output.
   */
  String getColumnKey();

  /**
   * @return The description of the multiproperties file.
   */
  String getFileDescription();

  /**
   * @return Configuration for the handler.
   */
  String getHandlerConfigurationString();

  /**
   * @return The input file for which from which to generate an output file.
   */
  Path getInputFile();

  /**
   * @return Items to process, either a {@link Comment}, a {@link Property}, or {@link Empty}.
   */
  List<Item> getItems();

  /**
   * @return Logger for logging progress, warnings or errors.
   */
  Log getLogger();

  /**
   * @return Whether to skip recreating an output file.
   */
  SkipOutputMode getSkipMode();

  /**
   * @return <code>true</code> if the first path segment of the output file path should be removed.
   */
  boolean isRemoveFirstPathSegment();
}
