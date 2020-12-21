package de.jflex.util.scanner;

import com.google.common.io.CharSource;
import com.google.common.io.Files;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 * Wraps the given content in a {@link BufferedReader} and constructs a scanner of type {@code T}.
 */
public class ScannerFactory<T> {

  private final Function<Reader, T> defaultConstructor;

  /** Use the {@link #of} method to create a scanner factory. */
  private ScannerFactory(Function<Reader, T> defaultConstructor) {
    this.defaultConstructor = defaultConstructor;
  }

  public T createScannerForFile(File inputFile) throws FileNotFoundException {
    return defaultConstructor.apply(Files.newReader(inputFile, StandardCharsets.UTF_8));
  }

  public T createScannerWithContent(String content) throws IOException {
    return defaultConstructor.apply(CharSource.wrap(content).openStream());
  }

  public T createForReader(Reader reader) {
    return defaultConstructor.apply(wrapInBufferedReader(reader));
  }

  private static BufferedReader wrapInBufferedReader(Reader reader) {
    if (reader instanceof BufferedReader) {
      return (BufferedReader) reader;
    } else {
      return new BufferedReader(reader);
    }
  }

  /**
   * Creates a ScannerFactory for the given scanner.
   *
   * @param constructorRef Reference to the default constructor of {@code <S>}.
   * @param <S> The type of the scanner.
   * @return A ScannerFactory that helps to create {@code <S>} scanners.
   */
  public static <S> ScannerFactory<S> of(Function<Reader, S> constructorRef) {
    return new ScannerFactory<>(constructorRef);
  }
}
