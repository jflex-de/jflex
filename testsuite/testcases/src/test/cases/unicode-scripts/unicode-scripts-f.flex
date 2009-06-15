%%

%unicode 5.1
%public
%class UnicodeScripts_f

%type int
%standalone

%include src/test/cases/unicode-scripts/common-unicode-scripts-java

%% 

\p{Script:This will never be a script name} { setCurCharBlock("Never a script name"); }

<<EOF>> { printOutput(); return 1; }
