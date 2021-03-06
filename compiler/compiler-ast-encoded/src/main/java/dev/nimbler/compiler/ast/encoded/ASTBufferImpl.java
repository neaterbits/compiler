package dev.nimbler.compiler.ast.encoded;

import java.util.Arrays;

import dev.nimbler.compiler.types.ParseTreeElement;

public final class ASTBufferImpl implements ASTBuffer {

    private byte [] buffer;

    private int index;

    public ASTBufferImpl() {
        this.buffer = new byte[1000];
    }

    private void checkSpace(int additionalSpace) {

        if (buffer.length - index < additionalSpace) {

            this.buffer = Arrays.copyOf(buffer, buffer.length * 3);
        }
    }

    private void writeUnsigned(int toWrite) {

        writeUnsigned(index ++, toWrite);
    }

    private void writeUnsigned(int index, int toWrite) {

        if (toWrite >= 256) {
            throw new IllegalArgumentException();
        }

        buffer[index] = (byte)toWrite;
    }

    private static int unsigned(byte value) {

        final int result;

        if (value < 0) {
            result = 0xFF + value + 1;
        }
        else {
            result = value;
        }

        return result;
    }

    @Override
    public int getWritePos() {
        return index;
    }

    @Override
    public void writeBoolean(boolean value) {

        checkSpace(1);

        writeUnsigned(value ? 1 : 0);
    }

    @Override
    public void writeByte(byte value) {

        checkSpace(1);

        buffer[index ++] = value;
    }

    @Override
    public void writeShort(short value) {

        checkSpace(2);

        final short shifted = (short)(value >>> 8);

        writeUnsigned(shifted);
        writeUnsigned(value & 0x00FF);
    }

    private void writeShort(int index, short value) {

        final short shifted = (short)(value >>> 8);

        writeUnsigned(index, shifted);
        writeUnsigned(index + 1, value & 0x00FF);
    }

    @Override
    public void writeInt(int value) {

        checkSpace(4);

        writeUnsigned( value >>> 24);
        writeUnsigned((value >>> 16) & 0x000000FF);
        writeUnsigned((value >>> 8 ) & 0x000000FF);
        writeUnsigned((value >>> 0 ) & 0x000000FF);
    }

    @Override
    public void writeLong(long value) {

        checkSpace(8);

        writeUnsigned((int)(value  >>> 56));
        writeUnsigned((int)((value >>> 48) & 0x000000FF));
        writeUnsigned((int)((value >>> 40) & 0x000000FF));
        writeUnsigned((int)((value >>> 32) & 0x000000FF));
        writeUnsigned((int)((value >>> 24) & 0x000000FF));
        writeUnsigned((int)((value >>> 16) & 0x000000FF));
        writeUnsigned((int)((value >>> 8 ) & 0x000000FF));
        writeUnsigned((int)((value >>> 0 ) & 0x000000FF));
    }

    @Override
    public void writeElementStart(ParseTreeElement element) {

        if (element.isLeaf()) {
            throw new IllegalArgumentException();
        }

        checkSpace(2);

        final int value = element.ordinal();

        if (value >= 256) {
            throw new IllegalArgumentException();
        }

        writeUnsigned(value);

        writeByte((byte)25); // start
    }

    @Override
    public void writeElementEnd(ParseTreeElement element) {

        if (element.isLeaf()) {
            throw new IllegalArgumentException();
        }

        checkSpace(2);

        final int value = element.ordinal();

        if (value >= 256) {
            throw new IllegalArgumentException();
        }

        writeUnsigned(value);

        writeByte((byte)35); // end
    }

    @Override
    public void writeLeafElement(ParseTreeElement element) {

        if (!element.isLeaf()) {
            throw new IllegalArgumentException();
        }

        checkSpace(1);

        final int value = element.ordinal();

        if (value >= 256) {
            throw new IllegalArgumentException();
        }

        writeUnsigned(value);
    }

    @Override
    public boolean getBoolean(int index) {

        final boolean result;

        switch (buffer[index]) {
        case 0:
            result = false;
            break;

        case 1:
            result = true;
            break;

        default:
            throw new IllegalStateException();
        }

        return result;
    }

    @Override
    public byte getByte(int index) {
        return buffer[index];
    }

    @Override
    public short getShort(int index) {

        return (short)(((short)unsigned(buffer[index])) << 8 | (short)unsigned(buffer[index + 1]));
    }

    @Override
    public int getInt(int index) {

        return
                  unsigned(buffer[index])     << 24
                | unsigned(buffer[index + 1]) << 16
                | unsigned(buffer[index + 2]) << 8
                | unsigned(buffer[index + 3]);
    }

    @Override
    public long getLong(int index) {

        return
                  (long)unsigned(buffer[index])     << 56
                | (long)unsigned(buffer[index + 1]) << 48
                | (long)unsigned(buffer[index + 2]) << 40
                | (long)unsigned(buffer[index + 3]) << 32
                | (long)unsigned(buffer[index + 4]) << 24
                | (long)unsigned(buffer[index + 5]) << 16
                | (long)unsigned(buffer[index + 6]) << 8
                | unsigned(buffer[index + 7]);
    }

    @Override
    public boolean hasStringRef(int index) {

        return getInt(index) != AST.NO_STRINGREF;
    }

    @Override
    public int getStringRef(int index) {

        return getInt(index);
    }

    @Override
    public <T extends Enum<T>> T getEnumByte(int index, Class<T> type) {

        final int ordinal = getByte(index);

        return type.getEnumConstants()[ordinal];
    }

    @Override
    public boolean hasContextRef(int index) {

        return getInt(index) != AST.NO_CONTEXTREF;
    }

    @Override
    public int getContextRef(int index) {

        return getInt(index);
    }

    @Override
    public ParseTreeElement getParseTreeElement(int index) {

        final byte b = buffer[index];

        if (b < 0) {
            throw new IllegalStateException();
        }

        return ParseTreeElement.values()[b];
    }

    @Override
    public void getParseTreeElement(int index, ParseTreeElementRef ref) {

        final int b = unsigned(buffer[index]);

        ref.element = ParseTreeElement.values()[b];

        if (ref.element.isLeaf()) {
            ref.isStart = true;
            ref.index = index + 1;
        }
        else {
            switch (buffer[index + 1]) {
            case 25:
                ref.isStart = true;
                break;

            case 35:
                ref.isStart = false;
                break;

            default:
                throw new IllegalStateException();
            }

            ref.index = index + 1 + 1;
        }
    }

    @Override
    public void appendFrom(int pos, int size) {

        checkSpace(size);

        if (pos + size > index) {
            throw new IllegalArgumentException("reading non-initialized " + pos + "/" +  size + "/" + index);
        }

        System.arraycopy(buffer, pos, buffer, index, size);

        this.index += size;
    }

    public static final int REPLACE_SIZE = 4;

    @Override
    public void replaceElement(int index, int replacementIndex, int originalSize) {

        if (replacementIndex >= 1 << 15) {
            throw new IllegalArgumentException();
        }

        if (originalSize >= 1 << 15) {
            throw new IllegalArgumentException();
        }

        writeUnsigned(index, ParseTreeElement.REPLACE.ordinal());
        writeShort(index + 1, (short)replacementIndex);
        writeShort(index + 3, (short)originalSize);
    }

    @Override
    public int getReplacementIndex(int index) {

        return getShort(index + 1);
    }

    @Override
    public int getOriginalSize(int index) {

        return getShort(index + 3);
    }
}
