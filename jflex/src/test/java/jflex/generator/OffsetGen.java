package jflex.generator;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/** Generator for Offset data values */
public class OffsetGen extends Generator<Integer> {
  public OffsetGen() {
    super(Integer.class);
  }

  @Override
  public Integer generate(SourceOfRandomness r, GenerationStatus status) {
    int rnd = r.nextInt(1, 100);

    // 1% change of getting number 0
    if (rnd == 1) {
      return 0;
    }

    // 1% change of getting number 1
    if (rnd == 2) {
      return 1;
    }

    // 1% change of getting Integer.MAX_VALUE
    if (rnd == 3) {
      return Integer.MAX_VALUE;
    }

    // 15% chance of getting a "larger" size
    if (rnd >= 4 && rnd <= 18) {
      return r.nextInt(200_001, 10_000_000);
    }

    // 5% chance of getting a "huge" size
    if (rnd >= 19 && rnd <= 23) {
      return r.nextInt(10_000_001, Integer.MAX_VALUE);
    }

    // 77% - normalish size
    return r.nextInt(100, 20_000);
  }
}
