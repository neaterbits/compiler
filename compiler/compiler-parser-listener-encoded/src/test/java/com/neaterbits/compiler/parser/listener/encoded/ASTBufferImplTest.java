package com.neaterbits.compiler.parser.listener.encoded;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.neaterbits.compiler.ast.encoded.ASTBufferImpl;

public class ASTBufferImplTest {

    private ASTBufferImpl buffer;
    
    @Before
    public void init() {
        this.buffer = new ASTBufferImpl();
    }

    @Test
    public void testReadWriteByte() {
        
        checkReadWriteByte(-128);
        checkReadWriteByte(-127);
        checkReadWriteByte(-1);
        checkReadWriteByte(0);
        checkReadWriteByte(1);
        checkReadWriteByte(127);
    }

    private void checkReadWriteByte(int value) {
        
        assertThat(value).isGreaterThanOrEqualTo(Byte.MIN_VALUE);
        assertThat(value).isLessThanOrEqualTo(Byte.MAX_VALUE);
        
        final int pos = buffer.getWritePos();
        
        buffer.writeByte((byte)value);

        assertThat((int)buffer.getByte(pos)).isEqualTo(value);
    }

    @Test
    public void testReadWriteShort() {
        
        checkReadWriteShort(-256);
        checkReadWriteShort(-255);
        checkReadWriteShort(-254);
        checkReadWriteShort(-129);
        checkReadWriteShort(-128);
        checkReadWriteShort(-127);
        checkReadWriteShort(-1);
        checkReadWriteShort(0);
        checkReadWriteShort(1);
        checkReadWriteShort(127);
        checkReadWriteShort(128);
        checkReadWriteShort(129);
        checkReadWriteShort(254);
        checkReadWriteShort(255);
        checkReadWriteShort(256);
        checkReadWriteShort(Short.MIN_VALUE);
        checkReadWriteShort(Short.MAX_VALUE);
    }

    private void checkReadWriteShort(int value) {
        
        assertThat(value).isGreaterThanOrEqualTo(Short.MIN_VALUE);
        assertThat(value).isLessThanOrEqualTo(Short.MAX_VALUE);
        
        final int pos = buffer.getWritePos();
        
        buffer.writeShort((short)value);

        assertThat((int)buffer.getShort(pos)).isEqualTo(value);
    }

    @Test
    public void testReadWriteInt() {
        
        checkReadWriteInt(-256);
        checkReadWriteInt(-255);
        checkReadWriteInt(-254);
        checkReadWriteInt(-129);
        checkReadWriteInt(-128);
        checkReadWriteInt(-127);
        checkReadWriteInt(-1);
        checkReadWriteInt(0);
        checkReadWriteInt(1);
        checkReadWriteInt(127);
        checkReadWriteInt(128);
        checkReadWriteInt(129);
        checkReadWriteInt(254);
        checkReadWriteInt(255);
        checkReadWriteInt(256);
        checkReadWriteInt(Integer.MIN_VALUE);
        checkReadWriteInt(Integer.MAX_VALUE);
    }
    
    private void checkReadWriteInt(int value) {
        
        final int pos = buffer.getWritePos();
        
        buffer.writeInt(value);

        assertThat(buffer.getInt(pos)).isEqualTo(value);
    }
    
    @Test
    public void testReadWriteLong() {
        
        checkReadWriteLong(-256);
        checkReadWriteLong(-255);
        checkReadWriteLong(-254);
        checkReadWriteLong(-129);
        checkReadWriteLong(-128);
        checkReadWriteLong(-127);
        checkReadWriteLong(-1);
        checkReadWriteLong(0);
        checkReadWriteLong(1);
        checkReadWriteLong(127);
        checkReadWriteLong(128);
        checkReadWriteLong(129);
        checkReadWriteLong(254);
        checkReadWriteLong(255);
        checkReadWriteLong(256);
        checkReadWriteLong(Integer.MIN_VALUE);
        checkReadWriteLong(Integer.MAX_VALUE);
        checkReadWriteLong(Long.MIN_VALUE);
        checkReadWriteLong(Long.MAX_VALUE);
    }

    private void checkReadWriteLong(long value) {
        
        final int pos = buffer.getWritePos();
        
        buffer.writeLong(value);

        assertThat(buffer.getLong(pos)).isEqualTo(value);
    }
    
    @Test
    public void testBooleanTrue() {

        final int pos = buffer.getWritePos();
        
        buffer.writeBoolean(true);

        assertThat(buffer.getBoolean(pos)).isTrue();
    }

    @Test
    public void testBooleanFalse() {

        final int pos = buffer.getWritePos();
        
        buffer.writeBoolean(true);

        assertThat(buffer.getBoolean(pos)).isTrue();
    }
}
