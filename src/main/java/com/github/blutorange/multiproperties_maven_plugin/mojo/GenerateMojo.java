package com.github.blutorange.multiproperties_maven_plugin.mojo;

import static com.github.blutorange.multiproperties_maven_plugin.common.CollectionHelper.isCollectionEmpty;
import static com.github.blutorange.multiproperties_maven_plugin.common.FileHelper.getIncludedFiles;
import static com.github.blutorange.multiproperties_maven_plugin.common.FileHelper.resolve;
import static com.github.blutorange.multiproperties_maven_plugin.common.FileHelper.shouldSkipInput;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.sonatype.plexus.build.incremental.BuildContext;

import com.github.blutorange.multiproperties_maven_plugin.generator.MultipropertiesGenerator;
import com.github.blutorange.multiproperties_maven_plugin.handler.DefaultHandler;
import com.github.blutorange.multiproperties_maven_plugin.handler.Handler;

/**
 * Goal which generates the derived files from a multiproperties file.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, threadSafe = false)
public class GenerateMojo extends AbstractMojo {
  /**
   * Base directory against which the <code>baseSourceDir</code> and the <code>baseTargetDir</code> are resolved. If
   * this is a relative path, it is resolved against the base directory of the current Maven project. Defaults to the
   * base directory of the current project.
   */
  @Parameter(property = "baseDir", defaultValue = "${project.basedir}")
  private String baseDir;

  /**
   * Base directory against which relative source (input) file paths are resolved. When this is a relative path, it is
   * resolved against the <code>baseDir</code>. Defaults to empty, i.e the same as <code>baseDir</code>.
   */
  @Parameter(property = "baseSourceDir")
  private String baseSourceDir;

  /**
   * Base directory against which relative output paths in the multiproperties file are resolved. If this this is a
   * relative path, it is resolved against the <code>baseDir</code>. Defaults to empty, i.e the same as
   * <code>baseDir</code>.
   * <p>
   * Note that the Eclipse multiproperties editor plugin resolves paths against the path of the Eclipse project, which
   * we can only guess in a Maven build.
   */
  @Parameter(property = "baseTargetDir")
  private String baseTargetDir;

  @Component
  private BuildContext buildContext;

  /**
   * Multiproperties files to process. Relative paths in the <code>directory</code> of a file set are resolved against
   * <code>baseSourceDir</code>, and relative paths in the <code>includes</code> of a file set are resolved against that
   * directory.
   * <p>
   * When both <code>baseSourceDir</code> and <code>fileSets</code> are not given, defaults to all files with the
   * extension <code>multiproperties</code> from all resource folders of the current Maven project. Otherwise, defaults
   * to including <code>**&frasl;*.multiproperties</code>.
   * <p>
   * If you wish to specify different options for different multiproperties files, use multiple executions.
   */
  @Parameter(property = "fileSets")
  private List<FileSet> fileSets;

  /**
   * Manage the output file handlers that control which files are generated from a multiproperties file. By default,
   * this is set to the <code>com.github.blutorange.multiproperties_maven_plugin.handler.DefaultHandler</code>, which
   * delegates to the handler as defined in the multiproperties file.
   * <p>
   * For the built-in handlers: Relative paths are resolved against the <code>baseTargetDir</code>. Since the output
   * path in multiproperties files starts with the name of the Eclipse project, the built-in <code>DefaultHandler</code>
   * also has a <code>removeFirstPathSegment</code> (default: <code>true</code>) to remove that name from the output
   * path, so that the output path is relative to the project's base directory.
   * <p>
   * The built-in handlers also support placeholders in the output path. These can be used to generate the file name
   * dynamically. Placeholder syntax is <code>#{name}</code>. Use <code>##</code> to insert a literal <code>#</code>.
   * Supported placeholder names are:
   * <ul>
   * <li>path - Path to the input file, relative to the <code>baseSourceDir</code>, e.g.
   * <code>src/main/resources/i18n</code></li>
   * <li>filename - File name of the input file, e.g. <code>translations.multiproperties</code></li>
   * <li>basename - Base name of the input file, the file name without the extension, e.g.
   * <code>translations</code></li>
   * <li>extension - Extension of the input file, without a leading dot, e.g. <code>multiproperties</code></li>
   * <li>key - Column key for which to generate the output file, e.g. <code>de</code> or <code>en</code></li>
   * </ul>
   * For example, to generate <code>translations_de.properties</code>, <code>translations_en.properties</code> etc. from
   * <code>translations.multiproperites</code>, you could use:
   * 
   * <pre>
   * &lt;handlers&gt;
   *   &lt;handler implementation=&quot;com.github.blutorange.multiproperties_maven_plugin.handler.JavaPropertiesHandler&quot;&gt;
   *     &lt;configuration&gt;
   *       &lt;outputPath&gt#{path}/#{basename}_#{key}.properties;&lt;/outputPath-&gt;
   *       &lt;encoding&gt;UTF-8&lt;/encodingPath-&gt;
   *     &lt;/configuration&gt;
   *   &lt;/handler&gt;
   * &lt;handlers&gt;
   * </pre>
   * <p>
   * You need to specify the implementation class for each handler you wish to defined. For example, you could specify
   * the default handler like this:
   * 
   * <pre>
   * &lt;handlers&gt;
   *   &lt;handler implementation=&quot;com.github.blutorange.multiproperties_maven_plugin.handler.DefaultHandler&quot;&gt;
   *     &lt;!-- Configure properties for the handler --&gt;
   *   &lt;/handler&gt;
   * &lt;handlers&gt;
   * </pre>
   * 
   * The available properties depend on the implementation class, and are derived from the fields of the implementation
   * POJO class.
   * <p>
   * Built-in handlers are:
   * <dd>
   * <dt>com.github.blutorange.multiproperties_maven_plugin.handler.DefaultHandler
   * <dt>
   * <dd>The default handler which delegates to the handler as defined in the multiproperties file.</dd>
   * <dt>com.github.blutorange.multiproperties_maven_plugin.handler.JavaPropertiesHandler
   * <dt>
   * <dd>Writes to a Java properties file.</dd>
   * <dt>com.github.blutorange.multiproperties_maven_plugin.handler.TextFileHandler
   * <dt>
   * <dd>Writes to a text file.</dd>
   * <dt>com.github.blutorange.multiproperties_maven_plugin.handler.NoneHandler
   * <dt>
   * <dd>Does not write any output files.</dd></dd>
   * <p>
   * To use your own handler implementation, write an implementation of
   * <code>com.github.blutorange.multiproperties_maven_plugin.handler.Handler</code> and include the class in the plugin
   * classpath via <code>&lt;dependencies&gt;</code>.
   */
  @Parameter(property = "handlers")
  private List<Handler> handlers;

  @Parameter(defaultValue = "${project}", readonly = true)
  private MavenProject project;

  /**
   * When set to <code>true</code>, skip execution of this plugin. Can also be set on the command line via
   * <code>-Dskip.multiproperties</code>
   */
  @Parameter(property = "skip", defaultValue = "${skip.multiproperties}")
  private boolean skip;

  /**
   * Whether to skip input files. Possible options are
   * <ul>
   * <li>UNCHANGED - When this plugin is executed as part of an m2e (Maven2Eclipse) incremental build , skip all files
   * that did not have changes since the last build.</li>
   * <li>NEVER - Do not skip any input files.</li>
   * </ul>
   */
  @Parameter(property = "skipInputMode", defaultValue = "UNCHANGED", required = true)
  private SkipInputMode skipInputMode;

  /**
   * This options lets configure how this plugin checks whether it should skip recreating an existing output file.
   * Available options are:
   * <ul>
   * <li>NEWER - Skip execution if the the target file exists already and all input files are older. Whether a file is
   * older is judged according to their modification date.</li>
   * <li>EXISTS - Skip execution if the target file exists already, irrespective of when thee files were last
   * modified.</li>
   * <li>NEVER - Never skip recreating an output file</li>
   * </ul>
   */
  @Parameter(property = "skipOutputMode", defaultValue = "NEWER", required = true)
  private SkipOutputMode skipOutputMode;

  /**
   * <p>
   * When this plugin is executed as part of an m2e (Maven2Eclipse) incremental build and this option is set to
   * <code>true</code>, skip the execution of this plugin.
   * </p>
   * <p>
   * For the m2e integration, this plugin is configured by default to run on incremental builds. When having a project
   * opened in Eclipse, this recreates the minified files every time a source file is changed.
   * </p>
   * <p>
   * You can disable this behavior via the <code>org.eclipse.m2e/lifefycle-mapping plugin</code>. As this is rather
   * verbose, this option offers a convenient way of disabling incremental builds. Please note that technically this
   * plugin is still executed on every incremental build cycle, but exits immediately without doing any work.
   * </p>
   */
  @Parameter(property = "skipRunOnIncremental", defaultValue = "false")
  private boolean skipRunOnIncremental;

  @Override
  public void execute() throws MojoExecutionException {
    applyDefaults();

    if (skip) {
      getLog().info("Skipping multiproperties Maven plugin as <skip> option was set to <true>.");
      return;
    }

    if (buildContext != null && buildContext.isIncremental() && skipRunOnIncremental) {
      getLog().info("skipRunOnIncremental was to true, so skipping incremental build.");
      return;
    }

    final var basePath = resolve(project.getBasedir().toPath().toAbsolutePath(), baseDir);
    final var baseSourcePath = resolve(basePath, baseSourceDir);
    final var baseTargetPath = resolve(basePath, baseTargetDir);

    logPaths(basePath, baseSourcePath, baseTargetPath);

    final var inputFiles = getIncludedFiles(baseSourcePath, fileSets);
    final var changedFiles = filterChangedFiles(inputFiles);

    if (isCollectionEmpty(changedFiles)) {
      logEmptyFiles(inputFiles, changedFiles);
      return;
    }

    final var generator = createGenerator(baseSourcePath, baseTargetPath);

    for (final var inputFile : changedFiles) {
      getLog().info(String.format("=== Processing multiproperties files <%s>", inputFile));
      try {
        generator.process(inputFile);
      }
      catch (final Exception e) {
        throw new MojoExecutionException("Could not process multiproperties file", e);
      }
      getLog().info("\n");
    }
  }

  private void applyDefaults() {
    if (fileSets == null) {
      fileSets = new ArrayList<>();
    }
    if (fileSets.isEmpty()) {
      if (baseSourceDir == null && project != null && project.getBuild() != null && project.getBuild().getResources() != null) {
        fileSets.addAll(newFileSetAllMultipropertiesFromProjectResources(project));
      }
      else {
        fileSets.add(newFileSetIncludeAllMultiproperties());
      }
    }

    if (handlers == null || handlers.isEmpty()) {
      final var handler = new DefaultHandler();
      handler.setRemoveFirstPathSegment(true);
      handlers = new ArrayList<>();
      handlers.add(handler);
    }

    if (baseSourceDir == null) {
      baseSourceDir = "";
    }

    if (baseTargetDir == null) {
      baseTargetDir = "";
    }
  }

  private MultipropertiesGenerator createGenerator(Path baseSourceDir, Path baseTargetDir) {
    final var builder = MultipropertiesGenerator.builder();
    builder.withLogger(getLog());
    builder.withHandlers(handlers);
    builder.withSourceDir(baseSourceDir);
    builder.withTargetDir(baseTargetDir);
    builder.withSkipMode(skipOutputMode);
    return builder.build();
  }

  private List<Path> filterChangedFiles(List<Path> files) {
    final var filtered = new ArrayList<Path>();
    for (final var file : files) {
      final var shouldSkip = shouldSkipInput(file, buildContext, skipInputMode);
      if (shouldSkip) {
        getLog().info(String.format("Skipping input <%s>: %s", file, skipInputMode.getReason()));
      }
      else {
        filtered.add(file);
      }
    }
    return filtered;
  }

  private void logEmptyFiles(List<Path> inputFiles, List<Path> changedFiles) {
    if (isCollectionEmpty(changedFiles)) {
      getLog().warn("Did not find any multiproperties files to process, skipping multiproperties plugin.");
    }
    else {
      getLog().info(String.format("Found %d files, but none had any changes, skipping multiproperties plugin", inputFiles.size()));
    }
  }

  private void logPaths(Path basePath, Path baseSourcePath, Path baseTargetPath) {
    if (getLog().isDebugEnabled()) {
      getLog().debug(String.format("Resolved base path = <%s>", basePath));
      getLog().debug(String.format("Resolved base source path = <%s>", baseSourcePath));
      getLog().debug(String.format("Resolved base target path = <%s>", baseTargetPath));
      for (final var fileSet : fileSets) {
        getLog().debug(String.format("Using file set <%s>", fileSet));
      }
    }
  }

  private static List<FileSet> newFileSetAllMultipropertiesFromProjectResources(MavenProject project) {
    final var fileSets = new ArrayList<FileSet>();
    for (final var resource : project.getBuild().getResources()) {
      final var fileSet = new FileSet();
      fileSet.setDirectory(resource.getDirectory());
      fileSet.setIncludes(resource.getIncludes());
      fileSet.setExcludes(resource.getExcludes());
      fileSet.setExtensions(List.of(".multiproperties"));
      fileSets.add(fileSet);
    }
    return fileSets;
  }

  private static FileSet newFileSetIncludeAllMultiproperties() {
    final var fileSet = new FileSet();
    fileSet.setIncludes(new ArrayList<>());
    fileSet.getIncludes().add("**/*.multiproperties");
    return fileSet;
  }
}
