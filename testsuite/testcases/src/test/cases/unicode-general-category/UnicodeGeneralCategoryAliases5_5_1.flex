%%

%unicode 5.1
%public
%class UnicodeGeneralCategoryAliases5_5_1

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\p{General_Category:Control} { setCurCharPropertyValue("Cc"); }
\p{General_Category:Format} { setCurCharPropertyValue("Cf"); }
\p{General_Category:Unassigned} { setCurCharPropertyValue("Cn"); }
\p{General_Category:Private_Use} { setCurCharPropertyValue("Co"); }
\p{General_Category:Lowercase_Letter} { setCurCharPropertyValue("Ll"); }
\p{General_Category:Modifier_Letter} { setCurCharPropertyValue("Lm"); }
\p{General_Category:Other_Letter} { setCurCharPropertyValue("Lo"); }
\p{General_Category:Titlecase_Letter} { setCurCharPropertyValue("Lt"); }
\p{General_Category:Uppercase_Letter} { setCurCharPropertyValue("Lu"); }
\p{General_Category:Spacing_Mark} { setCurCharPropertyValue("Mc"); }
\p{General_Category:Enclosing_Mark} { setCurCharPropertyValue("Me"); }
\p{General_Category:Nonspacing_Mark} { setCurCharPropertyValue("Mn"); }
\p{General_Category:Decimal_Number} { setCurCharPropertyValue("Nd"); }
\p{General_Category:Letter_Number} { setCurCharPropertyValue("Nl"); }
\p{General_Category:Other_Number} { setCurCharPropertyValue("No"); }
\p{General_Category:Connector_Punctuation} { setCurCharPropertyValue("Pc"); }
\p{General_Category:Dash_Punctuation} { setCurCharPropertyValue("Pd"); }
\p{General_Category:Close_Punctuation} { setCurCharPropertyValue("Pe"); }
\p{General_Category:Final_Punctuation} { setCurCharPropertyValue("Pf"); }
\p{General_Category:Initial_Punctuation} { setCurCharPropertyValue("Pi"); }
\p{General_Category:Other_Punctuation} { setCurCharPropertyValue("Po"); }
\p{General_Category:Open_Punctuation} { setCurCharPropertyValue("Ps"); }
\p{General_Category:Currency_Symbol} { setCurCharPropertyValue("Sc"); }
\p{General_Category:Modifier_Symbol} { setCurCharPropertyValue("Sk"); }
\p{General_Category:Math_Symbol} { setCurCharPropertyValue("Sm"); }
\p{General_Category:Other_Symbol} { setCurCharPropertyValue("So"); }
\p{General_Category:Line_Separator} { setCurCharPropertyValue("Zl"); }
\p{General_Category:Paragraph_Separator} { setCurCharPropertyValue("Zp"); }
\p{General_Category:Space_Separator} { setCurCharPropertyValue("Zs"); }
