package edu.tum.cup2.spec;


import edu.tum.cup2.grammar.*;
import edu.tum.cup2.semantics.*;

import static edu.tum.cup2.spec.SampleSpec.NonTerminals.*;
import static edu.tum.cup2.spec.SampleSpec.Terminals.*;

/**
 * Sample CUP specification: Simple calculator.
 * 
 * @author Andreas Wenger
 */
public class SampleSpec
	extends CUP2Specification
{

	//terminals (tokens returned by the scanner)
	public enum Terminals implements Terminal
	{
		SEMI, PLUS, TIMES, LPAREN, RPAREN, NUMBER;
	}
	
	//non-terminals
	public enum NonTerminals implements NonTerminal
	{
		expr, res;
	}

	//symbols with values
	public class NUMBER extends SymbolValue<Integer>{};
	public class expr   extends SymbolValue<Integer>{};
	public class res    extends SymbolValue<Integer>{};
	
	
	public SampleSpec()
	{
		
		//precedences
		precedences(
			left(TIMES),
			left(PLUS));
		
		//grammar
		grammar(
			prod(res,         rhs(expr, SEMI),  
			                    //when no $a is found within the rhs, the single action is
				                  //always the reduce action for this production
                          		  new Action() { public Integer a(Integer e) { return e; }}), 
			prod(expr,        rhs(NUMBER),
				                  new Action() { public Integer a(Integer e) { return e; }},
				                rhs(expr, PLUS, expr),
				                  new Action() { public Integer a(Integer e1, Integer e2)
				                    { return e1 + e2; }},
			                  rhs(expr, TIMES/*, SpecialTerminals.$a*/, expr),
			                    /* new Action() { public void a(Integer e1)
                            { System.out.println("Shifted over the TIMES"); }}, */
			                    new Action() { public Integer a(Integer e1, Integer e2)
		                        { return e1 * e2; }},
			                  rhs(LPAREN, expr, RPAREN),
			                    new Action() { public Integer a(Integer e) { return e; }})
		);
	}

}
