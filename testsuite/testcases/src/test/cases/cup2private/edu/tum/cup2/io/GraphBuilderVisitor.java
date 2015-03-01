package edu.tum.cup2.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import edu.tum.cup2.generator.Automaton;
import edu.tum.cup2.generator.Edge;
import edu.tum.cup2.generator.states.State;
import java.util.Set;

/**
 * GraphBuilderVisitor
 * visits the Automaton object and its parts (i.e. edges and states)
 * and creates a dot input file for GraphViz
 * in order to create a diagram of the automaton
 * 
 * @author Michael Hausmann
 *
 */ 
public class GraphBuilderVisitor implements IAutomatonVisitor
{
	//StringBuilders used to build graphViz input successively
	private StringBuilder fsbAcceptingStates; //dot input for accepting states
	private StringBuilder fsbNormalStates; //dot input for normal states
	private State fStartState; //start state of the automaton
	private StringBuilder fsbStartEdges; //start edges as dot input
	private StringBuilder fsbEdges; //edges as dot input
	private int fCounter; //counter for generating state numbers
	private HashMap<State, Integer> fStateNumbers;
	private HashSet<State> fAppendedNormalStates;
	private StringBuilder fLegend;
	
	/**
	 * initialize object before visiting new automaton
	 */
	private void init()
	{
		fsbAcceptingStates = new StringBuilder(); //dot input for accepting states
		fsbNormalStates = new StringBuilder(); //dot input for normal states
		fStartState = null; //start state of the automaton
		fsbStartEdges = new StringBuilder(); //start edges as dot input
		fsbEdges = new StringBuilder(); //edges as dot input
		fCounter = 0; //counter for generating state numbers
		fStateNumbers = new HashMap<State, Integer>();
		fAppendedNormalStates = new HashSet<State>();
		fLegend = new StringBuilder();
		
	}
	
	/**
	 * Visit an Automaton object
	 */
	public void visit(Automaton a) 
	{	
		init();
		
		this.fStartState = a.getStartState();
		Set<Edge> he = (Set<Edge>) a.getEdges();
		for(Edge e : he)
		{
			e.visited(this);
		}
		//just for debug
		System.out.println("visiting automaton structure done.");
		System.out.println("#states: " + a.getStates().size() 
						+ " #edges: " + a.getEdges().size());
	}

	/**
	 * Visit a generic state
	 */
	public void visit(State s) 
	{
		if(!this.fAppendedNormalStates.contains(s))
		{
			appendStateToBuilder(s, this.fsbNormalStates);
			this.fAppendedNormalStates.add(s);
		}
	}
	
	/**
	 * Visit an Edge of the Automaton
	 */
	public void visit(Edge e) 
	{
		State s1 = e.getSrc();
		State s2 = e.getDest();
		if(e.isDestAccepting())
		{
			appendStateToBuilder(s1, this.fsbAcceptingStates);
		}
		
		if(s1 != null && s2 != null)
		{
			//visit states to create long names
			s1.visited(this);
			s2.visited(this);
			
			if(s1.equals(this.fStartState))
			{
				//draw arrow from start state to target state
				fsbStartEdges.append(getStateNumber(s1));
				fsbStartEdges.append(" -> ");
				fsbStartEdges.append(getStateNumber(s2));
				fsbStartEdges.append(" [label=\"" + e.getSymbol().toString() + "\"]");
				fsbStartEdges.append(";\n ");
			}
			else
			{
				fsbEdges.append(getStateNumber(s1));
				fsbEdges.append(" -> ");
				fsbEdges.append(getStateNumber(s2));
				fsbEdges.append(" [label=\"" + e.getSymbol().toString() + "\"]");
				fsbEdges.append(";\n ");
			}
		}	
	}
	
	/**
	 * Retrieve the state number for an automaton state
	 * if number does not exist yet, generate one and
	 * append the state and its items to the legend
	 * 
	 * @param state
	 * @return state number
	 */
	private Integer getStateNumber(State s)
	{
		Integer n = fStateNumbers.get(s);
		if(n == null)
		{
			n = this.fCounter++;
			fStateNumbers.put(s, n);
			if(s.equals(this.fStartState))
				fLegend.append("start state (" + n + ")");
			else
			{
				fLegend.append("state ");
				fLegend.append(n);
			}
			fLegend.append(": \n");
			for(Object i : s.getItems())
			{
				fLegend.append(i.toString());
				fLegend.append("; \n");
			}
			fLegend.append("\n");
		}
		return n;
	}
	
	/**
	 * append state info to StringBuilder
	 * 
	 * @param s
	 * @param sb
	 */
	private void appendStateToBuilder(State s, StringBuilder sb)
	{
		Integer n = getStateNumber(s); 
		sb.append(n);
		sb.append(";\n ");
	}
	
	/**
	 * save the legend to a file
	 */
	private void saveLegendToFile(String strFileName)
	{
		File f = new File(strFileName);
		try {
			FileWriter fw = new FileWriter(f);
			fw.write(this.fLegend.toString());
			fw.close();
		
		} catch (IOException e) {
			System.err.println("Could not wirte legend to file: " + strFileName);
			e.printStackTrace();
		}
		
	}
	
	/**
	 * returns the dot input for GraphViz as String
	 * @return String
	 */
	protected String buildGraphVizInput()
	{
		StringBuilder fsbResult = new StringBuilder();
		fsbResult.append("digraph cup2Automaton {");
		
		//draw all accepting states with double circle
		fsbResult.append("node [shape=doublecircle]; ");
		fsbResult.append(this.fsbAcceptingStates.toString());
		
		//draw all non-accepting states with single circle
		fsbResult.append("node [shape=circle]; ");
		fsbResult.append(this.fsbNormalStates.toString());
		
		fsbResult.append(this.fsbStartEdges.toString());
		fsbResult.append(this.fsbEdges.toString());
		
		fsbResult.append("}"); //building the complete graph for the automaton done
		
		return fsbResult.toString();
	}
	
	/**
	 * Saving the dot input for GraphViz to a file
	 * 
	 * @param strFileName: name of the input file
	 */
	public void saveGraphVizInput(String strFileName)
	{
		//build the dot input for graphViz
		String strInput = buildGraphVizInput();
		
		//save the dot file 
		File f = new File(strFileName);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(f);
			fos.write(strInput.getBytes());
		} catch (FileNotFoundException e) {
			System.err.println("File " + f.getAbsolutePath() + " not found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IOException: " + e.getMessage());
			e.printStackTrace();
		}
		
		//save the description of the state to file
		saveLegendToFile(strFileName + ".legend.txt");
		
		//create a picture file with dot.exe from the dot input file
		try {
			File fDotExe = new File("dot");
			if(fDotExe.canExecute())
			{
				String[] cmdarray = {
					"dot", 
					"-Tpng", 
					"-o" + strFileName + ".png",
					strFileName
				};
				Runtime.getRuntime().exec(cmdarray);
					
			}
			else
			{
				System.err.println("dot.exe is not accessible! Could not create png file.");
			}
		} catch (IOException e) {
			System.err.println("Could not run dot.exe on created dot input");
			e.printStackTrace();
		}
	}

}
