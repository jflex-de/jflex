%%

%unicode 5.1
%public
%class UnicodeNotBlock

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%% 

\P{Block:Latin Extended Additional} { setCurCharPropertyValue("Not Latin Extended Additional"); }
\p{Block:Latin Extended Additional} { setCurCharPropertyValue("Latin Extended Additional"); }

<<EOF>> { printOutput(); return 1; }
