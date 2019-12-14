package edu.tum.cup2.io;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.tum.cup2.grammar.Grammar;
import edu.tum.cup2.grammar.NonTerminal;
import edu.tum.cup2.grammar.SpecialNonTerminals;
import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.parser.states.LRParserState;
import edu.tum.cup2.parser.tables.LRActionTable;
import edu.tum.cup2.parser.tables.LRGoToTable;
import edu.tum.cup2.parser.tables.LRParsingTable;
import edu.tum.cup2.util.XMLWriter;


/**
 * This class allows to create a HTML dump of a {@link LRParsingTable}.
 * 
 * @author Andreas Wenger
 */
public class LRParsingTableDump
{
	
	
	/**
	 * Creates a HTML dump of the given {@link LRParsingTable}, using the given {@link File}.
	 */
	public static void dumpToHTML(LRParsingTable table, File file)
	{
		//create document
		Document doc = XMLWriter.createEmptyDocument();
		//head
		Element eHTML = XMLWriter.addElement("html", doc);
		eHTML.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
		Element eHead = XMLWriter.addElement("head", eHTML);
		Element eMeta = XMLWriter.addElement("meta", eHead);
		eMeta.setAttribute("content-type", "text/html; charset=UTF-8");
		XMLWriter.addElement("title", "Parsing table dump", eHead);
		//body
		Element eBody = XMLWriter.addElement("body", eHTML);
		//productions
		Grammar grammar = table.getGrammar();
		for (int i = 0; i < grammar.getProductionCount(); i++)
		{
			XMLWriter.addElement("p", ""+grammar.getProductionAt(i), eBody);
		}
		//table
		Element eTable = XMLWriter.addElement("table", eBody);
		eTable.setAttribute("border", "1");
		//table header
		LRActionTable at = table.getActionTable();
		LRGoToTable gt = table.getGotoTable();
		Element eTR = XMLWriter.addElement("tr", eTable);
		XMLWriter.addElement("th", "state", eTR);
		for (Terminal terminal : at.getColumns())
		{
			XMLWriter.addElement("th", terminal.toString(), eTR);
		}
		for (NonTerminal nonTerminal : gt.getColumns())
		{
			if (nonTerminal != SpecialNonTerminals.StartLHS)
				XMLWriter.addElement("th", nonTerminal.toString(), eTR);
		}
		//cells
		for (LRParserState state : table.getStates())
		{
			eTR = XMLWriter.addElement("tr", eTable);
			XMLWriter.addElement("td", ""+state.getID(), eTR);
			for (Terminal terminal : at.getColumns())
			{
				String hasAction = "";
				XMLWriter.addElement("td", at.get(state, terminal).toString() + hasAction, eTR);
			}
			for (NonTerminal nonTerminal : gt.getColumns())
			{
				if (nonTerminal != SpecialNonTerminals.StartLHS)
					XMLWriter.addElement("td", gt.get(state, nonTerminal).toString(), eTR);
			}
		}
		//write to file
		try
		{
			XMLWriter.writeFile(doc, file.getAbsolutePath());
		}
		catch (Exception ex)
		{
			//TODO: global error handling
			ex.printStackTrace();
		}
	}
	
}
