package de.jflex.pmd;

import net.sourceforge.pmd.lang.TokenManager;
import net.sourceforge.pmd.lang.ast.AbstractTokenManager;

import java.io.Reader;
import java.util.logging.Logger;

/**
 * A {@link TokenManager} for JFlex grammars.
 */
public class JFlexTokenManager implements TokenManager {
  public static Logger logger = Logger.getLogger(TokenManager.class.getName());

  public JFlexTokenManager(Reader source) {
    logger.info("Create new " + JFlexTokenManager.class);
  }

  public Object getNextToken() {
    logger.fine("Next token");
    return null;
  }

  public void setFileName(String fileName) {
    AbstractTokenManager.setFileName(fileName);
  }
}
