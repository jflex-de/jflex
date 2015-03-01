package edu.tum.cup2.semantics;


import static edu.tum.cup2.semantics.SymbolValue.NoValue;

import java.lang.reflect.Method;

import edu.tum.cup2.parser.LRParser;
import edu.tum.cup2.spec.exceptions.IllegalSpecException;
import edu.tum.cup2.spec.util.RHSItem;

/**
 * Class around an action method.
 * Replaced by closures later if possible.
 * 
 * @author Andreas Wenger
 * @author Michael Hausmann 
 */

public class Action
	implements RHSItem
{
	
	protected transient Method method; //can not be final as it has to be set after de-serializing the Action object
	
	/**
	 * @serial actionSubclass is actually used for persisting the Action subclass that represents the semantic action
	 * @since 1
	 */
	protected Class<Action> actionSubclass; //this field is actually used for serialization 
	
	protected transient int paramsCount; //can not be final as it has to be set after de-serializing the Action object
	protected transient boolean returnsVoid; //can not be final as it has to be set after de-serializing the Action object
	
	private transient LRParser parserInstance = null; //associated parser instance
	
	/**
	 * Creates a new instance of a semantic action.
	 */
	@SuppressWarnings("unchecked")
	public Action()
	{
		init((Class<Action>) getClass());
	}
	
	protected void init(Class<Action> myclass)
	{
		actionSubclass = myclass;
		if(myclass == null) return;
		for (Method m : myclass.getMethods())
		{
			if (m.getName().equals("a"))
			{
				this.method = m;
				this.method.setAccessible(true);
				this.paramsCount = this.method.getParameterTypes().length;
				//we are not allowed to call a public method of an inner class,
				//but we can circumvent that
				this.returnsVoid = method.getReturnType().equals(Void.TYPE);
				return;
			}
		}
		throw new IllegalSpecException("Action has no method called \"a\"");
	}
	
	
	/**
	 * Executes the Action of this class. By default a method a(..) is searched
	 * over reflections and called with the current parameters. Don't forget to
	 * set the parameters previously!
	 * 
	 * @return
	 */
	public Object doAction(Object[] parameters) throws Exception {
		Method method = getMethod();
		Object ret = method.invoke(this, parameters);
		if (isVoidReturn())
			return NoValue;
		else
			return ret;
	}
	
	
	/**
	 * Gets the method assigned to this semantic action.
	 */
	public Method getMethod()
	{
		return method;
	}
	
	/**
	 * Gets the number of parameters the method accepts.
	 */
	public int getParamsCount()
	{
		return paramsCount;
	}
	
	/**
	 * Returns true, if the result type of the method is void.
	 */
	public boolean isVoidReturn()
	{
		return returnsVoid;
	}
	
	/**
	 * Returns the Name of the Action class
	 */
	protected String getActionSubclassName()
	{
		if(null == this.actionSubclass) return null;
		return this.actionSubclass.getName();
	}
	
	/**
	 * Returns the associated parser instance
	 */
	public LRParser getParser()
	{
		return this.parserInstance;
	}
	
	/**
	 * Sets the associated parser instance
	 */
	public void setParser(LRParser p)
	{
		this.parserInstance = p;
	}
}
