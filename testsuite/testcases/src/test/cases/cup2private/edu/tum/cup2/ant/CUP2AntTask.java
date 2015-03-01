package edu.tum.cup2.ant;

import java.io.File;
import java.util.LinkedList;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.taskdefs.Javac;

import edu.tum.cup2.generator.*;
import edu.tum.cup2.generator.exceptions.GeneratorException;
import edu.tum.cup2.generator.exceptions.ReduceReduceConflict;
import edu.tum.cup2.generator.exceptions.ShiftReduceConflict;
import edu.tum.cup2.grammar.CheckedGrammar;
import edu.tum.cup2.grammar.IGrammar;
import edu.tum.cup2.grammar.NonTerminal;
import edu.tum.cup2.io.LRParserSerialization;
import edu.tum.cup2.io.LRParserSerializer;
import edu.tum.cup2.io.LRParsingTableDump;
import edu.tum.cup2.parser.LRParser;
import edu.tum.cup2.parser.tables.LRParsingTable;
import edu.tum.cup2.spec.CUP2Specification;
import edu.tum.cup2.spec.exceptions.IllegalSpecException;


/**
 * Custom CUP2 compile task for Ant.
 * The task can be used to create a parser using CUP2 with a {@link CUP2Specification}.
 * Therefore it creates the class files for the parser with
 * the help of the javac task and creates the .cup2 file (serialization of the parser)
 * using the {@link LRParserSerializer} class.
 * 
 * See CUP2AntTask.txt for more information.
 * 
 * @author Michael Hausmann
 * @author Andreas Wenger
 */
