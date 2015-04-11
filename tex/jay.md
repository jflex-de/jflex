JFlex and Jay
-------------

Combining JFlex with the [Jay Parser Generator][Jay] @Jay is quite
simple. The Jay Parser Generator defines an interface called
`<parsername>.yyInput`. In the JFlex source the directive

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


[Jay]: http://www.cs.rit.edu/~ats/projects/lp/doc/jay/package-summary.html