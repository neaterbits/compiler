package dev.nimbler.compiler.parser.java.recursive;

import java.io.IOException;

import org.jutils.ArrayUtils;
import org.jutils.io.strings.CharInput;
import org.jutils.io.strings.Tokenizer;
import org.jutils.parse.Lexer;
import org.jutils.parse.ParserException;
import org.jutils.parse.context.Context;

import dev.nimbler.compiler.parser.listener.common.IterativeParseTreeListener;
import dev.nimbler.compiler.parser.recursive.cached.annotations.CachedAnnotationsList;
import dev.nimbler.compiler.parser.recursive.cached.keywords.CachedKeywordsList;
import dev.nimbler.compiler.parser.recursive.cached.types.TypeArgumentsList;
import dev.nimbler.compiler.types.ReferenceType;

abstract class JavaMemberLexerParser<COMPILATION_UNIT> extends JavaStatementsLexerParser<COMPILATION_UNIT> {

    private final TypeScratchInfo typeScratchInfo;
    
    // For returning multiple values without allocation
    private CachedKeywordsList<JavaToken> cachedModifiers;
    private CachedAnnotationsList cachedAnnotations;

    JavaMemberLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {
        
        super(file, lexer, tokenizer, listener);

        this.typeScratchInfo = new TypeScratchInfo();
    }

    private static JavaToken [] MEMBER_START_TOKENS = ArrayUtils.merge(
            
            
            SCALAR_TYPE_TOKENS,
            
            new JavaToken [] {
            
            JavaToken.PRIVATE,
            JavaToken.PUBLIC,
            
            JavaToken.LT, // generic method

            JavaToken.IDENTIFIER // type
    });
    
    final boolean parseMember(long implementingClassName) throws IOException, ParserException {

        parseAnyMemberModifiersOrAnnotations();
        
        final CachedKeywordsList<JavaToken> modifiers = cachedModifiers;
        this.cachedModifiers = null;
        
        final CachedAnnotationsList annotations = cachedAnnotations;
        this.cachedAnnotations = null;
        
        final JavaToken initialToken = lexer.lexSkipWS(MEMBER_START_TOKENS);
        
        boolean memberFound = true;
        
        switch (initialToken) {
        
        case VOID:
        case BYTE:
        case SHORT:
        case INT:
        case LONG:
        case FLOAT:
        case DOUBLE:
        case CHAR: {
            final Context afterTypeContext = initScratchContext();
            
            try {
                parseRestOfTypeAndFieldScalar(modifiers, annotations, writeCurContext(), getStringRef(), afterTypeContext);
            }
            finally {
                freeScratchContext(afterTypeContext);
            }
            break;
        }
        
        case IDENTIFIER:
            // Probably a type, is it a scoped type?
            final long typeName = lexer.getStringRef();
            
            parseRestOfTypeAndFieldScopedType(modifiers, annotations, implementingClassName, writeCurContext(), typeName);
            break;
            
        case LT: {

            final int startContext = writeCurContext();
            
            listener.onClassMethodStart(startContext);
            
            applyModifiersAndAnnotations(modifiers, annotations);

            parseGenericTypeParameters();
            
            parseMethod();
            
            listener.onClassMethodEnd(startContext, getLexerContext());
            break;
        }
        
        case NONE:
            // Not a member variable or method
            memberFound = false;
            break;
            
        default:
            throw lexer.unexpectedToken();
        }
        
        return memberFound;
    }
    
    private static final JavaToken [] MEMBER_MODIFIER_TOKENS = new JavaToken [] {
      
            JavaToken.AT,
            
            JavaToken.PUBLIC,
            JavaToken.PRIVATE,
            JavaToken.FINAL,
            
            JavaToken.STATIC,
            JavaToken.VOLATILE,
            JavaToken.TRANSIENT
    };
    
