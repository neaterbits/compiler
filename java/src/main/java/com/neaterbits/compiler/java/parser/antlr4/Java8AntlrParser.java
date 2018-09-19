package com.neaterbits.compiler.java.parser.antlr4;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;

import com.neaterbits.compiler.common.antlr4.AntlrParserFactory;
import com.neaterbits.compiler.common.antlr4.BaseParser;
import com.neaterbits.compiler.common.ast.CompilationUnit;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.java.Java8Lexer;
import com.neaterbits.compiler.java.Java8Parser;
import com.neaterbits.compiler.java.parser.JavaParserListener;

public class Java8AntlrParser extends BaseParser<CompilationUnit, JavaParserListener, Java8Lexer, Java8Parser> {

	public Java8AntlrParser(boolean debug) {
		super(debug, new AntlrParserFactory<>(Java8Lexer::new, Java8Parser::new));
	}

	@Override
	protected JavaParserListener createListener(ParseLogger parseLogger) {
		return new JavaParserListener(parseLogger);
	}

	@Override
	protected ParserRuleContext getMainContext(Java8Parser parser) {
		return parser.compilationUnit();
	}

	@Override
	protected ParseTreeListener makeParseTreeListener(JavaParserListener listener, ParseLogger parseLogger) {
		return new Java8AntlrParserListener(listener, parseLogger);
	}
}
