package de.jflex.pmd;

import de.jflex.pmd.ast.ASTSymbol;
import de.jflex.pmd.ast.CupNode;
import java_cup.runtime.Symbol;
import jflex.LexParse;
import jflex.LexScan;
import net.sourceforge.pmd.lang.AbstractParser;
import net.sourceforge.pmd.lang.ParserOptions;
import net.sourceforge.pmd.lang.TokenManager;
import net.sourceforge.pmd.lang.ast.AbstractTokenManager;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.ast.ParseException;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class JFlexParser extends AbstractParser {
  Logger logger = Logger.getLogger(JFlexParser.class.getName());
  private Map<Symbol, CupNode> nodeCache = new HashMap<Symbol, CupNode>();

  public JFlexParser(ParserOptions parserOptions) {
    super(parserOptions);
  }

  /**
   * Returns a new instance of {@link JFlexTokenManager}.
   */
  @Override
  protected TokenManager createTokenManager(Reader source) {
    logger.info("Create new " + JFlexTokenManager.class);
    return new JFlexTokenManager(source);
  }

  /**
   * Returns the root node of the AST tree obtained by parsing the Reader source.
   */
  public Node parse(String fileName, Reader source) throws ParseException {
    AbstractTokenManager.setFileName(fileName);
    LexScan scanner = new LexScan(source);
    // TODO ? scanner.setFile(new File(fileName));
    LexParse parser = new LexParse(scanner);
    try {
      Symbol rootSymbol = parser.parse();
      logger.info(String.format("File %s parsed. AST root: %s", fileName, rootSymbol.toString()));
      return new ASTSymbol(parser, rootSymbol);
    } catch (Exception e) {
      throw new ParseException(e);
    }
  }

  public Map<Integer, String> getSuppressMap() {
    // TODO
    return new HashMap<Integer, String>();
  }

  public boolean canParse() {
    return true;
  }
}
