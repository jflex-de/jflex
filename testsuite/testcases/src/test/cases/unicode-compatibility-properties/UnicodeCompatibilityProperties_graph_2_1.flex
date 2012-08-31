%%

%unicode 2.1
%public
%class UnicodeCompatibilityProperties_graph_2_1

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{graph} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
