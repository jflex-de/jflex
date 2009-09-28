%%

%unicode 3.2
%public
%class UnicodeLineBreak_3_2

%type int
%standalone

%include src/test/cases/unicode-line-break/common-unicode-line-break-java

%%

<<EOF>> { printOutput(); return 1; }
\p{LineBreak:AI} { setCurCharBlock("LineBreak:AI"); }
\p{LineBreak:AL} { setCurCharBlock("LineBreak:AL"); }
\p{LineBreak:B2} { setCurCharBlock("LineBreak:B2"); }
\p{LineBreak:BA} { setCurCharBlock("LineBreak:BA"); }
\p{LineBreak:BB} { setCurCharBlock("LineBreak:BB"); }
\p{LineBreak:BK} { setCurCharBlock("LineBreak:BK"); }
\p{LineBreak:CB} { setCurCharBlock("LineBreak:CB"); }
\p{LineBreak:CL} { setCurCharBlock("LineBreak:CL"); }
\p{LineBreak:CM} { setCurCharBlock("LineBreak:CM"); }
\p{LineBreak:CR} { setCurCharBlock("LineBreak:CR"); }
\p{LineBreak:EX} { setCurCharBlock("LineBreak:EX"); }
\p{LineBreak:GL} { setCurCharBlock("LineBreak:GL"); }
\p{LineBreak:HY} { setCurCharBlock("LineBreak:HY"); }
\p{LineBreak:ID} { setCurCharBlock("LineBreak:ID"); }
\p{LineBreak:IN} { setCurCharBlock("LineBreak:IN"); }
\p{LineBreak:IS} { setCurCharBlock("LineBreak:IS"); }
\p{LineBreak:LF} { setCurCharBlock("LineBreak:LF"); }
\p{LineBreak:NS} { setCurCharBlock("LineBreak:NS"); }
\p{LineBreak:NU} { setCurCharBlock("LineBreak:NU"); }
\p{LineBreak:OP} { setCurCharBlock("LineBreak:OP"); }
\p{LineBreak:PO} { setCurCharBlock("LineBreak:PO"); }
\p{LineBreak:PR} { setCurCharBlock("LineBreak:PR"); }
\p{LineBreak:QU} { setCurCharBlock("LineBreak:QU"); }
\p{LineBreak:SA} { setCurCharBlock("LineBreak:SA"); }
\p{LineBreak:SP} { setCurCharBlock("LineBreak:SP"); }
\p{LineBreak:SY} { setCurCharBlock("LineBreak:SY"); }
\p{LineBreak:XX} { setCurCharBlock("LineBreak:XX"); }
\p{LineBreak:ZW} { setCurCharBlock("LineBreak:ZW"); }
