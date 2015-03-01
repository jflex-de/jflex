package edu.tum.cup2.semantics;

import static edu.tum.cup2.semantics.SymbolValue.NoValue;

import java.io.Serializable;

import edu.tum.cup2.grammar.SpecialTerminals;
import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.scanner.ScannerToken;

/**
 * Objects of this class represent the {@link SymbolValue} of the
 * SpecialTerminal {@link Error}. They maintain different information about the
 * error-recovery taken place.
 * 
 * E.g. may be used for further recovery from the error or for describing the
 * error to the user.
 * 
 * @author Stefan Dangl
 * 
 **/
public class ErrorInformation implements Serializable {

	/**
	 * generated
	 */
	private static final long serialVersionUID = 507880614015208318L;

	private final ScannerToken<? extends Object> crashToken;
	private final Object[] poppedValues;
	private final ScannerToken<? extends Object>[] poppedTokens;
	private final ScannerToken<? extends Object>[] readTokens;
	private final Terminal[] expectedTerminals;
	private final boolean recovered;
	private final int beginLine;
	private final int beginColumn;
	private final int endLine;
	private final int endColumn;
	private final Object[] proposals;
	private final Object insertedValue;
	
	/**
	 * Creates a new {@link ErrorInformation}. Not to be used normally!
	 **/
	public ErrorInformation(
	    ScannerToken<? extends Object> crash_token,
	    boolean recovered,
		Object[] poppedValues,
		ScannerToken<? extends Object>[] poppedTokens,
		ScannerToken<? extends Object>[] readTokens,
		Terminal[] expectedTerminals,
		int beginLine, int beginColumn, int endLine, int endColumn
	) {
		this(crash_token, recovered, poppedValues, poppedTokens, readTokens,
		 expectedTerminals, beginLine, beginColumn, endLine, endColumn, null, NoValue);
	}

	public ErrorInformation(ScannerToken<? extends Object> crash_token,
		boolean recovered, Object[] poppedValues,
		ScannerToken<? extends Object>[] poppedTokens,
		ScannerToken<? extends Object>[] readTokens, Terminal[] expectedTerminals,
		int beginLine, int beginColumn, int endLine, int endColumn, Object[] proposals,
		Object insertedValue
	) {
		this.poppedValues       = poppedValues;
		this.poppedTokens       = poppedTokens;
		this.readTokens         = readTokens;
		this.expectedTerminals  = expectedTerminals;
		this.crashToken         = crash_token;
		this.recovered          = recovered;
		this.beginLine          = beginLine;
		this.beginColumn        = beginColumn;
		this.endLine            = endLine;
		this.endColumn          = endColumn;
		this.proposals = proposals;
		this.insertedValue = insertedValue;
	}

  /**
	 * Retrieve information on expected terminals.
	 * A token not included in this set of terminals lead to this error.
	 * 
	 * @return Returns those terminals which could have
	 **/
	public Terminal[] getExpectedTerminals() {
		return expectedTerminals;
	}
	
	/**
	 * Retrieve information on popped values.
	 * 
	 * @return Returns those values which have been created from correct
	 * tokens during reductions taken place before encountering the error.
	 **/
	public Object[] getPoppedValues() {
		return poppedValues;
	}

	/**
	 * Retrieve information on correctly read tokens.
	 * 
	 * @return Returns those tokens matched by the terminal Error, which
	 * have been read before encountering the error.
	 * These tokens have been used in order to create the
	 * popped values returned by {@link #getPoppedValues() getPoppedValues()}.
	 **/
	public ScannerToken<? extends Object>[] getCorrectTokens() {
		return poppedTokens;
	}

	/**
	 * Retrieve information on non-correctly read tokens.
	 * 
	 * @return Returns the first token which lead to the error
	 * followed by all tokens read in order to recover
	 * from the error.
	 **/
	public ScannerToken<? extends Object>[] getBadTokens() {
		return readTokens;
	}

	/**
	 * Retrieve all tokens matched by the terminal Error.
	 * 
	 * @return An array, which is a concatenation
	 * of {@link getCorrectTokens()} and {@link getBadTokens()}.
	 **/
	@SuppressWarnings("unchecked")
	public ScannerToken<Object>[] getTokens()
	{
		ScannerToken<Object>[] ret = (ScannerToken<Object>[])new ScannerToken[poppedTokens.length+readTokens.length];
		System.arraycopy(poppedTokens, 0, ret, 0, poppedTokens.length);
		System.arraycopy(readTokens, 0, ret, poppedTokens.length, readTokens.length);
		return ret;
	}
	
