def assertResult = { expectedDir, actualDir,name ->
  File actualDe = new File( basedir, "${actualDir}/${name}_de.properties" );
  File actualEn = new File( basedir, "${actualDir}/${name}_en.properties" );
  
  assert actualDe.isFile();
  assert actualEn.isFile();
  
  File expectedDe = new File( basedir, "${expectedDir}/${name}_de.properties" );
  File expectedEn = new File( basedir, "${expectedDir}/${name}_en.properties" );
  
  assert expectedDe.text.equals(actualDe.text);
  assert expectedEn.text.equals(actualEn.text); 
}

def assertResultNoComments = { expectedDir, actualDir, name, encoding ->
  File actualDe = new File( basedir, "${actualDir}/${name}_de.properties" );
  File actualEn = new File( basedir, "${actualDir}/${name}_en.properties" );
  
  assert actualDe.isFile();
  assert actualEn.isFile();
  
  File expectedDe = new File( basedir, "${expectedDir}/${name}_de.properties" );
  File expectedEn = new File( basedir, "${expectedDir}/${name}_en.properties" );
  
  String actualDeNoComments = java.util.regex.Pattern.compile("^#.*\n",java.util.regex.Pattern.MULTILINE).matcher(actualDe.getText(encoding)).replaceAll("");
  String actualEnNoComments = java.util.regex.Pattern.compile("^#.*\n",java.util.regex.Pattern.MULTILINE).matcher(actualEn.getText(encoding)).replaceAll("");
  
  assert expectedDe.getText(encoding).equals(actualDeNoComments);
  assert expectedEn.getText(encoding).equals(actualEnNoComments); 
}

def assertResultDefault = { name ->
  assertResult("expected", "src/main/resources/i18n", name);
}

def assertResultCustom = { name ->
  assertResult("custom/expected", "custom/output/maven-properties-plugin-basic/custom/output", name);
}

def assertResultHandlersBuiltin = { name ->
  assertResult("src/main/handler-builtin/expected", "target/generated-resources/input/dir-handler-builtin", name);
}

def assertResultHandlersSimple = { name ->
  assertResultNoComments("src/main/handler-simple/expected", "target/generated-resources/handler-simple/output", name, "UTF-8");
}

def assertResultHandlersSimpleUsAscii = { name ->
  assertResultNoComments("src/main/handler-simple-usascii/expected", "target/generated-resources/handler-simple-usascii/output", name, "US-ASCII");
}

def assertResultHandlersSimpleUtf16Be = { name ->
  assertResultNoComments("src/main/handler-simple-utf16be/expected", "target/generated-resources/handler-simple-utf16be/output", name, "UTF-16BE");
}

assertResultDefault.call("localization");
assertResultDefault.call("insertFileDescription");
assertResultDefault.call("insertColumnDescription");
assertResultDefault.call("insertFileAndColumnDescription");
assertResultDefault.call("writeDisabledAsComments");
assertResultDefault.call("disableDefaultValues");
assertResultDefault.call("utf16");
assertResultDefault.call("specialCharsIso88591");
assertResultDefault.call("specialCharsUsAscii");
assertResultDefault.call("specialCharsUtf8");
assertResultDefault.call("specialCharsUtf16");
assertResultDefault.call("specialCharsUtf16Be");
assertResultDefault.call("specialCharsUtf16Le");
assertResultDefault.call("singleAndMultiline");

assertResultCustom.call("lang");
assert new File(basedir, "custom/output/maven-properties-plugin-basic/custom/output/lang_de.properties").isFile();
assert new File(basedir, "custom/output/maven-properties-plugin-basic/custom/output/lang_en.properties").isFile();
assert !new File(basedir, "custom/output/maven-properties-plugin-basic/custom/output/something-else_de.properties").isFile();
assert !new File(basedir, "custom/output/maven-properties-plugin-basic/custom/output/something-else_en.properties").isFile();

assertResultHandlersBuiltin("translations");
assertResultHandlersBuiltin("translations2");
assertResultHandlersBuiltin("sub/more");

assertResultHandlersSimple("simpleMessages");
assertResultHandlersSimple("specialChars");

assertResultHandlersSimpleUsAscii("simpleMessages");
assertResultHandlersSimpleUsAscii("specialChars");

assertResultHandlersSimpleUtf16Be("simpleMessages");
assertResultHandlersSimpleUtf16Be("specialChars");