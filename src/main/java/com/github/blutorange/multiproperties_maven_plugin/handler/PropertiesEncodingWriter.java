package com.github.blutorange.multiproperties_maven_plugin.handler;

import static org.apache.commons.lang3.StringUtils.leftPad;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Locale;

final class PropertiesEncodingWriter extends Writer {
  private final OutputStreamWriter writer;
  private final CharsetEncoder encoder;

  public PropertiesEncodingWriter(OutputStreamWriter writer, Charset encoding) {
    this.encoder = encoding.newEncoder();
    this.writer = writer;
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    for (var i = off; i < off + len; i += 1) {
      final int c;
      if (i < len - 1 && Character.isSurrogatePair(cbuf[i], cbuf[i + 1])) {
        c = Character.toCodePoint(cbuf[i], cbuf[i + 1]);
        i += 1;
      }
      else {
        c = cbuf[i];
      }
      if (encoder.canEncode(Character.toString(c))) {
        writer.write(Character.toString(c));
      }
      else {
        if (c <= 0xFFFF) {
          writer.write('\\');
          writer.write('u');
          writer.write(leftPad(Integer.toString(c, 16).toUpperCase(Locale.ROOT), 4, '0'));
        }
        else {
          writer.write('\\');
          writer.write('u');
          writer.write(leftPad(Integer.toString(Character.highSurrogate(c), 16).toUpperCase(Locale.ROOT), 4, '0'));
          writer.write('\\');
          writer.write('u');
          writer.write(leftPad(Integer.toString(Character.lowSurrogate(c), 16).toUpperCase(Locale.ROOT), 4, '0'));
        }
      }
    }
  }

  @Override
  public void flush() throws IOException {
    writer.flush();
  }

  @Override
  public void close() throws IOException {
    writer.close();
  }
}
