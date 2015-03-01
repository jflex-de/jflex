package edu.tum.cup2.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import edu.tum.cup2.grammar.AuxiliaryLHS4SemanticShiftAction;
import edu.tum.cup2.grammar.NonTerminal;
import edu.tum.cup2.grammar.SpecialTerminals;
import edu.tum.cup2.grammar.Symbol;
import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.parser.LRParser;
import edu.tum.cup2.semantics.Action;
import edu.tum.cup2.semantics.SymbolValue;
import edu.tum.cup2.semantics.SymbolValueClasses;
import edu.tum.cup2.spec.CUP2Specification;
import edu.tum.cup2.spec.exceptions.IllegalSpecException;
import edu.tum.cup2.semantics.ErrorInformation;


/**
 * This is the "secret" slum of this project.
 * All nasty methods using reflection reside here.
 * But don't forget, that as so often in reality
 * it's the small people which make this world possible
 * in which we live.
 *
 * @author Andreas Wenger
 * @author Stefan Dangl
 */
public class Reflection
{


	/**
	 * Gets an array of all {@link Terminal}s that are defined in the enum
	 * "Terminals" in the first class that extends {@link CUP2Specification} in
	 * the current stack trace.
	 */
	public static Terminal[] getTerminals()
	{
		try
		{
			//get terminals as Objects
			Object[] terminals = getEnumElements("Terminals");
			//convert each of the values to a Terminal
			Terminal[] ret = new Terminal[terminals.length];
			for (int i = 0; i < ret.length; i++)
			{
				try
				{
					ret[i] = (Terminal) terminals[i];
				}
				catch (ClassCastException ex)
				{
					throw new IllegalSpecException("The enum called \"Terminals\" must implement " +
							"the Terminal interface!");
				}
			}
			return ret;
		}
		catch (Exception ex)
		{
			//TODO: global error handling
			ex.printStackTrace();
			System.exit(0);
			return null;
		}
	}


	/**
	 * Gets an array of all {@link NonTerminal}s that are defined in the enum
	 * "NonTerminals" in the first class that extends {@link CUP2Specification} in
	 * the current stack trace.
	 */
	public static NonTerminal[] getNonTerminals()
	{
		try
		{
			//get terminals as Objects
			Object[] terminals = getEnumElements("NonTerminals");
			//convert each of the values to a NonTerminals
			NonTerminal[] ret = new NonTerminal[terminals.length];
			for (int i = 0; i < ret.length; i++)
			{
				try
				{
					ret[i] = (NonTerminal) terminals[i];
				}
				catch (ClassCastException ex)
				{
					//TODO: illegalspecexception
					throw new Exception("The enum called \"NonTerminals\" must implement the NonTerminal interface!");
				}
			}
			return ret;
		}
		catch (Exception ex)
		{
			//TODO: global error handling
			ex.printStackTrace();
			System.exit(0);
			return null;
		}
	}


	/**
	 * Gets an array of all elements that are defined in the enum
	 * with the given name in the first class that extends {@link CUP2Specification} in
	 * the current stack trace.
	 */
	@SuppressWarnings("unchecked") private static Object[] getEnumElements(String name)
	{
		try
		{
			//get the spec class
			Class specClass = getSpecClass();
			//get the <name> enum
			Object elements[] = null;
			Class[] childClasses = specClass.getClasses();
			for (int i = 0; i < childClasses.length; i++)
			{
				Class childClass = childClasses[i];
				if (childClass.getSimpleName().equals(name))
				{
					elements = childClass.getEnumConstants();
					break;
				}
			}
			//if terminals is still null, there was no <name> enum
			if (elements == null)
			{
				//TODO: illegalspecexception
				throw new Exception("Specification is missing an enum called \"" + name + "\"!");
			}
			return elements;
		}
		catch (Exception ex)
		{
			//TODO: global error handling
			ex.printStackTrace();
			System.exit(0);
			return null;
		}
	}


