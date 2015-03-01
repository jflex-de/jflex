package edu.tum.cup2.semantics;

/**
 * This inteface may be implemented and used in the context
 * of a call to {@link edu.tum.cup2.parser.LRParser#register(ParserObserver)}
 * in order to listen to events created by the parser
 * or by the specification in the form of
 * {@linkedu.tum.cup2.spec.CUPSpecification#register(ParserObserver)}
 *
 * @author Stefan Dangl
 **/
public interface ParserObserver extends java.io.Serializable
{

  public void syntax_error(ErrorInformation error);

}
