package com.github.blutorange.multiproperties_maven_plugin.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link JavaPropertiesWriter}
 */
@SuppressWarnings("javadoc")
public class JavaPropertiesWriterTest {
  @Test
  public void testWriteKeyValuePair() throws IOException {
    assertWritesKeyValuePair("key=", "key", null);
    assertWritesKeyValuePair("delimiterCharacters\\:\\=\\ =foobar", "delimiterCharacters:= ", "foobar");
    assertWritesKeyValuePair("key=Line\\rbreak\\n\\\n\ttab\\tfeed\\f", "key", "Line\rbreak\ntab\tfeed\f");
    assertWritesKeyValuePair("key=Line\\n\\\n\t\\  break", "key", "Line\n  break");
    assertWritesKeyValuePair("key=c:\\\\wiki\\\\templates", "key", "c:\\wiki\\templates");
    assertWritesKeyValuePair("key=\\u3053\\u3093\\u306b\\u3061\\u306f", "key", "こんにちは");
    assertWritesKeyValuePair("key=\\u1f4ab", "key", "\u1f4ab");
    assertWritesKeyValuePair("key=\\  foobar  ", "key", "  foobar  ");
    assertWritesKeyValuePair("key=\\u0015", "key", "\u0015");
  }

  @Test
  public void testWriteKeyValuePairAsComment() throws IOException {
    assertWritesKeyValuePairAsComment("#delimiterCharacters\\:\\=\\ =foobar", "delimiterCharacters:= ", "foobar");
    assertWritesKeyValuePairAsComment("#key=Line\\rbreak\\n\\\r\n#\ttab\\tfeed\\f", "key", "Line\rbreak\ntab\tfeed\f");
    assertWritesKeyValuePairAsComment("#key=c:\\\\wiki\\\\templates", "key", "c:\\wiki\\templates");
    assertWritesKeyValuePairAsComment("#key=\\u3053\\u3093\\u306b\\u3061\\u306f", "key", "こんにちは");
    assertWritesKeyValuePairAsComment("#key=\\u1f4ab", "key", "\u1f4ab");
    assertWritesKeyValuePairAsComment("#key=\\  foobar  ", "key", "  foobar  ");
  }

  @Test
  public void testWriteComment() throws IOException {
    assertWritesComment("# hello\r\n", "hello");
    assertWritesComment("# a\r\n# hello\r\n# world\r\n# line3\r\n# \r\n# line4\r\n", "a\rhello\nworld\r\nline3\n\nline4");
  }

  private void assertWritesKeyValuePair(String expected, String key, String value) throws IOException {
    var writer = new StringWriter();
    var props = new JavaPropertiesWriter(writer);
    props.writeKeyValuePair(key, value);
    writer.flush();
    assertEquals(expected + "\r\n", writer.toString());
  }

  private void assertWritesKeyValuePairAsComment(String expected, String key, String value) throws IOException {
    var writer = new StringWriter();
    var props = new JavaPropertiesWriter(writer);
    props.writeKeyValuePairAsComment(key, value);
    writer.flush();
    assertEquals(expected + "\r\n", writer.toString());
  }

  private void assertWritesComment(String expected, String comment) throws IOException {
    var writer = new StringWriter();
    var props = new JavaPropertiesWriter(writer);
    props.writeComment(comment, true);
    writer.flush();
    var actual = writer.toString();
    assertEquals(expected, actual);
  }
}
