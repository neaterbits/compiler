package com.neaterbits.compiler.model.encoded;

import java.util.Objects;

import com.neaterbits.util.buffers.MapStringStorageBuffer;
import com.neaterbits.util.io.strings.Tokenizer;

public final class TestTokenizer implements Tokenizer {

    private final String [] strings;

    private int index;

    TestTokenizer() {

        this.strings = new String[1000];
        this.index = 0;
    }

    long addString(String string) {

        Objects.requireNonNull(string);

        final int stringRef = index;

        strings[index ++] = string;

        return stringRef;
    }


    @Override
    public int addToBuffer(MapStringStorageBuffer buffer, long stringRef) {

        return buffer.add(asString(stringRef));
    }

    @Override
    public String asString(long stringRef) {

        return strings[(int)stringRef];
    }

    @Override
    public String asString(long startStringRef, long endStringRef) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String asStringFromOffset(int startOffset, int endOffset) {
        throw new UnsupportedOperationException();
    }
}
