%%

%unicode 3.2
%public
%class UnicodeCompatibilityProperties_graph_3_2

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{graph} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
