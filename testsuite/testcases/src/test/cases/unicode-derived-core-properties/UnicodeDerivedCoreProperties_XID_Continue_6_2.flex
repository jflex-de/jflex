%%

%unicode 6.2
%public
%class UnicodeDerivedCoreProperties_XID_Continue_6_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{XID_Continue} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
