package com.neaterbits.compiler.parser.recursive;

import java.util.Objects;

import com.neaterbits.util.parse.IToken;

public final class CachedKeyword<TOKEN extends IToken> {

    private TOKEN token;
    private int context;
    private long stringRef;
    
    public CachedKeyword(TOKEN token, int context, long stringRef) {
        init(token, context, stringRef);
    }
    
    public void init(TOKEN token, int context, long stringRef) {

        Objects.requireNonNull(token);
        
        this.token = token;
        this.context = context;
        this.stringRef = stringRef;
    }

    public TOKEN getToken() {
        return token;
    }

    public int getContext() {
        return context;
    }

    public long getStringRef() {
        return stringRef;
    }
}
