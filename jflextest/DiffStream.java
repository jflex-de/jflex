package jflextest;

import java.io.*;
import java.util.*;

class DiffStream {

	public DiffStream() { }
	
	private int linesProcessed;
	
  /**
   * Test if two strings are equal modulo \ and /
   * 
   * @param s1  the first string
   * @param s2  the second string
   * 
   * @return true if they are equal 
   */
  private boolean match(String s1, String s2) {
    if (s1 == null) return s2 == null;        
    return s1.replace('\\', '/').equals(s2.replace('\\', '/'));    
  }
  
	public String diff(Reader input, Reader expected) {
    return diff(null,input,expected);
  }

  /**
   * Compares to streams line by line. 
   *
   * @return <code>null</code>  if they are the same, a String error message otherwise
   * 
   * @param diffLines   a List of Integer. The line numbers where
   *                    differences are expected (and to be ignored) 
   * @param input       the input stream
   * @param expected    the control stream
   */
	public String diff(List<Integer> diffLines, Reader input, Reader expected) {
    if (diffLines == null) diffLines = new ArrayList<Integer>();
		linesProcessed = 1;
		BufferedReader myInput = new BufferedReader(input);
		String readFromInput;
		BufferedReader myExpected = new BufferedReader(expected);
		String readFromExpected;
		try {
			do {
				readFromInput = myInput.readLine();
				readFromExpected = myExpected.readLine();
				// EOF reached in one Reader ?? -> Error
				if ((readFromInput==null)&&(readFromExpected!=null)) {
					return "EOF reached in Expected(Line:"+linesProcessed+")... got more Lines to compare.\n";
				}
				if ((readFromInput!=null)&&(readFromExpected==null)) {
					return "EOF reached in Input(Line:"+linesProcessed+")... more Lines expected.\n";
				}
        if (readFromInput != null && !match(readFromInput,readFromExpected)) {
          if (!diffLines.contains(new Integer(linesProcessed)))
            return "Difference in line "+linesProcessed+"\n"+
                   "expected :["+readFromExpected+"]\n"+
                   "but got  :["+readFromInput+"]";
        }
				linesProcessed++;			
			} while ((readFromInput!=null)&&(readFromExpected!=null));
		} 
		catch (IOException e) {
      return "Error processing Files...compare aborted.\n";
		}
		
		return null;		
	}

  public String diff(String input, String expected) {
    return diff(new StringReader(input), new StringReader(expected));
  }
}
