%%

%unicode 9.0
%public
%class UnicodeCompatibilityProperties_graph_9_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{graph} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
