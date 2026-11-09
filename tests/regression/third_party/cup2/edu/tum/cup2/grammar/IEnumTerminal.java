package edu.tum.cup2.grammar;

/**
 * Interface to equip the {@link Terminal} with {@link Enum} methods which it gets anyway when it is used with enums
 * (which is as basic principal of CUP2).
 * 
 * @author Gero
 */
public interface IEnumTerminal
{
	/**
	 * @return The ordinal value of this enum
	 * @see Enum#ordinal()
	 */
	public int ordinal();
}
