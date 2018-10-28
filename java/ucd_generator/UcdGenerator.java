/*
 * Copyright (C) 2018 Google, LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ucd_generator;

import com.google.common.collect.ImmutableMap;
import java.io.File;

public class UcdGenerator {

  /** Generates {@code UnicodeProperties_X_Y} from {@code //third_paty/unicode_ucd_X}. */
  public static void generate(
      ImmutableMap<String, ImmutableMap<ucd_generator.UcdFileType, File>> versions)
      throws Exception {
    Emitter emitter = new Emitter(new File("java/jflex/core/unicode"), versions);
    emitter.emitUnicodeProperties();
  }

  private UcdGenerator() {}
}
