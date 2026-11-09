package edu.tum.cup2.parser;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import edu.tum.cup2.parser.exceptions.ParserException;
import edu.tum.cup2.scanner.Scanner;
import edu.tum.cup2.semantics.ErrorInformation;
import edu.tum.cup2.semantics.ParserObserver;


/**
 * An abstraction of the different parsers implemented in Cup2
 * 
 * @author Gero
 */
public abstract class AParser
{
	protected final List<ParserObserver> observers = new LinkedList<ParserObserver>();
	
	
	public abstract Object parse(Scanner input, Object... initArgs) throws IOException, ParserException;
	
	/*
	 * Methods for observation.
	 */
	
	/** Registers a {@link ParserObserver}. **/
	public void register(ParserObserver parserObserver)
	{
		observers.add(parserObserver);
	}
	
	
	/** Unregisters a {@link ParserObserver}. **/
	public void unregister(ParserObserver parserObserver)
	{
		observers.remove(parserObserver);
	}
	
	
	protected void notifyObserversAbout(ErrorInformation e)
	{
		for (ParserObserver po : observers)
		{
			po.syntax_error(e);
		}
	}
}
