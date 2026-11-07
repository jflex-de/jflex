package edu.tum.cup2.generator.items;

import edu.tum.cup2.generator.states.LALR1CPState;


/**
 * Go-to context propagation link between a source
 * {@link LALR1CPStateItem} and a destination item, which
 * is represented by a {@link LALR1CPState} and a {@link LR0Item}.
 * 
 * Why not use two {@link LALR1CPStateItem}s? The reason for that is,
 * that we may notice later that the link points to a state which is
 * already existing. Then we would have to redirect the link. By using
 * a {@link LR0Item}, which equals by value, it is still valid even
 * when the link was redirected.
 * 
 * For the key however, we have to use a {@link LALR1CPStateItem}, which
 * equals by reference, so that we can manage all links in a global
 * hashmap.
 * 
 * @author Andreas Wenger
 */
public final class CPGoToLink
{
	
	private final LALR1CPItem source;
	private final LALR1CPState targetState;
	private final LR0Item targetItem;

	
	public CPGoToLink(LALR1CPItem source, LALR1CPState targetState, LR0Item targetItem)
	{
		this.source = source;
		this.targetState = targetState;
		this.targetItem = targetItem;
	}

	
	public LALR1CPItem getSource()
	{
		return source;
	}

	
	public LALR1CPState getTargetState()
	{
		return targetState;
	}

	
	public LR0Item getTargetItem()
	{
		return targetItem;
	}
	
	
	public CPGoToLink withTargetState(LALR1CPState targetState)
	{
		return new CPGoToLink(source, targetState, targetItem);
	}

}