	/**
	 * Simple string-representation of the tokens
	 * stored in this {@link ErrorInformation}
	 * in the form of "int myVariable >>> expected either = or ; here [line:55, column:15] - found instead : 5 ; <<<"
	 **/
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		if (beginLine!=-1)
		  sb.append("@["+beginLine+":"+beginColumn+"-"+getEndLine()+":"+getEndColumn()+"] ");
		for(ScannerToken<? extends Object> t:getCorrectTokens())
		{
			if (t.hasValue())
				sb.append(t.getValue());
			else
				sb.append(t);
			sb.append(" ");
		}
		sb.append(">>> ");
	    if (proposals!=null)
	    {
	      sb.append("use"); 
	      for(Object t:proposals)
	      {
	        sb.append(" ");
	        sb.append(t);
	      }
	      sb.append(" to complete - ");
	    }/*
	    if (insertedValue!=NoValue && insertedValue!=null)
	    {
	      sb.append("inserted ");
	      sb.append(this.insertedValue);
	      sb.append(" - ");
	    }*/
		if (getExpectedTerminals() != null && getExpectedTerminals().length > 0) {
			sb.append("expected");
			if (getExpectedTerminals().length > 1)
				sb.append(" either");
			boolean do_Or = false;
			for (Terminal t : getExpectedTerminals()) {
				if (do_Or)
					sb.append(" or");
				do_Or = true;
				sb.append(" ");
				sb.append(t);
			}
			sb.append(" here");
			if (getCrashToken().getLine() != -1) {
				sb.append(" (");
				sb.append(getCrashToken().getLine());
				sb.append(":");
				sb.append(getCrashToken().getColumn());
				sb.append(")");
			}
		}
		if (getBadTokens()!=null && getBadTokens().length>0)
		{
			sb.append(" - found instead :");
			for(ScannerToken<? extends Object> t:getBadTokens())
			{
				sb.append(" ");
				if (t.hasValue())
					sb.append(t.getValue());
				else
					sb.append(t);
			}
		}
		sb.append(" <<<");
		if (crashToken!=null &&
			crashToken.getSymbol()!=SpecialTerminals.EndOfInputStream &&
			getBadTokens().length==0 )
		{
			sb.append(" ");
			if (crashToken.hasValue())
				sb.append(crashToken.getValue());
			else
				sb.append(crashToken);
		}
		return sb.toString();
	}

	/**
	 * Returns the token which was responsible
	 * for the error to occur.
	 * This token normally is the same as the
	 * first token of {@link getBadTokens()},
	 * which may be empty.
	 **/
  public ScannerToken<? extends Object> getCrashToken() {
    return crashToken;
  }
  
  /**
   * Retrieves information about exact position
   * where the error occured.
   * 
   * @return Line-number of first bad token.
   **/
  public int getLine()
  {
    return crashToken.getLine();
  }
  /**
   * Retrieves information about exact position
   * where the error occured.
   * 
   * @return Position in line of first bad token (column).
   **/
  public int getColumn()
  {
    return crashToken.getColumn();
  }

  /**
   * Retrieves tokens read after the error.
   * 
   * (not implemented yet)
   * 
   * @return An amount of num tokens starting from
   * {@link crashToken() the first problematic token}.
   **//*
  public List<ScannerToken<Object>> lookahead( int num )
  {
    // TODO : Access parser
    return null;
  }*/
  /**
   * Retrieves tokens read before the error.
   * 
   * (not implemented yet)
   * 
   * @return An amount of num tokens in front of
   * {@link crashToken() the first problematic token}.
   **//*
  public List<ScannerToken<Object>> lookback( int num )
  {
    // TODO : Access parser
    return null;
  }*/

  /** Has the error been recovered from,
   *  or is it unrecoverable? **/
  public boolean isRecovered() {
    return recovered;
  }

  /** Returns the line in which the problematic zone starts **/
  public int getBeginLine() {
    return beginLine;
  }

  /** Returns the position where the problematic zone starts **/
  public int getBeginColumn() {
    return beginColumn;
  }

  /** Returns the line in which the problematic zone ends **/
  public int getEndLine() {
    return endLine;
  }

  /** Returns the position where the problematic zone ends **/
  public int getEndColumn() {
    return endColumn;
  }

  /** Returns the line in which the problematic zone was detected **/
  public int getCrashLine() {
    return crashToken.getLine();
  }

  /** Returns the position where the problematic zone was detected **/
  public int getCrashColumn() {
    return crashToken.getColumn();
  }

  /** Returns proposed values which could be inserted **/
  public Object[] getProposals() {
    return proposals;
  }

  /** Returns the actual value which could be inserted **/
  public Object getInsertedValue() {
    return insertedValue;
  }
  
}
