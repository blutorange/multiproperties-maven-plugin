package com.github.blutorange.multiproperties_maven_plugin.handler;

import static com.github.blutorange.multiproperties_maven_plugin.common.FileHelper.createDirectoriesIfMissing;
import static com.github.blutorange.multiproperties_maven_plugin.common.FileHelper.removeFirstPathSegmentFromString;
import static com.github.blutorange.multiproperties_maven_plugin.common.StringHelper.isNotEmpty;
import static com.github.blutorange.multiproperties_maven_plugin.common.StringHelper.removeStart;
import static com.github.blutorange.multiproperties_maven_plugin.handler.HandlerConfigurationParser.javaProperties;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;

import com.github.blutorange.multiproperties_maven_plugin.common.IoAction;
import com.github.blutorange.multiproperties_maven_plugin.parser.Comment;
import com.github.blutorange.multiproperties_maven_plugin.parser.Empty;
import com.github.blutorange.multiproperties_maven_plugin.parser.Property;

/**
 * Implementor for the {@link JavaPropertiesHandler}.
 */
public final class JavaPropertiesImplementor implements HandlerImplementor<JavaPropertiesHandler> {
  @Override
  public String getName() {
    return "Java Properties Handler";
  }

  @Override
  public void handleProperties(HandlerImplementorContext<JavaPropertiesHandler> context) throws Exception {
    withWriter(context, writer -> writeItems(context, writer));
  }

  @Override
  public JavaPropertiesHandler parseConfig(String configuration) {
    return javaProperties(configuration);
  }

  private void withWriter(HandlerImplementorContext<JavaPropertiesHandler> ctx, IoAction<Writer> action) throws IOException {
    final var config = ctx.getConfiguration();
    final var outputPath = resolveOutputPath(removeStart(config.getOutputPath(), "/"), ctx.isRemoveFirstPathSegment());
    final var outputFile = ctx.interpolateFilename(outputPath);
    final var encoding = Charset.forName(config.getEncoding());

    if (ctx.shouldSkipOutput(outputFile)) {
      ctx.getLogger().info(String.format("Skipping ouptut <%s>: %s", outputFile, ctx.getSkipMode().getReason()));
      return;
    }

    createDirectoriesIfMissing(outputFile.getParent());
    ctx.getLogger().info(String.format("Writing file <%s> with encoding <%s>", outputFile, encoding));

    try (final var out = Files.newOutputStream(outputFile, CREATE, WRITE, TRUNCATE_EXISTING)) {
      try (final var writer = new OutputStreamWriter(out, encoding)) {
        action.perform(writer);
      }
    }
  }

  private void writeComment(JavaPropertiesWriter propertiesWriter, Comment comment) throws IOException {
    propertiesWriter.writeComment(comment.getValue(), false);
  }

  private void writeEmpty(JavaPropertiesWriter propertiesWriter, Empty item) throws IOException {
    propertiesWriter.writeLineBreak();
  }

  private void writeItems(HandlerImplementorContext<JavaPropertiesHandler> ctx, Writer writer) throws IOException {
    final var config = ctx.getConfiguration();
    final var encoding = Charset.forName(config.getEncoding());
    final var columnKey = ctx.getColumnKey();

    final var propertiesWriter = new JavaPropertiesWriter(writer, encoding);

    if (config.isInsertFileDescriptionAsComment() && isNotEmpty(ctx.getFileDescription())) {
      propertiesWriter.writeComment(ctx.getFileDescription(), true);
      propertiesWriter.writeLineBreak();
    }

    if (config.isInsertColumnDescriptionAsComment()) {
      // Weird, but that's how the Eclipse add-on behaves currently...
      propertiesWriter.writeLineBreak();
    }

    for (final var item : ctx.getItems()) {
      if (item instanceof Property) {
        writeProperty(propertiesWriter, ctx, config, columnKey, (Property)item);
      }
      else if (item instanceof Comment) {
        writeComment(propertiesWriter, (Comment)item);
      }
      else if (item instanceof Empty) {
        writeEmpty(propertiesWriter, (Empty)item);
      }
    }
  }

  private void writeProperty(JavaPropertiesWriter propertiesWriter, HandlerImplementorContext<JavaPropertiesHandler> params, JavaPropertiesHandler configuration, String columnKey, Property property)
      throws IOException {
    final var name = property.getName();
    final var value = configuration.isDisableDefaultValues() ? property.getValue(columnKey) : property.getResolvedValue(columnKey);
    if (value == null) {
      return;
    }
    if (property.isDisabled()) {
      if (configuration.isWriteDisabledPropertiesAsComments()) {
        propertiesWriter.writeKeyValuePairAsComment(name, value);
      }
    }
    else {
      propertiesWriter.writeKeyValuePair(name, value);
    }
  }

  private static String resolveOutputPath(String outputPath, boolean removeFirstPathSegment) {
    return removeFirstPathSegment ? removeFirstPathSegmentFromString(outputPath) : outputPath;
  }
}
