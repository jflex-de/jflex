%%

%unicode 9.0
%public
%class UnicodeAge_9_0_age_subtraction

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-defined-values-only-java

%%

<<EOF>> { printOutput(); return 1; }
[\p{Age:2.0}--\p{Age:1.1}] { setCurCharPropertyValue("[\\p{Age:2.0}--\\p{Age:1.1}]"); }
[\p{Age:2.1}--\p{Age:2.0}] { setCurCharPropertyValue("[\\p{Age:2.1}--\\p{Age:2.0}]"); }
[\p{Age:3.0}--\p{Age:2.1}] { setCurCharPropertyValue("[\\p{Age:3.0}--\\p{Age:2.1}]"); }
[\p{Age:3.1}--\p{Age:3.0}] { setCurCharPropertyValue("[\\p{Age:3.1}--\\p{Age:3.0}]"); }
[\p{Age:3.2}--\p{Age:3.1}] { setCurCharPropertyValue("[\\p{Age:3.2}--\\p{Age:3.1}]"); }
[\p{Age:4.0}--\p{Age:3.2}] { setCurCharPropertyValue("[\\p{Age:4.0}--\\p{Age:3.2}]"); }
[\p{Age:4.1}--\p{Age:4.0}] { setCurCharPropertyValue("[\\p{Age:4.1}--\\p{Age:4.0}]"); }
[\p{Age:5.0}--\p{Age:4.1}] { setCurCharPropertyValue("[\\p{Age:5.0}--\\p{Age:4.1}]"); }
[\p{Age:5.1}--\p{Age:5.0}] { setCurCharPropertyValue("[\\p{Age:5.1}--\\p{Age:5.0}]"); }
[\p{Age:5.2}--\p{Age:5.1}] { setCurCharPropertyValue("[\\p{Age:5.2}--\\p{Age:5.1}]"); }
[\p{Age:6.0}--\p{Age:5.2}] { setCurCharPropertyValue("[\\p{Age:6.0}--\\p{Age:5.2}]"); }
[\p{Age:6.1}--\p{Age:6.0}] { setCurCharPropertyValue("[\\p{Age:6.1}--\\p{Age:6.0}]"); }
[\p{Age:6.2}--\p{Age:6.1}] { setCurCharPropertyValue("[\\p{Age:6.2}--\\p{Age:6.1}]"); }
[\p{Age:6.3}--\p{Age:6.2}] { setCurCharPropertyValue("[\\p{Age:6.3}--\\p{Age:6.2}]"); }
[\p{Age:7.0}--\p{Age:6.3}] { setCurCharPropertyValue("[\\p{Age:7.0}--\\p{Age:6.3}]"); }
[\p{Age:8.0}--\p{Age:7.0}] { setCurCharPropertyValue("[\\p{Age:8.0}--\\p{Age:7.0}]"); }
[\p{Age:9.0}--\p{Age:8.0}] { setCurCharPropertyValue("[\\p{Age:9.0}--\\p{Age:8.0}]"); }
[^] { }
