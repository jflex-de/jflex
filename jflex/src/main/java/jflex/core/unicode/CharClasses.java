/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.0-SNAPSHOT                                                    *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.core.unicode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jflex.chars.Interval;
import jflex.logging.Out;

/**
 * Character Classes.
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.0-SNAPSHOT
 */
public class CharClasses {

  /** debug flag (for char classes only) */
  private static final boolean DEBUG = false;

  /** the largest character that can be used in char classes */
  public static final int maxChar = 0x10FFFF;

  /** the char classes */
  private List<IntCharSet> classes;

  /** the largest character actually used in a specification */
  private int maxCharUsed;

  /** the @{link UnicodeProperties} the scanner used */
  private UnicodeProperties unicodeProps;

  /**
   * Constructs a new CharClasses object.
   *
   * <p>CharClasses.init() is delayed until UnicodeProperties.init() has been called, since the max
   * char code won't be known until then.
   */
  public CharClasses() {}

  /**
   * Provides space for classes of characters from 0 to maxCharCode.
   *
   * <p>Initially all characters are in class 0.
   *
   * @param maxCharCode the last character code to be considered. (127 for 7bit Lexers, 255 for 8bit
   *     Lexers and UnicodeProperties.getMaximumCodePoint() for Unicode Lexers).
   * @param scanner the scanner containing the UnicodeProperties instance from which caseless
   */
  public void init(int maxCharCode, ILexScan scanner) {
    if (maxCharCode < 0) {
      throw new IllegalArgumentException("maxCharCode " + maxCharCode + " is negative.");
    } else if (maxCharCode > maxChar) {
      throw new IllegalArgumentException(
          "maxCharCode "
              + Integer.toHexString(maxCharCode)
              + " is larger than maxChar "
              + Integer.toHexString(maxChar));
    }

    maxCharUsed = maxCharCode;
    this.unicodeProps = scanner.getUnicodeProperties();
    classes = new ArrayList<>();
    classes.add(IntCharSet.ofCharacterRange(0, maxCharCode));
  }

  /**
   * Returns the greatest Unicode value of the current input character set.
   *
   * @return unicode value.
   */
  public int getMaxCharCode() {
    return maxCharUsed;
  }

  /**
   * Sets the largest Unicode value of the current input character set.
   *
   * @param maxCharCode the largest character code, used for the scanner (i.e. %7bit, %8bit, %16bit
   *     etc.)
   */
  public void setMaxCharCode(int maxCharCode) {
    if (maxCharCode < 0) {
      throw new IllegalArgumentException("maxCharCode " + maxCharCode + " is negative.");
    } else if (maxCharCode > maxChar) {
      throw new IllegalArgumentException(
          "maxCharCode "
              + Integer.toHexString(maxCharCode)
              + " is larger than maxChar "
              + Integer.toHexString(maxChar));
    }

    maxCharUsed = maxCharCode;
  }

  /**
   * Returns the current number of character classes.
   *
   * @return number of character classes.
   */
  public int getNumClasses() {
    return classes.size();
  }

  /**
   * Updates the current partition, so that the specified set of characters gets a new character
   * class.
   *
   * <p>Characters that are elements of {@code set} are not in the same equivalence class with
   * characters that are not elements of {@code set}.
   *
   * @param set the set of characters to distinguish from the rest
   * @param caseless if true upper/lower/title case are considered equivalent
   */
  public void makeClass(IntCharSet set, boolean caseless) {
    set = IntCharSet.copyOf(set); // avoid destructively updating the original

    if (caseless) set = set.getCaseless(unicodeProps);

    if (DEBUG) {
      Out.dump("makeClass(" + set + ")");
      dump();
    }

    int oldSize = classes.size();
    for (int i = 0; i < oldSize; i++) {
      IntCharSet x = classes.get(i);

      if (Objects.equals(x, set)) return;

      IntCharSet and = x.and(set);

      if (and.containsElements()) {
        if (Objects.equals(x, and)) {
          set.sub(and);
          continue;
        } else if (Objects.equals(set, and)) {
          x.sub(and);
          classes.add(and);
          if (DEBUG) {
            Out.dump("makeClass(..) finished");
            dump();
          }
          return;
        }

        set.sub(and);
        x.sub(and);
        classes.add(and);
      }
    }

    if (DEBUG) {
      Out.dump("makeClass(..) finished");
      dump();
    }
  }

  /**
   * Returns the code of the character class the specified character belongs to.
   *
   * @param codePoint code point.
   * @return code of the character class.
   */
  public int getClassCode(int codePoint) {
    int i = -1;
    while (true) {
      IntCharSet x = classes.get(++i);
      if (x.contains(codePoint)) return i;
    }
  }

  /** Dumps charclasses to the dump output stream. */
  public void dump() {
    Out.dump(toString());
  }

  /**
   * Returns a string representation of one char class
   *
   * @param theClass the index of the class to
   * @return a {@link java.lang.String} object.
   */
  public String toString(int theClass) {
    return classes.get(theClass).toString();
  }

  /**
   * Returns a string representation of the char classes stored in this class.
   *
   * <p>Enumerates the classes by index.
   *
   * @return representation of this char class.
   */
  public String toString() {
    StringBuilder result = new StringBuilder("CharClasses:");

    result.append(Out.NL);

    for (int i = 0; i < classes.size(); i++)
      result
          .append("class ")
          .append(i)
          .append(":")
          .append(Out.NL)
          .append(classes.get(i))
          .append(Out.NL);

    return result.toString();
  }

