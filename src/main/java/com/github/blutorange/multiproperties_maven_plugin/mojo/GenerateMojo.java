package com.github.blutorange.multiproperties_maven_plugin.mojo;

import static com.github.blutorange.multiproperties_maven_plugin.common.CollectionHelper.areAllCollectionsEmpty;
import static com.github.blutorange.multiproperties_maven_plugin.common.CollectionHelper.isCollectionEmpty;
import static com.github.blutorange.multiproperties_maven_plugin.common.FileHelper.getIncludedFiles;
import static com.github.blutorange.multiproperties_maven_plugin.common.FileHelper.resolve;

import java.nio.file.Path;
import java.util.ArrayList;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.github.blutorange.multiproperties_maven_plugin.common.MultipropertiesGenerator;;

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
   * resolved against the <code>baseDir</code>. Defaults to the <code>src/main/resources</code> directory.
   */
  @Parameter(property = "baseSourceDir", defaultValue = "src/main/resources")
  private String baseSourceDir;

  /**
   * Base directory against which relative output paths in the multiproperties file are resolved. If this this is a
   * relative path, it is resolved against the <code>baseDir</code>. Defaults to the base directory of the topmost
   * parent Maven project, with the name of that Maven project added.
   * <p>
   * Note that the Eclipse multiproperties editor plugin resolves paths against the path of the Eclipse project, which
   * we can only guess in a Maven build. For multi-module Maven projects in Eclipse, it appears to use the path of the
   * parent BOM project.
   */
  @Parameter(property = "baseTargetDir")
  private String baseTargetDir;

  /**
   * Multiproperties files to process. If the path is relative, it is resolved against the given <code>baseDir</code>,
   * i.e. the base directory of the current project by default. Defaults to including
   * <code>**&frasl;*.multiproperties</code>.
   * <p>
   * If you wish to specify different options for different multiproperties files, use multiple executions.
   */
  @Parameter(property = "multipropertiesFiles")
  private FileSet multipropertiesFiles;

  @Parameter(defaultValue = "${project}", readonly = true)
  private MavenProject project;

  /**
   * When <code>true</code>, remove the first path segment from the multiproperties output file path. The first segment
   * is usually the name of the Eclipse project, which may be different from the name of the folder itself.
   */
  @Parameter(property = "removeFirstPathSegment", defaultValue = "true")
  private boolean removeFirstPathSegment;

  /**
   * When set to <code>true</code>, skip execution of this plugin. Can also be set on the command line via
   * <code>-Dskip.multiproperties</code>
   */
  @Parameter(property = "skip", defaultValue = "${skip.multiproperties}")
  private boolean skip;

  @Override
  public void execute() throws MojoExecutionException {
    applyDefaults();

    if (skip) {
      getLog().info("Skipping multiproperties Maven plugin as <skip> option was set to <true>.");
      return;
    }

    final var basePath = resolve(project.getBasedir().toPath().toAbsolutePath(), baseDir);
    final var baseSourcePath = resolve(basePath, baseSourceDir);
    final var baseTargetPath = resolve(basePath, baseTargetDir);

    if (getLog().isDebugEnabled()) {
      getLog().debug(String.format("Resolved paths: base=<%s>, source=<%s>, target=<%s>", basePath, baseSourcePath, baseTargetPath));
    }

    final var inputFiles = getIncludedFiles(baseSourcePath, multipropertiesFiles);

    if (isCollectionEmpty(inputFiles)) {
      getLog().warn("Did not find any multiproperties files to process, skipping plugin.");
      return;
    }

    final var generator = createGenerator(baseSourcePath, baseTargetPath);

    for (final var inputFile : inputFiles) {
      getLog().info(String.format("Processing multiproperties files <%s>", inputFile));
      try {
        generator.process(inputFile);
      }
      catch (final Exception e) {
        throw new MojoExecutionException("Could not processing multiproperties file", e);
      }
    }
  }

  private void applyDefaults() {
    if (multipropertiesFiles == null || areAllCollectionsEmpty(multipropertiesFiles.getIncludes(), multipropertiesFiles.getExcludes())) {
      multipropertiesFiles = new FileSet();
      multipropertiesFiles.setIncludes(new ArrayList<>());
      multipropertiesFiles.getIncludes().add("**/*.multiproperties");
    }
    if (baseTargetDir == null) {
      var parent = project;
      while (parent.getParent() != null) {
        parent = parent.getParent();
      }
      baseTargetDir = parent.getBasedir().getAbsolutePath();
    }
  }

  private MultipropertiesGenerator createGenerator(Path baseSourceDir, Path baseTargetDir) {
    final var builder = MultipropertiesGenerator.builder();
    builder.withLogger(getLog());
    builder.withSourceDir(baseSourceDir);
    builder.withTargetDir(baseTargetDir);
    builder.withRemoveFirstPathSegment(removeFirstPathSegment);
    return builder.build();
  }
}
