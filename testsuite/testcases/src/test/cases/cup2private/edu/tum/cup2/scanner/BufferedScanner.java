package edu.tum.cup2.scanner;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * BufferedScanner is a decorator for a Scanner in CUP2.
 * It uses a buffer to implement a look ahead functionality.
 * 
 * @author Michael Hausmann
 */

public class BufferedScanner implements Scanner 
{
	protected Scanner fScanner; //reference to the used scanner, may not be null
	protected int fNumberOfBufferedTokens = 0; //number of buffered tokens (value range: 0..n)
	protected LinkedList<ScannerToken<? extends Object>> fTokenQueue = null;
	
	/**
	 * create a BufferedScanner as decorator for a Scanner in CUP2.
	 * @param usedScanner
	 * @throws IllegalArgumentException
	 */
	public BufferedScanner(Scanner usedScanner) throws IllegalArgumentException
	{
		if (usedScanner == null)
			throw new IllegalArgumentException("The Scanner passed to BufferedScanner may not be null!");
		fScanner = usedScanner;
		fTokenQueue = new LinkedList<ScannerToken<? extends Object>>();
	}

	/**
	 * Reads the next terminal from the input, unless there are buffered tokens.
	 * Maintains counter fNumberOfBufferedTokens.
	 */
	public ScannerToken<? extends Object> readNextTerminal() throws IOException {
		if(0 == fNumberOfBufferedTokens)
			return fScanner.readNextTerminal();
		else
		{
			fNumberOfBufferedTokens --;
			ScannerToken<? extends Object> t = fTokenQueue.remove(0);
			return t;
		}
	}
	
	/**
	 * lookAhead(number of tokens to look ahead in the input)
	 * 
	 * lookAhead maintains the number of buffered tokens fNumberOfBufferedTokens.
	 * If not sufficient tokens are buffered for the look ahead, read more tokens from the input.
	 * When reading after end of input there may be an IOException 
	 * (depending on the concrete implementation of readNextTerminal() of the used scanner).
	 * 
	 * @param iLookAheadAmount
	 * @return list of tokens
	 * @throws IOException
	 */
	public List<ScannerToken<? extends Object>> lookAhead(int iLookAheadAmount) throws IOException {
		int iTokensToRead = iLookAheadAmount - fNumberOfBufferedTokens;
		for(; iTokensToRead > 0 ; iTokensToRead --, fNumberOfBufferedTokens ++)
		{
			ScannerToken<? extends Object> currToken = fScanner.readNextTerminal();
			fTokenQueue.add(currToken);
		}
		
		LinkedList<ScannerToken<? extends Object>> retList = new LinkedList<ScannerToken<? extends Object>>();
		for(int i = 0 ; i < iLookAheadAmount ; i++)
		{
			retList.add(fTokenQueue.get(i));
		}
		return retList; 
	}
	
	/** Push pack a single token **/
	public void pushBackToken( ScannerToken<? extends Object> x )
	{
	  fTokenQueue.addFirst(x);
	  fNumberOfBufferedTokens++;
	}

  public boolean hasBufferedTokens() {
    return fNumberOfBufferedTokens!=0;
  }
	
} /* end of class */
