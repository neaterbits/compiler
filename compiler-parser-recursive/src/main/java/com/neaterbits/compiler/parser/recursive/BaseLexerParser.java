package com.neaterbits.compiler.parser.recursive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    private final List<NamesImpl> namesScratch;
    private int namesScratchInUse; 
    
    private static class NamesImpl implements Names {
        
        private final List<NamePart> namePartList;
        private int namePartElements;
        private boolean inUse;
        
        NamesImpl() {
            // Allocate 100 elements, should always be enough or will cause
            // exception
            this.namePartList = new ArrayList<>(100);
            this.namePartElements = 0;
        }
        
        @Override
        public long getStringAt(int index) {
            return namePartList.get(index).getPart();
        }

        @Override
        public int getContextAt(int index) {
            return namePartList.get(index).getContext();
        }

        @Override
        public int count() {
            return namePartElements;
        }
    }
    
    public BaseLexerParser(String file, Lexer<TOKEN, CharInput> lexer, Tokenizer tokenizer) {
        
        Objects.requireNonNull(lexer);
        Objects.requireNonNull(tokenizer);

        this.lexer = lexer;
        this.tokenizer = tokenizer;
        this.context = new LexerContext(file, lexer, tokenizer);
        
        this.namesScratch = new ArrayList<>();
    }

    protected final Context getLexerContext() {
        return context;
    }

    protected final int startScratchNamePart() {

        final int index;
        
        if (namesScratchInUse == namesScratch.size()) {
            
            final NamesImpl names = new NamesImpl();
            names.inUse = true;
            
            namesScratch.add(names);
            index = namesScratchInUse;
        }
        else {
            int found = -1;
            
            for (int i = 0; i < namesScratch.size(); ++ i) {
                final NamesImpl names = namesScratch.get(i);
                if (!names.inUse) {
                    found = i;
                    names.inUse = true;
                    break;
                }
            }
            
            if (found == -1) {
                throw new IllegalStateException();
            }
            
            index = found;
        }
        
        if (namesScratch.get(index).namePartElements > 0) {
            throw new IllegalStateException();
        }
        
        ++ namesScratchInUse;
        
        return index;
    }

    protected final void addScratchNamePart(int context, long name, int index) {
        
        final NamesImpl names = namesScratch.get(index);
        
        if (!names.inUse) {
            throw new IllegalStateException();
        }
        
        if (names.namePartElements == names.namePartList.size()) {
            
            final NamePart namePart = new NamePart(context, name);

            names.namePartList.add(namePart);
        }
        else {
            names.namePartList.get(names.namePartElements).init(context, name);
        }

        ++ names.namePartElements;
    }
    
    @FunctionalInterface
    protected interface ProcessNameParts {
        
        void processParts(Names names) throws IOException, ParserException;
    }

    protected final void scratchNameParts(int index, ProcessNameParts process) throws IOException, ParserException {
        
        if (namesScratchInUse < 1) {
            throw new IllegalStateException();
        }
        
        if (index != namesScratchInUse - 1) {
            throw new IllegalArgumentException();
        }
        
        final NamesImpl names = namesScratch.get(index);

        try {
            process.processParts(names);
        }
        finally {
            names.namePartElements = 0;
            names.inUse = false;
            -- namesScratchInUse;
        }
    }
}
