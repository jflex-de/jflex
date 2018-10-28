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
package jflex.ucd_generator;

import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.FileOutputStream;
import jflex.testing.javac.PackageUtil;

public class UcdGenerator {

  public static final String PACKAGE_JFLEX_UNICODE = "jflex.core.unicode";

  /** Generates {@code UnicodeProperties_X_Y} from {@code //third_paty/unicode_ucd_X}. */
  public static void generate(ImmutableMap<String, ImmutableMap<UcdFileType, File>> versions)
      throws Exception {
    Package targetPackage = Package.getPackage(PACKAGE_JFLEX_UNICODE);
    Emitter emitter = new Emitter(targetPackage, versions);
    try (FileOutputStream output =
        new FileOutputStream(
            new File(PackageUtil.getPathForPackage(targetPackage), "UnicodeProperties.java"))) {
      emitter.emitUnicodeProperties(output);
    }
  }

  private UcdGenerator() {}
}
