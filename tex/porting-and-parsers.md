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
`flex` [@flex] available on most Unix systems to JFlex.

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
parser generators CUP [@CUP] and CUP2 [@CUP2] as easy as possible. This has
been done by providing the `cupCupMode` and `cup2CupMode` directives in
JFlex. However, each interface has two sides. This section concentrates
on the CUP side of the story.

### CUP2

Please refer to the CUP2 [@CUP2] documentation, which provides
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
<span>[BYacc/J](http://byaccj.sourceforge.net/)</span> [@BYaccJ] by Bob
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
[@Jay] is quite simple. The Jay Parser Generator defines an interface
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



[^2]: Thanks to Dimitri Maziuk for pointing these out.

