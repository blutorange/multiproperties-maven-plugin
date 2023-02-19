package com.github.blutorange.multiproperties_maven_plugin.parser;

import java.util.Map;

/**
 * Abstracts a multiproperties file over different versions of the file format.
 */
public interface IMultiproperties {
  /**
   * Gets the name of the output handler which defines how derived files are generated.
   * @return The name of the output handler.
   */
  String getHandler();

  /**
   * Gets all defined output handlers. The return value is a map between the column key (language) and the handler
   * string for that column.
   * @return A map with all defined output handlers.
   */
  Map<String, String> getHandlerConfigurations();

  /**
   * Gets all resolved properties contained in the multiproperties file. The return value is a map where the key is the
   * name of the property and the value another map between the column's key (language name) and the value for that
   * language. Returned properties are resolved against the default as specified in the multiproperties file.
   * @return A list of all contained properties.
   */
  Map<String, Map<String, String>> getResolvedProperties();
}
