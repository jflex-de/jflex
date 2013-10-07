%%

%unicode 6.3
%public
%class UnicodeCompatibilityProperties_graph_6_3

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{graph} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
