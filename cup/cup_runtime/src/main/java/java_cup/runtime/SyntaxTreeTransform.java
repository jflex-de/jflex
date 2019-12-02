package java_cup.runtime;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class SyntaxTreeTransform {

	private static class ChainRemover extends SyntaxTreeDFS.AbstractVisitor{
		public XMLElement root(){
			return stack.pop();
		}
		private Stack<XMLElement> stack = new Stack<XMLElement>();
		@Override
		public void defaultPost(XMLElement arg0, List<XMLElement> arg1) {
			int n = arg1.size();
			if (n>1){
				LinkedList<XMLElement> elems = new LinkedList<>();
				while (n-->0) elems.addFirst(stack.pop());
				XMLElement ne = new XMLElement.NonTerminal(arg0.getTagname(),0,elems.toArray(new XMLElement[0]));
				stack.push(ne);
				return;
			}
			//if (n==1){}
			if (n==0) stack.push(arg0);
		}

		@Override
		public void defaultPre(XMLElement arg0, List<XMLElement> arg1) {}
		
	}
	public static XMLElement removeUnaryChains(XMLElement elem) {
		ChainRemover cr = new ChainRemover();
		SyntaxTreeDFS.dfs(elem, cr);
		return cr.root();
	}

}
