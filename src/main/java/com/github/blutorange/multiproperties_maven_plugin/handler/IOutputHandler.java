package com.github.blutorange.multiproperties_maven_plugin.handler;

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
   * @param params Parameters that the handler may make use of, including the properties to output.
   * @throws Exception When the handler could not process the properties, e.g. when an IO error occurred.
   */
  void handleProperties(IOutputParams params) throws Exception;
}
