package com.neaterbits.compiler.parser.recursive.cached.expressions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ParametersList {
    
    private final int context;
    private final List<ExpressionCacheList> parameters;

    ParametersList(int context) {
        this.context = context;
        this.parameters = new ArrayList<>();
    }
    
    void addParameter(ExpressionCacheList parameter) {
        
        Objects.requireNonNull(parameter);
        
        parameters.add(parameter);
    }
    
    boolean isEmpty() {
        
        return parameters.isEmpty();
    }
    
    int count() {
        return parameters.size();
    }
    
    int getContext() {
        return context;
    }
    
    ExpressionCacheList getParameter(int index) {
        return parameters.get(index);
    }
}
