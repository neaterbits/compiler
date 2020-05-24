package com.neaterbits.compiler.parser.listener.encoded;

import java.util.Arrays;

import com.neaterbits.compiler.util.model.ParseTreeElement;

final class ASTBufferImpl implements ASTBuffer {

    private byte [] buffer;
    
    private int index;
    
    ASTBufferImpl() {
        this.buffer = new byte[1000];
    }
    
    private void checkSpace(int additionalSpace) {
        
        if (buffer.length - index < additionalSpace) {
            
            this.buffer = Arrays.copyOf(buffer, buffer.length * 3);
        }
    }
    
    private void writeUnsigned(int toWrite) {
     
        if (toWrite >= 256) {
            throw new IllegalArgumentException();
        }
        
        buffer[index ++] = (byte)toWrite;
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

        checkSpace(1);
        
        final int value = element.ordinal();
        
        if (value > Byte.MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        
        writeByte((byte)value);
    }

    @Override
    public void writeElementEnd(ParseTreeElement element) {
        
        checkSpace(1);

        final int value = element.ordinal();
        
        if (value > Byte.MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        
        writeByte((byte) -value);
    }

    @Override
    public void writeLeafElement(ParseTreeElement element) {
    
        checkSpace(1);
        
        final int value = element.ordinal();
        
        if (value > Byte.MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        
        writeByte((byte)value);
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
                | (long)unsigned(buffer[index + 7]);
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
        
        final byte b = buffer[index];
        
        if (b >= 0) {
            ref.element = ParseTreeElement.values()[b];
            ref.isStart = true;
        }
        else {
            ref.element = ParseTreeElement.values()[- b];
            ref.isStart = false;
        }
    }
}
