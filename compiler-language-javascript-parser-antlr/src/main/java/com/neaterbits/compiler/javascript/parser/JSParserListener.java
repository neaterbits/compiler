package com.neaterbits.compiler.javascript.parser;

import com.neaterbits.compiler.antlr4.ModelParserListener;
import com.neaterbits.compiler.ast.objects.parser.iterative.BaseIterativeOOParserListener;
import com.neaterbits.compiler.javascript.ast.JavascriptProgram;
import com.neaterbits.compiler.parser.listener.stackbased.ParseTreeFactory;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.io.strings.StringSource;

@SuppressWarnings("rawtypes")
public class JSParserListener extends BaseIterativeOOParserListener
			implements ModelParserListener<JavascriptProgram> {

	public JSParserListener(StringSource stringSource, ParseLogger logger, ParseTreeFactory parseTreeFactory) {
		super(stringSource, logger, parseTreeFactory);
	}

	@Override
	public JavascriptProgram getResult() {
		// TODO Auto-generated method stub
		return null;
	}
}
