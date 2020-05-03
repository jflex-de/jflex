/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.core.unicode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import jflex.base.Pair;
import jflex.chars.Interval;
import jflex.logging.Out;

/**
 * Character Classes.
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.2
 */
public class CharClasses {

  /** debug flag (for char classes only) */
  private static final boolean DEBUG = false;

  /** for sorting disjoint IntCharSets */
  private static final Comparator<IntCharSet> INT_CHAR_SET_COMPARATOR = new IntCharSetComparator();

  /** the largest character that can be used in char classes */
  public static final int maxChar = 0x10FFFF;

  /** the char classes */
  private List<IntCharSet> classes;

  /** the largest character actually used in a specification */
  private int maxCharUsed;

  /** the @{link UnicodeProperties} the spec scanner used */
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

  /** @return a deep-copy list of all char class partions. */
  public List<IntCharSet> allClasses() {
    List<IntCharSet> result = new ArrayList<>();
    for (IntCharSet ccl : classes) {
      result.add(IntCharSet.copyOf(ccl));
    }
    return result;
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

  /**
   * Retuns a copy of a single char class partition by code.
   *
   * @param code the code of the char class partition to return.
   * @return a copy of the char class with the specified code.
   */
  public IntCharSet getCharClass(int code) {
    return IntCharSet.copyOf(classes.get(code));
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

  @Override
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
   * @param str the String to iterate single char class creation over.
   */
  public void makeClass(String str, boolean caseless) {
    for (int i = 0; i < str.length(); ) {
      int ch = str.codePointAt(i);
      makeClass(ch, caseless);
      i += Character.charCount(ch);
    }
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
   * Checks the invariants of this object.
   *
   * <p>All classes must be disjoint, and their union must be the entire input set.
   *
   * @return true when the invariants of this objects hold.
   */
  public boolean invariants() {
    for (int i = 0; i < classes.size(); i++)
      for (int j = i + 1; j < classes.size(); j++) {
        if (classes.get(i).and(classes.get(j)).containsElements()) {
          return false;
        }
      }

    IntCharSet union = new IntCharSet();
    for (IntCharSet i : classes) {
      union.add(i);
    }

    return IntCharSet.allChars().equals(union);
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
    classes.sort(INT_CHAR_SET_COMPARATOR);
  }

  /**
   * Construct a (deep) copy of the the provided CharClasses object.
   *
   * @param c the CharClasses to copy
   * @return a deep copy of c
   */
  public static CharClasses copyOf(CharClasses c) {
    CharClasses result = new CharClasses();
    result.maxCharUsed = c.maxCharUsed;
    result.unicodeProps = c.unicodeProps;
    result.classes = c.allClasses();
    return result;
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

    for (i = 0; i < size; i++) numIntervals += classes.get(i).numIntervals();

    List<Iterator<Interval>> iterators = new ArrayList<>();
    for (IntCharSet set : classes) iterators.add(set.intervalIterator());

    CharClassInterval[] result = new CharClassInterval[numIntervals];

    i = 0;
    c = 0;
    while (i < numIntervals) {
      int code = getClassCode(c);
      Interval iv = iterators.get(code).next(); // must have enough elements
      result[i++] = new CharClassInterval(iv.start, iv.end, code);
      c = iv.end + 1;
    }

    return result;
  }

  /**
   * Computes a two-level table structure representing this CharClass object, where second-level
   * blocks are shared if equal. The hope is that this sharing happens (very) often with a large
   * number of blocks being mapped to the same character class.
   *
   * @return a pair of a top-level table, and a list of second-level blocks for this char class
   *     object.
   */
  Pair<int[], List<CMapBlock>> computeTables() {
    CharClassInterval[] intervals = getIntervals();
    int intervalIndex = 0;
    int curClass = intervals[intervalIndex].charClass;
    int codePoint = 0;

    int topLevelSize = (maxCharUsed + 1) >> CMapBlock.BLOCK_BITS;
    int[] topLevel = new int[topLevelSize];
    List<CMapBlock> blocks = new ArrayList<>();

    for (int topIndex = 0; topIndex < topLevelSize; topIndex++) {
      int[] block = new int[CMapBlock.BLOCK_SIZE];
      for (int i = 0; i < CMapBlock.BLOCK_SIZE; i++, codePoint++) {
        // if maxCharUsed doesn't align to CMapBlock.BLOCK_BITS, we leave the
        // rest of the highest block equal to 0.
        if (maxCharUsed < codePoint) break;
        if (!intervals[intervalIndex].contains(codePoint)) {
          curClass = intervals[++intervalIndex].charClass;
        }
        block[i] = curClass;
      }
      // find earliest equal block (if any)
      CMapBlock b = new CMapBlock(block);
      int idx = blocks.indexOf(b);
      if (idx < 0) {
        idx = blocks.size();
        blocks.add(b);
      }
      topLevel[topIndex] = idx;
    }
    return new Pair<int[], List<CMapBlock>>(topLevel, blocks);
  }

  /** Turn a list of second-level blocks into a flat array. */
  private static int[] flattenBlocks(List<CMapBlock> blocks) {
    int[] result = new int[blocks.size() * CMapBlock.BLOCK_SIZE];
    for (int i = 0; i < blocks.size(); i++) {
      int[] block = blocks.get(i).block;
      System.arraycopy(block, 0, result, i << CMapBlock.BLOCK_BITS, CMapBlock.BLOCK_SIZE);
    }
    return result;
  }

  /**
   * Returns a two-level table structure for this char-class object. The char class of input {@code
   * x} is {@code snd[(fst[x >> BLOCK_BITS]) | (x && BLOCK_MASK))]} where {@code BLOCK_MASK =
   * BLOCK_SIZE - 1}, and the index of the first block in the top level is guaranteed to be 0 (which
   * means the {@code fst} lookup can be skipped if {@code x <= BLOCK_MASK}).
   *
   * @see CMapBlock#BLOCK_BITS
   * @see CMapBlock#BLOCK_SIZE
   */
  public Pair<int[], int[]> getTables() {
    Pair<int[], List<CMapBlock>> p = computeTables();
    int[] shifted = new int[p.fst.length];
    for (int i = 0; i < p.fst.length; i++) {
      shifted[i] = p.fst[i] << CMapBlock.BLOCK_BITS;
    }
    return new Pair<int[], int[]>(shifted, flattenBlocks(p.snd));
  }
}
