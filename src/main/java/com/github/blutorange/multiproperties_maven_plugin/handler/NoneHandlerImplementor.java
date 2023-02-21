package com.github.blutorange.multiproperties_maven_plugin.handler;

import static com.github.blutorange.multiproperties_maven_plugin.handler.HandlerConfigurationParser.none;

/**
 * Implementor for the {@link NoneHandler}.
 */
public final class NoneHandlerImplementor implements HandlerImplementor<NoneHandler> {
  @Override
  public String getName() {
    return "None Handler";
  }

  @Override
  public void handleProperties(HandlerImplementorContext<NoneHandler> context) throws Exception {
    // no-op
  }

  @Override
  public NoneHandler parseConfig(String configuration) {
    return none(configuration);
  }
}
