package de.jflex.benchmark;

import de.jflex.benchmark.pregen.NoAction17;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

// @BenchmarkMode({Mode.AverageTime, Mode.SampleTime, Mode.SingleShotTime})
@BenchmarkMode({Mode.AverageTime, Mode.SingleShotTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1)
public class JFlexBench {

  @State(Scope.Benchmark)
  public static class LexerState {
    /**
     * Factor by which to scale the input size. We should see a benchmark time roughly linear in the
     * factor, i.e. the first time times 10 and 100.
     */
    @Param({"100", "1000", "10000"})
    public int factor;

    /** The length of the input for the benchmark. We give this to the baseline, but not JFlex. */
    public int length;

    /** The reader the input will be read from. Must support {@code reset()}. */
    public Reader reader;

    /** Create input and populate state fields. Runs once per entire benchmark. */
    @Setup
    public void setup() {
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < 10 * factor; i++) {
        // TODO: better input
        builder.append("aaa");
        builder.append("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        builder.append("  ");
      }
      length = builder.length();
      reader = new StringReader(builder.toString());
    }
  }

  @Benchmark
  public int noActionLexer(LexerState state) throws IOException {
    state.reader.reset();
    return new NoAction(state.reader).yylex();
  }

  @Benchmark
  public int noAction17Lexer(LexerState state) throws IOException {
    state.reader.reset();
    return new NoAction17(state.reader).yylex();
  }

  /**
   * The base line: a single continuous pass accessing each character once, through a buffer filled
   * by a reader in one single reader invocation.
   */
  @Benchmark
  public void baselineReader(LexerState state, Blackhole bh) throws IOException {
    char[] buffer = new char[state.length];
    state.reader.reset();
    state.reader.read(buffer, 0, buffer.length);
    for (int i = 0; i < buffer.length; i++) {
      bh.consume(buffer[i]);
    }
  }

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder().include(JFlexBench.class.getSimpleName()).build();

    new Runner(opt).run();
  }
}
