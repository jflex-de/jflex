%%

%unicode 10.0
%public
%class UnicodeCompatibilityProperties_graph_10_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{graph} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
