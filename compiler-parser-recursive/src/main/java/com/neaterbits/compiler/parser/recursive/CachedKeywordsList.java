package com.neaterbits.compiler.parser.recursive;

import com.neaterbits.util.parse.IToken;

public interface CachedKeywordsList<TOKEN extends IToken>
        extends ScratchList<CachedKeywords<TOKEN>> {

    void addScratchKeyword(TOKEN token, int context, long stringRef);
}
