%%

%unicode 5.0
%public
%class UnicodeCompatibilityProperties_graph_5_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{graph} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
