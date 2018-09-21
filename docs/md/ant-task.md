JFlex Ant Task
--------------

JFlex can easily be integrated with the [Ant](http://ant.apache.org/)
build tool. To use JFlex with Ant, simply copy the `lib/jflex-$VERSION.jar`
file to the `$ANT_HOME/lib/` directory or explicitly set the path to
`lib/jflex-$VERSION.jar` in the task definition (see example below).

The JFlex Ant Task invokes JFlex on a grammar file.

To use the JFlex task, place the following line in the Ant build file:

    <taskdef classname="jflex.anttask.JFlexTask" name="jflex" />

Or, setting the path to the JFlex jar explicitly:

    <taskdef classname="jflex.anttask.JFlexTask" name="jflex"
             classpath="path-to-jflex.jar" />

The JFlex task requires the `file` attribute to be set to the source
grammar file (`*.flex`). Unless the target directory is specified with
the `destdir` option, the generated class will be saved to the same
directory where the grammar file resides. Like `javac`, the JFlex task
creates subdirectories in `destdir` according to the generated class
package.

This task only invokes JFlex if the grammar file is newer than the
generated files.

### Parameters

The following attributes are available for invoking the JFlex task.

-  `file="file"`\
    The grammar file to process. This attribute is required.

-  `destdir="dir"`\
    The directory to write the generated files to. If not set, the files are
    written to the directory containing the grammar file. Note that unlike
    JFlex's `-d` command line option, `destdir` causes the generated file to
    be written to `{destdir}/`**`{packagename}`**. This behaviour is similar
    to `javac -d dir.

-  `outdir="dir"`\   
    The directory to write the generated files to. If not set, the files are
    written to the directory containing the grammar file. This options works
    exactly like JFlex's `-d` command line option, it causes the output file
    to be written to `dir` regardless of the package name.

-  `verbose` (default `"off"`)\
    Display generation process messages.                     

-  `encoding` (if unset uses the JVM default encoding)\
    The character encoding to use when reading lexer specifications and writing java files.

-  `dump` (default `"off"`)\           
    Dump character classes, NFA and DFA tables.                           

-  `time` or `timeStatistics` (default `"off"`)\
    Display generation time statistics.

-  `nomin` or `skipMinimization` (default `"off"`)\
    Skip DFA minimisation step.                  

-  `skel="file"` or `skeleton="file"`\
    Use external skeleton file.                        

-  `dot` or `generateDot` (default `"off"`)\
    Write graphviz `.dot` files for the generated automata.

-  `nobak` (default `"off"`)\
    Do not make a backup if the generated file exists.

-   `jlex` (default `"off"`)\
    Use JLex compatibility mode.

-  `legacydot` (default `"off"`)\
    The dot `.` meta-character matches `[^\n]` instead of `[^\n\r\u000B\u000C\u0085\u2028\u202 9]`

-  `unusedwarning` (default `"true"`)\
    Warn about unused macro definitions in the lexer specification.


### Example

After the task definition, the `<jflex ..>` task is available in Ant. For
example:

     <jflex
         file="src/parser/Parser.flex"
         destdir="build/generated/"
     />

JFlex generates the scanner for `src/parser/Scanner.flex` and
saves the result to `build/generated/parser/`, providing `Scanner.flex`
is declared to be in package `parser`.

     <jflex
         file="src/parser/Scanner.flex"
         destdir="build/generated/"
     />
     <javac
         srcdir="build/generated/"
         destdir="build/classes/"
     />

The same as above plus compile generated classes to `build/classes`

