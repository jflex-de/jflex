package edu.tum.cup2.io;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import edu.tum.cup2.parser.LRParser;

/**
 * This class allows to serialize a {@link LRParser}.
 * 
 * @author Michael Hausmann
 */
public class LRParserSerialization 
	implements IParserLoader, IParserSaver
{
	private String fFileName = "default.cup2"; //file that contains the serialized parser
	
	/**
	 * construct a LRParserSerialization object associated with a certain file
	 * @param strFileName
	 */
	public LRParserSerialization(String strFileName)
	{
		fFileName = strFileName;
	}
	
	/**
	 * load a LRParser instance from file
	 */
	public LRParser loadParser()
	{
		try{
			FileInputStream fis = new FileInputStream(fFileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			LRParser parser = (LRParser)ois.readObject();
			return parser;
		}catch(FileNotFoundException fnfe)
		{
			System.err.println("File Not Found: " + fFileName);
		}
		catch(IOException ioe)
		{
			System.err.println("IOException when trying to load the parser");
			System.err.println(ioe.getMessage());
		}
		catch(ClassNotFoundException cnfe)
		{
			System.err.println("ClassNotFoundException when trying to load the parser");
		}
		
		return null;
	}
	
	/**
	 * serialize the parser
	 */
	public void saveParser(LRParser parser)
	{
		try{
			//create file stream for saving the parser
			FileOutputStream fos = new FileOutputStream(fFileName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			//tell the java serialization to use PROTOCOL_VERSION_1 (maximum compatibility)
			oos.useProtocolVersion(ObjectOutputStream.PROTOCOL_VERSION_1);
			
			//serialize to parser (and all serializable components) to file
			oos.writeObject(parser);
			
			//close the streams
			fos.close();
			oos.close();
		}catch(FileNotFoundException fnfe)
		{
			System.err.println("FileNotFoundException " + fnfe.getMessage());
		}
		catch(IOException ioe)
		{
			System.err.println("IOException " + ioe.getMessage());
			ioe.printStackTrace();
			
		}
	}

}
