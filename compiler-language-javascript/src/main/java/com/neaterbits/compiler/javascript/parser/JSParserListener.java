package com.neaterbits.compiler.javascript.parser;

import com.neaterbits.compiler.common.antlr4.ModelParserListener;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.iterative.BaseIterativeOOParserListener;
import com.neaterbits.compiler.javascript.ast.JavascriptProgram;

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
