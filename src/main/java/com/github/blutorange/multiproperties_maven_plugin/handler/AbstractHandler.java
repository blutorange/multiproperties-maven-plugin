package com.github.blutorange.multiproperties_maven_plugin.handler;

/**
 * Base POJO class with the default fields for {@link Handler}.
 */
public abstract class AbstractHandler implements Handler {
  private Class<? extends HandlerImplementor> handlerClass;

  protected AbstractHandler(Class<? extends HandlerImplementor> handlerClass) {
    this.handlerClass = handlerClass;
  }

  @Override
  public Class<? extends HandlerImplementor> getImplementorClass() {
    return handlerClass;
  }
}
