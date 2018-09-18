package com.neaterbits.compiler.common.antlr4;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.TokenSource;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.neaterbits.compiler.common.log.ParseLogger;

import org.antlr.v4.runtime.ANTLRErrorListener;

public abstract class BaseParser<T, LISTENER extends ModelParserListener<T>, LEXER extends Lexer, PARSER extends Parser>
		implements AntlrParser<T, LISTENER> {

	protected abstract LISTENER createListener(ParseLogger parseLogger);

	protected abstract ParserRuleContext getMainContext(PARSER parser);
	
	protected abstract ParseTreeListener makeParseTreeListener(LISTENER listener, ParseLogger parseLogger);
	
	

	private final LEXER lexer;
	private final TokenSource tokenSource;
	private final CommonTokenStream tokens;
	private final PARSER parser;

	private final boolean debug;

	protected BaseParser(boolean debug, LEXER lexer, PARSER parser) {
		if (lexer == null) {
			throw new IllegalArgumentException("lexer == null");
		}

		if (parser == null) {
			throw new IllegalArgumentException("parser == null");
		}

		this.debug = debug;

		this.lexer = lexer;
		this.tokenSource = createTokenSource(lexer);
		this.tokens = new CommonTokenStream(tokenSource);

		this.parser = parser;

		parser.setTokenStream(tokens);
	}

	protected TokenSource createTokenSource(LEXER lexer) {
		return lexer;
	}

	@Override
	public final T parse(String string) {
		return parse(new ANTLRInputStream(string), new ArrayList<AntlrError>(), new ParseLogger(System.out));
	}

	@Override
	public final T parse(InputStream stream) throws IOException {
		return parse(new ANTLRInputStream(stream), new ArrayList<AntlrError>(), new ParseLogger(System.out));
	}

	@Override
	public T parse(String string, Collection<AntlrError> errors, ParseLogger parseLogger) {
		final LISTENER listener = createListener(parseLogger);

		parse(new ANTLRInputStream(string), listener, errors, parseLogger);

		return listener.getResult();
	}

	@Override
	public T parse(InputStream stream, Collection<AntlrError> errors, ParseLogger parseLogger) throws IOException {
		final LISTENER listener = createListener(parseLogger);

		parse(new ANTLRInputStream(stream), listener, errors, parseLogger);

		return listener.getResult();
	}

	private T parse(ANTLRInputStream stream, Collection<AntlrError> errors, ParseLogger parseLogger) {

		final LISTENER listener = createListener(parseLogger);

		parse(stream, listener, errors, parseLogger);

		return listener.getResult();
	}

	@Override
	public final Collection<AntlrError> parse(String string, LISTENER listener, ParseLogger parseLogger) {
		return parse(new ANTLRInputStream(string), listener, parseLogger);
	}

	@Override
	public final Collection<AntlrError> parse(InputStream stream, LISTENER listener, ParseLogger parseLogger) throws IOException {
		return parse(new ANTLRInputStream(stream), listener, parseLogger);
	}

	private final Collection<AntlrError> parse(ANTLRInputStream stream, LISTENER listener, ParseLogger parseLogger) {
		final List<AntlrError> errors = new ArrayList<AntlrError>();

		parse(stream, listener, errors, parseLogger);

		return errors;
	}

	private final void parse(ANTLRInputStream stream, LISTENER listener, final Collection<AntlrError> errors, ParseLogger parseLogger) {

		if (listener == null) {
			throw new IllegalArgumentException("listener == null");
		}

		lexer.setInputStream(stream);

		/*
		 * for (Token token = lexer.nextToken(); token.getType() != Token.EOF;
		 * token = lexer.nextToken()) {
		 * System.out.println("### got lexer token: " + token.getText() +
		 * " og type " + token.getType()); }
		 */

		/*
		 * parser.addParseListener(new ParseTreeListener() {
		 * 
		 * @Override public void visitTerminal(TerminalNode node) {
		 * System.out.println("## Token " +
		 * lexer.getTokenErrorDisplay(node.getSymbol())); }
		 * 
		 * @Override public void visitErrorNode(ErrorNode node) { // TODO
		 * Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void exitEveryRule(ParserRuleContext ctx) {
		 * System.out.println("## Enter " +
		 * parser.getRuleNames()[ctx.getRuleIndex()]);
		 * 
		 * }
		 * 
		 * @Override public void enterEveryRule(ParserRuleContext ctx) { // TODO
		 * Auto-generated method stub
		 * 
		 * } });
		 */

		tokens.setTokenSource(tokenSource);

		parser.setTokenStream(tokens);

		parser.addErrorListener(new ANTLRErrorListener() {

			@Override
			public void syntaxError(Recognizer<?, ?> arg0, Object arg1, int arg2, int arg3, String arg4,
					RecognitionException arg5) {

				errors.add(new AntlrError(arg2, arg3, arg4, arg5));
			}

			@Override
			public void reportContextSensitivity(Parser arg0, DFA arg1, int arg2, int arg3, int arg4,
					ATNConfigSet arg5) {
				// TODO Auto-generated method stub

			}

			@Override
			public void reportAttemptingFullContext(Parser arg0, DFA arg1, int arg2, int arg3, BitSet arg4,
					ATNConfigSet arg5) {

			}

			@Override
			public void reportAmbiguity(Parser arg0, DFA arg1, int arg2, int arg3, boolean arg4, BitSet arg5,
					ATNConfigSet arg6) {
				// TODO Auto-generated method stub

			}
		});
		
		parser.setBuildParseTree(true);

		final ParserRuleContext root = getMainContext(parser);

		final int numErrors = parser.getNumberOfSyntaxErrors();

		final ParseTreeListener parseTreeListener = makeParseTreeListener(listener, parseLogger);

		new ParseTreeWalker(){
			
			
		}.walk(parseTreeListener, root);
	}
}

