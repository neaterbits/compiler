package dev.nimbler.compiler.parser.java.recursive;

import java.io.IOException;

import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.StringRef;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

import dev.nimbler.compiler.parser.listener.common.IterativeParseTreeListener;
import dev.nimbler.compiler.types.typedefinition.ClassVisibility;
import dev.nimbler.compiler.types.typedefinition.Subclassing;
import dev.nimbler.compiler.util.ContextRef;

final class JavaLexerParser<COMPILATION_UNIT> extends JavaEnumLexerParser<COMPILATION_UNIT> {

    JavaLexerParser(
            String file,
            Lexer<JavaToken, CharInput> lexer,
            Tokenizer tokenizer,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        super(file, lexer, tokenizer, listener);
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
            JavaToken.ENUM,
            JavaToken.EOF
    };

    private static JavaToken [] TYPE_OR_EOF = new JavaToken [] {

            JavaToken.AT,
            JavaToken.PUBLIC,
            JavaToken.FINAL,
            JavaToken.ABSTRACT,
            
            JavaToken.CLASS,
            JavaToken.ENUM,
            JavaToken.EOF
    };
    
    private COMPILATION_UNIT parseCompilationUnit() throws IOException, ParserException {

        final int compilationUnitStartContext = writeCurContext();
        
        listener.onCompilationUnitStart(compilationUnitStartContext);

        JavaToken token = lexer.lexSkipWS(JavaToken.PACKAGE);

        final int packageContext;
        
        if (token != JavaToken.NONE) {
            
            packageContext = writeCurContext();
            
            final int packageKeywordContext = writeCurContext();

            parsePackageNameAndSemiColon(packageContext, getStringRef(), packageKeywordContext);
        }
        else {
            packageContext = ContextRef.NONE;
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
            case ENUM:
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
                
            case ENUM:
                final int enumStartContext = writeCurContext();
                final int enumKeywordContext = writeCurContext();
                
                parseEnum(enumStartContext, getStringRef(), enumKeywordContext);

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

        if (packageContext != ContextRef.NONE) {
            listener.onNameSpaceEnd(packageContext, getLexerContext());
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
}
