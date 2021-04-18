package dev.nimbler.compiler.ast.encoded;

import java.util.Objects;

import org.jutils.buffers.MapStringStorageBuffer;
import org.jutils.io.strings.StringBufferAdder;

import dev.nimbler.compiler.types.ParseTreeElement;

public final class StringASTBuffer {

    private final StringBufferAdder stringBufferAdder;

    private final ASTBuffer astBuffer;

    private final MapStringStorageBuffer stringBuffer;

    public StringASTBuffer(StringBufferAdder stringBufferAdder) {
        
        this(stringBufferAdder, new MapStringStorageBuffer(), false);

        Objects.requireNonNull(stringBufferAdder);
    }

    private StringASTBuffer(StringBufferAdder stringBufferAdder, MapStringStorageBuffer stringBuffer, boolean disambiguate) {

        Objects.requireNonNull(stringBuffer);
        
        this.stringBufferAdder = stringBufferAdder;
        this.stringBuffer = stringBuffer;

        this.astBuffer = new ASTBufferImpl();

    }

    public StringASTBuffer(MapStringStorageBuffer stringBuffer) {
        
        this(null, stringBuffer, false);
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

    public int getParseTreeRef() {

        return astBuffer.getWritePos();
    }

    public void writeElementStart(ParseTreeElement element) {

        astBuffer.writeElementStart(element);
    }

    public void writeElementEnd(ParseTreeElement element) {

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

    public ASTBuffer getASTBuffer() {
        return astBuffer;
    }

    public ASTBufferRead getASTReadBuffer() {
        return astBuffer;
    }

    public MapStringStorageBuffer getStringBuffer() {
        return stringBuffer;
    }

    @Override
    public String toString() {
        return "StringASTBuffer [astBuffer=" + astBuffer.getWritePos() + "]";
    }
}
