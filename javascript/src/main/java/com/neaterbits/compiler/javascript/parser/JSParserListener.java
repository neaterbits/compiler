package com.neaterbits.compiler.javascript.parser;

import com.neaterbits.compiler.common.antlr4.ModelParserListener;
import com.neaterbits.compiler.common.parser.iterative.oo.BaseIterativeOOParserListener;
import com.neaterbits.compiler.javascript.ast.JavascriptProgram;

public class JSParserListener extends BaseIterativeOOParserListener
			implements ModelParserListener<JavascriptProgram> {

	@Override
	public JavascriptProgram getResult() {
		// TODO Auto-generated method stub
		return null;
	}
}
