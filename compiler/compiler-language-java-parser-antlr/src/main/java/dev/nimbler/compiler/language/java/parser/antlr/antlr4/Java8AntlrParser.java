package dev.nimbler.compiler.language.java.parser.antlr.antlr4;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;

import dev.nimbler.compiler.java.Java8Lexer;
import dev.nimbler.compiler.java.Java8Parser;

import dev.nimbler.compiler.antlr4.AntlrParserFactory;
import dev.nimbler.compiler.antlr4.BaseAntlrParser;
import dev.nimbler.compiler.language.java.parser.antlr.JavaParserListener;
import dev.nimbler.compiler.util.parse.ParseLogger;

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
