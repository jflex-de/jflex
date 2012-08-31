%%

%unicode 3.1
%public
%class UnicodeNotScript

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%% 

\P{Canadian Aboriginal} { setCurCharPropertyValue("Not Canadian Aboriginal"); }
\p{Canadian Aboriginal} { setCurCharPropertyValue("Canadian Aboriginal"); }

<<EOF>> { printOutput(); return 1; }
