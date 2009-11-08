%%

%unicode 5.1
%public
%class UnicodeBlocks_f

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%% 

\p{Block:This will never be a block name} { setCurCharPropertyValue("Alphabetic Presentation Forms"); }

<<EOF>> { printOutput(); return 1; }
