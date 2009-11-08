import com.langchao.jalarm.model.AlarmObj;

%%

%byaccj

%{
  private Parser yyparser;
  private boolean flag_set = false;
  private AlarmObj alarm;

  public Yylex(java.io.Reader r, Parser yyparser, AlarmObj obj) {
    this(r);
    this.yyparser = yyparser;
    this.alarm = obj;
  }

  String getAttrType(String attrname) {
    String type;
    try {
      type = alarm.getClass().getDeclaredField(attrname).getType().getName();
    } catch (Exception e) {
      e.printStackTrace();
      type = new String("null");
    }
    return type;
  }

  String getAttrSval(String attrname) {
    String methodname = "get" + Character.toUpperCase(attrname.charAt(0)) +
                        attrname.substring(1);
    String value;
    try {
      value = (String) alarm.getClass().getMethod(methodname,
              null).invoke(alarm, null);
    } catch (Exception e) {
      e.printStackTrace();
      value = null;
    }
    return value;
  }

  double getAttrDval(String attrname) {
    String methodname = "get" + Character.toUpperCase(attrname.charAt(0)) +
                        attrname.substring(1);
    double value;
    try {
      value = ((Number) (alarm.getClass().getMethod(methodname,
                         null).invoke(alarm, null))).doubleValue();
    } catch (Exception e) {
      e.printStackTrace();
      value = 0;
    }
    return value;
  }

  boolean getAttrBval(String attrname) {
    String methodname = "is" + Character.toUpperCase(attrname.charAt(0)) +
                        attrname.substring(1);
    boolean value;
    try {
      value = ((Boolean)this.alarm.getClass().getMethod(methodname, null).
               invoke(alarm, null)).booleanValue();
    } catch (Exception e) {
      e.printStackTrace();
      value = false;
    }
    return value;
  }


%}

%%

[\t\n]+			{}
'(\\'|[^'])*'	{
					yyparser.yylval = new ParserVal(yytext().substring(1,yytext().length()-1));
					return Parser.STRING;
				}
[ ]				{if (flag_set)	return Parser.SP;}
"=="			{return Parser.EQ;}
"!="			{return Parser.NE;}
"<"				{return (int) yycharat(0);}
"<="			{return Parser.LE;}
">"				{return (int) yycharat(0);}
">="			{return Parser.GE;}
"in"			{return Parser.IN;}
"and"|
"AND"			{return Parser.AND;}
"or"|
"OR"			{return Parser.OR;}
"not"|
"NOT"			{return Parser.NOT;}
"match"|
"MATCH"			{return Parser.RMATCH;}
"find"|
"FIND"			{return Parser.FIND;}
[A-Za-z$][A-Za-z0-9_]*	{
							String type;
							type=getAttrType(yytext());
							if (type.equals("java.langchao.String")){
								String value;
								value=getAttrSval(yytext());
								yyparser.yylval = new ParserVal(value);
								if (value.matches("\\{.*\\}"))
									return Parser.SET;
								else
									return Parser.STRING;
							}
							else if (type.equals("int")||type.equals("short")||type.equals("double")||
								type.equals("float")||type.equals("long")||type.equals("byte")){
								yyparser.yylval = new ParserVal(getAttrDval(yytext()));
								return Parser.NUMBER;
							}
							else if (type.equals("boolean")){
								yyparser.yylval = new ParserVal(getAttrBval(yytext()));
								return Parser.BOOLEAN;
							}
						}
[0-9]+|
[0-9]+\.[0-9]+	{
							yyparser.yylval = new ParserVal(Double.parseDouble(yytext()));
							return Parser.NUMBER;
						}
"{"			{
				flag_set=true;
				return (int) yycharat(0);
			}
"}"			{
				flag_set=false;
				return (int) yycharat(0);
			}
[+-*/()]		{	return (int) yycharat(0);}


