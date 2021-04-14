package com.neaterbits.compiler.util;

import com.neaterbits.util.Hash;

public final class IntKeyIntValueHash {

    private long [] hash;
    
    public IntKeyIntValueHash(int initialSize) {

        this.hash = Hash.makeHashMap(initialSize, Hash.UNDEF);
    }
    
    public void put(int key, int value) {
        
        this.hash = Hash.hashStore(hash, key, value, Hash.UNDEF, Hash.INT_KEY_INT_VALUE);
    }
    
    public int get(int key) {
        
        final long value = Hash.hashGet(hash, key, Hash.UNDEF, Hash.INT_KEY_INT_VALUE);
        
        if (value > Integer.MAX_VALUE) {
            throw new IllegalStateException();
        }
        
        if (value < Integer.MIN_VALUE) {
            throw new IllegalStateException();
        }
        
        return (int)value;
    }
}
