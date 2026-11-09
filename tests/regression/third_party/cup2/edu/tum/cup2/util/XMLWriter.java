package edu.tum.cup2.util;

import java.io.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;


/**
 * This class contains some helper functions
 * to write XML files, using Java's built-in JAXP.
 *
 * @author Andreas Wenger
 */
public class XMLWriter
{
  
  
	/**
   * Creates an empty DOM document.
   */
  public static Document createEmptyDocument()
  {
    try
    {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      return builder.newDocument();
    }
    catch (Exception ex)
    {
      throw new RuntimeException("Creating a new DOM document failed: " +
        ex.toString());
    }
  }
  

  /**
   * Writes the given XML document into a file
   * at the given path.
   */
  public static void writeFile(Document doc, String path)
    throws IOException
  {
    File f = new File(path);
    FileOutputStream fos = new FileOutputStream(f);
    
    try
    {
      TransformerFactory tFactory =
        TransformerFactory.newInstance();
      Transformer transformer = tFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(
        "{http://xml.apache.org/xslt}indent-amount", "2");
      
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(fos);
      
      transformer.transform(source, result);
    }
    catch (Exception ex)
    {
      throw new IOException("Problem with XML-Transformer: " +
        ex.toString());
    }
  }
  
  
  /**
   * Adds and returns a new element with the given
   * name as a child of the given element.
   */
  public static Element addElement(String name, Node parentElement)
  {
    //get the parent document.
    //parentElement may be the document itself.
    Document parentDoc = parentElement.getOwnerDocument();
    Document doc = (parentDoc != null ? parentDoc : (Document) parentElement);
    //add element
    Node ret = doc.createElement(name);
    parentElement.appendChild(ret);
    return (Element) ret;
  }
  
  
  /**
   * Adds and returns a new element with the given
   * name as a child of the given element,using the given text content.
   */
  public static Element addElement(String name, String text, Node parentElement)
  {
    Element ret = addElement(name, parentElement);
    ret.setTextContent(text);
    return ret;
  }
  
  
  /**
   * Adds a new attribute with the given
   * name and value to the given element.
   */
  public static void addAttribute(Node element, String name, String value)
  {
    ((Element) element).setAttribute(name, value);
  }
  
  
}
