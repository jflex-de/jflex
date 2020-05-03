/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2019  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.core.unicode;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.generator.Size;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;
import java.util.ArrayList;
import java.util.List;

/**
 * Generator for random {@link CharClasses} instances.
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.2
 * @see CharClasses
 */
public class CharClassesGen extends Generator<CharClasses> {

  /**
   * Min bound for number of classes to add to default (must be => 0).
   *
   * <p>Number of classes in the final partition is added classes + 1.
   */
  private int minSize = 0;
  /** Max bound for number of classes */
  private int maxSize = 5;

  /** Generator for classes (= IntCharSet) */
  private final IntCharSetGen classGen;

  /** The UnicodeProperties that determine maxCharCode and getCaseless */
  // TODO(lsf): make UnicodeProperties a configurable option, return something useful
  private UnicodeProperties unicodeProps;

  /** Constructs generator for CharClasses */
  public CharClassesGen() {
    super(CharClasses.class);
    this.classGen = new IntCharSetGen();
  }

  /**
   * Constructs smallest possible test value, in this case the char class with only one partition.
   *
   * @return a CharClasses object with one partition.
   */
  private CharClasses smallest() {
    CharClasses result = new CharClasses();
    // TODO(lsf): make maxChar a configurable property
    result.init(CharClasses.maxChar, new UnicodePropertiesSupplier());
    return result;
  }

  @Override
  public CharClasses generate(SourceOfRandomness r, GenerationStatus status) {
    CharClasses result = smallest();

    int numClasses = r.nextInt(minSize, maxSize);
    for (int i = 0; i < numClasses; i++) {
      // TOOD(lsf): add caseless (needs actual UnicodeProperties)
      result.makeClass(classGen.generate(r, status), false);
    }

    return result;
  }

  /**
   * Configure this generator to only produce classes in the given range.
   *
   * @param range annotation that contains the char class constraints
   */
  public void configure(InRange range) {
    classGen.configure(range);
  }

  /**
   * Configure this generator to only produce CharClasses with a given range of number of classes.
   *
   * @param size annotation that contains how many classes should be added to the defualt
   *     (one-class) CharClassSet,
   */
  public void configure(Size size) {
    minSize = Math.max(0, size.min());
    maxSize = size.max();
  }

  /** For initialising the CharClasses object. */
  public class UnicodePropertiesSupplier implements ILexScan {
    @Override
    public UnicodeProperties getUnicodeProperties() {
      return unicodeProps;
    }
  }

  @Override
  public List<CharClasses> doShrink(SourceOfRandomness random, CharClasses larger) {
    List<CharClasses> results = new ArrayList<>();

    if (larger.getNumClasses() == 1) {
      // cannot shrink further
      return results;
    }

    // Try smallest possible value
    results.add(smallest());

    // try merging partitions into size 2
    List<IntCharSet> classes = larger.allClasses();
    for (int split = 1; split < classes.size() - 1; split++) {
      IntCharSet set = new IntCharSet();
      for (int i = 0; i < split; i++) {
        set.add(classes.get(i));
      }
      CharClasses next = smallest();
      next.makeClass(set, false);

      assert next.invariants() : next;
      results.add(next);
    }

    return results;
  }
}
