package edu.tum.cup2.spec;

import java.util.List; 
import java . util . LinkedList; 
import java . util . Collections; 
import edu.tum.cup2.grammar.NonTerminal;
import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.semantics.SymbolValue;
import edu.tum.cup2.semantics.Action;
import static edu.tum.cup2.grammar.SpecialTerminals.Epsilon;

import static edu.tum.cup2.spec.TranslatedCSpec2.NonTerminals.*;
import static edu.tum.cup2.spec.TranslatedCSpec2.Terminals.*;

public class TranslatedCSpec2 extends CUP2Specification {

public enum Terminals implements Terminal {
GLEICH,DIVGLEICH,SIGNED,ODERGLEICH,WHILE,LPARECKIG,MOD,CONST,PLUSPLUS,PLUSGLEICH,CASE,GLEICHGLEICH,CHAR,DO,FOR,EXTERN,MINUSMINUS,MINUSGLEICH,FLOAT,SEMIKOLON,KLEINERGLEICH,BREAK,LSHIFTGLEICH,IF,STRING_LITERAL,LPAR,CONTINUE,EXP,MULTGLEICH,EXPGLEICH,UNDGLEICH,IDENTIFIER,GROESSERGLEICH,TILDE,PFEIL,AUTO,RETURN,TYPE_NAME,DOUBLE,RSHIFTGLEICH,PLUS,VOID,KLEINER,GOTO,RSHIFT,REGISTER,UNGLEICH,UND,PUNKT,VOLATILE,DOPPELPUNKT,STATIC,PPP,LSHIFT,TYPEDEF,SWITCH,RPARSCHWEIF,ELSE,DEFAULT,UNDUND,SHORT,STRUCT,INT,MINUS,MULT,FRAGEZEICHEN,LPARSCHWEIF,ODER,UNION,ENUM,UNSIGNED,RPARECKIG,ODERODER,KOMMA,MODGLEICH,SIZEOF,CONSTANT,RPAR,NICHT,GROESSER,DIV,LONG
}
public enum NonTerminals implements NonTerminal {
expression,arg_expr_list,declarator,cast_expr,labeled_statement,multiplicative_expr,jump_statement,compound_statement,assignment_expr,unary_expr,rel_expr,enumerator,direct_abstract_declarator,struct_declarator_list,storage_class_specifier,shift_expr,struct_declarator,function_definition,declaration,init_declarator_list,init_declarator,case_statement,postfix_expr,struct_or_union_specifier,identifier_list,translation_unit,type_qualifier,enumerator_list,assignment_op,statement,incl_or_expr,log_and_expr,initializer_list,additive_expr,unary_operator,log_or_expr,iteration_statement,eq_expr,type_name,pointer,struct_or_union,parameter_list,and_expr,parameter_type_list,struct_declaration,enum_specifier,excl_or_expr,parameter_declaration,declaration_specifiers,struct_declaration_list,type_qualifier_list,statement_list,selection_statement,abstract_declarator,external_declaration,expr_stmt,const_expr,cond_expr,direct_declarator,declaration_list,primary_expression,type_specifier,specifier_qualifier_list,initializer
}


public TranslatedCSpec2() {
grammar(

prod(
  translation_unit,
  rhs(external_declaration 
/*
 * new Action() { public TranslationUnit a(List e) { //INSERTED CODE STRING:
 * RESULT = new TranslationUnit(); RESULT.prependAll(e); //END INSERT return
 * RESULT; } };
 */
  ),
  rhs(translation_unit, external_declaration 
/*
 * new Action() { public TranslationUnit a(TranslationUnit t, List e) {
 * //INSERTED CODE STRING: RESULT = t; t.appendAll(e); //END INSERT return
 * RESULT; } };
 */
  )
)
,
prod(
  external_declaration,
  rhs(function_definition 
/*
 * new Action() { public List a(FunDef e) { //INSERTED CODE STRING: RESULT =
 * Collections.singletonList(e); //END INSERT return RESULT; } };
 */
  ),
  rhs(declaration 
/*
 * new Action() { public List a(Decl e) { //INSERTED CODE STRING: RESULT =
 * ParserHelper.parseDecl(parser.ps, e); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  function_definition,
  rhs(declaration_specifiers, declarator, declaration_list, compound_statement 
/*
 * new Action() { public FunDef a(List s, Declarator d, List l, Stmt b) {
 * //INSERTED CODE STRING: RESULT = ParserHelper.parseFunDef(s,d,l,b); //END
 * INSERT return RESULT; } };
 */
  ),
  rhs(declaration_specifiers, declarator, compound_statement 
/*
 * new Action() { public FunDef a(List s, Declarator d, Stmt b) { //INSERTED
 * CODE STRING: RESULT = ParserHelper.parseFunDef(s,d,Collections.EMPTY_LIST,b);
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(declarator, declaration_list, compound_statement 
/*
 * new Action() { public FunDef a(Declarator d, List l, Stmt b) { //INSERTED
 * CODE STRING: RESULT = ParserHelper.parseFunDef(Collections.EMPTY_LIST,d,l,b);
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(declarator, compound_statement 
/*
 * new Action() { public FunDef a(Declarator d, Stmt b) { //INSERTED CODE
 * STRING: RESULT =
 * ParserHelper.parseFunDef(Collections.EMPTY_LIST,d,Collections.EMPTY_LIST,b);
 * //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  parameter_type_list,
  rhs(parameter_list 
/*
 * new Action() { public ParameterTypeList a(List pl) { //INSERTED CODE STRING:
 * RESULT = new ParameterTypeList(pl); //END INSERT return RESULT; } };
 */
  ),
  rhs(parameter_list, KOMMA, PPP 
/*
 * new Action() { public ParameterTypeList a(List pl, Terminal missingLabel1,
 * Terminal missingLabel2) { //INSERTED CODE STRING: RESULT = new
 * ParameterTypeList(pl, true); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  parameter_list,
  rhs(parameter_declaration 
/*
 * new Action() { public List a(ParameterDecl pd) { //INSERTED CODE STRING:
 * RESULT = new LinkedList(); RESULT.add(pd); //END INSERT return RESULT; } };
 */
  ),
  rhs(parameter_list, KOMMA, parameter_declaration 
/*
 * new Action() { public List a(List pl, Terminal missingLabel1, ParameterDecl
 * pd) { //INSERTED CODE STRING: RESULT = pl; RESULT.add(pd); //END INSERT
 * return RESULT; } };
 */
  )
)
,
prod(
  parameter_declaration,
  rhs(declaration_specifiers, declarator 
/*
 * new Action() { public ParameterDecl a(List ds, Declarator d) { //INSERTED
 * CODE STRING: RESULT = new ParameterDecl(ds, d); //END INSERT return RESULT; } };
 */
  ),
  rhs(declaration_specifiers, abstract_declarator 
/*
 * new Action() { public ParameterDecl a(List ds, Declarator ad) { //INSERTED
 * CODE STRING: RESULT = new ParameterDecl(ds, ad); //END INSERT return RESULT; } };
 */
  ),
  rhs(declaration_specifiers 
/*
 * new Action() { public ParameterDecl a(List ds) { //INSERTED CODE STRING:
 * RESULT = new ParameterDecl(ds, null); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  identifier_list,
  rhs(IDENTIFIER 
/*
 * new Action() { public List a(Terminal < String > id) { //INSERTED CODE
 * STRING: RESULT = new LinkedList(); RESULT.add(id.getValue()); //END INSERT
 * return RESULT; } };
 */
  ),
  rhs(identifier_list, KOMMA, IDENTIFIER 
/*
 * new Action() { public List a(List idl, Terminal missingLabel1, Terminal <
 * String > id) { //INSERTED CODE STRING: RESULT = idl;
 * RESULT.add(id.getValue()); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  declaration_list,
  rhs(declaration 
/*
 * new Action() { public List a(Decl d) { //INSERTED CODE STRING: RESULT = new
 * LinkedList(); RESULT.add(d); //END INSERT return RESULT; } };
 */
  ),
  rhs(declaration_list, declaration 
/*
 * new Action() { public List a(List dl, Decl d) { //INSERTED CODE STRING:
 * RESULT = dl; RESULT.add(d); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  declaration,
  rhs(declaration_specifiers, SEMIKOLON 
/*
 * new Action() { public Decl a(List ds, Terminal missingLabel1) { //INSERTED
 * CODE STRING: RESULT = new Decl(ds,Collections.EMPTY_LIST); //END INSERT
 * return RESULT; } };
 */
  ),
  rhs(declaration_specifiers, init_declarator_list, SEMIKOLON 
/*
 * new Action() { public Decl a(List ds, List idl, Terminal missingLabel2) {
 * //INSERTED CODE STRING:
 * 
 * RESULT = new Decl(ds,idl); parser.init_decl_list_geortet = false;
 * parser.typedef_geortet = false;
 * 
 * //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  declaration_specifiers,
  rhs(storage_class_specifier 
/*
 * new Action() { public List a(StorageClass sc) { //INSERTED CODE STRING:
 * 
 * RESULT = new LinkedList(); RESULT.add(sc);
 * 
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(storage_class_specifier, declaration_specifiers 
/*
 * new Action() { public List a(StorageClass sc, List ds) { //INSERTED CODE
 * STRING:
 * 
 * RESULT = ds; ds.add(0, sc);
 * 
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(type_specifier 
/*
 * new Action() { public List a(Object ts) { //INSERTED CODE STRING:
 * 
 * RESULT = new LinkedList(); RESULT.add(ts); parser.init_decl_list_geortet =
 * true;
 * 
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(type_specifier, declaration_specifiers 
/*
 * new Action() { public List a(Object ts, List ds) { //INSERTED CODE STRING:
 * 
 * RESULT = ds; ds.add(0, ts);
 * 
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(type_qualifier 
/*
 * new Action() { public List a(TypeQualifier tq) { //INSERTED CODE STRING:
 * 
 * RESULT = new LinkedList(); RESULT.add(tq); parser.init_decl_list_geortet =
 * true;
 * 
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(type_qualifier, declaration_specifiers 
/*
 * new Action() { public List a(TypeQualifier tq, List ds) { //INSERTED CODE
 * STRING:
 * 
 * RESULT = ds; ds.add(0, tq);
 * 
 * //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  declarator,
  rhs(direct_declarator 
/*
 * new Action() { public Declarator a(DirectDecl d) { //INSERTED CODE STRING:
 * RESULT = new Declarator(null, d); //END INSERT return RESULT; } };
 */
  ),
  rhs(pointer, direct_declarator 
/*
 * new Action() { public Declarator a(Pointer p, DirectDecl d) { //INSERTED CODE
 * STRING: RESULT = new Declarator(p,d); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  direct_declarator,
  rhs(IDENTIFIER 
/*
 * new Action() { public DirectDecl a(Terminal < String > id) { //INSERTED CODE
 * STRING: RESULT = DirectDecl.fromIdentifier(id.getValue());
 * parser.addTyp(id.getValue()); //END INSERT return RESULT; } };
 */
  ),
  rhs(LPAR, declarator, RPAR 
/*
 * new Action() { public DirectDecl a(Terminal missingLabel0, Declarator dd,
 * Terminal missingLabel2) { //INSERTED CODE STRING: RESULT =
 * DirectDecl.build(dd); //END INSERT return RESULT; } };
 */
  ),
  rhs(direct_declarator, LPARECKIG, const_expr, RPARECKIG 
/*
 * new Action() { public DirectDecl a(DirectDecl dd, Terminal missingLabel1,
 * Expr ce, Terminal missingLabel3) { //INSERTED CODE STRING: RESULT =
 * DirectDecl.buildArray(dd, ce); //END INSERT return RESULT; } };
 */
  ),
  rhs(direct_declarator, LPARECKIG, RPARECKIG 
/*
 * new Action() { public DirectDecl a(DirectDecl dd, Terminal missingLabel1,
 * Terminal missingLabel2) { //INSERTED CODE STRING: RESULT =
 * DirectDecl.buildArray(dd); //END INSERT return RESULT; } };
 */
  ),
  rhs(direct_declarator, LPAR, parameter_type_list, RPAR 
/*
 * new Action() { public DirectDecl a(DirectDecl dd, Terminal missingLabel1,
 * ParameterTypeList ptl, Terminal missingLabel3) { //INSERTED CODE STRING:
 * RESULT = DirectDecl.buildFun(dd, ptl); //END INSERT return RESULT; } };
 */
  ),
  rhs(direct_declarator, LPAR, identifier_list, RPAR 
/*
 * new Action() { public DirectDecl a(DirectDecl dd, Terminal missingLabel1,
 * List idl, Terminal missingLabel3) { //INSERTED CODE STRING: RESULT =
 * DirectDecl.buildFun(dd, ParserHelper.strArray(idl)); //END INSERT return
 * RESULT; } };
 */
  ),
  rhs(direct_declarator, LPAR, RPAR 
/*
 * new Action() { public DirectDecl a(DirectDecl dd, Terminal missingLabel1,
 * Terminal missingLabel2) { //INSERTED CODE STRING: RESULT =
 * DirectDecl.buildFun(dd); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  init_declarator_list,
  rhs(init_declarator 
/*
 * new Action() { public List a(InitDeclarator idecl) { //INSERTED CODE STRING:
 * RESULT = new LinkedList(); RESULT.add(idecl); //END INSERT return RESULT; } };
 */
  ),
  rhs(init_declarator_list, KOMMA, init_declarator 
/*
 * new Action() { public List a(List idl, Terminal missingLabel1, InitDeclarator
 * idecl) { //INSERTED CODE STRING: RESULT = idl; RESULT.add(idecl); //END
 * INSERT return RESULT; } };
 */
  )
)
,
prod(
  init_declarator,
  rhs(declarator 
/*
 * new Action() { public InitDeclarator a(Declarator d) { //INSERTED CODE
 * STRING: RESULT = new InitDeclarator(d, null); //END INSERT return RESULT; } };
 */
  ),
  rhs(declarator, GLEICH, initializer 
/*
 * new Action() { public InitDeclarator a(Declarator d, Terminal missingLabel1,
 * Expr init) { //INSERTED CODE STRING: RESULT = new InitDeclarator(d, init);
 * //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  abstract_declarator,
  rhs(pointer 
/*
 * new Action() { public Declarator a(Pointer ptr) { //INSERTED CODE STRING:
 * RESULT = new Declarator(ptr, null); //END INSERT return RESULT; } };
 */
  ),
  rhs(direct_abstract_declarator 
/*
 * new Action() { public Declarator a(DirectDecl dad) { //INSERTED CODE STRING:
 * RESULT = new Declarator(null, dad); //END INSERT return RESULT; } };
 */
  ),
  rhs(pointer, direct_abstract_declarator 
/*
 * new Action() { public Declarator a(Pointer ptr, DirectDecl dad) { //INSERTED
 * CODE STRING: RESULT = new Declarator(ptr, dad); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  direct_abstract_declarator,
  rhs(LPAR, abstract_declarator, RPAR 
/*
 * new Action() { public DirectDecl a(Terminal missingLabel0, Declarator dad,
 * Terminal missingLabel2) { //INSERTED CODE STRING: RESULT =
 * DirectDecl.build(dad); //END INSERT return RESULT; } };
 */
  ),
  rhs(LPARECKIG, RPARECKIG 
/*
 * new Action() { public DirectDecl a(Terminal missingLabel0, Terminal
 * missingLabel1) { //INSERTED CODE STRING: RESULT =
 * DirectDecl.buildArray(null); //END INSERT return RESULT; } };
 */
  ),
  rhs(LPARECKIG, const_expr, RPARECKIG 
/*
 * new Action() { public DirectDecl a(Terminal missingLabel0, Expr ce, Terminal
 * missingLabel2) { //INSERTED CODE STRING: RESULT = DirectDecl.buildArray(null,
 * ce); //END INSERT return RESULT; } };
 */
  ),
  rhs(direct_abstract_declarator, LPARECKIG, RPARECKIG 
/*
 * new Action() { public DirectDecl a(DirectDecl dad, Terminal missingLabel1,
 * Terminal missingLabel2) { //INSERTED CODE STRING: RESULT =
 * DirectDecl.buildArray(dad); //END INSERT return RESULT; } };
 */
  ),
  rhs(direct_abstract_declarator, LPARECKIG, const_expr, RPARECKIG 
/*
 * new Action() { public DirectDecl a(DirectDecl dad, Terminal missingLabel1,
 * Expr ce, Terminal missingLabel3) { //INSERTED CODE STRING: RESULT =
 * DirectDecl.buildArray(dad,ce); //END INSERT return RESULT; } };
 */
  ),
  rhs(LPAR, RPAR 
/*
 * new Action() { public DirectDecl a(Terminal missingLabel0, Terminal
 * missingLabel1) { //INSERTED CODE STRING: RESULT = DirectDecl.buildFun(null);
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(LPAR, parameter_type_list, RPAR 
/*
 * new Action() { public DirectDecl a(Terminal missingLabel0, ParameterTypeList
 * ptl, Terminal missingLabel2) { //INSERTED CODE STRING: RESULT =
 * DirectDecl.buildFun(null, ptl); //END INSERT return RESULT; } };
 */
  ),
  rhs(direct_abstract_declarator, LPAR, RPAR 
/*
 * new Action() { public DirectDecl a(DirectDecl dad, Terminal missingLabel1,
 * Terminal missingLabel2) { //INSERTED CODE STRING: RESULT =
 * DirectDecl.buildFun(dad); //END INSERT return RESULT; } };
 */
  ),
  rhs(direct_abstract_declarator, LPAR, parameter_type_list, RPAR 
/*
 * new Action() { public DirectDecl a(DirectDecl dad, Terminal missingLabel1,
 * ParameterTypeList ptl, Terminal missingLabel3) { //INSERTED CODE STRING:
 * RESULT = DirectDecl.buildFun(dad, ptl); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  type_specifier,
  rhs(VOID 
/*
 * new Action() { public null a(Terminal missingLabel0) { //INSERTED CODE
 * STRING: RESULT = TypeSpecifier.VOID; //END INSERT return RESULT; } };
 */
  ),
  rhs(CHAR 
/*
 * new Action() { public null a(Terminal missingLabel0) { //INSERTED CODE
 * STRING: RESULT = TypeSpecifier.CHAR; //END INSERT return RESULT; } };
 */
  ),
  rhs(SHORT 
/*
 * new Action() { public null a(Terminal missingLabel0) { //INSERTED CODE
 * STRING: RESULT = TypeSpecifier.SHORT; //END INSERT return RESULT; } };
 */
  ),
  rhs(INT 
/*
 * new Action() { public null a(Terminal missingLabel0) { //INSERTED CODE
 * STRING: RESULT = TypeSpecifier.INT; //END INSERT return RESULT; } };
 */
  ),
  rhs(LONG 
/*
 * new Action() { public null a(Terminal missingLabel0) { //INSERTED CODE
 * STRING: RESULT = TypeSpecifier.LONG; //END INSERT return RESULT; } };
 */
  ),
  rhs(FLOAT 
/*
 * new Action() { public null a(Terminal missingLabel0) { //INSERTED CODE
 * STRING: RESULT = TypeSpecifier.FLOAT; //END INSERT return RESULT; } };
 */
  ),
  rhs(DOUBLE 
/*
 * new Action() { public null a(Terminal missingLabel0) { //INSERTED CODE
 * STRING: RESULT = TypeSpecifier.DOUBLE; //END INSERT return RESULT; } };
 */
  ),
  rhs(SIGNED 
/*
 * new Action() { public null a(Terminal missingLabel0) { //INSERTED CODE
 * STRING: RESULT = TypeSpecifier.SIGNED; //END INSERT return RESULT; } };
 */
  ),
  rhs(UNSIGNED 
/*
 * new Action() { public null a(Terminal missingLabel0) { //INSERTED CODE
 * STRING: RESULT = TypeSpecifier.UNSIGNED; //END INSERT return RESULT; } };
 */
  ),
  rhs(struct_or_union_specifier 
/*
 * new Action() { public null a(StructUnionSpecifier sus) { //INSERTED CODE
 * STRING: RESULT = TypeSpecifier.fromStructOrUnionSpecifier(parser.ps, sus);
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(enum_specifier 
/*
 * new Action() { public null a(EnumSpecifier es) { //INSERTED CODE STRING:
 * RESULT = TypeSpecifier.fromEnumSpecifier(es); //END INSERT return RESULT; } };
 */
  ),
  rhs(TYPE_NAME 
/*
 * new Action() { public null a(Terminal t) { //INSERTED CODE STRING: RESULT =
 * TypeSpecifier.fromTypeName(parser.ps, t.getValue().toString()); //END INSERT
 * return RESULT; } };
 */
  )
)
,
prod(
  type_name,
  rhs(specifier_qualifier_list 
/*
 * new Action() { public TypeName a(List sql) { //INSERTED CODE STRING: RESULT =
 * new TypeName(sql, null); //END INSERT return RESULT; } };
 */
  ),
  rhs(specifier_qualifier_list, abstract_declarator 
/*
 * new Action() { public TypeName a(List sql, Declarator ad) { //INSERTED CODE
 * STRING: RESULT = new TypeName(sql, ad); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  storage_class_specifier,
  rhs(TYPEDEF 
/*
 * new Action() { public StorageClass a(Terminal missingLabel0) { //INSERTED
 * CODE STRING: RESULT = StorageClass.TYPEDEF; parser.typedef_geortet = true;
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(EXTERN 
/*
 * new Action() { public StorageClass a(Terminal missingLabel0) { //INSERTED
 * CODE STRING: RESULT = StorageClass.EXTERN; //END INSERT return RESULT; } };
 */
  ),
  rhs(STATIC 
/*
 * new Action() { public StorageClass a(Terminal missingLabel0) { //INSERTED
 * CODE STRING: RESULT = StorageClass.STATIC; //END INSERT return RESULT; } };
 */
  ),
  rhs(AUTO 
/*
 * new Action() { public StorageClass a(Terminal missingLabel0) { //INSERTED
 * CODE STRING: RESULT = StorageClass.AUTO; //END INSERT return RESULT; } };
 */
  ),
  rhs(REGISTER 
/*
 * new Action() { public StorageClass a(Terminal missingLabel0) { //INSERTED
 * CODE STRING: RESULT = StorageClass.REGISTER; //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  specifier_qualifier_list,
  rhs(type_specifier, specifier_qualifier_list 
/*
 * new Action() { public List a(Object ts, List s) { //INSERTED CODE STRING:
 * RESULT = s; s.add(0, ts); //END INSERT return RESULT; } };
 */
  ),
  rhs(type_specifier 
/*
 * new Action() { public List a(Object ts) { //INSERTED CODE STRING: RESULT =
 * new LinkedList(); RESULT.add(ts); //END INSERT return RESULT; } };
 */
  ),
  rhs(type_qualifier, specifier_qualifier_list 
/*
 * new Action() { public List a(TypeQualifier tq, List s) { //INSERTED CODE
 * STRING: RESULT = s; s.add(0, tq); //END INSERT return RESULT; } };
 */
  ),
  rhs(type_qualifier 
/*
 * new Action() { public List a(TypeQualifier tq) { //INSERTED CODE STRING:
 * RESULT = new LinkedList(); RESULT.add(tq); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  type_qualifier_list,
  rhs(type_qualifier 
/*
 * new Action() { public List a(TypeQualifier tq) { //INSERTED CODE STRING:
 * RESULT = new LinkedList(); RESULT.add(tq); //END INSERT return RESULT; } };
 */
  ),
  rhs(type_qualifier_list, type_qualifier 
/*
 * new Action() { public List a(List tql, TypeQualifier tq) { //INSERTED CODE
 * STRING: RESULT = tql; tql.add(tq); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  type_qualifier,
  rhs(CONST 
/*
 * new Action() { public TypeQualifier a(Terminal missingLabel0) { //INSERTED
 * CODE STRING: RESULT = TypeQualifier.CONST; //END INSERT return RESULT; } };
 */
  ),
  rhs(VOLATILE 
/*
 * new Action() { public TypeQualifier a(Terminal missingLabel0) { //INSERTED
 * CODE STRING: RESULT = TypeQualifier.VOLATILE; //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  struct_or_union_specifier,
  rhs(struct_or_union, IDENTIFIER, LPARSCHWEIF, struct_declaration_list, RPARSCHWEIF 
/*
 * new Action() { public StructUnionSpecifier a(String sou, Terminal < String >
 * i, Terminal missingLabel2, List sdl, Terminal missingLabel4) { //INSERTED
 * CODE STRING: RESULT = new StructUnionSpecifier(sou,i.getValue(),sdl); //END
 * INSERT return RESULT; } };
 */
  ),
  rhs(struct_or_union, LPARSCHWEIF, struct_declaration_list, RPARSCHWEIF 
/*
 * new Action() { public StructUnionSpecifier a(String sou, Terminal
 * missingLabel1, List sdl, Terminal missingLabel3) { //INSERTED CODE STRING:
 * RESULT = new StructUnionSpecifier(sou,null,sdl); //END INSERT return RESULT; } };
 */
  ),
  rhs(struct_or_union, IDENTIFIER 
/*
 * new Action() { public StructUnionSpecifier a(String sou, Terminal < String >
 * i) { //INSERTED CODE STRING: RESULT = new
 * StructUnionSpecifier(sou,i.getValue(),null); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  struct_or_union,
  rhs(STRUCT 
/*
 * new Action() { public String a(Terminal missingLabel0) { //INSERTED CODE
 * STRING: RESULT = StructUnionSpecifier.STRUCT; //END INSERT return RESULT; } };
 */
  ),
  rhs(UNION 
/*
 * new Action() { public String a(Terminal missingLabel0) { //INSERTED CODE
 * STRING: RESULT = StructUnionSpecifier.UNION; //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  struct_declaration_list,
  rhs(struct_declaration 
/*
 * new Action() { public List a(StructDecl sd) { //INSERTED CODE STRING: RESULT =
 * new LinkedList(); RESULT.add(sd); //END INSERT return RESULT; } };
 */
  ),
  rhs(struct_declaration_list, struct_declaration 
/*
 * new Action() { public List a(List sdl, StructDecl sd) { //INSERTED CODE
 * STRING: RESULT = sdl; RESULT.add(sd); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  struct_declaration,
  rhs(specifier_qualifier_list, struct_declarator_list, SEMIKOLON 
/*
 * new Action() { public StructDecl a(List sql, List sdl, Terminal
 * missingLabel2) { //INSERTED CODE STRING: RESULT = new StructDecl(sql,sdl);
 * //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  struct_declarator_list,
  rhs(struct_declarator 
/*
 * new Action() { public List a(StructDeclarator sd) { //INSERTED CODE STRING:
 * RESULT = new LinkedList(); RESULT.add(sd); //END INSERT return RESULT; } };
 */
  ),
  rhs(struct_declarator_list, KOMMA, struct_declarator 
/*
 * new Action() { public List a(List sdl, Terminal missingLabel1,
 * StructDeclarator sd) { //INSERTED CODE STRING: RESULT = sdl; RESULT.add(sd);
 * //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  struct_declarator,
  rhs(declarator 
/*
 * new Action() { public StructDeclarator a(Declarator d) { //INSERTED CODE
 * STRING: RESULT = new StructDeclarator(d,null); //END INSERT return RESULT; } };
 */
  ),
  rhs(DOPPELPUNKT, const_expr 
/*
 * new Action() { public StructDeclarator a(Terminal missingLabel0, Expr ce) {
 * //INSERTED CODE STRING: RESULT = new StructDeclarator(null,ce); //END INSERT
 * return RESULT; } };
 */
  ),
  rhs(declarator, DOPPELPUNKT, const_expr 
/*
 * new Action() { public StructDeclarator a(Declarator d, Terminal
 * missingLabel1, Expr ce) { //INSERTED CODE STRING: RESULT = new
 * StructDeclarator(d,ce); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  enum_specifier,
  rhs(ENUM, LPARSCHWEIF, enumerator_list, RPARSCHWEIF
  ),
  rhs(ENUM, IDENTIFIER, LPARSCHWEIF, enumerator_list, RPARSCHWEIF
  ),
  rhs(ENUM, IDENTIFIER
  )
)
,
prod(
  enumerator_list,
  rhs(enumerator
  ),
  rhs(enumerator_list, KOMMA, enumerator
  )
)
,
prod(
  enumerator,
  rhs(IDENTIFIER
  ),
  rhs(IDENTIFIER, GLEICH, const_expr
  )
)
,
prod(
  pointer,
  rhs(MULT 
/*
 * new Action() { public Pointer a(Terminal missingLabel0) { //INSERTED CODE
 * STRING: RESULT = new Pointer(null, null); //END INSERT return RESULT; } };
 */
  ),
  rhs(MULT, type_qualifier_list 
/*
 * new Action() { public Pointer a(Terminal missingLabel0, List tql) {
 * //INSERTED CODE STRING: RESULT = new Pointer(tql, null); //END INSERT return
 * RESULT; } };
 */
  ),
  rhs(MULT, pointer 
/*
 * new Action() { public Pointer a(Terminal missingLabel0, Pointer ptr) {
 * //INSERTED CODE STRING: RESULT = new Pointer(null, ptr); //END INSERT return
 * RESULT; } };
 */
  ),
  rhs(MULT, type_qualifier_list, pointer 
/*
 * new Action() { public Pointer a(Terminal missingLabel0, List tql, Pointer
 * ptr) { //INSERTED CODE STRING: RESULT = new Pointer(tql, ptr); //END INSERT
 * return RESULT; } };
 */
  )
)
,
prod(
  primary_expression,
  rhs(IDENTIFIER 
/*
 * new Action() { public Expr a(Terminal < String > i) { //INSERTED CODE STRING:
 * RESULT = Expr.varfun(i.getValue()).locate(i); //END INSERT return RESULT; } };
 */
  ),
  rhs(CONSTANT 
/*
 * new Action() { public Expr a(Terminal < String > c) { //INSERTED CODE STRING:
 * RESULT = Expr.number(c.getValue()).locate(c); //END INSERT return RESULT; } };
 */
  ),
  rhs(STRING_LITERAL 
/*
 * new Action() { public Expr a(Terminal < String > sl) { //INSERTED CODE
 * STRING: RESULT = Expr.str(sl.getValue()).locate(sl); //END INSERT return
 * RESULT; } };
 */
  ),
  rhs(LPAR, expression, RPAR 
/*
 * new Action() { public Expr a(Terminal l, Expr e, Terminal r) { //INSERTED
 * CODE STRING: RESULT = e.locate(l,r); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  expression,
  rhs(assignment_expr 
/*
 * new Action() { public Expr a(Expr as) { //INSERTED CODE STRING: RESULT = as;
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(expression, KOMMA, assignment_expr 
/*
 * new Action() { public Expr a(Expr lhs, Terminal missingLabel1, Expr rhs) {
 * //INSERTED CODE STRING: RESULT = Expr.seq(lhs, rhs); //END INSERT return
 * RESULT; } };
 */
  )
)
,
prod(
  const_expr,
  rhs(cond_expr 
/*
 * new Action() { public Expr a(Expr ce) { //INSERTED CODE STRING: RESULT = ce;
 * //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  unary_expr,
  rhs(postfix_expr 
/*
 * new Action() { public Expr a(Expr pe) { //INSERTED CODE STRING: RESULT = pe;
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(PLUSPLUS, unary_expr 
/*
 * new Action() { public Expr a(Terminal missingLabel0, Expr ue) { //INSERTED
 * CODE STRING: RESULT = Expr.preinc(ue); //END INSERT return RESULT; } };
 */
  ),
  rhs(MINUSMINUS, unary_expr 
/*
 * new Action() { public Expr a(Terminal missingLabel0, Expr ue) { //INSERTED
 * CODE STRING: RESULT = Expr.predec(ue); //END INSERT return RESULT; } };
 */
  ),
  rhs(unary_operator, cast_expr 
/*
 * new Action() { public Expr a(Located < UnaryOp > uop, Expr ce) { //INSERTED
 * CODE STRING: RESULT = ParserHelper.expr(uop.getObject(), ce).locate(uop,ce);
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(SIZEOF, unary_expr 
/*
 * new Action() { public Expr a(Terminal missingLabel0, Expr ue) { //INSERTED
 * CODE STRING: RESULT = Expr.sizeof(ue); //END INSERT return RESULT; } };
 */
  ),
  rhs(SIZEOF, LPAR, type_name, RPAR 
/*
 * new Action() { public Expr a(Terminal missingLabel0, Terminal missingLabel1,
 * TypeName tn, Terminal missingLabel3) { //INSERTED CODE STRING: RESULT =
 * Expr.sizeof(ParserHelper.type(tn)); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  postfix_expr,
  rhs(primary_expression 
/*
 * new Action() { public Expr a(Expr pe) { //INSERTED CODE STRING: RESULT = pe;
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(postfix_expr, LPARECKIG, expression, RPARECKIG 
/*
 * new Action() { public Expr a(Expr pe, Terminal missingLabel1, Expr e,
 * Terminal missingLabel3) { //INSERTED CODE STRING: RESULT = Expr.subscript(pe,
 * e); //END INSERT return RESULT; } };
 */
  ),
  rhs(postfix_expr, LPAR, RPAR 
/*
 * new Action() { public Expr a(Expr pe, Terminal missingLabel1, Terminal
 * missingLabel2) { //INSERTED CODE STRING: RESULT = Expr.call(pe); //END INSERT
 * return RESULT; } };
 */
  ),
  rhs(postfix_expr, LPAR, arg_expr_list, RPAR 
/*
 * new Action() { public Expr a(Expr pe, Terminal missingLabel1, List ael,
 * Terminal missingLabel3) { //INSERTED CODE STRING: RESULT = Expr.call(pe,
 * ParserHelper.exprArray(ael)); //END INSERT return RESULT; } };
 */
  ),
  rhs(postfix_expr, PUNKT, IDENTIFIER 
/*
 * new Action() { public Expr a(Expr pe, Terminal missingLabel1, Terminal <
 * String > i) { //INSERTED CODE STRING: RESULT = Expr.access(pe, i.getValue());
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(postfix_expr, PFEIL, IDENTIFIER 
/*
 * new Action() { public Expr a(Expr pe, Terminal missingLabel1, Terminal <
 * String > i) { //INSERTED CODE STRING: RESULT = Expr.ptraccess(pe,
 * i.getValue()); //END INSERT return RESULT; } };
 */
  ),
  rhs(postfix_expr, PLUSPLUS 
/*
 * new Action() { public Expr a(Expr pe, Terminal missingLabel1) { //INSERTED
 * CODE STRING: RESULT = Expr.postinc(pe); //END INSERT return RESULT; } };
 */
  ),
  rhs(postfix_expr, MINUSMINUS 
/*
 * new Action() { public Expr a(Expr pe, Terminal missingLabel1) { //INSERTED
 * CODE STRING: RESULT = Expr.postdec(pe); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  additive_expr,
  rhs(multiplicative_expr 
/*
 * new Action() { public Expr a(Expr me) { //INSERTED CODE STRING: RESULT = me;
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(additive_expr, PLUS, multiplicative_expr 
/*
 * new Action() { public Expr a(Expr lhs, Terminal missingLabel1, Expr rhs) {
 * //INSERTED CODE STRING: RESULT = Expr.add(lhs,rhs); //END INSERT return
 * RESULT; } };
 */
  ),
  rhs(additive_expr, MINUS, multiplicative_expr 
/*
 * new Action() { public Expr a(Expr lhs, Terminal missingLabel1, Expr rhs) {
 * //INSERTED CODE STRING: RESULT = Expr.sub(lhs,rhs); //END INSERT return
 * RESULT; } };
 */
  )
)
,
prod(
  multiplicative_expr,
  rhs(cast_expr 
/*
 * new Action() { public Expr a(Expr ce) { //INSERTED CODE STRING: RESULT = ce;
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(multiplicative_expr, MULT, cast_expr 
/*
 * new Action() { public Expr a(Expr lhs, Terminal missingLabel1, Expr rhs) {
 * //INSERTED CODE STRING: RESULT = Expr.mul(lhs,rhs); //END INSERT return
 * RESULT; } };
 */
  ),
  rhs(multiplicative_expr, DIV, cast_expr 
/*
 * new Action() { public Expr a(Expr lhs, Terminal missingLabel1, Expr rhs) {
 * //INSERTED CODE STRING: RESULT = Expr.div(lhs,rhs); //END INSERT return
 * RESULT; } };
 */
  ),
  rhs(multiplicative_expr, MOD, cast_expr 
/*
 * new Action() { public Expr a(Expr lhs, Terminal missingLabel1, Expr rhs) {
 * //INSERTED CODE STRING: RESULT = Expr.mod(lhs,rhs); //END INSERT return
 * RESULT; } };
 */
  )
)
,
prod(
  shift_expr,
  rhs(additive_expr 
/*
 * new Action() { public Expr a(Expr ae) { //INSERTED CODE STRING: RESULT = ae;
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(shift_expr, LSHIFT, additive_expr 
/*
 * new Action() { public Expr a(Expr lhs, Terminal missingLabel1, Expr rhs) {
 * //INSERTED CODE STRING: RESULT = Expr.shl(lhs,rhs); //END INSERT return
 * RESULT; } };
 */
  ),
  rhs(shift_expr, RSHIFT, additive_expr 
/*
 * new Action() { public Expr a(Expr lhs, Terminal missingLabel1, Expr rhs) {
 * //INSERTED CODE STRING: RESULT = Expr.shr(lhs,rhs); //END INSERT return
 * RESULT; } };
 */
  )
)
,
prod(
  and_expr,
  rhs(eq_expr 
/*
 * new Action() { public Expr a(Expr ee) { //INSERTED CODE STRING: RESULT = ee;
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(and_expr, UND, eq_expr 
/*
 * new Action() { public Expr a(Expr lhs, Terminal missingLabel1, Expr rhs) {
 * //INSERTED CODE STRING: RESULT = Expr.band(lhs,rhs); //END INSERT return
 * RESULT; } };
 */
  )
)
,
prod(
  excl_or_expr,
  rhs(and_expr 
/*
 * new Action() { public Expr a(Expr ae) { //INSERTED CODE STRING: RESULT = ae;
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(excl_or_expr, EXP, and_expr 
/*
 * new Action() { public Expr a(Expr eoe, Terminal missingLabel1, Expr ae) {
 * //INSERTED CODE STRING: RESULT = Expr.bxor(eoe, ae); //END INSERT return
 * RESULT; } };
 */
  )
)
,
prod(
  incl_or_expr,
  rhs(excl_or_expr 
/*
 * new Action() { public Expr a(Expr eoe) { //INSERTED CODE STRING: RESULT =
 * eoe; //END INSERT return RESULT; } };
 */
  ),
  rhs(incl_or_expr, ODER, excl_or_expr 
/*
 * new Action() { public Expr a(Expr lhs, Terminal missingLabel1, Expr rhs) {
 * //INSERTED CODE STRING: RESULT = Expr.bor(lhs,rhs); //END INSERT return
 * RESULT; } };
 */
  )
)
,
prod(
  log_and_expr,
  rhs(incl_or_expr 
/*
 * new Action() { public Expr a(Expr ioe) { //INSERTED CODE STRING: RESULT =
 * ioe; //END INSERT return RESULT; } };
 */
  ),
  rhs(log_and_expr, UNDUND, incl_or_expr 
/*
 * new Action() { public Expr a(Expr lhs, Terminal missingLabel1, Expr rhs) {
 * //INSERTED CODE STRING: RESULT = Expr.land(lhs,rhs); //END INSERT return
 * RESULT; } };
 */
  )
)
,
prod(
  log_or_expr,
  rhs(log_and_expr 
/*
 * new Action() { public Expr a(Expr lae) { //INSERTED CODE STRING: RESULT =
 * lae; //END INSERT return RESULT; } };
 */
  ),
  rhs(log_or_expr, ODERODER, log_and_expr 
/*
 * new Action() { public Expr a(Expr lhs, Terminal missingLabel1, Expr rhs) {
 * //INSERTED CODE STRING: RESULT = Expr.lor(lhs,rhs); //END INSERT return
 * RESULT; } };
 */
  )
)
,
prod(
  rel_expr,
  rhs(shift_expr 
/*
 * new Action() { public Expr a(Expr se) { //INSERTED CODE STRING: RESULT = se;
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(rel_expr, KLEINER, shift_expr 
/*
 * new Action() { public Expr a(Expr rhs, Terminal missingLabel1, Expr lhs) {
 * //INSERTED CODE STRING: RESULT = Expr.lt(rhs, lhs); //END INSERT return
 * RESULT; } };
 */
  ),
  rhs(rel_expr, GROESSER, shift_expr 
/*
 * new Action() { public Expr a(Expr rhs, Terminal missingLabel1, Expr lhs) {
 * //INSERTED CODE STRING: RESULT = Expr.gt(rhs, lhs); //END INSERT return
 * RESULT; } };
 */
  ),
  rhs(rel_expr, KLEINERGLEICH, shift_expr 
/*
 * new Action() { public Expr a(Expr rhs, Terminal missingLabel1, Expr lhs) {
 * //INSERTED CODE STRING: RESULT = Expr.le(rhs, lhs); //END INSERT return
 * RESULT; } };
 */
  ),
  rhs(rel_expr, GROESSERGLEICH, shift_expr 
/*
 * new Action() { public Expr a(Expr rhs, Terminal missingLabel1, Expr lhs) {
 * //INSERTED CODE STRING: RESULT = Expr.ge(rhs, lhs); //END INSERT return
 * RESULT; } };
 */
  )
)
,
prod(
  eq_expr,
  rhs(rel_expr 
/*
 * new Action() { public Expr a(Expr re) { //INSERTED CODE STRING: RESULT = re;
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(eq_expr, GLEICHGLEICH, rel_expr 
/*
 * new Action() { public Expr a(Expr ee, Terminal missingLabel1, Expr re) {
 * //INSERTED CODE STRING: RESULT = Expr.eq(ee, re); //END INSERT return RESULT; } };
 */
  ),
  rhs(eq_expr, UNGLEICH, rel_expr 
/*
 * new Action() { public Expr a(Expr ee, Terminal missingLabel1, Expr re) {
 * //INSERTED CODE STRING: RESULT = Expr.neq(ee,re); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  cond_expr,
  rhs(log_or_expr 
/*
 * new Action() { public Expr a(Expr loe) { //INSERTED CODE STRING: RESULT =
 * loe; //END INSERT return RESULT; } };
 */
  ),
  rhs(log_or_expr, FRAGEZEICHEN, expression, DOPPELPUNKT, cond_expr 
/*
 * new Action() { public Expr a(Expr cond, Terminal missingLabel1, Expr e1,
 * Terminal missingLabel3, Expr e2) { //INSERTED CODE STRING: RESULT =
 * Expr.conditional(cond,e1,e2); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  assignment_expr,
  rhs(cond_expr 
/*
 * new Action() { public Expr a(Expr ce) { //INSERTED CODE STRING: RESULT = ce;
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(unary_expr, assignment_op, assignment_expr 
/*
 * new Action() { public Expr a(Expr lhs, AssignmentOp aop, Expr rhs) {
 * //INSERTED CODE STRING: RESULT = ParserHelper.expr(lhs,aop,rhs); //END INSERT
 * return RESULT; } };
 */
  )
)
,
prod(
  cast_expr,
  rhs(unary_expr 
/*
 * new Action() { public Expr a(Expr ue) { //INSERTED CODE STRING: RESULT = ue;
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(LPAR, type_name, RPAR, cast_expr 
/*
 * new Action() { public Expr a(Terminal missingLabel0, TypeName tn, Terminal
 * missingLabel2, Expr ce) { //INSERTED CODE STRING: RESULT =
 * Expr.cast(ParserHelper.type(tn), ce); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  arg_expr_list,
  rhs(assignment_expr 
/*
 * new Action() { public List a(Expr e) { //INSERTED CODE STRING: RESULT = new
 * LinkedList(); RESULT.add(e); //END INSERT return RESULT; } };
 */
  ),
  rhs(arg_expr_list, KOMMA, assignment_expr 
/*
 * new Action() { public List a(List ael, Terminal missingLabel1, Expr e) {
 * //INSERTED CODE STRING: RESULT = ael; RESULT.add(e); //END INSERT return
 * RESULT; } };
 */
  )
)
,
prod(
  initializer,
  rhs(assignment_expr 
/*
 * new Action() { public Expr a(Expr as) { //INSERTED CODE STRING: RESULT = as;
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(LPARSCHWEIF, initializer_list, RPARSCHWEIF 
/*
 * new Action() { public Expr a(Terminal missingLabel0, List il, Terminal
 * missingLabel2) { //INSERTED CODE STRING: RESULT =
 * Expr.arrayInit(ParserHelper.exprArray(il)); //END INSERT return RESULT; } };
 */
  ),
  rhs(LPARSCHWEIF, initializer_list, KOMMA, RPARSCHWEIF 
/*
 * new Action() { public Expr a(Terminal missingLabel0, List il, Terminal
 * missingLabel2, Terminal missingLabel3) { //INSERTED CODE STRING: RESULT =
 * Expr.arrayInit(ParserHelper.exprArray(il)); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  initializer_list,
  rhs(initializer 
/*
 * new Action() { public List a(Expr init) { //INSERTED CODE STRING: RESULT =
 * new LinkedList(); RESULT.add(init); //END INSERT return RESULT; } };
 */
  ),
  rhs(initializer_list, KOMMA, initializer 
/*
 * new Action() { public List a(List il, Terminal missingLabel1, Expr init) {
 * //INSERTED CODE STRING: RESULT = il; RESULT.add(init); //END INSERT return
 * RESULT; } };
 */
  )
)
,
prod(
  unary_operator,
  rhs(UND 
/*
 * new Action() { public Located < UnaryOp > a(Terminal p) { //INSERTED CODE
 * STRING: RESULT = new Located<UnaryOp>(UnaryOp.ADDROF, p); //END INSERT
 * return RESULT; } };
 */
  ),
  rhs(MULT 
/*
 * new Action() { public Located < UnaryOp > a(Terminal p) { //INSERTED CODE
 * STRING: RESULT = new Located<UnaryOp>(UnaryOp.DEREF, p); //END INSERT return
 * RESULT; } };
 */
  ),
  rhs(PLUS 
/*
 * new Action() { public Located < UnaryOp > a(Terminal p) { //INSERTED CODE
 * STRING: RESULT = new Located<UnaryOp>(UnaryOp.PLUS, p); //END INSERT return
 * RESULT; } };
 */
  ),
  rhs(MINUS 
/*
 * new Action() { public Located < UnaryOp > a(Terminal p) { //INSERTED CODE
 * STRING: RESULT = new Located<UnaryOp>(UnaryOp.NEG, p); //END INSERT return
 * RESULT; } };
 */
  ),
  rhs(TILDE 
/*
 * new Action() { public Located < UnaryOp > a(Terminal p) { //INSERTED CODE
 * STRING: RESULT = new Located<UnaryOp>(UnaryOp.ONESCOMPLEMENT, p); //END
 * INSERT return RESULT; } };
 */
  ),
  rhs(NICHT 
/*
 * new Action() { public Located < UnaryOp > a(Terminal p) { //INSERTED CODE
 * STRING: RESULT = new Located<UnaryOp>(UnaryOp.NOT, p); //END INSERT return
 * RESULT; } };
 */
  )
)
,
prod(
  assignment_op,
  rhs(GLEICH 
/*
 * new Action() { public AssignmentOp a(Terminal missingLabel0) { //INSERTED
 * CODE STRING: RESULT = AssignmentOp.ASSIGN; //END INSERT return RESULT; } };
 */
  ),
  rhs(MULTGLEICH 
/*
 * new Action() { public AssignmentOp a(Terminal missingLabel0) { //INSERTED
 * CODE STRING: RESULT = AssignmentOp.MUL_ASSIGN; //END INSERT return RESULT; } };
 */
  ),
  rhs(DIVGLEICH 
/*
 * new Action() { public AssignmentOp a(Terminal missingLabel0) { //INSERTED
 * CODE STRING: RESULT = AssignmentOp.DIV_ASSIGN; //END INSERT return RESULT; } };
 */
  ),
  rhs(MODGLEICH 
/*
 * new Action() { public AssignmentOp a(Terminal missingLabel0) { //INSERTED
 * CODE STRING: RESULT = AssignmentOp.MOD_ASSIGN; //END INSERT return RESULT; } };
 */
  ),
  rhs(PLUSGLEICH 
/*
 * new Action() { public AssignmentOp a(Terminal missingLabel0) { //INSERTED
 * CODE STRING: RESULT = AssignmentOp.ADD_ASSIGN; //END INSERT return RESULT; } };
 */
  ),
  rhs(MINUSGLEICH 
/*
 * new Action() { public AssignmentOp a(Terminal missingLabel0) { //INSERTED
 * CODE STRING: RESULT = AssignmentOp.SUB_ASSIGN; //END INSERT return RESULT; } };
 */
  ),
  rhs(LSHIFTGLEICH 
/*
 * new Action() { public AssignmentOp a(Terminal missingLabel0) { //INSERTED
 * CODE STRING: RESULT = AssignmentOp.SHL_ASSIGN; //END INSERT return RESULT; } };
 */
  ),
  rhs(RSHIFTGLEICH 
/*
 * new Action() { public AssignmentOp a(Terminal missingLabel0) { //INSERTED
 * CODE STRING: RESULT = AssignmentOp.SHR_ASSIGN; //END INSERT return RESULT; } };
 */
  ),
  rhs(UNDGLEICH 
/*
 * new Action() { public AssignmentOp a(Terminal missingLabel0) { //INSERTED
 * CODE STRING: RESULT = AssignmentOp.AND_ASSIGN; //END INSERT return RESULT; } };
 */
  ),
  rhs(EXPGLEICH 
/*
 * new Action() { public AssignmentOp a(Terminal missingLabel0) { //INSERTED
 * CODE STRING: RESULT = AssignmentOp.XOR_ASSIGN; //END INSERT return RESULT; } };
 */
  ),
  rhs(ODERGLEICH 
/*
 * new Action() { public AssignmentOp a(Terminal missingLabel0) { //INSERTED
 * CODE STRING: RESULT = AssignmentOp.OR_ASSIGN; //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  statement_list,
  rhs(statement 
/*
 * new Action() { public List a(Stmt s) { //INSERTED CODE STRING: RESULT = new
 * LinkedList(); RESULT.add(s); //END INSERT return RESULT; } };
 */
  ),
  rhs(statement_list, statement 
/*
 * new Action() { public List a(List sl, Stmt s) { //INSERTED CODE STRING:
 * RESULT = sl; RESULT.add(s); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  statement,
  rhs(labeled_statement 
/*
 * new Action() { public Stmt a(Stmt ls) { //INSERTED CODE STRING: RESULT = ls;
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(compound_statement 
/*
 * new Action() { public Stmt a(Stmt cs) { //INSERTED CODE STRING: RESULT = cs;
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(expr_stmt 
/*
 * new Action() { public Stmt a(Stmt es) { //INSERTED CODE STRING: RESULT = es;
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(selection_statement 
/*
 * new Action() { public Stmt a(Stmt ss) { //INSERTED CODE STRING: RESULT = ss;
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(iteration_statement 
/*
 * new Action() { public Stmt a(Stmt is) { //INSERTED CODE STRING: RESULT = is;
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(jump_statement 
/*
 * new Action() { public Stmt a(Stmt js) { //INSERTED CODE STRING: RESULT = js;
 * //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  labeled_statement,
  rhs(IDENTIFIER, DOPPELPUNKT, statement 
/*
 * new Action() { public Stmt a(Terminal < String > i, Terminal missingLabel1,
 * Stmt s) { //INSERTED CODE STRING: CmacParser.unsupported(); //END INSERT
 * return RESULT; } };
 */
  ),
  rhs(CASE, const_expr, DOPPELPUNKT, statement 
/*
 * new Action() { public Stmt a(Terminal missingLabel0, Expr ce, Terminal
 * missingLabel2, Stmt s) { //INSERTED CODE STRING: CmacParser.unsupported();
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(DEFAULT, DOPPELPUNKT, statement 
/*
 * new Action() { public Stmt a(Terminal missingLabel0, Terminal missingLabel1,
 * Stmt s) { //INSERTED CODE STRING: CmacParser.unsupported(); //END INSERT
 * return RESULT; } };
 */
  )
)
,
prod(
  compound_statement,
  rhs(LPARSCHWEIF, RPARSCHWEIF 
/*
 * new Action() { public Stmt a(Terminal l, Terminal r) { //INSERTED CODE
 * STRING: RESULT = Stmt.block(Collections.EMPTY_LIST,
 * Collections.EMPTY_LIST).locate(l,r); //END INSERT return RESULT; } };
 */
  ),
  rhs(LPARSCHWEIF, statement_list, RPARSCHWEIF 
/*
 * new Action() { public Stmt a(Terminal l, List sl, Terminal r) { //INSERTED
 * CODE STRING: RESULT = Stmt.block(Collections.EMPTY_LIST, sl).locate(l,r);
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(LPARSCHWEIF, declaration_list, RPARSCHWEIF 
/*
 * new Action() { public Stmt a(Terminal l, List dl, Terminal r) { //INSERTED
 * CODE STRING: RESULT =
 * Stmt.block(ParserHelper.declListToDeclarationList(parser.ps, dl),
 * Collections.EMPTY_LIST).locate(l,r); //END INSERT return RESULT; } };
 */
  ),
  rhs(LPARSCHWEIF, declaration_list, statement_list, RPARSCHWEIF 
/*
 * new Action() { public Stmt a(Terminal l, List dl, List sl, Terminal r) {
 * //INSERTED CODE STRING: RESULT =
 * Stmt.block(ParserHelper.declListToDeclarationList(parser.ps,
 * dl),sl).locate(l,r); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  expr_stmt,
  rhs(SEMIKOLON 
/*
 * new Action() { public Stmt a(Terminal s) { //INSERTED CODE STRING: RESULT =
 * Stmt.nop().locate(s); //END INSERT return RESULT; } };
 */
  ),
  rhs(expression, SEMIKOLON 
/*
 * new Action() { public Stmt a(Expr e, Terminal r) { //INSERTED CODE STRING:
 * RESULT = Stmt.fromExpr(e).locate(e,r); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  selection_statement,
  rhs(IF, LPAR, expression, RPAR, statement 
/*
 * new Action() { public Stmt a(Terminal l, Terminal missingLabel1, Expr e,
 * Terminal missingLabel3, Stmt s) { //INSERTED CODE STRING: RESULT =
 * Stmt.ifThen(e,s).locate(l,s); //END INSERT return RESULT; } };
 */
  ),
  rhs(IF, LPAR, expression, RPAR, statement, ELSE, statement 
/*
 * new Action() { public Stmt a(Terminal l, Terminal missingLabel1, Expr e,
 * Terminal missingLabel3, Stmt ifs, Terminal missingLabel5, Stmt elses) {
 * //INSERTED CODE STRING: RESULT =
 * Stmt.ifThenElse(e,ifs,elses).locate(l,elses); //END INSERT return RESULT; } };
 */
  ),
  rhs(SWITCH, LPAR, expression, RPAR, statement 
/*
 * new Action() { public Stmt a(Terminal missingLabel0, Terminal missingLabel1,
 * Expr e, Terminal missingLabel3, Stmt s) { //INSERTED CODE STRING:
 * CmacParser.unsupported(); //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  case_statement,
  rhs(CASE, const_expr, DOPPELPUNKT, statement 
/*
 * new Action() { public SwitchCase a(Terminal missingLabel0, Expr e, Terminal
 * missingLabel2, Stmt s) { //INSERTED CODE STRING: CmacParser.unsupported();
 * //END INSERT return RESULT; } };
 */
  ),
  rhs(DEFAULT, DOPPELPUNKT, statement 
/*
 * new Action() { public SwitchCase a(Terminal missingLabel0, Terminal
 * missingLabel1, Stmt s) { //INSERTED CODE STRING: CmacParser.unsupported();
 * //END INSERT return RESULT; } };
 */
  )
)
,
prod(
  iteration_statement,
  rhs(WHILE, LPAR, expression, RPAR, statement 
/*
 * new Action() { public Stmt a(Terminal l, Terminal missingLabel1, Expr e,
 * Terminal missingLabel3, Stmt s) { //INSERTED CODE STRING: RESULT =
 * Stmt.whileLoop(e, s).locate(l,s); //END INSERT return RESULT; } };
 */
  ),
  rhs(DO, statement, WHILE, LPAR, expression, RPAR, SEMIKOLON 
/*
 * new Action() { public Stmt a(Terminal l, Stmt s, Terminal missingLabel2,
 * Terminal missingLabel3, Expr e, Terminal missingLabel5, Terminal r) {
 * //INSERTED CODE STRING: RESULT = Stmt.doWhileLoop(s, e).locate(l,r); //END
 * INSERT return RESULT; } };
 */
  ),
  rhs(FOR, LPAR, expr_stmt, expr_stmt, RPAR, statement 
/*
 * new Action() { public Stmt a(Terminal l, Terminal missingLabel1, Stmt e1,
 * Stmt e2, Terminal missingLabel4, Stmt s) { //INSERTED CODE STRING: RESULT =
 * Stmt.forLoop(e1.getExpr(), e2.getExpr(), s).locate(l,s); //END INSERT return
 * RESULT; } };
 */
  ),
  rhs(FOR, LPAR, expr_stmt, expr_stmt, expression, RPAR, statement 
/*
 * new Action() { public Stmt a(Terminal l, Terminal missingLabel1, Stmt e1,
 * Stmt e2, Expr e3, Terminal missingLabel5, Stmt s) { //INSERTED CODE STRING:
 * RESULT = Stmt.forLoop(e1.getExpr(), e2.getExpr(), e3, s).locate(l,s); //END
 * INSERT return RESULT; } };
 */
  )
)
,
prod(
  jump_statement,
  rhs(GOTO, IDENTIFIER, SEMIKOLON 
/*
 * new Action() { public Stmt a(Terminal l, Terminal < String > i, Terminal r) {
 * //INSERTED CODE STRING: CmacParser.unsupported(); //END INSERT return RESULT; } };
 */
  ),
  rhs(CONTINUE, SEMIKOLON 
/*
 * new Action() { public Stmt a(Terminal l, Terminal r) { //INSERTED CODE
 * STRING: RESULT = Stmt.cont().locate(l,r); //END INSERT return RESULT; } };
 */
  ),
  rhs(BREAK, SEMIKOLON 
/*
 * new Action() { public Stmt a(Terminal l, Terminal r) { //INSERTED CODE
 * STRING: RESULT = Stmt.brk().locate(l,r); //END INSERT return RESULT; } };
 */
  ),
  rhs(RETURN, SEMIKOLON 
/*
 * new Action() { public Stmt a(Terminal l, Terminal r) { //INSERTED CODE
 * STRING: RESULT = Stmt.ret().locate(l,r); //END INSERT return RESULT; } };
 */
  ),
  rhs(RETURN, expression, SEMIKOLON 
/*
 * new Action() { public Stmt a(Terminal l, Expr e, Terminal r) { //INSERTED
 * CODE STRING: RESULT = Stmt.ret(e).locate(l,r); //END INSERT return RESULT; } };
 */
  )
)
);
}
}
