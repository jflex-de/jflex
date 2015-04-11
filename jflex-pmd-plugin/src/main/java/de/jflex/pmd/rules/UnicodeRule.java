package de.jflex.pmd.rules;

import de.jflex.pmd.JFlexLanguageModule;
import de.jflex.pmd.JFlexRuleViolationFactory;
import de.jflex.pmd.ast.ASTRoot;
import de.jflex.pmd.ast.ASTSymbol;
import de.jflex.pmd.ast.CupNode;
import jflex.sym;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.rule.AbstractRule;

import java.util.List;
import java.util.logging.Logger;

public class UnicodeRule extends AbstractRule {
  public static Logger logger = Logger.getLogger(UnicodeRule.class.getName());
  public UnicodeRule() {
    super.setLanguage(LanguageRegistry.getLanguage(JFlexLanguageModule.NAME));
  }

  public void apply(List<? extends Node> list, RuleContext ruleContext) {
    logger.info("Applying rule");
    for (Node e : list) {
      visit((CupNode)e, ruleContext);
    }
  }

  private void visit(CupNode node, RuleContext context) {
    logger.finer(String.format("Visiting %s", node));
    if (node instanceof ASTRoot) {
      ASTRoot root = (ASTRoot) node;
      String unicodeToken = null;
      for (ASTSymbol token : root.getTokens()) {
        if (isUnicodeChar(token)) {
          // Warn for the next character.
          unicodeToken = token.text;
        } else {
          if (unicodeToken != null && isHexadecimalCharacter(token)) {
            String msg = String.format("'%s' is followed by character '%s'", unicodeToken, token.text);
            logger.warning(msg);
            JFlexRuleViolationFactory.INSTANCE.addViolation(context, this, token, msg, root.getTokens().toArray());
          }
          unicodeToken = null;
        }
      }
    }
  }

  private boolean isHexadecimalCharacter(ASTSymbol token) {
    if (! isChar(token)) {
      return false;
    }
    char c = token.text.charAt(0);
    return 'a' <= c && c <= 'f'
        || 'A' <=c && c <= 'F'
        || '0' <= c && c <= '9';
  }

  private boolean isUnicodeChar(ASTSymbol token) {
    return isChar(token) && token.text.startsWith("\\u");
  }

  private boolean isChar(ASTSymbol token) {
    return token.jjtGetId() == sym.CHAR;
  }

}
