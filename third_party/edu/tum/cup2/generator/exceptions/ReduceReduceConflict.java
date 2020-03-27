package edu.tum.cup2.generator.exceptions;

import edu.tum.cup2.grammar.AuxiliaryLHS4SemanticShiftAction;
import edu.tum.cup2.parser.actions.Reduce;


/**
 * Exception for a reduce/reduce conflict.
 * 
 * @author Andreas Wenger
 */
public class ReduceReduceConflict extends GeneratorException
{
	/**  */
	private static final long serialVersionUID = -5837785749070073639L;
	
	String word = null;
	private String msg;
	private final Reduce reduce1;
	private final Reduce reduce2;
	
	
	public ReduceReduceConflict(Reduce r1, Reduce r2)
	{
		reduce1 = r1;
		reduce2 = r2;
	}
	
	
	public ReduceReduceConflict(String message)
	{
		this.msg = message;
		reduce1 = null;
		reduce2 = null;
	}
	
	
	public void setWord(String word)
	{
		this.word = word;
	}
	
	
	@Override
	public String getMessage()
	{
		String rText1 = null;
		String rText2 = null;
		if (reduce1 != null)
		{
			if (reduce1.getProduction().getLHS() instanceof AuxiliaryLHS4SemanticShiftAction)
				rText1 = ((AuxiliaryLHS4SemanticShiftAction) reduce1.getProduction().getLHS()).originatedFrom;
			else
				rText1 = reduce1.getProduction().toString(reduce1.getProduction().getRHS().size());
			if (reduce2.getProduction().getLHS() instanceof AuxiliaryLHS4SemanticShiftAction)
				rText2 = ((AuxiliaryLHS4SemanticShiftAction) reduce2.getProduction().getLHS()).originatedFrom;
			else
				rText2 = reduce2.getProduction().toString(reduce2.getProduction().getRHS().size());
		}
		return "Reduce/reduce conflict between:\n" + "  " + rText1 + "\n" + "  and\n" + "  " + rText2 + "\n"
				+ (msg == null ? "" : msg + " -- ") + (word == null ? "" : "Example word: " + word);
	}
	
	
}
