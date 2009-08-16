%%

%unicode 5.1
%public
%class UnicodeGeneralCategoryAliases5_5_1

%type int
%standalone

%include src/test/cases/unicode-general-category/common-unicode-general-category-java

%%

<<EOF>> { printOutput(); return 1; }
\p{General_Category:Control} { setCurCharBlock("Cc"); }
\p{General_Category:Format} { setCurCharBlock("Cf"); }
\p{General_Category:Unassigned} { setCurCharBlock("Cn"); }
\p{General_Category:Private_Use} { setCurCharBlock("Co"); }
\p{General_Category:Lowercase_Letter} { setCurCharBlock("Ll"); }
\p{General_Category:Modifier_Letter} { setCurCharBlock("Lm"); }
\p{General_Category:Other_Letter} { setCurCharBlock("Lo"); }
\p{General_Category:Titlecase_Letter} { setCurCharBlock("Lt"); }
\p{General_Category:Uppercase_Letter} { setCurCharBlock("Lu"); }
\p{General_Category:Spacing_Mark} { setCurCharBlock("Mc"); }
\p{General_Category:Enclosing_Mark} { setCurCharBlock("Me"); }
\p{General_Category:Nonspacing_Mark} { setCurCharBlock("Mn"); }
\p{General_Category:Decimal_Number} { setCurCharBlock("Nd"); }
\p{General_Category:Letter_Number} { setCurCharBlock("Nl"); }
\p{General_Category:Other_Number} { setCurCharBlock("No"); }
\p{General_Category:Connector_Punctuation} { setCurCharBlock("Pc"); }
\p{General_Category:Dash_Punctuation} { setCurCharBlock("Pd"); }
\p{General_Category:Close_Punctuation} { setCurCharBlock("Pe"); }
\p{General_Category:Final_Punctuation} { setCurCharBlock("Pf"); }
\p{General_Category:Initial_Punctuation} { setCurCharBlock("Pi"); }
\p{General_Category:Other_Punctuation} { setCurCharBlock("Po"); }
\p{General_Category:Open_Punctuation} { setCurCharBlock("Ps"); }
\p{General_Category:Currency_Symbol} { setCurCharBlock("Sc"); }
\p{General_Category:Modifier_Symbol} { setCurCharBlock("Sk"); }
\p{General_Category:Math_Symbol} { setCurCharBlock("Sm"); }
\p{General_Category:Other_Symbol} { setCurCharBlock("So"); }
\p{General_Category:Line_Separator} { setCurCharBlock("Zl"); }
\p{General_Category:Paragraph_Separator} { setCurCharBlock("Zp"); }
\p{General_Category:Space_Separator} { setCurCharBlock("Zs"); }
