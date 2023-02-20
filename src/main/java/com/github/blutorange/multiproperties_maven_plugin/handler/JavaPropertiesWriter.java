package com.github.blutorange.multiproperties_maven_plugin.handler;

import static com.github.blutorange.multiproperties_maven_plugin.common.StringHelper.padLeft;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

final class JavaPropertiesWriter {
  private static final String LINE_SEPARATOR = "\r\n";

  private final Writer writer;

  public JavaPropertiesWriter(Writer writer) {
    this.writer = writer;
  }

  public void writeComment(String value, boolean whitespace) throws IOException {
    final var lines = value.split("\\r\\n|\\n|\\r");
    for (final var line : lines) {
      writer.write("#");
      if (whitespace) {
        writer.write(" ");
      }
      writer.write(line);
      writeLineBreak();
    }
  }

  public void writeKeyValuePair(String key, String value) throws IOException {
    writeKeyValuePair(writer, key, value);
  }

  public void writeKeyValuePairAsComment(String key, String value) throws IOException {
    final var writer = new StringWriter();
    writeKeyValuePair(writer, key, value);
    writeComment(writer.toString(), false);
  }

  public void writeLineBreak() throws IOException {
    writer.write(LINE_SEPARATOR);
  }

  public static void writeKeyValuePair(Writer writer, String key, String value) throws IOException {
    writer.write(escapeKey(key));
    writer.write("=");
    if (value != null) {
      writer.write(escapeValue(value));
    }
    writer.write(LINE_SEPARATOR);
  }

  private static String escapeKey(String name) {
    return name.replaceAll("([=: \\t\\f])", "\\\\$1");
  }

  private static String escapeValue(String value) {
    var nonWhitespace = false;
    final var it = value.chars().iterator();
    final var sb = new StringBuilder();
    while (it.hasNext()) {
      final var c = it.nextInt();
      switch (c) {
        case ' ':
          if (!nonWhitespace) {
            sb.append("\\ ");
            nonWhitespace = true;
          }
          else {
            sb.append(" ");
          }
          break;
        case '\\':
          sb.append("\\\\");
          nonWhitespace = true;
          break;
        case '\r':
          sb.append("\\r");
          nonWhitespace = true;
          break;
        case '\n':
          sb.append("\\n\\\n\t");
          nonWhitespace = false;
          break;
        case '\t':
          sb.append("\\t");
          break;
        case '\f':
          sb.append("\\f");
          break;
        default:
          nonWhitespace = true;
          if (c >= 32 && c <= 126) {
            sb.appendCodePoint(c);
          }
          else {
            sb.append("\\u");
            sb.append(padLeft(Integer.toString(c, 16), 4, '0'));
          }
          break;
      }
    }

    return sb.toString();
  }
}
