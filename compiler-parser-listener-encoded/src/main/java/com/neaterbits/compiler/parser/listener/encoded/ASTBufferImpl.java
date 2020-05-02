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
        
        writeUnsigned(value >>> 16);
        writeUnsigned(value & 0x000000FF);
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
        
        writeUnsigned((int)(value >>> 56));
        writeUnsigned((int)(value >>> 48));
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

        return (short)(((short)buffer[index]) << 8 | (short)buffer[index + 1]);
    }

    @Override
    public int getInt(int index) {
        
        return buffer[index] << 24 | buffer[index + 1] << 16 | buffer[index + 2] << 8 | buffer[index + 3];
    }

    @Override
    public long getLong(int index) {

        return 
                  (long)buffer[index] << 56
                | (long)buffer[index + 1] << 48
                | (long)buffer[index + 2] << 40
                | (long)buffer[index + 3] << 32
                | (long)buffer[index] << 24
                | (long)buffer[index + 1] << 16
                | (long)buffer[index + 2] << 8
                | (long)buffer[index + 3];
    }

    @Override
    public ParseTreeElement getParseTreeElement(int index) {

        final byte b = buffer[index];
        
        if (b < 0) {
            throw new IllegalStateException();
        }
        
        return ParseTreeElement.values()[b];
    }
}
