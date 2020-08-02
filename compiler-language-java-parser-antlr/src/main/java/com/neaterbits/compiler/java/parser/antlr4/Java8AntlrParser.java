package com.neaterbits.compiler.java.parser.antlr4;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;

import com.neaterbits.compiler.antlr4.AntlrParserFactory;
import com.neaterbits.compiler.antlr4.BaseAntlrParser;
import com.neaterbits.compiler.java.Java8Lexer;
import com.neaterbits.compiler.java.Java8Parser;
import com.neaterbits.compiler.java.parser.JavaParserListener;
import com.neaterbits.compiler.util.parse.ParseLogger;

public abstract class Java8AntlrParser<COMPILATION_UNIT>
        extends BaseAntlrParser<
                    COMPILATION_UNIT,
                    JavaParserListener<COMPILATION_UNIT>,
                    Java8Lexer,
                    Java8Parser> {

	public Java8AntlrParser(boolean debug) {
		super(debug, new AntlrParserFactory<>(Java8Lexer::new, Java8Parser::new));
	}

	@Override
	protected final ParserRuleContext getMainContext(Java8Parser parser) {
		return parser.compilationUnit();
	}

	@Override
	protected final ParseTreeListener makeParseTreeListener(JavaParserListener<COMPILATION_UNIT> listener, boolean debug, String file, ParseLogger parseLogger) {
		return new Java8AntlrParserListener<>(listener, debug, file, parseLogger);
	}
}
