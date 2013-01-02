%%

%unicode 6.2
%public
%class UnicodeDerivedCoreProperties_XID_Start_6_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{XID_Start} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
