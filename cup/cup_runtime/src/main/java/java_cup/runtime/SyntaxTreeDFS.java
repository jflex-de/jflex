package java_cup.runtime;

import java.util.HashMap;
import java.util.List;

public class SyntaxTreeDFS {
	public static interface ElementHandler {
		public void handle(XMLElement parent, List<XMLElement> children);
	}
	public static abstract class AbstractVisitor implements Visitor {
		private HashMap<String, ElementHandler> preMap = new HashMap<>();
		private HashMap<String, ElementHandler> postMap = new HashMap<>();
		public abstract void defaultPre(XMLElement element, List<XMLElement> children);
		public abstract void defaultPost(XMLElement element,List<XMLElement> children);
		@Override
		public void preVisit(XMLElement element) {
			ElementHandler handler = preMap.get(element.tagname);
			if (handler==null) {
				defaultPre(element,element.getChildren());
			} else
				handler.handle(element, element.getChildren());
		}
		@Override
		public void postVisit(XMLElement element) {
			ElementHandler handler = postMap.get(element.tagname);
			if (handler==null) {
				defaultPost(element,element.getChildren());
			} else
				handler.handle(element, element.getChildren());			
		}
		public void registerPreVisit(String s,ElementHandler h){
			preMap.put(s, h);
		}
		public void registerPostVisit(String s, ElementHandler h){
			postMap.put(s, h);
		}
	}
	public static interface Visitor {
		public void preVisit(XMLElement element);
		public void postVisit(XMLElement element);
	}
	public static void dfs(XMLElement element, Visitor visitor){
		visitor.preVisit(element);
		for (XMLElement el :element.getChildren()){
			dfs(el,visitor);
		}
		visitor.postVisit(element);
	}
}
