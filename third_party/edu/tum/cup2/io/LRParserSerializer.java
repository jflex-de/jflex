package edu.tum.cup2.io;

import edu.tum.cup2.generator.LR1Generator;
import edu.tum.cup2.generator.exceptions.GeneratorException;
import edu.tum.cup2.parser.LRParser;
import edu.tum.cup2.spec.CUP2Specification;

/**
 * LRParserSerializer generates a parser for a given CUPSpecification and
 * serializes a LRParser (mainly the ParsingTable)
 * to a cup2 file.
 * 
 * @author Michael Hausmann 
 */

@SuppressWarnings("unchecked")
public final class LRParserSerializer 
{
	/**
	 * @param arg[0]: CUPSpecification name
	 *        arg[1]: cup2 file name where to store serialization
	 * @return 0: successful
	 *       !=0: error
	 */
	public static int main(String args[])
	{
		if(args.length != 2)
		{
			System.err.println("== ERROR LRParserSerializer.main required 2 arguments: specifiaction name, file name ==");
			return -1;
		}
		String strSpecName = args[0];
		String strFileName = args[1];
		
		try
		{
			createSerialisation(strSpecName, strFileName);
			System.out.println("Parser successfully serialized.");
		
		} catch(ClassNotFoundException cnfe) {
			System.err.println("Could not find " + strSpecName);
			System.out.println("Parser NOT successfully serialized.");
			return 1; //createSerialisation failed
		} catch(InstantiationException ie) {
			System.err.println("Could not instantiate " + strSpecName);
			System.out.println("Parser NOT successfully serialized.");
			return 2; //createSerialisation failed
		} catch(Exception e) {
			System.err.println("Parser NOT successfully serialized.");
			System.out.println("Parser NOT successfully serialized.");
			return 3; //createSerialisation failed
		}
		
		return 0; //successfully serialized
	}
	
	private static void createSerialisation(String strSpecName, String strFileName) 
		throws 
			GeneratorException, 
			InstantiationException, 
			IllegalAccessException, 
			ClassNotFoundException //createSerialisation failed
	{
		Class<CUP2Specification> c = null;
		CUP2Specification spec = null;
		LR1Generator generator = null;

		c = (Class<CUP2Specification>) Class.forName(strSpecName);
		spec = (CUP2Specification) c.newInstance();
		generator = new LR1Generator(spec);
		
		LRParser parser = new LRParser(generator.getParsingTable());
	
		//create LRParserSerialization object
		LRParserSerialization serial = new LRParserSerialization(strFileName);
		
		//serialize parser
		serial.saveParser(parser);
		
		//createSerialisation successful
	} /* end of createSerialisation */
} /*end of class*/
