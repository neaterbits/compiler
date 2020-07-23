package com.neaterbits.compiler.parser.recursive.cached.expressions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.Base;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.name.Names;
import com.neaterbits.compiler.util.operator.Arity;
import com.neaterbits.compiler.util.operator.Operator;

final class ExpressionCacheList implements Names {
    
    private final List<CachedPrimary> primaries;
    private final List<CachedOperator> operators;

    private PrimariesAllocator primariesAllocator;
    private OperatorsAllocator operatorsAllocator;
    
    ExpressionCacheList() {
        this.primaries = new ArrayList<>();
        this.operators = new ArrayList<>();
    }
    
    boolean isEmpty() {
        return primaries.isEmpty();
    }
    
    int primariesCount() {
        return primaries.size();
    }
    
    CachedPrimary getCachedPrimary(int index) {
        return primaries.get(index);
    }
    
    CachedPrimary getLast() {
        return primaries.get(primaries.size() - 1);
    }
    
    CachedOperator getCachedOperator(int index) {
        return operators.get(index);
    }
    
    void replaceLastWithSubList(
            int operatorContext,
            Operator operator,
            int precedence,
            ExpressionCacheList subList,
            ContextWriter contextWriter) {
        
        Objects.requireNonNull(operator);
        Objects.requireNonNull(subList);
        
        if (!subList.isEmpty()) {
            throw new IllegalArgumentException();
        }
        
        final CachedPrimary subListPrimary = primariesAllocator.getOrCreatePrimary();
        
        subListPrimary.initSubList(
                contextWriter.writeContext(subListPrimary.getContext()),
                subList);
        
        final CachedPrimary lastPrimary = primaries.set(primaries.size() - 1, subListPrimary);
        
        subList.primaries.add(lastPrimary);
        
        subList.addOperator(operatorContext, operator, precedence);
    }
    
    int operatorsCount() {
        return operators.size();
    }
    
    int getOperatorPrecedence() {
        return operators.get(0).getPrecedence();
    }
    
    Arity getArity() {
        return operators.get(0).getOperator().getArity();
    }
    
    void init(PrimariesAllocator primariesList, OperatorsAllocator operatorsList) {
        
        Objects.requireNonNull(primariesList);
        Objects.requireNonNull(operatorsList);

        this.primariesAllocator = primariesList;
        this.operatorsAllocator = operatorsList;
    }
    
    private CachedPrimary addPrimary() {
        
        final CachedPrimary primary = primariesAllocator.getOrCreatePrimary();
        
        primaries.add(primary);
        
        return primary;
    }
    
    void addName(int context, long name) {
        
        addPrimary().initName(context, name);
    }
    
    void addIntegerLiteral(int context, long value, Base base, boolean signed, int bits) {
        
        addPrimary().initIntegerLiteral(context, value, base, signed, bits);
    }

    void addStringLiteral(int context, long value) {
        
        addPrimary().initStringLiteral(context, value);
    }

    void addBooleanLiteral(int context, boolean value) {
        
        addPrimary().initBooleanLiteral(context, value);
    }
    
    void addSubList(int context, ExpressionCacheList subList) {

        addPrimary().initSubList(context, subList);
    }
    
    void replaceLastWithMethodInvocation(int context, ParametersList parametersSubList) {
        
        final CachedPrimary last = getLast();
        if (last.getType() != ParseTreeElement.NAME) {
            throw new IllegalStateException();
        }
        
        last.initMethodInvocation(
                context,
                last.getName(),
                last.getContext(),
                parametersSubList);
    }
    
    void addOperator(int context, Operator operator, int precedence) {
        
        final CachedOperator cachedOperator = operatorsAllocator.getOrCreateOperator();
    
        operators.add(cachedOperator);
        
        cachedOperator.init(context, operator, precedence);
    }

    @Override
    public long getStringAt(int index) {
        return primaries.get(index).getName();
    }

    @Override
    public int getContextAt(int index) {
        return primaries.get(index).getContext();
    }

    @Override
    public int count() {
        return primaries.size();
    }

    @Override
    public String toString() {
        return "ExpressionCacheList [primaries=" + primaries + ", operators=" + operators + "]";
    }
}
