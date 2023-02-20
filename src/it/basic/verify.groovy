def assertResult = { name ->
  File actualDe = new File( basedir, "src/main/properties/i18n/${name}_de.properties" );
  File actualEn = new File( basedir, "src/main/properties/i18n/${name}_en.properties" );
  
  assert actualDe.isFile();
  assert actualEn.isFile();
  
  File expectedDe = new File( basedir, "expected/${name}_de.properties" );
  File expectedEn = new File( basedir, "expected/${name}_en.properties" );
  
  assert expectedDe.text.equals(actualDe.text);
  assert expectedEn.text.equals(actualEn.text); 
}

assertResult.call("localization");
assertResult.call("insertFileDescription");
assertResult.call("insertColumnDescription");
assertResult.call("insertFileAndColumnDescription");
assertResult.call("writeDisabledAsComments");
assertResult.call("disableDefaultValues");
assertResult.call("utf16");
