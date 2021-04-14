package com.neaterbits.compiler.parser.java;

import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.util.parse.Lexer;

final class LexerContext implements Context {

    private final String file;
    private final Lexer<?, ?> lexer;

    LexerContext(String file, Lexer<?, ?> lexer) {

        Objects.requireNonNull(file);
        Objects.requireNonNull(lexer);

        this.file = file;
        this.lexer = lexer;
    }

    @Override
    public String getFile() {
        return file;
    }

    @Override
    public int getStartLine() {
        return lexer.getStartLineNo();
    }

    @Override
    public int getStartPosInLine() {
        return lexer.getStartPosInLine();
    }

    @Override
    public int getStartOffset() {
        return (int)lexer.getTokenStartPos();
    }

    @Override
    public int getEndLine() {
        return lexer.getEndLineNo();
    }

    @Override
    public int getEndPosInLine() {
        return lexer.getStartPosInLine() + lexer.getTokenLength();
    }

    @Override
    public int getEndOffset() {
        return (int)lexer.getTokenStartPos() + lexer.getTokenLength();
    }

    @Override
    public int getLength() {
        return lexer.getTokenLength();
    }

    @Override
    public String getText() {
        // FIXME Auto-generated method stub
        return null;
    }

    @Override
    public int getTokenSequenceNo() {
        // FIXME Auto-generated method stub
        return 0;
    }
    
}
