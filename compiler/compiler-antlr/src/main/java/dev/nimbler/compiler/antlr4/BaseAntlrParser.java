package dev.nimbler.compiler.antlr4;

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
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.neaterbits.util.io.strings.StringSource;
import com.neaterbits.util.io.strings.StringSourceInputStream;

import dev.nimbler.compiler.util.parse.BaseParser;
import dev.nimbler.compiler.util.parse.ParseError;
import dev.nimbler.compiler.util.parse.ParseLogger;

import org.antlr.v4.runtime.ANTLRErrorListener;

public abstract class BaseAntlrParser<T, LISTENER extends ModelParserListener<T>, LEXER extends Lexer, PARSER extends Parser>
		extends BaseParser<T, LISTENER>
		implements AntlrParser<T, LISTENER> {

	protected abstract LISTENER createListener(StringSource stringSource, ParseLogger parseLogger, String file);

	protected abstract ParserRuleContext getMainContext(PARSER parser);
	
	protected abstract ParseTreeListener makeParseTreeListener(LISTENER listener, boolean debug, String file, ParseLogger parseLogger);

	private final boolean debug;
	
	private final AntlrParserFactory<LEXER, PARSER> parserFactory;
	
	private final AntlrParserInstantiator<LEXER, PARSER> parserInstantiator;

	protected BaseAntlrParser(boolean debug, AntlrParserFactory<LEXER, PARSER> parserFactory) {

		Objects.requireNonNull(parserFactory);

		this.debug = debug;
		this.parserFactory = parserFactory;

		// this.parserInstantiator = new ReinitializeAntlrParserInstantiator<>();
		this.parserInstantiator = new InstantiateAntlrParserInstantiator<>();
	}

	@Override
	protected final T parse(StringSourceInputStream stream, Collection<ParseError> errors, String file, ParseLogger parseLogger) throws IOException {

		final LISTENER listener = createListener(stream, parseLogger, file);

		parse(new ANTLRInputStream(stream), listener, errors, file, parseLogger);

		return listener.getResult();
	}


	@Override
	public final Collection<ParseError> parse(String string, LISTENER listener, ParseLogger parseLogger) {
		
		try {
			return parse(new ANTLRInputStream(string), listener, null, parseLogger);
		}
		catch (IOException ex) {
			throw new IllegalStateException(ex);
		}
	}

	@Override
	public final Collection<ParseError> parse(InputStream stream, LISTENER listener, String file, ParseLogger parseLogger) throws IOException {
		return parse(new ANTLRInputStream(stream), listener, file, parseLogger);
	}

	private final Collection<ParseError> parse(ANTLRInputStream stream, LISTENER listener, String file, ParseLogger parseLogger) throws IOException {
		final List<ParseError> errors = new ArrayList<>();

		parse(stream, listener, errors, file, parseLogger);

		return errors;
	}

	private final void parse(ANTLRInputStream stream, LISTENER listener, Collection<ParseError> errors, String file, ParseLogger parseLogger) throws IOException {

		if (listener == null) {
			throw new IllegalArgumentException("listener == null");
		}

		

		/*
		 * for (Token token = lexer.nextToken(); token.getType() != Token.EOF;
		 * token = lexer.nextToken()) {
		 * System.out.println("### got lexer token: " + token.getText() +
		 * " og type " + token.getType()); }
		 */

		
		 

		final PARSER parser = parserInstantiator.getParserForInput(stream, parserFactory);
		
		parser.addParseListener(new ParseTreeListener() {

			@Override
			public void visitTerminal(TerminalNode node) {
				// System.out.println("## Token " + lexer.getTokenErrorDisplay(node.getSymbol()));
			}

			@Override
			public void visitErrorNode(ErrorNode node) { // TODO

			}

			@Override
			public void enterEveryRule(ParserRuleContext ctx) { // TODO
				// System.out.println("## Enter " + parser.getRuleNames()[ctx.getRuleIndex()] + " " + ctx.getText());

			}

			@Override
			public void exitEveryRule(ParserRuleContext ctx) {
				// System.out.println("## Exit " + parser.getRuleNames()[ctx.getRuleIndex()]);
			}
		});
			 
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

		final ParseTreeListener parseTreeListener = makeParseTreeListener(listener, debug, file, parseLogger);

		new ParseTreeWalker(){
			
		}.walk(parseTreeListener, root);
	}
}

