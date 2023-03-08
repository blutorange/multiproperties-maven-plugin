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

def assertResultDefault = { name ->
  assertResult("expected", "src/main/resources/i18n", name);
}

def assertResultCustom = { name ->
  assertResult("custom/expected", "custom/output/maven-properties-plugin-basic/custom/output", name);
}

def assertResultHandlersBuiltin = { name ->
  assertResult("src/main/handler-builtin/expected", "target/generated-resources/input/dir-handler-builtin", name);
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

