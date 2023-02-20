# multiproperties-maven-plugin

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
output files. You may want to use the `baseDir`, `baseSourceDir`, `baseTargetDir` and `fileSets` settings
to configure which files to process.

---


Currently there is only an Eclipse addon editor which creates the derived `*.properties` files when the multiproperties file is saved. This is bad because

* it is redundant: you now have the same data multiple times in your repository
* it is error-prone: you may forget to open the multiproperties files and save it after a merge, which may result in outdated properties files
* it creates unnecessary merge conflicts in derived properties files

Instead generate the properties files from the multiproperties file during the Maven build instead, which is what this plugin does.

Caveats:

* The `Text File Handler` is not supported as of now. The Eclipse add-on seems to be broken currently and does not generate the text files (or rather, the generated files only contain `nullnullnull...`). If anybody needs this and knows the format of these generated text files, open an issue and let me know.

Other than that, it should generate the same output as the Eclipse addon, with a few intentional exceptions:

* `Write disabled properties as comment` is turned on and a multiline property is disabled, the Eclipse addon
  fails to comment every line other than the first line of the multiline property, which is a bug. This plugin adds
  the `#` before each line.
* Backslashes `\` in the value of a property are properly escaped as two backslashes `\\`. The Eclipse add-on fails to
  do so when the file encoding is UTF8 or UTF16.
* Characters not representable in US-ASCII are escaped when US-ASCII is selected as the encoding. The Eclipse add-on fails
  to escape such characters for US-ASCII, resulting in question marks `?` being written to the properties file.

Note that the setting `Insert description of column in the beginning as comment` currently does not do what it advertises, it only add a line break at the beginning of the file. This plugin intentionally reproduces this behavior as I would not consider that to be
a major bug and it does not result in broken properties files or missing data.