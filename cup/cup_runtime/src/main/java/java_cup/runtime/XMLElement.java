package java_cup.runtime;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
import java_cup.runtime.ComplexSymbolFactory.Location;

public abstract class XMLElement {
	public abstract List<XMLElement> selectById(String s);
	public static void dump(XMLStreamWriter writer, XMLElement elem, String ... blacklist) throws XMLStreamException {
		dump(null,writer,elem,blacklist);
	}
	public static void dump(ScannerBuffer buffer, XMLStreamWriter writer, XMLElement elem, String ... blacklist) throws XMLStreamException {
		writer.writeStartDocument("utf-8", "1.0");
		writer.writeProcessingInstruction("xml-stylesheet","href=\"tree.xsl\" type=\"text/xsl\"");
		writer.writeStartElement("document");
		
		if (blacklist.length>0) {
			writer.writeStartElement("blacklist");
			for (String s: blacklist){
				writer.writeStartElement("symbol");
				writer.writeCharacters(s);
				writer.writeEndElement();
			}
			writer.writeEndElement();
		}
		
		writer.writeStartElement("parsetree");
		elem.dump(writer);
		writer.writeEndElement();

		if (buffer!=null){
			writer.writeStartElement("tokensequence");
			for (Symbol s:buffer.getBuffered()){
				if (s instanceof ComplexSymbol){
					ComplexSymbol cs = (ComplexSymbol)s;
					if (cs.value!=null){
						writer.writeStartElement("token");
						writer.writeAttribute("name",cs.getName());
						cs.getLeft().toXML(writer, "left");
						writer.writeCharacters(cs.value+"");
						cs.getRight().toXML(writer, "right");
						writer.writeEndElement();
					} else
					{
						writer.writeStartElement("keyword");
						writer.writeAttribute("left",cs.getLeft()+"");
						writer.writeAttribute("right",cs.getRight()+"");
						writer.writeCharacters(cs.getName()+"");
						writer.writeEndElement();
					}
				}
				else 
					if (s instanceof Symbol) {
						writer.writeStartElement("token");
						writer.writeCharacters(s.toString());
						writer.writeEndElement();						
					}
			}
			writer.writeEndElement();
		}
		writer.writeEndElement();
		writer.writeEndDocument();
		writer.flush();
		writer.close();
	}
	protected String tagname;
        public String getTagname() { return tagname; }
	public abstract Location right();
	public abstract Location left();
	protected abstract void dump(XMLStreamWriter writer) throws XMLStreamException;
        public List<XMLElement> getChildren() {return new LinkedList<XMLElement>(); };
        public boolean hasChildren() { return false; };
	public static class NonTerminal extends XMLElement {
	    public boolean hasChildren()  { return !list.isEmpty(); }
	    public List<XMLElement> getChildren()  { return list; }
	    @Override
		public List<XMLElement> selectById(String s) {
			LinkedList<XMLElement> response= new LinkedList<XMLElement>();
			if (tagname.equals(s))
				response.add(this);
			for (XMLElement e : list){
				List<XMLElement> selection =e.selectById(s);
				response.addAll(selection);
			}
			return response;
		}
		private int variant;
	    public int getVariant() {
		return variant;
	    }
		LinkedList<XMLElement> list;
		public NonTerminal(String tagname, int variant, XMLElement... l) {
			this.tagname=tagname;
			this.variant=variant;
			list = new LinkedList<XMLElement>(Arrays.asList(l));
		}

		public Location left() {
			for (XMLElement e : list){
				Location loc = e.left();
				if (loc!=null) return loc;
			}
			return null;	
		}
		public Location right() {
			for (Iterator<XMLElement> it = list.descendingIterator();it.hasNext();){
				 Location loc = it.next().right();
				 if (loc!=null) return loc;
			}
			return null;
		}

		public String toString() {
			if (list.isEmpty()){
				return "<nonterminal id=\"" + tagname +"\" variant=\""+variant+"\" />" ;
			}
			String ret = "<nonterminal id=\"" + tagname +"\" left=\"" + left()
					+ "\" right=\"" + right() + "\" variant=\""+variant+"\">";
			for (XMLElement e : list)
				ret += e.toString();
			return ret + "</nonterminal>";
		}
		@Override
		protected void dump(XMLStreamWriter writer) throws XMLStreamException {
			writer.writeStartElement("nonterminal");
			writer.writeAttribute("id", tagname);
			writer.writeAttribute("variant", variant+"");
//			if (!list.isEmpty()){
				Location loc = left();
				if (loc!=null) loc.toXML(writer, "left");
//			}
			for (XMLElement e:list)
				e.dump(writer);
			loc = right();
			if (loc!=null) loc.toXML(writer, "right");
			writer.writeEndElement();
		}
	}

	public static class Error extends XMLElement {
	    public boolean hasChildren()  { return false; }
	    @Override
		public List<XMLElement> selectById(String s) {
			return new LinkedList<XMLElement>();
		}
		Location l,r;
		public Error(Location l, Location r) {
			this.l=l;
			this.r=r;
		}
		public Location left() {	return l; 	}
		public Location right() {	return r;	}

		public String toString() {
			return  "<error left=\"" + l + "\" right=\"" + r + "\"/>";
		}
		@Override
		protected void dump(XMLStreamWriter writer) throws XMLStreamException {
			writer.writeStartElement("error");
			writer.writeAttribute("left", left()+"");
			writer.writeAttribute("right", right()+"");
			writer.writeEndElement();
		}
	}
	
	public static class Terminal extends XMLElement {
	    public boolean hasChildren()  { return false; }
	    public List<XMLElement> selectById(String s) {
			List<XMLElement> ret = new LinkedList<XMLElement>();
			if (tagname.equals(s)) { ret.add(this);	}
			return ret;
		};
		Location l, r;
		Object value;

		public Terminal(Location l, String symbolname, Location r) {
			this(l, symbolname, null, r);
		}

		public Terminal(Location l, String symbolname, Object i, Location r) {
			this.l = l;
			this.r = r;
			this.value = i;
			this.tagname = symbolname;
		}

		public Object   value() {return value; }
		public Location left() {	return l; 	}
		public Location right() {	return r;	}

		public String toString() {
			return (value == null) ? "<terminal id=\"" + tagname + "\"/>"
					: "<terminal id=\"" + tagname + "\" left=\"" + l
							+ "\" right=\"" + r + "\">" + value
							+ "</terminal>";
		}
		@Override
		protected void dump(XMLStreamWriter writer) throws XMLStreamException {
			writer.writeStartElement("terminal");
			writer.writeAttribute("id", tagname);
			writer.writeAttribute("left", left()+"");
			writer.writeAttribute("right", right()+"");
			if (value!=null) writer.writeCharacters(value+"");
			writer.writeEndElement();
		}
	}
}
