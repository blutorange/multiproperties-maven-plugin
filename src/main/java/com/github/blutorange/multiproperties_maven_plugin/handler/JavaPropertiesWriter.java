package com.github.blutorange.multiproperties_maven_plugin.handler;
import static org.apache.commons.lang3.StringUtils.leftPad;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Pattern;

final class JavaPropertiesWriter {
  private static final String LINE_SEPARATOR = "\r\n";

  private final CharsetEncoder encoder;

  private final JavaPropertiesQuirkSettings quirks;

  private final Writer writer;

  public JavaPropertiesWriter(Writer writer, Charset charset, JavaPropertiesQuirkSettings quirks) {
    this.writer = writer;
    this.encoder = charset.newEncoder();
    this.quirks = quirks;
  }

  public void writeComment(String value, boolean whitespace, boolean forceCommentMultilines) throws IOException {
    writeComment(value, whitespace, LINE_SEPARATOR, forceCommentMultilines);
  }

  public void writeKeyValuePair(String key, String value) throws IOException {
    writeKeyValuePair(writer, encoder, key, value);
  }

  public void writeKeyValuePair(Writer writer, CharsetEncoder charset, String key, String value) throws IOException {
    writer.write(escapeKey(key));
    writer.write("=");
    if (value != null) {
      writer.write(escapeValue(trimEndValue(normalizeLineBreaks(value)), charset));
    }
    writer.write(LINE_SEPARATOR);
  }

  private String trimEndValue(String value) {
    return Pattern.compile("\\n$").matcher(value).replaceFirst("");
  }

  private String normalizeLineBreaks(String value) {
    return value.replaceAll("(\\r\\n|\\r|\\n)", "\n");
  }

  public void writeKeyValuePairAsComment(String key, String value) throws IOException {
    final var writer = new StringWriter();
    writeKeyValuePair(writer, encoder, key, value);
    writeComment(writer.toString(), false, "\n", false);
  }

  public void writeLineBreak() throws IOException {
    writer.write(LINE_SEPARATOR);
  }

  private String escapeKey(String name) {
    return name.replaceAll("([=: \\t\\f])", "\\\\$1");
  }

  private String escapeValue(String value, CharsetEncoder encoder) {
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
          sb.append(quirks.skipEscapingBackslash() && isQuestionMarkCharset(encoder.charset()) ? "\\" : "\\\\");
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
          final boolean justWriteQuestionMark = quirks.writeQuestionMarksInsteadOfProperlyEscapingChars() //
              && encoder.charset().equals(StandardCharsets.US_ASCII) && c >= 0x80;
          final var alwaysEscape = encoder.charset().equals(StandardCharsets.ISO_8859_1) //
              ? c >= 0x7f || c == '!' || c == '#' || c == ':' || c == '=' //
              : false;
          if (justWriteQuestionMark) {
            sb.append("?");
          }
          else if (c >= 32 && !alwaysEscape && encoder.canEncode(Character.toString(c))) {
            sb.appendCodePoint(c);
          }
          else {
            if (c == '!' || c == '#' || c == ':' || c == '=') {
              sb.append("\\").appendCodePoint(c);
            }
            else {
              if (c <= 0xFFFF) {
                sb.append("\\u");
                sb.append(leftPad(Integer.toString(c, 16).toUpperCase(Locale.ROOT), 4, '0'));
              }
              else {
                sb.append("\\u");
                sb.append(leftPad(Integer.toString(Character.highSurrogate(c), 16).toUpperCase(Locale.ROOT), 4, '0'));
                sb.append("\\u");
                sb.append(leftPad(Integer.toString(Character.lowSurrogate(c), 16).toUpperCase(Locale.ROOT), 4, '0'));
              }
            }
          }
          break;
      }
    }

    return sb.toString();
  }

  private boolean isQuestionMarkCharset(Charset charset) {
    return encoder.charset().equals(StandardCharsets.US_ASCII) //
        || encoder.charset().equals(StandardCharsets.UTF_16) //
        || encoder.charset().equals(StandardCharsets.UTF_16BE)
        || encoder.charset().equals(StandardCharsets.UTF_16LE) //
        || encoder.charset().equals(StandardCharsets.UTF_8);
  }

  private void writeComment(String value, boolean whitespace, String lineSeparator, boolean forceCommentMultilines) throws IOException {
    final var lines = value.split("\\r\\n|\\n|\\r");
    for (var index = 0; index < lines.length; index += 1) {
      final var line = lines[index];
      if (index == 0 || !quirks.skipCommentingMultiLines() || forceCommentMultilines) {
        writer.write("#");
      }
      if (whitespace) {
        writer.write(" ");
      }
      writer.write(line);
      writer.write(index == lines.length - 1 ? LINE_SEPARATOR : lineSeparator);
    }
  }
}
