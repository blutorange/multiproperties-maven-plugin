package com.github.blutorange.multiproperties_maven_plugin.generator;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.List;

import org.apache.maven.plugin.logging.Log;

import com.github.blutorange.multiproperties_maven_plugin.handler.Handler;
import com.github.blutorange.multiproperties_maven_plugin.handler.HandlerImplementor;
import com.github.blutorange.multiproperties_maven_plugin.mojo.SkipOutputMode;
import com.github.blutorange.multiproperties_maven_plugin.parser.MultipropertiesParser;

/**
 * Processes multiproperties files and generates the derived files. Each instance has an immutable configuration.
 */
public final class MultipropertiesGenerator {
  private final List<Handler> handlers;
  private final Log logger;
  private final SkipOutputMode skipMode;
  private final Path sourceDir;
  private final Path targetDir;

  MultipropertiesGenerator(MultipropertiesGeneratorBuilder builder) {
    this.handlers = builder.handlers;
    this.logger = builder.logger;
    this.skipMode = builder.skipMode;
    this.sourceDir = builder.sourceDir;
    this.targetDir = builder.targetDir;
  }

  /**
   * Processes a multiproperties file by invoking the configured output handler on all defined columns.
   * @param file Multiproperties file to process.
   * @throws Exception When the file could not be processed, e.g. when the file does not exist, is invalid or the output
   * could not be written.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void process(Path file) throws Exception {
    final var parsed = MultipropertiesParser.parse(file);
    for (final var handler : handlers) {
      final var implementor = instantiateImplementor(handler);
      logger.info(String.format("Using output handler <%s>", implementor.getName()));
      for (final var handlerConfiguration : parsed.getHandlerConfigurations()) {
        logger.info(String.format("  Processing column <%s>", handlerConfiguration.getColumnKey()));
        final var params = outputParamsBuilder() //
            .withInputFile(file) //
            .withMultiproperties(parsed) //
            .withConfiguration(handler) //
            .withDefaultHandlerConfiguration(handlerConfiguration) //
            .build();
        implementor.handleProperties((DefaultHandlerImplementorContext)params);
      }
    }
  }

  private <C extends Handler> DefaultHandlerImplementorContextBuilder<C> outputParamsBuilder() {
    return DefaultHandlerImplementorContext.<C> builder() //
        .withLogger(logger) //
        .withSourceDir(sourceDir) //
        .withTargetDir(targetDir) //
        .withSkipMode(skipMode);
  }

  /**
   * @return A builder for configuring a multiproperties processor.
   */
  public static MultipropertiesGeneratorBuilder builder() {
    return new MultipropertiesGeneratorBuilder();
  }

  private static HandlerImplementor<?> instantiateImplementor(Handler handler) {
    try {
      final var ctor = handler.getImplementorClass().getConstructor();
      return ctor.newInstance();
    }
    catch (final NoSuchMethodException | SecurityException | IllegalAccessException | InstantiationException e) {
      throw new IllegalArgumentException(String.format("Handler class <%s> must have an accessible default constructor", handler.getImplementorClass()), e);
    }
    catch (final InvocationTargetException e) {
      throw new IllegalStateException(String.format("Handler class <%s> threw an exception during instantiation", handler.getImplementorClass()), e);
    }
  }
}
