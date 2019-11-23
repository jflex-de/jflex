%%

%unicode 11.0
%public
%class UnicodeDerivedCoreProperties_XID_Start_11_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{XID_Start} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
