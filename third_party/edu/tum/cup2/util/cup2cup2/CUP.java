package edu.tum.cup2.util.cup2cup2;

import java.util.*;

import edu.tum.cup2.generator.LALR1Generator;
import edu.tum.cup2.generator.exceptions.GeneratorException;
import edu.tum.cup2.grammar.NonTerminal;
import edu.tum.cup2.grammar.SpecialTerminals;
import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.spec.CUP2Specification;
import edu.tum.cup2.semantics.SymbolValue;
import edu.tum.cup2.semantics.Action;
import static edu.tum.cup2.util.cup2cup2.CUP.Terminals.*;
import static edu.tum.cup2.util.cup2cup2.CUP.NonTerminals.*;

public class CUP extends CUP2Specification {

	public static void main(String[] args) {
		CUP2Specification spec = new CUP();
		try {
			LALR1Generator gen = new LALR1Generator(spec);
		} catch (GeneratorException e) {
			e.printStackTrace();
		}
	}

	public enum Terminals implements Terminal {
		PACKAGE, RBRACK, LT, STAR, LBRACK, NON, TERMINAL, CODE_STRING, PERCENT_PREC, RIGHT, ID, SEMI, ACTION, CODE, COLON, COLON_COLON_EQUALS, NONTERMINAL, IMPORT, NONASSOC, QUESTION, COMMA, LEFT, SCAN, GT, START, SUPER, DOT, EXTENDS, INIT, PARSER, PRECEDENCE, BAR, WITH
	}

	public class CODE_STRING extends SymbolValue<String> {
	};

	public class ID extends SymbolValue<String> {
	};

	public enum NonTerminals implements NonTerminal {
		spec, import_id, start_spec, robust_id, production_list, wildcard, prod_part_list, term_name_list, package_spec, typearglist, code_parts, type_id, prod_part, rhs_list, typearguement, import_list, multipart_id, opt_semi, terminal_list, rhs, non_terminal, precedence_list, symbol_list
	}

	public class import_id extends SymbolValue<String> {
	};

	public class spec extends SymbolValue<Specification> {
	};

	public class start_spec extends SymbolValue<String> {
	};

	public class robust_id extends SymbolValue<String> {
	};

	public class production_list extends
			SymbolValue<Map<String, RightHandSide>> {
	};

	public class wildcard extends SymbolValue<String> {
	};

	public class prod_part_list extends SymbolValue<List<Part>> {
	};

	public class term_name_list extends SymbolValue<List<String>> {
	};

	public class package_spec extends SymbolValue<String> {
	};

	public class typearglist extends SymbolValue<String> {
	};

	public class code_parts extends SymbolValue<ArrayList<String>> {
	};

	public class type_id extends SymbolValue<String> {
	};

	public class prod_part extends SymbolValue<Part> {
	};

	public class rhs_list extends SymbolValue<List<RightHandSide>> {
	};

	public class typearguement extends SymbolValue<String> {
	};

	public class import_list extends SymbolValue<List<String>> {
	};

	public class multipart_id extends SymbolValue<String> {
	};

	public class terminal_list extends SymbolValue<List<String>> {
	};

	public class rhs extends SymbolValue<RightHandSide> {
	};

	public class non_terminal extends SymbolValue<Boolean> {
	};

	public class precedence_list extends SymbolValue<Precedences> {
	};

	public class symbol_list extends SymbolValue<SymbolRegistry> {
	};

