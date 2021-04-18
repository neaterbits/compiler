package dev.nimbler.compiler.parser.recursive.cached.keywords;

import org.jutils.parse.IToken;

import dev.nimbler.compiler.parser.recursive.cached.ScratchList;

public interface CachedKeywordsList<TOKEN extends IToken>
        extends ScratchList<CachedKeywords<TOKEN>> {

    void addScratchKeyword(TOKEN token, int context, long stringRef);
}
