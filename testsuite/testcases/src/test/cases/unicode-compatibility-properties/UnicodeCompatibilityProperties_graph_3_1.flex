%%

%unicode 3.1
%public
%class UnicodeCompatibilityProperties_graph_3_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{graph} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
