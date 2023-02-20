def assertResult = { name ->
  File actualDe = new File( basedir, "src/main/resources/i18n/${name}_de.properties" );
  File actualEn = new File( basedir, "src/main/resources/i18n/${name}_en.properties" );
  
  assert actualDe.isFile();
  assert actualEn.isFile();
  
  File expectedDe = new File( basedir, "expected/${name}_de.properties" );
  File expectedEn = new File( basedir, "expected/${name}_en.properties" );
  
  System.out.println(name);
  System.out.println(expectedDe);
  System.out.println(actualDe);
  
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
assertResult.call("specialCharsIso88591");
assertResult.call("specialCharsUsAscii");
assertResult.call("specialCharsUtf8");
assertResult.call("specialCharsUtf16");
assertResult.call("specialCharsUtf16Be");
assertResult.call("specialCharsUtf16Le");
