%%

%unicode 5.1
%public
%class UnicodeGeneralCategoryAliases4_5_1

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\p{gc:Control} { setCurCharPropertyValue("Cc"); }
\p{gc:Format} { setCurCharPropertyValue("Cf"); }
\p{gc:Unassigned} { setCurCharPropertyValue("Cn"); }
\p{gc:Private_Use} { setCurCharPropertyValue("Co"); }
\p{gc:Lowercase_Letter} { setCurCharPropertyValue("Ll"); }
\p{gc:Modifier_Letter} { setCurCharPropertyValue("Lm"); }
\p{gc:Other_Letter} { setCurCharPropertyValue("Lo"); }
\p{gc:Titlecase_Letter} { setCurCharPropertyValue("Lt"); }
\p{gc:Uppercase_Letter} { setCurCharPropertyValue("Lu"); }
\p{gc:Spacing_Mark} { setCurCharPropertyValue("Mc"); }
\p{gc:Enclosing_Mark} { setCurCharPropertyValue("Me"); }
\p{gc:Nonspacing_Mark} { setCurCharPropertyValue("Mn"); }
\p{gc:Decimal_Number} { setCurCharPropertyValue("Nd"); }
\p{gc:Letter_Number} { setCurCharPropertyValue("Nl"); }
\p{gc:Other_Number} { setCurCharPropertyValue("No"); }
\p{gc:Connector_Punctuation} { setCurCharPropertyValue("Pc"); }
\p{gc:Dash_Punctuation} { setCurCharPropertyValue("Pd"); }
\p{gc:Close_Punctuation} { setCurCharPropertyValue("Pe"); }
\p{gc:Final_Punctuation} { setCurCharPropertyValue("Pf"); }
\p{gc:Initial_Punctuation} { setCurCharPropertyValue("Pi"); }
\p{gc:Other_Punctuation} { setCurCharPropertyValue("Po"); }
\p{gc:Open_Punctuation} { setCurCharPropertyValue("Ps"); }
\p{gc:Currency_Symbol} { setCurCharPropertyValue("Sc"); }
\p{gc:Modifier_Symbol} { setCurCharPropertyValue("Sk"); }
\p{gc:Math_Symbol} { setCurCharPropertyValue("Sm"); }
\p{gc:Other_Symbol} { setCurCharPropertyValue("So"); }
\p{gc:Line_Separator} { setCurCharPropertyValue("Zl"); }
\p{gc:Paragraph_Separator} { setCurCharPropertyValue("Zp"); }
\p{gc:Space_Separator} { setCurCharPropertyValue("Zs"); }
