%%

%unicode 6.3
%public
%class UnicodeDerivedCoreProperties_XID_Continue_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{XID_Continue} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
