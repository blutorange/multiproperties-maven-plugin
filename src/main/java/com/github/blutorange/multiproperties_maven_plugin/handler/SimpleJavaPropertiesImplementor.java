package com.github.blutorange.multiproperties_maven_plugin.handler;

import static com.github.blutorange.multiproperties_maven_plugin.common.FileHelper.createDirectoriesIfMissing;
import static com.github.blutorange.multiproperties_maven_plugin.handler.HandlerConfigurationParser.simpleJavaProperties;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.removeStart;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Properties;

import com.github.blutorange.multiproperties_maven_plugin.common.IoAction;
import com.github.blutorange.multiproperties_maven_plugin.parser.Property;

/**
 * Implementor for the {@link SimpleJavaPropertiesHandler}.
 */
public final class SimpleJavaPropertiesImplementor implements HandlerImplementor<SimpleJavaPropertiesHandler> {
  @Override
  public String getName() {
    return "Simple Java Properties Handler";
  }

  @Override
  public void handleProperties(HandlerImplementorContext<SimpleJavaPropertiesHandler> context) throws Exception {
    withWriter(context, writer -> writeItems(context, writer));
  }

  @Override
  public SimpleJavaPropertiesHandler parseConfig(String configuration) {
    return simpleJavaProperties(configuration);
  }

  private void withWriter(HandlerImplementorContext<SimpleJavaPropertiesHandler> ctx, IoAction<Writer> action) throws IOException {
    final var config = ctx.getConfiguration();
    final var outputPath = removeStart(config.getOutputPath(), "/");
    final var outputFile = ctx.interpolateFilename(outputPath);
    final var encoding = Charset.forName(config.getEncoding());

    if (ctx.shouldSkipOutput(outputFile)) {
      ctx.getLogger().info(String.format("    Skipping ouptut <%s>: %s", outputFile, ctx.getSkipMode().getReason()));
      return;
    }

    createDirectoriesIfMissing(outputFile.getParent());
    ctx.getLogger().info(String.format("    Writing file <%s> with encoding <%s>", outputFile, encoding));

    try (final var out = Files.newOutputStream(outputFile, CREATE, WRITE, TRUNCATE_EXISTING)) {
      try (final var writer = new OutputStreamWriter(out, encoding)) {
        try (final var encodingWriter = new PropertiesEncodingWriter(writer, encoding)) {
          action.perform(encodingWriter);
        }
      }
    }
  }

  private void writeItems(HandlerImplementorContext<SimpleJavaPropertiesHandler> ctx, Writer writer) throws IOException {
    final var config = ctx.getConfiguration();
    final var columnKey = ctx.getColumnKey();
    final var properties = new Properties();
    final var comments = config.isInsertFileDescriptionAsComment() ? ctx.getFileDescription() : null;

    for (final var item : ctx.getItems()) {
      // ignore comments and empty
      if (item instanceof Property) {
        final var property = (Property)item;
        if (isNotEmpty(property.getName())) {
          properties.setProperty(property.getName(), defaultString(property.getResolvedValue(columnKey)));
        }
      }
    }

    properties.store(writer, comments);
  }
}
