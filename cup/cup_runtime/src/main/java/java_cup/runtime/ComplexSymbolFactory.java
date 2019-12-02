package java_cup.runtime;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Default Implementation for SymbolFactory, creates
 * plain old Symbols
 *
 * @version last updated 27-03-2006
 * @author Michael Petter
 */

/* *************************************************
  class DefaultSymbolFactory
  interface for creating new symbols  
 ***************************************************/
public class ComplexSymbolFactory implements SymbolFactory{
    public static class Location {
        private String unit="unknown";
        private int line, column, offset=-1;
        /**
         * Copy Constructor for other ComplexSymbolFactory based Locations
         * @param other
         */
        public Location(Location other){
        	this(other.unit,other.line,other.column,other.offset);
        }
	/**
	 * Location Object
	 * stores compilation unit, line, column and offset to the file start
	 * @param unit      compilation unit, e.g. file name
	 * @param line      line number
	 * @param column    column number
	 * @param offset    offset from file start
	 */
    public Location(String unit, int line, int column, int offset){
	   this(unit,line,column);
	   this.offset=offset;
	}
	/**
	 * Location Object
	 * stores compilation unit, line and column
	 * @param unit      compilation unit, e.g. file name
	 * @param line      line number
	 * @param column    column number
	 */
        public Location(String unit, int line, int column){
            this.unit=unit;
            this.line=line;
            this.column=column;
        }
	/**
	 * Location Object
	 * stores line, column and offset to the file start
	 * @param line      line number
	 * @param column    column number
	 * @param offset    offset from file start
	 */
        public Location(int line, int column, int offset){
	    this(line,column);
	    this.offset=offset;
	}
	/**
	 * Location Object
	 * stores line and column 
	 * @param line      line number
	 * @param column    column number
	 */
        public Location(int line, int column){
            this.line=line;
            this.column=column;
        }
	/**
	 * getColumn
	 * @returns column if known, else -1
	 */
        public int getColumn(){
            return column;
        }
	/**
	 * getLine
	 * @returns line if known, else -1
	 */
        public int getLine(){
            return line;
        }
        /**
         * move moves this Location by the given differences. 
         * @param linediff
         * @param coldiff
         * @param offsetdiff
         */
        public void move(int linediff, int coldiff, int offsetdiff){
    		if (this.line >= 0)
    			this.line += linediff;
    		if (this.column >= 0)
    			this.column += coldiff;
    		if (this.offset >= 0)
    			this.offset += offsetdiff;
        }
        /**
         * Cloning factory method
         * @param other
         * @return new cloned Location
         */
        public static Location clone(Location other){
        	return new Location(other);
        }
	/**
	 * getUnit
	 * @returns compilation unit if known, else 'unknown'
	 */
        public String getUnit(){
            return unit;
        }
	/**
	 * getLine
	 * @returns line if known, else -1
	 */
        public String toString(){
            return getUnit()+":"+getLine()+"/"+getColumn()+"("+offset+")";
        }
        /**
         * Writes the location information directly into an XML document
         * @param writer the destination XML Document
         * @param orientation adds details about the orientation of this location as an attribute; often used with the strings "left" or "right"
         * @throws XMLStreamException
         */
        public void toXML(XMLStreamWriter writer,String orientation) throws XMLStreamException {
        	writer.writeStartElement("location");
        	writer.writeAttribute("compilationunit", unit);
        	writer.writeAttribute("orientation", orientation);
        	writer.writeAttribute("linenumber", line+"");
        	writer.writeAttribute("columnnumber", column+"");
        	writer.writeAttribute("offset", offset+"");
        	writer.writeEndElement();
        }
	/**
	 * getOffset
	 * @returns offset to start if known, else -1
	 */
        public int getOffset(){
            return offset;
        }
    }
    /**
     * ComplexSymbol with detailed Location Informations and a Name
     */
    public static class ComplexSymbol extends Symbol {
        protected String name;
        public Location xleft,xright;
        public ComplexSymbol(String name, int id) {
            super(id);
            this.name=name;
        }
        public ComplexSymbol(String name, int id, Object value) {
            super(id,value);
            this.name=name;
        }
        public String toString(){
            if (xleft==null || xright==null) return "Symbol: "+name;
            return "Symbol: "+name+" ("+xleft+" - "+xright+")";
        }
        public String getName(){
        	return name;
        }
        public ComplexSymbol(String name, int id, int state) {
            super(id,state);
            this.name=name;
        }
        public ComplexSymbol(String name, int id, Symbol left, Symbol right) {
            super(id,left,right);
            this.name=name;
            if (left!=null)  this.xleft = ((ComplexSymbol)left).xleft;
            if (right!=null) this.xright= ((ComplexSymbol)right).xright;
        }
        public ComplexSymbol(String name, int id, Location left, Location right) {
            super(id,left.offset,right.offset);
            this.name=name;
            this.xleft=left;
            this.xright=right;
        }
        public ComplexSymbol(String name, int id, Symbol left, Symbol right, Object value) {
            super(id,left.left,right.right,value);
            this.name=name;
            if (left!=null)  this.xleft = ((ComplexSymbol)left).xleft;
            if (right!=null) this.xright= ((ComplexSymbol)right).xright;
        }
        public ComplexSymbol(String name, int id, Symbol left, Object value) {
            super(id,left.right,left.right,value);
            this.name=name;
            if (left!=null) { 
            	this.xleft = ((ComplexSymbol)left).xright;
            	this.xright= ((ComplexSymbol)left).xright;
            }
        }
        public ComplexSymbol(String name, int id, Location left, Location right, Object value) {
            super(id,left.offset,right.offset,value);
            this.name=name;
            this.xleft=left;
            this.xright=right;
        }
        public Location getLeft(){
            return xleft;
        }
        public Location getRight(){
            return xright;
        }
    }


    // Factory methods
    /**
     * newSymbol
     * creates a complex symbol with Location objects for left and right boundaries;
     * this is used for terminals with values!
     */
    public Symbol newSymbol(String name, int id, Location left, Location right, Object value){
        return new ComplexSymbol(name,id,left,right,value);
    }
    /**
     * newSymbol
     * creates a complex symbol with Location objects for left and right boundaries;
     * this is used for terminals without values!
     */
    public Symbol newSymbol(String name, int id, Location left, Location right){
        return new ComplexSymbol(name,id,left,right);
    }
	public Symbol newSymbol(String name, int id, Symbol left, Object value) {
		return new ComplexSymbol(name,id,left,value);
	}
    public Symbol newSymbol(String name, int id, Symbol left, Symbol right, Object value){
        return new ComplexSymbol(name,id,left,right,value);
    }
    public Symbol newSymbol(String name, int id, Symbol left, Symbol right){
        return new ComplexSymbol(name,id,left,right);
    }
    public Symbol newSymbol(String name, int id){
        return new ComplexSymbol(name,id);
    }
    public Symbol newSymbol(String name, int id, Object value){
        return new ComplexSymbol(name,id,value);
    }
    public Symbol startSymbol(String name, int id, int state){
        return new ComplexSymbol(name,id,state);
    }
}
