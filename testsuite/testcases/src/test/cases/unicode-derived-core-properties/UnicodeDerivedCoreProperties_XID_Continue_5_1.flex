%%

%unicode 5.1
%public
%class UnicodeDerivedCoreProperties_XID_Continue_5_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{XID_Continue} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
