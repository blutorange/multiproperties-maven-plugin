package com.github.blutorange.multiproperties_maven_plugin.handler;

/**
 * Base interface for all properties handlers.
 */
public interface Handler {
  /**
   * @return Class which implements the business logic.
   */
  Class<? extends HandlerImplementor<?>> getImplementorClass();
}
