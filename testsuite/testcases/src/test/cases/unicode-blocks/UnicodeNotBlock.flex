%%

%unicode 5.1
%public
%class UnicodeNotBlock

%type int
%standalone

%include src/test/cases/unicode-blocks/common-unicode-blocks-java 

%% 

\P{Block:Latin Extended Additional} { setCurCharBlock("Not Latin Extended Additional"); }
\p{Block:Latin Extended Additional} { setCurCharBlock("Latin Extended Additional"); }

<<EOF>> { printOutput(); return 1; }
