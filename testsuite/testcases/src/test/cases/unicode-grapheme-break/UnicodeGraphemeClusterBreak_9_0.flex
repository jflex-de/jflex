%%

%unicode 9.0
%public
%class UnicodeGraphemeClusterBreak_9_0

%type int
%standalone

%include ../../resources/common-unicode-all-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\p{GraphemeClusterBreak:CR} { setCurCharPropertyValue("GraphemeClusterBreak:CR"); }
\p{GraphemeClusterBreak:Control} { setCurCharPropertyValue("GraphemeClusterBreak:Control"); }
\p{GraphemeClusterBreak:E_Base} { setCurCharPropertyValue("GraphemeClusterBreak:E_Base"); }
\p{GraphemeClusterBreak:E_Base_GAZ} { setCurCharPropertyValue("GraphemeClusterBreak:E_Base_GAZ"); }
\p{GraphemeClusterBreak:E_Modifier} { setCurCharPropertyValue("GraphemeClusterBreak:E_Modifier"); }
\p{GraphemeClusterBreak:Extend} { setCurCharPropertyValue("GraphemeClusterBreak:Extend"); }
\p{GraphemeClusterBreak:Glue_After_Zwj} { setCurCharPropertyValue("GraphemeClusterBreak:Glue_After_Zwj"); }
\p{GraphemeClusterBreak:L} { setCurCharPropertyValue("GraphemeClusterBreak:L"); }
\p{GraphemeClusterBreak:LF} { setCurCharPropertyValue("GraphemeClusterBreak:LF"); }
\p{GraphemeClusterBreak:LV} { setCurCharPropertyValue("GraphemeClusterBreak:LV"); }
\p{GraphemeClusterBreak:LVT} { setCurCharPropertyValue("GraphemeClusterBreak:LVT"); }
\p{GraphemeClusterBreak:Other} { setCurCharPropertyValue("GraphemeClusterBreak:Other"); }
\p{GraphemeClusterBreak:Prepend} { setCurCharPropertyValue("GraphemeClusterBreak:Prepend"); }
\p{GraphemeClusterBreak:Regional_Indicator} { setCurCharPropertyValue("GraphemeClusterBreak:Regional_Indicator"); }
\p{GraphemeClusterBreak:SpacingMark} { setCurCharPropertyValue("GraphemeClusterBreak:SpacingMark"); }
\p{GraphemeClusterBreak:T} { setCurCharPropertyValue("GraphemeClusterBreak:T"); }
\p{GraphemeClusterBreak:V} { setCurCharPropertyValue("GraphemeClusterBreak:V"); }
\p{GraphemeClusterBreak:ZWJ} { setCurCharPropertyValue("GraphemeClusterBreak:ZWJ"); }
