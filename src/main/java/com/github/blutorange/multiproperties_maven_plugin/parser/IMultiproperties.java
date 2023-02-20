package com.github.blutorange.multiproperties_maven_plugin.parser;

import java.util.List;

/**
 * Abstracts a multiproperties file over different versions of the file format.
 */
public interface IMultiproperties {
  /**
   * @return The description of the multiproperties file.
   */
  String getFileDescription();

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
  List<HandlerConfiguration> getHandlerConfigurations();

  /**
   * Gets all items contained in the multiproperties file. Default values etc. MUST NOT be resolved.
   * @return A list of all contained properties.
   */
  List<Item> getItems();
}
