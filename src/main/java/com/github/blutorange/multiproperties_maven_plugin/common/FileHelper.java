package com.github.blutorange.multiproperties_maven_plugin.common;

import static com.github.blutorange.multiproperties_maven_plugin.common.CollectionHelper.isCollectionEmpty;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.removeStart;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.commons.lang3.ObjectUtils;
import org.codehaus.plexus.util.DirectoryScanner;
import org.sonatype.plexus.build.incremental.BuildContext;

import com.github.blutorange.multiproperties_maven_plugin.mojo.FileSet;
import com.github.blutorange.multiproperties_maven_plugin.mojo.SkipInputMode;
import com.github.blutorange.multiproperties_maven_plugin.mojo.SkipOutputMode;

/**
 * Utility methods for working with paths and files.
 */
public final class FileHelper {
  private FileHelper() {}

  /**
   * Creates the given directory and all parent directories if missing. Does nothing if the directory exists already.
   * @param dir Directory to create.
   * @throws IOException When a directory could not be created.
   */
  public static void createDirectoriesIfMissing(Path dir) throws IOException {
    Files.createDirectories(dir);
  }

  /**
   * Processes the includes and excludes relative to the given base directory, and returns all included files.
   * @param baseDir Base directory of the includes and excludes.
   * @param fileSets File patterns to include and exclude.
   * @return A list of all files matching the given includes and excludes.
   */
  public static List<Path> getIncludedFiles(Path baseDir, List<FileSet> fileSets) {
    final var result = new HashSet<Path>();
    for (final var fileSet : fileSets) {
      final var resolved = getIncludedFiles(baseDir, fileSet);
      result.addAll(resolved);
    }
    return new ArrayList<>(result);
  }

  /**
   * Processes the includes and excludes relative to the given base directory, and returns all included files.
   * @param baseDir Base directory of the includes and excludes. contain a leading dot.
   * @param fileSet File to to resolve.
   * @return A list of all files matching the given includes and excludes.
   */
  public static List<Path> getIncludedFiles(Path baseDir, FileSet fileSet) {
    final var finalBaseDir = isNotEmpty(fileSet.getDirectory()) //
        ? baseDir.resolve(fileSet.getDirectory()) //
        : baseDir;

    final var scanner = new DirectoryScanner();
    scanner.setIncludes(isCollectionEmpty(fileSet.getIncludes()) ? null : fileSet.getIncludes().toArray(String[]::new));
    scanner.setExcludes(fileSet.getExcludes().toArray(String[]::new));
    if (!fileSet.isSkipDefaultExcludes()) {
      scanner.addDefaultExcludes();
    }
    scanner.setCaseSensitive(!fileSet.isCaseInsensitive());
    scanner.setBasedir(finalBaseDir.toFile());
    scanner.scan();

    return Arrays.stream(scanner.getIncludedFiles()) //
        .filter(matchesExtension(fileSet.getExtensions(), fileSet.isCaseInsensitive())) //
        .map(file -> finalBaseDir.resolve(file)) //
        .map(path -> path.toAbsolutePath()) //
        .collect(toList());
  }

  /**
   * @param base Base path against which to make the path relative.
   * @param target Target path to make relative.
   * @return The path of the given {@code target}, relative to the specified {@code base} file.
   * @throws IOException When the file system could not be accessed.
   */
  public static String relativizePath(Path base, Path target) throws IOException {
    if (base == null) {
      return target.toString();
    }
    else {
      return base.relativize(target).toString();
    }
  }

  /**
   * Removes the first segment of the given path, e.g. turns {@code some/path/to/file} into {path/to/file}.
   * @param path Path to process.
   * @return The path with the first segment removed. When the path has no or only a single segment, an empty path is
   * returned.
   */
  public static String removeFirstPathSegmentFromString(String path) {
    if (isEmpty(path)) {
      return "";
    }
    final var parsed = Paths.get(path);
    if (parsed.getNameCount() <= 1) {
      return "";
    }
    return parsed.subpath(1, parsed.getNameCount()).toString();
  }

