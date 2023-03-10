# multiproperties-maven-plugin

## Overview

Maven plugin for generating derived files from a multiproperties file, see 

* https://github.com/skazsi/multiproperties 
* https://marketplace.eclipse.org/content/multiproperties

```xml
<plugin>
	<groupId>com.github.blutorange</groupId>
	<artifactId>multiproperties-maven-plugin</artifactId>
	<version>${multiproperties-maven-plugin.version}</version>
	<executions>
		<execution>
			<goals><goal>generate</goal></goals>
		</execution>
	</executions>
</plugin>
```

By default, this processes all `*.multiproperties` files from all resource folders and writes the `*.properties`
output files to the directory as configured in the multiproperties. You may want to use the `baseDir`,
`baseSourceDir`, `baseTargetDir` and `fileSets` settings to configure which files to process.

---

Currently there is only an Eclipse addon editor which creates the derived `*.properties` files when the multiproperties file is saved. This is bad because

* it is redundant: you now have the same data multiple times in your repository
* it is error-prone: you may forget to open the multiproperties files and save it after a merge, which may result in outdated properties files
* it creates unnecessary merge conflicts in derived properties files

Instead generate the properties files from the multiproperties file during the Maven build instead, which is what this plugin does.

Caveats:

* The `Text File Handler` is not supported as of now. The Eclipse add-on seems to be broken currently and does not generate the text files (or rather, the generated files only contain `nullnullnull...`). If anybody needs this and knows the format of these generated text files, open an issue and let me know.

Other than that, it should generate the same output as the Eclipse addon, including the following "quirks" (see the
Simple properties handler below if you don't care about producing exactly the same output as the Eclipse add-on):

* When `Write disabled properties as comment` is turned on and a multiline property is disabled, the Eclipse addon
  fails to properly comment lines other than the first line of the multiline property.
* Backslashes `\` in the value of a property are not escaped as two backslashes `\\`.
* Characters not representable in US-ASCII are not escaped when US-ASCII is selected as the encoding. The Eclipse
  add-on fails to escape such characters for US-ASCII, resulting in question marks `?` being written to the properties
  file.

Note that the setting `Insert description of column in the beginning as comment` currently does not do what it advertises, it only add a line break at the beginning of the file. This plugin intentionally reproduces this behavior as I would not consider that to be
a major bug and it does not result in broken properties files or missing data.

## Advanced usage

You might also want to disable the Eclipse addon so that it does not produce any output and use it only for editing the
multiproperties file. In that case, you can set the handler to `none` and add a custom handler configuration in the pom:

```xml
<plugin>
	<groupId>com.github.blutorange</groupId>
	<artifactId>multiproperties-maven-plugin</artifactId>
	<version>${multiproperties-maven-plugin.version}</version>
	<executions>
		<execution>
			<id>default</id>
			<goals>
				<goal>generate</goal>
			</goals>
			<configuration>
				<baseDir>${project.basedir}</baseDir>
				<baseSourceDir>src/main/resources</baseSourceDir>
				<baseTargetDir>target/generated-resources</baseTargetDir>
				<handlers>
					<handler implementation="com.github.blutorange.multiproperties_maven_plugin.handler.JavaPropertiesHandler">
						<outputPath>#{path}/#{basename}_#{key}.properties</outputPath>
						<encoding>UTF-8</encoding>
					</handler>
				</handlers>
			</configuration>
		</execution>
	</executions>
</plugin>
```

## Simple properties handler

If you do not care about exactly the same output as the Eclipse add-on, there's also a simple properties handler that
uses the `Properties` class to write a Java *.properties file, which handles escaping correctly. It does not support
comment entries (I also see no need for comments in generated  files). This is especially useful if you only use
the Eclipse add-on to edit multiproperties files, but do not generate any output with the Eclipse add-on (i.e. if you
set the handler to `None`). You can use it like this:

```xml
<plugin>
	<groupId>com.github.blutorange</groupId>
	<artifactId>multiproperties-maven-plugin</artifactId>
	<version>${multiproperties-maven-plugin.version}</version>
	<executions>
		<execution>
			<id>default</id>
			<goals>
				<goal>generate</goal>
			</goals>
			<configuration>
				<baseDir>${project.basedir}</baseDir>
				<baseSourceDir>src/main/resources</baseSourceDir>
				<baseTargetDir>target/generated-resources</baseTargetDir>
				<handlers>
					<handler implementation="com.github.blutorange.multiproperties_maven_plugin.handler.SimpleJavaPropertiesHandler">
						<outputPath>#{path}/#{basename}_#{key}.properties</outputPath>
						<encoding>US-ASCII</encoding>
					</handler>
				</handlers>
			</configuration>
		</execution>
	</executions>
</plugin>
```

## Testing

To run IT tests with debugging:

> mvn verify -Dinvoker.mavenOpts="-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=9001"

## Release

> mvn clean deploy -P release
