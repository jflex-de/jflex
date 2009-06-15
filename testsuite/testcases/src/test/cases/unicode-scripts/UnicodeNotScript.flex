%%

%unicode 3.1
%public
%class UnicodeNotScript

%type int
%standalone

%include src/test/cases/unicode-scripts/common-unicode-scripts-java 

%% 

\P{Canadian Aboriginal} { setCurCharBlock("Not Canadian Aboriginal"); }
\p{Canadian Aboriginal} { setCurCharBlock("Canadian Aboriginal"); }

<<EOF>> { printOutput(); return 1; }
