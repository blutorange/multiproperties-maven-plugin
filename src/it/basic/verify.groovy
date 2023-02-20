File actualDe = new File( basedir, "src/main/properties/i18n/localization_de.properties" );
File actualEn = new File( basedir, "src/main/properties/i18n/localization_en.properties" );

assert actualDe.isFile();
assert actualEn.isFile();

File expectedDe = new File( basedir, "expected/localization_de.properties" );
File expectedEn = new File( basedir, "expected/localization_en.properties" );

assert expectedDe.text.equals(actualDe.text);
assert expectedEn.text.equals(actualEn.text);
