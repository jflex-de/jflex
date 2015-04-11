Introduction {#Intro}
============

JFlex is a lexical analyser generator for Java[^1] written in Java. It is also
a rewrite of the tool JLex @JLex which was developed by Elliot Berk at
Princeton University. As Vern Paxson states for his C/C++ tool flex @flex: they
do not share any code though.

Design goals
------------

The main design goals of JFlex are:

-   **Full unicode support**
-   **Fast generated scanners**
-   **Fast scanner generation**
-   **Convenient specification syntax**
-   **Platform independence**
-   **JLex compatibility**

About this manual
-----------------

This manual gives a brief but complete description of the tool JFlex. It
assumes that you are familiar with the topic of lexical analysis in parsing.
The references @Aho_SU_86, @Appel_98, and @Wilhelm_Maurer_97 provide a good
introduction.

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

