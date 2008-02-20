 ------
 Usage 
 ------
 Gerwin Klein <lsf37@users.sf.net>
 Régis Décamps <decamps@users.sf.net>
 ------
 November 2, 2007
 ------
 

About this project
	
	This sample project parses a grammar with JFlex.
	
	The project management is done with Maven.
	
	The project contains a simple grammar for the "Toy programming language".
	It is the example from the JLex web site with some small
	modifications, to make it a bit more readable.
	It does nothing really useful, because there is no parser for
	the toy programming language. It's just a demonstration how a
	small simple scanner looks like.
	
	
* JFlex
	
	JFlex is a parser generator. Given a grammar,  JFlex generate
	Java (TM) code to parse documents that follow this grammar.
	
* Maven

	Maven is a project management framework.
	The project is described in as a POM (project object model, 
	stored into <<<pom.xml>>>).
	This document isn't intended to be a tutorial on the use of Maven 2,
	you should consult the {{{http://maven.apache.org/}Maven web site}}.
	
	The integration of JFlex and Maven is done with 
	{{{http://jflex.sourceforge.net/maven-flex-plugin/}maven-flex-plugin}}.
	

Usage
	 
* mvn generate-sources

	The maven-jflex-plugin will read the grammar 
	<<<src/main/jflex/simple.jflex>>>
	and generate a Java scanner <<<Yylex.java>>>
	in <<<target/generated-sources/flex>>>

	This is defined by the following section
	
+---------
  <build>
    <plugins>
      <plugin>
        <groupId>de.jflex.maven.plugin</groupId>
        <artifactId>maven-jflex-plugin</artifactId>
        <version>0.2</version>
        <executions>
          <execution>
            <goals>
              <goal>generate</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
+------------

	By default, the maven-jflex-plugin generates a scanner/parser for every file
	in <<<src/main/jflex/>>>.


* mvn test

	This goal test the generated scanner (if required, the lexer will be 
	generated and all Java classes will be compiled)
	by running all tests in <<<src/test/java>>>.
	
	There is only one test in <<<src/test/java/YylexTest.java>>>.
	In this test, 
	the scanner is run with the input file <<<src/test/resources/test.txt>>>.
	
	By default, the scanner outputs debugging information about each 
	returned token to <<<System.out>>> until the end of file is reached, 
	or an error occurs.
	But in the test, the output is redirected into <<<target/output.actual>>>.
	
	The test is successful if every line match
	with <<<src/test/resources/output.good>>>,
	which is the expected scanner debugging information.
	

References

    * {{{http://jflex.sourceforge.net/maven-flex-plugin/}maven-flex-plugin}}
    
    * {{{http://maven.apache.org/}Maven web site}}.
