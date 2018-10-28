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

import jflex.velocity.TemplateVars;

// the fields in this class are read via reflection by Velocity
@SuppressWarnings({"unused", "WeakerAccess"})
public class UnicodePropertiesVars extends TemplateVars {
  public String packageName;
  public String classComment;
  public String versionsAsString;
  public String latestVersion;
  public Iterable<String> versions;
  public UcdVersions ucdVersions;
}
