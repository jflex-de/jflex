/*
 * Copyright (C) 2018-2019 Google, LLC.
 *
 * License: https://opensource.org/licenses/BSD-3-Clause
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior written permission.
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
package jflex.testing;

import static com.google.common.truth.Truth.assertThat;
import static jflex.testing.assertion.MoreAsserts.assertThrows;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import jflex.testing.diff.DiffOutputStream;
import org.junit.Test;

/** Test for {@link DiffOutputStreamTest}. */
public class DiffOutputStreamTest {

  @Test
  public void testOneLine_equal() throws Exception {
    String in = "Hello world!\n";
    @SuppressWarnings("RedundantStringConstructorCall")
    String out = new String(in);
    DiffOutputStream differ = diff(in, out);
    assertThat(differ.isCompleted()).isTrue();
  }

  @Test
  public void testOneLine_differ_inHasMore() throws Exception {
    String in = "Hello world!\n";
    String out = "Hello\n";
    assertThrows(
        DiffOutputStream.class + " throws an exception when the content differs",
        AssertionError.class,
        () -> diff(in, out));
  }

  @Test
  public void testOneLine_differs_outHasMore() throws Exception {
    String in = "Hello!\n";
    String out = "Hello world!\n";
    assertThrows(
        DiffOutputStream.class + " throws an exception when the content differs",
        AssertionError.class,
        () -> diff(in, out));
  }

  private static DiffOutputStream diff(String in, String out) throws IOException {
    DiffOutputStream diffStream = new DiffOutputStream(new StringReader(in));
    try {
      diffStream.write(out.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException impossible) {
    }
    diffStream.flush();
    return diffStream;
  }
}
