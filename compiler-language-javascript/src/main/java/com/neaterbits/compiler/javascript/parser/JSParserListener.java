package com.neaterbits.compiler.javascript.parser;

import com.neaterbits.compiler.antlr4.ModelParserListener;
import com.neaterbits.compiler.ast.parser.iterative.BaseIterativeOOParserListener;
import com.neaterbits.compiler.javascript.ast.JavascriptProgram;
import com.neaterbits.compiler.util.parse.ParseLogger;

public class JSParserListener extends BaseIterativeOOParserListener
			implements ModelParserListener<JavascriptProgram> {

	public JSParserListener(ParseLogger logger) {
		super(logger);
	}

	@Override
	public JavascriptProgram getResult() {
		// TODO Auto-generated method stub
		return null;
	}
}
