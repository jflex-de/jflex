%%

%unicode 5.1
%public
%class UnicodeBlocks_f

%type int
%standalone

%include src/test/cases/unicode-blocks/common-unicode-blocks-java

%% 

\p{General Category:This will never be a general category property name} { setCurCharBlock("Not a general category property name"); }

<<EOF>> { printOutput(); return 1; }
