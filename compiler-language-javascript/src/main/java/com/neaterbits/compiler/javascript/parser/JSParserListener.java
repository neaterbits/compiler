package com.neaterbits.compiler.javascript.parser;

import com.neaterbits.compiler.antlr4.ModelParserListener;

import com.neaterbits.compiler.ast.parser.iterative.BaseIterativeOOParserListener;
import com.neaterbits.compiler.javascript.ast.JavascriptProgram;
import com.neaterbits.compiler.util.parse.ParseLogger;

import com.neaterbits.compiler.util.parse.baseparserlistener.ParseTreeFactory;
import com.neaterbits.util.io.strings.StringSource;

public class JSParserListener extends BaseIterativeOOParserListener
			implements ModelParserListener<JavascriptProgram> {

	public JSParserListener(StringSource stringSource, ParseLogger logger, @SuppressWarnings("rawtypes") ParseTreeFactory parseTreeFactory) {
		super(stringSource, logger, parseTreeFactory);
	}

	@Override
	public JavascriptProgram getResult() {
		// TODO Auto-generated method stub
		return null;
	}
}
