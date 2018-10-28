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

import com.google.common.base.Joiner;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import jflex.testing.javac.PackageUtil;
import jflex.velocity.Velocity;

public class Emitter {

  private static final String UNICODE_PROPERTIES_TEMPLATE =
      PackageUtil.getPathForClass(Emitter.class) + "/UnicodeProperties.java.vm";

  private final String targetPackage;
  private final UcdVersions versions;

  Emitter(String targetPackage, UcdVersions versions) {
    this.targetPackage = targetPackage;
    this.versions = versions;
  }

  void emitUnicodeProperties(OutputStream output) throws Exception {
    System.out.println("Rendering " + UNICODE_PROPERTIES_TEMPLATE);

    UnicodePropertiesVars unicodePropertiesVars = createUnicodePropertiesVars();
    try (Writer writer = new BufferedWriter(new OutputStreamWriter(output))) {
      Velocity.render(readResource(), "UnicodeProperties", unicodePropertiesVars, writer);
    }

    //    emitClassComment();
    //    // Class declaration and unicode versions static field declaration
    //    skeleton.emitNext();
    //    emitUnicodeVersionsString();
    //    // default unicode version static field declaration
    //    skeleton.emitNext(builder);
    //    emitDefaultUnicodeVersion();
    //    // static vars and fixed method definitions, part 1
    //    skeleton.emitNext();
    //    emitInitBody();
    //    skeleton.emitNext(); // Fixed method definitions, part 2; etc.
    //    writeOutputFile();
  }

  private UnicodePropertiesVars createUnicodePropertiesVars() {
    UnicodePropertiesVars unicodePropertiesVars = new UnicodePropertiesVars();
    unicodePropertiesVars.packageName = targetPackage;
    unicodePropertiesVars.classComment = createClassComment();
    unicodePropertiesVars.versionsAsString = Joiner.on(", ").join(versions.versions());
    unicodePropertiesVars.latestVersion = versions.getLastVersion();
    unicodePropertiesVars.versions = versions.versions();
    unicodePropertiesVars.ucdVersions = versions;
    return unicodePropertiesVars;
  }

  // TODO(regisd) Make it code comment. This is not proper javadoc.
  private static String createClassComment() {
    return "/**\n"
        + " * This class was automatically generated by jflex-unicode-maven-plugin based on data files\n"
        + " * downloaded from unicode.org.\n"
        + " *\n"
        + " * @author JFlex contributors.\n"
        + " */";
  }

  private InputStreamReader readResource() {
    return new InputStreamReader(
        getClass().getClassLoader().getResourceAsStream(UNICODE_PROPERTIES_TEMPLATE));
  }
}
