package java_cup.runtime;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SyntaxTreeXPath {
	public static List<XMLElement> query(String query, XMLElement element){
		if (query.startsWith("/")) query=query.substring(1);
		return query0(new LinkedList<String>(Arrays.asList( query.split("/"))),0,element,0);
	}
	private static List<XMLElement> query0(List<String> q,int idx, XMLElement element,int seq){
		
		if (q.get(idx).isEmpty()) { // match deeper descendant q[1]
			return matchDeeperDescendant(q,idx+1 ,element,seq);
		}
		List<XMLElement> l = new LinkedList<XMLElement>();
		
		
		if (!match(q.get(idx),element,seq)) return new LinkedList();
		if (q.size()-1==idx) return singleton(element);
		List<XMLElement> children = element.getChildren();
		for (int i=0; i< children.size();i++){
			XMLElement child= children.get(i); 
			l.addAll(query0(q,idx+1,child,i));
		}
		return l;
	}
	private static List<XMLElement> matchDeeperDescendant(List<String> query,int idx, XMLElement element, int seq){
		if (query.size()<=idx) return singleton(element);
		boolean matches = match(query.get(idx),element,seq); 
		List<XMLElement> l = new LinkedList<XMLElement>();
		List<XMLElement> children = element.getChildren();
		if (matches) return query0(query,idx,element,seq);
		for (int i=0; i< children.size();i++){
			XMLElement child= children.get(i);
			l.addAll(matchDeeperDescendant(query, idx, child,i));
		}
		return l;
	}
	private static boolean match(String m,XMLElement elem,int seq){
//		System.out.println("Matching "+elem.tagname+" with "+m);
		boolean result = true;
		String[] name = m.split("\\[");
		String[] tag = name[0].split("\\*");
		if (tag[0].isEmpty()) { // start is wildcard
			if (tag.length>2)
				result &= elem.tagname.contains(tag[1]);
			else	
				if (tag.length==2)
					result &= elem.tagname.endsWith(tag[1]);
				else
					result &= false;
		} else { // match with start
			if (tag.length==2)
				result &=elem.tagname.startsWith(tag[1]);
			else
				result = elem.tagname.equals(tag[0]);
		}
		for (int i=1; i<name.length; i++) {
			String predicate= name[i];
			if (!predicate.endsWith("]")) return false;
			predicate=predicate.substring(0, predicate.length()-1);
			
			if (predicate.startsWith("@")){
				if (predicate.substring(1).startsWith("variant"))
					if ((elem instanceof XMLElement.NonTerminal)&& Integer.parseInt(predicate.substring(9))==((XMLElement.NonTerminal)elem).getVariant())
							result&=true;
					else
						return false;
				else return false;
			} else 
			if (predicate.matches("\\d+")){
				result &= Integer.parseInt(predicate)==seq;
			}
			else 
				return false; // submatch
		}
		return result;
	}
	private static List<XMLElement> singleton(XMLElement elem){
		LinkedList<XMLElement> l = new LinkedList<XMLElement>();
		l.add(elem);
		return l;
	}
} 