public class CUP2AntTask
	extends Task
{

	//required attributes
	private String spec = null; //specification name; e.g: edu.tum.cup2.test.SpecCalc4
	private String specdir = null; //root source directory for the specification
	private String destdir = null; //destination directory for CUP2 and the compiled spec
	private Path classpath = null;
	
	//optional attributes
	private String cup2srcdir = null; //source path of cup2
	private String algorithm = null; //generator algorithm; e.g: lr1
	private boolean verbose = false; //print a lot of status messages?
	private String parsetable = null; //where to store the parsing table?

	//other fields
	private CUP2Specification cup2Specification = null; //instantiated specification
	
	
	/**
	 * executing the cup2 task
	 */
	public void execute()
	{
		//print parameter settings 
		if (this.verbose)
			this.displayValues();

		//check if required attribute spec is set
		if (spec == null || spec.length() == 0)
		{
			log("spec missing!", Project.MSG_ERR);
		}
		//check if required attribute specsrc is set
		else if (specdir == null || specdir.length() == 0)
		{
			log("specsrc is missing!", Project.MSG_ERR);
		}
		//check if required attribute dest is set
		else if (destdir == null || destdir.length() == 0)
		{
			log("dest is missing!", Project.MSG_ERR);
		}
		//check if required classpath is set
		else if (classpath == null)
		{
			log("classpath is missing!", Project.MSG_ERR);
		}
		else
		{
			//check optional attribute classpath
			if (classpath == null)
				log("No classpath set! Is this correct?", Project.MSG_WARN);

			//if optional attribute cup2src is set, compile cup2 sources
			if (cup2srcdir != null)
			{
				this.compileCup2();
			}

			//compiling the CUPSpecification
			compileSpec();
			if (verbose)
				log(" === Compiling Spec done ===");

			//instantiate CUPSpecification
			try
			{
				this.cup2Specification = this.createCUP2Specification();
			}
			catch (InstantiationException e)
			{
				log("InstantiationException when trying to create CUP2Specification");
				e.printStackTrace();
			}
			catch (IllegalSpecException e)
			{
				log("CUP2 Specification invalid : " + e);
				e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{
				log("ClassNotFoundException when trying to create CUP2Specification");
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				log("IllegalAccessException when trying to create CUP2Specification");
				e.printStackTrace();
			}

			if (this.cup2Specification == null)
			{
				log("Error: could not create CUPSpecification", Project.MSG_ERR);
			}
			else
			{
				//Productivity and reachability is checked
				if (verbose)
					log(" === Checking productivity and reachability ===");

				IGrammar orgGram = this.cup2Specification.getGrammar();
				CheckedGrammar cg = new CheckedGrammar(orgGram);
				if (!cg.isReduced())
				{
					log("The grammar is not reduced!", Project.MSG_WARN);
					if (cg.hasNotProductiveNonTerminals())
					{
						log("The grammar contains not productive NonTerminals!", Project.MSG_WARN);
						LinkedList<NonTerminal> notProductiveNonTerminals = cg
							.getNotProductiveNonTerminals();
						log("Not productive are: " + notProductiveNonTerminals.toString());

					}
					if (cg.hasNotReachableNonTerminals())
					{
						log("The grammar contains not reachable NonTerminals!", Project.MSG_WARN);
						LinkedList<NonTerminal> notReachableNonTerminals = cg
							.getNotReachableNonTerminals();
						log("Not reachable are: " + notReachableNonTerminals.toString());

					}
				}

				//create serialization
				LRParsingTable table = null;
				try
				{
					if (verbose)
						log(" === Creating parsing table ===");

					//create Parsing Table
					LRGenerator<?, ?> generator = createGenerator(this.cup2Specification);
					table = generator.getParsingTable();

					LRParser parser = new LRParser(table);
					if (verbose)
						log(" === Serializing parser ===");
					serializeParser(parser);
				}
				catch (ShiftReduceConflict e)
				{
					log("Shift-Reduce-Conflict during parser generation: " + e.getMessage(),
						Project.MSG_ERR);
				}
				catch (ReduceReduceConflict e)
				{
					log("Reduc-Reduce-Conflict during parser generation: " + e.getMessage(),
						Project.MSG_ERR);
				}
				catch (GeneratorException e)
				{
					log("GeneratorException occured during parser generation: " + e.getMessage(),
						Project.MSG_ERR);
				}
				catch (IllegalSpecException e)
				{
					log("Cup2 Specification invalid : " + e);
					e.printStackTrace();
				}

				//print parsing table
				if (this.parsetable != null && parsetable.length() > 0)
				{
					printParsingTable(table, this.parsetable);
				}
			}
		} /*end else*/
		if (verbose)
			log(" === Cup2 Ant Task ended ===", Project.MSG_INFO);
	} /*end of execute*/


	/**
	 * display all field values
	 */
	private void displayValues()
	{
		log("cup2 task - attribute settings: \n ========================", Project.MSG_INFO);
		log("spec..........: " + this.spec);
		log("- source......: " + getSpecAbsolutePath());
		log("- serialized..: " + getSerializedSpecAbsolutePath());
		log("specdir.......: " + this.specdir);
		log("cup2srcdir....: " + this.cup2srcdir);
		log("classpath.....: " + this.classpath);
		log("algorithm.....: " + this.algorithm);
		log("verbose.......: " + this.verbose);
		log("parsetable....: " + this.parsetable);
		log(" ========================");
	}


	/**
	 * Compiles CUP2 from its sources.
	 */
	private void compileCup2()
	{
		log(" === Compiling CUP2 sources ===", Project.MSG_INFO);

		//set params
		Javac jcct = new Javac();
		jcct.setSourcepath(new Path(this.getProject(), cup2srcdir));
		jcct.setSrcdir(new Path(this.getProject(), cup2srcdir));
		jcct.setDestdir(new File(this.destdir));
		jcct.setClasspath(classpath);

		jcct.setProject(this.getProject());

		//compile cup2 sources
		jcct.execute();
	}


	/**
	 * Compiles the CUP2Specification.
	 */
	private void compileSpec()
	{
		//setting params for the compile task
		log(" === Creating parser  (Specification = " + this.spec + " Directory = "
			+ this.specdir + ")", Project.MSG_INFO);
		Javac jsct = new Javac(); //JavaSpecCompileTaks  
		jsct.setProject(this.getProject());
		jsct.setSrcdir(new Path(this.getProject(), specdir));
		jsct.setIncludes(getSpecRelativePath());
		jsct.setClasspath(classpath);
		jsct.setDestdir(new File(destdir));

		//compile the spec
		jsct.execute();
	}


	/**
	 * Creates a generator depending on algorithm attribute.
	 */
	private LRGenerator<? extends Object, ? extends Object> createGenerator(
		CUP2Specification spec)
		throws GeneratorException
	{
		LRGenerator<? extends Object, ? extends Object> generator = null;
		if (verbose)
			log("algorithm = " + algorithm);
		if (this.algorithm == null || this.algorithm.equals("lr1"))
		{
			if (verbose)
				log("instantiating lr1 generator...");
			generator = new LR1Generator(spec);
		}
		else if (this.algorithm.equals("lr0"))
		{
			if (verbose)
				log("instantiating lr0 generator...");
			generator = new LR0Generator(spec);
		}
		else if (this.algorithm.equals("lalr1"))
		{
			if (verbose)
				log("instantiating lalr1 generator...");
			generator = new LALR1Generator(spec);
		}
		return generator;
	}


	/**
	 * Instantiates the {@link CUP2Specification} with reflection.
	 */
	@SuppressWarnings("unchecked") private CUP2Specification createCUP2Specification()
		throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		Class<CUP2Specification> c = (Class<CUP2Specification>) Class.forName(spec);
		return (CUP2Specification) c.newInstance();
	}


	/**
	 * Saves the parsing table to a file
	 * (if the related specification is newer than the
	 * cup2 file that is to hold the serialization)
	 */
	private void serializeParser(LRParser parser)
	{
		File cup2File = new File(getSerializedSpecAbsolutePath());
		File specFile = new File(getSpecAbsolutePath());
		
		if (verbose) //if verbose = "on" ==> print files and time stamps
		{
			log("specification source file     = " + getSpecAbsolutePath());
			log("Cup2 file for serialization   = " + cup2File.getAbsolutePath());
			log("specification - last modified = " + specFile.lastModified());
			log("CUP2 file     - last modified = " + cup2File.lastModified());
		}

		//check if CUP2 file is not older than spec (spec has to be newer)
		if (specFile.lastModified() > cup2File.lastModified() || !cup2File.exists())
		{
			//spec file is newer ==> serialize

			if (verbose && !cup2File.exists())
				log("cup2 file for serialization does not yet exist and is created...");
			else if (verbose && specFile.lastModified() > cup2File.lastModified())
				log("Spec file is newer than cup2 file... creating cup2 file");

			//create LRParserSerialization object
			LRParserSerialization serial = new LRParserSerialization(cup2File.getAbsolutePath());

			//serialize parser
			serial.saveParser(parser);
		}
		else if (verbose)
		{
			log("Specification is older than CUP2 file. No serialization.");
		}
	}


	/**
	 * output the parsing table of the LRParser to a file
	 * @param table: Parsing Table of an LRParser
	 * @param fileName: filename for the output
	 */
	private void printParsingTable(LRParsingTable table, String fileName)
	{
		if (this.verbose)
		{
			log("Printing ParsingTable to: " + fileName);
		}
		File file = new File(fileName);
		LRParsingTableDump.dumpToHTML(table, file);
	}
	
	
	private String getSpecRelativePath()
	{
		return spec.replaceAll("\\.", "/") + ".java";
	}

	
	private String getSpecAbsolutePath()
	{
		return specdir + "/" + getSpecRelativePath();
	}
	
	
	private String getSerializedSpecAbsolutePath()
	{
		return destdir + "/" + spec.replaceAll("\\.", "/") + ".cup2";
	}
	
	
	public void setSpec(String spec)
	{
		this.spec = spec;
	}
	
	
	public void setSpecdir(String specdir)
	{
		this.specdir = specdir;
	}
	
	
	public void setDestdir(String destdir)
	{
		this.destdir = destdir;
	}
	
	
	public void addConfiguredClasspath(Path cp)
	{
		this.classpath = cp;
	}
	
	
	public void setCup2srcdir(String cup2srcdir)
	{
		this.cup2srcdir = cup2srcdir;
	}
	
	
	public void setAlgorithm(String s)
	{
		this.algorithm = s.trim().toLowerCase();
	}
	
	
	public void setVerbose(boolean b)
	{
		this.verbose = b;
	}
	
	
	public void setParsetable(String parsetable)
	{
		this.parsetable = parsetable;
	}
	

}
