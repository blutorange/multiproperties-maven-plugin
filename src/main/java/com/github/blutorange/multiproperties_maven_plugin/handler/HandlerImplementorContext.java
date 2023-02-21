package com.github.blutorange.multiproperties_maven_plugin.handler;

import java.io.IOException;
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
 * @param <C> Type of the handler configuration.
 */
public interface HandlerImplementorContext<C> {
  /**
   * Derives a context for a different handler.
   * @param <T> Type of the new handler configuration.
   * @param configuration New handler configuration.
   * @return A context builder for a new handler configuration, initialized with the current data.
   */
  <T extends Handler> HandlerImplementorContextBuilder<T> derive(T configuration);

  /**
   * @return Key of the column for which to write the output.
   */
  String getColumnKey();

  /**
   * @return The configuration for the handler.
   */
  C getConfiguration();

  /**
   * @return The configuration string for the handler from the multiproperties file.
   */
  String getDefaultHandlerConfigurationString();

  /**
   * @return The {@link HandlerImplementor#getName()} of the output handler as specified in the multiproperties file.
   */
  String getDefaultHandlerName();

  /**
   * @return The description of the multiproperties file.
   */
  String getFileDescription();

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
   * @return Source directory from which multiproperties files are read.
   */
  Path getSourceDir();

  /**
   * @return Base directory against which relative file paths should be resolved.
   */
  Path getTargetDir();

  /**
   * @param path Path to interpolate.
   * @return The path resolved against the target directory and with placeholders replaced.
   * @throws IOException When the file system could not be accessed.
   */
  Path interpolateFilename(Path path) throws IOException;

  /**
   * @param path Path to interpolate.
   * @return The path resolved against the target directory and with placeholders replaced.
   * @throws IOException When the file system could not be accessed.
   */
  Path interpolateFilename(String path) throws IOException;

  /**
   * @return <code>true</code> if the first path segment of the output file path should be removed.
   */
  boolean isRemoveFirstPathSegment();

  /**
   * @param outputFile Output file to check.
   * @return <code>true</code> if the output should be skipped and not be regenerated from the input.
   * @throws IOException When the file could not accessed.
   */
  boolean shouldSkipOutput(Path outputFile) throws IOException;
}
