%%

%unicode 3.1
%public
%class UnicodeDerivedCoreProperties_XID_Continue_3_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{XID_Continue} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
