package edu.tum.cup2.semantics;

import java.io.Serializable;

import edu.tum.cup2.parser.LRParser;


/*
 * This interface may be implemented by the author of the grammar in order to
 * receive call-backs from the parser.
 * 
 * Feel free to override any method of this class.
 * 
 * @author Stefan Dangl
 */
public class ParserInterface
	implements Serializable
{
	private static final long	serialVersionUID	= 2481613275884015641L;

	//public final boolean supplyErrorTokens;
	//public ParserInterface() { this(false); }
	//public ParserInterface( boolean supplyErrorTokens ) { this.supplyErrorTokens = supplyErrorTokens; }

	/**
	 * Initialization of grammar.
	 * 
	 * Before parsing the input, this method will be called.
	 * Initialization of grammar should occur here according to
	 * initialization-arguments. 
	 * 
	 **/
	public void init(LRParser p, Object... initArgs) {
	}

	/**  **/
	public void exit(LRParser p) {
	}

	/**
	 * Determines how many correct tokens have to be read after error-recovery
	 * before acting on the assumption that the error is recovered from.
	 * 
	 * If not enough tokens could be read before encountering the next error,
	 * those two errors will be merged.
	 * 
	 * @return The default implementation returns 3.
	 **/
	public int getErrorSyncSize() {
		return 3;
	}

	// example-methods for errors during generation.
	public void generation_error(String problem) {
	};
	public void generation_error(String problem, Exception hasBeenThrown) {
	}

	public int getMaxTokenInsertTries() {
		return 20;
	}

	public void error(ErrorInformation errInf) {
	}
/*
	public int getTokenErrorSyncSize() {
		return 2;
	};*/

}
