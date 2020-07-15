package com.neaterbits.compiler.parser.recursive.cached.expressions;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.parser.recursive.cached.ScratchBuf;
import com.neaterbits.compiler.util.Base;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.operator.Operator;
import com.neaterbits.compiler.util.operator.OperatorType;

public final class ExpressionCache {

    private final ContextWriter contextWriter;
    private final LanguageOperatorPrecedence languageOperatorPrecedence;
    
    private final ExpressionCacheList [] stack;
    private int lastIndex;
    
    private final ScratchBuf<
        CachedPrimary,
        Primaries,
        PrimariesAllocator,
        CachedPrimariesImpl> cachedPrimaries;
    
    private final ScratchBuf<
        CachedOperator,
        Operators,
        OperatorsAllocator,
        CachedOperatorsImpl> cachedOperators;
    
    public ExpressionCache(ContextWriter contextWriter, LanguageOperatorPrecedence languageOperatorPrecedence) {

        this.contextWriter = contextWriter;
        this.languageOperatorPrecedence = languageOperatorPrecedence;
        
        this.cachedPrimaries = new ScratchBuf<>(CachedPrimariesImpl::new);
        this.cachedOperators = new ScratchBuf<>(CachedOperatorsImpl::new);
        
        this.stack = new ExpressionCacheList[1000];
        this.lastIndex = -1;
        
        push();
    }
    
    private void push() {
        
        push(getOrCreateCacheList());
    }

    private void push(ExpressionCacheList subList) {
        
        Objects.requireNonNull(subList);
        
        stack[++ lastIndex] = subList;
    }
    
    private ExpressionCacheList getOrCreateCacheList() {
        
        final ExpressionCacheList expressionCacheList = new ExpressionCacheList();
        
        expressionCacheList.init(
                cachedPrimaries.startScratchParts(),
                cachedOperators.startScratchParts());
        
        return expressionCacheList;
    }
    
    private ExpressionCacheList get() {
        return stack[lastIndex];
    }

    public void addName(int context, long name) {
        get().addName(context, name);
    }
    
    public void addIntegerLiteral(int context, long value, Base base, boolean signed, int bits) {

        get().addIntegerLiteral(context, value, base, signed, bits);
    }
    
    public void addOperator(int context, Operator operator) {
        
        final int precedence = languageOperatorPrecedence.getPrecedence(operator);

        final ExpressionCacheList list = get();
        
        switch (list.primariesCount()) {
        case 0:
            throw new IllegalStateException();
            
        case 1:
            list.addOperator(context, operator, precedence);
            break;
            
        default:
            final int listOperatorPrecedence = list.getOperatorPrecedence();

            // Find precedence
            if (precedence == listOperatorPrecedence) {
                list.addOperator(context, operator, precedence);
            }
            else if (precedence < listOperatorPrecedence) {
                throw new UnsupportedOperationException();
            }
            else {
                // precedence > listOperatorPrecedence
                // eg. 1 + 2 * 3 means '*' has higher precedence than '+'
                // Move 2 * 3 onto a sublist
                
                final ExpressionCacheList subList = getOrCreateCacheList();

                get().replaceLastWithSubList(
                        context,
                        operator,
                        precedence,
                        subList,
                        contextWriter);
                
                push(subList);
            }
            break;
        }
    }
    
    public <COMPILATION_UNIT> void apply(IterativeParserListener<COMPILATION_UNIT> listener) {
        
        System.out.println("apply " + stack[0]);
        
        apply(stack[0], listener);
    }
    
    public void clear() {

        cachedPrimaries.clear();
        cachedOperators.clear();

        this.lastIndex = -1;
        push();
    }

    private <COMPILATION_UNIT> void apply(ExpressionCacheList cacheList, IterativeParserListener<COMPILATION_UNIT> listener) {

        final int numOperators = cacheList.operatorsCount();
        
        final CachedPrimary initialPrimary = cacheList.getCachedPrimary(0);
        
        if (
                   initialPrimary.getType() == ParseTreeElement.NAME
                && numOperators > 0
                && cacheList.getCachedOperator(0).getOperator().getOperatorType() == OperatorType.SCOPE) {
            
            // Should be all names
            
            // for now
            throw new UnsupportedOperationException();
        }
        else {
        
            for (int i = 0; i <= numOperators; ++ i) {
    
                final CachedPrimary primary = cacheList.getCachedPrimary(i);
                
                applyPrimaryToListener(primary, listener);
                
                if (i < numOperators) {
                    final CachedOperator cachedOperator = cacheList.getCachedOperator(i);

                    listener.onExpressionBinaryOperator(cachedOperator.getContext(), cachedOperator.getOperator());
                }
            }
        }
    }
    
    private <COMPILATION_UNIT> void applyPrimaryToListener(
            CachedPrimary primary,
            IterativeParserListener<COMPILATION_UNIT> listener) {
 
        switch (primary.getType()) {
        
        case INTEGER_LITERAL:
            listener.onIntegerLiteral(
                    primary.getContext(),
                    primary.getIntegerLiteralValue(),
                    primary.getBase(),
                    primary.isSigned(),
                    primary.getBits());
            break;
         
        case PRIMARY_LIST:
            apply(primary.getSubList(), listener);
            break;
            
        case NAME:
            listener.onNameReference(primary.getContext(), primary.getName());
            break;
        
        default:
            throw new UnsupportedOperationException("Unknown primary type " + primary.getType());
        }
    }
}
