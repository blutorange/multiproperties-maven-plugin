package com.github.blutorange.multiproperties_maven_plugin.handler;

/**
 * Implementor for the {@link DefaultHandler}.
 */
public final class DefaultHandlerImplementor implements HandlerImplementor {
  @Override
  public String getName() {
    return "Default Multiproperties Handler";
  }

  @Override
  public void handleProperties(HandlerImplementorContext params) throws Exception {
    final var delegate = DefaultHandlerImplementorFactory.forName(params.getDefaultHandlerName());
    delegate.handleProperties(params);
  }
}
