package com.neaterbits.compiler.parser.recursive;

import java.util.Objects;

import com.neaterbits.compiler.parser.recursive.cached.ScratchBuf;
import com.neaterbits.compiler.parser.recursive.cached.expressions.ContextWriter;
import com.neaterbits.compiler.parser.recursive.cached.expressions.ExpressionCache;
import com.neaterbits.compiler.parser.recursive.cached.expressions.LanguageOperatorPrecedence;
import com.neaterbits.compiler.parser.recursive.cached.keywords.CachedKeyword;
import com.neaterbits.compiler.parser.recursive.cached.keywords.CachedKeywords;
import com.neaterbits.compiler.parser.recursive.cached.keywords.CachedKeywordsImpl;
import com.neaterbits.compiler.parser.recursive.cached.keywords.CachedKeywordsList;
import com.neaterbits.compiler.parser.recursive.cached.names.NamesImpl;
import com.neaterbits.compiler.parser.recursive.cached.names.NamesList;
import com.neaterbits.compiler.parser.recursive.cached.types.TypeArgumentImpl;
import com.neaterbits.compiler.parser.recursive.cached.types.TypeArguments;
import com.neaterbits.compiler.parser.recursive.cached.types.TypeArgumentsImpl;
import com.neaterbits.compiler.parser.recursive.cached.types.TypeArgumentsList;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.MutableContext;
import com.neaterbits.compiler.util.name.Names;
import com.neaterbits.compiler.util.parse.NamePart;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.IToken;
import com.neaterbits.util.parse.Lexer;

public abstract class BaseLexerParser<TOKEN extends Enum<TOKEN> & IToken> {

    protected final Lexer<TOKEN, CharInput> lexer;
    protected final Tokenizer tokenizer;

    private final LexerContext context;
    
    protected final ExpressionCache expressionCache;

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
        this.context = new LexerContext(file, lexer, tokenizer);
        
        this.expressionCache = new ExpressionCache(contextWriter, languageOperatorPrecedence);
        
        final int numScratchContexts = 10;
        
        this.scratchContexts = new MutableContext[numScratchContexts];
        
        for (int i = 0; i < numScratchContexts; ++ i) {
            scratchContexts[i] = new MutableContext();
        }
        
        this.scratchContextInUse = new boolean[numScratchContexts];

        this.scratchNames = new ScratchBuf<>(NamesImpl::new);
        this.scratchTypeArguments = new ScratchBuf<>(TypeArgumentsImpl::new);
        this.scratchKeywords = new ScratchBuf<>(CachedKeywordsImpl::new);
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
}
