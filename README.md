# multiproperties-maven-plugin

Maven plugin for generating derived files from a multiproperties file, see https://github.com/skazsi/multiproperties and https://marketplace.eclipse.org/content/multiproperties

Currently there is only an Eclipse addon editor which creates the derived `*.properties` files when the multiproperties file is saved. This is bad because

* it is redundant: you now have the same data multiple times in your repository
* it is error-prone: you may forget to open the multiproperties files and save it after a merge, which may result in outdated properties files
* it creates unnecessary merge conflicts in derived properties files

Instead generate the properties files from the multiproperties file during the Maven build instead, which is what this plugin does.

Caveats:

* The `Text File Handler` is not supported currently. The Eclipse addon seems to be broken currently and does not generate the text files. If anybody needs this and knows the format of these generated text files, open an issue and let me know.
* The options for including comments in the Java properties files are not supported for now. I do not see much need for this. If you need this feature, open an issue and let me know.

Other than that, it should generate the same output as the Eclipse addon.
