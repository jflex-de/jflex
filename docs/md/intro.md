Introduction {#Intro}
============

```
Ah 'she' says
```

JFlex is a lexical analyser generator for Java[^1] written in Java. It is
also a rewrite of the tool JLex [@JLex] which was developed by Elliot Berk at
Princeton University. As Vern Paxson states for his C/C++ tool flex [@flex]:
they do not share any code though.

A lexical analyser generator takes as input a specification with a set of
regular expressions and corresponding actions. It generates a program (a
*lexer*) that reads input, matches the input against the regular expressions
in the spec file, and runs the corresponding action if a regular expression
matched. Lexers usually are the first front-end step in compilers, matching
keywords, comments, operators, etc, and generating an input token stream for
parsers. They can also be used for many other purposes.


Design goals
------------

The main design goals of JFlex are:

-   **Unicode support**
-   **Fast generated scanners**
-   **Fast scanner generation**
-   **Convenient specification syntax**
-   **Platform independence**
-   **JLex compatibility**

About this manual
-----------------

This manual gives a brief but complete description of the tool JFlex. It
assumes that you are familiar with the topic of lexical analysis in parsing.
The references @Aho_SU_86 and @Appel_98 provide a good introduction.

The next section of this manual describes [installation
procedures](#Installing) for JFlex. [Working with JFlex - an example](#Example)
runs through an example specification and explains how it works. The section on
[Lexical specifications](#Specifications) presents all JFlex options and the
complete specification syntax; [Encodings, Platforms, and
Unicode](#sec:encodings) provides information about Unicode and scanning text
vs. binary files. [A few words on performance](#performance) gives tips on how
to write fast scanners. The section on [porting scanners](#Porting) shows how
to port scanners from JLex, and from the `lex` and `flex` tools for C. Finally,
[working together](#WorkingTog) discusses interfacing JFlex scanners with the
LALR parser generators CUP, CUP2, BYacc/J, Jay.



[^1]: Java is a trademark of Sun Microsystems, Inc., and refers to Sun’s
    Java programming language. JFlex is not sponsored by or affiliated
    with Sun Microsystems, Inc.

