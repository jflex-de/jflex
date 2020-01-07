package de.jflex.benchmark;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

// @BenchmarkMode({Mode.AverageTime, Mode.SampleTime})
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class JFlexBench {

  @Benchmark
  public int dummy() {
    // do anything silly
    int x = 0;
    for (int i = 0; i < 1000; i++) {
      x += i;
    }
    return x;
  }

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder().include(JFlexBench.class.getSimpleName()).forks(1).build();

    new Runner(opt).run();
  }
}
