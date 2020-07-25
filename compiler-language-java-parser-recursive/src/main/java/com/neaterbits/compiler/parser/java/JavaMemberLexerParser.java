package com.neaterbits.compiler.parser.java;

import java.io.IOException;

import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.parser.recursive.cached.annotations.CachedAnnotationsList;
import com.neaterbits.compiler.parser.recursive.cached.keywords.CachedKeywordsList;
import com.neaterbits.compiler.parser.recursive.cached.types.TypeArgumentsList;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ReferenceType;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

abstract class JavaMemberLexerParser<COMPILATION_UNIT> extends JavaStatementsLexerParser<COMPILATION_UNIT> {

    private final TypeScratchInfo typeScratchInfo;
    
    // For returning multiple values without allocation
    private CachedKeywordsList<JavaToken> cachedModifiers;
    private CachedAnnotationsList cachedAnnotations;

    JavaMemberLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParserListener<COMPILATION_UNIT> listener) {
        
        super(file, lexer, tokenizer, listener);

        this.typeScratchInfo = new TypeScratchInfo();
    }

    private static JavaToken [] MEMBER_START_TOKENS = new JavaToken [] {
            
            JavaToken.PRIVATE,
            JavaToken.PUBLIC,
            
            JavaToken.LT, // generic method
            
            JavaToken.VOID,
            
            JavaToken.BYTE,
            JavaToken.SHORT,
            JavaToken.INT,
            JavaToken.LONG,
            JavaToken.FLOAT,
            JavaToken.DOUBLE,
            JavaToken.CHAR,

            JavaToken.IDENTIFIER // type
    };
    
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
            
            JavaToken.STATIC
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
            JavaToken.LBRACKET,
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
                
                final TypeArgumentsList typeArguments = tryParseGenericTypeParametersToScratchList();

                typeScratchInfo.initScoped(names, getLexerContext(), ReferenceType.REFERENCE);

                parseRestOfMemberAfterInitialTypeDeclaration(modifiers, annotations, typeScratchInfo, typeArguments);
            });
        }
        else {
            final TypeArgumentsList typeArguments = tryParseGenericTypeParametersToScratchList();

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
                    
                    parseMethod(modifiers, annotations, typeScratchInfo, getLexerContext(), typeNameContext, typeName);
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
                    parseMethod(modifiers, annotations, typeName, typeEndContext, identifierContext, identifier);
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
    
    private void parseMethod(
            CachedKeywordsList<JavaToken> modifiers,
            CachedAnnotationsList annotations,
            TypeScratchInfo typeInfo,
            Context typeEndContext,
            int identifierContext,
            long identifier) throws IOException, ParserException {

        final int typeStartContext = typeInfo.getStartContext();
        
        final int classMethodStartContext = writeContext(typeStartContext);
        final int methodReturnTypeStartContext = writeContext(typeStartContext);
        final int methodParametersStartContext = writeContext(typeStartContext);
        
        listener.onClassMethodStart(classMethodStartContext);
        
        if (modifiers != null) {
            modifiers.complete(keywords -> {
                listenerHelper.callClassMethodMemberModifiers(keywords);
            });
        }
        
        if (annotations != null) {
            apply(annotations, listener);
        }
        
        listener.onMethodReturnTypeStart(methodReturnTypeStartContext);
        
        listenerHelper.onType(typeInfo, null, typeEndContext);
        
        listener.onMethodReturnTypeEnd(methodReturnTypeStartContext, getLexerContext());
        
        listener.onMethodName(identifierContext, identifier);

        parseParametersAndMethod(methodParametersStartContext);

        listener.onClassMethodEnd(classMethodStartContext, getLexerContext());

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
            
            parseMethodBodyOrSemicolon();
            break;

        default:
            parseMethodSignatureParameters();
            
            final JavaToken token = lexer.lexSkipWS(PARAM_TYPE_RPAREN);
            if (token != JavaToken.RPAREN) {
                throw lexer.unexpectedToken();
            }
            
            parseMethodBodyOrSemicolon();
            break;
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
        
        parseBlockAndRBrace();
    }
}
