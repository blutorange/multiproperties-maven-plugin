package com.github.blutorange.multiproperties_maven_plugin.handler;

import java.io.IOException;

@FunctionalInterface
public interface IoAction<T> {
  void perform(T value) throws IOException;
}
