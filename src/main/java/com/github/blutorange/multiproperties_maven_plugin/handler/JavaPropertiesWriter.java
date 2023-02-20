package com.github.blutorange.multiproperties_maven_plugin.handler;

import static com.github.blutorange.multiproperties_maven_plugin.common.StringHelper.padLeft;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

final class JavaPropertiesWriter {
  private static final String LINE_SEPARATOR = "\r\n";

  private final CharsetEncoder encoder;

  private final Writer writer;

  public JavaPropertiesWriter(Writer writer, Charset charset) {
    this.writer = writer;
    this.encoder = charset.newEncoder();
  }

  public void writeComment(String value, boolean whitespace) throws IOException {
    writeComment(value, whitespace, LINE_SEPARATOR);
  }

  public void writeKeyValuePair(String key, String value) throws IOException {
    writeKeyValuePair(writer, encoder, key, value);
  }

  public void writeKeyValuePairAsComment(String key, String value) throws IOException {
    final var writer = new StringWriter();
    writeKeyValuePair(writer, encoder, key, value);
    writeComment(writer.toString(), false, "\n");
  }

  public void writeLineBreak() throws IOException {
    writer.write(LINE_SEPARATOR);
  }

  private void writeComment(String value, boolean whitespace, String lineSeparator) throws IOException {
    final var lines = value.split("\\r\\n|\\n|\\r");
    for (var index = 0; index < lines.length; index += 1) {
      final var line = lines[index];
      writer.write("#");
      if (whitespace) {
        writer.write(" ");
      }
      writer.write(line);
      writer.write(index == lines.length - 1 ? LINE_SEPARATOR : lineSeparator);
    }
  }

  public static void writeKeyValuePair(Writer writer, CharsetEncoder charset, String key, String value) throws IOException {
    writer.write(escapeKey(key));
    writer.write("=");
    if (value != null) {
      writer.write(escapeValue(value, charset));
    }
    writer.write(LINE_SEPARATOR);
  }

  private static String escapeKey(String name) {
    return name.replaceAll("([=: \\t\\f])", "\\\\$1");
  }

  private static String escapeValue(String value, CharsetEncoder encoder) {
    var nonWhitespace = false;
    final var it = value.codePoints().iterator();
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
          final var alwaysEscape = encoder.charset().equals(StandardCharsets.ISO_8859_1) //
              ? c >= 0x7f || c == '!' || c == '#' || c == ':' || c == '=' //
              : false;
          if (c >= 32 && !alwaysEscape && encoder.canEncode(Character.toString(c))) {
            sb.appendCodePoint(c);
          }
          else {
            if (c == '!' || c == '#' || c == ':' || c == '=') {
              sb.append("\\").appendCodePoint(c);
            }
            else {
              if (c <= 0xFFFF) {
                sb.append("\\u");
                sb.append(padLeft(Integer.toString(c, 16).toUpperCase(Locale.ROOT), 4, '0'));
              }
              else {
                sb.append("\\u");
                sb.append(padLeft(Integer.toString(Character.highSurrogate(c), 16).toUpperCase(Locale.ROOT), 4, '0'));
                sb.append("\\u");
                sb.append(padLeft(Integer.toString(Character.lowSurrogate(c), 16).toUpperCase(Locale.ROOT), 4, '0'));
              }
            }
          }
          break;
      }
    }

    return sb.toString();
  }
}
