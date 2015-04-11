package de.jflex.pmd;

import jflex.LexScan;
import net.sourceforge.pmd.lang.TokenManager;
import net.sourceforge.pmd.lang.ast.AbstractTokenManager;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

/**
 * A {@link TokenManager} for JFlex grammars.
 */
public class JFlexTokenManager implements TokenManager {

  public JFlexTokenManager(Reader source) {

  }

  public Object getNextToken() {

    // TODO
    return null;
  }

  public void setFileName(String fileName) {
    AbstractTokenManager.setFileName(fileName);
  }
}
