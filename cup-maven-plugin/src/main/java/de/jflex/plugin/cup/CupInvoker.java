package de.jflex.plugin.cup;

public interface CupInvoker {

  void invoke(String javaPackage, String parserFileName, String symFile, String cupFileName)
      throws Exception;
}
