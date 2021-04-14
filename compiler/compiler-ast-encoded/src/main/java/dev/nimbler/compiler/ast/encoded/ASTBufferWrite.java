package dev.nimbler.compiler.ast.encoded;

import dev.nimbler.compiler.types.ParseTreeElement;

public interface ASTBufferWrite {

    void writeBoolean(boolean value);

    void writeByte(byte value);

    void writeShort(short value);

    void writeInt(int value);

    void writeLong(long value);

    void writeElementStart(ParseTreeElement element);

    void writeElementEnd(ParseTreeElement element);

    void writeLeafElement(ParseTreeElement element);

    void replaceElement(int index, int replacementIndex, int continueIndex);
}
