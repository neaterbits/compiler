package com.neaterbits.compiler.java.parser.antlr4;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;

import com.neaterbits.compiler.antlr4.AntlrParserFactory;
import com.neaterbits.compiler.antlr4.BaseAntlrParser;
import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.java.Java8Lexer;
import com.neaterbits.compiler.java.Java8Parser;
import com.neaterbits.compiler.java.parser.JavaParserListener;
import com.neaterbits.compiler.util.TokenSequenceNoGenerator;
import com.neaterbits.compiler.util.parse.ParseLogger;

public class Java8AntlrParser extends BaseAntlrParser<CompilationUnit, JavaParserListener, Java8Lexer, Java8Parser> {

	public Java8AntlrParser(boolean debug) {
		super(debug, new AntlrParserFactory<>(Java8Lexer::new, Java8Parser::new));
	}

	@Override
	protected JavaParserListener createListener(ParseLogger parseLogger, String file, TokenSequenceNoGenerator gen) {
		return new JavaParserListener(parseLogger, file, gen);
	}

	@Override
	protected ParserRuleContext getMainContext(Java8Parser parser) {
		return parser.compilationUnit();
	}

	@Override
	protected ParseTreeListener makeParseTreeListener(JavaParserListener listener, boolean debug, String file, ParseLogger parseLogger, TokenSequenceNoGenerator gen) {
		return new Java8AntlrParserListener(listener, debug, file, parseLogger, gen);
	}
}
