package edu.tum.cup2.semantics;

import java.util.Arrays;
import java.util.Stack;


/**
 * This class performs a given {@link Action}, using
 * the given stack of semantic values.
 * 
 * @author Andreas Wenger
 */
public class ActionPerformer
{
	
	
	/**
	 * Performs the given action. TODO: optimize for speed
	 * @param action      the {@link Action} instance
	 * @param valueStack  the stack of semantic values, which is not
	 *                    changed by this method
	 * @param rhsSize     the number of right hand side symbols of the
	 *                    corresponding production
	 * @return  the resulting new value. If the method has a void return type, NoValue is returned.
	 */
	public static Object perform(Action action, Stack<Object> valueStack, int rhsSize)
	{
		//collect all semantic values of the production (ignore null values)
		Object[] parameters = new Object[action.getParamsCount()];
		int parametersIndex = 0;
		for (int i = 0; i < rhsSize; i++)
		{
			Object v = valueStack.get(valueStack.size() - 1 - i);
			if (v != SymbolValue.NoValue)
			{
				if (parametersIndex < parameters.length)
				{
					parameters[parameters.length - 1 - parametersIndex] = v;
					parametersIndex++;
				}
				else
				{
					throw new IllegalStateException("Too many parameters for semantic action found on stack!");
				}
			}
		}
		//call the first (and only) method of the action with the collected parameters
		try
		{
			return action.doAction(parameters);
		}
		catch (IllegalArgumentException ex)
		{
			//TODO
			System.err.println("Error:");
			System.err.println("  Requested action: " + action.getMethod().toGenericString());
			System.err.println("  Given parameters: " + Arrays.toString(parameters));
			System.err.println("  RHS size:         " + rhsSize);
			System.err.println("  Value stack:      " + valueStack);
			System.exit(0);
			return null;
		}
		catch (Exception ex)
		{
			//TODO
			ex.printStackTrace();
			//System.exit(0);
			return null;
		}
	}

}
