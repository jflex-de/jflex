package jflex.testing.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestSpec {

  /** Lex specification. Required. */
  String lex();

  /** Exception that the generator is expected to throw. Defaults to none. */
  Class<? extends Throwable> generatorThrows() default NoExceptionThrown.class;

  /** The expected output logs of the JFlex generator. */
  // TODO Refacotring how logging is done so that we can verify the output easily.
  String expectedLogs() default "";
}
