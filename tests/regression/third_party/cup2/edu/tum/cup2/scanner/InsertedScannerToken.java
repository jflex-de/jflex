package edu.tum.cup2.scanner;

import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.semantics.ErrorInformation;

public class InsertedScannerToken<T>
	extends ScannerToken<Object>
{	
	private final ErrorInformation	error;

	public InsertedScannerToken(Terminal terminal, Object value, int line, int column, ErrorInformation error) {
		super(terminal, value, line, column);
		this.error = error;
		if (value==NoValue)
			this.hasValue = false;
	}

	public ErrorInformation getErrorInformation() {
		return error;
	}

}
