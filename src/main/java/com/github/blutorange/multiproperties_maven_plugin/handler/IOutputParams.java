package com.github.blutorange.multiproperties_maven_plugin.handler;

import java.nio.file.Path;
import java.util.Map;

import org.apache.maven.plugin.logging.Log;

/**
 * Parameters passed to each {@link IOutputHandler}.
 */
public interface IOutputParams {
  /**
   * @return Base directory against which relative file paths should be resolved.
   */
  Path getBaseDir();

  /**
   * @return Configuration for the handler.
   */
  String getConfiguration();

  /**
   * @return Logger for logging progress, warnings or errors.
   */
  Log getLogger();

  /**
   * @return Properties to process. The key is the name of the property, the value is the value of the property.
   */
  Map<String, String> getProperties();
}
