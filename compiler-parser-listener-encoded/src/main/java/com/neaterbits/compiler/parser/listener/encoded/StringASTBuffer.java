package com.neaterbits.compiler.parser.listener.encoded;

import java.util.Objects;

import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.buffers.MapStringStorageBuffer;
import com.neaterbits.util.io.strings.StringBufferAdder;

final class StringASTBuffer {

    private final StringBufferAdder stringBufferAdder;

    private final ASTBuffer astBuffer;

    private final MapStringStorageBuffer stringBuffer;

    public StringASTBuffer(StringBufferAdder stringBufferAdder) {

        Objects.requireNonNull(stringBufferAdder);

        this.stringBufferAdder = stringBufferAdder;

        this.astBuffer = new ASTBufferImpl();
        this.stringBuffer = new MapStringStorageBuffer();
    }

    private int getStringIndex(long stringRef) {

        // TODO fix allocation
        return stringBufferAdder.addToBuffer(stringBuffer, stringRef);
    }

    void writeStringRef(long stringRef) {

        astBuffer.writeInt(getStringIndex(stringRef));
    }

    void writeNoStringRef() {

        astBuffer.writeInt(-1);
    }

    void writeContextRef(int contextRef) {

        astBuffer.writeInt(contextRef);
    }

    void writeNoContextRef() {

        astBuffer.writeInt(-1);
    }

    int getParseTreeRef() {

        return astBuffer.getWritePos();
    }

    void writeElementStart(ParseTreeElement element) {

        astBuffer.writeElementStart(element);
    }

    void writeElementEnd(ParseTreeElement element) {

        astBuffer.writeElementEnd(element);
    }

    void writeLeafElement(ParseTreeElement element) {

        astBuffer.writeLeafElement(element);
    }

    void writeByte(byte value) {
        astBuffer.writeByte(value);
    }

    void writeShort(short value) {
        astBuffer.writeShort(value);
    }

    void writeInt(int value) {
        astBuffer.writeInt(value);
    }

    void writeLong(long value) {
        astBuffer.writeLong(value);
    }

    void writeBoolean(boolean value) {
        astBuffer.writeBoolean(value);
    }

    <T extends Enum<T>> void writeEnumByte(Enum<T> enumValue) {

        final int ordinal = enumValue.ordinal();

        if (ordinal > 127) {
            throw new IllegalArgumentException();
        }

        astBuffer.writeByte((byte)ordinal);
    }

    ASTBufferRead getASTReadBuffer() {
        return astBuffer;
    }

    MapStringStorageBuffer getStringBuffer() {
        return stringBuffer;
    }

    @Override
    public String toString() {
        return "StringASTBuffer [astBuffer=" + astBuffer.getWritePos() + "]";
    }
}