    private void parseAnyMemberModifiersOrAnnotations() throws IOException, ParserException {
        
        boolean done = false;
        
        this.cachedModifiers = startScratchKeywords();
        this.cachedAnnotations = startScratchAnnotations();
        
        do {
            final JavaToken memberModifierToken = lexer.lexSkipWS(MEMBER_MODIFIER_TOKENS);

            switch (memberModifierToken) {
            
            case AT:
                parseAnnotation(cachedAnnotations, writeCurContext());
                break;
            
            case PUBLIC:
            case PRIVATE:
            case FINAL:
            case STATIC:
            case VOLATILE:
            case TRANSIENT:
                cachedModifiers.addScratchKeyword(memberModifierToken, writeCurContext(), getStringRef());
                break;

            case NONE:
                done = true;
                break;

            default:
                throw lexer.unexpectedToken();
            }
        } while (!done);
    }
    
    private static final JavaToken [] AFTER_FIELD_NAME = new JavaToken [] {
            
            JavaToken.SEMI,
            JavaToken.COMMA,
            JavaToken.ASSIGN,
            JavaToken.LPAREN
    };

    private static final JavaToken [] AFTER_INITIALIZER = new JavaToken [] {
            JavaToken.SEMI,
            JavaToken.COMMA
    };

    private void parseRestOfTypeAndFieldScalar(
            CachedKeywordsList<JavaToken> modifiers,
            CachedAnnotationsList annotations,
            int typeNameContext,
            long typeName,
            Context afterTypeContext) throws IOException, ParserException {
        
        typeScratchInfo.initNonScoped(typeName, typeNameContext, afterTypeContext, ReferenceType.SCALAR);
        
        parseRestOfMemberAfterInitialTypeDeclaration(modifiers, annotations, typeScratchInfo, null);
    }

    private void parseRestOfTypeAndFieldScopedType(
            CachedKeywordsList<JavaToken> modifiers,
            CachedAnnotationsList annotations,
            long implementingClassName,
            int typeNameContext,
            long typeName) throws IOException, ParserException {
        
        final JavaToken periodToken = lexer.lexSkipWS(JavaToken.PERIOD);
        
        if (periodToken == JavaToken.PERIOD) {
            
            parseNameListUntilOtherToken(typeNameContext, typeName, names -> {
                
                final TypeArgumentsList typeArguments = tryParseGenericTypeArgumentsToScratchList();

                typeScratchInfo.initScoped(names, getLexerContext(), ReferenceType.REFERENCE);

                parseRestOfMemberAfterInitialTypeDeclaration(modifiers, annotations, typeScratchInfo, typeArguments);
            });
        }
        else {
            final TypeArgumentsList typeArguments = tryParseGenericTypeArgumentsToScratchList();

            // Is this a constructor method?
            if (lexer.lexSkipWS(JavaToken.LPAREN) == JavaToken.LPAREN) {
                
                // This is a constructor so no type declaration, or one forgot to add
                // the type declaration
                // Compare the names to the class name
                if (tokenizer.equals(implementingClassName, typeName)) {
                    // typeName is same as implementing class name so this is really a constructor
                    final int startContext = writeContext(typeNameContext);
                    
                    listener.onConstructorStart(startContext);
                    
                    listener.onConstructorName(typeNameContext, typeName);

                    parseParametersAndMethod(writeCurContext());
                    
                    listener.onConstructorEnd(startContext, getLexerContext());
                }
                else {
                    // Not the same so try to just parse as a method but without return type since missing
                    typeScratchInfo.initNoType();
                    
                    parseMethod(modifiers, annotations, typeScratchInfo, typeArguments, getLexerContext(), typeNameContext, typeName);
                }
            }
            else {
                typeScratchInfo.initNonScoped(typeName, typeNameContext, getLexerContext(), ReferenceType.REFERENCE);
                
                parseRestOfMemberAfterInitialTypeDeclaration(modifiers, annotations, typeScratchInfo, typeArguments);
            }
        }
    }
    
