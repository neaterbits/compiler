package com.neaterbits.compiler.ast.encoded;

import com.neaterbits.compiler.types.ParseTreeElement;

public interface ASTBufferRead {

    boolean getBoolean(int index);

    byte getByte(int index);

    short getShort(int index);

    int getInt(int index);

    long getLong(int index);

    boolean hasStringRef(int index);

    int getStringRef(int index);

    boolean hasContextRef(int index);

    int getContextRef(int index);

    <T extends Enum<T>> T getEnumByte(int index, Class<T> type);

    ParseTreeElement getParseTreeElement(int index);

    public static class ParseTreeElementRef {
        public ParseTreeElement element;
        public boolean isStart;
        public int index;
    }

    void getParseTreeElement(int index, ParseTreeElementRef ref);

    int getReplacementIndex(int index);

    int getOriginalSize(int index);
}
