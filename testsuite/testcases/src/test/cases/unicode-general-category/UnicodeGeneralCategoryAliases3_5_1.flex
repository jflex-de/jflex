%%

%unicode 5.1
%public
%class UnicodeGeneralCategoryAliases3_5_1

%type int
%standalone

%include ../../resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\p{Control} { setCurCharPropertyValue("Cc"); }
\p{Format} { setCurCharPropertyValue("Cf"); }
\p{Unassigned} { setCurCharPropertyValue("Cn"); }
\p{Private_Use} { setCurCharPropertyValue("Co"); }
\p{Lowercase_Letter} { setCurCharPropertyValue("Ll"); }
\p{Modifier_Letter} { setCurCharPropertyValue("Lm"); }
\p{Other_Letter} { setCurCharPropertyValue("Lo"); }
\p{Titlecase_Letter} { setCurCharPropertyValue("Lt"); }
\p{Uppercase_Letter} { setCurCharPropertyValue("Lu"); }
\p{Spacing_Mark} { setCurCharPropertyValue("Mc"); }
\p{Enclosing_Mark} { setCurCharPropertyValue("Me"); }
\p{Nonspacing_Mark} { setCurCharPropertyValue("Mn"); }
\p{Decimal_Number} { setCurCharPropertyValue("Nd"); }
\p{Letter_Number} { setCurCharPropertyValue("Nl"); }
\p{Other_Number} { setCurCharPropertyValue("No"); }
\p{Connector_Punctuation} { setCurCharPropertyValue("Pc"); }
\p{Dash_Punctuation} { setCurCharPropertyValue("Pd"); }
\p{Close_Punctuation} { setCurCharPropertyValue("Pe"); }
\p{Final_Punctuation} { setCurCharPropertyValue("Pf"); }
\p{Initial_Punctuation} { setCurCharPropertyValue("Pi"); }
\p{Other_Punctuation} { setCurCharPropertyValue("Po"); }
\p{Open_Punctuation} { setCurCharPropertyValue("Ps"); }
\p{Currency_Symbol} { setCurCharPropertyValue("Sc"); }
\p{Modifier_Symbol} { setCurCharPropertyValue("Sk"); }
\p{Math_Symbol} { setCurCharPropertyValue("Sm"); }
\p{Other_Symbol} { setCurCharPropertyValue("So"); }
\p{Line_Separator} { setCurCharPropertyValue("Zl"); }
\p{Paragraph_Separator} { setCurCharPropertyValue("Zp"); }
\p{Space_Separator} { setCurCharPropertyValue("Zs"); }