	/**
	 * Gets the first class that extends {@link CUP2Specification} in
	 * the current stack trace.
	 */
	@SuppressWarnings("unchecked") public static Class getSpecClass()
	{
		try
		{
			StackTraceElement[] stack = Thread.currentThread().getStackTrace();
			for (StackTraceElement ste : new It<StackTraceElement>(stack))
			{
				String className = ste.getClassName();
				Class classClass = Class.forName(className);
				if (!classClass.equals(CUP2Specification.class) && CUP2Specification.class.isAssignableFrom(classClass))
				{
					//subclass of CUPSpecification found
					return classClass;
				}
			}
			throw new Exception("No class extending CUPSpecification in stack trace!");
		}
		catch (Exception ex)
		{
			//TODO: global error handling
			ex.printStackTrace();
			System.exit(0);
			return null;
		}
	}
	
	/**
	 * Gets the first occurrence of class {@link LRParser} in
	 * the current stack trace.
	 */
	@SuppressWarnings("unchecked") public static StackTraceElement getLRParserClass()
	{
		try
		{
			StackTraceElement[] stack = Thread.currentThread().getStackTrace();
			for (StackTraceElement ste : new It<StackTraceElement>(stack))
			{
				Class classClass = ste.getClass();
				if (!classClass.equals(LRParser.class))
				{
					//LRParser found
					return ste;
				}
			}
			throw new Exception("NO occurance of LRParser in stack trace!");
		}
		catch (Exception ex)
		{
			//TODO: global error handling
			ex.printStackTrace();
			System.exit(0);
			return null;
		}
	}


	/**
	 * Gets the symbol-value-binding classes in a hashmap with the symbols as keys.
	 * These classes are found in the first class that extends {@link CUP2Specification} in
	 * the current stack trace.
	 */
	@SuppressWarnings("unchecked") public static SymbolValueClasses
		getSymbolValueClasses(Terminal[] terminals, NonTerminal[] nonTerminals)
	{
		return getSymbolValueClasses(terminals, nonTerminals, getSpecClass());
	}
	
	/**
     * @see {@link Reflection#getSymbolValueClasses(Terminal[], NonTerminal[])}
     */
	@SuppressWarnings("unchecked") public static SymbolValueClasses
		getSymbolValueClasses(Terminal[] terminals, NonTerminal[] nonTerminals, Class specClass)
	{
		try
		{
			//get all classes that extend the SymbolValue class
			SymbolValueClasses ret = new SymbolValueClasses();
			Class[] childClasses = specClass.getClasses();
			for (int i = 0; i < childClasses.length; i++)
			{
				Class childClass = childClasses[i];
				if (SymbolValue.class.isAssignableFrom(childClass))
				{
					//the symbol is the terminal/nonterminal with the same name
					Symbol symbol = getSymbolByName(childClass.getSimpleName(), terminals, nonTerminals);
					if (symbol == null)
					{
						throw new IllegalSpecException("The SymbolValue \"" + childClass.getSimpleName() + "\" belongs " +
							"to none of the available terminals/nonterminals, but it must have the same name.");
					}
					ret.put(symbol, childClass);
				}
			}
			return ret;
		}
		catch (Exception ex)
		{
			//TODO: global error handling
			ex.printStackTrace();
			System.exit(0);
			return null;
		}
	}


	/**
	 * Returns the terminal or nonterminal with the given name from
	 * the given lists of terminals and nonterminals. If not existing, null is returned.
	 */
	private static Symbol getSymbolByName(String name, Terminal[] terminals, NonTerminal[] nonTerminals)
	{
		for (Terminal e : new It<Terminal>(terminals))
		{
			if (e.toString().equals(name))
				return e;
		}
		for (NonTerminal e : new It<NonTerminal>(nonTerminals))
		{
			if (e.toString().equals(name))
				return e;
		}
		return null;
	}


