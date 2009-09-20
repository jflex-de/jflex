%%

%unicode 5.0
%public
%class UnicodeDerivedCoreProperties_XID_Continue_5_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{XID_Continue} { setCurCharBlock(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
