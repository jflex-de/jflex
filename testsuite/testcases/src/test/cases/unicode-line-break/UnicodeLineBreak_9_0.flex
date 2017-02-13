%%

%unicode 9.0
%public
%class UnicodeLineBreak_9_0

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\p{LineBreak:AI} { setCurCharPropertyValue("LineBreak:AI"); }
\p{LineBreak:AL} { setCurCharPropertyValue("LineBreak:AL"); }
\p{LineBreak:B2} { setCurCharPropertyValue("LineBreak:B2"); }
\p{LineBreak:BA} { setCurCharPropertyValue("LineBreak:BA"); }
\p{LineBreak:BB} { setCurCharPropertyValue("LineBreak:BB"); }
\p{LineBreak:BK} { setCurCharPropertyValue("LineBreak:BK"); }
\p{LineBreak:CB} { setCurCharPropertyValue("LineBreak:CB"); }
\p{LineBreak:CJ} { setCurCharPropertyValue("LineBreak:CJ"); }
\p{LineBreak:CL} { setCurCharPropertyValue("LineBreak:CL"); }
\p{LineBreak:CM} { setCurCharPropertyValue("LineBreak:CM"); }
\p{LineBreak:CP} { setCurCharPropertyValue("LineBreak:CP"); }
\p{LineBreak:CR} { setCurCharPropertyValue("LineBreak:CR"); }
\p{LineBreak:EB} { setCurCharPropertyValue("LineBreak:EB"); }
\p{LineBreak:EM} { setCurCharPropertyValue("LineBreak:EM"); }
\p{LineBreak:EX} { setCurCharPropertyValue("LineBreak:EX"); }
\p{LineBreak:GL} { setCurCharPropertyValue("LineBreak:GL"); }
\p{LineBreak:H2} { setCurCharPropertyValue("LineBreak:H2"); }
\p{LineBreak:H3} { setCurCharPropertyValue("LineBreak:H3"); }
\p{LineBreak:HL} { setCurCharPropertyValue("LineBreak:HL"); }
\p{LineBreak:HY} { setCurCharPropertyValue("LineBreak:HY"); }
\p{LineBreak:ID} { setCurCharPropertyValue("LineBreak:ID"); }
\p{LineBreak:IN} { setCurCharPropertyValue("LineBreak:IN"); }
\p{LineBreak:IS} { setCurCharPropertyValue("LineBreak:IS"); }
\p{LineBreak:JL} { setCurCharPropertyValue("LineBreak:JL"); }
\p{LineBreak:JT} { setCurCharPropertyValue("LineBreak:JT"); }
\p{LineBreak:JV} { setCurCharPropertyValue("LineBreak:JV"); }
\p{LineBreak:LF} { setCurCharPropertyValue("LineBreak:LF"); }
\p{LineBreak:NL} { setCurCharPropertyValue("LineBreak:NL"); }
\p{LineBreak:NS} { setCurCharPropertyValue("LineBreak:NS"); }
\p{LineBreak:NU} { setCurCharPropertyValue("LineBreak:NU"); }
\p{LineBreak:OP} { setCurCharPropertyValue("LineBreak:OP"); }
\p{LineBreak:PO} { setCurCharPropertyValue("LineBreak:PO"); }
\p{LineBreak:PR} { setCurCharPropertyValue("LineBreak:PR"); }
\p{LineBreak:QU} { setCurCharPropertyValue("LineBreak:QU"); }
\p{LineBreak:RI} { setCurCharPropertyValue("LineBreak:RI"); }
\p{LineBreak:SA} { setCurCharPropertyValue("LineBreak:SA"); }
\p{LineBreak:SP} { setCurCharPropertyValue("LineBreak:SP"); }
\p{LineBreak:SY} { setCurCharPropertyValue("LineBreak:SY"); }
\p{LineBreak:WJ} { setCurCharPropertyValue("LineBreak:WJ"); }
\p{LineBreak:XX} { setCurCharPropertyValue("LineBreak:XX"); }
\p{LineBreak:ZW} { setCurCharPropertyValue("LineBreak:ZW"); }
\p{LineBreak:ZWJ} { setCurCharPropertyValue("LineBreak:ZWJ"); }
