package com.github.blutorange.multiproperties_maven_plugin.handler;

import static com.github.blutorange.multiproperties_maven_plugin.common.FileHelper.createDirectoriesIfMissing;
import static com.github.blutorange.multiproperties_maven_plugin.common.FileHelper.removeFirstPathSegmentFromString;
import static com.github.blutorange.multiproperties_maven_plugin.common.FileHelper.shouldSkipOutput;
import static com.github.blutorange.multiproperties_maven_plugin.common.StringHelper.isNotEmpty;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;

import com.github.blutorange.multiproperties_maven_plugin.parser.Comment;
import com.github.blutorange.multiproperties_maven_plugin.parser.Empty;
import com.github.blutorange.multiproperties_maven_plugin.parser.Property;

final class JavaPropertiesHandler implements IOutputHandler {
  @Override
  public String getName() {
    return "Java Properties Handler";
  }

  @Override
  public void handleProperties(IOutputParams params) throws Exception {
    final var configuration = new JavaPropertiesConfiguration(params.getHandlerConfigurationString());
    withWriter(params, configuration, writer -> writeItems(params, configuration, writer));
  }

  private void withWriter(IOutputParams params, JavaPropertiesConfiguration configuration, IoAction<Writer> action) throws IOException {
    final var outputPath = resolveOutputPath(configuration.getOutputPath(), params.isRemoveFirstPathSegment());
    final var outputFile = params.getBaseDir().resolve(outputPath);
    final var encoding = configuration.getEncoding();

    if (shouldSkipOutput(params.getInputFile(), outputFile, params.getSkipMode())) {
      params.getLogger().info(String.format("Skipping ouptut <%s>: %s", outputFile, params.getSkipMode().getReason()));
      return;
    }

    createDirectoriesIfMissing(outputFile.getParent());
    params.getLogger().info(String.format("Writing file <%s> with encoding <%s>", outputFile, encoding));

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

  private void writeItems(IOutputParams params, JavaPropertiesConfiguration configuration, Writer writer) throws IOException {
    final var columnKey = params.getColumnKey();
    final var propertiesWriter = new JavaPropertiesWriter(writer, configuration.getEncoding());

    if (configuration.isInsertFileDescriptionAsComment() && isNotEmpty(params.getFileDescription())) {
      propertiesWriter.writeComment(params.getFileDescription(), true);
      propertiesWriter.writeLineBreak();
    }

    if (configuration.isInsertColumnDescriptionAsComment()) {
      // Weird, but that's how the Eclipse add-on behaves currently...
      propertiesWriter.writeLineBreak();
    }

    for (final var item : params.getItems()) {
      if (item instanceof Property) {
        writeProperty(propertiesWriter, params, configuration, columnKey, (Property)item);
      }
      else if (item instanceof Comment) {
        writeComment(propertiesWriter, (Comment)item);
      }
      else if (item instanceof Empty) {
        writeEmpty(propertiesWriter, (Empty)item);
      }
    }
  }

  private void writeProperty(JavaPropertiesWriter propertiesWriter, IOutputParams params, JavaPropertiesConfiguration configuration, String columnKey, Property property) throws IOException {
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