	public CUP() {

		grammar(

		prod(spec, rhs(package_spec, import_list, code_parts, symbol_list,
				precedence_list, start_spec, production_list), new Action() {
			public Specification a(String pack, List<String> imp,
					List<String> codes, SymbolRegistry syms, Precedences precs,
					String start, Map<String, RightHandSide> prods) {
				return new Specification(pack, imp, codes, syms, precs, start,
						prods);
			}
		}), prod(robust_id, rhs(ID), new Action() {
			public String a(String id) {
				return id;
			}
		}, rhs(CODE), new Action() {
			public String a() {
				return "code";
			}
		}, rhs(ACTION), new Action() {
			public String a() {
				return "action";
			}
		}, rhs(PARSER), new Action() {
			public String a() {
				return "parser";
			}
		}, rhs(TERMINAL), new Action() {
			public String a() {
				return "terminal";
			}
		}, rhs(NON), new Action() {
			public String a() {
				return "non";
			}
		}, rhs(NONTERMINAL), new Action() {
			public String a() {
				return "nonterminal";
			}
		}, rhs(INIT), new Action() {
			public String a() {
				return "init";
			}
		}, rhs(SCAN), new Action() {
			public String a() {
				return "scan";
			}
		}, rhs(WITH), new Action() {
			public String a() {
				return "with";
			}
		}, rhs(START), new Action() {
			public String a() {
				return "start";
			}
		}, rhs(PRECEDENCE), new Action() {
			public String a() {
				return "precedence";
			}
		}, rhs(LEFT), new Action() {
			public String a() {

				return "left";
			}
		}, rhs(RIGHT), new Action() {
			public String a() {
				return "right";
			}
		}, rhs(NONASSOC), new Action() {
			public String a() {
				return "nonassoc";
			}
		}, rhs(SpecialTerminals.Error)), prod(

		terminal_list,

		rhs(terminal_list, COMMA, ID), new Action() {
			public List<String> a(List<String> list, String id) {
				List<String> RESULT = list;
				list.add(id);
				return RESULT;
			}
		}, rhs(ID), new Action() {
			public List<String> a(String id) {

				LinkedList<String> RESULT = new LinkedList<String>();
				RESULT.add(id);
				return RESULT;
			}
		}), prod(start_spec, rhs(START, WITH, ID, SEMI), new Action() {
			public String a(String id) {
				return id;
			}
		}, rhs(), new Action() {
			public String a() {
				return null;
			}
		}), prod(package_spec, rhs(PACKAGE, multipart_id, SEMI), new Action() {
			public String a(String id) {
				return id;
			}
		}, rhs(), new Action() {
			public String a() {
				return null;
			}
		}), prod(import_list, rhs(import_list, IMPORT, import_id, SEMI),
				new Action() {
					public List<String> a(List<String> list, String id) {
						List<String> RESULT = list;
						list.add("import " + id + ";\n");
						return RESULT;
					}
				}, rhs(), new Action() {
					public List<String> a() {
						return new LinkedList<String>();
					}
				}), prod(opt_semi, rhs(), rhs(SEMI)), prod(code_parts, rhs(),
				new Action() {
					public List<String> a() {
						return new ArrayList<String>(4);
					}
				}, rhs(code_parts, ACTION, CODE, CODE_STRING, opt_semi),
				new Action() {
					public List<String> a(List<String> p, String str) {
						p.set(0, str);
						return p;
					}
				}, rhs(code_parts, PARSER, CODE, CODE_STRING, opt_semi),
				new Action() {
					public List<String> a(List<String> p, String str) {
						p.set(1, str);
						return p;
					}
				}, rhs(code_parts, INIT, WITH, CODE_STRING, opt_semi),
				new Action() {
					public List<String> a(List<String> p, String str) {
						p.set(2, str);
						return p;
					}
				}, rhs(code_parts, SCAN, WITH, CODE_STRING, opt_semi),
				new Action() {
					public List<String> a(List<String> p, String str) {
						p.set(3, str);
						return p;
					}
				}), prod(symbol_list, rhs(symbol_list, non_terminal, type_id,
				term_name_list, SEMI), new Action() {
			public SymbolRegistry a(SymbolRegistry list, Boolean nt, String ty,
					List<String> terms) {
				if (!nt)
					list.putTerminals(ty, terms);
				else
					list.putNonTerminals(ty, terms);
				return list;
			}
		}, rhs(symbol_list, non_terminal, term_name_list, SEMI), new Action() {
			public SymbolRegistry a(SymbolRegistry list, Boolean nt,
					List<String> terms) {
				if (!nt)
					list.putTerminals("Object", terms);
				else
					list.putNonTerminals("Object", terms);

				return list;
			}
		}, rhs(), new Action() {
			public SymbolRegistry a() {
				return new SymbolRegistry();
			}
		}), prod(non_terminal, rhs(NON, TERMINAL), new Action() {
			public boolean a() {
				return true;
			}
		}, rhs(NONTERMINAL), new Action() {
			public boolean a() {
				return true;
			}
		}, rhs(TERMINAL), new Action() {
			public boolean a() {
				return false;
			}
		}), prod(import_id, rhs(multipart_id, DOT, STAR), new Action() {
			public String a(String id) {
				return id + ".*";
			}
		}, rhs(multipart_id), new Action() {
			public String a(String id) {
				return id;
			}
		}), prod(multipart_id, rhs(multipart_id, DOT, robust_id), new Action() {
			public String a(String id1, String id2) {
				return id1 + "." + id2;
			}
		}, rhs(multipart_id, LT, typearglist, GT), new Action() {
			public String a(String id, String list) {
				return id + "<" + list + ">";
			}
		}, rhs(robust_id), new Action() {
			public String a(String id) {
				return id;
			}
		}), prod(typearglist, rhs(typearguement), new Action() {
			public String a(String ta) {
				return ta;
			}
		}, rhs(typearglist, COMMA, typearguement), new Action() {
			public String a(String list, String ta) {
				return list + "," + ta;
			}
		}), prod(typearguement, rhs(type_id), new Action() {
			public String a(String id) {
				return id;
			}
		}, rhs(wildcard), new Action() {
			public String a(String wc) {
				return wc;
			}
		}), prod(wildcard, rhs(QUESTION), new Action() {
			public String a() {
				return "?";
			}
		}, rhs(QUESTION, EXTENDS, type_id), new Action() {
			public String a(String ty) {
				return "? extends " + ty;
			}
		}, rhs(QUESTION, SUPER, type_id), new Action() {
			public String a(String ty) {
				return "? super " + ty;
			}
		}), prod(type_id, rhs(multipart_id), new Action() {
			public String a(String id) {
				return id;
			}
		}, rhs(type_id, LBRACK, RBRACK), new Action() {
			public String a(String ty) {
				return ty + "[]";
			}
		}), prod(precedence_list, rhs(precedence_list, PRECEDENCE, LEFT,
				terminal_list, SEMI), new Action() {
			public Precedences a(Precedences p, List<String> list) {
				p.left(list);
				return p;
			}
		}, rhs(precedence_list, PRECEDENCE, RIGHT, terminal_list, SEMI),
				new Action() {
					public Precedences a(Precedences p, List<String> list) {
						p.right(list);
						return p;
					}
				}, rhs(precedence_list, PRECEDENCE, NONASSOC, terminal_list,
						SEMI), new Action() {
					public Precedences a(Precedences p, List<String> list) {
						p.nonassoc(list);
						return p;
					}
				}, rhs(), new Action() {
					public Precedences a() {

						return new Precedences();
					}
				}), prod(term_name_list, rhs(term_name_list, COMMA, ID),
				new Action() {
					public List<String> a(List<String> list, String id) {
						list.add(id);
						return list;
					}
				}, rhs(ID), new Action() {
					public List<String> a(String id) {
						List<String> RESULT = new LinkedList<String>();
						RESULT.add(id);
						return RESULT;
					}
				}), prod(prod_part_list, rhs(prod_part_list, prod_part),
				new Action() {
					public List<Part> a(List<Part> list, Part part) {
						list.add(part);
						return list;
					}
				}, rhs(), new Action() {
					public List<Part> a() {
						return new LinkedList<Part>();
					}
				}), prod(prod_part, rhs(ID, COLON, robust_id), new Action() {
			public Part a(String id, String label) {
				return Part.id(id, label);
			}
		}, rhs(ID), new Action() {
			public Part a(String id) {
				return Part.id(id);
			}
		}, rhs(CODE_STRING), new Action() {
			public Part a(String str) {
				return Part.code(str);
			}
		}), prod(production_list, rhs(production_list, ID, COLON_COLON_EQUALS,
				rhs_list, SEMI), new Action() {
			public Map<String, List<RightHandSide>> a(
					Map<String, List<RightHandSide>> pl, String id,
					List<RightHandSide> rhs) {
				pl.get(id).addAll(rhs);
				return pl;
			}
		}, rhs(), new Action() {
			public Map<String, RightHandSide> a() {

				return new HashMap<String, RightHandSide>();
			}
		}), prod(rhs, rhs(prod_part_list, PERCENT_PREC, ID), new Action() {
			public RightHandSide a(List<Part> list, String id) {
				return new RightHandSide(list, id);
			}
		}, rhs(prod_part_list), new Action() {
			public RightHandSide a(List<Part> list) {
				return new RightHandSide(list);
			}
		}), prod(rhs_list, rhs(rhs_list, BAR, rhs), new Action() {
			public List<RightHandSide> a(List<RightHandSide> list,
					RightHandSide r) {
				list.add(r);
				return list;
			}
		}, rhs(rhs), new Action() {
			public List<RightHandSide> a(RightHandSide node) {
				List<RightHandSide> RESULT = new LinkedList<RightHandSide>();
				RESULT.add(node);
				return RESULT;
			}
		}));
	}
}