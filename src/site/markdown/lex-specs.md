Lexical Specifications {#Specifications}
======================

As shown above, a lexical specification file for JFlex consists of three
parts divided by a single line starting with `%%`:

    UserCode
    %%
    Options and declarations
    %%
    Lexical rules

In all parts of the specification comments of the form `/* comment text */`
and Java-style end-of-line comments starting with `//` are permitted. JFlex
comments do nest - so the number of `/*` and `*/` should be balanced.

User code
---------

The first part contains user code that is copied verbatim to the beginning of
the generated source file before the scanner class declaration. As shown in
the example spec, this is the place to put `package` declarations and
`import` statements. It is possible, but not considered good Java style to
put helper classes, such as token classes, into this section; they are
usually better declared in their own `.java` files.

Options and declarations
------------------------

The second part of the lexical specification contains options and directives
to customise the generated lexer, declarations of 
[lexical states](#StateDecl) and [macro definitions](#MacroDefs).

Each JFlex directive must sit at the beginning of a line and starts with the
`%` character. Directives that have one or more parameters are described as
follows.

    %class "classname"

means that you start a line with `%class` followed by a space followed by the
name of the class for the generated scanner (the double quotes are *not* to
be entered, see also the [example specification](#Example)).

### Class options and user class code {#ClassOptions}

These options regard name, constructor, API, and related parts of the
generated scanner class.

-   `%class "classname"`

    Tells JFlex to give the generated class the name `classname` and
    to write the generated code to a file `classname.java`. If the
    `-d <directory>` command line option is not used, the code will be
    written to the directory where the specification file resides. If no
    `%class` directive is present in the specification, the generated
    class will get the name `Yylex` and will be written to a file
    `Yylex.java`. There should be only one `%class` directive in a
    specification.

-   `%implements "interface 1"[, "interface 2", ..]`

    Makes the generated lexer class implement the specified interfaces. If
    more than one `%implements` directive is present, all specified
    interfaces will be implemented.

-   `%extends "classname"`

    Makes the generated class a subclass of the class `classname`.
    There should be only one `%extends` directive in a specification.

-   `%public`

    Makes the generated class public (the class is only accessible in
    its own package by default).

-   `%final`

    Makes the generated class final.

-   `%abstract`

    Makes the generated class abstract.

-   `%apiprivate`

    Makes all generated methods and fields of the class private.
    Exceptions are the constructor, user code in the specification, and,
    if `%cup` is present, the method `next_token`. All occurrences of
    ` public ` (one space character before and after `public`) in the
    skeleton file are replaced by ` private ` (even if a user-specified
    skeleton is used). Access to the generated class is expected to be
    mediated by user class code (see next switch).

-   `%{`\
    `...`\
    `%}`

    The code enclosed in `%{` and `%}` is copied verbatim into the
    generated class. Here you can define your own member variables and
    functions in the generated scanner. Like all options, both `%{` and
    `%}` must start a line in the specification. If more than one class
    code directive `%{...%}` is present, the code is concatenated in
    order of appearance in the specification.

-   `%init{`\
    `...`\
    `%init}`

    The code enclosed in `%init{` and `%init}` is copied verbatim into
    the constructor of the generated class. Here, member variables
    declared in the `%{...%}` directive can be initialised. If more than
    one initialiser option is present, the code is concatenated in order
    of appearance in the specification.

-   `%initthrow{`\
    `"exception1"[, "exception2", ...]`\
    `%initthrow}`

    or (on a single line) just

    `%initthrow "exception1" [, "exception2", ...]`

    Causes the specified exceptions to be declared in the `throws`
    clause of the constructor. If more than one `%initthrow{` `...`
    `%initthrow}` directive is present in the specification, all
    specified exceptions will be declared.

-   `%ctorarg "type" "ident"`

    Adds the specified argument to the constructors of the generated
    scanner. If more than one such directive is present, the arguments
    are added in order of occurrence in the specification. Note that
    this option conflicts with the `%standalone` and `%debug`
    directives, because there is no sensible default that can be created
    automatically for such parameters in the generated `main` methods.
    JFlex will warn in this case and generate an additional default
    constructor without these parameters and without user init code
    (which might potentially refer to the parameters).

-   `%scanerror "exception"`

    Causes the generated scanner to throw an instance of the specified
    exception in case of an internal error (default is
    `java.lang.Error`). Note that this exception is only for internal
    scanner errors. With usual specifications it should never occur
    (i.e. if there is an error fallback rule in the specification and
    only the documented scanner API is used).

-   `%buffer "size"`

    Set the initial size of the scan buffer to the specified value
    (decimal, in bytes). The default value is 16384.

-   `%include "filename"`

    Replaces the `%include` verbatim by the specified file.


### Scanning method

This section shows how the scanning method can be customised. You can
redefine the name and return type of the method and it is possible to
declare exceptions that may be thrown in one of the actions of the
specification. If no return type is specified, the scanning method will
be declared as returning values of class `Yytoken`.

-   `%function "name"`

    Causes the scanning method to get the specified name. If no `%function`
    directive is present in the specification, the scanning method gets the
    name `yylex`. This directive overrides settings of the `%cup` switch. The
    default name of the scanning method with the `%cup` switch is
    `next_token`. Overriding this name might lead to the generated scanner
    being implicitly declared as `abstract`, because it does not provide the
    method `next_token` of the interface `java_cup.runtime.Scanner`. It is of
    course possible to provide a dummy implementation of that method in the
    class code section if you still want to override the function name.

-   `%integer`\
    `%int`

    Both cause the scanning method to be declared as returning Java type
    `int`. Actions in the specification can then return `int` values as
    tokens. The default end of file value under this setting is `YYEOF`,
    which is a `public static final int` member of the generated class.

-   `%intwrap`

    Causes the scanning method to be declared as of the Java wrapper
    type `Integer`. Actions in the specification can then return
    `Integer` values as tokens. The default end of file value under this
    setting is `null`.

-   `%type "typename"`

    Causes the scanning method to be declared as returning values of the
    specified type. Actions in the specification can then return values of
    `typename` as tokens. The default end of file value under this setting is
    `null`. If `typename` is not a subclass of `java.lang.Object`, you should
    specify another end of file value using the `%eofval{` `...` `%eofval}`
    directive or the [`<<EOF>>` rule](#Grammar). The `%type` directive
    overrides settings of the `%cup` switch.

-   `%yylexthrow{`\
    `"exception1" [, "exception2", ... ]`\
    `%yylexthrow}`

    or, on a single line, just

    `%yylexthrow "exception1" [, "exception2", ...]`

    The exceptions listed inside `%yylexthrow{` `...` `%yylexthrow}`
    will be declared in the throws clause of the scanning method. If
    there is more than one `%yylexthrow{` `...` `%yylexthrow}` clause in
    the specification, all specified exceptions will be declared.


### The end of file

There is always a default value that the scanning method will return
when the end of file has been reached. You may however define a specific
value to return and a specific piece of code that should be executed
when the end of file is reached.

The default end of file value depends on the return type of the scanning
method:

-   For `%integer`, the scanning method will return the value `YYEOF`,
    which is a `public static final int` member of the generated class.

-   For `%intwrap`,

-   for no specified type at all, or

-   for a user defined type, declared using `%type`, the value is `null`.

-   In CUP compatibility mode, using `%cup`, the value is

    `new java_cup.runtime.Symbol(sym.EOF)`

User values and code to be executed at the end of file can be defined using
these directives:

-   `%eofval{`\
    `...`\
    `%eofval}`

    The code included in `%eofval{` `...` `%eofval}` will be copied verbatim
    into the scanning method and will be executed _each time_ the end of file
    is reached (more than once is possible when the scanning method is called
    again after the end of file has been reached). The code should return the
    value that indicates the end of file to the parser. There should be only
    one `%eofval{` `...` `%eofval}` clause in the specification. The
    `%eofval{ ... %eofval}` directive overrides settings of the `%cup` switch
    and `%byaccj` switch. There is also an alternative, more readable way to
    specify the end of file value using the [`<<EOF>>` rule](#Grammar).

-   `%eof{`\
    `...`\
    `%eof}`

    The code included in `%{eof ... %eof}` will be executed exactly once,
    when the end of file is reached. The code is included inside a method
    `void yy_do_eof()` and should not return any value (use
    `%eofval{...%eofval}` or `<<EOF>>` for this purpose). If more than one
    end of file code directive is present, the code will be concatenated in
    order of appearance in the specification.

-   `%eofthrow{`\
    `"exception1" [,"exception2", ... ]`\
    `%eofthrow}`

    or, on a single line:

    `%eofthrow "exception1" [, "exception2", ...]`

    The exceptions listed inside `%eofthrow{...%eofthrow}` will be declared
    in the throws clause of the method `yy_do_eof()`. If there is more than
    one `%eofthrow{...%eofthrow}` clause in the specification, all specified
    exceptions will be declared.

-   `%eofclose`

    Causes JFlex to close the input stream at the end of file. The code
    `yyclose()` is appended to the method `yy_do_eof()` (together with the
    code specified in `%eof{...%eof}`) and the exception
    `java.io.IOException` is declared in the throws clause of this method
    (together with those of `%eofthrow{...%eofthrow}`)

-   `%eofclose false`

    Turns the effect of `%eofclose` off again (e.g. in case closing of input
    stream is not wanted after `%cup`).


### Standalone scanners

-   `%debug`

    Creates a main function in the generated class that expects the name
    of an input file on the command line and then runs the scanner on
    this input file by printing information about each returned token to
    the Java console until the end of file is reached. The information
    includes: line number (if line counting is enabled), column (if
    column counting is enabled), the matched text, and the executed
    action (with line number in the specification).

-   `%standalone`

    Creates a main function in the generated class that expects the name of
    an input file on the command line and then runs the scanner on this input
    file. The values returned by the scanner are ignored, but any unmatched
    text is printed to the Java console instead. To avoid having to use an
    extra token class, the scanning method will be declared as having default
    type `int`, not `YYtoken` (if there isn’t any other type explicitly
    specified). This is in most cases irrelevant, but could be useful to know
    when making another scanner standalone for some purpose. You should
    consider using the `%debug` directive, if you just want to be able to run
    the scanner without a parser attached for testing etc.


### CUP compatibility

You may also want to read the [CUP section](#CUPWork) if you are interested
in how to interface your generated scanner with CUP.

-   `%cup`

    The `%cup` directive enables CUP compatibility mode and is equivalent to
    the following set of directives:

        %implements java_cup.runtime.Scanner
        %function next_token
        %type java_cup.runtime.Symbol
        %eofval{
          return new java_cup.runtime.Symbol(<CUPSYM>.EOF);
        %eofval}
        %eofclose

    The value of `<CUPSYM>` defaults to `sym` and can be changed with
    the `%cupsym` directive. In JLex compatibility mode (`--jlex` switch
    on the command line), `%eofclose` will not be turned on.

-   `%cup2`

    The `%cup2` directive is similar to CUP mode, just for the CUP2
    generator from TU Munich at <http://www2.in.tum.de/cup2>. It does
    the following:

    -   adds CUP2 package import declarations
    -   implements the CUP2 scanner interface
    -   switches on line and column count
    -   sets the scanner function to `readNextTerminal`
    -   sets the token type to `ScannerToken<? extends Object>`
    -   returns the special CUP2 EOF token at end of file
    -   switches on unicode

-   `%cupsym "classname"`

    Customises the name of the CUP generated class/interface containing
    the names of terminal tokens. Default is `sym`. The directive should
    not be used after `%cup`, only before.
    <!-- FIXME: check if this can be relaxed -->

-   `%cupdebug`

    Creates a main function in the generated class that expects the name
    of an input file on the command line and then runs the scanner on
    this input file. Prints line, column, matched text, and CUP symbol
    name for each returned token to standard out.



### BYacc/J compatibility

You may also want to read [JFlex and BYacc/J](#BYaccJ) if you are
interested in how to interface your generated scanner with Byacc/J.

-   `%byacc`

    The `%byacc` directive enables BYacc/J compatibility mode and is
    equivalent to the following set of directives:

        %integer
        %eofval{
          return 0;
        %eofval}
        %eofclose


### Input Character sets

-   `%7bit`

    Causes the generated scanner to use an 7 bit input character set
    (character codes 0-127). If an input character with a code greater than
    127 is encountered in an input at runtime, the scanner will throw an
    `ArrayIndexOutofBoundsException`. Not only because of this, you should
    consider using the `%unicode` directive. See also
    [Encodings](#sec:encodings) for information about character encodings.
    This is the default in JLex compatibility mode.

-   `%full`\
    `%8bit`

    Both options cause the generated scanner to use an 8 bit input character
    set (character codes 0-255). If an input character with a code greater
    than 255 is encountered in an input at runtime, the scanner will throw an
    `ArrayIndexOutofBoundsException`. Note that even if your platform uses
    only one byte per character, the Unicode value of a character may still
    be greater than 255. If you are scanning text files, you should consider
    using the `%unicode` directive. See also section
    [Econdings](#sec:encodings) for more information about character
    encodings.

-   `%unicode`\
    `%16bit`

    Both options cause the generated scanner to use the full Unicode input
    character set, including supplementary code points: 0-0x10FFFF.
    `%unicode` does not mean that the scanner will read two bytes at a time.
    What is read and what constitutes a character depends on the runtime
    platform. See also section [Encodings](#sec:encodings) for more
    information about character encodings. This is the default unless the
    JLex compatibility mode is used (command line option `--jlex`).

-   `%caseless`\
    `%ignorecase`

    This option causes JFlex to handle all characters and strings in the
    specification as if they were specified in both uppercase and
    lowercase form. This enables an easy way to specify a scanner for a
    language with case insensitive keywords. The string `break` in a
    specification is for instance handled like the expression
    `[bB][rR][eE][aA][kK]`. The `%caseless` option does not change the
    matched text and does not affect character classes. So `[a]` still
    only matches the character `a` and not `A`. Which letters are
    uppercase and which lowercase letters, is defined by the Unicode
    standard. In JLex compatibility mode (`--jlex` switch on the command
    line), `%caseless` and `%ignorecase` also affect character classes.


### Line, character and column counting

-   `%char`

    Turns character counting on. The `int` member variable `yychar`
    contains the number of characters (starting with 0) from the
    beginning of input to the beginning of the current token.

-   `%line`

    Turns line counting on. The `int` member variable `yyline` contains
    the number of lines (starting with 0) from the beginning of input to
    the beginning of the current token.

-   `%column`

    Turns column counting on. The `int` member variable `yycolumn`
    contains the number of characters (starting with 0) from the
    beginning of the current line to the beginning of the current token.


### Obsolete JLex options

-   `%notunix`

    This JLex option is obsolete in JFlex but still recognised as valid
    directive. It used to switch between Windows and Unix kind of line
    terminators (`\r\n` and `\n`) for the `$` operator in regular
    expressions. JFlex always recognises both styles of platform
    dependent line terminators.

-   `%yyeof`

    This JLex option is obsolete in JFlex but still recognised as valid
    directive. In JLex it declares a public member constant `YYEOF`.
    JFlex declares it in any case.


### State declarations {#StateDecl}

State declarations have the following form:

`%s[tate] "state identifier" [, "state identifier", ... ]` for inclusive or\
`%x[state] "state identifier" [, "state identifier", ... ]` for exclusive states

There may be more than one line of state declarations, each starting with
`%state` or `%xstate`. State identifiers are letters followed by a sequence
of letters, digits or underscores. State identifiers can be separated by
white-space or comma.

The sequence

    %state STATE1
    %xstate STATE3, XYZ, STATE_10
    %state ABC STATE5

declares the set of identifiers `STATE1, STATE3, XYZ, STATE_10, ABC, STATE5`
as lexical states, `STATE1`, `ABC`, `STATE5` as inclusive, and `STATE3`,
`XYZ`, `STATE_10` as exclusive. See also [How the Input is
Matched](#HowMatched) on the way lexical states influence how the input is
matched.

### Macro definitions {#MacroDefs}

A macro definition has the form

    macroidentifier = regular expression

That means, a macro definition is a macro identifier (letter followed by a
sequence of letters, digits or underscores), that can later be used to
reference the macro, followed by optional white-space, followed by an `=`,
followed by optional white-space, followed by a regular expression (see
[Lexical Rules](#LexRules) for more information about the regular expression
syntax).

The regular expression on the right hand side must be well formed and
must not contain the `^`, `/` or `$` operators. *Differently to JLex,
macros are not just pieces of text that are expanded by copying* - they
are parsed and must be well formed.

**This is a feature.** It eliminates some very hard to find bugs in lexical
specifications (such like not having parentheses around more complicated
macros - which is not necessary with JFlex). See [Porting from
JLex](#Porting) for more details on the problems of JLex style macros.

Since it is allowed to have macro usages in macro definitions, it is possible
to use a grammar-like notation to specify the desired lexical structure.
However, macros remain just abbreviations of the regular expressions they
represent. They are not non-terminals of a grammar and cannot be used
recursively. JFlex detects cycles in macro definitions and reports them at
generation time. JFlex also warns you about macros that have been defined but
never used in the _lexical rules_ section of the specification.


Lexical rules {#LexRules}
-------------

The _lexical rules_ section of a JFlex specification contains a set of
regular expressions and actions (Java code) that are executed when the
scanner matches the associated regular expression.

The `%include` directive may be used in this section to include lexical
rules from a separate file. The directive will be replaced verbatim by
the contents of the specified file.


### Syntax {#Grammar}

The syntax of the _lexical rules_ section is described by the following
EBNF grammar (terminal symbols are enclosed in ’quotes’):

    LexicalRules ::= (Include|Rule)+
    Include      ::= '%include' (' '|'\t'|'\b')+ File
    Rule         ::= [StateList] ['^'] RegExp [LookAhead] Action 
                   | [StateList] '<<EOF>>' Action
                   | StateGroup 
    StateGroup   ::= StateList '{' Rule+ '}' 
    StateList    ::= '<' Identifier (',' Identifier)* '>' 
    LookAhead    ::= '$' | '/' RegExp
    Action       ::= '{' JavaCode '}' | '|'

    RegExp       ::= RegExp '|' RegExp 
                   | RegExp RegExp 
                   | '(' RegExp ')'
                   | ('!'|'~') RegExp
                   | RegExp ('*'|'+'|'?')
                   | RegExp "{" Number ["," Number] "}" 
                   | CharClass
                   | PredefinedClass 
                   | MacroUsage 
                   | '"' StringCharacter+ '"' 
                   | Character 

    CharClass    ::= '[' ['^'] CharClassContent* ']'
                   | '[' ['^'] CharClassContent+ 
                         CharClassOperator CharClassContent+ ']'
                     
    CharClassContent    ::= CharClass | Character |
                            Character'-'Character | 
                            MacroUsage | PredefinedClass

    CharClassOperator   ::= '||' | '&&' | '--' | '~~'

    MacroUsage          ::= '{' Identifier '}'

    PredefinedClass     ::= '[:jletter:]' 
                          | '[:jletterdigit:]' 
                          | '[:letter:]' 
                          | '[:digit:]'
                          | '[:uppercase:]' 
                          | '[:lowercase:]'
                          | '\d' | '\D'
                          | '\s' | '\S'
                          | '\w' | '\W'
                          | '\p{' UnicodePropertySpec '}'
                          | '\P{' UnicodePropertySpec '}'
                          | '\R'
                          | '.'          
                                
    UnicodePropertySpec ::= BinaryProperty | 
                            EnumeratedProperty (':' | '=') PropertyValue

    BinaryProperty      ::= Identifier

    EnumeratedProperty  ::= Identifier

    PropertyValue       ::= Identifier

The grammar uses the following terminal symbols:

-   `File`\
    a file name, either absolute or relative to the directory containing
    the lexical specification.

-   `JavaCode`\
    a sequence of `BlockStatements` as described in the Java Language
    Specification [@LangSpec], section 14.2.

-   `Number`\
    a non negative decimal integer.

-   `Identifier`\
    a letter `[a-zA-Z]` followed by a sequence of zero or more letters,
    digits or underscores `[a-zA-Z0-9_]`

-   `Character`\
    an escape sequence or any unicode character that is not one of these
    meta characters:
    `  |  (  )  {  }  [  ]  < >  \  .  *  +  ?  ^  $  / . " ~ !`

-   `StringCharacter`\
    an escape sequence or any unicode character that is not one of these
    meta characters: `  \  "`

-   An escape sequence

    -   `\n` `\r` `\t` `\f` `\b`

    -   a `\x` followed by two hexadecimal digits `[a-fA-F0-9]`
        (denoting an ASCII escape sequence);

    -   a `\u` followed by four hexadecimal digits `[a-fA-F0-9]`,
        denoting a unicode escape sequence. Note that these are 
        precisely four digits, i.e. `\u12345` is the character
        `\u1234` followed by the character `5`.

    -   a `\U` (note that the ’U’ is uppercase) followed by six
        hexadecimal digits `[a-fA-F0-9]`, denoting a unicode code point
        escape sequence;

    -   `\u{H+( H+)*}`, where `H+` is one or more hexadecimal digits
        `[a-fA-F0-9]`, each `H+` denotes a code point - note that in
        character classes, only one code point is allowed;

    -   a backslash followed by a three digit octal number from 000 to
        377, denoting an ASCII escape sequence; or

    -   a backslash followed by any other unicode character that stands
        for this character.

Please note that the `\n` escape sequence stands for the ASCII LF
character - not for the end of line. If you would like to match the line
terminator, you should use the expression `\r|\n|\r\n` if you want the
Java conventions, or `\r\n|[\r\n\u2028\u2029\u000B\u000C\u0085]`
(provided as predefined class `\R`) if you want to be fully Unicode
compliant (see also [@unicode_rep]).

The white-space characters `" "` (space) and `\t` (tab) can be used to
improve the readability of regular expressions. They will be ignored by
JFlex. In character classes and strings, however, white-space characters keep
standing for themselves (so the string `" "` still matches exactly one space
character and `[ \n]` still matches an ASCII LF or a space character).

JFlex applies the following standard operator precedences in regular
expression (from highest to lowest):

-   unary postfix operators (`*`, `+`, `?`, `{n}`, `{n,m}`)

-   unary prefix operators (`!`, `~`)

-   concatenation (`RegExp::= RegExp Regexp`)

-   union (`RegExp::= RegExp '|' RegExp`)

So the expression `a | abc | !cd*` for instance is parsed as
`(a|(abc)) | ((!c)(d*))`.


### Semantics {#Semantics}

This section gives an informal description of which text is matched by a
regular expression, i.e. an expression described by the `RegExp` production
of the grammar [above](#Grammar).

A regular expression that consists solely of

-   a `Character` matches this character.

-   a character class `[...]` matches any character in that class. A
    `Character` is considered an element of a class if it is
    listed in the class or if its code lies within a listed character
    range `Character’-’Character` or Macro or predefined character
    class. So `[a0-3\n]` for instance matches the characters

    `a 0 1 2 3 \n`

    If the list of characters is empty (i.e. just `[]`), the expression
    matches nothing at all (the empty set), not even the empty string.
    This can be useful in combination with the negation operator `!`.

    Character sets may be nested, e.g. `[[[abc]d[e]]fg]` is equivalent
    to `[abcdefg]`.

    Supported character set operations:

    -   Union (`||`), e.g. `[[a-c]||[d-f]]`, equivalent to `[a-cd-f]`:
        this is the default character set operation when no operator is
        specified.

    -   Intersection (`&&`), e.g. `[[a-f]&&[f-m]]`, equivalent to `[f]`.

    -   Set difference (`--`), e.g. `[[a-z]--m]`, equivalent to
        `[a-ln-z]`.

    -   Symmetric difference (`~~`): the union of two classes minus
        their intersection. For instance 

            [\p{Letter}~~\p{ASCII}] 

        is equivalent to 
        
            [[\p{Letter}||\p{ASCII}]--[\p{Letter}&&\p{ASCII}]]
        
        the set of characters that are present in either `\p{Letter}` or in
        `\p{ASCII}`, but not in both.

-   a negated character class `'[^...]'` matches all characters not
    listed in the class. If the list of characters is empty (i.e.
    `[^]`), the expression matches any character of the input character
    set.

-   a string `’’ StringCharacter+ ’’` matches the exact text enclosed in
    double quotes. All meta characters apart from `\` and `"` lose their
    special meaning inside a string. See also the `%ignorecase`
    switch.

-   a macro usage `'{' Identifier '}'` matches the input that is matched
    by the right hand side of the macro with name `Identifier`.

-   a predefined character class matches any of the characters in that
    class. There are the following predefined character classes:

    -   two predefined character classes that are determined by Java
        library functions in class `java.lang.Character`:

                [:jletter:]       isJavaIdentifierStart()
                [:jletterdigit:]  isJavaIdentifierPart()

    -   four predefined character classes equivalent to the
        following Unicode properties (described [below](#unipropsyntax)):

                [:letter:]     \p{Letter}
                [:digit:]      \p{Digit}
                [:uppercase:]  \p{Uppercase}
                [:lowercase:]  \p{Lowercase}

    -   the following meta characters, equivalent to these (sets of)
        Unicode Properties (described [below](#unipropsyntax)):

                \d  \p{Digit}
                \D  \P{Digit}
                \s  \p{Whitespace}
                \S  \P{Whitespace}
                \w  [\p{Alpha}\p{Digit}\p{Mark}
                     \p{Connector Punctuation}\p{Join Control}]
                \W  [^\p{Alpha}\p{Digit}\p{Mark}
                      \p{Connector Punctuation}\p{Join Control}]

    -   \label{unipropsyntax}
        <!-- FIXME: inline refs don't link properly in pdf -->
        <a name="unipropsyntax"></a>Unicode Properties 
        are character classes specified by each
        version of the Unicode Standard. JFlex supports a subset of all
        defined Properties for each supported Unicode version. To see
        the full list of supported Properties, give the
        `–uniprops <ver>` option on the JFlex command line, where
        `<ver>` is the Unicode version. Some Properties have aliases;
        JFlex recognizes all aliases for all supported properties. JFlex
        supports loose matching of Properties: case distinctions,
        whitespace, hyphens, and underscores are ignored.

        To refer to a Unicode Property, use the `\p{...}` syntax, e.g.
        the Greek Block can be referred to as `\p{Block:Greek}`. To
        match all characters not included in a property, use the
        `\P{...}` syntax (note that the ’`P`’ is uppercase), e.g. to
        match all characters that are **not** letters, use `\P{Letter}`.

        See UTS\#18 [@unicode_rep] for a description of and links to
        definitions of some supported Properties. UnicodeSet [@UnicodeSet]
        is an online utility to show the character sets corresponding to
        Unicode Properties and set operations on them, but only for the
        most recent Unicode version.

    -   Dot (`.`) matches `[^\r\n\u2028\u2029\u000B\u000C\u0085]`.\
        Use the `–legacydot` option to instead match `[^\n]`.

    -   `\R` matches any newline:
        `\r\n|[\r\n\u2028\u2029\u000B\u000C\u0085]`.

If `a` and `b` are regular expressions, then

-   `a | b` (union)

    is the regular expression that matches all input matched by `a` or
    by `b`.

-   `a b` (concatenation)

    is the regular expression that matches the input matched by `a`
    followed by the input matched by `b`.

-   `a*` (Kleene closure)

    matches zero or more repetitions of the input matched by `a`

-   `a+` (iteration)

    is equivalent to `aa*`

-   `a?` (option)

    matches the empty input or the input matched by `a`

-   `!a` (negation)

    matches everything but the strings matched by `a`. Use with care:
    the construction of `!a` involves an additional, possibly
    exponential NFA to DFA transformation on the NFA for `a`. Note that
    with negation and union you also have (by applying DeMorgan)
    intersection and set difference: the intersection of `a` and `b` is
    `!(!a|!b)`, the expression that matches everything of `a` not
    matched by `b` is `!(!a|b)`

-   `~a` (upto)

    matches everything up to (and including) the first occurrence of a
    text matched by `a`. The expression `~a` is equivalent to
    `!([^]* a [^]*) a`. A traditional C-style comment is matched by
    `"/*" ~"*/"`

-   `a {n}` (repeat)

    is equivalent to `n` times the concatenation of `a`. So `a{4}` for
    instance is equivalent to the expression `a a a a`. The decimal
    integer `n` must be positive.

-   `a {n,m}`

    is equivalent to at least `n` times and at most `m` times the
    concatenation of `a`. So `a{2,4}` for instance is equivalent to the
    expression `a a a? a?`. Both `n` and `m` are non-negative decimal
    integers and `m` must not be smaller than `n`.

-   `(a)`

    matches the same input as `a`.

In a lexical rule, a regular expression `r` may be preceded by a `^`
(the beginning of line operator). `r` is then only matched at the
beginning of a line in the input. A line begins after each occurrence of
`\r|\n|\r\n|\u2028|\u2029|\u000B|\u000C|\u0085` (see also [@unicode_rep])
and at the beginning of input. The preceding line terminator in the
input is not consumed and can be matched by another rule.

In a lexical rule, a regular expression `r` may be followed by a look-ahead
expression. A look-ahead expression is either `$` (the end of line operator)
or `/` followed by an arbitrary regular expression. In both cases the
look-ahead is not consumed and not included in the matched text region, but
it **is** considered while determining which rule has the longest match (see
also [How the input is matched](#HowMatched)).

In the `$` case, `r` is only matched at the end of a line in the input.
The end of a line is denoted by the regular expression
`\r|\n|\r\n|\u2028|\u2029|\u000B|\u000C|\u0085`. So `a$` is equivalent
to `a / \r|\n|\r\n|\u2028|\u2029|\u000B|\u000C|\u0085`. This is
different to the situation described in [@unicode_rep]: since in JFlex `$`
is a true trailing context, the end of file does **not** count as end of
line.

For arbitrary look-ahead (also called _trailing context_) the expression is
matched only when followed by input that matches the trailing context.

JFlex allows lex/flex style `<<EOF>>` rules in lexical specifications. A rule

    [StateList]  <<EOF>>    { action code }

is very similar to the `%eofval` directive. The difference lies in the
optional `StateList` that may precede the `<<EOF>>` rule. The action code
will only be executed when the end of file is read and the scanner is
currently in one of the lexical states listed in `StateList`. The same
`StateGroup` (see section [How the input is matched](#HowMatched)) and
precedence rules as in the “normal” rule case apply (i.e. if there is more
than one `<<EOF>>` rule for a certain lexical state, the action of the one
appearing earlier in the specification will be executed). `<<EOF>>` rules
override settings of the `%cup` and `%byaccj` options and should not be mixed
with the `%eofval` directive.

An `Action` consists either of a piece of Java code enclosed in curly
braces or is the special `|` action. The `|` action is an abbreviation
for the action of the following expression.

Example:

    expression1   |
    expression2   |
    expression3   { some action }

is equivalent to the expanded form

    expression1   { some action }
    expression2   { some action }
    expression3   { some action }

They are useful when working with trailing context expressions. The
expression `a | (c / d) | b` is not a syntactically legal regular expression,
but can be expressed using the `|` action:

    a       |
    c / d   |
    b       { some action }


### How the input is matched {#HowMatched}

When consuming its input, the scanner determines the regular expression
that matches the longest portion of the input (longest match rule). If
there is more than one regular expression that matches the longest
portion of input (i.e. they all match the same input), the generated
scanner chooses the expression that appears first in the specification.
After determining the active regular expression, the associated action
is executed. If there is no matching regular expression, the scanner
terminates the program with an error message (if the `%standalone`
directive has been used, the scanner prints the unmatched input to
`java.lang.System.out` instead and resumes scanning).

Lexical states can be used to further restrict the set of regular
expressions that match the current input.

-   A regular expression can only be matched when its associated set of
    lexical states includes the currently active lexical state of the
    scanner or if the set of associated lexical states is empty and the
    currently active lexical state is inclusive. Exclusive and inclusive
    states only differ in this one point: rules with an empty set of
    associated states.

-   The currently active lexical state of the scanner can be changed
    from within an action of a regular expression using the method
    `yybegin()`.

-   The scanner starts in the inclusive lexical state `YYINITIAL`, which
    is always declared by default.

-   The set of lexical states associated with a regular expression is
    the `StateList` that precedes the expression. If a rule is contained
    in one or more `StateGroups`, then the states of these are also
    associated with the rule, i.e. they accumulate over `StateGroups`.

    Example:

        %states A, B
        %xstates C
        %%
        expr1                   { yybegin(A); action }
        <YYINITIAL, A> expr2    { action }
        <A> {
          expr3                 { action }
          <B,C> expr4           { action }
        }

    The first line declares two (inclusive) lexical states `A` and `B`,
    the second line an exclusive lexical state `C`. The default
    (inclusive) state `YYINITIAL` is always implicitly there and doesn’t
    need to be declared. The rule with `expr1` has no states listed, and
    is thus matched in all states but the exclusive ones, i.e. `A`, `B`,
    and `YYINITIAL`. In its action, the scanner is switched to state
    `A`. The second rule `expr2` can only match when the scanner is in
    state `YYINITIAL` or `A`. The rule `expr3` can only be matched in
    state `A` and `expr4` in states `A`, `B`, and `C`.

-   Lexical states are declared and used as Java `int` constants in the
    generated class under the same name as they are used in the
    specification. There is no guarantee that the values of these
    integer constants are distinct. They are pointers into the generated
    DFA table, and if JFlex recognises two states as lexically
    equivalent (if they are used with the exact same set of regular
    expressions), then the two constants will get the same value.


### The generated class

JFlex generates exactly one file containing one class from the
specification (unless you have declared another class in the first
specification section).

The generated class contains (among other things) the DFA tables, an
input buffer, the lexical states of the specification, a constructor,
and the scanning method with the user supplied actions.

The name of the class is by default `Yylex`. The name is customisable with
the `%class` directive. The input buffer of the lexer is connected with
external input through the `java.io.Reader` object which is passed to the
lexer in the generated constructor. If you provide your own constructor for
the lexer, you should always chain-call the generated one to initialise the
input buffer. The input buffer should not be accessed directly, but only
through the advertised API (see also [Scanner Methods](#ScannerMethods)). Its
internal implementation may change between releases or skeleton files without
notice.

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


### Scanner methods and fields accessible in actions (API) {#ScannerMethods}

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
    (as opposed to Unicode code points). Does notrequire a `String` object to be
    created.

-   `char yycharat(int pos)`

    returns the Java `char` at position `pos` from the matched text. It is
    equivalent to `yytext().charAt(pos)`, but faster. `pos` must be a
    value from `0` to `yylength()-1`.

-   `void yyclose()`

    closes the input stream. All subsequent calls to the scanning method
    will return the end of file value

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

    Note that with Unicode surrogate characters it is possible that
    expressions such as `[^]` match more than one `char`.

-   `int yyline`

    contains the current line of input (starting with 0, only active
    with the `lineCounting` directive)

-   `int yychar`

    contains the current character count in the input (starting with 0,
    only active with the `charCounting` directive)

-   `int yycolumn`

    contains the current column of the current line (starting with 0,
    only active with the `columnCounting` directive)

