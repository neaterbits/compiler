package dev.nimbler.compiler.parser.recursive.cached.keywords;

import org.jutils.parse.IToken;

public interface CachedKeywords<TOKEN extends IToken> {

    CachedKeyword<TOKEN> getKeyword(int index);
    
    int count();
    
}