	/**
	 * Returns the position for the given {@link Action} (i.e. how many
	 * symbols have to be shifted before this action is called).
	 * OBSOLETE
	 */
	@Deprecated @SuppressWarnings("unchecked") public static int getActionPosition(Action action, List<Symbol> rhsSymbols,
		SymbolValueClasses symbolValueClasses)
	{
		try
		{
			//gets the method named "a" from the given action
			Method method = null;
			Method[] methods = action.getClass().getMethods();
			for (int i = 0; i < methods.length; i++)
			{
				Method m = methods[i];
				if (m.getName().equals("a"))
				{
					method = m;
					break;
				}
			}
			if (method == null)
			{
				throw new IllegalSpecException("All actions must have exactly one method called \"a\"!");
			}
			//get the parameters of this method
			Class[] parameters = method.getParameterTypes();
			//for each parameter, find the appropriate symbol-value (go on if not matching,
			//since unneeded symbols can be left out). when we have gone
			//through all parameters, we know the position.
			int position = 0;
			It<Symbol> rhs = new It<Symbol>(rhsSymbols);
			for (int i = 0; i < parameters.length; i++)
			{
				//class of action parameter
				Class paramClass = parameters[i];
				//find corresponding RHS symbol
				while (true)
				{
					if (!rhs.hasNext())
					{
						throw new IllegalSpecException("Parameters do not match for an action for the RHS: " + rhsSymbols.toString());
					}
					Symbol rhsSymbol = rhs.next();
					position++;
					//first option: parameter stands for a symbol-value-binding
					//this is the case when the parameter class is the same as the rhsSymbol's value class
					Class<SymbolValue<Object>> symbolClass = symbolValueClasses.get(rhsSymbol);
					if (symbolClass != null && paramClass.equals(
						((ParameterizedType) symbolClass.getGenericSuperclass()).getActualTypeArguments()[0])) //TODO: UGLY! UGLY! UGLY!
						break;
					//second option: parameter stands for a terminal/nonterminal (name doesn't matter)
					if (paramClass.isAssignableFrom(Terminal.class) || paramClass.isAssignableFrom(NonTerminal.class))
						break;
					//third option: current symbol was left out, parameter stands for a later symbol: go on
				}
			}
			return position;
		}
		catch (Exception ex)
		{
			//TODO: global error handling
			ex.printStackTrace();
			System.exit(0);
			return 0;
		}
	}


