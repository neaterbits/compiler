package dev.nimbler.compiler.parser.recursive.cached.expressions;

import dev.nimbler.compiler.parser.recursive.cached.ScratchBuf;
import dev.nimbler.compiler.parser.recursive.cached.ScratchEntity;

final class CachedOperatorsImpl
    extends ScratchEntity<CachedOperator, Operators, OperatorsAllocator>
    implements Operators, OperatorsAllocator {

    CachedOperatorsImpl(ScratchBuf<CachedOperator, Operators, OperatorsAllocator, ?> buf) {
        super(buf);
    }

    @Override
    public CachedOperator getOrCreateOperator() {
        return getOrCreate();
    }

    @Override
    protected CachedOperator createPart() {
        return new CachedOperator();
    }

    @Override
    protected Operators getToProcess() {
        return this;
    }

    @Override
    protected OperatorsAllocator getList() {
        return this;
    }
}
