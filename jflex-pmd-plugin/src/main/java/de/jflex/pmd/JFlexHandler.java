package de.jflex.pmd;

import net.sourceforge.pmd.lang.AbstractLanguageVersionHandler;
import net.sourceforge.pmd.lang.LanguageVersionHandler;
import net.sourceforge.pmd.lang.Parser;
import net.sourceforge.pmd.lang.ParserOptions;
import net.sourceforge.pmd.lang.rule.RuleViolationFactory;

/**
 * Implementation of {@link LanguageVersionHandler} for JFlex grammars.
 */
public class JFlexHandler extends AbstractLanguageVersionHandler {

  public Parser getParser(ParserOptions parserOptions) {
    return new JFlexParser(parserOptions);
  }

  public RuleViolationFactory getRuleViolationFactory() {
    return JFlexRuleViolationFactory.INSTANCE;
  }
  ///// Methods not implemented /////

//  public DataFlowHandler getDataFlowHandler() {
//    return null;
//  }
//
//  public XPathHandler getXPathHandler() {
//    return null;
//  }
//
//  public RuleViolationFactory getRuleViolationFactory() {
//    return null;
//  }
//
//  public ParserOptions getDefaultParserOptions() {
//    return null;
//  }
//
//  public VisitorStarter getDataFlowFacade() {
//    return null;
//  }
//
//  public VisitorStarter getSymbolFacade() {
//    return null;
//  }
//
//  public VisitorStarter getSymbolFacade(ClassLoader classLoader) {
//    return null;
//  }
//
//  public VisitorStarter getTypeResolutionFacade(ClassLoader classLoader) {
//    return null;
//  }
//
//  public VisitorStarter getDumpFacade(Writer writer, String s, boolean b) {
//    return null;
//  }
//
//  public DFAGraphRule getDFAGraphRule() {
//    return null;
//  }
}
