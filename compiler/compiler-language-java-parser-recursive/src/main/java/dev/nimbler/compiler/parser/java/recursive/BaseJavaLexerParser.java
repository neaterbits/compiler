package dev.nimbler.compiler.parser.java.recursive;

import java.io.IOException;
import java.util.Objects;

import org.jutils.io.strings.CharInput;
import org.jutils.io.strings.Tokenizer;
import org.jutils.parse.Lexer;
import org.jutils.parse.ParserException;

import dev.nimbler.compiler.parser.listener.common.IterativeParseTreeListener;
import dev.nimbler.compiler.parser.recursive.BaseLexerParser;
import dev.nimbler.compiler.parser.recursive.cached.ProcessParts;
import dev.nimbler.compiler.parser.recursive.cached.expressions.ExpressionCache;
import dev.nimbler.compiler.parser.recursive.cached.names.NamesList;
import dev.nimbler.compiler.util.name.Names;

abstract class BaseJavaLexerParser<COMPILATION_UNIT> extends BaseLexerParser<JavaToken> {

    final IterativeParseTreeListener<COMPILATION_UNIT> listener;
    final JavaListenerHelper<COMPILATION_UNIT> listenerHelper;

    BaseJavaLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        super(
                file,
                lexer,
                tokenizer,
                listener::writeContext,
                JavaLanguageOperatorPrecedence.INSTANCE);

        Objects.requireNonNull(listener);
        
        this.listener = listener;
        this.listenerHelper = new JavaListenerHelper<>(listener, this::writeContext);
    }
    
    final int writeCurContext() {
        
        return listener.writeContext(getLexerContext());
    }

    final int writeContext(int otherContext) {
        
        return listener.writeContext(otherContext);
    }

    final long getStringRef() {
        return lexer.getStringRef(0, 0);
    }

    final void parseNameListUntilOtherToken(
            int identifierContext,
            long identifier,
            ProcessParts<Names> processNameParts) throws IOException, ParserException {
        
        final NamesList scratch = startScratchNameParts();

        scratch.add(identifierContext, identifier);
        
        parseNames(scratch, processNameParts);
    }

    final void parseNameListUntilOtherToken(ProcessParts<Names> processNameParts) throws IOException, ParserException {
        
        final NamesList scratch = startScratchNameParts();

        parseNames(scratch, processNameParts);
    }

    private void parseNames(NamesList scratch, ProcessParts<Names> processNameParts) throws IOException, ParserException {
        
        parseNames(scratch);
        
        // reach non-namepart so process now
        scratch.complete(processNameParts);
    }
    
    final void parseNames(NamesList scratch) throws IOException, ParserException {
        
        for (;;) {

            final JavaToken partToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
            
            if (partToken != JavaToken.IDENTIFIER) {
                throw lexer.unexpectedToken();
            }
            
            scratch.add(writeCurContext(), getStringRef());
            
            final JavaToken endOfScopeToken = lexer.lexSkipWS(JavaToken.PERIOD);
            
            if (endOfScopeToken != JavaToken.PERIOD) {
                break;
            }
        }
    }
    
    final void applyAndClearExpressionCache(ExpressionCache expressionCache) {
        
        applyAndClearExpressionCache(expressionCache, listener);
    }

    static final <COMPILATION_UNIT> void applyAndClearExpressionCache(
            ExpressionCache expressionCache,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {
        
        expressionCache.apply(listener);

        expressionCache.clear();
    }
    @FunctionalInterface
    interface OnScopedNamePart {
        
        void onPart(int context, long identifier);
    }
    
    final void parseScopedName(OnScopedNamePart processPart) throws IOException, ParserException {

        for (;;) {
            final JavaToken identifierToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
            
            processPart.onPart(writeCurContext(), lexer.getStringRef());
            
            if (identifierToken == JavaToken.NONE) {
                throw lexer.unexpectedToken();
            }
            
            final JavaToken periodToken = lexer.lexSkipWS(JavaToken.PERIOD);
            if (periodToken == JavaToken.NONE) {
                break;
            }
        }
    }
}
