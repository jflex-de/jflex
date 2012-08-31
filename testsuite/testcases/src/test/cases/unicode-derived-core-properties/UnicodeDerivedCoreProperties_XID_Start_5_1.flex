%%

%unicode 5.1
%public
%class UnicodeDerivedCoreProperties_XID_Start_5_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{XID_Start} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
