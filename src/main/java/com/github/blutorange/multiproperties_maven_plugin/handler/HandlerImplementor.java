package com.github.blutorange.multiproperties_maven_plugin.handler;

/**
 * An output handler of a multiproperties file. A handler may e.g. write the properties to a Java properties file.
 * @param <C> Type of the handler configuration.
 */
public interface HandlerImplementor<C extends Handler> {
  /**
   * @return The name of this handler.
   */
  String getName();
  
  /**
   * Parses a configuration string into the corresponding configuration class.
   * @param configuration A configuration string.
   * @return The parsed configuration.
   */
  C parseConfig(String configuration);

  /**
   * Handles the given properties, e.g. by writing the properties to a file.
   * @param params Parameters that the handler may make use of, including the properties to output.
   * @throws Exception When the handler could not process the properties, e.g. when an IO error occurred.
   */
  void handleProperties(HandlerImplementorContext<C> params) throws Exception;
}
