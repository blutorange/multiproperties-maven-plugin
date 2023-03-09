package com.github.blutorange.multiproperties_maven_plugin.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link JavaPropertiesWriter}
 */
@SuppressWarnings("javadoc")
public class JavaPropertiesWriterTest {
  private static JavaPropertiesQuirkSettings quirksFull = JavaPropertiesQuirkSettings.builder() //
      .skipCommentingMultiLines(true) //
      .skipEscapingBackslash(true) //
      .writeQuestionMarksInsteadOfProperlyEscapingChars(true) //
      .build();
  private static JavaPropertiesQuirkSettings quirksNone = JavaPropertiesQuirkSettings.builder() //
      .build();

  @Test
  public void testWriteLineBreak() throws IOException {
    var writer = new StringWriter();
    var props = new JavaPropertiesWriter(writer, StandardCharsets.UTF_8, quirksFull);
    props.writeLineBreak();
    writer.flush();
    assertEquals("\r\n", writer.toString());
  }

  @Test
  public void testWriteKeyValuePair() throws IOException {
    assertWritesKeyValuePair("key=", "key", null, quirksFull);
    assertWritesKeyValuePair("delimiterCharacters\\:\\=\\ =foobar", "delimiterCharacters:= ", "foobar", quirksFull);
    assertWritesKeyValuePair("key=Line\\n\\\n\tbreak\\n\\\n\ttab\\tfeed\\f", "key", "Line\rbreak\ntab\tfeed\f", quirksFull);
    assertWritesKeyValuePair("key=Line\\n\\\n\t\\  break", "key", "Line\n  break", quirksFull);
    assertWritesKeyValuePair("key=c\\:\\\\wiki\\\\templates", "key", "c:\\wiki\\templates", quirksFull);
    assertWritesKeyValuePair("key=c\\:\\\\wiki\\\\templates", "key", "c:\\wiki\\templates", quirksNone);
    assertWritesKeyValuePair("key=\\u3053\\u3093\\u306B\\u3061\\u306F", "key", "こんにちは", quirksFull);
    assertWritesKeyValuePair("key=\\u0001\\uF4AB", "key", "\u0001\uf4ab", quirksFull);
    assertWritesKeyValuePair("key=\\  foobar  ", "key", "  foobar  ", quirksFull);
    assertWritesKeyValuePair("key=\\u0015", "key", "\u0015", quirksFull);
    assertWritesKeyValuePair("key=こんにちは", "key", "こんにちは", StandardCharsets.UTF_8, quirksFull);
    assertWritesKeyValuePair("key=\\uD83D\\uDCAB", "key", "💫", StandardCharsets.ISO_8859_1, quirksFull);
    assertWritesKeyValuePair("key=?", "key", "💫", StandardCharsets.US_ASCII, quirksFull);
    assertWritesKeyValuePair("key=?????", "key", "こんにちは", StandardCharsets.US_ASCII, quirksFull);
    assertWritesKeyValuePair("key=\\uD83D\\uDCAB", "key", "💫", StandardCharsets.US_ASCII, quirksNone);
    assertWritesKeyValuePair("key=\\u3053\\u3093\\u306B\\u3061\\u306F", "key", "こんにちは", StandardCharsets.US_ASCII, quirksNone);
    assertWritesKeyValuePair("key=💫", "key", "💫", StandardCharsets.UTF_8, quirksFull);
    assertWritesKeyValuePair("key=💫", "key", "💫", StandardCharsets.UTF_16, quirksFull);
    assertWritesKeyValuePair("key=💫", "key", "💫", StandardCharsets.UTF_16BE, quirksFull);
    assertWritesKeyValuePair("key=💫", "key", "💫", StandardCharsets.UTF_16LE, quirksFull);
    assertWritesKeyValuePair("key=\\:\\!\\#\\=", "key", ":!#=", StandardCharsets.ISO_8859_1, quirksFull);
    assertWritesKeyValuePair("key=:!#=", "key", ":!#=", StandardCharsets.UTF_8, quirksFull);
    assertWritesKeyValuePair("key=[\\\\]", "key", "[\\]", StandardCharsets.ISO_8859_1, quirksNone);
    assertWritesKeyValuePair("key=[\\\\]", "key", "[\\]", StandardCharsets.UTF_8, quirksNone);
    assertWritesKeyValuePair("key=[\\\\]", "key", "[\\]", StandardCharsets.ISO_8859_1, quirksFull);
    assertWritesKeyValuePair("key=[\\]", "key", "[\\]", StandardCharsets.UTF_8, quirksFull);
    assertWritesKeyValuePair("key=[\\]", "key", "[\\]", StandardCharsets.UTF_16, quirksFull);
    assertWritesKeyValuePair("key=[\\]", "key", "[\\]", StandardCharsets.UTF_16BE, quirksFull);
    assertWritesKeyValuePair("key=[\\]", "key", "[\\]", StandardCharsets.UTF_16LE, quirksFull);
    assertWritesKeyValuePair("key=[\\]", "key", "[\\]", StandardCharsets.US_ASCII, quirksFull);
    assertWritesKeyValuePair("key=\\n\\\n\tvalue", "key", "\rvalue", quirksFull);
    assertWritesKeyValuePair("key=\\n\\\n\tvalue", "key", "\nvalue", quirksFull);
    assertWritesKeyValuePair("key=value", "key", "value\r", quirksFull);
    assertWritesKeyValuePair("key=value", "key", "value\n", quirksFull);
    assertWritesKeyValuePair("key=value", "key", "value\r\n", quirksFull);
    assertWritesKeyValuePair("key=value\\n\\\n\t\\n\\\n\t", "key", "value\r\n\n\r", quirksFull);
    assertWritesKeyValuePair("key=value\\n\\\n\t\\n\\\n\t\\n\\\n\t\\t", "key", "value\r\n\n\r\t", quirksFull);
    assertWritesKeyValuePair("key=value\\n\\\n\t\\n\\\n\tfoo", "key", "value\r\r\nfoo", quirksFull);
    assertWritesKeyValuePair("key=<p>Hallo</p>\\n\\\n\t", "key", "<p>Hallo</p>\r\r\n", quirksFull);
  }