  /**
   * Creates a new character class for the single character {@code singleChar}.
   *
   * @param caseless if true upper/lower/title case are considered equivalent
   * @param singleChar character.
   */
  public void makeClass(int singleChar, boolean caseless) {
    makeClass(IntCharSet.ofCharacter(singleChar), caseless);
  }

  /**
   * Creates a new character class for each character of the specified String.
   *
   * @param caseless if true upper/lower/title case are considered equivalent
   * @param str set of characters.
   */
  public void makeClass(String str, boolean caseless) {
    for (int i = 0; i < str.length(); ) {
      int ch = str.codePointAt(i);
      makeClass(ch, caseless);
      i += Character.charCount(ch);
    }
  }

  /**
   * Updates the current partition, so that the specified set of characters gets a new character
   * class.
   *
   * <p>Characters that are elements of the set {@code l} are not in the same equivalence class with
   * characters that are not elements of the set {@code l}.
   *
   * @param l a List of Interval objects. This List represents a set of characters. The set of
   *     characters is the union of all intervals in the List.
   * @param caseless if true upper/lower/title case are considered equivalent
   */
  public void makeClass(List<Interval> l, boolean caseless) {
    makeClass(IntCharSet.of(l), caseless);
  }

  /**
   * Updates the current partition, so that the set of all characters not contained in the specified
   * set of characters gets a new character class.
   *
   * <p>Characters that are elements of the set {@code v} are not in the same equivalence class with
   * characters that are not elements of the set {@code v}.
   *
   * <p>This method is equivalent to {@code makeClass(v)}
   *
   * @param l a List of Interval objects. This List represents a set of characters. The set of
   *     characters is the union of all intervals in the List.
   * @param caseless if true upper/lower/title case are considered equivalent
   */
  public void makeClassNot(List<Interval> l, boolean caseless) {
    makeClass(IntCharSet.of(l), caseless);
  }

  /**
   * Returns an array that contains the character class codes of all characters in the specified set
   * of input characters.
   */
  public int[] getClassCodes(IntCharSet set, boolean negate) {

    if (DEBUG) {
      Out.dump("getting class codes for " + set);
      if (negate) Out.dump("[negated]");
    }

    int size = classes.size();

    // [fixme: optimize]
    int[] temp = new int[size];
    int length = 0;

    for (int i = 0; i < size; i++) {
      IntCharSet x = classes.get(i);
      if (negate) {
        if (!set.and(x).containsElements()) {
          temp[length++] = i;
          if (DEBUG) Out.dump("code " + i);
        }
      } else {
        if (set.and(x).containsElements()) {
          temp[length++] = i;
          if (DEBUG) Out.dump("code " + i);
        }
      }
    }

    int[] result = new int[length];
    System.arraycopy(temp, 0, result, 0, length);

    return result;
  }

  /**
   * Check consistency of the stored classes [debug].
   *
   * <p>all classes must be disjoint, checks if all characters have a class assigned.
   */
  public void check() {
    for (int i = 0; i < classes.size(); i++)
      for (int j = i + 1; j < classes.size(); j++) {
        IntCharSet x = classes.get(i);
        IntCharSet y = classes.get(j);
        if (x.and(y).containsElements()) {
          System.out.println("Error: non disjoint char classes " + i + " and " + j);
          System.out.println("class " + i + ": " + x);
          System.out.println("class " + j + ": " + y);
        }
      }

    // check if each character has a classcode
    // (= if getClassCode terminates)
    for (int c = 0; c < maxChar; c++) {
      getClassCode(c);
      if (c % 100 == 0) System.out.print(".");
    }

    getClassCode(maxChar);
  }

  /**
   * Returns an array of all CharClassIntervals in this char class collection.
   *
   * <p>The array is ordered by char code, i.e. {@code result[i+1].start = result[i].end+1} Each
   * CharClassInterval contains the number of the char class it belongs to.
   *
   * @return an array of all {@link CharClassInterval} in this char class collection.
   */
  public CharClassInterval[] getIntervals() {
    int i, c;
    int size = classes.size();
    int numIntervals = 0;

    for (i = 0; i < size; i++) numIntervals += (classes.get(i)).numIntervals();

    CharClassInterval[] result = new CharClassInterval[numIntervals];

    i = 0;
    c = 0;
    while (i < numIntervals) {
      int code = getClassCode(c);
      IntCharSet set = classes.get(code);
      Interval iv = set.getNext();

      result[i++] = new CharClassInterval(iv.start, iv.end, code);
      c = iv.end + 1;
    }

    return result;
  }

  /**
   * Brings the partitions into a canonical order such that objects that implement the same
   * partitions but in different order become equal.
   *
   * <p>For example, [ {0}, {1} ] and [ {1}, {0} ] implement the same partition of the set {0,1} but
   * have different content. Different order will lead to different input assignments in the NFA and
   * DFA phases and will make otherwise equal automata look distinct.
   *
   * <p>This is not needed for correctness, but it makes the comparison of output DFAs (e.g. in the
   * test suite) for equivalence more robust.
   */
  public void normalise() {
    classes.sort((s1, s2) -> s1.compareTo(s2));
  }
}
