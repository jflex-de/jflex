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
import org.apache.velocity.runtime.parser.node.SimpleNode;
import velocity.Velocity;

public class Emitter {

  public static final String UNICODE_PROPERTIES_TEMPLATE =
      Emitter.class.getPackage().getName().replace('.', '/') + "/UnicodeProperties.java.vm";

  Emitter(File skeleton, ImmutableMap<String, ImmutableMap<UcdFileType, File>> versions) {}

  void emitUnicodeProperties() throws Exception {
    SimpleNode template =
        Velocity.parsedTemplateForResource(UNICODE_PROPERTIES_TEMPLATE, "emitUnicodeProperties");

    //    UnicodePropertiesSkeleton skeleton = new UnicodePropertiesSkeleton(out);
    //
    //    skeleton.emitNext(); // Header
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
}
