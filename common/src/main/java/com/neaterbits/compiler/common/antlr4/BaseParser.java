package com.neaterbits.compiler.common.antlr4;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
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
	
	protected abstract ParseTreeListener makeParseTreeListener(LISTENER listener, boolean debug, ParseLogger parseLogger);

	private final boolean debug;
	
	private final AntlrParserFactory<LEXER, PARSER> parserFactory;
	
	private final AntlrParserInstantiator<LEXER, PARSER> parserInstantiator;

	protected BaseParser(boolean debug, AntlrParserFactory<LEXER, PARSER> parserFactory) {

		Objects.requireNonNull(parserFactory);

		this.debug = debug;
		this.parserFactory = parserFactory;

		// this.parserInstantiator = new ReinitializeAntlrParserInstantiator<>();
		this.parserInstantiator = new InstantiateAntlrParserInstantiator<>();
	}

	@Override
	public final T parse(String string) {
		try {
			return parse(new ANTLRInputStream(string), new ArrayList<AntlrError>(), new ParseLogger(System.out));
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

	@Override
	public final T parse(InputStream stream) throws IOException {
		return parse(new ANTLRInputStream(stream), new ArrayList<AntlrError>(), new ParseLogger(System.out));
	}

	@Override
	public T parse(String string, Collection<AntlrError> errors, ParseLogger parseLogger) {
		final LISTENER listener = createListener(parseLogger);

		try {
			parse(new ANTLRInputStream(string), listener, errors, parseLogger);
		} catch (IOException ex) {
			throw new IllegalStateException(ex);
		}

		return listener.getResult();
	}

	@Override
	public T parse(InputStream stream, Collection<AntlrError> errors, ParseLogger parseLogger) throws IOException {
		final LISTENER listener = createListener(parseLogger);

		parse(new ANTLRInputStream(stream), listener, errors, parseLogger);

		return listener.getResult();
	}

	private T parse(ANTLRInputStream stream, Collection<AntlrError> errors, ParseLogger parseLogger) throws IOException {

		final LISTENER listener = createListener(parseLogger);

		parse(stream, listener, errors, parseLogger);

		return listener.getResult();
	}

	@Override
	public final Collection<AntlrError> parse(String string, LISTENER listener, ParseLogger parseLogger) {
		
		try {
			return parse(new ANTLRInputStream(string), listener, parseLogger);
		}
		catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

	@Override
	public final Collection<AntlrError> parse(InputStream stream, LISTENER listener, ParseLogger parseLogger) throws IOException {
		return parse(new ANTLRInputStream(stream), listener, parseLogger);
	}

	private final Collection<AntlrError> parse(ANTLRInputStream stream, LISTENER listener, ParseLogger parseLogger) throws IOException {
		final List<AntlrError> errors = new ArrayList<AntlrError>();

		parse(stream, listener, errors, parseLogger);

		return errors;
	}

	private final void parse(ANTLRInputStream stream, LISTENER listener, final Collection<AntlrError> errors, ParseLogger parseLogger) throws IOException {

		if (listener == null) {
			throw new IllegalArgumentException("listener == null");
		}


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

		final PARSER parser = parserInstantiator.getParserForInput(stream, parserFactory);
		
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

		final ParseTreeListener parseTreeListener = makeParseTreeListener(listener, debug, parseLogger);

		new ParseTreeWalker(){
			
		}.walk(parseTreeListener, root);
	}
}

