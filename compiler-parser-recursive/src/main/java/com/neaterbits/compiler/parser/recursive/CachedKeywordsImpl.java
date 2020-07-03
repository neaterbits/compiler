package com.neaterbits.compiler.parser.recursive;

import com.neaterbits.util.parse.IToken;

final class CachedKeywordsImpl<TOKEN extends IToken>
    extends ScratchEntity<
                CachedKeyword<TOKEN>,
                CachedKeywords<TOKEN>,
                CachedKeywordsList<TOKEN>>

    implements CachedKeywords<TOKEN>,
               CachedKeywordsList<TOKEN> {

    
    public CachedKeywordsImpl(
            ScratchBuf<
                CachedKeyword<TOKEN>,
                CachedKeywords<TOKEN>,
                CachedKeywordsList<TOKEN>, ?> buf) {
        super(buf);
    }

    @Override
    CachedKeywords<TOKEN> getToProcess() {
        return this;
    }

    @Override
    CachedKeywordsList<TOKEN> getList() {
        return this;
    }

    @Override
    CachedKeyword<TOKEN> createPart() {
        
        return new CachedKeyword<>();
    }

    @Override
    public void addScratchKeyword(TOKEN token, int context, long stringRef) {
        
        getOrCreate().init(token, context, stringRef);
    }

    @Override
    public CachedKeyword<TOKEN> getKeyword(int index) {

        return get(index);
    }

    @Override
    public int count() {

        return getCount();
    }
}
