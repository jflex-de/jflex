package edu.tum.cup2.parser.exceptions;


import edu.tum.cup2.parser.actions.Reduce;
import edu.tum.cup2.parser.actions.Shift;
import edu.tum.cup2.scanner.ScannerToken;


/**
 * Exception that is raised when there are two consecutive
 * equal precedence terminals
 * 
 * @author Michael Hausmann
 */
public class ConsecutiveNonAssocException extends LLkParserException
{
	private static final long serialVersionUID = -2974049956302726506L;
	public ScannerToken nonterminal;
	
	public ConsecutiveNonAssocException(ScannerToken currenttoken)
	{
		super("@ "+currenttoken.getLine() +"/"+currenttoken.getColumn()+", Symbol "+currenttoken.getSymbol()+": Two or more consecutive terminals with equal"
				+ " precedence that were declared \"nonassoc\" finally occured in the source file; unsure, which one should be treated with precedence.\n"+
                        "Either equip Your grammar with appropriate precedences or restate Your current statement unambiguously!");
                nonterminal=currenttoken;
	}
	
}
