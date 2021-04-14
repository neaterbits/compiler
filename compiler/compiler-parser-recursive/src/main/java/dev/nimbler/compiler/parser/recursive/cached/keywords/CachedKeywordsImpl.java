package dev.nimbler.compiler.parser.recursive.cached.keywords;

import com.neaterbits.util.parse.IToken;

import dev.nimbler.compiler.parser.recursive.cached.ScratchBuf;
import dev.nimbler.compiler.parser.recursive.cached.ScratchEntity;

public final class CachedKeywordsImpl<TOKEN extends IToken>
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
    protected CachedKeywords<TOKEN> getToProcess() {
        return this;
    }

    @Override
    protected CachedKeywordsList<TOKEN> getList() {
        return this;
    }

    @Override
    protected CachedKeyword<TOKEN> createPart() {
        
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
