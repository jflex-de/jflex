%%

%unicode 12.1
%public
%class UnicodeDerivedCoreProperties_XID_Start_12_1

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{XID_Start} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
