package edu.tum.cup2.scanner;


/**
 * Interface for all lexical scanners connected to CUP2.
 * 
 * @author Andreas Wenger
 */
public interface Scanner
{
  
  public ScannerToken<? extends Object> readNextTerminal()
  	throws java.io.IOException;
  
}