	/**
	 * Checks, if the given action {@link Action} fits to the given position (i.e. how many
	 * symbols have to be shifted before this action is called). If it is ok, nothing happens,
	 * but if it does not fit (i.e. the action is invalid or if its parameters do not fit to the
	 * symbol-values of the production up to the given position) an {@link IllegalSpecException} is thrown.
	 * 
	 * TODO : very strict, should also check possible casts.
	 */
	public static void checkAction(Action action, int position,
		List<Symbol> rhsSymbols, SymbolValueClasses symbolValueClasses)
	{
		try
		{
			//gets the method named "a" from the given action
			Method method = null;
			Method[] methods = action.getClass().getMethods();
			for (int i = 0; i < methods.length; i++)
			{
				Method m = methods[i];
				if (m.getName().equals("a"))
				{
					method = m;
					break;
				}
			}
			if (method == null)
			{
				throw new IllegalSpecException("All actions must have exactly one method called \"a\"!");
			}
			
			// do the checking
			checkParamsOfAction(method, position, rhsSymbols, symbolValueClasses);
			
		}
		catch (Exception ex)
		{
			//TODO
			ex.printStackTrace();
			throw new IllegalSpecException(ex.getMessage());
		}
	}
	
	
	/**
	 * Checks the parameters of the method <code>method</code> if they are compatible with
	 * the expected values. Note that there is a special checking for primitive types which
	 * can occur if the specification is written in scala.
	 * 
	 * @param method
	 * @param position
	 * @param rhsSymbols
	 * @param symbolValueClasses
	 * @throws Exception
	 */
	public static void checkParamsOfAction(Method method, int position,
		List<Symbol> rhsSymbols, SymbolValueClasses symbolValueClasses) throws Exception {
	
		//get the parameters of this method
		Type[] parameters = method.getGenericParameterTypes();
		//for each parameter, find the appropriate symbol-value (go on if not matching,
		//since unneeded symbols can be left out).
		//when we have gone through all parameters, it is ok if we have not exceeded the given position.
		int curPosition = 0;
		It<Symbol> rhs = new It<Symbol>(rhsSymbols);
		for (int i = 0; i < parameters.length; i++)
		{
			//type of action parameter
			Type paramType = parameters[i];
			//find corresponding RHS symbol
			while (true)
			{
				if (!rhs.hasNext())
				{
					throw new IllegalSpecException("Parameters do not match for an action for the RHS: " + rhsSymbols.toString());
				}
				Symbol rhsSymbol = rhs.next();
				curPosition++;
				if  (curPosition > position)
	      {
	        throw new IllegalSpecException("The action occurs too early or it's method requires too much parameters: " + rhsSymbols.toString());
	      }
				//does parameter stand for a symbol-value-binding?
				//this is the case when the parameter class is the same as the rhsSymbol's value class
				Class<SymbolValue<Object>> symbolClass = symbolValueClasses.get(rhsSymbol);
				if (symbolClass != null)
				{
					// rhsSymbol has symbol-value-binding => parameter MUST HAVE
					// equal type.
					Type expectedType = ((ParameterizedType) symbolClass
							.getGenericSuperclass()).getActualTypeArguments()[0];
					if (expectedType instanceof ParameterizedType)
						expectedType = ((ParameterizedType) expectedType)
								.getRawType();
					if (paramType instanceof ParameterizedType)
						paramType = ((ParameterizedType) paramType)
								.getRawType();
					// System.out.println(paramType+" == "+expectedType);

					Class<?> resolvedClass = resolvePrimitiveTypes(((Class<?>) paramType));
					
					if (resolvedClass.isAssignableFrom((Class<?>) expectedType))
						break;
					else {

						throw new IllegalSpecException(
								"Parameters do not match for an action for the RHS: "
										+ rhsSymbols.toString() + "(expected "
										+ expectedType + ", found " + paramType
										+ ")");
					}
				}
      if (rhsSymbol instanceof AuxiliaryLHS4SemanticShiftAction)
      {
        // rhsSymbol has no symbol-value-binding, but is an auxiliary NT =>
        // if auxNT has symbol-value void ignore auxiliary NT,
        // otherwise parameter MUST BE symbolvaluetype of aux NT.
        AuxiliaryLHS4SemanticShiftAction auxNT = (AuxiliaryLHS4SemanticShiftAction)rhsSymbol;
        if (auxNT.symbolValueType==Void.TYPE)
          continue;
        if (((Class<?>)paramType).isAssignableFrom(auxNT.symbolValueType))
          break;
        else
          throw new IllegalSpecException(
              "Parameters do not match for an action for the RHS: " + rhsSymbols.toString() +
              "(expected "+auxNT.symbolValueType+", found "+paramType+")"
          );
      }
      if (SpecialTerminals.Error.equals(rhsSymbol))
      {
        // rhsSymbol has no symbol-value-binding, is no auxiliary NT, but is an Error => parameter MUST BE ErrorInformation
        if(paramType.equals(ErrorInformation.class))
          break;
        else
          throw new IllegalSpecException(
              "Parameters do not match for an action for the RHS: " + rhsSymbols.toString() +
              "(expected ErrorInformation, found "+paramType+")"
          );
      }
      // rhsSymbol is neither value-binded, an aux NT nor an Error => It has no meaning at all (for example Epsilon)
			}
		}
		//given position must be at least as high as the computed minimal position
		if (curPosition < position)
		{
		  while(curPosition < position)
		  {
		    if (!rhs.hasNext())
		      throw new IllegalSpecException("Parameters do not match for an action for the RHS: " + rhsSymbols.toString());
		    Symbol rhsSymbol = rhs.next();
		    curPosition++;
      Class<SymbolValue<Object>> symbolClass = symbolValueClasses.get(rhsSymbol);
		    if (
		        symbolClass!=null ||
		        (
		            rhsSymbol instanceof AuxiliaryLHS4SemanticShiftAction &&
		            ((AuxiliaryLHS4SemanticShiftAction)rhsSymbol).symbolValueType!=Void.TYPE
		        ) ||
		        SpecialTerminals.Error.equals(rhsSymbol)
		    )
		      throw new IllegalSpecException("The action occurs too late or it's method requires too less parameters: " + rhsSymbols.toString());
		  }
		}
	}
	
	public static Class<?> resolvePrimitiveTypes(Class<?> clazz) {

		// primitive Type can occur if you use scala for the specification,
		// so first wrap the primitive types and then go on with checking
		if (clazz.isPrimitive()) {
			Class<?> c = null;
			
			if(clazz.isAssignableFrom(Integer.TYPE)) {
				c = Integer.class;
			} else if(clazz.isAssignableFrom(Boolean.TYPE)) {
				c = Boolean.class;
			} else if(clazz.isAssignableFrom(Character.TYPE)) {
				c = Character.class;
			} else if(clazz.isAssignableFrom(Byte.TYPE)) {
				c = Byte.class;
			} else if(clazz.isAssignableFrom(Short.TYPE)) {
				c = Short.class;
			} else if(clazz.isAssignableFrom(Long.TYPE)) {
				c = Long.class;
			} else if(clazz.isAssignableFrom(Float.TYPE)) {
				c = Float.class;
			} else if(clazz.isAssignableFrom(Double.TYPE)) {
				c = Double.class;
			}
			
			return c;
		}
		
		return clazz;
	}
	
