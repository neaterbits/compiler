package com.neaterbits.compiler.parser.recursive.cached.expressions;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.parser.recursive.cached.ScratchBuf;
import com.neaterbits.compiler.util.Base;
import com.neaterbits.compiler.util.method.MethodInvocationType;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.name.Names;
import com.neaterbits.compiler.util.operator.Operator;
import com.neaterbits.compiler.util.operator.OperatorType;
import com.neaterbits.compiler.util.parse.FieldAccessType;

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

    public void addName(int context, long name) {
        get().addName(context, name);
    }
    
    public void addIntegerLiteral(int context, long value, Base base, boolean signed, int bits) {

        get().addIntegerLiteral(context, value, base, signed, bits);
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
                if (list.getLast().getType() != ParseTreeElement.NAME) {
                    throw new IllegalStateException();
                }
                
                throw new UnsupportedOperationException();
            }
        }
    }
    
    private static boolean isScopedFieldType(ParseTreeElement type) {
        
        return type == ParseTreeElement.NAME || type == ParseTreeElement.METHOD_INVOCATION_EXPRESSION;
    }
    
    private void addMethodInvocationFromName(
            ExpressionCacheList list,
            int parametersContext) {

        final CachedPrimary primary = list.getCachedPrimary(0);
        
        if (!isScopedFieldType(primary.getType())) {
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
        
        if (
                   numOperators > 0
                && cacheList.getCachedOperator(0).getOperator().getOperatorType() == OperatorType.SCOPE) {
            
            final int startContext = contextWriter.writeContext(cacheList.getContextAt(0));
            
            listener.onPrimaryStart(startContext);

            boolean atObject = false;
            
            for (int i = 0; i <= numOperators; ++ i) {

                final CachedPrimary primary = cacheList.getCachedPrimary(i);
                
                if (!isScopedFieldType(primary.getType())) {
                    throw new IllegalStateException();
                }
                
                if (primary.getType() == ParseTreeElement.NAME) {
                    
                    if (atObject) {
                        listener.onFieldAccess(
                                primary.getContext(),
                                FieldAccessType.FIELD,
                                null,
                                null,
                                primary.getName(),
                                contextWriter.writeContext(primary.getContext()));
                    }
                    else {
                        listener.onNamePrimary(primary.getContext(), primary.getName());
                    }
                }
                else {
                    applyPrimaryToListener(primary, listener);
                    
                    if (primary.getType() == ParseTreeElement.METHOD_INVOCATION_EXPRESSION) {
                        // Anything after this is field access
                        atObject = true;
                    }
                }
            }

            listener.onPrimaryEnd(startContext, null);
        }
        else {
        
            for (int i = 0; i <= numOperators; ++ i) {
    
                final CachedPrimary primary = cacheList.getCachedPrimary(i);
                
                applyPrimaryToListener(primary, listener);
                
                if (i < numOperators) {
                    final CachedOperator cachedOperator = cacheList.getCachedOperator(i);
                    
                    System.out.println("## add operator " + cachedOperator.getOperator());

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
            
            listener.onNestedExpressionStart(primary.getContext());

            apply(primary.getSubList(), listener);

            listener.onNestedExpressionEnd(primary.getContext(), null);
            break;
            
        case NAME:
            listener.onNameReference(primary.getContext(), primary.getName());
            break;
            
        case METHOD_INVOCATION_EXPRESSION:
            
            listener.onMethodInvocationStart(
                    primary.getContext(),
                    MethodInvocationType.UNRESOLVED,
                    primary.getMethodName(),
                    primary.getMethodNameContext());
            
            final ParametersList parametersList = primary.getParametersList();
            
            if (parametersList != null) {
                
                final int parametersCount = parametersList.count();
                
                if (parametersCount != 0) {
                    
                    listener.onParametersStart(parametersList.getContext());
                    
                    for (int i = 0; i < parametersCount; ++ i) {
                        
                        final ExpressionCacheList list = parametersList.getParameter(i);
                        
                        final int paramStartContext = contextWriter.writeContext(list.getCachedPrimary(0).getContext());
                        
                        listener.onParameterStart(paramStartContext);
                        
                        apply(list, listener);
                        
                        listener.onParameterEnd(paramStartContext, null);
                    }
        
                    listener.onParametersEnd(parametersList.getContext(), null);
                }
            }
    
            listener.onMethodInvocationEnd(primary.getContext(), null);
            break;
        
        default:
            throw new UnsupportedOperationException("Unknown primary type " + primary.getType());
        }
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