    private void parseRestOfMemberAfterInitialTypeDeclaration(
            CachedKeywordsList<JavaToken> modifiers,
            CachedAnnotationsList annotations,
            TypeScratchInfo typeName,
            TypeArgumentsList typeArguments) throws ParserException, IOException {
        
        final Context typeEndContext = initScratchContext();
        
        try {
            
            int numDims = parseArrayIndicators(0);

            // Next should be the name of the field or member
            final JavaToken fieldNameToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
        
            
            if (fieldNameToken != JavaToken.IDENTIFIER) {
                throw lexer.unexpectedToken();
            }
            
            final int identifierContext = writeCurContext();
            final long identifier = getStringRef();
            
            final Context variableDeclaratorEndContext = initScratchContext();
            
            final int fieldDeclarationStartContext = writeContext(typeName.getStartContext());
            
            try {
            
                // Additional array indicators
                numDims = parseArrayIndicators(numDims);
                
                // Next should be start of method, semicolon after type, array indicator or comma separated variables
                final JavaToken afterFieldToken = lexer.lexSkipWS(AFTER_FIELD_NAME);
                
                switch (afterFieldToken) {
                case SEMI: {
                    // This is a field with a single variable name, like 'int a;'
                    listener.onFieldDeclarationStart(fieldDeclarationStartContext);
                    
                    listenerHelper.onMemberVariableDeclaration(
                            modifiers,
                            annotations,
                            typeName,
                            typeEndContext,
                            typeArguments,
                            identifier, identifierContext,
                            numDims,
                            variableDeclaratorEndContext);
                    
                    listener.onFieldDeclarationEnd(fieldDeclarationStartContext, getLexerContext());
                    break;
                }
                    
                case COMMA: {
                    // This is a field with multiple variable names, like 'int a, b, c;'
                    listener.onFieldDeclarationStart(fieldDeclarationStartContext);
                    
                    listenerHelper.onMemberVariableDeclaration(
                            modifiers,
                            annotations,
                            typeName,
                            typeEndContext,
                            typeArguments,
                            identifier, identifierContext,
                            numDims,
                            variableDeclaratorEndContext);
        
                    parseVariableDeclaratorList();
                    
                    listener.onFieldDeclarationEnd(fieldDeclarationStartContext, getLexerContext());
                    break;
                }
                
                case ASSIGN: {
                    
                    listener.onFieldDeclarationStart(fieldDeclarationStartContext);
                    
                    listenerHelper.onModifiersAndType(
                            modifiers,
                            annotations,
                            listenerHelper::callFieldMemberModifiers,
                            typeName,
                            typeEndContext,
                            typeArguments);
                    
                    final int variableDeclaratorStartContext = writeContext(identifierContext);

                    listenerHelper.onVariableDeclarator(
                            variableDeclaratorStartContext,
                            identifier, identifierContext,
                            this::parseExpression,
                            this::getLexerContext);
                    
                    switch (lexer.lexSkipWS(AFTER_INITIALIZER)) {
                    case SEMI:
                        break;
                        
                    case COMMA:
                        parseVariableDeclaratorList();
                        break;
                        
                    default:
                        throw lexer.unexpectedToken();
                    }
                    
                    listener.onFieldDeclarationEnd(fieldDeclarationStartContext, getLexerContext());
                    break;
                }
                 
                case LPAREN: {
                    parseMethod(modifiers, annotations, typeName, typeArguments, typeEndContext, identifierContext, identifier);
                    break;
                }
                    
                default:
                    throw lexer.unexpectedToken();
                }
            }
            finally {
                freeScratchContext(variableDeclaratorEndContext);
            }
        }
        finally {
            freeScratchContext(typeEndContext);
        }
    }

    private void parseMethod() throws IOException, ParserException {
        
        final int returnTypeStartContext = writeCurContext();
        
        listener.onMethodReturnTypeStart(returnTypeStartContext);
        
        parseScalarOrTypeReference();
        
        listener.onMethodReturnTypeEnd(returnTypeStartContext, getLexerContext());
        
        if (lexer.lexSkipWS(JavaToken.IDENTIFIER) != JavaToken.IDENTIFIER) {
            throw lexer.unexpectedToken();
        }

        listener.onMethodName(writeCurContext(), getStringRef());

        if (lexer.lexSkipWS(JavaToken.LPAREN) != JavaToken.LPAREN) {
            throw lexer.unexpectedToken();
        }

        parseParametersAndMethod(writeCurContext());
    }

