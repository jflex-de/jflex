%%

%unicode 11.0
%public
%class UnicodeCompatibilityProperties_graph_11_0

%type int
%standalone

%include ../../resources/common-unicode-all-binary-property-java

%%

\p{graph} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
