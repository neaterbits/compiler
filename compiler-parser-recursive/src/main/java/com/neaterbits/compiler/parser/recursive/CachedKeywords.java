package com.neaterbits.compiler.parser.recursive;

import com.neaterbits.util.parse.IToken;

public interface CachedKeywords<TOKEN extends IToken> {

    CachedKeyword<TOKEN> getKeyword(int index);
    
    int count();
    
}
