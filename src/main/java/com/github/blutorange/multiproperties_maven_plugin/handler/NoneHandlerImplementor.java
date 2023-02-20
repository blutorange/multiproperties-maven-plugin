package com.github.blutorange.multiproperties_maven_plugin.handler;

/**
 * Implementor for the {@link NoneHandler}.
 */
public final class NoneHandlerImplementor implements HandlerImplementor {
  @Override
  public String getName() {
    return "None Handler";
  }

  @Override
  public void handleProperties(HandlerImplementorContext params) throws Exception {
    // no-op
  }
}
