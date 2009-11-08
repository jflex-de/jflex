%%

%unicode 5.1
%public
%class UnicodeBlocks_f

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%% 

\p{General Category:This will never be a general category property name} { setCurCharPropertyValue("Not a general category property name"); }

<<EOF>> { printOutput(); return 1; }
