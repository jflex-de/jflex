%%

%unicode 5.0
%public
%class UnicodeDerivedCoreProperties_XID_Start_5_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{XID_Start} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
