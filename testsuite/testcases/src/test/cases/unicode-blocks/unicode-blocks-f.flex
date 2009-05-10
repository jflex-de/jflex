%%

%unicode 5.1
%public
%class UnicodeBlocks_f

%type int
%standalone

%include src/test/cases/unicode-blocks/common-unicode-blocks-java

%% 

\p{Block:This will never be a block name} { setCurCharBlock("Alphabetic Presentation Forms"); }

<<EOF>> { printOutput(); return 1; }
