package edu.tum.cup2.parser.states;

import edu.tum.cup2.generator.items.LLkItem;
import edu.tum.cup2.generator.states.LLkState;
import edu.tum.cup2.parser.tables.LLkParsingTable;


/**
 * The error state for anything which can go wrong in the {@link LLkParsingTable}
 * 
 * @author Gero
 * 
 */
public class LLkErrorState extends LLkState
{
	private static final long serialVersionUID = -9192911347109792768L;
	
	private final String msg;
	
	
	/**
	 * @param msg
	 */
	public LLkErrorState(String msg)
	{
		super(LLkItem.FAKE_ITEM);
		this.msg = msg;
	}
	
	
	public String getMsg()
	{
		return msg;
	}
}
