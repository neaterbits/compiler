package dev.nimbler.compiler.parser.recursive;

import java.util.Objects;

import org.jutils.io.strings.Tokenizer;
import org.jutils.parse.Lexer;
import org.jutils.parse.context.Context;
import org.jutils.parse.context.FullContext;
import org.jutils.parse.context.ImmutableFullContext;

final class LexerContext implements FullContext {

    private final String file;
    private final Lexer<?, ?> lexer;
    private final Tokenizer tokenizer;

    LexerContext(String file, Lexer<?, ?> lexer, Tokenizer tokenizer) {

        Objects.requireNonNull(file);
        Objects.requireNonNull(lexer);
        Objects.requireNonNull(tokenizer);

        this.file = file;
        this.lexer = lexer;
        this.tokenizer = tokenizer;
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
        return lexer.getTokenStartOffset();
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
        return lexer.getTokenStartOffset() + lexer.getTokenLength();
    }

    @Override
    public int getLength() {
        return lexer.getTokenLength();
    }

    @Override
    public String getText() {
        return tokenizer.asString(lexer.getStringRef());
    }

    @Override
    public Context makeImmutable() {
        return new ImmutableFullContext(this);
    }

    @Override
    public String toString() {
        return "LexerContext [getStartOffset()=" + getStartOffset() + ", getEndOffset()=" + getEndOffset() + "]";
    }
}
