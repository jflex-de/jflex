package java_cup.runtime;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ScannerBuffer implements Scanner {
	private Scanner inner;
	private List<Symbol> buffer = new LinkedList<Symbol>();
	/**
	 * Wraps around a custom scanner and stores all so far produced tokens in a buffer
	 * @param inner the scanner to buffer
	 */
	public ScannerBuffer(Scanner inner){
		this.inner=inner;
	}
	/**
	 * Read-Only access to the buffered Symbols 
	 * @return an unmodifiable Version of the buffer
	 */
	public List<Symbol> getBuffered() {
		return Collections.unmodifiableList(buffer);
	}
	@Override
	public Symbol next_token() throws Exception {
		Symbol buffered = inner.next_token();
		buffer.add(buffered);
		return buffered;
	}

}
