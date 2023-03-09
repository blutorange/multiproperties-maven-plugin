package com.github.blutorange.multiproperties_maven_plugin.generator;

import static org.apache.commons.lang3.StringUtils.removeStart;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;

import com.github.blutorange.multiproperties_maven_plugin.common.FileHelper;

/**
 * Interpolator for a file name based on a pattern.
 */
final class FilenameInterpolator {
  private final String pattern;
  private final String prefix;
  private final String suffix;
  private final char escapeChar;

  public FilenameInterpolator(String pattern) {
    this(pattern, "#{", "}", '#');
  }

  public FilenameInterpolator(String pattern, String prefix, String suffix, char escapeChar) {
    this.pattern = pattern;
    this.prefix = prefix;
    this.suffix = suffix;
    this.escapeChar = escapeChar;
  }

  public Path interpolate(Path inputFile, Path inputBaseDir, Path targetDirectory) throws IOException {
    return interpolate(inputFile, inputBaseDir, targetDirectory, null);
  }

  public Path interpolate(Path inputFile, Path inputBaseDir, Path targetDirectory, Map<String, String> additionalData) throws IOException {
    final var inputFilename = inputFile.getFileName().toString();
    final var data = new HashMap<String, String>();
    data.put("filename", inputFilename);
    data.put("extension", FileHelper.getFileExtensionWithoutDot(inputFilename));
    data.put("basename", FileHelper.getFileBasename(inputFilename));
    data.put("path", FileHelper.relativizePath(inputBaseDir, inputFile.getParent()));
    if (additionalData != null) {
      data.putAll(additionalData);
    }
    final var substitutor = new StringSubstitutor(data, prefix, suffix, escapeChar);
    final var interpolatedFilename = substitutor.replace(pattern);
    final var interpolatedFile = targetDirectory.resolve(removeStart(interpolatedFilename, "/"));
    return interpolatedFile;
  }
}