package dev.nimbler.compiler.parser.java.recursive;

import java.io.IOException;

import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

import dev.nimbler.compiler.parser.listener.common.IterativeParseTreeListener;
import dev.nimbler.compiler.types.ReferenceType;

abstract class JavaParametersLexerParser<COMPILATION_UNIT>
    extends JavaVariablesLexerParser<COMPILATION_UNIT> {

    JavaParametersLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {
        
        super(file, lexer, tokenizer, listener);
    }

    private static final JavaToken [] PARAMETER_TOKENS = new JavaToken [] {
            JavaToken.IDENTIFIER,
            JavaToken.COMMA
    };

    private static final JavaToken [] PARAMETER_NAME_OR_VARARGS_TOKENS = new JavaToken [] {
            JavaToken.IDENTIFIER,
            JavaToken.VARARGS,
    };

    final void parseMethodSignatureParameters() throws IOException, ParserException {
        
        boolean done = false;
        
        do {
            final int curParameterStartContext = writeCurContext();

            listener.onMethodSignatureParameterStart(curParameterStartContext, false);

            parseAnyParameterModifiersOrAnnotations();

            parseParameterType();

            final JavaToken parameterNameOrVarargsToken = lexer.lexSkipWS(PARAMETER_NAME_OR_VARARGS_TOKENS);
            
            switch (parameterNameOrVarargsToken) {
            case VARARGS:
                listener.onMethodSignatureParameterVarargs(writeCurContext());
                
                parseParameterVariableName();
                break;
                
            case IDENTIFIER:
                listener.onVariableName(curParameterStartContext, getStringRef(), 0);
                break;
                
            default:
                throw lexer.unexpectedToken();
            }

            final JavaToken afterParameterName = lexer.lexSkipWS(PARAMETER_TOKENS);
            
            switch (afterParameterName) {
            case COMMA:
                listener.onMethodSignatureParameterEnd(curParameterStartContext, getLexerContext());
                // Next parameter
                break;

            default:
                listener.onMethodSignatureParameterEnd(curParameterStartContext, getLexerContext());
                done = true;
                break;
            }

        } while(!done);
    }

    private void parseParameterVariableName() throws IOException, ParserException {

        final JavaToken parameterNameToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
        
        if (parameterNameToken != JavaToken.IDENTIFIER) {
            throw lexer.unexpectedToken();
        }

        listener.onVariableName(writeCurContext(), getStringRef(), 0);
    }
    
    private static final JavaToken [] PARAMETER_MODIFIER_TOKENS = new JavaToken [] {
            
            JavaToken.AT
    };
    
    private void parseAnyParameterModifiersOrAnnotations() throws IOException, ParserException {
        
        boolean done = false;
        
        do {
            final JavaToken parameterModifierToken = lexer.lexSkipWS(PARAMETER_MODIFIER_TOKENS);

            switch (parameterModifierToken) {
            
            case AT:
                parseAnnotation(writeCurContext());
                break;
            
            case NONE:
                done = true;
                break;

            default:
                throw lexer.unexpectedToken();
            }
        } while (!done);
    }

    private static final JavaToken [] PARAM_TYPE = {
            
            JavaToken.BYTE,
            JavaToken.SHORT,
            JavaToken.INT,
            JavaToken.LONG,
            JavaToken.FLOAT,
            JavaToken.DOUBLE,
            JavaToken.CHAR,
            
            JavaToken.IDENTIFIER
    };

    private void parseParameterType() throws IOException, ParserException {

        final JavaToken typeToken = lexer.lexSkipWS(PARAM_TYPE);
        
        switch (typeToken) {
            
        case BYTE:
        case SHORT:
        case INT:
        case LONG:
        case FLOAT:
        case DOUBLE:
        case CHAR:
            listener.onLeafTypeReference(writeCurContext(), getStringRef(), ReferenceType.SCALAR);
            break;
            
        case IDENTIFIER:
            final int initialIdentifierContext = writeCurContext();

            parseUserType(initialIdentifierContext);
            break;
            
        default:
            throw lexer.unexpectedToken();
        }
    }
    
    private static final JavaToken [] VARARGS_OR_PERIOD_TOKENS = new JavaToken [] {
            
            JavaToken.VARARGS,
            JavaToken.PERIOD
    };

    private void parseUserType(int initialPartContext) throws ParserException, IOException {
        
        final long stringRef = getStringRef();

        final JavaToken scopeToken = lexer.lexSkipWS(VARARGS_OR_PERIOD_TOKENS);
        
        boolean varargs = false;
        
        switch (scopeToken) {
        case VARARGS:
            varargs = true;
            
        case NONE:
            final int startContext = writeContext(initialPartContext);

            listener.onNonScopedTypeReferenceStart(startContext, stringRef, ReferenceType.REFERENCE);

            tryParseGenericTypeArguments();

            listener.onNonScopedTypeReferenceEnd(startContext, getLexerContext());
            break;
            
        case PERIOD:
            parseUserType(initialPartContext, stringRef, true);
            break;
            
        default:
            throw lexer.unexpectedToken();
        }

        if (varargs) {
            listener.onMethodSignatureParameterVarargs(writeCurContext());
        }
    }

    private void parseUserType(int initialPartContext, long stringRef, boolean gotPeriodToken) throws ParserException, IOException {
        
        if (gotPeriodToken) {
            parseUserTypeAfterPeriod(initialPartContext, stringRef);
        }
        else {
            final int startContext = writeContext(initialPartContext);

            listener.onNonScopedTypeReferenceStart(startContext, stringRef, ReferenceType.REFERENCE);
            
            tryParseGenericTypeArguments();
            
            listener.onNonScopedTypeReferenceEnd(startContext, getLexerContext());
        }
    }

    private void parseUserTypeAfterPeriod(int initialPartContext, long stringRef) throws IOException, ParserException {
        
        final int typeStartContext = writeContext(initialPartContext);

        listener.onScopedTypeReferenceStart(typeStartContext, ReferenceType.REFERENCE);
        
        final int namesStartContext = writeContext(typeStartContext);
        
        listener.onScopedTypeReferenceNameStart(namesStartContext);
        
        listener.onScopedTypeReferenceNamePart(initialPartContext, stringRef);
        
        boolean varargs = false;

        for (;;) {

            if (lexer.lexSkipWS(JavaToken.IDENTIFIER) != JavaToken.IDENTIFIER) {
                throw lexer.unexpectedToken();
            }
            
            listener.onScopedTypeReferenceNamePart(writeCurContext(), getStringRef());

            boolean done = false;

            final JavaToken partToken = lexer.lexSkipWS(VARARGS_OR_PERIOD_TOKENS);
            
            switch (partToken) {
            case VARARGS:
                varargs = true;
                break;

            case PERIOD:
                break;

            default:
                done = true;
                break;
            }
            
            if (done || varargs) {
                break;
            }
        }

        tryParseGenericTypeArguments();

        listener.onScopedTypeReferenceNameEnd(namesStartContext, getLexerContext());
        
        listener.onScopedTypeReferenceEnd(typeStartContext, getLexerContext());
        
        if (varargs) {
            listener.onMethodSignatureParameterVarargs(writeCurContext());
        }
    }

}
