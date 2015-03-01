package edu.tum.cup2.util;

import java.util.ArrayList;
import java.util.List;


public class TokenNumberGenerator
{
	private final boolean	DEBUG = false;

	/** Storage type for possible number **/
	private class MinMaxNumber
	{
		public MinMaxNumber(int i, int minimum, int maximum) {
			value = i;
			minValue = minimum;
			maxValue = maximum;
		}
		public MinMaxNumber() {
		}

		public int	value		= Integer.MIN_VALUE;
		public int	minValue	= Integer.MIN_VALUE;
		public int	maxValue	= Integer.MIN_VALUE;

		public void set(int i, int minimum, int maximum) {
			value = i;
			minValue = minimum;
			maxValue = maximum;
		}
		public String toString() {
			return /*minValue+"<="+*/value+"<="+maxValue;
		}
	}

	/** List of numbers which may state a rule **/
	private ArrayList<MinMaxNumber>	numbers				= new ArrayList<MinMaxNumber>();
	/** Current position from where to read/write **/
	public int						position			= 0;
	public int						lastBreakPosition	= 0;
	/** Number of token-tries**/
	public int						tokenInsertTries	= 0;
	public int						startPosition;
	public int						inserted;
	public int						tooMuchPosition;
	public int						anzTooMuch;
	public int						possibilitiesBefore	= 0;
	public int						possibilitiesBeforeTooMuch;
	public int						errorSyncSize		= 0;
	public int						lastInsertStart = 0;

	public TokenNumberGenerator() {
		if (DEBUG)
			System.out.println("create");
	}
	public TokenNumberGenerator(Integer tokRec_tCS_size) {
		for (int i = 0; i < tokRec_tCS_size; i++)
			numbers.add(null);
		numbers.add(new MinMaxNumber(0, -1, 0));
		if (DEBUG)
			System.out.println("create : "+numbers);
	}
	public Integer getTokenNumber(int position, int minimum, int maximum)
		throws RuntimeException {
		//tokenInsertTries--;
		// item was disabled
		if (DEBUG)
			System.out.print("get    : ");
		if (position == numbers.size())
			numbers.add(new MinMaxNumber(minimum, minimum, maximum));
		else
		if (position > numbers.size())
			throw new RuntimeException(
				"TokenNumberGenerator can only produce one number at a time");
		MinMaxNumber i = numbers.get(position);
		if (i == null) {
			i = new MinMaxNumber(minimum, minimum, maximum);
			numbers.set(position, i);
		}else{
			if (maximum>i.maxValue) i.maxValue = maximum;
			if (minimum<i.minValue) i.minValue = minimum;
			if (i.value==i.minValue && position < numbers.size()-1)
				System.err.print("critical numGen error: minValue-get (check missed)"); // TODO : Would be a big problem if it happens!
		}
		possibilitiesBefore += maximum-minimum; // count as info is now newly available
		if (i.value > maximum || i.value < minimum) {
			//System.err.println("minimum too high :-(");
			i.maxValue = maximum;
			i.minValue = minimum;
			i.value = minimum;
			if (position>0)
				increase(position-1);
		}
		if (DEBUG)
			System.out.println(numbers+"("+position+","+possibilitiesBefore+")");
		return i.value;
	}/*
	public int getTokenNumber(int position, int maximum) {
		return getTokenNumber(position, 0, maximum);
	}*/
	private void increase(int pos) {
		MinMaxNumber itm = numbers.get(pos);
		itm.value++;
		if (itm.value > itm.maxValue) {
			if (pos == 0)
			{
				for (int i = pos; i < numbers.size(); i++)
					numbers.set(i, null);
				throw new RuntimeException("All possible token values checked..");
			}
			increase(pos - 1);
		} else {
			//System.out.println("changing last insertStart from "+lastInsertStart+" to "+pos);
			this.lastInsertStart = pos;
			for (int i = pos + 1; i < numbers.size(); i++)
				numbers.set(i, null);
		}
	}
	public boolean indicateBad(int position) {
		if (DEBUG)
			System.out.println("bad    : "+numbers+"("+position+","+possibilitiesBefore+")");
		lastBreakPosition = position;
		if (position > numbers.size())
			return false; // TODO !!!
		//throw new RuntimeException("TokenNumberGenerator can only produce one number at a time");
		else
			if (position == numbers.size())
				numbers.add(new MinMaxNumber(-1, -1, 0)); // for old token rec. only?? TODO
		try {
			if (numbers.get(position)!=null)
				increase(position);
		} catch (RuntimeException e) {
			if (DEBUG)
				System.out.println("         really bad...");
			return false;
		}
		if (DEBUG)
			System.out.println("         "+numbers);
		return true;
	}
	public boolean hasNumberNotMinimumOrNull(int position2) {
		if (numbers.size() <= position2)
			return true;
		else {
			MinMaxNumber x = numbers.get(position2);
			// if x==null and x minvalued, count it's poss.
			// because item has already correct information
			// but is disabled (value==-1)
			if (x!=null && x.value==x.minValue)
				possibilitiesBefore += x.maxValue-x.minValue;
			return (x == null || x.value > x.minValue);
		}
	}
	public void removeFirst() {
		if ( --startPosition==0)
			startPosition = 0;
		if (--position<0)
			position = 0;
		if (numbers.size()>0)
			numbers.remove(0);
	}/*
	public boolean wasSetToNull(int position) {
		if (numbers.size() <= position)
			return false;
		else {
			return (numbers.get(position) == null);
		}
	}*//*
	public boolean isEndNumber(int position)
	{
		return (this.startPosition+this.inserted<position);
	}*/
	public void setValue(int i, Integer terminalToChoose) {
		if (numbers.size()<=i) return;
		MinMaxNumber n = numbers.get(i);
		if (n==null) return;
		n.value = terminalToChoose;
		/*if (n.value>n.maxValue || n.value<n.minValue)
			throw new RuntimeException("DEBUG RuntimeException");*/
	}

}
