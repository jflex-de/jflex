%%

%unicode 5.1
%public
%class UnicodeScripts_f

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%% 

\p{Script:This will never be a script name} { setCurCharPropertyValue("Never a script name"); }

<<EOF>> { printOutput(); return 1; }
