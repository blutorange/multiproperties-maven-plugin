package com.github.blutorange.multiproperties_maven_plugin.handler.common;

import static com.github.blutorange.multiproperties_maven_plugin.common.FileHelper.getFileBasename;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.blutorange.multiproperties_maven_plugin.common.FileHelper;

/**
 * Tests for {@link FileHelper}
 */
@SuppressWarnings("javadoc")
public class FileHelperTest {
  @Test
  public void testGetFileBasename() {
    assertEquals("", getFileBasename(null));
    assertEquals("", getFileBasename(""));
    assertEquals("", getFileBasename("."));
    assertEquals("", getFileBasename(".."));
    assertEquals("", getFileBasename("/"));
    assertEquals("", getFileBasename("//"));
    assertEquals("a", getFileBasename("a"));
    assertEquals("translations", getFileBasename("translations.multiproperties"));
    assertEquals("translations", getFileBasename("foo/translations.multiproperties"));
    assertEquals("translations", getFileBasename("foo\\translations.multiproperties"));
    assertEquals("translations", getFileBasename("foo\\bar/translations.multiproperties"));
    assertEquals("translations", getFileBasename("foo/bar\\translations.multiproperties"));
    assertEquals("translations", getFileBasename("foo\\bar/translations"));
    assertEquals("script", getFileBasename("foo/script.d.ts"));
    assertEquals("bar", getFileBasename("foo.script/bar"));
  }
}
