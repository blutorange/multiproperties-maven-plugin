package com.github.blutorange.multiproperties_maven_plugin.handler;

/**
 * The {@link HandlerImplementor output handler} for {@code None}. Does not create an output file.
 */
public final class NoneHandler extends AbstractHandler {
  protected NoneHandler() {
    super(NoneHandlerImplementor.class);
  }
}
