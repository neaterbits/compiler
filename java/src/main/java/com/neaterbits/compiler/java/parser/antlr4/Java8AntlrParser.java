package com.neaterbits.compiler.java.parser.antlr4;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;

import com.neaterbits.compiler.common.antlr4.BaseParser;
import com.neaterbits.compiler.java.Java8Lexer;
import com.neaterbits.compiler.java.Java8Parser;
import com.neaterbits.compiler.java.ast.JavaProgram;
import com.neaterbits.compiler.java.parser.JavaParserListener;

public class Java8AntlrParser extends BaseParser<JavaProgram, JavaParserListener, Java8Lexer, Java8Parser> {

	public Java8AntlrParser(boolean debug, Java8Lexer lexer, Java8Parser parser) {
		super(debug, lexer, parser);
	}

	@Override
	protected JavaParserListener createListener() {
		return new JavaParserListener();
	}

	@Override
	protected ParserRuleContext getMainContext(Java8Parser parser) {
		return parser.compilationUnit();
	}

	@Override
	protected ParseTreeListener makeParseTreeListener(JavaParserListener listener) {
		return new Java8AntlrParserListener(listener);
	}
}
