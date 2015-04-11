package de.jflex.pmd;

import de.jflex.pmd.ast.AstRoot;
import de.jflex.pmd.ast.AstSymbol;
import de.jflex.pmd.ast.CupNode;
import java_cup.runtime.Symbol;
import jflex.LexScan;
import jflex.sym;
import net.sourceforge.pmd.lang.AbstractParser;
import net.sourceforge.pmd.lang.ParserOptions;
import net.sourceforge.pmd.lang.TokenManager;
import net.sourceforge.pmd.lang.ast.AbstractTokenManager;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.ast.ParseException;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


    try {
      LexScan scanner = new LexScan(source);
      // TODO ? scanner.setFile(new File(fileName));
      logger.info(String.format("Scanner is %s", scanner));
      List<AstSymbol> tokens = new ArrayList<AstSymbol>();
      for (Symbol token = scanner.next_token(); token!=null && token.sym != sym.EOF; token = scanner.next_token()) {
        logger.info(String.format("Tokens: %s %s (%s)", sym.terminalNames[token.sym], token.value, scanner.yytext()));
        tokens.add(new AstSymbol(scanner, token));
      }
      return new AstRoot(tokens);
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
