%%

%unicode 2.0
%public
%class UnicodeCompatibilityProperties_graph_2_0

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{graph} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
