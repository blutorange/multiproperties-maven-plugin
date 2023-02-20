package com.github.blutorange.multiproperties_maven_plugin.handler;

import java.nio.file.Path;
import java.util.List;

import org.apache.maven.plugin.logging.Log;

import com.github.blutorange.multiproperties_maven_plugin.parser.Comment;
import com.github.blutorange.multiproperties_maven_plugin.parser.Empty;
import com.github.blutorange.multiproperties_maven_plugin.parser.Item;
import com.github.blutorange.multiproperties_maven_plugin.parser.Property;

/**
 * Parameters passed to each {@link IOutputHandler}.
 */
public interface IOutputParams {
  /**
   * @return Base directory against which relative file paths should be resolved.
   */
  Path getBaseDir();

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
   * @return Items to process, either a {@link Comment}, a {@link Property}, or {@link Empty}.
   */
  List<Item> getItems();

  /**
   * @return Logger for logging progress, warnings or errors.
   */
  Log getLogger();

  /**
   * @return <code>true</code> if the first path segment of the output file path should be removed.
   */
  boolean isRemoveFirstPathSegment();
}
