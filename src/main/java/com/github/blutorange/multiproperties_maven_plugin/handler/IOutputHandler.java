package com.github.blutorange.multiproperties_maven_plugin.handler;

import java.nio.file.Path;
import java.util.Map;

/**
 * An output handler of a multiproperties file. A handler may e.g. write the properties to a Java properties file.
 */
public interface IOutputHandler {
  /**
   * @return The name of this handler.
   */
  String getName();

  /**
   * Handles the given properties, e.g. by writing the properties to a file.
   * @param configuration Configuration for the handler.
   * @param properties Properties to process. The key is the name of the property, the value is the value of the
   * property.
   * @param baseDir Base directory against which relative file paths should be resolved.
   * @throws Exception When the handler could not process the properties, e.g. when an IO error occurred.
   */
  void handleProperties(String configuration, Map<String, String> properties, Path baseDir) throws Exception;
}
