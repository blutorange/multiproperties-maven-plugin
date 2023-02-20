package com.github.blutorange.multiproperties_maven_plugin.handler;

/**
 * The {@link HandlerImplementor output handler} which delegates to the handler as defined in the multiproperties file.
 */
public final class DefaultHandler extends AbstractHandler {
  /**
   * Creates a new empty default handler instance.
   */
  public DefaultHandler() {
    super(DefaultHandlerImplementor.class);
  }
}
