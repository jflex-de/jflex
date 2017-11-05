Maven plugin
------------
 
The plugin reads JFlex grammar definition files (`.jflex`) and
generates a corresponding Java parser 
(in `target/generated-source/jflex` by default).

### Usage

#### Minimal configuration

This configuration generates java code of a parser
for all grammar files (`*.jflex`, `*.jlex`, `*.lex`, `*.flex`) 
found in  `src/main/jflex/`
and its sub-directories.

The name and package of the generated Java source code are the ones defined in the grammar.
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
        <version>1.6.1</version>
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
        <version>1.6.0</version>
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
        <version>1.6.0</version>
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
      
More documentation on the configuration options can be found 
in the description for the mojo:
```
mvn help:describe  -DgroupId=de.jflex -DartifactId=maven-jflex-plugin -Ddetail
```
  
More information in the [POM reference guide on plugins](http://maven.apache.org/pom.html#Plugins).


### Versions

Which version of the plugin is best for you?

  * jflex-maven-plugin-$VERSION depends on $VERSION
    and requires Java 7 when you `mvn jflex:generate`

  * jflex-maven-plugin-1.6.1 depends on JFlex 1.6.1
    and requires Java 5 when you `mvn jflex:generate`

  * jflex-maven-plugin-1.5.0 depends on JFlex 1.5.0
    and requires Java 5 when you `mvn jflex:generate`

  * maven-jflex-plugin-1.4.3-r1 depends on JFlex 1.4.3
    and requires Java 1.3 when you `mvn jflex:generate`

