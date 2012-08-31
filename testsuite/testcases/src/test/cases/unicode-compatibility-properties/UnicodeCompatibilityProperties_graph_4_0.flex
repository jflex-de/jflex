%%

%unicode 4.0
%public
%class UnicodeCompatibilityProperties_graph_4_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{graph} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
