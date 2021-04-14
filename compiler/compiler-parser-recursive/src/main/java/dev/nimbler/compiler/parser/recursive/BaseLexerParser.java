package dev.nimbler.compiler.parser.recursive;

import java.util.Objects;

import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.IToken;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.context.Context;
import com.neaterbits.util.parse.context.MutableContext;

import dev.nimbler.compiler.parser.recursive.cached.ScratchBuf;
import dev.nimbler.compiler.parser.recursive.cached.annotations.CachedAnnotation;
import dev.nimbler.compiler.parser.recursive.cached.annotations.CachedAnnotations;
import dev.nimbler.compiler.parser.recursive.cached.annotations.CachedAnnotationsImpl;
import dev.nimbler.compiler.parser.recursive.cached.annotations.CachedAnnotationsList;
import dev.nimbler.compiler.parser.recursive.cached.annotations.elements.CachedAnnotationElement;
import dev.nimbler.compiler.parser.recursive.cached.annotations.elements.CachedAnnotationElements;
import dev.nimbler.compiler.parser.recursive.cached.annotations.elements.CachedAnnotationElementsImpl;
import dev.nimbler.compiler.parser.recursive.cached.annotations.elements.CachedAnnotationElementsList;
import dev.nimbler.compiler.parser.recursive.cached.expressions.ContextWriter;
import dev.nimbler.compiler.parser.recursive.cached.expressions.ExpressionCache;
import dev.nimbler.compiler.parser.recursive.cached.expressions.LanguageOperatorPrecedence;
import dev.nimbler.compiler.parser.recursive.cached.keywords.CachedKeyword;
import dev.nimbler.compiler.parser.recursive.cached.keywords.CachedKeywords;
import dev.nimbler.compiler.parser.recursive.cached.keywords.CachedKeywordsImpl;
import dev.nimbler.compiler.parser.recursive.cached.keywords.CachedKeywordsList;
import dev.nimbler.compiler.parser.recursive.cached.names.NamesImpl;
import dev.nimbler.compiler.parser.recursive.cached.names.NamesList;
import dev.nimbler.compiler.parser.recursive.cached.types.TypeArgumentImpl;
import dev.nimbler.compiler.parser.recursive.cached.types.TypeArguments;
import dev.nimbler.compiler.parser.recursive.cached.types.TypeArgumentsImpl;
import dev.nimbler.compiler.parser.recursive.cached.types.TypeArgumentsList;
import dev.nimbler.compiler.util.name.Names;
import dev.nimbler.compiler.util.parse.NamePart;

public abstract class BaseLexerParser<TOKEN extends Enum<TOKEN> & IToken> {

    protected final Lexer<TOKEN, CharInput> lexer;
    protected final Tokenizer tokenizer;

    private final LexerContext context;
    
    protected final ContextWriter contextWriter;
    protected final LanguageOperatorPrecedence languageOperatorPrecedence;
    
    protected final ExpressionCache baseClassExpressionCache;

    private final MutableContext [] scratchContexts;
    private final boolean [] scratchContextInUse;

    private final ScratchBuf<NamePart, Names, NamesList, NamesImpl> scratchNames;
    private final ScratchBuf<TypeArgumentImpl, TypeArguments, TypeArgumentsList, TypeArgumentsImpl> scratchTypeArguments;
    private final ScratchBuf<
        CachedKeyword<TOKEN>,
        CachedKeywords<TOKEN>,
        CachedKeywordsList<TOKEN>,
        CachedKeywordsImpl<TOKEN>>
            scratchKeywords;

    private final ScratchBuf<
        CachedAnnotation,
        CachedAnnotations,
        CachedAnnotationsList,
        CachedAnnotationsImpl> scratchAnnotations;

    private final ScratchBuf<
        CachedAnnotationElement,
        CachedAnnotationElements,
        CachedAnnotationElementsList,
        CachedAnnotationElementsImpl> scratchAnnotationElements;

    public BaseLexerParser(
            String file,
            Lexer<TOKEN, CharInput> lexer,
            Tokenizer tokenizer,
            ContextWriter contextWriter,
            LanguageOperatorPrecedence languageOperatorPrecedence) {
        
        Objects.requireNonNull(lexer);
        Objects.requireNonNull(tokenizer);

        this.lexer = lexer;
        this.tokenizer = tokenizer;
        this.contextWriter = contextWriter;
        this.languageOperatorPrecedence = languageOperatorPrecedence;
        this.context = new LexerContext(file, lexer, tokenizer);
        
        this.baseClassExpressionCache = new ExpressionCache(contextWriter, languageOperatorPrecedence);
        
        final int numScratchContexts = 10;
        
        this.scratchContexts = new MutableContext[numScratchContexts];
        
        for (int i = 0; i < numScratchContexts; ++ i) {
            scratchContexts[i] = new MutableContext();
        }
        
        this.scratchContextInUse = new boolean[numScratchContexts];

        this.scratchNames = new ScratchBuf<>(NamesImpl::new);
        this.scratchTypeArguments = new ScratchBuf<>(TypeArgumentsImpl::new);
        this.scratchKeywords = new ScratchBuf<>(CachedKeywordsImpl::new);
        this.scratchAnnotations = new ScratchBuf<>(CachedAnnotationsImpl::new);
        this.scratchAnnotationElements = new ScratchBuf<>(CachedAnnotationElementsImpl::new);
    }

    protected final Context getLexerContext() {
        return context;
    }

    protected final Context initScratchContext() {

        MutableContext context = null;
        
        for (int i = 0; i < scratchContextInUse.length; ++ i) {
            if (!scratchContextInUse[i]) {
                scratchContextInUse[i] = true;
            
                context = scratchContexts[i];
                break;
            }
        }

        context.init(this.context);
        
        return context;
    }
    
    protected final void freeScratchContext(Context context) {
        
        Objects.requireNonNull(context);

        boolean found = false;
        
        for (int i = 0; i < scratchContexts.length; ++ i) {
            
            if (context == scratchContexts[i]) {
                scratchContextInUse[i] = false;

                found = true;
                break;
            }
        }
        
        if (!found) {
            throw new IllegalStateException();
        }
    }

    protected final NamesList startScratchNameParts() {
        
        return scratchNames.startScratchParts();
    }

    protected final TypeArgumentsList startScratchTypeArguments() {
        
        return scratchTypeArguments.startScratchParts();
    }

    protected final CachedKeywordsList<TOKEN> startScratchKeywords() {
        
        return scratchKeywords.startScratchParts();
    }

    protected final CachedAnnotationsList startScratchAnnotations() {
        
        return scratchAnnotations.startScratchParts();
    }

    protected final CachedAnnotationElementsList startScratchAnnotationElements() {
        
        return scratchAnnotationElements.startScratchParts();
    }
}
