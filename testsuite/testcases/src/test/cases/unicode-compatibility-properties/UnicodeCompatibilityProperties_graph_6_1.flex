%%

%unicode 6.1
%public
%class UnicodeCompatibilityProperties_graph_6_1

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{graph} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