  /**
   * @param baseDir Base directory.
   * @param target Target to resolve.
   * @return An absolute path, the result of resolving target against the given base directory.
   */
  public static Path resolve(Path baseDir, Path target) {
    return target != null ? baseDir.resolve(target).toAbsolutePath() : baseDir;
  }

  /**
   * @param baseDir Base directory.
   * @param target Target to resolve.
   * @return An absolute path, the result of resolving target against the given base directory.
   */
  public static Path resolve(Path baseDir, String target) {
    return resolve(baseDir, target != null ? Paths.get(target) : null);
  }

  /**
   * @param sourceFile Source file takes as input.
   * @param targetFile Target file generated from the source file.
   * @param skipOutputMode Skip mode.
   * @return <code>true</code> if the target file should be skipped and not be regenerated from the source file.
   * @throws IOException When the files could not be accessed.
   */
  public static boolean shouldSkipOutput(Path sourceFile, Path targetFile, SkipOutputMode skipOutputMode) throws IOException {
    final boolean ouptutFilesExist = Files.exists(targetFile);
    switch (ObjectUtils.defaultIfNull(skipOutputMode, SkipOutputMode.NEWER)) {
      case NEVER:
        return false;
      case NEWER:
        if (ouptutFilesExist) {
          final var targetModified = Files.getLastModifiedTime(targetFile);
          final var sourceModified = Files.getLastModifiedTime(sourceFile);
          return targetModified.compareTo(sourceModified) > 0;
        }
        else {
          return false;
        }
      case EXISTS:
        return ouptutFilesExist;
      default:
        throw new RuntimeException("Unhandled enum: " + skipOutputMode);
    }
  }

  /**
   * @param file Input file to check.
   * @param buildContext Current build context.
   * @param skipInputMode Skip mode.
   * @return <code>true</code> if the input file should be skipped under the given skip mode.
   */
  public static boolean shouldSkipInput(Path file, BuildContext buildContext, SkipInputMode skipInputMode) {
    switch (skipInputMode) {
      case NEVER:
        return false;
      case UNCHANGED:
        return buildContext != null && buildContext.isIncremental() && buildContext.hasDelta(file.toFile());
      default:
        throw new RuntimeException("Unhandled enum: " + skipInputMode);
    }
  }

  /**
   * @param filename Name of a file, may contain a path.
   * @return The extension of the file, without a leading dot.
   */
  public static String getFileExtensionWithoutDot(String filename) {
    return getFileExtensionWithoutDot(filename, false);
  }

  /**
   * @param filename Name of a file, may contain a path.
   * @return The base name of the file, i.e. the file name without the extension.
   */
  public static String getFileBasename(String filename) {
    if (isEmpty(filename)) {
      return "";
    }
    final var startIndex = 1 + Math.max(filename.lastIndexOf("/"), filename.lastIndexOf("\\"));
    final var endIndex = filename.indexOf(".", startIndex);
    return filename.substring(startIndex, endIndex >= 0 ? endIndex : filename.length());
  }

  private static String getFileExtensionWithoutDot(String filename, boolean caseInsensitive) {
    if (isEmpty(filename)) {
      return "";
    }
    final var index = filename.lastIndexOf(".");
    final var extension = index >= 0 ? filename.substring(index + 1) : "";
    return caseInsensitive ? extension.toLowerCase(Locale.ROOT) : extension;
  }

  private static Predicate<? super String> matchesExtension(List<String> extensions, boolean caseInsensitive) {
    final var extensionSet = extensions != null ? normalizeExtensions(extensions, caseInsensitive) : new HashSet<>();
    return includedFilename -> {
      final var extension = getFileExtensionWithoutDot(includedFilename, caseInsensitive);
      return extensionSet.size() == 0 || extensionSet.contains(extension);
    };
  }

  private static Set<String> normalizeExtensions(List<String> extensions, boolean caseInsensitive) {
    return extensions.stream() //
        .filter(ext -> isNotEmpty(ext)).map(ext -> removeStart(ext, ".")) //
        .map(ext -> caseInsensitive ? ext.toLowerCase(Locale.ROOT) : ext).collect(toSet());
  }
}
