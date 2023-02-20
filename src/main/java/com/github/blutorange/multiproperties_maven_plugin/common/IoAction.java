package com.github.blutorange.multiproperties_maven_plugin.common;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * An IO action which may throw an {@link IOException}, unlike a {@link Consumer}.
 * @param <T> Type of the parameter for the action.
 */
@FunctionalInterface
public interface IoAction<T> {
  /**
   * Runs an IO action which may throw.
   * @param value Parameter for the IO action.
   * @throws IOException When the IO action fails.
   */
  void perform(T value) throws IOException;
}
