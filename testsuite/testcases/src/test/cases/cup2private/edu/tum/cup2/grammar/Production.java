package edu.tum.cup2.grammar;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import edu.tum.cup2.semantics.Action;
import edu.tum.cup2.spec.CUP2Specification;
import edu.tum.cup2.util.It;


/**
 * A production consists of a left hand side {@link NonTerminal} and a list of right hand side {@link Symbol}s.
 * There may be on single {@link Action} which is performed when
 * the production is reduced.
 * 
 * If there are productions with more than one right hand sides
 * (in parser books separated by "|"), each of these sides
 * must be represented by its own {@link Production} instance.
 * 
 * A production should have an index, to support debugging and
 * parsing table dumps.
 * 
 * TODO: TIDY constructors
 * 
 * @author Andreas Wenger
 * @author Michael Hausmann
 * @author Stefan Dangl
 */
public final class Production implements Serializable
{
	private static final long serialVersionUID = 2L;
	
	private int id;
	private NonTerminal lhs;
	private List<Symbol> rhs;
	private Action reduceAction;
	
	// cache
	private Terminal lastTerminal;
	private Terminal precTerminal; // save the Terminal with the precedence of this Production
	private int rhsSizeWithoutEpsilon;
	private int hashCode;
	
	
	/**
	 * Creates a new {@link Production}.
	 */
	public Production(int id, NonTerminal lhs, List<Symbol> rhs, Action reduceAction, Terminal precTerminal)
	{
		// check rhs
		if (rhs.size() == 0)
		{
			// empty RHS means epsilon
			rhs = Arrays.asList((Symbol) SpecialTerminals.Epsilon);
			this.rhsSizeWithoutEpsilon = 0;
		} else if (rhs.size() == 1)
		{
			this.rhsSizeWithoutEpsilon = (rhs.get(0) == SpecialTerminals.Epsilon ? 0 : 1);
		} else if (rhs.size() > 1)
		{
			// check, that there is no epsilon in a RHS with at least 2 symbols
			for (Symbol symbol : rhs)
			{
				if (symbol == SpecialTerminals.Epsilon)
				{
					throw new IllegalArgumentException("Epsilon is only allowed as the single symbol of a RHS!");
				}
			}
			this.rhsSizeWithoutEpsilon = rhs.size();
		}
		this.id = id;
		this.lhs = lhs;
		this.rhs = rhs;
		this.reduceAction = reduceAction;
		// cache
		// find last terminal
		Terminal lastTerminal = null;
		for (int i = rhs.size() - 1; i >= 0; i--)
		{
			if (rhs.get(i) instanceof Terminal)
			{
				lastTerminal = (Terminal) rhs.get(i);
				break;
			}
		}
		this.lastTerminal = lastTerminal;
		this.precTerminal = (precTerminal != null) ? precTerminal : lastTerminal;
		// hash code
		computeHashcode();
	}
	
	
	/**
	 * compute the hashCode for this Production
	 * @return
	 */
	private int computeHashcode()
	{
		int sum = lhs.hashCode(); // default hashCode is the object address
		for (Symbol s : rhs)
		{
			sum = (sum + s.hashCode()) % ((1 << 31) - 1);
		}
		this.hashCode = sum;
		return this.hashCode;
	}
	
	
	/**
	 * Convenience constructor: Just give an ID, a left hand side {@link NonTerminal} and a list of right hand side
	 * {@link Symbol}s.
	 * The {@link Production} contains no {@link Action}s.
	 */
	public Production(int id, NonTerminal lhs, Symbol... rhs)
	{
		this(id, lhs, Arrays.asList(rhs), null, null);
	}
	
	
	/**
	 * readObject() de-serializes a semantic action
	 */
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException
	{
		this.id = (Integer) stream.readObject();
		this.lhs = (NonTerminal) stream.readObject();
		this.rhs = (List<Symbol>) stream.readObject();
		// this.actions = (List<Action>) stream.readObject();
		deserializeReduceAction(stream);
		this.lastTerminal = (Terminal) stream.readObject();
		this.rhsSizeWithoutEpsilon = (Integer) stream.readObject();
		this.hashCode = (Integer) stream.readObject();
	}
	
	
	@SuppressWarnings("unchecked")
	private void deserializeReduceAction(ObjectInputStream stream) throws IOException, ClassNotFoundException
	{
		try
		{
			Object outer = null; // helper variable for specification which this production belongs to
			Class<Action> clazz = (Class<Action>) stream.readObject();
			
			// if clazz is null then add the null entry to the actions list.
			// this guarantees that the actions list is completely restored.
			// attention: when clazz is null, the loop continues with the next iteration
			if (clazz == null)
			{
				reduceAction = null;
				return;
			}
			// System.out.println("clazz : "+clazz);
			Constructor<CUP2Specification> c_enclosing = (Constructor<CUP2Specification>) clazz.getEnclosingConstructor(); // gets
																																								// constructor
																																								// for
																																								// specification
			// System.out.println("constructor enclosing : "+c_enclosing);
			Class<CUP2Specification> clazz_enclosing = (Class<CUP2Specification>) clazz.getEnclosingClass();
			if (c_enclosing == null)
			{
				int min = Integer.MAX_VALUE;
				for (Constructor<?> c : clazz_enclosing.getDeclaredConstructors())
				{
					Class<?>[] cParms = c.getParameterTypes();
					if (cParms.length <= min)
					{
						min = cParms.length;
						c_enclosing = (Constructor<CUP2Specification>) c;
						break;
					}
				}
			}
			// System.out.println("clazz enclosing : "+clazz_enclosing);
			Constructor<?>[] c_declared = clazz.getDeclaredConstructors(); // 1 constructor - NOT public, NOT default (expects
																							// reference to outer object)
			for (Constructor<?> c : c_declared)
			{
				Class<?>[] cParms = c.getParameterTypes();
				if (cParms.length == 1)
				{
					if (cParms[0].equals(clazz_enclosing))
					{
						// creating a new instance of Specification is ugly, if Specification should be serialized:
						// after de-serialization actions belong to a different specification instance
						// this could lead to errors, if actions reference fields in the spec
						if (outer == null)
						{
							Object[] args = new Object[c_enclosing.getParameterTypes().length];
							outer = c_enclosing.newInstance(args);
						}
						
						// Problem: constructor c needs to be public in order to be accessed from here
						c.setAccessible(true);
						
						Action a = (Action) c.newInstance(outer);
						reduceAction = a;
					}
				}
			}
			
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
			throw new IOException();
		} catch (InstantiationException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
			throw new IOException();
		} catch (InvocationTargetException tie)
		{
			throw new IOException();
		}
	}
	
	
	/**
	 * writes fields to file
	 */
	private synchronized void writeObject(ObjectOutputStream stream) throws IOException
	{
		stream.writeObject(this.id);
		stream.writeObject(this.lhs);
		stream.writeObject(this.rhs);
		this.serializeReduceAction(stream);
		stream.writeObject(this.lastTerminal);
		stream.writeObject(this.rhsSizeWithoutEpsilon);
		stream.writeObject(this.hashCode);
	}
	
	
	private void serializeReduceAction(ObjectOutputStream stream) throws IOException
	{
		if (reduceAction != null)
		{
			@SuppressWarnings("unchecked")
			Class<Action> c = (Class<Action>) reduceAction.getClass();
			stream.writeObject(c);
		} else
		{
			stream.writeObject(null);
		}
	}
	
	
	/**
	 * Gets the ID of this production.
	 */
	public int getID()
	{
		return id;
	}
	
	
	/**
	 * Gets the left hand side non-terminal.
	 */
	public NonTerminal getLHS()
	{
		return lhs;
	}
	
	
	/**
	 * Returns a list of the the right hand side symbols.
	 */
	public List<Symbol> getRHS()
	{
		return rhs;
	}
	
	
	/**
	 * Returns the number of right hand side symbols,
	 * without epsilons.
	 */
	public int getRHSSizeWithoutEpsilon()
	{
		return rhsSizeWithoutEpsilon;
	}
	
	
	/**
	 * Gets the semantic action assigned to the given position.
	 * If there is no action, null is returned.
	 * 
	 * For example, <code>0</code> is the action before shifting the first symbol,
	 * and <code>getRHS().getSize()<code> is the action when the production is reduced.
	 * 
	 * @deprecated
	 * 
	 */
	@Deprecated
	public Action getAction(int position)
	{
		if (position < this.rhs.size())
			return null;
		else
			return reduceAction;
	}
	
	
	public Action getReduceAction()
	{
		return reduceAction;
	}
	
	
	/**
	 * Returns the last terminal within this production or null if there is none.
	 */
	public Terminal getLastTerminal()
	{
		return lastTerminal;
	}
	
	
	/**
	 * Returns the Terminal responsible for the precedence rating of this production, or null if there is none.
	 */
	public Terminal getPrecedenceTerminal()
	{
		return precTerminal;
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Production)
		{
			Production p = (Production) obj;
			if (lhs != p.lhs)
				return false;
			if (!rhs.equals(p.rhs))
				return false;
			return true;
		}
		return false;
	}
	
	
	@Override
	public int hashCode()
	{
		return hashCode;
	}
	
	
	@Override
	public String toString()
	{
		StringBuilder rhsString = new StringBuilder();
		for (Symbol s : rhs)
			rhsString.append(s + " ");
		return "Production " + id + ": " + lhs + " → " + rhsString;
	}
	
	
	public String toString(int dotPosition)
	{
		StringBuilder rhsString = new StringBuilder();
		It<Symbol> rhsSymbols = new It<Symbol>(rhs);
		for (Symbol s : rhsSymbols)
		{
			if (dotPosition == rhsSymbols.getIndex())
				rhsString.append(". ");
			rhsString.append(s + " ");
		}
		if (dotPosition == rhs.size())
			rhsString.append(". ");
		return "Production " + id + ": " + lhs + " → " + rhsString;
	}
	
	
}
