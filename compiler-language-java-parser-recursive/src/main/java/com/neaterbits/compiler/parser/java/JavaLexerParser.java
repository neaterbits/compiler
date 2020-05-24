package com.neaterbits.compiler.parser.java;

import java.io.IOException;
import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ImmutableContext;
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
    interface ScopedNamePart {
        
        void onPart(Context context, long identifier);
    }
    
    private void parseScopedName(ScopedNamePart processPart) throws IOException, ParserException {

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
        
        if (lexer.lexSkipWS(JavaToken.RBRACE) != JavaToken.RBRACE) {
            throw lexer.unexpectedToken();
        }
    }
}
