package edu.tum.cup2.scanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import edu.tum.cup2.grammar.SpecialTerminals;
import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.spec.CUP2Specification;

public class RandomTokenCreator
	implements Scanner
{

	private final Terminal[]									terminals;
	private final Random										r;
	private final List<List<ScannerToken<? extends Object>>>	correctInputs;
	private LinkedList<ScannerToken<? extends Object>>			toOutput	= new LinkedList<ScannerToken<? extends Object>>();
	private int	inputLength;

	public RandomTokenCreator( CUP2Specification spec, List<List<ScannerToken<? extends Object>>> correctList )
	{
		LinkedList<Terminal> terminals = (LinkedList<Terminal>) spec.getGrammar().getTerminals().clone();
		terminals.remove(SpecialTerminals.Error);
		terminals.remove(SpecialTerminals.Epsilon);
		terminals.remove(SpecialTerminals.$a);
		terminals.remove(SpecialTerminals.Placeholder);
		terminals.remove(SpecialTerminals.WholeRow);
		terminals.remove(SpecialTerminals.EndOfInputStream);
		this.terminals = terminals.toArray(new Terminal[terminals.size()]);
		r = new Random();
		if (correctList==null)
			correctList =  new LinkedList<List<ScannerToken<? extends Object>>>();
		this.correctInputs = correctList;
		this.inputLength = r.nextInt(50)+10;
	}
	public RandomTokenCreator( CUP2Specification spec )
	{
		this( spec, null );
	}
	
	public ScannerToken<? extends Object> readNextTerminal() throws IOException {
		if (inputLength--<=0)
			return new ScannerToken<Object>(SpecialTerminals.EndOfInputStream,"$Random_1$");
		while (true)
		{
			if (toOutput.size()>0)
			{
				if (r.nextInt()<=-0x11111111)
				{
					return getRandomToken();
				}
				else if (r.nextInt()<=-0x11111111)
					toOutput.poll();
				else
					return (ScannerToken<Object>) toOutput.poll();
			}
			if (r.nextInt(50)==1)
				for (int j=0;j<r.nextInt(3); j++)
					toOutput.add(getRandomToken());
			else if (correctInputs.size()!=0)
			{
				// correct tokens
				int i = r.nextInt(correctInputs.size());
				toOutput.addAll(correctInputs.get(i));
			}
		}
	}
	private ScannerToken<? extends Object> getRandomToken() {
		ScannerToken<? extends Object> t = null;
		while (t==null)
		{
			if (correctInputs.size()==0)
			{
				System.err.println("No correct example-input for random token creator!");
				inputLength = 0;
				return new ScannerToken<Object>(SpecialTerminals.EndOfInputStream,"$Random_2$");
			}
			int i = r.nextInt(correctInputs.size());
			List<ScannerToken<? extends Object>> l = correctInputs.get(i);
			if (l.size()==0)
				correctInputs.remove(i);
			else
				t = l.get(r.nextInt(l.size()));
		}
		return t;
	}

}
