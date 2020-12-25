/*
 * Copyright (C) 2019-2020 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 *    and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 *    conditions and the following disclaimer in the documentation and/or other materials provided with
 *    the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 *    endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
