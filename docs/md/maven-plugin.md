Maven plugin
------------
 
The plugin reads JFlex grammar specification files (`.jflex`) and
generates a corresponding Java parser 
(in `target/generated-source/jflex` by default).

### Usage

#### Minimal configuration

This configuration generates java code of a parser
for all grammar files (`*.jflex`, `*.jlex`, `*.lex`, `*.flex`) 
found in  `src/main/jflex/` and its sub-directories.

The name and package of the generated Java source code are the ones defined in
the grammar.
The generated Java source code is placed in `target/generated-source/jflex`,
in sub-directories following the Java convention on package names.


Update the `pom.xml` to add the plugin:

```
<project>
  <!-- ... -->
  <build>
    <plugins>
      <plugin>
        <groupId>de.jflex</groupId>
        <artifactId>jflex-maven-plugin</artifactId>
        <version>${project.version}</version>
        <executions>
          <execution>
            <goals>
              <goal>generate</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
    <!-- ... -->
  </build>
  <!-- ... -->
</project>
```

#### More complex configuration

This example generates the source for the two grammars 
`src/main/lex/preprocessor.jflex` and `/pub/postprocessor.jflex`,
as well as all grammar files found in  `src/main/jflex` (and its sub-directories).
The generated Java code is placed into `src/main/java` instead of
`target/generated-sources/jflex`.

```
      <plugin>
        <groupId>de.jflex</groupId>
        <artifactId>jflex-maven-plugin</artifactId>
        <version>${project.version}</version>
        <executions>
          <execution>
            <goals>
              <goal>generate</goal>
            </goals>
            <configuration>
              <outputDirectory>src/main/java</outputDirectory>
              <lexDefinitions>
                <lexDefinition>src/main/jflex</lexDefinition>
                <lexDefinition>src/main/lex/preprocessor.jflex</lexDefinition>
                <lexDefinition>/pub/postprocessor.jflex</lexDefinition>
              </lexDefinitions>
            </configuration>
          </execution>
        </executions>
      </plugin>
```

#### Even more complex configuration, using several executions

This generates the source for 

* all files found in 
`src/main/lex/`, using strict JLex compatibility.

* and all files found in 
 `src/main/jflex`, in verbose mode.

```
      <plugin>
        <groupId>de.jflex</groupId>
        <artifactId>jflex-maven-plugin</artifactId>
        <version>${project.version}</version>
        <executions>
          <execution>
            <id>strict jlex</id>
            <goals>
              <goal>generate</goal>
            </goals>
            <configuration>
              <lexDefinitions>
                <lexDefinition>src/main/lex</lexDefinition>
              </lexDefinitions>
              <jlex>true</jlex>
            </configuration>
          </execution>
          <execution>
            <id>jflex</id>
            <goals>
              <goal>generate</goal>
            </goals>
            <configuration>
              <lexDefinitions>
                <lexDefinition>src/main/jflex</lexDefinition>
              </lexDefinitions>
              <verbose>true</verbose>
            </configuration>
          </execution>
        </executions>
      </plugin>
```
      
  
### More information

* [jflex:generate](https://jflex-de.github.io/jflex-web/jflex-maven-plugin/generate-mojo.html)
  for more information about the configuration options of the
  jflex-maven-plugin.
* [POM reference guide on plugins](http://maven.apache.org/pom.html#Plugins)
  for more information about using plugins in a project.

