The generated class {#GeneratedJavaCode}
===================

JFlex generates exactly one file containing one class from the
specification (unless you have declared another class in the first
specification section).

The generated class contains (among other things) the DFA tables, an
input buffer, the lexical states of the specification, a constructor,
and the scanning method with the user supplied actions.

Name of the generated class {#GeneratedClassName}
---------------------------

The name of the class is by default `Yylex`. The name is customisable with
the `%class` directive. The input buffer of the lexer is connected with
external input through the `java.io.Reader` object which is passed to the
lexer in the generated constructor. If you provide your own constructor for
the lexer, you should always chain-call the generated one to initialise the
input buffer. The input buffer should not be accessed directly, but only
through the advertised API (see also [Scanner Methods](#ScannerMethods)). Its
internal implementation may change between releases or skeleton files without
notice.

Scanning method {#ScanningMethod}
---------------

The main interface to the outside world is the generated scanning method
(default name `yylex`, default return type `Yytoken`). Most of its aspects
are customisable (name, return type, declared exceptions etc.). If it is
called, it will consume input until one of the expressions in the
specification is matched or an error occurs. If an expression is matched, the
corresponding action is executed. It may return a value of the specified
return type (in which case the scanning method returns with this value), or,
if it does not return a value, the scanner resumes consuming input until the
next expression is matched. If the end of file is reached, the scanner
executes the `EOF` action, and (also upon each further call to the scanning
method) returns the specified `EOF` value.


Scanner methods and fields accessible in actions (API) {#ScannerMethods}
------------------------------------------------------

Generated methods and member fields in JFlex scanners are prefixed with `yy`
to indicate that they are generated and to avoid name conflicts with user
code copied into the class. Since user code is part of the same class, JFlex
has no language means like the `private` modifier to indicate which members
and methods are internal and which ones belong to the API. Instead, JFlex
follows a naming convention: everything starting with `zz`, such as
`zzStartRead`, is internal and subject to change without notice between JFlex
releases. Methods and members of the generated class that do not have a `zz`
prefix, such as `yycharat`, belong to the API that the scanner class provides
to users in action code of the specification. They will remain stable and
supported between JFlex releases as long as possible.

Currently, the API consists of the following methods and member fields:

-   `String yytext()`

    returns the matched input text region

-   `int yylength()`

    returns the length of the matched input text region as number of Java `chars`
    (as opposed to Unicode code points). It is equivalent to
    `yytext().length()`, but faster since it does not require a `String` object
    to be created.

-   `char yycharat(int pos)`

    returns the Java `char` at position `pos` from the matched text. It is
    equivalent to `yytext().charAt(pos)`, but faster. `pos` must be a
    value from `0` to `yylength()-1`.

-   `void yyclose()`

    closes the input stream. All subsequent calls to the scanning method
    will return the end of file value.

-   `void yyreset(java.io.Reader reader)`

    closes the current input stream, and resets the scanner to read from
    a new Reader. All internal variables are reset, the old Reader
    *cannot* be reused (content of the internal buffer is discarded and
    lost). The lexical state is set to `YY_INITIAL`. The `%{init` code
    is *not* included in `yyreset`, because it is assumed to run in the
    context of a constructor, not a normal method. If `%{init` does need
    to be repeated, consider constructing a new lexer object instead, or
    calling a custom function that performs any additional user-level
    state reset.

-   `void yypushStream(java.io.Reader reader)`

    Stores the current input stream on a stack, and reads from a new
    stream. Lexical state, line, char, and column counting remain
    untouched. The current input stream can be restored with
    `yypopStream` (usually in an `<<EOF>>` action).

    A typical example for this are include files in style of the C
    pre-processor. The corresponding JFlex specification could look
    like this:

        "#include" {FILE}  { yypushStream(new FileReader(getFile(yytext()))); }
        ...
        <<EOF>>            { if (yymoreStreams()) yypopStream(); else return EOF; }

    This method is only available in the skeleton file
    `skeleton.nested`. You can find it in the `src` directory of the
    JFlex distribution.

-   `void yypopStream()`

    Closes the current input stream and continues to read from the one
    on top of the stream stack.

    This method is only available in the skeleton file
    `skeleton.nested`. You can find it in the `src` directory of the
    JFlex distribution.

-   `boolean yymoreStreams()`

    Returns true iff there are still streams for `yypopStream` left to
    read from on the stream stack.

    This method is only available in the skeleton file
    `skeleton.nested`. You can find it in the `src` directory of the
    JFlex distribution.

-   `int yystate()`

    returns the current lexical state of the scanner.

-   `void yybegin(int lexicalState)`

    enters the lexical state `lexicalState`

-   `void yypushback(int number)`

    pushes `number` Java `char`s (as opposed to Unicode code points)
    of the matched text back into the input
    stream. They will be read again in the next call of the scanning
    method. The number of chars to be read again must not be
    greater than the length of the matched text. The pushed back
    characters will not be included in `yylength()` and `yytext()`. Note
    that in Java strings are unchangeable, i.e. an action code like

            String matched = yytext();
            yypushback(1);
            return matched;

    will return the whole matched text, while

            yypushback(1);
            return yytext();

    will return the matched text minus the last character.

    Note that supplementary Unicode characters (i.e., those above the
    Basic Multilingual Plane) are represented in Java Strings by paired
    Unicode surrogate characters, and as a result expressions such as
    `[^]` (any character) and `\p{...}` (characters having a Unicode
    property) can match more than one `char`.
    
-   `yyatEOF()`
    
    returns whether the scanner has reached the end of the reader it reads
    from, which often corresponds to the end of file.

-   `int yyline`

    contains the current line of input (starting with 0, only active
    with the `lineCounting` directive)

-   `long yychar`

    contains the current character count in the input (starting with 0,
    only active with the `charCounting` directive)

-   `int yycolumn`

    contains the current column of the current line (starting with 0,
    only active with the `columnCounting` directive)

