package edu.tum.cup2.grammar;



/**
 * Enum for special terminals.
 *
 * @author Andreas Wenger
 */
public enum SpecialTerminals
	implements Terminal
{

	/**
	 * The epsilon terminal.
	 */
	Epsilon,


	/**
	 * The end of input stream terminal.
	 */
	EndOfInputStream,


	/**
	 * Placeholder, when a terminal must be given, but
	 * is not needed.
	 */
	Placeholder,


	/**
	 * Action marker.
	 */
	$a,


	/**
	 * Unused terminal, that is a placeholder for
	 * the whole row of an LR action table.
	 */
	WholeRow,

	/**
	 * Error terminal, used in order to handle errors during parse-time.
	 * Do not read/write this terminal from/to the input stream!
	 * It represents an arbitrary amount of terminals.
	 * Usage:
	 *  prod(
	 *    MyNonTerminal,
	 *    rhs(SpecialTerminals.Error),
	 *    new Action() {
	 *      public void a(ErrorInformation errorTokens)
	 *      {
	 *        System.err.println("Oh no, you have some error in the input : "+errorTokens);
	 *      }
	 *    }
	 *  )
	 *
	 */
	Error, //BestError,

}