	/**
	 * Instantiates enumeration object at runtime.
	 * @author Stefan Dangl
	 **/
	public static AuxiliaryLHS4SemanticShiftAction createAuxLHS4SemanticShiftAction( String name )
	{
	  AuxiliaryLHS4SemanticShiftAction obj = null;
    Exception why = null;
    
    /**
     * Retrieve the constructor of the super-enum's class.
     * As enumerations shall not be created during runtime according
     * to the designers of Java, a complicated workaround is required.
     */
    
    @SuppressWarnings("unchecked")
    Constructor con = AuxiliaryLHS4SemanticShiftAction.class.getDeclaredConstructors()[0];
//    System.out.println("Constructor: "+con);

    /**
     * Use the constructor accessor of the constructor in order
     * to retrieve a newInstance method, which may then be used
     * to create an object which extends the enum AuxiliaryLHS4SemanticShiftAction.
     */
    try {

      // Invoke private void Constructor.acquireConstructorAccessor()
      // in order to create a constructor-accessor if none has been created yet.
      Method[] methods = con.getClass().getDeclaredMethods();
      for (Method m : methods) {
        if (m.getName().equals("acquireConstructorAccessor")) {
//          System.out.println("acquireConstructorAccessor() : "+m+" (method)");
          m.setAccessible(true);
          m.invoke(con, new Object[0]);
        }
      }
      
      // Retrieve the created constructor-accessor.
      Field[] fields = con.getClass().getDeclaredFields();
      Object ca = null;
      for (Field f : fields) {
        if (f.getName().equals("constructorAccessor")) {
//          System.out.println("constructorAccessor : "+f+" (field)");
          f.setAccessible(true);
          ca = f.get(con);
        }
      }
      
//      System.out.println("Retrieved constructor accessor successfully :-)");
      
      // Invoke the newInstance(Class[]) method of the constructor-accessor      
      Method m = ca.getClass().getMethod("newInstance", new Class[] { Object[].class });
      m.setAccessible(true);
      obj = (AuxiliaryLHS4SemanticShiftAction) m.invoke(
          ca, new Object[] { 
              new Object[] { name, Integer.MAX_VALUE }
          }
      );
      
//      System.out.println(obj.getClass() + " : ( " + obj.name() + " , " + obj.ordinal()+" ) ");

    } catch (Exception e) {
      why = e; //e.printStackTrace();
    }
    
    if (obj==null || why!=null)
    {
      IllegalSpecException rte = new IllegalSpecException(
          "Error during creation of artificial enum at runtime : "+why
      );
      rte.initCause(why);
      throw rte;
    }
    
    return obj;
	}

	/**
	 * 
	 * Retrieves the return type of a semantic action object.
	 * 
	 **/
  public static Class<?> getReturnTypeOfAction(Action action) {
    return action.getMethod().getReturnType();
  }

  public static Object getFieldValueFromObject( Object obj, String field_name )
  {
    Class<?> c = obj.getClass();
    if (c==null) return null;
    Field fld = null;
    try {
      fld = c.getDeclaredField(field_name);
    } catch (SecurityException e1) {
      e1.printStackTrace();
      return null;
    } catch (NoSuchFieldException e) {
      return null;
    }
    if (fld==null) return null;
    Object value = null;
    try {
      value  = fld.get(obj);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return value;
  }
  /*
  public static Object getFieldValueFromTerminalsSymbolValue( SymbolValueClasses svc, Terminal t, String name )
  {
    Class<SymbolValue<Object>> c = svc.get(t);
    if (c==null) return null;
    Field fld = null;
    try {
      fld = c.getDeclaredField(name);
    } catch (SecurityException e1) {
      e1.printStackTrace();
      return null;
    } catch (NoSuchFieldException e) {
      return null;
    }
    if (fld==null) return null;
    Object value = null;
    try {
      value  = fld.get(null);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return value;
  }*/

}
