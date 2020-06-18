package com.neaterbits.compiler.parser.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.Base;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ContextRef;
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
    
    private Context getLexerContext() {
        return context;
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
    
    private int writeCurContext() {
        
        return listener.writeContext(context);
    }

    private int writeContext(int otherContext) {
        
        return listener.writeContext(otherContext);
    }
    
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
        
        long modifierClassVisibilityKeyword = StringRef.STRING_NONE;
        ClassVisibility classVisibility = null;
        int modifierClassVisibilityKeywordContext = ContextRef.NONE;
        
        long modifierSubclassingKeyword = StringRef.STRING_NONE;
        Subclassing subclassing = null;
        int modifierSubclassingKeywordContext = ContextRef.NONE;
        
        do {
            token = lexer.lexSkipWS(IMPORT_OR_TYPE_OR_EOF);
            
            switch (token) {
            case IMPORT: {
                final int importStartContext = writeCurContext();
                final int importKeywordContext = writeCurContext();
                
                parseImport(importStartContext, getStringRef(), importKeywordContext);
                break;
            }
                
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
                modifierClassVisibilityKeywordContext = writeCurContext();
                break;
                
            case ABSTRACT:
                if (modifierSubclassingKeyword != StringRef.STRING_NONE) {
                    throw lexer.unexpectedToken();
                }
                
                subclassing = Subclassing.ABSTRACT;
                modifierSubclassingKeyword = getStringRef();
                modifierSubclassingKeywordContext = writeCurContext();
                break;
                
            case FINAL:
                if (modifierSubclassingKeyword != StringRef.STRING_NONE) {
                    throw lexer.unexpectedToken();
                }
                
                subclassing = Subclassing.FINAL;
                modifierSubclassingKeyword = getStringRef();
                modifierSubclassingKeywordContext = writeCurContext();
                break;
                
            case CLASS:
                final int classStartContext = writeCurContext();
                final int classKeywordContext = writeCurContext();
                
                parseClass(
                        classStartContext,
                        getStringRef(),
                        classKeywordContext,
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
            listener.onImportIdentifier(importIdentifierContext, getStringRef());
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
                
                listener.onImportIdentifier(anotherImportIdentifierContext, getStringRef());
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
            int classKeywordContext,
            long modifierClassVisibilityKeyword,
            ClassVisibility classVisibility,
            int modifierClassVisibilityKeywordContext,
            long modifierSubclassingKeyword,
            Subclassing subclassing,
            int modifierSubclassingKeywordContext) throws IOException, ParserException {
        
        final JavaToken classNameToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
        if (classNameToken != JavaToken.IDENTIFIER) {
            throw lexer.unexpectedToken();
        }
        
        final long className = getStringRef();
        final int classNameContext = writeCurContext();

        // Initial context of class is either class visibility or subclassing or class keyword
        listener.onClassStart(classStartContext, classKeyword, classKeywordContext, className, classNameContext);
        
        if (modifierClassVisibilityKeyword != StringRef.STRING_NONE) {
            listener.onVisibilityClassModifier(modifierClassVisibilityKeywordContext, classVisibility);
        }
        
        if (modifierSubclassingKeyword != StringRef.STRING_NONE) {
            listener.onSubclassingModifier(modifierSubclassingKeywordContext, subclassing);
        }

        parseClassGenericsOrExtendsOrImplementsOrBody();
        
        listener.onClassEnd(classStartContext, getLexerContext());
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
        case CHAR: {
            parseRestOfTypeAndFieldScalar(writeCurContext(), getStringRef());
            break;
        }
        
        case IDENTIFIER:
            // Probably a type, is it a scoped type?
            final long typeName = lexer.getStringRef();
            
            parseRestOfTypeAndFieldScopedType(writeCurContext(), typeName);
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

    private void parseRestOfTypeAndFieldScalar(int typeNameContext, long typeName) throws IOException, ParserException {
        
        parseRestOfMember(typeNameContext, typeName, null, ReferenceType.SCALAR);
    }

    private void parseRestOfTypeAndFieldScopedType(int typeNameContext, long typeName) throws IOException, ParserException {
        
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
            int typeNameContext,
            long typeName,
            List<ScopedNamePart> scopedTypeName,
            ReferenceType referenceType) throws ParserException, IOException {

        // Next should be the name of the field or member
        final JavaToken fieldNameToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
    
        if (fieldNameToken != JavaToken.IDENTIFIER) {
            throw lexer.unexpectedToken();
        }
        
        final int identifierContext = writeCurContext();
        final long identifier = getStringRef();
        
        final int fieldDeclarationStartContext = writeCurContext();
        
        // Next should be start of method, semicolon after type, array indicator or comma separated variables
        final JavaToken afterFieldToken = lexer.lexSkipWS(AFTER_FIELD_NAME);
        
        switch (afterFieldToken) {
        case SEMI: {
            // This is a field with a single variable name, like 'int a;'
            listener.onFieldDeclarationStart(fieldDeclarationStartContext);
            
            onType(fieldDeclarationStartContext, typeName, scopedTypeName, referenceType);
            
            final int variableDeclaratorStartContext = writeCurContext();

            listener.onVariableDeclaratorStart(variableDeclaratorStartContext);
            
            listener.onVariableName(identifierContext, identifier, 0);
            
            listener.onVariableDeclaratorEnd(variableDeclaratorStartContext, getLexerContext());
            
            listener.onFieldDeclarationEnd(fieldDeclarationStartContext, getLexerContext());
            break;
        }
            
        case COMMA: {
            // This is a field with multiple variable names, like 'int a, b, c;'
            listener.onFieldDeclarationStart(fieldDeclarationStartContext);
            
            onType(fieldDeclarationStartContext, typeName, scopedTypeName, referenceType);

            // Initial variable name
            final int variableDeclaratorStartContext = writeCurContext();

            listener.onVariableDeclaratorStart(variableDeclaratorStartContext);
            listener.onVariableName(fieldDeclarationStartContext, identifier, 0);
            listener.onVariableDeclaratorEnd(fieldDeclarationStartContext, getLexerContext());

            parseVariableDeclaratorList(typeNameContext);
            
            listener.onFieldDeclarationEnd(variableDeclaratorStartContext, getLexerContext());
            break;
        }
         
        case LPAREN: {
            final int classMethodStartContext = writeContext(typeNameContext);
            final int methodReturnTypeStartContext = writeContext(typeNameContext);
            final int methodParametersStartContext = writeContext(typeNameContext);
            
            listener.onClassMethodStart(classMethodStartContext);
            
            listener.onMethodReturnTypeStart(methodReturnTypeStartContext);
            
            onType(typeNameContext, typeName, scopedTypeName, referenceType);
            
            listener.onMethodReturnTypeEnd(methodReturnTypeStartContext, getLexerContext());
            
            listener.onMethodName(identifierContext, identifier);

            parseParametersAndMethod(classMethodStartContext, methodParametersStartContext);
            break;
        }
            
        default:
            throw lexer.unexpectedToken();
        }
    }
    
    private void onType(int typeNameContext, long typeName, List<ScopedNamePart> scopedTypeName, ReferenceType referenceType) {
        
        
        if (scopedTypeName != null) {
            listener.onScopedTypeReferenceStart(typeNameContext, referenceType);
            
            for (ScopedNamePart part : scopedTypeName) {
                listener.onScopedTypeReferencePart(part.getContext(), part.getPart());
            }
            
            listener.onScopedTypeReferenceEnd(typeNameContext, getLexerContext());
        }
        else if (typeName != StringRef.STRING_NONE) {
            listener.onNonScopedTypeReference(typeNameContext, typeName, referenceType);
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

    private void parseVariableDeclaratorList(int fieldContext) throws IOException, ParserException {

        boolean done = false;
        
        do {
            final JavaToken fieldNameToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
            
            if (fieldNameToken != JavaToken.IDENTIFIER) {
                throw lexer.unexpectedToken();
            }
            
            final long identifier = getStringRef();
            
            final int declaratorStartContext = writeCurContext();
            
            final JavaToken afterVarNameToken = lexer.lexSkipWS(AFTER_VARIABLE_NAME);

            switch (afterVarNameToken) {
            case COMMA:
            case SEMI:
                listener.onVariableDeclaratorStart(declaratorStartContext);
                
                listener.onVariableName(declaratorStartContext, identifier, 0);
                
                listener.onVariableDeclaratorEnd(declaratorStartContext, getLexerContext());
                
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

    private void parseParametersAndMethod(int methodStartContext, int methodParametersStartContext) throws IOException, ParserException {

        listener.onMethodSignatureParametersStart(methodParametersStartContext);
        
        final JavaToken initialToken = lexer.lexSkipWS(PARAM_TYPE_RPAREN);
        
        switch (initialToken) {
        
        case RPAREN:
            listener.onMethodSignatureParametersEnd(methodParametersStartContext, getLexerContext());
            
            parseMethodBodyOrSemicolon(methodParametersStartContext);
            break;

        default:
            parseParameters();
            
            final JavaToken token = lexer.lexSkipWS(PARAM_TYPE_RPAREN);
            if (token != JavaToken.RPAREN) {
                throw lexer.unexpectedToken();
            }
            
            parseMethodBodyOrSemicolon(methodStartContext);
            break;
        }
        
        listener.onMethodSignatureParametersEnd(methodParametersStartContext, getLexerContext());
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
            listener.onNonScopedTypeReference(writeCurContext(), getStringRef(), ReferenceType.SCALAR);
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
    
    private void parseParameters() throws IOException, ParserException {
        
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
    
    private void parseMethodBodyOrSemicolon(int methodStartContext) throws ParserException, IOException {
        
        final JavaToken token = lexer.lexSkipWS(BODY_OR_SEMI_COLON);
        
        switch (token) {

        case LBRACE:
            parseMethodBodyAndRBrace();
            
            listener.onClassMethodEnd(methodStartContext, getLexerContext());
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
    
    private void parseIfElseIfElse(long ifKeyword, int ifKeywordContext) throws IOException, ParserException {

        final int ifStartContext = writeContext(ifKeywordContext);
        final int ifStartConditionBlockContext = writeContext(ifKeywordContext);
        
        listener.onIfStatementStart(ifStartContext, ifKeyword, ifKeywordContext);
        
        listener.onIfStatementInitialBlockStart(ifStartConditionBlockContext);
        
        parseConditionInParenthesis();

        parseStatementOrBlock();

        listener.onIfStatementInitialBlockEnd(ifStartContext, getLexerContext());
        
        final JavaToken elseIfOrElseToken = lexer.lexSkipWS(ELSE_IF_OR_ELSE);
        switch (elseIfOrElseToken) {
        case ELSE:
            final int elseStatementStartContext = writeCurContext();
            final int elseKeywordContext = writeCurContext();
            
            final long elseKeyword = getStringRef();
            
            listener.onElseStatementStart(elseStatementStartContext, elseKeyword, elseKeywordContext);
            
            parseStatementOrBlock();
            
            listener.onElseStatementEnd(elseStatementStartContext, getLexerContext());
            
            listener.onEndIfStatement(ifStartContext, getLexerContext());
            break;
            
        default:
            listener.onEndIfStatement(ifStartContext, getLexerContext());
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
                    callListenerAndParseExpression(writeCurContext(), Relational.EQUALS);
                    break;
                    
                case NOT_EQUALS:
                    callListenerAndParseExpression(writeCurContext(), Relational.NOT_EQUALS);
                    break;
                    
                case LT:
                    callListenerAndParseExpression(writeCurContext(), Relational.LESS_THAN);
                    break;
                    
                case GT:
                    callListenerAndParseExpression(writeCurContext(), Relational.GREATER_THAN);
                    break;
    
                case LTE:
                    callListenerAndParseExpression(writeCurContext(), Relational.LESS_THAN_OR_EQUALS);
                    break;
                    
                case GTE:
                    callListenerAndParseExpression(writeCurContext(), Relational.GREATER_THAN_OR_EQUALS);
                    break;
                    
                default:
                    done = true;
                    break;
                }
            }
        } while (!done);
    }
    
    private void callListenerAndParseExpression(int context, Operator operator) throws IOException, ParserException {
        
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
            parseVariableReferenceExpression(writeCurContext(), getStringRef());
            break;

        case NUMBER:
            parseNumericLiteral(writeCurContext(), getStringRef());
            break;

        default:
            expressionFound = false;
            break;
        }
        
        return expressionFound;
    }
    
    private void parseVariableReferenceExpression(int context, long stringRef) {
        
        // For now just say this is a variable
        listener.onVariableReference(context, stringRef);
        
    }
    
    private void parseNumericLiteral(int context, long stringRef) {

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
            final int typeNameContext = writeCurContext();
            final long typeName = getStringRef();
            
            parseNonScopedTypeVariableDeclarationStatement(typeNameContext, typeName, ReferenceType.SCALAR);
            break;
        }
        
        case IDENTIFIER: {
            final int identifierContext = writeCurContext();
            
            final int statementContext = writeContext(identifierContext);
            
            // Can be method invocation, for now parse as variable name
            listener.onVariableDeclarationStatementStart(statementContext);

            parseUserType(identifierContext);
            
            parseVariableDeclaratorList(writeCurContext());
            
            listener.onVariableDeclarationStatementEnd(statementContext, getLexerContext());
            break;
        }
        
        case IF: {
            final int ifKeywordContext = writeCurContext();
            parseIfElseIfElse(getStringRef(), ifKeywordContext);
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
    
    private void parseNonScopedTypeVariableDeclarationStatement(
            int typeContext,
            long typeName,
            ReferenceType referenceType) throws ParserException, IOException {

        final int statementStartContext = writeContext(typeContext);
        
        listener.onVariableDeclarationStatementStart(statementStartContext);
        
        listener.onNonScopedTypeReference(typeContext, typeName, referenceType);

        parseVariableDeclaratorList(typeContext);
        
        listener.onVariableDeclarationStatementEnd(statementStartContext, getLexerContext());
    }
    
    private void parseUserType(int initialPartContext) throws ParserException, IOException {
        
        final int typeStartContext = writeContext(initialPartContext);

        final long stringRef = getStringRef();

        JavaToken scopeToken = lexer.lexSkipWS(JavaToken.PERIOD);
        if (scopeToken == JavaToken.PERIOD) {

            listener.onScopedTypeReferenceStart(typeStartContext, ReferenceType.REFERENCE);
            
            listener.onScopedTypeReferencePart(initialPartContext, stringRef);

            for (;;) {

                final JavaToken partToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
                
                if (partToken != JavaToken.IDENTIFIER) {
                    throw lexer.unexpectedToken();
                }
                
                listener.onScopedTypeReferencePart(writeCurContext(), getStringRef());
                
                final JavaToken endOfScopeToken = lexer.lexSkipWS(JavaToken.PERIOD);
                
                if (endOfScopeToken != JavaToken.PERIOD) {
                    break;
                }
            }

            listener.onScopedTypeReferenceEnd(typeStartContext, getLexerContext());
        }
        else {
            listener.onNonScopedTypeReference(initialPartContext, stringRef, ReferenceType.REFERENCE);
        }
    }

    private List<ScopedNamePart> parseRestOfScopedName(int identifierContext, long identifier) throws IOException, ParserException {
        
        final List<ScopedNamePart> scopedTypeName = new ArrayList<>();

        scopedTypeName.add(new ScopedNamePart(identifierContext, identifier));

        for (;;) {

            final JavaToken partToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
            
            if (partToken != JavaToken.IDENTIFIER) {
                throw lexer.unexpectedToken();
            }
            
            scopedTypeName.add(new ScopedNamePart(writeCurContext(), getStringRef()));
            
            final JavaToken endOfScopeToken = lexer.lexSkipWS(JavaToken.PERIOD);
            
            if (endOfScopeToken != JavaToken.PERIOD) {
                break;
            }
        }
        
        return scopedTypeName;
    }
}
