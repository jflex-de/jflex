package jflex.util.scanner;

import com.google.common.io.CharSource;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public abstract class ScannerFactory<T> {
  /** Use the {@link #of} method to create a scanner. */
  private ScannerFactory() {}

  public T createScanner(File inputFile) throws FileNotFoundException {
    return createScanner(Files.newReader(inputFile, StandardCharsets.UTF_8));
  }

  public T createScanner(String content) throws IOException {
    return createScanner(CharSource.wrap(content).openStream());
  }

  protected abstract T createScanner(Reader reader);

  public static <S> ScannerFactory<S> of(Function<Reader, S> createFunction) {
    return new ScannerFactory<S>() {
      @Override
      protected S createScanner(Reader reader) {
        return createFunction.apply(reader);
      }
    };
  }
}