  @Test
  public void testWriteKeyValuePairAsComment() throws IOException {
    assertWritesKeyValuePairAsComment("#delimiterCharacters\\:\\=\\ =foobar", "delimiterCharacters:= ", "foobar", quirksNone);
    assertWritesKeyValuePairAsComment("#key=Line\\n\\\n#\tbreak\\n\\\n#\ttab\\tfeed\\f", "key", "Line\rbreak\ntab\tfeed\f", quirksNone);
    assertWritesKeyValuePairAsComment("#key=c\\:\\\\wiki\\\\templates", "key", "c:\\wiki\\templates", quirksNone);
    assertWritesKeyValuePairAsComment("#key=\\u3053\\u3093\\u306B\\u3061\\u306F", "key", "こんにちは", quirksNone);
    assertWritesKeyValuePairAsComment("#key=\\u0001\\uF4AB", "key", "\u0001\uf4ab", quirksNone);
    assertWritesKeyValuePairAsComment("#key=\\  foobar  ", "key", "  foobar  ", quirksNone);
    assertWritesKeyValuePairAsComment("#key=foo\\n\\\n#\tbar", "key", "foo\nbar", quirksNone);
    assertWritesKeyValuePairAsComment("#key=foo\\n\\\n#\tbar", "key", "foo\r\nbar", quirksNone);

    assertWritesKeyValuePairAsComment("#delimiterCharacters\\:\\=\\ =foobar", "delimiterCharacters:= ", "foobar", quirksFull);
    assertWritesKeyValuePairAsComment("#key=Line\\n\\\n\tbreak\\n\\\n\ttab\\tfeed\\f", "key", "Line\rbreak\ntab\tfeed\f", quirksFull);
    assertWritesKeyValuePairAsComment("#key=c\\:\\\\wiki\\\\templates", "key", "c:\\wiki\\templates", quirksFull);
    assertWritesKeyValuePairAsComment("#key=c\\:\\\\wiki\\\\templates", "key", "c:\\wiki\\templates", quirksNone);
    assertWritesKeyValuePairAsComment("#key=\\u3053\\u3093\\u306B\\u3061\\u306F", "key", "こんにちは", quirksFull);
    assertWritesKeyValuePairAsComment("#key=\\u0001\\uF4AB", "key", "\u0001\uf4ab", quirksFull);
    assertWritesKeyValuePairAsComment("#key=\\  foobar  ", "key", "  foobar  ", quirksFull);
    assertWritesKeyValuePairAsComment("#key=foo\\n\\\n\tbar", "key", "foo\nbar", quirksFull);
    assertWritesKeyValuePairAsComment("#key=foo\\n\\\n\tbar", "key", "foo\r\nbar", quirksFull);
  }

  @Test
  public void testWriteComment() throws IOException {
    assertWritesComment("# hello\r\n", "hello", quirksFull, false);
    assertWritesComment("# a\r\n hello\r\n world\r\n line3\r\n \r\n line4\r\n", "a\rhello\nworld\r\nline3\n\nline4", quirksFull, false);
    assertWritesComment("# a\r\n# hello\r\n# world\r\n# line3\r\n# \r\n# line4\r\n", "a\rhello\nworld\r\nline3\n\nline4", quirksNone, false);

    assertWritesComment("# hello\r\n", "hello", quirksFull, true);
    assertWritesComment("# a\r\n# hello\r\n# world\r\n# line3\r\n# \r\n# line4\r\n", "a\rhello\nworld\r\nline3\n\nline4", quirksFull, true);
    assertWritesComment("# a\r\n# hello\r\n# world\r\n# line3\r\n# \r\n# line4\r\n", "a\rhello\nworld\r\nline3\n\nline4", quirksNone, true);
}

  private void assertWritesKeyValuePair(String expected, String key, String value, JavaPropertiesQuirkSettings quirks) throws IOException {
    assertWritesKeyValuePair(expected, key, value, StandardCharsets.ISO_8859_1, quirks);
  }

  private void assertWritesKeyValuePair(String expected, String key, String value, Charset charset, JavaPropertiesQuirkSettings quirks) throws IOException {
    var writer = new StringWriter();
    var props = new JavaPropertiesWriter(writer, charset, quirks);
    props.writeKeyValuePair(key, value);
    writer.flush();
    assertEquals(expected + "\r\n", writer.toString());
  }

  private void assertWritesKeyValuePairAsComment(String expected, String key, String value, JavaPropertiesQuirkSettings quirks) throws IOException {
    var writer = new StringWriter();
    var props = new JavaPropertiesWriter(writer, StandardCharsets.ISO_8859_1, quirks);
    props.writeKeyValuePairAsComment(key, value);
    writer.flush();
    assertEquals(expected + "\r\n", writer.toString());
  }

  private void assertWritesComment(String expected, String comment, JavaPropertiesQuirkSettings quirks, boolean forceCommentMultilines) throws IOException {
    var writer = new StringWriter();
    var props = new JavaPropertiesWriter(writer, StandardCharsets.ISO_8859_1, quirks);
    props.writeComment(comment, true, forceCommentMultilines);
    writer.flush();
    var actual = writer.toString();
    assertEquals(expected, actual);
  }
}
