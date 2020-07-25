package com.neaterbits.compiler.parser.recursive.cached.expressions;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.parser.recursive.cached.ScratchBuf;
import com.neaterbits.compiler.util.Base;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.name.Names;
import com.neaterbits.compiler.util.operator.Arity;
import com.neaterbits.compiler.util.operator.Operator;
import com.neaterbits.compiler.util.operator.OperatorType;

public final class ExpressionCache {

    private final ContextWriter contextWriter;
    private final LanguageOperatorPrecedence languageOperatorPrecedence;
    
    private final ExpressionCacheApplier applier;
    
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
        
        this.applier = new ExpressionCacheApplier(contextWriter);
        
        this.cachedPrimaries = new ScratchBuf<>(CachedPrimariesImpl::new);
        this.cachedOperators = new ScratchBuf<>(CachedOperatorsImpl::new);
        
        this.stack = new ExpressionCacheList[1000];
        this.lastIndex = -1;
        
        push();
    }
    
    public boolean isEmpty() {
        return lastIndex == 0 && stack[0].count() == 0;
    }
    
    private ExpressionCacheList push() {
        
        final ExpressionCacheList subList = getOrCreateCacheList();
        
        push(subList);
        
        return subList;
    }

    private void push(ExpressionCacheList subList) {
        
        Objects.requireNonNull(subList);
        
        stack[++ lastIndex] = subList;
    }
    
    private void pop() {
        
        if (lastIndex < 0) {
            throw new IllegalStateException();
        }

        -- lastIndex;
    }
    
    private ExpressionCacheList getOrCreateCacheList() {
        
        final ExpressionCacheList expressionCacheList = new ExpressionCacheList();
        
        expressionCacheList.init(
                cachedPrimaries.startScratchParts(),
                cachedOperators.startScratchParts());
        
        return expressionCacheList;
    }
    
    private ParametersList getOrCreateParametersList(int parametersContext) {
        
        return new ParametersList(parametersContext);
    }
    
    private ExpressionCacheList get() {
        return stack[lastIndex];
    }
    
    private boolean atRoot() {
        
        return lastIndex == 0;
    }

    public void addName(int context, long name) {
        get().addName(context, name);
    }
    
    public void addIntegerLiteral(int context, long value, Base base, boolean signed, int bits) {

        get().addIntegerLiteral(context, value, base, signed, bits);
    }

    public void addStringLiteral(int context, long value) {

        get().addStringLiteral(context, value);
    }

    public void addBooleanLiteral(int context, boolean value) {
        
        get().addBooleanLiteral(context, value);
    }
    
    public ParseTreeElement getTypeOfLast() {
        return get().getLast().getType();
    }
    
    public void addMethodInvocationStart(int parametersContext) {

        final ExpressionCacheList list = get();
        
        // Last should be name
        final CachedPrimary last = list.getLast();
        if (last.getType() != ParseTreeElement.NAME) {
            throw new IllegalArgumentException();
        }
        
        final int numOperators = list.operatorsCount();
        
        // Should be all names leading up to this or just one name at the end
        if (numOperators == 0) {

            // Should be only name
            addMethodInvocationFromName(list, parametersContext);
        }
        else {
            // Could be eg. literal + method()
            // only names like or Class.staticMethod() 

            if (list.getCachedOperator(0).getOperator().getOperatorType() == OperatorType.SCOPE) {
                // Solely names
                addMethodInvocationFromName(list, parametersContext);
            }
            else {
                // Only last node is a name
                if (last.getType() != ParseTreeElement.NAME) {
                    throw new IllegalStateException();
                }
                
                addMethodInvocationFromName(list, parametersContext);
            }
        }
    }
    
    private void addMethodInvocationFromName(
            ExpressionCacheList list,
            int parametersContext) {

        final CachedPrimary primary = list.getCachedPrimary(0);
        
        if (!ExpressionCacheApplier.isScopedFieldType(primary.getType())) {
            throw new IllegalStateException("Not a name type " + primary.getType());
        }

        final ParametersList parametersList = getOrCreateParametersList(parametersContext);
        
        list.replaceLastWithMethodInvocation(
                contextWriter.writeContext(primary.getContext()),
                parametersList);
    }
    
    public void addParametersStart(int startContext) {
        
    }
    
    private CachedPrimary getMethodInvocation() {
        
        final CachedPrimary methodInvocation = get().getLast();
        
        if (methodInvocation.getType() != ParseTreeElement.METHOD_INVOCATION_EXPRESSION) {
            throw new IllegalStateException();
        }
        
        return methodInvocation;
    }

    public void addParameterStart() {
        
        final CachedPrimary methodInvocationPrimary = getMethodInvocation();
        
        final ExpressionCacheList list = push();
        
        methodInvocationPrimary.addParameter(list);
    }
    
    public void addParameterEnd() {
        
        pop();
    }

    public void addParametersEnd(int startContext) {
        
    }
    
    public void addMethodInvocationEnd() {
        
    }
    
    public void addOperator(int context, Operator operator) {
        
        if (operator.getArity() == Arity.UNARY) {
            addUnaryOperator(context, operator);
        }
        else if (operator.getArity() == Arity.BINARY) {
            addBinaryOperator(context, operator);
        }
        else {
            throw new UnsupportedOperationException();
        }
    }
    
    private void addUnaryOperator(int context, Operator operator) {

        final ExpressionCacheList list = get();
        
        if (list.operatorsCount() == 0) {
            switch (list.primariesCount()) {
            case 0: {
                // prefix
                final int precedence = languageOperatorPrecedence.getPrecedence(operator);
    
                list.addOperator(context, operator, precedence);
                break;
            }

            case 1: {
                // postfix
                final int precedence = languageOperatorPrecedence.getPrecedence(operator);
                
                list.addOperator(context, operator, precedence);
                break;
            }
                
            default:
                throw new IllegalStateException();
            }
        }
        else {
            switch (list.getArity()) {
            case UNARY:
                addUnaryOperatorToUnary(list, context, operator);
                break;
                
            case BINARY:
                addUnaryOperatorToList(list, context, operator);
                break;
         
            default:
                throw new UnsupportedOperationException();
            }
        }
    }
    
    private void addUnaryOperatorToUnary(ExpressionCacheList list, int context, Operator operator) {

        if (list.primariesCount() != 0) {
            throw new IllegalStateException();
        }
        
        final int precedence = languageOperatorPrecedence.getPrecedence(operator);

        final int listOperatorPrecedence = list.getOperatorPrecedence();
        
        if (precedence != listOperatorPrecedence) {
            throw new UnsupportedOperationException();
        }

        final ExpressionCacheList sub = push();
        
        list.addSubList(context, sub);
        
        sub.addOperator(context, operator, precedence);
    }

    private void addUnaryOperatorToList(ExpressionCacheList list, int context, Operator operator) {

        if (list.primariesCount() == 1 && list.operatorsCount() == 0) {
            
            // Unary operator should be before?
            throw new IllegalStateException();
        }
        else if (list.primariesCount() == list.operatorsCount() || list.primariesCount() == list.operatorsCount() + 1) {

            final int precedence = languageOperatorPrecedence.getPrecedence(operator);
            
            final ExpressionCacheList cacheList = getOrCreateCacheList();
            
            final int listOperatorPrecedence = list.getOperatorPrecedence();

            cacheList.addOperator(context, operator, precedence);
            
            if (precedence <= listOperatorPrecedence) {
                
                pop();
                
                get().addSubList(contextWriter.writeContext(context), cacheList);
                
                push(cacheList);
            }
            else {
                
                get().addSubList(contextWriter.writeContext(context), cacheList);
                
                push(cacheList);
            }
        }
        else {
            throw new UnsupportedOperationException();
        }
    }

    private void addBinaryOperator(int context, Operator operator) {
        
        final int precedence = languageOperatorPrecedence.getPrecedence(operator);

        final ExpressionCacheList list = get();
        
        switch (list.primariesCount()) {
        case 0:
            throw new IllegalStateException();
            
        case 1:
            if (list.operatorsCount() > 0 && list.getArity() == Arity.UNARY) {
                // Cannot add to unary directly
                if (precedence < list.getOperatorPrecedence()) {

                    if (atRoot()) {
                        
                        // Unary is at root so must move to sublist
                        pop();
                        
                        final ExpressionCacheList root = push();
                        
                        root.addSubList(
                                contextWriter.writeContext(list.getContextAt(0)),
                                list);
                        
                        root.addOperator(context, operator, precedence);
                    }
                    else {
                        pop(); // pop unary
                        
                        // add to current list above
                        addBinaryOperator(context, operator);
                    }
                }
                else {
                    // Move to sublist
                    final ExpressionCacheList subList = getOrCreateCacheList();
                    
                    get().replaceLastWithSubList(context, operator, precedence, subList, contextWriter);

                    push(subList);
                }
            }
            else {
                list.addOperator(context, operator, precedence);
            }
            break;
            
        default:
            
            if (list.getArity() == Arity.UNARY) {
                throw new IllegalStateException();
            }
            
            final int listOperatorPrecedence = list.getOperatorPrecedence();

            // Find precedence
            if (precedence == listOperatorPrecedence) {
                list.addOperator(context, operator, precedence);
            }
            else if (precedence < listOperatorPrecedence) {
                // Has to move current onto sublist.
                
                final ExpressionCacheList curList = get();
                
                final ExpressionCacheList opList = getOrCreateCacheList();
                
                opList.addOperator(context, operator, precedence);

                opList.addSubList(curList.getContextAt(0), curList);

                if (lastIndex > 0) {
                    // Replace
                    stack[lastIndex - 1].replaceLastWithSubList(context, operator, listOperatorPrecedence, opList, contextWriter);
                }
                
                stack[lastIndex] = opList;
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
        
        applier.apply(getStartingPoint(), listener);
    }
    
    public ExpressionCacheList getStartingPoint() {
        return stack[0];
    }

    public ExpressionCacheApplier getApplier() {
        return applier;
    }
    
    public void clear() {

        cachedPrimaries.clear();
        cachedOperators.clear();

        this.lastIndex = -1;
        
        push();
    }

    public boolean areAllTopLevelNames() {
        
        boolean allTopLevelNames;

        if (lastIndex != 0) {
            allTopLevelNames = false;
        }
        else {
            allTopLevelNames = true;
            
            final ExpressionCacheList list = stack[0];
            
            final int numPrimaries = list.primariesCount();
            
            for (int i = 0; i < numPrimaries; ++ i) {
                
                if (list.getCachedPrimary(i).getType() != ParseTreeElement.NAME) {
                    allTopLevelNames = false;
                    break;
                }
            }
        }
        
        return allTopLevelNames;
    }

    public Names getTopLevelNames() {
        
        return stack[0];
    }
}
