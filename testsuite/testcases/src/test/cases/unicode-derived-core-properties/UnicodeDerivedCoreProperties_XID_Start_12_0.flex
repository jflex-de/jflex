%%

%unicode 12.0
%public
%class UnicodeDerivedCoreProperties_XID_Start_12_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{XID_Start} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
