package com.neaterbits.compiler.parser.recursive;

import java.io.IOException;
import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.name.Names;
import com.neaterbits.compiler.util.parse.NamePart;
import com.neaterbits.util.io.strings.CharInput;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.IToken;
import com.neaterbits.util.parse.Lexer;
import com.neaterbits.util.parse.ParserException;

public abstract class BaseLexerParser<TOKEN extends Enum<TOKEN> & IToken> {

    protected final Lexer<TOKEN, CharInput> lexer;
    protected final Tokenizer tokenizer;

    private final LexerContext context;

    private final ScratchBuf<NamePart, Names, NamesImpl> scratchNames;
    
    public BaseLexerParser(String file, Lexer<TOKEN, CharInput> lexer, Tokenizer tokenizer) {
        
        Objects.requireNonNull(lexer);
        Objects.requireNonNull(tokenizer);

        this.lexer = lexer;
        this.tokenizer = tokenizer;
        this.context = new LexerContext(file, lexer, tokenizer);

        this.scratchNames = new ScratchBuf<>(NamesImpl::new);
    }

    protected final Context getLexerContext() {
        return context;
    }

    protected final int startScratchNameParts() {
        
        return scratchNames.startScratchParts();
    }

    protected final void addScratchNamePart(int context, long name, int index) {
        
        scratchNames.addScratchPart(
                index,
                () -> new NamePart(context, name),
                part -> part.init(context, name));
    }
    
    @FunctionalInterface
    protected interface ProcessNameParts {
        
        void processParts(Names names) throws IOException, ParserException;
    }
    
    protected final void completeScratchNameParts(int index, ProcessNameParts process) throws IOException, ParserException {
        
        scratchNames.completeScratchParts(index, names -> process.processParts(names));
    }
}
