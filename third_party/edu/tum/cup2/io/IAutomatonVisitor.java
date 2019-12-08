package edu.tum.cup2.io;

import edu.tum.cup2.generator.Automaton;
import edu.tum.cup2.generator.Edge;
import edu.tum.cup2.generator.states.State;

public interface IAutomatonVisitor 
{
	void visit(Automaton a);
	void visit(Edge e);
	void visit(State s);
	
}
