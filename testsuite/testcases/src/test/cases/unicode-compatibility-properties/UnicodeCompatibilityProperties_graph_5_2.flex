%%

%unicode 5.2
%public
%class UnicodeCompatibilityProperties_graph_5_2

%type int
%standalone

%include ../../resources/common-unicode-binary-property-java

%%

\p{graph} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
