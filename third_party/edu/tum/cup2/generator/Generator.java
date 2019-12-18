package edu.tum.cup2.generator;

import java.io.PrintStream;

import edu.tum.cup2.grammar.Grammar;
import edu.tum.cup2.parser.tables.LRParsingTable;
import edu.tum.cup2.precedences.Precedences;


/**
 * Abstract class for a parser generator, that
 * creates a parsing table and a start state for a
 * given {@link Grammar}.
 * 
 * This class can write its {@link LRParsingTable} to a Java file that will
 * reconstruct the table when executed. (TODO)
 * 
 * @author Andreas Wenger
 * @author Gero
 */
public abstract class Generator
{
	
	//specification
	protected final Grammar grammar;
	protected final Precedences precedences;
	
	//debugging
	protected final Verbosity verbosity;
	protected final PrintStream debugOut;
	
	
	/**
	 * A generator needs a grammar and precedences (optional) to do its work.
	 * The given verbosity tells the generator how many debug messages are requested.
	 */
	public Generator(Grammar grammar, Precedences precedences, Verbosity verbosity)
	{
		this.grammar = grammar;
		this.precedences = precedences;
		this.verbosity = verbosity;
		this.debugOut = System.out; //at the moment, print all debug messages to System.out
	}
	
	
	/**
	 * Gets the (extended) grammar this generator works with.
	 */
	public Grammar getGrammar()
	{
		return grammar;
	}


	/**
	 * Gets the precedences this generator works with, or null
	 * if there are none.
	 */
	public Precedences getPrecedences()
	{
		return precedences;
	}
	
	
	/**
	 * Gets the requested detail level for debug messages.
	 */
	public Verbosity getVerbosity()
	{
		return verbosity;
	}

	/**
	 * Get the output stream for debug messages
	 */
	public PrintStream getDebugOut()
	{
		return this.debugOut;
	}
}
