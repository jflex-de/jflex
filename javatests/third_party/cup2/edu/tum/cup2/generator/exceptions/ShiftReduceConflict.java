package edu.tum.cup2.generator.exceptions;

import edu.tum.cup2.grammar.AuxiliaryLHS4SemanticShiftAction;
import edu.tum.cup2.grammar.SpecialTerminals;
import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.parser.actions.Reduce;
import edu.tum.cup2.parser.actions.Shift;


/**
 * Exception for a shift/reduce conflict.
 * 
 * @author Andreas Wenger
 */
public class ShiftReduceConflict extends GeneratorException
{
	/**  */
	private static final long serialVersionUID = -3750877152502657460L;
	
	private final Shift shift;
	private final Reduce reduce;
	private final Terminal terminal;
	private String word;
	
	
	public ShiftReduceConflict(Shift shift, Reduce reduce, Terminal terminal)
	{
		this.shift = shift;
		this.reduce = reduce;
		this.terminal = terminal;
		word = null;
	}
	
	
	public void setWord(String word)
	{
		this.word = word;
	}
	
	
	public Shift getShift()
	{
		return shift;
	}
	
	
	public Reduce getReduce()
	{
		return reduce;
	}
	
	
	public Terminal getTerminal()
	{
		return terminal;
	}
	
	
	@Override
	public String getMessage()
	{
		String sText = shift.getProduction().toString(shift.getPosition() - 1);
		String rText = null;
		if (reduce.getProduction().getLHS() instanceof AuxiliaryLHS4SemanticShiftAction)
			rText = ((AuxiliaryLHS4SemanticShiftAction) reduce.getProduction().getLHS()).originatedFrom;
		else
			rText = reduce.getProduction().toString(reduce.getProduction().getRHS().size());
		return "Shift/reduce conflict under terminal \""
				+ terminal
				+ "\" between:\n"
				+ "  "
				+ sText
				+ "\n"
				+ "  and\n"
				+ "  "
				+ rText
				+ "\n"
				+ (word == null ? "" : "  A minimal example input : " + word + " [conflict]"
						+ (getTerminal() == SpecialTerminals.WholeRow ? "" : (" " + getTerminal())) + " ...");
	}
	
	
}
