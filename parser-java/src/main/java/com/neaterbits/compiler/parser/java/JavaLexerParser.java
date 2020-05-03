package com . neaterbits.compiler.parser.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ImmutableContext;
import com.neaterbits.compiler.util.parse.listener.IterativeParserListener;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.StringRef;
import com.neaterbits.util.parse.Lexer;

final class JavaLexerParser<COMPILATION_UNIT> {

    private final Lexer<JavaToken, CharInput> lexer;
    private final IterativeParserListener<COMPILATION_UNIT> listener;

    private final Context context;
    
    JavaLexerParser(String file, Lexer<JavaToken, CharInput> lexer, IterativeParserListener<COMPILATION_UNIT> listener) {

        Objects.requireNonNull(lexer);
        Objects.requireNonNull(listener);
        
        this.lexer = lexer;
        this.listener = listener;
        
        this.context = new LexerContext(file, lexer);
    }

    void parse() throws IOException, ParseException {
        
        parseCompilationUnit();
    }
    
    private ImmutableContext getCurrentContext() {
        
        return new ImmutableContext(context);
    }
    
    private long getStringRef() {
        return lexer.getStringRef(0, 0);
    }
    
    private static JavaToken [] IMPORT_OR_CLASS_OR_EOF = new JavaToken [] {
            JavaToken.IMPORT,
            JavaToken.CLASS,
            JavaToken.EOF
    };

    private void parseCompilationUnit() throws IOException, ParseException {

        listener.onCompilationUnitStart(context);

        final JavaToken token = lexer.lexSkipWS(JavaToken.PACKAGE);

        if (token != JavaToken.NONE) {

            parsePackageNameAndSemiColon(getStringRef(), getCurrentContext());

        }
        
        // Either import or class
        boolean done = false;
        
        do {
            final JavaToken importOrClass = lexer.lexSkipWS(IMPORT_OR_CLASS_OR_EOF);
            
            switch (importOrClass) {
            case IMPORT:
                parseImport(getStringRef(), getCurrentContext());
                break;
                
            case CLASS:
                break;
                
            default:
                done = true;
                break;
            }
            
            
        } while(!done);
                

        listener.onCompilationUnitEnd(context);
    }
    
    private static final JavaToken [] PERIOD_OR_SEMI = new JavaToken[] {
            JavaToken.PERIOD,
            JavaToken.SEMI
    };
    
    private void parsePackageNameAndSemiColon(long namespaceKeyword, Context namespaceKeywordContext) throws IOException, ParseException {
        
        final List<String> parts = new ArrayList<>();
        
        for (;;) {
            
            final JavaToken identifierToken = lexer.lexSkipWS(JavaToken.IDENTIFIER);

            if (identifierToken == JavaToken.NONE) {
                break;
            }

            final JavaToken nextToken = lexer.lexSkipWS(PERIOD_OR_SEMI);
            
            if (nextToken == JavaToken.SEMI) {
                break;
            }
            else if (nextToken == JavaToken.NONE) {
                throw new ParseException("Neither period nor semicolon at end of package declaration");
            }
            else {
                if (nextToken != JavaToken.PERIOD) {
                    throw new IllegalStateException();
                }
            }
        }

        listener.onNamespaceStart(
                null,
                namespaceKeyword,
                namespaceKeywordContext,
                StringRef.STRING_NONE,
                null,
                parts.toArray(new String[parts.size()]));
    }
    
    private static final JavaToken [] IMPORT_STATIC_OR_IDENTIFIER = new JavaToken [] {
      
            JavaToken.STATIC,
            JavaToken.IDENTIFIER
            
    };

    private static JavaToken [] IMPORT_IDENTIFIER_OR_ASTERISK = new JavaToken[] {
      
            JavaToken.IDENTIFIER,
            JavaToken.ASTERISK
    };
    
    private void parseImport(long importKeyword, ImmutableContext importKeywordContext) throws IOException, ParseException {
        
        final JavaToken staticOrIdentifiertoken = lexer.lex(IMPORT_STATIC_OR_IDENTIFIER);
        
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
                    break;
                }
                else if (identifierToken == JavaToken.ASTERISK) {
                    ondemand = true;
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
                throw new ParseException("Neither period nor semicolon at end of package declaration");
            }
            else {
                if (nextToken != JavaToken.PERIOD) {
                    throw new IllegalStateException();
                }
            }
        }
        
        listener.onImportEnd(null, ondemand);
    }
    
}
