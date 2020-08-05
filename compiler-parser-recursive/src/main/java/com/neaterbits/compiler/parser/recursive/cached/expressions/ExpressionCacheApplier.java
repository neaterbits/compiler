package com.neaterbits.compiler.parser.recursive.cached.expressions;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.common.IterativeParseTreeListener;
import com.neaterbits.compiler.util.method.MethodInvocationType;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.operator.Arity;
import com.neaterbits.compiler.util.operator.Instantiation;
import com.neaterbits.compiler.util.operator.OperatorType;
import com.neaterbits.compiler.util.parse.FieldAccessType;

public final class ExpressionCacheApplier {

    private final ContextWriter contextWriter;
    
    ExpressionCacheApplier(ContextWriter contextWriter) {
        
        Objects.requireNonNull(contextWriter);
        
        this.contextWriter = contextWriter;
    }

    public <COMPILATION_UNIT> void apply(ExpressionCacheList cacheList, IterativeParseTreeListener<COMPILATION_UNIT> listener) {
        
        if (cacheList.operatorsCount() > 0) {
            
            final Arity arity = cacheList.getArity();
            
            switch (arity) {
            
            case UNARY:
                applyUnaryOperatorsList(cacheList, listener);
                break;
            
            case BINARY:
                applyBinaryOperatorsList(cacheList, listener);
                break;
                
            default:
                throw new UnsupportedOperationException();
                
            }
        }
        else {
            applyPrimaryToListener(cacheList.getCachedPrimary(0), listener);
        }
    }
    
    private <COMPILATION_UNIT> void applyUnaryOperatorsList(ExpressionCacheList cacheList, IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        if (cacheList.operatorsCount() != 1) {
            throw new IllegalStateException();
        }
        
        if (cacheList.primariesCount() != 1) {
            throw new IllegalStateException();
        }
        
        if (cacheList.getCachedOperator(0).getOperator() == Instantiation.NEW) {
            
            final CachedPrimary methodInvocation = cacheList.getCachedPrimary(0);
            
            if (methodInvocation.getType() != ParseTreeElement.METHOD_INVOCATION_EXPRESSION) {
                throw new IllegalStateException();
            }
            
            final int startContext = methodInvocation.getContext();
            
            listener.onClassInstanceCreationExpressionStart(startContext);
            
            listener.onClassInstanceCreationTypeAndConstructorName(
                    methodInvocation.getMethodNameContext(),
                    methodInvocation.getMethodName());
        
            listener.onClassInstanceCreationExpressionEnd(startContext, null);
        }
        else {
        
            final int startContext = cacheList.getContextAt(0);
            
            listener.onUnaryExpressionStart(startContext, cacheList.getCachedOperator(0).getOperator());
            
            applyPrimaryToListener(cacheList.getCachedPrimary(0), listener);
            
            listener.onUnaryExpressionEnd(startContext, null);
        }
    }

    private <COMPILATION_UNIT> void applyBinaryOperatorsList(ExpressionCacheList cacheList, IterativeParseTreeListener<COMPILATION_UNIT> listener) {

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
                    
                    listener.onExpressionBinaryOperator(cachedOperator.getContext(), cachedOperator.getOperator());
                }
            }
        }
    }
    
    private <COMPILATION_UNIT> void applyPrimaryToListener(
            CachedPrimary primary,
            IterativeParseTreeListener<COMPILATION_UNIT> listener) {
 
        switch (primary.getType()) {
        
        case INTEGER_LITERAL:
            listener.onIntegerLiteral(
                    primary.getContext(),
                    primary.getIntegerLiteralValue(),
                    primary.getBase(),
                    primary.isSigned(),
                    primary.getBits());
            break;

        case STRING_LITERAL:
            listener.onStringLiteral(primary.getContext(), primary.getStringLiteralValue());
            break;

        case CHARACTER_LITERAL:
            listener.onCharacterLiteral(primary.getContext(), primary.getCharacterLiteralValue());
            break;

        case BOOLEAN_LITERAL:
            listener.onBooleanLiteral(primary.getContext(), primary.getBooleanLiteralValue());
            break;
         
        case PRIMARY_LIST: {
            
            final ExpressionCacheList list = primary.getSubList();
            
            switch (list.getArity()) {
            
            case UNARY:
                applyUnaryOperatorsList(list, listener);
                break;
                
            case BINARY:
                listener.onNestedExpressionStart(primary.getContext());
    
                applyBinaryOperatorsList(list, listener);
    
                listener.onNestedExpressionEnd(primary.getContext(), null);
                break;
                
            default:
                throw new UnsupportedOperationException();
            }
            break;
        }
            
        case NAME:
            listener.onNameReference(primary.getContext(), primary.getName());
            break;
            
        case METHOD_INVOCATION_EXPRESSION: {
            
            listener.onMethodInvocationStart(
                    primary.getContext(),
                    MethodInvocationType.UNRESOLVED,
                    primary.getMethodName(),
                    primary.getMethodNameContext());
            
            final ParametersList parametersList = primary.getParametersList();
            
            if (parametersList != null) {
                
                if (!parametersList.isEmpty()) {

                    applyParameters(parametersList, listener);
                }
            }
    
            listener.onMethodInvocationEnd(primary.getContext(), null);
            break;
        }
        
        default:
            throw new UnsupportedOperationException("Unknown primary type " + primary.getType());
        }
    }
    
    public <COMPILATION_UNIT> void applyParameters(ParametersList parametersList, IterativeParseTreeListener<COMPILATION_UNIT> listener) {

        final int parametersCount = parametersList.count();

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

    static boolean isScopedFieldType(ParseTreeElement type) {
        
        return type == ParseTreeElement.NAME || type == ParseTreeElement.METHOD_INVOCATION_EXPRESSION;
    }
}