    private void parseMethod(
            CachedKeywordsList<JavaToken> modifiers,
            CachedAnnotationsList annotations,
            TypeScratchInfo typeInfo,
            TypeArgumentsList typeArgumentsList,
            Context typeEndContext,
            int identifierContext,
            long identifier) throws IOException, ParserException {

        final int typeStartContext = typeInfo.getStartContext();
        
        final int classMethodStartContext = writeContext(typeStartContext);
        final int methodReturnTypeStartContext = writeContext(typeStartContext);
        final int methodParametersStartContext = writeContext(typeStartContext);
        
        listener.onClassMethodStart(classMethodStartContext);

        applyModifiersAndAnnotations(modifiers, annotations);

        listener.onMethodReturnTypeStart(methodReturnTypeStartContext);
        
        listenerHelper.onTypeAndOptionalArgumentsList(typeInfo, typeArgumentsList, typeEndContext);
        
        listener.onMethodReturnTypeEnd(methodReturnTypeStartContext, getLexerContext());
        
        listener.onMethodName(identifierContext, identifier);

        parseParametersAndMethod(methodParametersStartContext);

        listener.onClassMethodEnd(classMethodStartContext, getLexerContext());

    }
    
    private void applyModifiersAndAnnotations(CachedKeywordsList<JavaToken> modifiers, CachedAnnotationsList annotations) throws IOException, ParserException {

        if (modifiers != null) {
            modifiers.complete(keywords -> {
                listenerHelper.callClassMethodMemberModifiers(keywords);
            });
        }
        
        if (annotations != null) {
            apply(annotations, listener);
        }

    }
    
    private static final JavaToken [] PARAM_TYPE_RPAREN = {
            JavaToken.RPAREN
    };

    private void parseParametersAndMethod(int methodParametersStartContext) throws IOException, ParserException {

        listener.onMethodSignatureParametersStart(methodParametersStartContext);
        
        final JavaToken initialToken = lexer.lexSkipWS(PARAM_TYPE_RPAREN);
        
        switch (initialToken) {
        
        case RPAREN:
            listener.onMethodSignatureParametersEnd(methodParametersStartContext, getLexerContext());
            
            parseAnyThrownExceptions();
            
            parseMethodBodyOrSemicolon();
            break;

        default:
            parseMethodSignatureParameters();
            
            final JavaToken token = lexer.lexSkipWS(PARAM_TYPE_RPAREN);
            if (token != JavaToken.RPAREN) {
                throw lexer.unexpectedToken();
            }
            
            parseAnyThrownExceptions();
            
            parseMethodBodyOrSemicolon();
            break;
        }
    }

    private void parseAnyThrownExceptions() throws IOException, ParserException {
        
        if (lexer.lexSkipWS(JavaToken.THROWS) == JavaToken.THROWS) {

            final int startContext = writeCurContext();
            
            listener.onThrowsStart(startContext);
    
            for (;;) {
                
                parseTypeReference();
                
                if (lexer.lexSkipWS(JavaToken.COMMA) != JavaToken.COMMA) {
                    break;
                }
            }

            listener.onThrowsEnd(startContext, getLexerContext());
        }
    }

    private static final JavaToken [] BODY_OR_SEMI_COLON = new JavaToken [] {
            
            JavaToken.LBRACE,
            JavaToken.SEMI
    }; 
    
    private void parseMethodBodyOrSemicolon() throws ParserException, IOException {
        
        final JavaToken token = lexer.lexSkipWS(BODY_OR_SEMI_COLON);
        
        switch (token) {

        case LBRACE:
            parseMethodBodyAndRBrace();
            break;
            
        default:
            throw lexer.unexpectedToken();
        }
    }

    private void parseMethodBodyAndRBrace() throws IOException, ParserException {
        
        parseBlockStatementsAndRBrace();
    }
}
