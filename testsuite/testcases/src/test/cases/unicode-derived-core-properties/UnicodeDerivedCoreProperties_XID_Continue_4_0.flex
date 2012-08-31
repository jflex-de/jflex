%%

%unicode 4.0
%public
%class UnicodeDerivedCoreProperties_XID_Continue_4_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{XID_Continue} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
