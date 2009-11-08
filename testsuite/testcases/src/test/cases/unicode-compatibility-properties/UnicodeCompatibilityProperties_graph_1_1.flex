%%

%unicode 1.1
%public
%class UnicodeCompatibilityProperties_graph_1_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{graph} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
