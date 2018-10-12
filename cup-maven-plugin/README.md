# CUP Maven plugin

This is a plugin to invoke cup from Maven.

Version 1.0 of the plugin uses CUP 11b.

## Usage

By default, this plugin runs

```
cup -destdir target/generated-sources/cup -parser parser -symbols sym src/main/cup/*.cup
```

```xml
      <plugin>
        <groupId>de.jflex</groupId>
        <artifactId>cup-maven-plugin</artifactId>
        <version>1.0</version>
        <executions>
          <execution>
            <goals>
              <goal>generate</goal>
            </goals>
            <configuration>
            </configuration>
          </execution>
        </executions>
      </plugin>
```

## Example

Please see [cup/sample-project](/jflex-de/jflex/cup/sample-project/)
