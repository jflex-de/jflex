package edu.tum.cup2.semantics;

import java.util.HashMap;

import edu.tum.cup2.grammar.Symbol;


/**
 * This class maps {@link Symbol}s to their {@link SymbolValue} instances.
 * 
 * @author Andreas Wenger
 */
public class SymbolValueClasses
	extends HashMap<Symbol, Class<SymbolValue<Object>>>
{

}
