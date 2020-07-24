package com.neaterbits.compiler.parser.java;

import java.io.IOException;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ContextRef;
import com.neaterbits.compiler.util.model.ReferenceType;
import com.neaterbits.compiler.util.typedefinition.ClassVisibility;
import com.neaterbits.compiler.util.typedefinition.Subclassing;
import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.parser.recursive.cached.keywords.CachedKeywordsList;
import com.neaterbits.compiler.parser.recursive.cached.types.TypeArgumentsList;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.StringRef;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

final class JavaLexerParser<COMPILATION_UNIT> extends JavaStatementsLexerParser<COMPILATION_UNIT> {

    private final TypeScratchInfo typeScratchInfo;
    
    JavaLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParserListener<COMPILATION_UNIT> listener) {

        super(file, lexer, tokenizer, listener);
        
        this.typeScratchInfo = new TypeScratchInfo();
    }

    COMPILATION_UNIT parse() throws IOException, ParserException {
        
        return parseCompilationUnit();
    }
    
    private static JavaToken [] IMPORT_OR_TYPE_OR_EOF = new JavaToken [] {
            JavaToken.IMPORT,
            
            JavaToken.AT,
            JavaToken.PUBLIC,
            JavaToken.FINAL,
            JavaToken.ABSTRACT,
            
            JavaToken.CLASS,
            JavaToken.EOF
    };

    private static JavaToken [] TYPE_OR_EOF = new JavaToken [] {

            JavaToken.AT,
            JavaToken.PUBLIC,
            JavaToken.FINAL,
            JavaToken.ABSTRACT,
            
            JavaToken.CLASS,
            JavaToken.EOF
    };
    
    private COMPILATION_UNIT parseCompilationUnit() throws IOException, ParserException {

        final int compilationUnitStartContext = writeCurContext();
        
        listener.onCompilationUnitStart(compilationUnitStartContext);

        JavaToken token = lexer.lexSkipWS(JavaToken.PACKAGE);

        if (token != JavaToken.NONE) {
            
            final int packageContext = writeCurContext();
            
            final int packageKeywordContext = writeCurContext();

            parsePackageNameAndSemiColon(packageContext, getStringRef(), packageKeywordContext);
        }
        
        // Either import or class
        boolean done = false;
        
        do {
            token = lexer.lexSkipWS(IMPORT_OR_TYPE_OR_EOF);
            
            switch (token) {
            case IMPORT: {
                final int importStartContext = writeCurContext();
                final int importKeywordContext = writeCurContext();
                
                parseImport(importStartContext, getStringRef(), importKeywordContext);
                break;
            }
                
            case AT:
            case PUBLIC:
            case ABSTRACT:
            case FINAL:
            case CLASS:
            case EOF:
                done = true;
                break;

            default:
                throw lexer.unexpectedToken();
            }
            
        } while (!done);

        done = false;
        
        final int typeStartContext;
        
        if (token != JavaToken.EOF) {
            typeStartContext = writeContext(compilationUnitStartContext);
            
            listener.onTypeDefinitionStart(typeStartContext);
        }
        else {
            typeStartContext = ContextRef.NONE;
        }
        
        do {
            // token might be set from while scanning for imports
            switch (token) {
            
            case AT:
                parseAnnotation(writeCurContext());
                break;
            
            case PUBLIC:
                listener.onVisibilityClassModifier(writeCurContext(), ClassVisibility.PUBLIC);
                break;
                
            case ABSTRACT:
                listener.onSubclassingModifier(writeCurContext(), Subclassing.ABSTRACT);
                break;
                
            case FINAL:
                listener.onSubclassingModifier(writeCurContext(), Subclassing.FINAL);
                break;
                
            case CLASS:
                final int classStartContext = writeCurContext();
                final int classKeywordContext = writeCurContext();
                
                parseClass(classStartContext, getStringRef(), classKeywordContext);

                if (lexer.lexSkipWS(JavaToken.EOF) != JavaToken.EOF) {
                    throw lexer.unexpectedToken();
                }
             
                done = true;
                break;
                
            case EOF:
                done = true;
                break;

            default:
                throw lexer.unexpectedToken();
            }

            if (!done) {
                token = lexer.lexSkipWS(TYPE_OR_EOF);
            }

        } while(!done);

        if (token != JavaToken.EOF) {
            listener.onTypeDefinitionEnd(typeStartContext, getLexerContext());
        }
        
        return listener.onCompilationUnitEnd(compilationUnitStartContext, getLexerContext());
    }
    
    private static final JavaToken [] PERIOD_OR_SEMI = new JavaToken[] {
            JavaToken.PERIOD,
            JavaToken.SEMI
    };
    
    private void parsePackageNameAndSemiColon(
            int namespaceStartContext,
            long namespaceKeyword,
            int namespaceKeywordContext) throws IOException, ParserException {

        listener.onNamespaceStart(
                namespaceStartContext,
                namespaceKeyword,
                namespaceKeywordContext);

        for (;;) {
            
            final JavaToken identifierToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
            
            if (identifierToken == JavaToken.NONE) {
                break;
            }

            final int namespacePartContext = writeCurContext();
            
            listener.onNamespacePart(namespacePartContext, lexer.getStringRef());

            final JavaToken nextToken = lexer.lexSkipWS(PERIOD_OR_SEMI);
            
            if (nextToken == JavaToken.SEMI) {
                break;
            }
            else if (nextToken == JavaToken.NONE) {
                throw new ParserException("Neither period nor semicolon at end of package declaration");
            }
            else {
                if (nextToken != JavaToken.PERIOD) {
                    throw lexer.unexpectedToken();
                }
            }
        }

        listener.onNameSpaceEnd(namespaceStartContext, getLexerContext());
    }
    
    private static final JavaToken [] IMPORT_STATIC_OR_IDENTIFIER = new JavaToken [] {
      
            JavaToken.STATIC,
            JavaToken.IDENTIFIER
            
    };

    private static JavaToken [] IMPORT_IDENTIFIER_OR_ASTERISK = new JavaToken[] {
      
            JavaToken.IDENTIFIER,
            JavaToken.ASTERISK
    };
    
    private void parseImport(int importStartContext, long importKeyword, int importKeywordContext) throws IOException, ParserException {
        
        final JavaToken staticOrIdentifiertoken = lexer.lexSkipWS(IMPORT_STATIC_OR_IDENTIFIER);
        
        final long staticKeyword;
        final int staticKeywordContext;
        
        if (staticOrIdentifiertoken == JavaToken.STATIC) {
            staticKeyword = getStringRef();
            staticKeywordContext = writeCurContext();
        }
        else {
            staticKeyword = StringRef.STRING_NONE;
            staticKeywordContext = ContextRef.NONE;
        }
        
        listener.onImportStart(
                importStartContext,
                importKeyword, importKeywordContext,
                staticKeyword, staticKeywordContext);
        
        if (staticOrIdentifiertoken == JavaToken.IDENTIFIER) {
            
            final int importIdentifierContext = writeCurContext();
            
            // Initial token of package name
            listener.onImportName(importIdentifierContext, getStringRef());
        }

        boolean parseIdentifier = staticOrIdentifiertoken != JavaToken.IDENTIFIER;
        
        boolean ondemand = false;
        
        for (;;) {
            
            if (parseIdentifier) {
                // Not already parsed identifier, parse that first
                
                final JavaToken identifierToken = lexer.lexSkipWS(IMPORT_IDENTIFIER_OR_ASTERISK);

                if (identifierToken == JavaToken.NONE) {
                    if (lexer.lexSkipWS(JavaToken.SEMI) != JavaToken.SEMI) {
                        throw lexer.unexpectedToken();
                    }
                    break;
                }
                else if (identifierToken == JavaToken.ASTERISK) {
                    ondemand = true;

                    if (lexer.lexSkipWS(JavaToken.SEMI) != JavaToken.SEMI) {
                        throw lexer.unexpectedToken();
                    }
                    break;
                }

                final int anotherImportIdentifierContext = writeCurContext();
                
                listener.onImportName(anotherImportIdentifierContext, getStringRef());
            }
            else {
                parseIdentifier = true;
            }

            final JavaToken nextToken = lexer.lexSkipWS(PERIOD_OR_SEMI);
            
            if (nextToken == JavaToken.SEMI) {
                break;
            }
            else if (nextToken == JavaToken.NONE) {
                throw new ParserException("Neither period nor semicolon at end of package declaration");
            }
            else {
                if (nextToken != JavaToken.PERIOD) {
                    throw new IllegalStateException();
                }
            }
        }
        
        listener.onImportEnd(importStartContext, getLexerContext(), ondemand);
    }
    
    
    private void parseClass(
            int classStartContext,
            long classKeyword,
            int classKeywordContext) throws IOException, ParserException {
        
        final JavaToken classNameToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
        if (classNameToken != JavaToken.IDENTIFIER) {
            throw lexer.unexpectedToken();
        }
        
        final long className = getStringRef();
        final int classNameContext = writeCurContext();

        // Initial context of class is either class visibility or subclassing or class keyword
        listener.onClassStart(classStartContext, classKeyword, classKeywordContext, className, classNameContext);
        
        parseClassGenericsOrExtendsOrImplementsOrBody(className);
        
        listener.onClassEnd(classStartContext, getLexerContext());
    }
    
    private static final JavaToken [] AFTER_CLASSNAME = new JavaToken [] {
            JavaToken.LT, // generics type start
            JavaToken.EXTENDS,
            JavaToken.IMPLEMENTS,
            JavaToken.LBRACE
    };

    private void parseClassGenericsOrExtendsOrImplementsOrBody(long implementingClassName) throws IOException, ParserException {
        
        final JavaToken afterClassName = lexer.lexSkipWS(AFTER_CLASSNAME);
        
        switch (afterClassName) {
        
        case LT:
            parseGenericTypeArguments();
            
            parseExtendsOrImplementsOrBody(implementingClassName);
            break;
        
        case EXTENDS:
            parseExtends();
            
            parseImplementsOrBody(implementingClassName);
            break;
            
        case IMPLEMENTS:
            parseImplements();
            
            parseClassBody(implementingClassName);
            break;
            
        case LBRACE:
            parseClassBody(implementingClassName);
            break;
            
        default:
            throw lexer.unexpectedToken();
        }
    }
    

    private static final JavaToken [] EXTENDS_OR_IMPLEMENTS_OR_BODY = new JavaToken [] {
            JavaToken.EXTENDS,
            JavaToken.IMPLEMENTS,
            JavaToken.LBRACE
    };

    private void parseExtendsOrImplementsOrBody(long implementingClassName) throws IOException, ParserException {
        
        final JavaToken afterClassName = lexer.lexSkipWS(EXTENDS_OR_IMPLEMENTS_OR_BODY);
        
        switch (afterClassName) {
        
        case EXTENDS:
            parseExtends();
            
            parseImplementsOrBody(implementingClassName);
            break;
            
        case IMPLEMENTS:
            parseImplements();
            
            parseClassBody(implementingClassName);
            break;
            
        case LBRACE:
            parseClassBody(implementingClassName);
            break;
            
        default:
            throw lexer.unexpectedToken();
        }
    }

    private static final JavaToken [] AFTER_EXTENDS = new JavaToken [] {
            JavaToken.IMPLEMENTS,
            JavaToken.LBRACE
    };

    private void parseImplementsOrBody(long implementingClassName) throws IOException, ParserException {
        
        final JavaToken afterExtends = lexer.lexSkipWS(AFTER_EXTENDS);
        
        switch (afterExtends) {
        
        case IMPLEMENTS:
            parseImplements();
            
            parseClassBody(implementingClassName);
            break;
            
        case LBRACE:
            parseClassBody(implementingClassName);
            break;
            
        default:
            throw lexer.unexpectedToken();
        }
    }
    
    private void parseExtends() throws IOException, ParserException {

        final int extendsStartContext = writeCurContext();
        
        final long extendsKeyword = lexer.getStringRef();
        final int extendsKeywordContext = writeCurContext();
        
        listener.onClassExtendsStart(
                extendsStartContext,
                extendsKeyword,
                extendsKeywordContext);
        
        parseScopedName(listener::onClassExtendsNamePart);
        
        listener.onClassExtendsEnd(extendsStartContext, getLexerContext());
    }
    
    private static final JavaToken [] AFTER_IMPLEMENTS_TYPE = new JavaToken [] {
            JavaToken.LBRACE,
            JavaToken.COMMA
    };
    
    private void parseImplements() throws IOException, ParserException {
        
        final int implementsStartContext = writeCurContext();
        final int implementsKeywordContext = writeContext(implementsStartContext);
        
        final long implementsKeyword = lexer.getStringRef();
        
        listener.onClassImplementsStart(implementsStartContext, implementsKeyword, implementsKeywordContext);
    
        for (;;) {

            final int implementsTypeStartContext = writeCurContext();
            
            listener.onClassImplementsTypeStart(implementsTypeStartContext);

            parseScopedName(listener::onClassImplementsNamePart);

            listener.onClassImplementsTypeEnd(implementsTypeStartContext, getLexerContext());

            final JavaToken afterType = lexer.lexSkipWS(AFTER_IMPLEMENTS_TYPE);
            
            if (afterType == JavaToken.LBRACE) {
                break;
            }
            else if (afterType == JavaToken.COMMA) {
                // Continue
            }
            else {
                throw lexer.unexpectedToken();
            }
        }
        
        listener.onClassImplementsEnd(implementsStartContext, getLexerContext());
    }
    
    @FunctionalInterface
    interface OnScopedNamePart {
        
        void onPart(int context, long identifier);
    }
    
    private void parseScopedName(OnScopedNamePart processPart) throws IOException, ParserException {

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
    
    private void parseClassBody(long implementingClassName) throws IOException, ParserException {
        
        do {
            
            parseMember(implementingClassName);
            
        } while (lexer.lexSkipWS(JavaToken.RBRACE) != JavaToken.RBRACE);
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
    
    private void parseMember(long implementingClassName) throws IOException, ParserException {

        final CachedKeywordsList<JavaToken> modifiers = parseAnyMemberModifiers();
        
        final JavaToken initialToken = lexer.lexSkipWS(MEMBER_START_TOKENS);
        
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
                parseRestOfTypeAndFieldScalar(modifiers, writeCurContext(), getStringRef(), afterTypeContext);
            }
            finally {
                freeScratchContext(afterTypeContext);
            }
            break;
        }
        
        case IDENTIFIER:
            // Probably a type, is it a scoped type?
            final long typeName = lexer.getStringRef();
            
            parseRestOfTypeAndFieldScopedType(modifiers, implementingClassName, writeCurContext(), typeName);
            break;
        
        case NONE:
            // Not a member variable or method
            break;
            
        default:
            throw lexer.unexpectedToken();
        }
    }
    
    private static final JavaToken [] MEMBER_MODIFIER_TOKENS = new JavaToken [] {
      
            JavaToken.PUBLIC,
            JavaToken.PRIVATE,
            JavaToken.FINAL,
            
            JavaToken.STATIC
    };
    
    private CachedKeywordsList<JavaToken> parseAnyMemberModifiers() throws IOException, ParserException {
        
        boolean done = false;
        
        final CachedKeywordsList<JavaToken> cachedModifiers = startScratchKeywords();
        
        do {
            final JavaToken memberModifierToken = lexer.lexSkipWS(MEMBER_MODIFIER_TOKENS);

            switch (memberModifierToken) {
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
        
        return cachedModifiers;
    }
    
    private static final JavaToken [] AFTER_FIELD_NAME = new JavaToken [] {
            
            JavaToken.SEMI,
            JavaToken.COMMA,
            JavaToken.LBRACKET,
            JavaToken.LPAREN
    };

    private void parseRestOfTypeAndFieldScalar(
            CachedKeywordsList<JavaToken> modifiers,
            int typeNameContext,
            long typeName,
            Context afterTypeContext) throws IOException, ParserException {
        
        typeScratchInfo.initNonScoped(typeName, typeNameContext, afterTypeContext, ReferenceType.SCALAR);
        
        parseRestOfMemberAfterInitialTypeDeclaration(modifiers, typeScratchInfo, null);
    }

    private void parseRestOfTypeAndFieldScopedType(
            CachedKeywordsList<JavaToken> modifiers,
            long implementingClassName,
            int typeNameContext,
            long typeName) throws IOException, ParserException {
        
        final JavaToken periodToken = lexer.lexSkipWS(JavaToken.PERIOD);
        
        if (periodToken == JavaToken.PERIOD) {
            
            parseNameListUntilOtherToken(typeNameContext, typeName, names -> {
                
                final TypeArgumentsList typeArguments = tryParseGenericTypeParametersToScratchList();

                typeScratchInfo.initScoped(names, getLexerContext(), ReferenceType.REFERENCE);

                parseRestOfMemberAfterInitialTypeDeclaration(modifiers, typeScratchInfo, typeArguments);
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
                    
                    parseMethod(modifiers, typeScratchInfo, getLexerContext(), typeNameContext, typeName);
                }
            }
            else {
                typeScratchInfo.initNonScoped(typeName, typeNameContext, getLexerContext(), ReferenceType.REFERENCE);
                
                parseRestOfMemberAfterInitialTypeDeclaration(modifiers, typeScratchInfo, typeArguments);
            }
        }
    }
    
    private void parseRestOfMemberAfterInitialTypeDeclaration(
            CachedKeywordsList<JavaToken> modifiers,
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
                    
                    if (modifiers != null) {
                        modifiers.complete(keywords -> {
                            listenerHelper.callFieldMemberModifiers(keywords);
                        });
                    }
                    
                    if (typeArguments != null) {
                        typeArguments.complete(genericTypes -> listenerHelper.onType(typeName, genericTypes, typeEndContext));
                    }
                    else {
                        listenerHelper.onType(typeName, null, typeEndContext);
                    }
                    
                    final int variableDeclaratorStartContext = writeContext(identifierContext);
                    
                    listenerHelper.onVariableDeclarator(
                            variableDeclaratorStartContext,
                            identifier,
                            identifierContext,
                            variableDeclaratorEndContext);
                    
                    listener.onFieldDeclarationEnd(fieldDeclarationStartContext, getLexerContext());
                    break;
                }
                    
                case COMMA: {
                    // This is a field with multiple variable names, like 'int a, b, c;'
                    listener.onFieldDeclarationStart(fieldDeclarationStartContext);
                    
                    if (typeArguments != null) {
                        typeArguments.complete(genericTypes -> listenerHelper.onType(typeName, genericTypes, getLexerContext()));
                    }
                    else {
                        listenerHelper.onType(typeName, null, getLexerContext());
                    }
        
                    // Initial variable name
                    final int variableDeclaratorStartContext = writeContext(identifierContext);
        
                    listenerHelper.onVariableDeclarator(
                            variableDeclaratorStartContext,
                            identifier,
                            identifierContext,
                            variableDeclaratorEndContext);
        
                    parseVariableDeclaratorList();
                    
                    listener.onFieldDeclarationEnd(variableDeclaratorStartContext, getLexerContext());
                    break;
                }
                 
                case LPAREN: {
                    parseMethod(modifiers, typeName, typeEndContext, identifierContext, identifier);
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
    
    private static final JavaToken [] PARAMETER_TOKENS = new JavaToken [] {
            JavaToken.IDENTIFIER,
            JavaToken.COMMA
    };
    
    private void parseMethodSignatureParameters() throws IOException, ParserException {
        
        boolean done = false;
        
        do {
            final int curParameterStartContext = writeCurContext();

            listener.onMethodSignatureParameterStart(curParameterStartContext, false);

            parseParameterType();
            
            final JavaToken parameterNameToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
            
            final long parameterName = lexer.getStringRef();

            if (parameterNameToken != JavaToken.IDENTIFIER) {
                throw lexer.unexpectedToken();
            }

            listener.onVariableName(curParameterStartContext, parameterName, 0);

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
