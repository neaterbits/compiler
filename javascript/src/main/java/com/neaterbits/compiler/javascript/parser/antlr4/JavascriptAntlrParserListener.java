package com.neaterbits.compiler.javascript.parser.antlr4;

import com.neaterbits.compiler.javascript.JavaScriptParserBaseListener;
import com.neaterbits.compiler.javascript.parser.JSParserListener;

public class JavascriptAntlrParserListener extends JavaScriptParserBaseListener {

	private final JSParserListener delegate;

	public JavascriptAntlrParserListener(JSParserListener delegate) {
		this.delegate = delegate;
	}
}
