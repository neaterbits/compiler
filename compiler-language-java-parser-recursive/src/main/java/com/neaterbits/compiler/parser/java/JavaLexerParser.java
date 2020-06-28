package com.neaterbits.compiler.parser.java;

import java.io.IOException;
import java.util.Objects;

import com.neaterbits.compiler.util.Base;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ContextImpl;
import com.neaterbits.compiler.util.ContextRef;
import com.neaterbits.compiler.util.method.MethodInvocationType;
import com.neaterbits.compiler.util.model.ReferenceType;
import com.neaterbits.compiler.util.name.Names;
import com.neaterbits.compiler.util.operator.Arithmetic;
import com.neaterbits.compiler.util.operator.Operator;
import com.neaterbits.compiler.util.operator.Relational;
import com.neaterbits.compiler.util.parse.FieldAccessType;
import com.neaterbits.compiler.util.typedefinition.ClassVisibility;
import com.neaterbits.compiler.util.typedefinition.Subclassing;
import com.neaterbits.compiler.util.typedefinition.TypeBoundType;
import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.parser.recursive.BaseLexerParser;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.StringRef;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

final class JavaLexerParser<COMPILATION_UNIT> extends BaseLexerParser<JavaToken> {

    private final IterativeParserListener<COMPILATION_UNIT> listener;

    JavaLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParserListener<COMPILATION_UNIT> listener) {

        super(file, lexer, tokenizer);

        Objects.requireNonNull(listener);
        
        this.listener = listener;
    }

    COMPILATION_UNIT parse() throws IOException, ParserException {
        
        return parseCompilationUnit();
    }
    
    private long getStringRef() {
        return lexer.getStringRef(0, 0);
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
    
    private int writeCurContext() {
        
        return listener.writeContext(getLexerContext());
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
    
    private void parseAnnotation(int startContext) throws IOException, ParserException {
        
        parseNameListUntilOtherToken(names -> {

            listener.onAnnotationStart(startContext, names);
        
            parseAnyAnnotationElements();
            
            listener.onAnnotationEnd(startContext, getLexerContext());
        });
    }
    
    private void parseAnyAnnotationElements() throws IOException, ParserException {

        if (lexer.lexSkipWS(JavaToken.LPAREN) == JavaToken.LPAREN) {
            
            // Is this @Annotation(identifier = value) or @Annotation(STATIC_CONSTANT)
            // or @Annotation(LITERAL)
            
            if (lexer.lexSkipWS(JavaToken.IDENTIFIER) == JavaToken.IDENTIFIER) {
         
                final int identifierContext = writeCurContext();
                final int elementStartContext = writeContext(identifierContext);
                        
                // @Annotation(identifier = value) or @Annotation(STATIC_CONSTANT)

                final long stringRef = getStringRef();

                if (lexer.lexSkipWS(JavaToken.ASSIGN) == JavaToken.ASSIGN) {

                    // @Annotation(identifier = value)
                    parseExpressionOrAnnotationOrList(elementStartContext, stringRef, identifierContext);
                    
                    parsePerhapsMultipleElementValues();
                }
                else {
                    listener.onAnnotationElementStart(elementStartContext, StringRef.STRING_NONE, ContextRef.NONE);
                    
                    listener.onVariableReference(identifierContext, stringRef);

                    listener.onAnnotationElementEnd(elementStartContext, getLexerContext());
                }
            }
            else {
                parseExpressionOrAnnotationOrList(writeCurContext(), StringRef.STRING_NONE, ContextRef.NONE);
            }
            
            if (lexer.lexSkipWS(JavaToken.RPAREN) != JavaToken.RPAREN) {
                throw lexer.unexpectedToken();
            }
        }
    }
    
    private void parsePerhapsMultipleElementValues() throws IOException, ParserException {

        // List of more element values?
        for (;;) {
            
            if (lexer.lexSkipWS(JavaToken.COMMA) != JavaToken.COMMA) {
                break;
            }
            
            final JavaToken otherIdentifier = lexer.lexSkipWS(JavaToken.IDENTIFIER);
            
            if (otherIdentifier != JavaToken.IDENTIFIER) {
                throw lexer.unexpectedToken();
            }
            
            final int otherIdentifierContext = writeCurContext();
            final int otherElementStartContext = writeContext(otherIdentifierContext);
            
            final long otherIdentifierRef = getStringRef();
            
            if (lexer.lexSkipWS(JavaToken.ASSIGN) != JavaToken.ASSIGN) {
                throw lexer.unexpectedToken();
            }

            parseExpressionOrAnnotationOrList(otherElementStartContext, otherIdentifierRef, otherIdentifierContext);
        }
    }

    private static final JavaToken [] EXPRESSION_OR_ANNOTATION_OR_LIST_TOKENS = new JavaToken [] {
            JavaToken.AT,
            JavaToken.LBRACE
    };
    
    private static final JavaToken [] AFTER_ANNOTATION_ELEMENT_LIST_TOKENS = new JavaToken [] {
            JavaToken.COMMA,
            JavaToken.RBRACE
    };

    private void parseExpressionOrAnnotationOrList(
            int startContext,
            long identifier,
            int identifierContext) throws IOException, ParserException {

        final JavaToken token = lexer.lexSkipWS(EXPRESSION_OR_ANNOTATION_OR_LIST_TOKENS);
        
        listener.onAnnotationElementStart(startContext, identifier, identifierContext);

        if (token == JavaToken.AT) {
            
            // @TheAnnotation(@OtherAnnotation)
            
            parseAnnotation(writeCurContext());
        }
        else if (token == JavaToken.LBRACE) {
            
            for (;;) {

                final int listStartContext = writeCurContext();
                
                listener.onAnnotationElementStart(listStartContext, StringRef.STRING_NONE, ContextRef.NONE);
                
                if (lexer.lexSkipWS(JavaToken.AT) == JavaToken.AT) {
                    parseAnnotation(startContext);
                }
                else {
                    parseExpression();
                }
                
                listener.onAnnotationElementEnd(listStartContext, getLexerContext());
                
                final JavaToken listToken = lexer.lexSkipWS(AFTER_ANNOTATION_ELEMENT_LIST_TOKENS);
                
                if (listToken == JavaToken.COMMA) {
                    // Continue to iterate
                }
                else if (listToken == JavaToken.RBRACE) {
                    break;
                }
                else {
                    throw lexer.unexpectedToken();
                }
            }
        }
        else {
            parseExpression();
        }
        
        listener.onAnnotationElementEnd(startContext, getLexerContext());
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
        
        case LT:
            parseGenericTypeArguments();
            
            parseExtendsOrImplementsOrBody();
            break;
        
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
    
    private static final JavaToken [] GENERIC_TYPE_TOKENS = new JavaToken [] {
            JavaToken.IDENTIFIER,
            JavaToken.QUESTION_MARK
    };
    
    private static final JavaToken [] AFTER_TYPE_ARGUMENT_TOKENS = new JavaToken [] {
            JavaToken.COMMA,
            JavaToken.GT
    };
    
    private void parseGenericTypeArguments() throws IOException, ParserException {
        
        final int startContext = writeCurContext();
        
        listener.onGenericClassDefinitionTypeListStart(startContext);
        
        for (;;) {
            
            final JavaToken typeArgumentToken = lexer.lexSkipWS(GENERIC_TYPE_TOKENS);
            
            if (typeArgumentToken == JavaToken.IDENTIFIER) {
    
                final int nameContext = writeCurContext();
                final int namedTypeStartContext = writeContext(nameContext);
                
                listener.onGenericNamedTypeStart(namedTypeStartContext, getStringRef(), nameContext);
                
                if (lexer.lexSkipWS(JavaToken.EXTENDS) == JavaToken.EXTENDS) {
                    parseTypeBound(writeCurContext(), TypeBoundType.EXTENDS);
                }
                
                listener.onGenericNamedTypeEnd(namedTypeStartContext, getLexerContext());
            }
            else {
                throw lexer.unexpectedToken();
            }
            
            final JavaToken afterTypeArgument = lexer.lexSkipWS(AFTER_TYPE_ARGUMENT_TOKENS);
            
            if (afterTypeArgument == JavaToken.COMMA) {
                // Continue on next type
            }
            else if (afterTypeArgument == JavaToken.GT) {
                break;
            }
            else {
                throw lexer.unexpectedToken();
            }
        }
        
        listener.onGenericClassDefinitionTypeListEnd(startContext, getLexerContext());
    }
    
    private void parseTypeBound(int startContext, TypeBoundType type) throws IOException, ParserException {
        
        parseNameListUntilOtherToken(names -> {
        
            listener.onTypeBoundStart(startContext, type, names);
            
            listener.onTypeBoundEnd(startContext, getLexerContext());
        });
        
    }

    private static final JavaToken [] EXTENDS_OR_IMPLEMENTS_OR_BODY = new JavaToken [] {
            JavaToken.EXTENDS,
            JavaToken.IMPLEMENTS,
            JavaToken.LBRACE
    };

    private void parseExtendsOrImplementsOrBody() throws IOException, ParserException {
        
        final JavaToken afterClassName = lexer.lexSkipWS(EXTENDS_OR_IMPLEMENTS_OR_BODY);
        
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
        
        if (periodToken == JavaToken.PERIOD) {
            parseNameListUntilOtherToken(typeNameContext, typeName, names -> {

                parseRestOfMember(typeNameContext, typeName, names, ReferenceType.REFERENCE);
            });
        }
        else {
            parseRestOfMember(typeNameContext, typeName, null, ReferenceType.REFERENCE);
        }
    }
    
    private void parseRestOfMember(
            int typeNameContext,
            long typeName,
            Names names,
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
            
            onType(fieldDeclarationStartContext, typeName, names, referenceType);
            
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
            
            onType(fieldDeclarationStartContext, typeName, names, referenceType);

            // Initial variable name
            final int variableDeclaratorStartContext = writeCurContext();

            listener.onVariableDeclaratorStart(variableDeclaratorStartContext);
            listener.onVariableName(fieldDeclarationStartContext, identifier, 0);
            listener.onVariableDeclaratorEnd(fieldDeclarationStartContext, getLexerContext());

            parseVariableDeclaratorList();
            
            listener.onFieldDeclarationEnd(variableDeclaratorStartContext, getLexerContext());
            break;
        }
         
        case LPAREN: {
            final int classMethodStartContext = writeContext(typeNameContext);
            final int methodReturnTypeStartContext = writeContext(typeNameContext);
            final int methodParametersStartContext = writeContext(typeNameContext);
            
            listener.onClassMethodStart(classMethodStartContext);
            
            listener.onMethodReturnTypeStart(methodReturnTypeStartContext);
            
            onType(typeNameContext, typeName, names, referenceType);
            
            listener.onMethodReturnTypeEnd(methodReturnTypeStartContext, getLexerContext());
            
            listener.onMethodName(identifierContext, identifier);

            parseParametersAndMethod(classMethodStartContext, methodParametersStartContext);
            break;
        }
            
        default:
            throw lexer.unexpectedToken();
        }
    }
    
    private void onType(
            int typeNameContext,
            long typeName,
            Names scopedTypeName,
            ReferenceType referenceType) {
        
        
        if (scopedTypeName != null) {
            final int startContext = writeContext(scopedTypeName.getContextAt(0));
            listener.onScopedTypeReferenceStart(startContext, referenceType);
            
            for (int i = 0; i < scopedTypeName.count(); ++ i) {

                final int context = scopedTypeName.getContextAt(i);
                final long part = scopedTypeName.getStringAt(i);
                
                listener.onScopedTypeReferencePart(context, part);
            }
            
            listener.onScopedTypeReferenceEnd(startContext, getLexerContext());
        }
        else if (typeName != StringRef.STRING_NONE) {
            listener.onNonScopedTypeReference(typeNameContext, typeName, referenceType);
        }
        else {
            throw new IllegalStateException();
        }
    }
    
    private void onMethodInvocationPrimaryList(
            int context,
            long identifier,
            int identifierContext,
            MethodInvocationType methodInvocationType,
            Names names) throws IOException, ParserException {
        
        final int primaryListContext = writeContext(context);
        
        listener.onPrimaryStart(primaryListContext);
        
        final int methodInvocationContext = writeContext(context);
        
        listener.onMethodInvocationStart(
                methodInvocationContext,
                methodInvocationType,
                names,
                names != null ? names.getStringAt(names.count() - 1) : identifier,
                names != null ? names.getContextAt(names.count() - 1) : identifierContext);
        
        parseMethodInvocationParameters();
        
        listener.onMethodInvocationEnd(methodInvocationContext, names == null, getLexerContext());
        
        parseAnyAdditionalPrimaries();
        
        listener.onPrimaryEnd(primaryListContext, getLexerContext());
    }

    private static final JavaToken [] AFTER_VARIABLE_NAME = new JavaToken [] {
            
            JavaToken.SEMI,
            JavaToken.COMMA,
            JavaToken.ASSIGN,
            JavaToken.LBRACKET
    };
    
    private static final JavaToken [] AFTER_VARIABLE_INITIALIZER = new JavaToken [] {
            JavaToken.SEMI,
            JavaToken.COMMA
    };

    private void parseVariableDeclaratorList() throws IOException, ParserException {
        
        parseVariableDeclaratorList(StringRef.STRING_NONE, ContextRef.NONE);
    }

    private void parseVariableDeclaratorList(long initialVarName, int initialVarNameContext) throws IOException, ParserException {

        boolean done = false;
        
        boolean initialIteration = true;
        
        do {
            final long identifier;
            final int declaratorStartContext;
            
            if (initialIteration && initialVarName != StringRef.STRING_NONE) {
                identifier = initialVarName;
                declaratorStartContext = initialVarNameContext;
            }
            else {
                final JavaToken fieldNameToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
                
                if (fieldNameToken != JavaToken.IDENTIFIER) {
                    throw lexer.unexpectedToken();
                }

                identifier = getStringRef();
                declaratorStartContext = writeCurContext();
            }
            
            initialIteration = false;

            final int declaratorNameContext = writeContext(declaratorStartContext);
            
            final JavaToken afterVarNameToken = lexer.lexSkipWS(AFTER_VARIABLE_NAME);
            
            switch (afterVarNameToken) {
            case COMMA:
            case SEMI:
                listener.onVariableDeclaratorStart(declaratorStartContext);
                
                listener.onVariableName(declaratorStartContext, identifier, declaratorNameContext);
                
                listener.onVariableDeclaratorEnd(declaratorStartContext, getLexerContext());
                
                if (afterVarNameToken == JavaToken.SEMI) {
                    done = true;
                }
                break;

            case ASSIGN:
                listener.onVariableDeclaratorStart(declaratorStartContext);
                
                listener.onVariableName(declaratorStartContext, identifier, declaratorNameContext);
                
                parseExpressionList();

                listener.onVariableDeclaratorEnd(declaratorStartContext, getLexerContext());
                
                // Skip out if next is semicolon
                switch (lexer.lexSkipWS(AFTER_VARIABLE_INITIALIZER)) {
                case COMMA:
                    break;
                    
                case SEMI:
                    done = true;
                    break;
                    
                default:
                    throw lexer.unexpectedToken();
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
            parseMethodSignatureParameters();
            
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
            JavaToken.WHILE,

            JavaToken.IDENTIFIER,
            
            JavaToken.SEMI
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
    
    private void parseIfElseIfElse(long ifKeyword, int ifKeywordContext) throws IOException, ParserException {

        final int ifStartContext = writeContext(ifKeywordContext);
        final int ifStartConditionBlockContext = writeContext(ifKeywordContext);
        
        listener.onIfStatementStart(ifStartContext, ifKeyword, ifKeywordContext);
        
        listener.onIfStatementInitialBlockStart(ifStartConditionBlockContext);
        
        parseConditionInParenthesis();

        parseStatementOrBlock();

        listener.onIfStatementInitialBlockEnd(ifStartContext, getLexerContext());
        
        for (;;) {
            final JavaToken elseToken = lexer.lexSkipWS(JavaToken.ELSE);
            
            if (elseToken == JavaToken.ELSE) {
    
                final long elseKeyword = getStringRef();
    
                final int elseStatementStartContext = writeCurContext();
                final int elseKeywordContext = writeCurContext();
                
                // Is this an else-if?
                final JavaToken ifToken = lexer.lexSkipWS(JavaToken.IF);
                
                if (ifToken == JavaToken.IF) {
                    final int elseIfStatementStartContext = elseStatementStartContext;

                    final int elseIfKeywordContext = writeCurContext();
                    final long elseIfKeyword = getStringRef();
    
                    listener.onElseIfStatementStart(
                            elseIfStatementStartContext,
                            elseKeyword, elseKeywordContext,
                            elseIfKeyword, elseIfKeywordContext);
                    
                    parseConditionInParenthesis();
                 
                    parseStatementOrBlock();
                    
                    listener.onElseIfStatementEnd(elseIfStatementStartContext, getLexerContext());
                }
                else {
                    // This was an 'else', not an 'else if'
                    listener.onElseStatementStart(elseStatementStartContext, elseKeyword, elseKeywordContext);
                    
                    parseStatementOrBlock();
                    
                    listener.onElseStatementEnd(elseStatementStartContext, getLexerContext());
                    
                    listener.onEndIfStatement(ifStartContext, getLexerContext());
                    
                    break; // last statement in 'if - else if - else' so break out
                }
            }
            else {
                // No 'else' keyword
                listener.onEndIfStatement(ifStartContext, getLexerContext());
                
                break; // 'last statement in 'if - else' so break out
            }
        }
    }

    private void parseWhile(long whileKeyword, int whileKeywordContext) throws IOException, ParserException {
        
        final int whileStartContext = writeContext(whileKeywordContext);
        
        listener.onWhileStatementStart(whileStartContext, whileKeyword, whileKeywordContext);
        
        parseConditionInParenthesis();
        
        parseStatementOrBlock();
        
        listener.onWhileStatementEnd(whileStartContext, getLexerContext());
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
            JavaToken.GTE,
            
            JavaToken.PLUS,
            JavaToken.MINUS,
            JavaToken.MUL,
            JavaToken.DIV,
            JavaToken.MOD
            
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
                    
                case PLUS:
                    callListenerAndParseExpression(writeCurContext(), Arithmetic.PLUS);
                    break;
                    
                case MINUS:
                    callListenerAndParseExpression(writeCurContext(), Arithmetic.MINUS);
                    break;

                case MUL:
                    callListenerAndParseExpression(writeCurContext(), Arithmetic.MULTIPLY);
                    break;

                case DIV:
                    callListenerAndParseExpression(writeCurContext(), Arithmetic.DIVIDE);
                    break;

                case MOD:
                    callListenerAndParseExpression(writeCurContext(), Arithmetic.MODULUS);
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
    
    private static final JavaToken [] STATEMENT_STARTING_WITH_IDENTIFIER_TOKENS = new JavaToken [] {
            JavaToken.PERIOD, // User type for variable declaration or method invocation or field dereferencing
            JavaToken.ASSIGN, // Assignment
            JavaToken.IDENTIFIER, // eg. SomeType varName;
            JavaToken.LPAREN
    };
    
    
    private static final JavaToken [] AFTER_NAMELIST_TOKENS = new JavaToken[] {

            JavaToken.ASSIGN, // Assignment
            JavaToken.IDENTIFIER, // eg. SomeType varName;
            JavaToken.LPAREN
            
    };
    
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

            final long identifier = getStringRef();

            final JavaToken statementStartingWithIdentifierToken = lexer.lexSkipWS(STATEMENT_STARTING_WITH_IDENTIFIER_TOKENS);

            // TODO try to remove
            final Context afterIdentifierContext = new ContextImpl(getLexerContext());
            
            switch (statementStartingWithIdentifierToken) {
            case PERIOD:
                
                // Identifier, then '.' can be many things like
                //
                // SomeClass.staticMethod()
                // STATIC_MEMBER.staticMethod();
                // a.complete.packagagepath.SomeClass.staticMethod()
                // a.complete.Type varName
                // etc
                // So just parse all names and the look at what tokens shows up after that 
                
                parseNameListUntilOtherToken(
                        identifierContext,
                        identifier,
                        names -> parseNameListAfterStatementIdentifiers(
                                statementContext,
                                identifier,
                                identifierContext,
                                names));
                
                /*
                listener.onVariableDeclarationStatementStart(statementContext);
    
                parseUserTypeAfterPeriod(identifierContext, identifier);
                
                parseVariableDeclaratorList(writeCurContext());
                
                listener.onVariableDeclarationStatementEnd(statementContext, getLexerContext());
                */
                break;
                
            case ASSIGN: {
                // variable = value;
                listener.onAssignmentStatementStart(statementContext);
                
                final int expressionContext = writeContext(statementContext);
                
                listener.onEnterAssignmentExpression(expressionContext);

                final int lhsStartContext = writeContext(statementContext);
                listener.onEnterAssignmentLHS(lhsStartContext);
                
                final int leafContext = writeContext(lhsStartContext);
                listener.onVariableReference(leafContext, identifier);
                
                listener.onExitAssignmentLHS(lhsStartContext, afterIdentifierContext);
                
                final Context endContext = getLexerContext();
                
                parseExpressionList();
                
                listener.onExitAssignmentExpression(expressionContext, endContext);
                
                listener.onAssignmentStatementEnd(statementContext, endContext);
                break;
            }
            
            case IDENTIFIER: {
                final long varName = getStringRef();
                final int varNameContext = writeCurContext();

                listener.onVariableDeclarationStatementStart(statementContext);
                
                listener.onNonScopedTypeReference(identifierContext, identifier, ReferenceType.REFERENCE);

                parseVariableDeclaratorList(varName, varNameContext);
                
                listener.onVariableDeclarationStatementEnd(statementContext, getLexerContext());

                break;
            }
            
            case LPAREN: {
                // Method invocation
                listener.onExpressionStatementStart(statementContext);
                
                onMethodInvocationPrimaryList(
                        statementContext,
                        identifier,
                        identifierContext,
                        MethodInvocationType.NO_OBJECT,
                        null);
                
                listener.onExpressionStatementEnd(statementContext, getLexerContext());
                break;
            }
                
            default:
                throw lexer.unexpectedToken();
            }
            break;
        }
        
        case IF: {
            final int ifKeywordContext = writeCurContext();
            
            parseIfElseIfElse(getStringRef(), ifKeywordContext);
            break;
        }
        
        case WHILE: {
            final int whileKeywordContext = writeCurContext(); 
        
            parseWhile(getStringRef(), whileKeywordContext);
            break;
        }

        case SEMI:
            // Only ';'
            break;
            
        default:
            foundStatement = false;
            break;
        }

        return foundStatement;
    }

    private void parseNameListAfterStatementIdentifiers(
            int context,
            long identifier,
            int identifierContext,
            Names names) throws IOException, ParserException {
        
        if (names.count() <= 1) {
            throw new IllegalArgumentException();
        }

        // What is next token?
        final JavaToken afterNameListToken = lexer.lexSkipWS(AFTER_NAMELIST_TOKENS);

        switch (afterNameListToken) {
        case LPAREN:
            listener.onExpressionStatementStart(context);

            onMethodInvocationPrimaryList(
                    context,
                    identifier,
                    identifierContext,
                    MethodInvocationType.NAMED_CLASS_STATIC_OR_STATIC_VAR,
                    names);
            
            listener.onExpressionStatementEnd(context, getLexerContext());
            break;
            
        case IDENTIFIER:
            // some.scope.Type variableDeclaration
            listener.onVariableDeclarationStatementStart(context);
            
            onType(ContextRef.NONE, StringRef.STRING_NONE, names, ReferenceType.REFERENCE);

            parseVariableDeclaratorList(getStringRef(), writeCurContext());
            
            listener.onVariableDeclarationStatementEnd(context, getLexerContext());
            break;
            
        default:
            throw lexer.unexpectedToken();
        }
    }

    private void parseAnyAdditionalPrimaries() throws IOException, ParserException {

        for (;;) {
            final JavaToken periodToken = lexer.lexSkipWS(JavaToken.PERIOD);
            
            if (periodToken != JavaToken.PERIOD) {
                break;
            }

            parseAnAdditionalPrimary();
        }
    }
    
    private void parseAnAdditionalPrimary() throws IOException, ParserException {
        
        final JavaToken identifierToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);

        if (identifierToken !=  JavaToken.IDENTIFIER) {
            throw lexer.unexpectedToken();
        }
        
        final long identifier = getStringRef();
        final int identifierContext = writeCurContext();
        
        final JavaToken nextToken = lexer.lexSkipWS(JavaToken.LPAREN);
        
        if (nextToken == JavaToken.LPAREN) {
            // Method invocation
            final int methodInvocationContext = writeContext(identifierContext);
            
            listener.onMethodInvocationStart(
                    methodInvocationContext,
                    MethodInvocationType.PRIMARY,
                    null,
                    identifier,
                    identifierContext);
            
            parseMethodInvocationParameters();
            
            listener.onMethodInvocationEnd(methodInvocationContext, true, getLexerContext());
        }
        else {
            
            listener.onFieldAccess(
                    identifierContext,
                    FieldAccessType.FIELD,
                    null,
                    null,
                    identifier,
                    identifierContext);
        }
    }
    
    private static JavaToken [] AFTER_PARAMETER_TOKEN = new JavaToken [] {
      
            JavaToken.COMMA,
            JavaToken.RPAREN
            
    };
    
    private void parseMethodInvocationParameters() throws IOException, ParserException {
        
        final int startContext = writeCurContext();
        
        listener.onParametersStart(startContext);
        // See if there is an initial end of parameters
        final JavaToken endOfParameters = lexer.lexSkipWS(JavaToken.RPAREN);
        
        if (endOfParameters == JavaToken.RPAREN) {
            listener.onParametersEnd(startContext, getLexerContext());
        }
        else {
            for (;;) {
                
                final int paramContext = writeCurContext();
                
                listener.onParameterStart(paramContext);
                
                parseExpressionList();
                
                listener.onParameterEnd(paramContext, getLexerContext());

                final JavaToken paramToken = lexer.lexSkipWS(AFTER_PARAMETER_TOKEN);
                
                if (paramToken == JavaToken.RPAREN) {
                    listener.onParametersEnd(startContext, getLexerContext());
                    break;
                }
                else if (paramToken == JavaToken.COMMA) {
                    // Continue with next
                }
                else {
                    throw lexer.unexpectedToken();
                }
            }
        }
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

        parseVariableDeclaratorList();
        
        listener.onVariableDeclarationStatementEnd(statementStartContext, getLexerContext());
    }
    
    private void parseUserType(int initialPartContext) throws ParserException, IOException {
        
        final long stringRef = getStringRef();

        JavaToken scopeToken = lexer.lexSkipWS(JavaToken.PERIOD);
        
        parseUserType(initialPartContext, stringRef, scopeToken == JavaToken.PERIOD);
    }

    private void parseUserType(int initialPartContext, long stringRef, boolean gotPeriodToken) throws ParserException, IOException {
        
        if (gotPeriodToken) {
            parseUserTypeAfterPeriod(initialPartContext, stringRef);
        }
        else {
            listener.onNonScopedTypeReference(initialPartContext, stringRef, ReferenceType.REFERENCE);
        }
    }

    private void parseUserTypeAfterPeriod(int initialPartContext, long stringRef) throws IOException, ParserException {
        
        final int typeStartContext = writeContext(initialPartContext);

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

    private void parseNameListUntilOtherToken(
            int identifierContext,
            long identifier,
            ProcessNameParts processNameParts) throws IOException, ParserException {
        
        final int scratchIndex = startScratchNamePart();

        addScratchNamePart(identifierContext, identifier, scratchIndex);
        
        parseNames(scratchIndex, processNameParts);
    }

    private void parseNameListUntilOtherToken(ProcessNameParts processNameParts) throws IOException, ParserException {
        
        final int scratchIndex = startScratchNamePart();

        parseNames(scratchIndex, processNameParts);
    }

    private void parseNames(int scratchIndex, ProcessNameParts processNameParts) throws IOException, ParserException {
        
        for (;;) {

            final JavaToken partToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);
            
            if (partToken != JavaToken.IDENTIFIER) {
                throw lexer.unexpectedToken();
            }
            
            addScratchNamePart(writeCurContext(), getStringRef(), scratchIndex);
            
            final JavaToken endOfScopeToken = lexer.lexSkipWS(JavaToken.PERIOD);
            
            if (endOfScopeToken != JavaToken.PERIOD) {
                break;
            }
        }
        
        // reach non-namepart so process now
        scratchNameParts(scratchIndex, processNameParts);
    }
}
