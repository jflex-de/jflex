package edu.tum.cup2.spec;

/*
 * The Hyacc example grammars. Likely to be "true" LR(1)
 * see: http://hyacc.sourceforge.net/
 */

///* http://www.gnu.org/software/bison/manual/html_mono/bison.html.gz */
///* Infix notation calculator.  */
// 
//%{
//       #define YYSTYPE double
//       #include <math.h>
//       #include <stdio.h>
//       #include <stdlib.h>
//       #include <ctype.h>
//       int yylex (void);
//       void yyerror (char const *);
//       char * cursor = "#";
//%}
//     
///* Bison declarations.  */
//%token NUM
//%left '-' '+'
//%left '*' '/'
//%left NEG     /* negation--unary minus */
//%right '^'    /* exponentiation */
//    
//%% /* Grammar rules and actions follow.  */
//
//     input:    /* empty */
//             | input line
//     ;
//     
//     line:     '\n'          { printf ("\n%s ", cursor);   }
//             | exp '\n'      { printf ("\t%.10g\n%s ", $1, cursor); }
//             | error '\n'    { yyerrok;                  }
//     ;
//     
//     exp:      NUM                { $$ = $1;         }
//             | exp '+' exp        { $$ = $1 + $3;    }
//             | exp '-' exp        { $$ = $1 - $3;    }
//             | exp '*' exp        { $$ = $1 * $3;    }
//             | exp '/' exp        { $$ = $1 / $3;    }
//             | '-' exp  %prec NEG { $$ = -$2;        }
//             | exp '^' exp        { $$ = pow ($1, $3); }
//             | '(' exp ')'        { $$ = $2;         }
//     ;
//%%
//
//     /* The lexical analyzer returns a double floating point
//        number on the stack and the token NUM, or the numeric code
//        of the character read if not a number.  It skips all blanks
//        and tabs, and returns 0 for end-of-input.  */
//     
//     #include <ctype.h>
//     
//     int
//     yylex (void)
//     {
//       int c;
//     
//       /* Skip white space.  */
//       while ((c = getchar ()) == ' ' || c == '\t')
//         ;
//       /* Process numbers.  */
//       if (c == '.' || isdigit (c))
//         {
//           ungetc (c, stdin);
//           //ungetc (c);
//           scanf ("%lf", &yylval);
//           return NUM;
//         }
//       /* Return end-of-input.  */
//       if (c == EOF)
//         return 0;
//       /* Return a single char.  */
//       return c;
//     }
//
//     int
//     main (void)
//     {
//       printf("%s ", cursor);
//       return yyparse ();
//     }
//
//     #include <stdio.h>
//     
//     /* Called by yyparse on error.  */
//     void
//     yyerror (char const *s)
//     {
//       fprintf (stderr, "%s\n%s", s, cursor);
//     }
//
//*/



public class The_Hyacc_Examples
{

}
