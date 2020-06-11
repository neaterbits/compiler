package com.neaterbits.compiler.parser.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.Base;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ImmutableContext;
import com.neaterbits.compiler.util.model.ReferenceType;
import com.neaterbits.compiler.util.operator.Operator;
import com.neaterbits.compiler.util.operator.Relational;
import com.neaterbits.compiler.util.typedefinition.ClassVisibility;
import com.neaterbits.compiler.util.typedefinition.Subclassing;
import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.StringRef;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

final class JavaLexerParser<COMPILATION_UNIT> {
    
    private final Lexer<JavaToken, CharInput> lexer;
    private final Tokenizer tokenizer;
    private final IterativeParserListener<COMPILATION_UNIT> listener;

    private final Context context;
    
    JavaLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParserListener<COMPILATION_UNIT> listener) {

        Objects.requireNonNull(lexer);
        Objects.requireNonNull(listener);
        
        this.lexer = lexer;
        this.listener = listener;
        this.tokenizer = tokenizer;
        
        this.context = new LexerContext(file, lexer, tokenizer);
    }

    COMPILATION_UNIT parse() throws IOException, ParserException {
        
        return parseCompilationUnit();
    }
    
    private ImmutableContext getCurrentContext() {
        
        return new ImmutableContext(context);
    }
    
    private long getStringRef() {
        return lexer.getStringRef(0, 0);
    }
    
    private static JavaToken [] IMPORT_OR_TYPE_OR_EOF = new JavaToken [] {
            JavaToken.IMPORT,
            
            JavaToken.PUBLIC,
            JavaToken.FINAL,
            JavaToken.ABSTRACT,
            
            JavaToken.CLASS,
            JavaToken.EOF
    };

    private static JavaToken [] TYPE_OR_EOF = new JavaToken [] {

            JavaToken.PUBLIC,
            JavaToken.FINAL,
            JavaToken.ABSTRACT,
            
            JavaToken.CLASS,
            JavaToken.EOF
    };

    private COMPILATION_UNIT parseCompilationUnit() throws IOException, ParserException {

        listener.onCompilationUnitStart(context);

        JavaToken token = lexer.lexSkipWS(JavaToken.PACKAGE);

        if (token != JavaToken.NONE) {

            parsePackageNameAndSemiColon(getStringRef(), getCurrentContext());

        }
        
        // Either import or class
        boolean done = false;
        
        long modifierClassVisibilityKeyword = StringRef.STRING_NONE;
        ClassVisibility classVisibility = null;
        ImmutableContext modifierClassVisibilityKeywordContext = null;
        
        long modifierSubclassingKeyword = StringRef.STRING_NONE;
        Subclassing subclassing = null;
        ImmutableContext modifierSubclassingKeywordContext = null;
        
        do {
            token = lexer.lexSkipWS(IMPORT_OR_TYPE_OR_EOF);
            
            switch (token) {
            case IMPORT:
                parseImport(getStringRef(), getCurrentContext());
                break;
                
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
        
        do {
            // token might be set from while scanning for imports
            switch (token) {
            
            case PUBLIC:
                classVisibility = ClassVisibility.PUBLIC;
                modifierClassVisibilityKeyword = getStringRef();
                modifierClassVisibilityKeywordContext = getCurrentContext();
                break;
                
            case ABSTRACT:
                if (modifierSubclassingKeyword != StringRef.STRING_NONE) {
                    throw lexer.unexpectedToken();
                }
                
                subclassing = Subclassing.ABSTRACT;
                modifierSubclassingKeyword = getStringRef();
                modifierSubclassingKeywordContext = getCurrentContext();
                break;
                
            case FINAL:
                if (modifierSubclassingKeyword != StringRef.STRING_NONE) {
                    throw lexer.unexpectedToken();
                }
                
                subclassing = Subclassing.FINAL;
                modifierSubclassingKeyword = getStringRef();
                modifierSubclassingKeywordContext = getCurrentContext();
                break;
                
            case CLASS:
                parseClass(
                        getStringRef(),
                        getCurrentContext(),
                        modifierClassVisibilityKeyword,
                        classVisibility,
                        modifierClassVisibilityKeywordContext,
                        modifierSubclassingKeyword,
                        subclassing,
                        modifierSubclassingKeywordContext);

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

        return listener.onCompilationUnitEnd(context);
    }
    
    private static final JavaToken [] PERIOD_OR_SEMI = new JavaToken[] {
            JavaToken.PERIOD,
            JavaToken.SEMI
    };
    
    private void parsePackageNameAndSemiColon(
            long namespaceKeyword,
            Context namespaceKeywordContext) throws IOException, ParserException {

        listener.onNamespaceStart(
                null,
                namespaceKeyword,
                namespaceKeywordContext);

        for (;;) {
            
            final JavaToken identifierToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
            
            if (identifierToken == JavaToken.NONE) {
                break;
            }

            listener.onNamespacePart(getCurrentContext(), lexer.getStringRef());

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

        listener.onNameSpaceEnd(null);
    }
    
    private static final JavaToken [] IMPORT_STATIC_OR_IDENTIFIER = new JavaToken [] {
      
            JavaToken.STATIC,
            JavaToken.IDENTIFIER
            
    };

    private static JavaToken [] IMPORT_IDENTIFIER_OR_ASTERISK = new JavaToken[] {
      
            JavaToken.IDENTIFIER,
            JavaToken.ASTERISK
    };
    
    private void parseImport(long importKeyword, ImmutableContext importKeywordContext) throws IOException, ParserException {
        
        final JavaToken staticOrIdentifiertoken = lexer.lexSkipWS(IMPORT_STATIC_OR_IDENTIFIER);
        
        final long staticKeyword;
        final ImmutableContext staticKeywordContext;
        
        if (staticOrIdentifiertoken == JavaToken.STATIC) {
            staticKeyword = getStringRef();
            staticKeywordContext = getCurrentContext();
        }
        else {
            staticKeyword = StringRef.STRING_NONE;
            staticKeywordContext = null;
        }
        
        listener.onImportStart(
                null,
                importKeyword, importKeywordContext,
                staticKeyword, staticKeywordContext);
        
        if (staticOrIdentifiertoken == JavaToken.IDENTIFIER) {
            
            // Initial token of package name
            listener.onImportIdentifier(context, getStringRef());
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
                
                listener.onImportIdentifier(context, getStringRef());
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
        
        listener.onImportEnd(null, ondemand);
    }
    
    private void parseClass(
            long classKeyword,
            Context classKeywordContext,
            long modifierClassVisibilityKeyword,
            ClassVisibility classVisibility,
            Context modifierClassVisibilityKeywordContext,
            long modifierSubclassingKeyword,
            Subclassing subclassing,
            Context modifierSubclassingKeywordContext) throws IOException, ParserException {
        
        final JavaToken classNameToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
        if (classNameToken != JavaToken.IDENTIFIER) {
            throw lexer.unexpectedToken();
        }
        
        // Initial context of class is either class visibility or subclassing or class keyword
        final Context classStartContext;
        
        if (modifierClassVisibilityKeywordContext != null && modifierSubclassingKeywordContext != null) {
            
            classStartContext = modifierClassVisibilityKeywordContext.getStartOffset() < modifierSubclassingKeywordContext.getStartOffset()
                    ? modifierClassVisibilityKeywordContext
                    : modifierSubclassingKeywordContext;
        }
        else if (modifierClassVisibilityKeywordContext != null) {
            classStartContext = modifierClassVisibilityKeywordContext;
        }
        else if (modifierSubclassingKeywordContext != null) {
            classStartContext = modifierSubclassingKeywordContext;
        }
        else {
            classStartContext = classKeywordContext;
        }
        
        listener.onClassStart(classStartContext, classKeyword, classKeywordContext, getStringRef(), getCurrentContext());
        
        if (modifierClassVisibilityKeyword != StringRef.STRING_NONE) {
            listener.onVisibilityClassModifier(modifierClassVisibilityKeywordContext, classVisibility);
        }
        
        if (modifierSubclassingKeyword != StringRef.STRING_NONE) {
            listener.onSubclassingModifier(modifierSubclassingKeywordContext, subclassing);
        }

        parseClassGenericsOrExtendsOrImplementsOrBody();
        
        listener.onClassEnd(null);
    }
    
    private static final JavaToken [] AFTER_CLASSNAME = new JavaToken [] {
            JavaToken.LT, // generics type start
            JavaToken.EXTENDS,
            JavaToken.IMPLEMENTS,
            JavaToken.LBRACE
    };

    private void parseClassGenericsOrExtendsOrImplementsOrBody() throws IOException, ParserException {
        
        final JavaToken afterClassName = lexer.lexSkipWS(AFTER_CLASSNAME);
        
        switch (afterClassName) {
        case EXTENDS:
            parseExtends();
            
            parseImplementsOrBody();
            break;
            
        case IMPLEMENTS:
            parseImplements();
            
            parseClassBody();
            break;
            
        case LBRACE:
            parseClassBody();
            break;
            
        default:
            throw lexer.unexpectedToken();
        }
    }

    private static final JavaToken [] AFTER_EXTENDS = new JavaToken [] {
            JavaToken.IMPLEMENTS,
            JavaToken.LBRACE
    };

    private void parseImplementsOrBody() throws IOException, ParserException {
        
        final JavaToken afterExtends = lexer.lexSkipWS(AFTER_EXTENDS);
        
        switch (afterExtends) {
        
        case IMPLEMENTS:
            parseImplements();
            
            parseClassBody();
            break;
            
        case LBRACE:
            parseClassBody();
            break;
            
        default:
            throw lexer.unexpectedToken();
        }
    }
    
    private void parseExtends() throws IOException, ParserException {

        final Context extendsContext = getCurrentContext();
        
        final long extendsKeyword = lexer.getStringRef();
        
        listener.onClassExtendsStart(
                extendsContext,
                extendsKeyword,
                extendsContext);
        
        parseScopedName(listener::onClassExtendsNamePart);
        
        listener.onClassExtendsEnd(extendsContext);
    }
    
    private static final JavaToken [] AFTER_IMPLEMENTS_TYPE = new JavaToken [] {
            JavaToken.LBRACE,
            JavaToken.COMMA
    };
    
    private void parseImplements() throws IOException, ParserException {
        
        final Context implementsContext = getCurrentContext();
        
        final long implementsKeyword = lexer.getStringRef();
        
        listener.onClassImplementsStart(implementsContext, implementsKeyword, implementsContext);
    
        for (;;) {

            listener.onClassImplementsTypeStart(implementsContext);

            parseScopedName(listener::onClassImplementsNamePart);

            listener.onClassImplementsTypeEnd(implementsContext);

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
        
        listener.onClassImplementsEnd(implementsContext);
    }
    
    @FunctionalInterface
    interface OnScopedNamePart {
        
        void onPart(Context context, long identifier);
    }
    
    private void parseScopedName(OnScopedNamePart processPart) throws IOException, ParserException {

        for (;;) {
            final JavaToken identifierToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
            
            processPart.onPart(getCurrentContext(), lexer.getStringRef());
            
            if (identifierToken == JavaToken.NONE) {
                throw lexer.unexpectedToken();
            }
            
            final JavaToken periodToken = lexer.lexSkipWS(JavaToken.PERIOD);
            if (periodToken == JavaToken.NONE) {
                break;
            }
        }
    }
    
    private void parseClassBody() throws IOException, ParserException {
        
        do {
            
            parseMember();
            
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
    
    private void parseMember() throws IOException, ParserException {

        final JavaToken initialToken = lexer.lexSkipWS(MEMBER_START_TOKENS);
        
        switch (initialToken) {
        
        case VOID:
        case BYTE:
        case SHORT:
        case INT:
        case LONG:
        case FLOAT:
        case DOUBLE:
        case CHAR:
            parseRestOfTypeAndFieldScalar(getCurrentContext(), getStringRef());
            break;
        
        case IDENTIFIER:
            // Probably a type, is it a scoped type?
            final long typeName = lexer.getStringRef();
            
            parseRestOfTypeAndFieldScopedType(getCurrentContext(), typeName);
            break;
        
        case NONE:
            // Not a member variable or method
            break;
            
        default:
            throw lexer.unexpectedToken();
        }
    }
    
    private static final JavaToken [] AFTER_FIELD_NAME = new JavaToken [] {
            
            JavaToken.SEMI,
            JavaToken.COMMA,
            JavaToken.LBRACKET,
            JavaToken.LPAREN
    };

    private void parseRestOfTypeAndFieldScalar(Context typeNameContext, long typeName) throws IOException, ParserException {
        
        parseRestOfMember(typeNameContext, typeName, null, ReferenceType.SCALAR);
    }

    private void parseRestOfTypeAndFieldScopedType(Context typeNameContext, long typeName) throws IOException, ParserException {
        
        final JavaToken periodToken = lexer.lexSkipWS(JavaToken.PERIOD);
        
        final List<ScopedNamePart> scopedName;
        if (periodToken == JavaToken.PERIOD) {
            scopedName = parseRestOfScopedName(typeNameContext, typeName);
        }
        else {
            scopedName = null;
        }
        
        parseRestOfMember(typeNameContext, typeName, scopedName, ReferenceType.REFERENCE);
    }
    
    private void parseRestOfMember(
            Context typeNameContext,
            long typeName,
            List<ScopedNamePart> scopedTypeName,
            ReferenceType referenceType) throws ParserException, IOException {

        // Next should be the name of the field or member
        final JavaToken fieldNameToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
    
        if (fieldNameToken != JavaToken.IDENTIFIER) {
            throw lexer.unexpectedToken();
        }
        
        final long identifier = getStringRef();
        
        final Context declaratorContext = getCurrentContext();
        
        // Next should be start of method, semicolon after type, array indicator or comma separated variables
        final JavaToken afterFieldToken = lexer.lexSkipWS(AFTER_FIELD_NAME);
        
        switch (afterFieldToken) {
        case SEMI:
            // This is a field with a single variable name, like 'int a;'
            listener.onFieldDeclarationStart(context);
            
            onType(declaratorContext, typeName, scopedTypeName, referenceType);

            listener.onVariableDeclaratorStart(declaratorContext);
            
            listener.onVariableName(declaratorContext, identifier, 0);
            
            listener.onVariableDeclaratorEnd(declaratorContext);
            
            listener.onFieldDeclarationEnd(context);
            break;
            
        case COMMA:
            // This is a field with multiple variable names, like 'int a, b, c;'
            listener.onFieldDeclarationStart(context);
            
            onType(declaratorContext, typeName, scopedTypeName, referenceType);

            // Initial variable name
            listener.onVariableDeclaratorStart(declaratorContext);
            listener.onVariableName(declaratorContext, identifier, 0);
            listener.onVariableDeclaratorEnd(declaratorContext);

            parseVariableDeclaratorList(typeNameContext);
            
            listener.onFieldDeclarationEnd(context);
            break;
         
        case LPAREN:
            listener.onClassMethodStart(typeNameContext);
            listener.onMethodReturnTypeStart(declaratorContext);
            onType(typeNameContext, typeName, scopedTypeName, referenceType);
            listener.onMethodReturnTypeEnd(typeNameContext);
            listener.onMethodName(declaratorContext, identifier);

            parseParametersAndMethod(typeNameContext);
            break;
            
        default:
            throw lexer.unexpectedToken();
        }
    }
    
    private void onType(Context context, long typeName, List<ScopedNamePart> scopedTypeName, ReferenceType referenceType) {
        
        
        if (scopedTypeName != null) {
            listener.onScopedTypeReferenceStart(context, referenceType);
            
            for (ScopedNamePart part : scopedTypeName) {
                listener.onScopedTypeReferencePart(part.getContext(), part.getPart());
            }
            
            listener.onScopedTypeReferenceEnd(context);
        }
        else if (typeName != StringRef.STRING_NONE) {
            listener.onNonScopedTypeReference(context, typeName, referenceType);
        }
        else {
            throw new IllegalStateException();
        }
    }

    private static final JavaToken [] AFTER_VARIABLE_NAME = new JavaToken [] {
            
            JavaToken.SEMI,
            JavaToken.COMMA,
            JavaToken.LBRACKET
    };

    private void parseVariableDeclaratorList(Context fieldContext) throws IOException, ParserException {

        boolean done = false;
        
        do {
            final JavaToken fieldNameToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
            
            if (fieldNameToken != JavaToken.IDENTIFIER) {
                throw lexer.unexpectedToken();
            }
            
            final long identifier = getStringRef();
            
            final Context declaratorContext = getCurrentContext();
            
            final JavaToken afterVarNameToken = lexer.lexSkipWS(AFTER_VARIABLE_NAME);

            switch (afterVarNameToken) {
            case COMMA:
            case SEMI:
                listener.onVariableDeclaratorStart(declaratorContext);
                
                listener.onVariableName(declaratorContext, identifier, 0);
                
                listener.onVariableDeclaratorEnd(declaratorContext);
                
                if (afterVarNameToken == JavaToken.SEMI) {
                    done = true;
                }
                break;
                
            default:
                throw lexer.unexpectedToken();
            }
        } while (!done);
    }
    
    private static final JavaToken [] PARAM_TYPE_RPAREN = {
            JavaToken.RPAREN
    };

    private void parseParametersAndMethod(Context methodContext) throws IOException, ParserException {

        listener.onMethodSignatureParametersStart(methodContext);
        
        final JavaToken initialToken = lexer.lexSkipWS(PARAM_TYPE_RPAREN);
        
        switch (initialToken) {
        
        case RPAREN:
            listener.onMethodSignatureParametersEnd(methodContext);
            
            parseMethodBodyOrSemicolon(methodContext);
            break;

        default:
            parseParameters();
            
            final JavaToken token = lexer.lexSkipWS(PARAM_TYPE_RPAREN);
            if (token != JavaToken.RPAREN) {
                throw lexer.unexpectedToken();
            }
            
            parseMethodBodyOrSemicolon(methodContext);
            break;
        }
        
        listener.onMethodSignatureParametersEnd(methodContext);
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
            listener.onNonScopedTypeReference(getCurrentContext(), getStringRef(), ReferenceType.SCALAR);
            break;
            
        case IDENTIFIER:
            parseUserType();
            break;
            
        default:
            throw lexer.unexpectedToken();
        }
    }
    
    private static final JavaToken [] PARAMETER_TOKENS = new JavaToken [] {
            JavaToken.IDENTIFIER,
            JavaToken.COMMA
    };
    
    private void parseParameters() throws IOException, ParserException {
        
        boolean done = false;
        
        do {
            Context curParameterContext = getCurrentContext();

            listener.onMethodSignatureParameterStart(curParameterContext, false);

            parseParameterType();
            
            final JavaToken parameterNameToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
            
            final long parameterName = lexer.getStringRef();

            if (parameterNameToken != JavaToken.IDENTIFIER) {
                throw lexer.unexpectedToken();
            }

            listener.onVariableName(curParameterContext, parameterName, 0);

            final JavaToken afterParameterName = lexer.lexSkipWS(PARAMETER_TOKENS);
            
            switch (afterParameterName) {
            case COMMA:
                listener.onMethodSignatureParameterEnd(curParameterContext);
                // Next parameter
                break;

            default:
                listener.onMethodSignatureParameterEnd(curParameterContext);
                done = true;
                break;
            }

        } while(!done);
        
    }
    
    private static final JavaToken [] BODY_OR_SEMI_COLON = new JavaToken [] {
            
            JavaToken.LBRACE,
            JavaToken.SEMI
    }; 
    
    private void parseMethodBodyOrSemicolon(Context methodContext) throws ParserException, IOException {
        
        final JavaToken token = lexer.lexSkipWS(BODY_OR_SEMI_COLON);
        
        switch (token) {

        case LBRACE:
            parseMethodBodyAndRBrace();
            
            listener.onClassMethodEnd(methodContext);
            break;
            
        default:
            throw lexer.unexpectedToken();
        }
    }

    private static final JavaToken [] STATEMENT_TOKENS = new JavaToken [] {

            // Local variables
            JavaToken.BYTE,
            JavaToken.SHORT,
            JavaToken.INT,
            JavaToken.LONG,
            JavaToken.FLOAT,
            JavaToken.DOUBLE,
            JavaToken.CHAR,

            JavaToken.IF,

            JavaToken.IDENTIFIER,
    };

    private void parseMethodBodyAndRBrace() throws IOException, ParserException {
        
        parseBlockAndRBrace();
    }
    
    private void parseBlockAndRBrace() throws ParserException, IOException {

        parseBlock();
        
        final JavaToken rbraceToken = lexer.lexSkipWS(JavaToken.RBRACE);
        
        if (rbraceToken != JavaToken.RBRACE) {
            throw lexer.unexpectedToken();
        }
    }
    
    private void parseBlock() throws ParserException, IOException {
        
        boolean done = false;
        
        do {
            final boolean statementFound = parseStatement();
            
            done = !statementFound;
            
        } while (!done);
    }
    
    private static JavaToken [] ELSE_IF_OR_ELSE = new JavaToken [] {
            JavaToken.ELSE_IF,
            JavaToken.ELSE
    };
    
    private void parseIfElseIfElse(Context context, long ifKeyword, Context ifKeywordContext) throws IOException, ParserException {

        listener.onIfStatementStart(context, ifKeyword, ifKeywordContext);
        
        parseConditionInParenthesis();

        parseStatementOrBlock();

        listener.onIfStatementInitialBlockEnd(ifKeywordContext);
        
        final JavaToken elseIfOrElseToken = lexer.lexSkipWS(ELSE_IF_OR_ELSE);
        switch (elseIfOrElseToken) {
        case ELSE:
            final Context elseKeywordContext = getCurrentContext();
            
            final long elseKeyword = getStringRef();
            
            System.out.println("## else keyword '" + tokenizer.asString(elseKeyword) + "'");
            
            listener.onElseStatementStart(elseKeywordContext, elseKeyword, elseKeywordContext);
            
            parseStatementOrBlock();
            
            listener.onElseStatementEnd(elseKeywordContext);
            
            listener.onEndIfStatement(getCurrentContext());
            break;
            
        default:
            listener.onEndIfStatement(ifKeywordContext);
            break;
        }
    }
    
    private void parseStatementOrBlock() throws ParserException, IOException {

        final JavaToken optionalLBrace = lexer.lexSkipWS(JavaToken.LBRACE);
        
        if (optionalLBrace == JavaToken.LBRACE) {
            parseBlockAndRBrace();
        }
        else {
            parseStatement();
        }
    }
    
    private void parseConditionInParenthesis() throws IOException, ParserException {
        
        final JavaToken lparen = lexer.lexSkipWS(JavaToken.LPAREN);
        
        if (lparen != JavaToken.LPAREN) {
            throw lexer.unexpectedToken();
        }
        
        parseExpressionList();

        final JavaToken rparen = lexer.lexSkipWS(JavaToken.RPAREN);
        
        if (rparen != JavaToken.RPAREN) {
            throw lexer.unexpectedToken();
        }
    }
    
    private static final JavaToken [] OPERATOR_TOKENS = new JavaToken [] {
            JavaToken.EQUALS,
            JavaToken.NOT_EQUALS,
            JavaToken.LT,
            JavaToken.GT,
            JavaToken.LTE,
            JavaToken.GTE
    };
    
    private void parseExpressionList() throws IOException, ParserException {

        boolean done = false;
        boolean initial = false;
        
        do {
            final boolean expressionFound = parseExpression();
            
            if (!expressionFound) {
                if (initial) {
                    throw lexer.unexpectedToken();
                }
                else {
                    done = true;
                }
            }
            else {
                
                initial = false;
                
                final JavaToken operatorToken = lexer.lexSkipWS(OPERATOR_TOKENS);
                
                switch (operatorToken) {
                case EQUALS:
                    callListenerAndParseExpression(getCurrentContext(), Relational.EQUALS);
                    break;
                    
                case NOT_EQUALS:
                    callListenerAndParseExpression(getCurrentContext(), Relational.NOT_EQUALS);
                    break;
                    
                case LT:
                    callListenerAndParseExpression(getCurrentContext(), Relational.LESS_THAN);
                    break;
                    
                case GT:
                    callListenerAndParseExpression(getCurrentContext(), Relational.GREATER_THAN);
                    break;
    
                case LTE:
                    callListenerAndParseExpression(getCurrentContext(), Relational.LESS_THAN_OR_EQUALS);
                    break;
                    
                case GTE:
                    callListenerAndParseExpression(getCurrentContext(), Relational.GREATER_THAN_OR_EQUALS);
                    break;
                    
                default:
                    done = true;
                    break;
                }
            }
        } while (!done);
    }
    
    private void callListenerAndParseExpression(Context context, Operator operator) throws IOException, ParserException {
        
        listener.onExpressionBinaryOperator(context, operator);
        
    }

    private static final JavaToken [] EXPRESSION_TOKENS = new JavaToken [] {
            
            JavaToken.IDENTIFIER,
            JavaToken.NUMBER,
    };
            
    
    private boolean parseExpression() throws IOException, ParserException {
        
        final JavaToken token = lexer.lexSkipWS(EXPRESSION_TOKENS);
        
        boolean expressionFound = true;
        
        switch (token) {
        case IDENTIFIER:
            // Variable or 'this' or method call
            parseVariableReferenceExpression(getCurrentContext(), getStringRef());
            break;

        case NUMBER:
            parseNumericLiteral(getCurrentContext(), getStringRef());
            break;

        default:
            expressionFound = false;
            break;
        }
        
        return expressionFound;
    }
    
    private void parseVariableReferenceExpression(Context context, long stringRef) {
        
        // For now just say this is a variable
        listener.onVariableReference(context, stringRef);
        
    }
    
    private void parseNumericLiteral(Context context, long stringRef) {

        listener.onIntegerLiteral(
                context,
                tokenizer.asInt(stringRef),
                Base.DECIMAL,
                true,
                32);
    }
    
    private boolean parseStatement() throws ParserException, IOException {
        
        boolean foundStatement = true;
        
        final JavaToken statementToken = lexer.lexSkipWS(STATEMENT_TOKENS);
        
        switch (statementToken) {
        case BYTE:
        case SHORT:
        case INT:
        case LONG:
        case FLOAT:
        case DOUBLE:
        case CHAR: {
            final Context context = getCurrentContext();
            final long typeName = getStringRef();
            
            parseNonScopedTypeVariableDeclarationStatement(context, typeName, ReferenceType.SCALAR);
            break;
        }
        
        case IDENTIFIER: {
            final Context context = getCurrentContext();
            
            // Can be method invocation, for now parse as variable name
            listener.onVariableDeclarationStatementStart(context);

            parseUserType();
            
            parseVariableDeclaratorList(context);
            
            listener.onVariableDeclarationStatementEnd(context);
            break;
        }
        
        case IF: {
            final Context context = getCurrentContext();
            
            parseIfElseIfElse(context, getStringRef(), context);
            break;
        }

        default:
            foundStatement = false;
            break;
        }

        return foundStatement;
    }
    
    //private static JavaToken [] VARIABLE_DECLARATION_STATEMENT_TOKENS = new JavaToken [] {
    //        JavaToken.COMMA
    // };
    
    private void parseNonScopedTypeVariableDeclarationStatement(Context typeContext, long typeName, ReferenceType referenceType) throws ParserException, IOException {

        listener.onVariableDeclarationStatementStart(context);
        
        listener.onNonScopedTypeReference(typeContext, typeName, referenceType);

        parseVariableDeclaratorList(typeContext);
        
        listener.onVariableDeclarationStatementEnd(context);
    }
    
    private void parseUserType() throws ParserException, IOException {
        final Context identifierContext = getCurrentContext();
        final long stringRef = getStringRef();

        JavaToken scopeToken = lexer.lexSkipWS(JavaToken.PERIOD);
        if (scopeToken == JavaToken.PERIOD) {

            listener.onScopedTypeReferenceStart(identifierContext, ReferenceType.REFERENCE);
            
            listener.onScopedTypeReferencePart(identifierContext, stringRef);

            for (;;) {

                final JavaToken partToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
                
                if (partToken != JavaToken.IDENTIFIER) {
                    throw lexer.unexpectedToken();
                }
                
                listener.onScopedTypeReferencePart(getCurrentContext(), getStringRef());
                
                final JavaToken endOfScopeToken = lexer.lexSkipWS(JavaToken.PERIOD);
                
                if (endOfScopeToken != JavaToken.PERIOD) {
                    break;
                }
            }

            listener.onScopedTypeReferenceEnd(identifierContext);
        }
        else {
            listener.onNonScopedTypeReference(identifierContext, stringRef, ReferenceType.REFERENCE);
        }
    }

    private List<ScopedNamePart> parseRestOfScopedName(Context identifierContext, long identifier) throws IOException, ParserException {
        
        final List<ScopedNamePart> scopedTypeName = new ArrayList<>();

        scopedTypeName.add(new ScopedNamePart(identifierContext, identifier));

        for (;;) {

            final JavaToken partToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
            
            if (partToken != JavaToken.IDENTIFIER) {
                throw lexer.unexpectedToken();
            }
            
            scopedTypeName.add(new ScopedNamePart(getCurrentContext(), getStringRef()));
            
            final JavaToken endOfScopeToken = lexer.lexSkipWS(JavaToken.PERIOD);
            
            if (endOfScopeToken != JavaToken.PERIOD) {
                break;
            }
        }
        
        return scopedTypeName;
    }
}
