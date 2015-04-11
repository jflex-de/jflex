Lexical Specifications {#Specifications}
======================

As shown above, a lexical specification file for JFlex consists of three
parts divided by a single line starting with `%%`:

`UserCode`\
`%%`\
`Options and declarations`\
`%%`\
`Lexical rules`

In all parts of the specification comments of the form
`/* comment text */` and the Java style end of line comments starting
with `//` are permitted. JFlex comments do nest - so the number of `/*`
and `*/` should be balanced.

User code {#SpecUsercode}
---------

The first part contains user code that is copied verbatim into the
beginning of the source file of the generated lexer before the scanner
class is declared. As shown in the example above, this is the place to
put `package` declarations and `import` statements. It is possible, but
not considered good Java programming style to put own helper classes
(such as token classes) in this section. They should get their own
`.java` file instead.

Options and declarations {#SpecOptions}
------------------------

The second part of the lexical specification contains
<span>[options](#SpecOptDirectives)</span> to customise your generated
lexer (JFlex directives and Java code to include in different parts of
the lexer), declarations of <span>[lexical states](#StateDecl)</span>
and <span>[macro definitions](#MacroDefs)</span> for use in the third
section <span>[“Lexical rules”](#LexRules)</span> of the lexical
specification file. [SpecOptDirectives]

Each JFlex directive must be situated at the beginning of a line and
starts with the `%` character. Directives that have one or more
parameters are described as follows:

`%class classname`

means that you start a line with `%class` followed by a space followed
by the name of the class for the generated scanner (the double quotes
are *not* to be entered, see the <span>[example
specification](#CodeOptions)</span> in section [CodeOptions]).

### Class options and user class code {#ClassOptions}

These options regard name, constructor, API, and related parts of the
generated scanner class.

-   **`%class classname`**

    Tells JFlex to give the generated class the name “`classname`” and
    to write the generated code to a file “`classname.java`”. If the
    `-d <directory>` command line option is not used, the code will be
    written to the directory where the specification file resides. If no
    `%class` directive is present in the specification, the generated
    class will get the name “`Yylex`” and will be written to a file
    “`Yylex.java`”. There should be only one `%class` directive in a
    specification.

-   **`%implements interface 1[, interface 2, ..]`**

    Makes the generated class implement the specified interfaces. If
    more than one `%implements` directive is present, all the specified
    interfaces will be implemented.

-   **`%extends classname`**

    Makes the generated class a subclass of the class “`classname`”.
    There should be only one `%extends` directive in a specification.

-   **`%public`**

    Makes the generated class public (the class is only accessible in
    its own package by default).

-   **`%final`**

    Makes the generated class final.

-   **`%abstract`**

    Makes the generated class abstract.

-   **`%apiprivate`**

    Makes all generated methods and fields of the class private.
    Exceptions are the constructor, user code in the specification, and,
    if `%cup` is present, the method `next_token`. All occurrences of
    ` public ` (one space character before and after `public`) in the
    skeleton file are replaced by ` private ` (even if a user-specified
    skeleton is used). Access to the generated class is expected to be
    mediated by user class code (see next switch).

-   <span>**`%{`**</span>\
    **`...`**\
    <span>**`%}`**</span>

    The code enclosed in `%{` and `%}` is copied verbatim into the
    generated class. Here you can define your own member variables and
    functions in the generated scanner. Like all options, both `%{` and
    `%}` must start a line in the specification. If more than one class
    code directive `%{...%}` is present, the code is concatenated in
    order of appearance in the specification.

-   <span>**`%init{`**</span>\
    **`...`**\
    <span>**`%init}`**</span>

    The code enclosed in `%init{` and `%init}` is copied verbatim into
    the constructor of the generated class. Here, member variables
    declared in the `%{...%}` directive can be initialised. If more than
    one initialiser option is present, the code is concatenated in order
    of appearance in the specification.

-   <span>**`%initthrow{`**</span>\
    <span>**`exception1[, exception2, ...]`**</span>\
    <span>**`%initthrow}`**</span>

    or (on a single line) just

    <span>**`%initthrow exception1 [, exception2, ...]`**</span>

    Causes the specified exceptions to be declared in the `throws`
    clause of the constructor. If more than one `%initthrow{` `...`
    `%initthrow}` directive is present in the specification, all
    specified exceptions will be declared.

-   <span>**`%ctorarg type ident`**</span>

    Adds the specified argument to the constructors of the generated
    scanner. If more than one such directive is present, the arguments
    are added in order of occurrence in the specification. Note that
    this option conflicts with the `%standalone` and `%debug`
    directives, because there is no sensible default that can be created
    automatically for such parameters in the generated `main` methods.
    JFlex will warn in this case and generate an additional default
    constructor without these parameters and without user init code
    (which might potentially refer to the parameters).

-   <span>**`%scanerror exception`**</span>

    Causes the generated scanner to throw an instance of the specified
    exception in case of an internal error (default is
    `java.lang.Error`). Note that this exception is only for internal
    scanner errors. With usual specifications it should never occur
    (i.e. if there is an error fallback rule in the specification and
    only the documented scanner API is used).

-   <span>**`%buffer size`**</span>

    Set the initial size of the scan buffer to the specified value
    (decimal, in bytes). The default value is 16384.

-   <span>**`%include filename`**</span>

    Replaces the `%include` verbatim by the specified file. This feature
    is still experimental. It works, but error reporting can be strange
    if a syntax error occurs on the last token in the included file.

### Scanning method {#ScanningMethod}

This section shows how the scanning method can be customised. You can
redefine the name and return type of the method and it is possible to
declare exceptions that may be thrown in one of the actions of the
specification. If no return type is specified, the scanning method will
be declared as returning values of class `Yytoken`.

-   <span>**`%function name`**</span>

    Causes the scanning method to get the specified name. If no
    `%function` directive is present in the specification, the scanning
    method gets the name “`yylex`”. This directive overrides settings of
    the `cupCupMode` switch. Please note that the default name of the
    scanning method with the `cupCupMode` switch is `next_token`.
    Overriding this name might lead to the generated scanner being
    implicitly declared as `abstract`, because it does not provide the
    method `next_token` of the interface `java_cup.runtime.Scanner`. It
    is of course possible to provide a dummy implementation of that
    method in the class code section if you still want to override the
    function name.

-   <span>**`%integer`**</span>\
    <span>**`%int`**</span>

    Both cause the scanning method to be declared as of Java type `int`.
    Actions in the specification can then return `int` values as tokens.
    The default end of file value under this setting is `YYEOF`, which
    is a `public static final int` member of the generated class.

-   <span>**`%intwrap`**</span>

    Causes the scanning method to be declared as of the Java wrapper
    type `Integer`. Actions in the specification can then return
    `Integer` values as tokens. The default end of file value under this
    setting is `null`.

-   <span>**`%type typename`**</span>

    Causes the scanning method to be declared as returning values of the
    specified type. Actions in the specification can then return values
    of `typename` as tokens. The default end of file value under this
    setting is `null`. If `typename` is not a subclass of
    `java.lang.Object`, you should specify another end of file value
    using the <span>[](#\texttt)<span>%eofval</span> `...`
    `%eofval`</span><span>eofval</span> directive or the
    <span>[`<<EOF>>` rule](#EOFRule)</span>. The `%type` directive
    overrides settings of the `cupCupMode` switch.

-   <span>**`%yylexthrow{`**</span>\
    <span>**`exception1[, exception2, ... ]`**</span>\
    <span>**`%yylexthrow}`**</span>

    or (on a single line) just

    <span>**`%yylexthrow exception1 [, exception2, ...]`**</span>

    The exceptions listed inside `%yylexthrow{` `...` `%yylexthrow}`
    will be declared in the throws clause of the scanning method. If
    there is more than one `%yylexthrow{` `...` `%yylexthrow}` clause in
    the specification, all specified exceptions will be declared.

### The end of file {#EOF}

There is always a default value that the scanning method will return
when the end of file has been reached. You may however define a specific
value to return and a specific piece of code that should be executed
when the end of file is reached.

The default end of file value depends on the return type of the scanning
method:

-   For <span>**`%integer`**</span>, the scanning method will return the
    value <span>**`YYEOF`**</span>, which is a `public static final int`
    member of the generated class.

-   For <span>**`%intwrap`**</span>,

-   no specified type at all, or a

-   user defined type, declared using <span>**`%type`**</span>, the
    value is <span>**`null`**</span>.

-   In CUP compatibility mode, using <span>**`%cup`**</span>, the value
    is

    <span>**`new java_cup.runtime.Symbol(sym.EOF)`**</span>

User values and code to be executed at the end of file can be defined
using these directives:

-   <span>**`%eofval{`**</span>\
    <span>**`...`**</span>\
    <span>**`%eofval}`**</span>

    The code included in `%eofval{` `...` `%eofval}` will be copied
    verbatim into the scanning method and will be executed
    <span><span>**</span>each time</span> when the end of file is
    reached (this is possible when the scanning method is called again
    after the end of file has been reached). The code should return the
    value that indicates the end of file to the parser. There should be
    only one `%eofval{` `...` `%eofval}` clause in the specification.
    The `%eofval{ ... %eofval}` directive overrides settings of the
    `cupCupMode` switch and `byaccjYaccMode` switch. As of version 1.2
    JFlex provides a more readable way to specify the end of file value
    using the <span>[`<<EOF>>` rule](#EOFRule)</span> (see also section
    [EOFRule]).

-   [eof] <span>**`%eof{`**</span>\
    <span>**`...`**</span>\
    <span>**`%eof}`**</span>

    The code included in `%{eof ... %eof}` will be executed exactly
    once, when the end of file is reached. The code is included inside a
    method `void yy_do_eof()` and should not return any value (use
    `%eofval{...%eofval}` or <span>[`<<EOF>>`](#EOFRule)</span> for this
    purpose). If more than one end of file code directive is present,
    the code will be concatenated in order of appearance in the
    specification.

-   <span>**`%eofthrow{`**</span>\
    <span>**`exception1[,exception2, ... ]`**</span>\
    <span>**`%eofthrow}`**</span>

    or (on a single line) just

    <span>**`%eofthrow exception1 [, exception2, ...]`**</span>

    The exceptions listed inside `%eofthrow{...%eofthrow}` will be
    declared in the throws clause of the method `yy_do_eof()` (see
    <span>[](#\texttt)<span>%eof</span></span><span>eof</span> for more
    on that method). If there is more than one `%eofthrow{...%eofthrow}`
    clause in the specification, all specified exceptions will be
    declared.

    [eofclose]

-   <span>**`%eofclose`**</span>

    Causes JFlex to close the input stream at the end of file. The code
    `yyclose()` is appended to the method `yy_do_eof()` (together with
    the code specified in `%eof{...%eof}`) and the exception
    `java.io.IOException` is declared in the throws clause of this
    method (together with those of `%eofthrow{...%eofthrow}`)

-   <span>**`%eofclose false`**</span>

    Turns the effect of `%eofclose` off again (e.g. in case closing of
    input stream is not wanted after `%cup`).

### Standalone scanners {#Standalone}

-   <span>**`%debug`**</span>

    Creates a main function in the generated class that expects the name
    of an input file on the command line and then runs the scanner on
    this input file by printing information about each returned token to
    the Java console until the end of file is reached. The information
    includes: line number (if line counting is enabled), column (if
    column counting is enabled), the matched text, and the executed
    action (with line number in the specification).

-   <span>**`%standalone`**</span>

    Creates a main function in the generated class that expects the name
    of an input file on the command line and then runs the scanner on
    this input file. The values returned by the scanner are ignored, but
    any unmatched text is printed to the Java console instead (as the
    C/C++ tool flex does, if run as standalone program). To avoid having
    to use an extra token class, the scanning method will be declared as
    having default type `int`, not `YYtoken` (if there isn’t any other
    type explicitly specified). This is in most cases irrelevant, but
    could be useful to know when making another scanner standalone for
    some purpose. You should also consider using the `%debug` directive,
    if you just want to be able to run the scanner without a parser
    attached for testing etc.

### CUP compatibility {#CupMode}

You may also want to read section [CUPWork] <span>[*JFlex and
CUP*](#CUPWork)</span> if you are interested in how to interface your
generated scanner with CUP.

-   <span>**`%cup`**</span>

    The `%cup` directive enables the CUP compatibility mode and is
    equivalent to the following set of directives:

        %implements java_cup.runtime.Scanner
        %function next_token
        %type java_cup.runtime.Symbol
        %eofval{
          return new java_cup.runtime.Symbol(<CUPSYM>.EOF);
        %eofval}
        %eofclose

    The value of `<CUPSYM>` defaults to `sym` and can be changed with
    the `%cupsym` directive. In JLex compatibility mode (`–jlex` switch
    on the command line), `%eofclose` will not be turned on.

-   <span>**`%cup2`**</span>

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

-   <span>**`%cupsym classname`**</span>

    Customises the name of the CUP generated class/interface containing
    the names of terminal tokens. Default is `sym`. The directive should
    not be used after `%cup`, but before.

-   <span>**`%cupdebug`**</span>

    Creates a main function in the generated class that expects the name
    of an input file on the command line and then runs the scanner on
    this input file. Prints line, column, matched text, and CUP symbol
    name for each returned token to standard out.

### BYacc/J compatibility {#YaccMode}

You may also want to read section [YaccWork] <span>[*JFlex and
BYacc/J*](#YaccWork)</span> if you are interested in how to interface
your generated scanner with Byacc/J.

-   <span>**`%byacc`**</span>

    The `%byacc` directive enables the BYacc/J compatibility mode and is
    equivalent to the following set of directives:

        %integer
        %eofval{
          return 0;
        %eofval}
        %eofclose

### Character sets {#CharacterSets}

-   <span>**`%7bit`**</span>

    Causes the generated scanner to use an 7 bit input character set
    (character codes 0-127). If an input character with a code greater
    than 127 is encountered in an input at runtime, the scanner will
    throw an `ArrayIndexOutofBoundsException`. Not only because of this,
    you should consider using the `%unicode` directive. See also section
    [sec:encodings] for information about character encodings. This is
    the default in JLex compatibility mode.

-   <span>**`%full`**</span>\
    <span>**`%8bit`**</span>

    Both options cause the generated scanner to use an 8 bit input
    character set (character codes 0-255). If an input character with a
    code greater than 255 is encountered in an input at runtime, the
    scanner will throw an `ArrayIndexOutofBoundsException`. Note that
    even if your platform uses only one byte per character, the Unicode
    value of a character may still be greater than 255. If you are
    scanning text files, you should consider using the `%unicode`
    directive. See also section [sec:encodings] for more information
    about character encodings.

-   <span>**`%unicode`**</span>\
    <span>**`%16bit`**</span>

    Both options cause the generated scanner to use the full Unicode
    input character set, including supplementary code points:
    0-0x10FFFF. `%unicode` does not mean that the scanner will read two
    bytes at a time. What is read and what constitutes a character
    depends on the runtime platform. See also section [sec:encodings]
    for more information about character encodings. This is the default
    unless the JLex compatibility mode is used (command line option
    `–jlex`).

    [caseless]

-   <span>**`%caseless`**</span>\
    <span>**`%ignorecase`**</span>

    This option causes JFlex to handle all characters and strings in the
    specification as if they were specified in both uppercase and
    lowercase form. This enables an easy way to specify a scanner for a
    language with case insensitive keywords. The string “`break`” in a
    specification is for instance handled like the expression
    `([bB][rR][eE][aA][kK])`. The `%caseless` option does not change the
    matched text and does not affect character classes. So `[a]` still
    only matches the character `a` and not `A`, too. Which letters are
    uppercase and which lowercase letters, is defined by the Unicode
    standard. In JLex compatibility mode (`–jlex` switch on the command
    line), `%caseless` and `%ignorecase` also affect character classes.

### Line, character and column counting {#Counting}

-   <span>**`%char`**</span>

    Turns character counting on. The `int` member variable `yychar`
    contains the number of characters (starting with 0) from the
    beginning of input to the beginning of the current token.

-   <span>**`%line`**</span>

    Turns line counting on. The `int` member variable `yyline` contains
    the number of lines (starting with 0) from the beginning of input to
    the beginning of the current token.

-   <span>**`%column`**</span>

    Turns column counting on. The `int` member variable `yycolumn`
    contains the number of characters (starting with 0) from the
    beginning of the current line to the beginning of the current token.

### Obsolete JLex options {#Obsolete}

-   <span>**`%notunix`**</span>

    This JLex option is obsolete in JFlex but still recognised as valid
    directive. It used to switch between Windows and Unix kind of line
    terminators (`\r\n` and `\n`) for the `$` operator in regular
    expressions. JFlex always recognises both styles of platform
    dependent line terminators.

-   <span>**`%yyeof`**</span>

    This JLex option is obsolete in JFlex but still recognised as valid
    directive. In JLex it declares a public member constant `YYEOF`.
    JFlex declares it in any case.

### State declarations {#StateDecl}

State declarations have the following form:

`%s[tate] state identifier [, state identifier, ... ]` for inclusive or\
`%x[state] state identifier [, state identifier, ... ]` for exclusive
states

There may be more than one line of state declarations, each starting
with `%state` or `%xstate` (the first character is sufficient, `%s` and
`%x` works, too). State identifiers are letters followed by a sequence
of letters, digits or underscores. State identifiers can be separated by
white-space or comma.

The sequence

`%state STATE1`\
`%xstate STATE3, XYZ, STATE_10`\
`%state ABC STATE5`

declares the set of identifiers
`STATE1, STATE3, XYZ, STATE_10, ABC, STATE5` as lexical states,
`STATE1`, `ABC`, `STATE5` as inclusive, and `STATE3`, `XYZ`, `STATE_10`
as exclusive. See also section [HowMatched] on the way lexical states
influence how the input is matched.

### Macro definitions {#MacroDefs}

A macro definition has the form

`macroidentifier = regular expression`

That means, a macro definition is a macro identifier (letter followed by
a sequence of letters, digits or underscores), that can later be used to
reference the macro, followed by optional white-space, followed by an
“`=`”, followed by optional white-space, followed by a regular
expression (see section [LexRules] <span>[*lexical
rules*](#LexRules)</span> for more information about regular
expressions).

The regular expression on the right hand side must be well formed and
must not contain the `^`, `/` or `$` operators. **Differently to JLex,
macros are not just pieces of text that are expanded by copying** - they
are parsed and must be well formed.

**This is a feature.** It eliminates some very hard to find bugs in
lexical specifications (such like not having parentheses around more
complicated macros - which is not necessary with JFlex). See section
[Porting] <span>[*Porting from JLex*](#Porting)</span> for more details
on the problems of JLex style macros.

Since it is allowed to have macro usages in macro definitions, it is
possible to use a grammar like notation to specify the desired lexical
structure. Macros however remain just abbreviations of the regular
expressions they represent. They are not non terminals of a grammar and
cannot be used recursively in any way. JFlex detects cycles in macro
definitions and reports them at generation time. JFlex also warns you
about macros that have been defined but never used in the “lexical
rules” section of the specification.

Lexical rules {#LexRules}
-------------

The “lexical rules” section of a JFlex specification contains a set of
regular expressions and actions (Java code) that are executed when the
scanner matches the associated regular expression.

The `%include` directive may be used in this section to include lexical
rules from a separate file. The directive will be replaced verbatim by
the contents of the specified file.

### Syntax {#Grammar}

The syntax of the “lexical rules” section is described by the following
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

[Terminals] The grammar uses the following terminal symbols:

-   `File`\
    a file name, either absolute or relative to the directory containing
    the lexical specification.

-   `JavaCode`\
    a sequence of <span><span>**</span>`BlockStatements`</span> as
    described in the Java Language Specification @LangSpec, section
    14.2.

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
        (denoting a standard ASCII escape sequence);

    -   a `\u` followed by four hexadecimal digits `[a-fA-F0-9]`
        (denoting a unicode escape sequence);

    -   a `\U` (note that the ’U’ is uppercase) followed by six
        hexadecimal digits `[a-fA-F0-9]` (denoting a unicode code point
        escape sequence);

    -   `\u{H+( H+)*}`, where `H+` is one or more hexadecimal digits
        `[a-fA-F0-9]`, each `H+` denotes a code point - note that in
        character classes, only one code point is allowed;

    -   a backslash followed by a three digit octal number from 000 to
        377 (denoting a standard ASCII escape sequence); or

    -   a backslash followed by any other unicode character that stands
        for this character.

Please note that the `\n` escape sequence stands for the ASCII LF
character - not for the end of line. If you would like to match the line
terminator, you should use the expression `\r|\n|\r\n` if you want the
Java conventions, or `\r\n|[\r\n\u2028\u2029\u000B\u000C\u0085]`
(provided as predefined class `\R`) if you want to be fully Unicode
compliant (see also @unicode_rep).

As of version 1.1 of JFlex the white-space characters `" "` (space) and
`"\t"` (tab) can be used to improve the readability of regular
expressions. They will be ignored by JFlex. In character classes and
strings however, white-space characters keep standing for themselves (so
the string `" "` still matches exactly one space character and `[ \n]`
still matches an ASCII LF or a space character).

JFlex applies the following standard operator precedences in regular
expression (from highest to lowest):

-   unary postfix operators (`'*', '+', '?', {n}, {n,m}`)

-   unary prefix operators (`'!', '~'`)

-   concatenation (`RegExp::= RegExp Regexp`)

-   union (`RegExp::= RegExp '|' RegExp`)

So the expression `a | abc | !cd*` for instance is parsed as
`(a|(abc)) | ((!c)(d*))`.

### Semantics {#Semantics}

This section gives an informal description of which text is matched by a
regular expression (i.e. an expression described by the `RegExp`
production of the grammar presented <span>[above](#Grammar)</span>).

A regular expression that consists solely of

-   a `Character` matches this character.

-   a character class `'[...]'` matches any character in that class. A
    `Character` is to be considered an element of a class, if it is
    listed in the class or if its code lies within a listed character
    range `Character’-’Character` or Macro or predefined character
    class. So `[a0-3\n]` for instance matches the characters

    `a 0 1 2 3 \n`

    If the list of characters is empty (i.e. just `[]`), the expression
    matches nothing at all (the empty set), not even the empty string.
    This may be useful in combination with the negation operator `'!'`.

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
        their intersection. For instance `[\p{Letter}~~\p{ASCII}]` is
        equivalent to `[[\p{Letter}||\p{ASCII}]--`
        `[\p{Letter}&&\p{ASCII}]]`: the set of characters that are
        present in either `\p{Letter}` or in `\p{ASCII}`, but not in
        both.

-   a negated character class `'[^...]'` matches all characters not
    listed in the class. If the list of characters is empty (i.e.
    `[^]`), the expression matches any character of the input character
    set.

-   a string `’’ StringCharacter+ ’’` matches the exact text enclosed in
    double quotes. All meta characters but `\` and `"` lose their
    special meaning inside a string. See also the
    <span>[](#\texttt)<span>%ignorecase</span></span><span>caseless</span>
    switch.

-   a macro usage `'{' Identifier '}'` matches the input that is matched
    by the right hand side of the macro with name “`Identifier`”.

    [predefCharCl]

-   a predefined character class matches any of the characters in that
    class. There are the following predefined character classes:

    -   These two predefined character classes are determined by Java
        functions of class `java`.`lang`.`Character`:

                [:jletter:]       isJavaIdentifierStart()
                [:jletterdigit:]  isJavaIdentifierPart()
                

    -   These four predefined character classes are equivalent to the
        following Unicode properties (described
        <span>[below](#unipropsyntax)</span>):

                [:letter:]     \p{Letter}
                [:digit:]      \p{Digit}
                [:uppercase:]  \p{Uppercase}
                [:lowercase:]  \p{Lowercase}
                

    -   These meta characters are equivalent to the following (sets of)
        Unicode Properties (described
        <span>[below](#unipropsyntax)</span>):

                \d  \p{Digit}
                \D  \P{Digit}
                \s  \p{Whitespace}
                \S  \P{Whitespace}
                \w  [\p{Alpha}\p{Digit}\p{Mark}
                     \p{Connector Punctuation}\p{Join Control}]
                \W  [^\p{Alpha}\p{Digit}\p{Mark}
                      \p{Connector Punctuation}\p{Join Control}]
                

        [unipropsyntax]

    -   Unicode Properties are character classes specified by each
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
        match the all characters not included in a property, use the
        `\P{...}` syntax (note that the ’`P`’ is uppercase), e.g. to
        match all characters that are **not** letters: `\P{Letter}`.

        See UTS\#18 @unicode_rep for a description of and links to
        definitions of some supported Properties. UnicodeSet @UnicodeSet
        is an online utility to show the character sets corresponding to
        Unicode Properties and set operations on them, but only for the
        most recent Unicode version.

    -   Dot (`.`) matches `[^\r\n\u2028\u2029\u000B\u000C\u0085]`.\
        Use the `–legacydot` option to instead match `[^\n]`.

    -   `\R` matches any newline:
        `\r\n|[\r\n\u2028\u2029\u000B\u000C\u0085]`.

If `a` and `b` are regular expressions, then

-   (union)

    is the regular expression that matches all input matched by `a` or
    by `b`.

-   (concatenation)

    is the regular expression that matches the input matched by `a`
    followed by the input matched by `b`.

-   (Kleene closure)

    matches zero or more repetitions of the input matched by `a`

-   (iteration)

    is equivalent to `aa*`

-   (option)

    matches the empty input or the input matched by `a`

-   (negation)

    matches everything but the strings matched by `a`. Use with care:
    the construction of `!a` involves an additional, possibly
    exponential NFA to DFA transformation on the NFA for `a`. Note that
    with negation and union you also have (by applying DeMorgan)
    intersection and set difference: the intersection of `a` and `b` is
    `!(!a|!b)`, the expression that matches everything of `a` not
    matched by `b` is `!(!a|b)`

-   (upto)

    matches everything up to (and including) the first occurrence of a
    text matched by `a`. The expression `~a` is equivalent to
    `!([^]* a [^]*) a`. A traditional C-style comment is matched by
    `"/*" ~"*/"`

-   (repeat)

    is equivalent to `n` times the concatenation of `a`. So `a{4}` for
    instance is equivalent to the expression `a a a a`. The decimal
    integer `n` must be positive.

-   is equivalent to at least `n` times and at most `m` times the
    concatenation of `a`. So `a{2,4}` for instance is equivalent to the
    expression `a a a? a?`. Both `n` and `m` are non negative decimal
    integers and `m` must not be smaller than `n`.

-   matches the same input as `a`.

In a lexical rule, a regular expression `r` may be preceded by a ’`^`’
(the beginning of line operator). `r` is then only matched at the
beginning of a line in the input. A line begins after each occurrence of
`\r|\n|\r\n|\u2028|\u2029|\u000B|\u000C|\u0085` (see also @unicode_rep)
and at the beginning of input. The preceding line terminator in the
input is not consumed and can be matched by another rule.

In a lexical rule, a regular expression `r` may be followed by a
look-ahead expression. A look-ahead expression is either a ’`$`’ (the
end of line operator) or a `'/'` followed by an arbitrary regular
expression. In both cases the look-ahead is not consumed and not
included in the matched text region, but it
<span><span>**</span>is</span> considered while determining which rule
has the longest match (see also [HowMatched] <span>[*How the input is
matched*](#HowMatched)</span>).

In the ’`$`’ case `r` is only matched at the end of a line in the input.
The end of a line is denoted by the regular expression
`\r|\n|\r\n|\u2028|\u2029|\u000B|\u000C|\u0085`. So `a$` is equivalent
to `a / \r|\n|\r\n|\u2028|\u2029|\u000B|\u000C|\u0085`. This is
different to the situation described in @unicode_rep: since in JFlex `$`
is a true trailing context, the end of file does **not** count as end of
line.

[trailingContext] For arbitrary look-ahead (also called
<span><span>**</span>trailing context</span>) the expression is matched
only when followed by input that matches the trailing context.

[EOFRule] As of version 1.2, JFlex allows lex/flex style `<<EOF>>` rules
in lexical specifications. A rule

    [StateList]  <<EOF>>    { some action code }

is very similar to the <span>[](#\texttt)<span>%eofval</span>
directive</span><span>eofval</span> (section [eofval]). The difference
lies in the optional `StateList` that may precede the `<<EOF>>` rule.
The action code will only be executed when the end of file is read and
the scanner is currently in one of the lexical states listed in
`StateList`. The same `StateGroup` (see section [HowMatched] <span>[*How
the input is matched*](#HowMatched)</span>) and precedence rules as in
the “normal” rule case apply (i.e. if there is more than one `<<EOF>>`
rule for a certain lexical state, the action of the one appearing
earlier in the specification will be executed). `<<EOF>>` rules override
settings of the `%cup` and `%byaccj` options and should not be mixed
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

They are useful when you work with trailing context expressions. The
expression `a | (c / d) | b` is not syntactically legal, but can easily
be expressed using the `|` action:

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
    states only differ at this point: rules with an empty set of
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

The name of the class is by default `Yylex`, it is customisable with the
`%class` directive (see also section [ClassOptions]). The input buffer
of the lexer is connected with an input stream over the `java.io.Reader`
object which is passed to the lexer in the generated constructor. If you
want to provide your own constructor for the lexer, you should always
call the generated one in it to initialise the input buffer. The input
buffer should not be accessed directly, but only over the advertised API
(see also section [ScannerMethods]). Its internal implementation may
change between releases or skeleton files without notice.

The main interface to the outside world is the generated scanning method
(default name `yylex`, default return type `Yytoken`). Most of its
aspects are customisable (name, return type, declared exceptions etc.,
see also section [ScanningMethod]). If it is called, it will consume
input until one of the expressions in the specification is matched or an
error occurs. If an expression is matched, the corresponding action is
executed. It may return a value of the specified return type (in which
case the scanning method returns with this value), or if it doesn’t
return a value, the scanner resumes consuming input until the next
expression is matched. If the end of file is reached, the scanner
executes the EOF action, and (also upon each further call to the
scanning method) returns the specified EOF value (see also section
[EOF]).

### Scanner methods and fields accessible in actions (API) {#ScannerMethods}

Generated methods and member fields in JFlex scanners are prefixed with
`yy` to indicate that they are generated and to avoid name conflicts
with user code copied into the class. Since user code is part of the
same class, JFlex has no language means like the `private` modifier to
indicate which members and methods are internal and which ones belong to
the API. Instead, JFlex follows a naming convention: everything starting
with a `zz` prefix like `zzStartRead` is to be considered internal and
subject to change without notice between JFlex releases. Methods and
members of the generated class that do not have a `zz` prefix like
`yycharat` belong to the API that the scanner class provides to users in
action code of the specification. They will remain stable and supported
between JFlex releases as long as possible.

Currently, the API consists of the following methods and member fields:

-   `String yytext()`\
    returns the matched input text region

-   `int yylength()`\
    returns the length of the matched input text region (does not
    require a `String` object to be created)

-   `char yycharat(int pos)`\
    returns the character at position `pos` from the matched text. It is
    equivalent to `yytext().charAt(pos)`, but faster. `pos` must be a
    value from `0` to `yylength()-1`.

-   `void yyclose()`\
    closes the input stream. All subsequent calls to the scanning method
    will return the end of file value

-   `void yyreset(java.io.Reader reader)`\
    closes the current input stream, and resets the scanner to read from
    a new Reader. All internal variables are reset, the old Reader
    *cannot* be reused (content of the internal buffer is discarded and
    lost). The lexical state is set to `YY_INITIAL`. The `%{init` code
    is *not* included in `yyreset`, because it is assumed to run in the
    context of a constructor, not a normal method. If `%{init` does need
    to be repeated, consider constructing a new lexer object instead or
    additionally calling a custom function that performs any additional
    user-level state reset.

-   `void yypushStream(java.io.Reader reader)`\
    Stores the current input stream on a stack, and reads from a new
    stream. Lexical state, line, char, and column counting remain
    untouched. The current input stream can be restored with
    `yypopStream` (usually in an `<<EOF>>` action).

    A typical example for this are include files in style of the C
    pre-processor. The corresponding JFlex specification could look
    somewhat like this:

        "#include" {FILE}  { yypushStream(new FileReader(getFile(yytext()))); }
        ..
        <<EOF>>        { if (yymoreStreams()) yypopStream(); else return EOF; }

    This method is only available in the skeleton file
    `skeleton.nested`. You can find it in the `src` directory of the
    JFlex distribution.

-   `void yypopStream()`\
    Closes the current input stream and continues to read from the one
    on top of the stream stack.

    This method is only available in the skeleton file
    `skeleton.nested`. You can find it in the `src` directory of the
    JFlex distribution.

-   `boolean yymoreStreams()`\
    Returns true iff there are still streams for `yypopStream` left to
    read from on the stream stack.

    This method is only available in the skeleton file
    `skeleton.nested`. You can find it in the `src` directory of the
    JFlex distribution.

-   `int yystate()`\
    returns the current lexical state of the scanner.

-   `void yybegin(int lexicalState)`\
    enters the lexical state `lexicalState`

-   `void yypushback(int number)`\
    pushes `number` characters of the matched text back into the input
    stream. They will be read again in the next call of the scanning
    method. The number of characters to be read again must not be
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

-   `int yyline`\
    contains the current line of input (starting with 0, only active
    with the `lineCounting` directive)

-   `int yychar`\
    contains the current character count in the input (starting with 0,
    only active with the `charCounting` directive)

-   `int yycolumn`\
    contains the current column of the current line (starting with 0,
    only active with the `columnCounting` directive)

Encodings, Platforms, and Unicode {#sec:encodings}
=================================

This section tries to shed some light on the issues of Unicode and
encodings, cross platform scanning, and how to deal with binary data. My
thanks go to Stephen Ostermiller for his input on this topic.

The Problem {#sec:howtoencoding}
-----------

Before we dive straight into details, let’s take a look at what the
problem is. The problem is Java’s platform independence when you want to
use it. For scanners the interesting part about platform independence is
character encodings and how they are handled.

If a program reads a file from disk, it gets a stream of bytes. In
earlier times, when the grass was green, and the world was much simpler,
everybody knew that the byte value 65 is, of course, an A. It was no
problem to see which bytes meant which characters (actually these times
never existed, but anyway). The normal Latin alphabet only has 26
characters, so 7 bits or 128 distinct values should surely be enough to
map them, even if you allow yourself the luxury of upper and lower case.
Nowadays, things are different. The world suddenly grew much larger, and
all kinds of people wanted all kinds of special characters, just because
they use them in their language and writing. This is were the mess
starts. Since the 128 distinct values were already filled up with other
stuff, people began to use all 8 bits of the byte, and extended the
byte/character mappings to fit their need, and of course everybody did
it differently. Some people for instance may have said “let’s use the
value 213 for the German character <span>ä</span>”. Others may have
found that 213 should much rather mean <span>é</span>, because they
didn’t need German and wrote French instead. As long as you use your
program and data files only on one platform, this is no problem, as all
know what means what, and everything gets used consistently.

Now Java comes into play, and wants to run everywhere (once written,
that is) and now there suddenly is a problem: how do I get the same
program to say <span>ä</span> to a certain byte when it runs in Germany
and maybe <span>é</span> when it runs in France? And also the other way
around: when I want to say <span>é</span> on the screen, which byte
value should I send to the operating system?

Java’s solution to this is to use Unicode internally. Unicode aims to be
a superset of all known character sets and is therefore a perfect base
for encoding things that might get used all over the world. To make
things work correctly, you still have to know where you are and how to
map byte values to Unicode characters and vice versa, but the important
thing is, that this mapping is at least possible (you can map Kanji
characters to Unicode, but you cannot map them to ASCII or iso-latin-1).

Scanning text files {#sec:howtotext}
-------------------

Scanning text files is the standard application for scanners like JFlex.
Therefore it should also be the most convenient one. Most times it is.

The following scenario works like a breeze: You work on a platform X,
write your lexer specification there, can use any obscure Unicode
character in it as you like, and compile the program. Your users work on
any platform Y (possibly but not necessarily something different from
X), they write their input files on Y and they run your program on Y. No
problems.

Java does this as follows: If you want to read anything in Java that is
supposed to contain text, you use a `FileReader` or some `InputStream`
together with an `InputStreamReader`. `InputStreams` return the raw
bytes, the `InputStreamReader` converts the bytes into Unicode
characters with the platform’s default encoding. If a text file is
produced on the same platform, the platform’s default encoding should do
the mapping correctly. Since JFlex also uses readers and Unicode
internally, this mechanism also works for the scanner specifications. If
you write an `A` in your text editor and the editor uses the platform’s
encoding (say `A` is 65), then Java translates this into the logical
Unicode `A` internally. If a user writes an `A` on a completely
different platform (say `A` is 237 there), then Java also translates
this into the logical Unicode `A` internally. Scanning is performed
after that translation and both match.

Note that because of this mapping from bytes to characters, you should
always use the `%unicode` switch in you lexer specification if you want
to scan text files. `%8bit` may not be enough, even if you know that
your platform only uses one byte per character. The encoding Cp1252 used
on many Windows machines for instance knows 256 characters, but the
character <span>'</span> with Cp1252 code `\x92` has the Unicode value
`\u2019`, which is larger than 255 and which would make your scanner
throw an `ArrayIndexOutOfBoundsException` if it is encountered.

So for the usual case you don’t have to do anything but use the
`%unicode` switch in your lexer specification.

Things may break when you produce a text file on platform X and consume
it on a different platform Y. Let’s say you have a file written on a
Windows PC using the encoding Cp1252. Then you move this file to a Linux
PC with encoding ISO 8859-1 and there you want to run your scanner on
it. Java now thinks the file is encoded in ISO 8859-1 (the platform’s
default encoding) while it really is encoded in Cp1252. For most
characters Cp1252 and ISO 8859-1 are the same, but for the byte values
`\x80` to `\x9f` they disagree: ISO 8859-1 is undefined there. You can
fix the problem by telling Java explicitly which encoding to use. When
constructing the `InputStreamReader`, you can give the encoding as
argument. The line

`Reader r = new InputStreamReader(input, Cp1252); `

will do the trick.

Of course the encoding to use can also come from the data itself: for
instance, when you scan an HTML page, it may have embedded information
about its character encoding in the headers.

More information about encodings, which ones are supported, how they are
called, and how to set them may be found in the official Java
documentation in the chapter about internationalisation. The link
<span>[`http://docs.oracle.com/javase/1.5.0/docs/guide/intl/`](http://docs.oracle.com/javase/1.5.0/docs/guide/intl/)</span>
leads to an online version of this for Oracle’s JDK 1.5.

Scanning binaries {#sec:howtobinary}
-----------------

Scanning binaries is both easier and more difficult than scanning text
files. It’s easier because you want the raw bytes and not their meaning,
i.e. you don’t want any translation. It’s more difficult because it’s
not so easy to get “no translation” when you use Java readers.

The problem (for binaries) is that JFlex scanners are designed to work
on text. Therefore the interface is the `Reader` class (there is a
constructor for `InputStream` instances, but it’s just there for
convenience and wraps an `InputStreamReader` around it to get
characters, not bytes). You can still get a binary scanner when you
write your own custom `InputStreamReader` class that does explicitly no
translation, but just copies byte values to character codes instead. It
sounds quite easy, and actually it is no big deal, but there are a few
little pitfalls on the way. In the scanner specification you can only
enter positive character codes (for bytes that is `\x00` to `\xFF`).
Java’s `byte` type on the other hand is a signed 8 bit integer (-128 to
127), so you have to convert them properly in your custom `Reader`.
Also, you should take care when you write your lexer spec: if you use
text in there, it gets interpreted by an encoding first, and what
scanner you get as result might depend on which platform you run JFlex
on when you generate the scanner (this is what you want for text, but
for binaries it gets in the way). If you are not sure, or if the
development platform might change, it’s probably best to use character
code escapes in all places, since they don’t change their meaning.

Conformance with Unicode Regular Expressions UTS\#18 {#unicoderegexconformance}
====================================================

This section gives details about JFlex <span>1.7.0-SNAPSHOT</span>’s
conformance with the requirements for Basic Unicode Support Level 1
given in UTS\#18 @unicode_rep.

### RL1.1 Hex Notation {#rl1.1-hex-notation .unnumbered}

> *To meet this requirement, an implementation shall supply a mechanism
> for specifying any Unicode code point (from U+0000 to U+10FFFF), using
> the hexadecimal code point representation.*

JFlex conforms. Syntax is provided to express values across the whole
range, via `\uXXXX`, where `XXXX` is a 4-digit hex value; `\Uyyyyyy`,
where `yyyyyy` is a 6-digit hex value; and `\u{X+( X+)*}`, where `X+` is
a 1-6 digit hex value.

### RL1.2 Properties {#rl1.2-properties .unnumbered}

> *To meet this requirement, an implementation shall provide at least a
> minimal list of properties, consisting of the following:
> General\_Category, Script and Script\_Extensions, Alphabetic,
> Uppercase, Lowercase, White\_Space, Noncharacter\_Code\_Point,
> Default\_Ignorable\_Code\_Point, ANY, ASCII, ASSIGNED.*
>
> *The values for these properties must follow the Unicode definitions,
> and include the property and property value aliases from the UCD.
> Matching of Binary, Enumerated, Catalog, and Name values, must follow
> the Matching Rules from [UAX44].*

JFlex conforms. The minimal set of properties is supported, as well as a
few others. To see the full list of supported properties, use the JFlex
command line option `--uniprops <ver>`, where `<ver>` is the Unicode
version. Loose matching is performed: case distinctions, whitespace,
underscores and hyphens in property names and values are ignored.

### RL1.2a Compatibility Properties {#rl1.2a-compatibility-properties .unnumbered}

> *To meet this requirement, an implementation shall provide the
> properties listed in Annex C: Compatibility Properties, with the
> property values as listed there. Such an implementation shall document
> whether it is using the Standard Recommendation or POSIX-compatible
> properties.*

JFlex does not fully conform. The Standard Recommendation version of the
Annex C Compatibility Properties are provided, with two exceptions: `\X`
Extended Grapheme Clusters; and `\b` Default Word Boundaries.

### RL1.3 Subtraction and Intersection {#rl1.3-subtraction-and-intersection .unnumbered}

> *To meet this requirement, an implementation shall supply mechanisms
> for union, intersection and set-difference of Unicode sets.*

JFlex conforms by providing these mechanisms, as well as symmetric
difference.

### RL1.4 Simple Word Boundaries {#rl1.4-simple-word-boundaries .unnumbered}

> *To meet this requirement, an implementation shall extend the word
> boundary mechanism so that:*
>
> 1.  *The class of `<word_character>` includes all the Alphabetic
>     values from the Unicode character database, from UnicodeData.txt
>     [UData], plus the decimals (General\_Category=Decimal\_Number, or
>     equivalently Numeric\_Type=Decimal), and the U+200C ZERO WIDTH
>     NON-JOINER and U+200D ZERO WIDTH JOINER (Join\_Control=True). See
>     also Annex C: Compatibility Properties.*
>
> 2.  *Nonspacing marks are never divided from their base characters,
>     and otherwise ignored in locating boundaries.*
>
JFlex does not conform: `\b` does not match simple word boundaries.

### RL1.5 Simple Loose Matches {#rl1.5-simple-loose-matches .unnumbered}

> *To meet this requirement, if an implementation provides for
> case-insensitive matching, then it shall provide at least the simple,
> default Unicode case-insensitive matching, and specify which
> properties are closed and which are not.*
>
> *To meet this requirement, if an implementation provides for case
> conversions, then it shall provide at least the simple, default
> Unicode case folding.*

JFlex conforms. All supported Unicode Properties are closed.

### RL1.6 Line Boundaries {#rl1.6-line-boundaries .unnumbered}

> *To meet this requirement, if an implementation provides for
> line-boundary testing, it shall recognize not only CRLF, LF, CR, but
> also NEL (U+0085), PARAGRAPH SEPARATOR (U+2029) and LINE SEPARATOR
> (U+2028).*

JFlex conforms.

### RL1.7 Supplementary Code Points {#rl1.7-supplementary-code-points .unnumbered}

> *To meet this requirement, an implementation shall handle the full
> range of Unicode code points, including values from U+FFFF to
> U+10FFFF. In particular, where UTF-16 is used, a sequence consisting
> of a leading surrogate followed by a trailing surrogate shall be
> handled as a single code point in matching.*

JFlex conforms.

A few words on performance {#performance}
==========================

This section gives some tips on how to make your specification produce a
faster scanner.

Although JFlex generated scanners show good performance without special
optimisations, there are some heuristics that can make a lexical
specification produce an even faster scanner. Those are (roughly in
order of performance gain):

-   Avoid rules that require backtracking

    From the C/C++ flex @flex man page: <span><span>**</span>“Getting
    rid of backtracking is messy and often may be an enormous amount of
    work for a complicated scanner.”</span> Backtracking is introduced
    by the longest match rule and occurs for instance on this set of
    expressions:

    `  averylongkeyword`\
    `  .`

    With input `averylongjoke` the scanner has to read all characters up
    to `’j’` to decide that rule `.` should be matched. All characters
    of `verylong` have to be read again for the next matching process.
    Backtracking can be avoided in general by adding error rules that
    match those error conditions

    ` "av"|"ave"|"avery"|"averyl"|..`

    While this is impractical in most scanners, there is still the
    possibility to add a “catch all” rule for a lengthy list of keywords

        "keyword1"  { return symbol(KEYWORD1); } 
        .. 
        "keywordn"  { return symbol(KEYWORDn); }
        [a-z]+      { error("not a keyword"); }

    Most programming language scanners already have a rule like this for
    some kind of variable length identifiers.

-   Avoid line and column counting

    It costs multiple additional comparisons per input character and the
    matched text has to be re-scanned for counting. In most scanners it
    is possible to do the line counting in the specification by
    incrementing `yyline` each time a line terminator has been matched.
    Column counting could also be included in actions. This will be
    faster, but can in some cases become quite messy.

-   Avoid look-ahead expressions and the end of line operator ’\$’

    In the best case, the trailing context will first have to be read
    and then (because it is not to be consumed) re-read again. The cases
    of fixed-length look-ahead and fixed-length base expressions are
    handled efficiently by matching the concatenation and then pushing
    back the required amount of characters. This extends to the case of
    a disjunction of fixed-length look-ahead expressions such as
    `r1 / \r|\n|\r\n`. All other cases `r1 / r2` are handled by first
    scanning the concatenation of `r1` and `r2`, and then finding the
    correct end of `r1`. The end of `r1` is found by scanning forwards
    in the match again, marking all possible `r1` terminations, and then
    scanning the reverse of `r2` backwards from the end until a start of
    `r2` intersects with an end of `r1`. This algorithm is linear in the
    size of the input (not quadratic or worse as backtracking is), but
    about a factor of 2 slower than normal scanning. It also consumes
    memory proportional to the size of the matched input for `r1 r2`.

-   Avoid the beginning of line operator ’`^`’

    It costs multiple additional comparisons per match. In some cases
    one extra look-ahead character is needed (when the last character
    read is `\r`, the scanner has to read one character ahead to check
    if the next one is an `\n` or not).

-   Match as much text as possible in a rule.

    One rule is matched in the innermost loop of the scanner. After each
    action some overhead for setting up the internal state of the
    scanner is necessary.

Note that writing more rules in a specification does not make the
generated scanner slower.

The two main rules of optimisation apply also for lexical
specifications:

1.  **don’t do it**

2.  **(for experts only) don’t do it yet**

Some of the performance tips above contradict a readable and compact
specification style. When in doubt or when requirements are not or not
yet fixed: don’t use them — the specification can always be optimised in
a later state of the development process.

Porting Issues {#Porting}
==============

Porting from JLex
-----------------

JFlex was designed to read old JLex specifications unchanged and to
generate a scanner which behaves exactly the same as the one generated
by JLex with the only difference of being faster.

This works as expected on all well formed JLex specifications.

Since the statement above is somewhat absolute, let’s take a look at
what “well formed” means here. A JLex specification is well formed, when
it

-   generates a working scanner with JLex

-   doesn’t contain the unescaped characters `!` and ``

    They are operators in JFlex while JLex treats them as normal input
    characters. You can easily port such a JLex specification to JFlex
    by replacing every `!` with `\!` and every `~` with `\~` in all
    regular expressions.

-   has only complete regular expressions surrounded by parentheses in
    macro definitions

    This may sound a bit harsh, but could otherwise be a major problem –
    it can also help you find some disgusting bugs in your specification
    that didn’t show up in the first place. In JLex, a right hand side
    of a macro is just a piece of text, that is copied to the point
    where the macro is used. With this, some weird kind of stuff like

          macro1 = ("hello"
          macro2 = {macro1})*
          

    was possible (with `macro2` expanding to `("hello")*`). This is not
    allowed in JFlex and you will have to transform such definitions.
    There are however some more subtle kinds of errors that can be
    introduced by JLex macros. Let’s consider a definition like
    `macro = a|b` and a usage like `{macro}*`. This expands in JLex to
    `a|b*` and not to the probably intended `(a|b)*`.

    JFlex uses always the second form of expansion, since this is the
    natural form of thinking about abbreviations for regular
    expressions.

    Most specifications shouldn’t suffer from this problem, because
    macros often only contain (harmless) character classes like
    `alpha = [a-zA-Z]` and more dangerous definitions like

    ` ident = {alpha}({alpha}|{digit})*`

    are only used to write rules like

    ` {ident}       { .. action .. }`

    and not more complex expressions like

    ` {ident}*      { .. action .. }`

    where the kind of error presented above would show up.

Porting from lex/flex
---------------------

This section gives an incomplete overview of potential pitfalls and
steps for porting a lexical specification from the C/C++ tools `lex` and
`flex` @flex available on most Unix systems to JFlex.

Most of the C/C++ specific features are naturally not present in JFlex,
but most “clean” lex/flex lexical specifications can be ported to JFlex
without too much work.

### Basic structure

A lexical specification for flex has the following basic structure:

    definitions
    %%
    rules
    %%
    user code

The `user code` section usually contains C code that is used in actions
of the `rules` part of the specification. For JFlex, this code will have
to be translated to Java, and most of it will then go into the class
code `%{..%}` directive in the `options and declarations` section.

### Macros and Regular Expression Syntax

The `definitions` section of a flex specification is quite similar to
the `options and declarations` part of JFlex specs.

Macro definitions in flex have the form:

    <identifier>  <expression>

To port them to JFlex macros, just insert a `=` between `<identifier>`
and `<expression>`.

The syntax and semantics of regular expressions in flex are pretty much
the same as in JFlex. Some attention is needed for escape sequences
present in flex (such as `\a`) that are not supported in JFlex. These
escape sequences should be transformed into their octal or hexadecimal
equivalent.

### Character Classes

Flex offers the character classes directly supported by C, JFlex offers
the ones supported by Java. These classes will sometimes have to be
listed manually.

In flex more special characters lose their meaning in character classes.
In particular[^2]:

-   in flex `[][]` is the character class containing `]` and `[`,
    whereas in JFlex, the expression means “empty expression” followed
    by “empty expression”. To get `]` and `[` in JFlex, use for instance
    `[\]\[]`.

-   the classes `[]` and `[^]` are illegal in flex, but have meaning in
    JFlex.

-   in flex `["]` is legal, in JFlex you need `[\"]`.

### Lexical Rules

Since flex is mostly Unix based, the ’`^`’ (beginning of line) and ’`$`’
(end of line) operators, consider the `\n` character as only line
terminator. This should usually not cause much problems, but you should
be prepared for occurrences of `\r` or `\r\n` or one of the characters
`\u2028`, `\u2029`, `\u000B`, `\u000C`, or `\u0085`. They are considered
to be line terminators in Unicode and therefore may not be consumed when
`^` or `$` is present in a rule.

Working together {#WorkingTog}
================

JFlex and CUP {#CUPWork}
-------------

One of the main design goals of JFlex was to make interfacing with the
parser generators CUP @CUP and CUP2 @CUP2 as easy as possible. This has
been done by providing the `cupCupMode` and `cup2CupMode` directives in
JFlex. However, each interface has two sides. This section concentrates
on the CUP side of the story.

### CUP2

Please refer to the CUP2 @CUP2 documentation, which provides
instructions on how to interface with JFlex. The CUP2 JFlex patch
provided there is not necessary any more for JFlex versions greater than
1.5.0.

### CUP version 0.10j and above

Since CUP version 0.10j, this has been simplified greatly by the new CUP
scanner interface `java_cup.runtime.Scanner`. JFlex lexers now implement
this interface automatically when the `cupCupMode` switch is used. There
are no special `parser code`, `init code` or `scan with` options any
more that you have to provide in your CUP parser specification. You can
just concentrate on your grammar.

If your generated lexer has the class name `Scanner`, the parser is
started from the main program like this:

    ...
      try {
        parser p = new parser(new Scanner(new FileReader(fileName)));
        Object result = p.parse().value;
      }
      catch (Exception e) {
    ...

### Custom symbol interface

If you have used the `-symbol` command line switch of CUP to change the
name of the generated symbol interface, you have to tell JFlex about
this change of interface so that correct end-of-file code is generated.
You can do so either by using an `%eofval{` directive or by using an
`<<EOF>>` rule.

If your new symbol interface is called `mysym` for example, the
corresponding code in the jflex specification would be either

    %eofval{
      return mysym.EOF;
    %eofval}

in the macro/directives section of the spec, or it would be

      <<EOF>>  { return mysym.EOF; }

in the rules section of your spec.

### Using existing JFlex/CUP specifications with CUP 0.10j

If you already have an existing specification and you would like to
upgrade both JFlex and CUP to their newest version, you will probably
have to adjust your specification.

The main difference between the `cupCupMode` switch in JFlex 1.2.1 and
lower, and the current JFlex version is, that JFlex scanners now
automatically implement the `java_cup.runtime.Scanner` interface. This
means the scanning function changes its name from `yylex()` to
`next_token()`.

The main difference from older CUP versions to 0.10j is, that CUP now
has a default constructor that accepts a `java_cup.runtime.Scanner` as
argument and that uses this scanner as default (so no `scan with` code
is necessary any more).

If you have an existing CUP specification, it will probably look
somewhat like this:

    parser code {:
      Lexer lexer;

      public parser (java.io.Reader input) {
        lexer = new Lexer(input);
      }
    :};

    scan with {: return lexer.yylex(); :};

To upgrade to CUP 0.10j, you could change it to look like this:

    parser code {:
      public parser (java.io.Reader input) {
        super(new Lexer(input));
      }
    :};

If you do not mind to change the method that is calling the parser, you
could remove the constructor entirely (and if there is nothing else in
it, the whole `parser code` section as well, of course). The calling
main procedure would then construct the parser as shown in the section
above.

The JFlex specification does not need to be changed.

JFlex and BYacc/J[YaccWork]
---------------------------

JFlex has built-in support for the Java extension
<span>[BYacc/J](http://byaccj.sourceforge.net/)</span> @BYaccJ by Bob
Jamison to the classical Berkeley Yacc parser generator. This section
describes how to interface BYacc/J with JFlex. It builds on many helpful
suggestions and comments from Larry Bell.

Since Yacc’s architecture is a bit different from CUP’s, the interface
setup also works in a slightly different manner. BYacc/J expects a
function `int yylex()` in the parser class that returns each next token.
Semantic values are expected in a field `yylval` of type `parserval`
where “`parser`” is the name of the generated parser class.

For a small calculator example, one could use a setup like the following
on the JFlex side:


    %%

    %byaccj

    %{
      /* store a reference to the parser object */
      private parser yyparser;

      /* constructor taking an additional parser object */
      public Yylex(java.io.Reader r, parser yyparser) {
        this(r);
        this.yyparser = yyparser;
      }
    %}

    NUM = [0-9]+ ("." [0-9]+)?
    NL  = \n | \r | \r\n

    %%

    /* operators */
    "+" | 
    ..
    "(" | 
    ")"    { return (int) yycharat(0); }

    /* newline */
    {NL}   { return parser.NL; }

    /* float */
    {NUM}  { yyparser.yylval = new parserval(Double.parseDouble(yytext()));
             return parser.NUM; }

The lexer expects a reference to the parser in its constructor. Since
Yacc allows direct use of terminal characters like `’+’` in its
specifications, we just return the character code for single char
matches (e.g. the operators in the example). Symbolic token names are
stored as `public static int` constants in the generated parser class.
They are used as in the `NL` token above. Finally, for some tokens, a
semantic value may have to be communicated to the parser. The `NUM` rule
demonstrates that bit.

A matching BYacc/J parser specification could look like this:

    %{
      import java.io.*;
    %}
          
    %token NL          /* newline  */
    %token <dval> NUM  /* a number */

    %type <dval> exp

    %left '-' '+'
    ..
    %right '^'         /* exponentiation */
          
    %%

    ..
          
    exp:     NUM          { $$ = $1; }
           | exp '+' exp  { $$ = $1 + $3; }
           ..
           | exp '^' exp  { $$ = Math.pow($1, $3); }
           | '(' exp ')'  { $$ = $2; }
           ;

    %%
      /* a reference to the lexer object */
      private Yylex lexer;

      /* interface to the lexer */
      private int yylex () {
        int yyl_return = -1;
        try {
          yyl_return = lexer.yylex();
        }
        catch (IOException e) {
          System.err.println("IO error :"+e);
        }
        return yyl_return;
      }

      /* error reporting */
      public void yyerror (String error) {
        System.err.println ("Error: " + error);
      }

      /* lexer is created in the constructor */
      public parser(Reader r) {
        lexer = new Yylex(r, this);
      }

      /* that's how you use the parser */
      public static void main(String args[]) throws IOException {
        parser yyparser = new parser(new FileReader(args[0]));
        yyparser.yyparse();    
      }

Here, the customised part is mostly in the user code section: We create
the lexer in the constructor of the parser and store a reference to it
for later use in the parser’s `int yylex()` method. This `yylex` in the
parser only calls `int yylex()` of the generated lexer and passes the
result on. If something goes wrong, it returns -1 to indicate an error.

Runnable versions of the specifications above are located in the
`examples/byaccj` directory of the JFlex distribution.

JFlex and Jay
-------------

Combining JFlex with the [Jay Parser
Generator](http://www.cs.rit.edu/~ats/projects/lp/doc/jay/package-summary.html)
@Jay is quite simple. The Jay Parser Generator defines an interface
called `<parsername>.yyInput`. In the JFlex source the directive

    %implements <parsername>.yyInput

tells JFlex to generate the corresponding class declaration.

The three interface methods to implement are

-   `advance()` which should return a boolean that is `true` if there is
    more work to do and `false` if the end of input has been reached,

-   `token()` which returns the last scanned token, and

-   `value()` which returns an Object that contains the (optional) value
    of the last read token.

The following shows a small example with Jay parser specification and
corresponding JFlex code. First of all the Jay code (in a file
`MiniParser.jay`):

    %{
    //
    // Prefix Code like Package declaration, 
    // imports, variables and the parser class declaration
    // 

    import java.io.*;
    import java.util.*;

    public class MiniParser 
    {

    %}

    // Token declarations, and types of non-terminals

    %token DASH COLON
    %token <Integer> NUMBER

    %token <String> NAME

    %type <Gameresult> game
    %type <Vector<Gameresult>> gamelist

    // start symbol
    %start gamelist

    %%

    gamelist: game        { $$ = new Vector<Gameresult>();
                            $<Vector<Gameresult>>$.add($1);
                          }
      |  gamelist game    { $1.add($2); }

    game: NAME DASH NAME NUMBER COLON NUMBER {
          $$ = new Gameresult($1, $3, $4, $6); }

    %%

      // supporting methods part of the parser class
      public static void main(String argv[])
      {
        MiniScanner scanner = new MiniScanner(new InputStreamReader(System.in));
        MiniParser parser = new MiniParser();
        try {
          parser.yyparse (scanner);
        } catch (final IOException ioe) {
          System.out.println("I/O Exception : " + ioe.toString());
        } catch (final MiniParser.yyException ye) {
          System.out.println ("Oops : " + ye.toString());
        }
      }

    } // closing brace for the parser class

    class Gameresult {
      String homeTeam;
      String outTeam;
      Integer homeScore;
      Integer outScore;

      public Gameresult(String ht, String ot, Integer hs, Integer os)
      {
        homeTeam = ht;
        outTeam = ot;
        homeScore = hs;
        outScore = os;
      }
    }

The corresponding JFlex code (MiniScanner.jflex) could be

    %%

    %public
    %class MiniScanner
    %implements MiniParser.yyInput
    %integer

    %line
    %column
    %unicode

    %{
    private int token;
    private Object value;

    // the next 3 methods are required to implement the yyInput interface

    public boolean advance() throws java.io.IOException {
      value = new String("");
      token = yylex();
      return (token != YYEOF);
    }

    public int token() {
      return token;
    }

    public Object value() {
      return value;
    }

    %}

    nl =     [\n\r]+
    ws =     [ \t\b\015]+
    number = [0-9]+
    name =   [a-zA-Z]+
    dash =   "-"
    colon =  ":"

    %%

    {nl}      { /* do nothing */ }
    {ws}      { /* happy meal */ }
    {name}    { value = yytext(); return MiniParser.NAME; }
    {dash}    { return MiniParser.DASH; }
    {colon}   { return MiniParser.COLON; }
    {number}  { try  {
                  value = new Integer(Integer.parseInt(yytext()));
                } catch (NumberFormatException nfe) {
                  // shouldn't happen
                  throw new Error();
                }
                return MiniParser.NUMBER;
              }

This small example reads an input like

    Borussia - Schalke 3:2
    ACMilano - Juventus 1:4

Bugs and Deficiencies {#Bugs}
=====================

Deficiencies
------------

JFlex <span>1.7.0-SNAPSHOT</span> conforms with Unicode Regular
Expressions UTS\#18 @unicode_rep Basic Unicode Support Level 1, with a
few exceptions - for details see <span>[UTS\#18
Conformance](#unicoderegexconformance)</span>.

Bugs
----

As of , no major open problems are known for JFlex version
<span>1.7.0-SNAPSHOT</span>.

Please use the bugs section of the to check for open issues.

Copying and License {#Copyright}
===================

JFlex is free software, published under a BSD-style license.

There is absolutely NO WARRANTY for JFlex, its code and its
documentation.

See the file <span>[`COPYRIGHT`](COPYRIGHT)</span> for more information.

[^1]: Java is a trademark of Sun Microsystems, Inc., and refers to Sun’s
    Java programming language. JFlex is not sponsored by or affiliated
    with Sun Microsystems, Inc.

[^2]: Thanks to Dimitri Maziuk for pointing these out.
