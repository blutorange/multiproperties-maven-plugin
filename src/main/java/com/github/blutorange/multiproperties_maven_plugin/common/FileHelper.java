package com.github.blutorange.multiproperties_maven_plugin.common;

import static com.github.blutorange.multiproperties_maven_plugin.common.CollectionHelper.isCollectionEmpty;
import static com.github.blutorange.multiproperties_maven_plugin.common.StringHelper.isEmpty;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.codehaus.plexus.util.DirectoryScanner;

import com.github.blutorange.multiproperties_maven_plugin.mojo.FileSet;
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
   * @param files File patterns to include and exclude.
   * @return A list of all files matching the given includes and excludes.
   */
  public static List<Path> getIncludedFiles(Path baseDir, FileSet files) {
    return getIncludedFiles(baseDir, files.getIncludes(), files.getExcludes());
  }

  /**
   * Processes the includes and excludes relative to the given base directory, and returns all included files.
   * @param baseDir Base directory of the includes and excludes.
   * @param includes List of specified includes
   * @param excludes List of specified excludes
   * @return A list of all files matching the given includes and excludes.
   */
  public static List<Path> getIncludedFiles(Path baseDir, List<String> includes, List<String> excludes) {
    if (isCollectionEmpty(includes)) {
      return new ArrayList<>();
    }
    final var excludesArray = excludes.toArray(String[]::new);

    return IntStream.range(0, includes.size()) //
        .mapToObj(i -> Map.entry(i, includes.get(i))) //
        .flatMap(include -> {
          final var scanner = new DirectoryScanner();
          scanner.setIncludes(new String[] { include.getValue() });
          scanner.setExcludes(excludesArray);
          scanner.addDefaultExcludes();
          scanner.setBasedir(baseDir.toFile());
          scanner.scan();
          return Arrays.stream(scanner.getIncludedFiles()) //
              .map(includedFilename -> {
                final var includedFile = baseDir.resolve(includedFilename).toAbsolutePath();
                return Map.entry(include.getKey(), includedFile);
              });
        }) //
        .sorted(Comparator.comparing(Map.Entry::getKey)) //
        .map(Map.Entry::getValue) //
        .filter(distinctByKey(p -> p.toString())) //
        .collect(toList());
  }

  /**
   * @param base Base path against which to make the path relative.
   * @param target Target path to make relative.
   * @return The path of the given {@code target}, relative to the specified {@code base} file.
   * @throws IOException When the file system could not be accessed.
   */
  public static String relativizePath(File base, File target) throws IOException {
    final Path targetPath = Paths.get(target.getCanonicalPath());
    if (base == null) {
      return targetPath.toString();
    }
    else {
      final Path basePath = base.getCanonicalFile().toPath();
      final String relativePath = basePath.relativize(targetPath).toString();
      return relativePath;
    }
  }

  /**
   * @param baseDir Base directory.
   * @param target Target to resolve.
   * @return An absolute path, the result of resolving target against the given base directory.
   */
  public static Path resolve(Path baseDir, File target) {
    return resolve(baseDir, target != null ? target.toPath() : null);
  }

  /**
   * @param baseDir Base directory.
   * @param target Target to resolve.
   * @return An absolute path, the result of resolving target against the given base directory.
   */
  public static Path resolve(Path baseDir, Path target) {
    return target != null ? baseDir.resolve(target).toAbsolutePath() : baseDir;
  }

  private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    final var seen = new HashSet<>();
    return item -> seen.add(keyExtractor.apply(item));
  }

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
}
