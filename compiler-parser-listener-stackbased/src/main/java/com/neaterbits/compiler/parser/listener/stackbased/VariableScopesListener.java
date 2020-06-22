package com.neaterbits.compiler.parser.listener.stackbased;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.BaseStackVariableDeclaration;
import com.neaterbits.compiler.parser.listener.stackbased.state.base.VariablesMap;
import com.neaterbits.compiler.util.ArrayStack;
import com.neaterbits.compiler.util.Context;

@Deprecated // moved out of parser listener
final class VariableScopesListener<VARIABLE_DECLARATION, VARIABLE_MODIFIER_HOLDER, TYPE_REFERENCE> {
    
    interface CreateDeclaration<VD, VMH, TR> {
        
        // SIMPLE_VARIABLE_REFERENCE createSimpleVariableReference(Context context, VARIABLE_DECLARATION variableDeclaration);

        VD createVariableDeclaration(
                Context context,
                List<VMH> modifiers,
                TR type,
                String name,
                int numDims);
    }
    
    // Scope for variables
    private final ArrayStack<VariablesMap<VARIABLE_DECLARATION>> variableScopes;
    private final CreateDeclaration<
                VARIABLE_DECLARATION,
                VARIABLE_MODIFIER_HOLDER,
                TYPE_REFERENCE> createDeclaration;
    
    VariableScopesListener(
            CreateDeclaration<
                VARIABLE_DECLARATION,
                VARIABLE_MODIFIER_HOLDER,
                TYPE_REFERENCE> createDeclaration) {
            
        this.variableScopes = new ArrayStack<>();
        this.createDeclaration = createDeclaration;
    }

    @SuppressWarnings("unused")
    private boolean variableScopesContain(String name) {

        boolean contains = false;

        for (int i = variableScopes.size() - 1; i >= 0; --i) {
            if (variableScopes.get(i).hasVariable(name)) {
                contains = true;
                break;
            }
        }

        return contains;
    }

    protected final VARIABLE_DECLARATION findVariableDeclaration(String name) {
        Objects.requireNonNull(name);

        for (int i = variableScopes.size() - 1; i >= 0; --i) {
            final VARIABLE_DECLARATION variableDeclaration = variableScopes.get(i).findVariable(name);

            if (variableDeclaration != null) {
                return variableDeclaration;
            }
        }

        return null;
    }

    protected final void pushVariableScope() {
        variableScopes.push(new VariablesMap<>());
    }

    protected final void popVariableScope() {
        variableScopes.pop();
    }

    @SuppressWarnings("unused")
    private final VARIABLE_DECLARATION makeVariableDeclaration(Context context,
            BaseStackVariableDeclaration<VARIABLE_MODIFIER_HOLDER, TYPE_REFERENCE> stackDeclaration) {

        final VARIABLE_DECLARATION variableDeclaration = createDeclaration.createVariableDeclaration(
                context,
                stackDeclaration.getModifiers(),
                stackDeclaration.getTypeReference(),
                stackDeclaration.getName(),
                stackDeclaration.getNumDims());

        return variableDeclaration;
    }

    /*
     
    @Override
    public final void onStaticInitializerStart(int startContext) {

        final Context context = getStartContext(startContext);
        
        logEnter(context);

        // VARSCOPE pushVariableScope();

        push(new StackStaticInitializer<>(logger));

        logExit(context);
    }

    @Override
    public final void onStaticInitializerEnd(int startContext, Context endContext) {

        final Context context = getEndContext(startContext, endContext);
        
        logEnter(context);

        final StackStaticInitializer<STATEMENT> stackStaticInitializer = pop();

        final STATIC_INITIALIZER initializer = parseTreeFactory.createStaticInitializer(context,
                stackStaticInitializer.getList());

        final StackClass<COMPLEX_MEMBER_DEFINITION, CONSTRUCTOR_MEMBER, CLASS_METHOD_MEMBER> stackClass = get();

        stackClass.add(initializer);

        // VARSCOPE popVariableScope();

        logExit(context);
    }

     
    @Override
    public final void onConstructorStart(int startContext) {

        final Context context = getStartContext(startContext);
        
        logEnter(context);

        push(new StackConstructor<>(logger));

        // VARSCOPE pushVariableScope();

        logExit(context);
    }
     
    @Override
    public final void onConstructorEnd(int startContext, Context endContext) {

        final Context context = getEndContext(startContext, endContext);
        
        logEnter(context);

        // VARSCOPE popVariableScope();

        final StackConstructor<STATEMENT, PARAMETER, TYPE_REFERENCE, CONSTRUCTOR_MODIFIER_HOLDER> stackConstructor = pop();

        final CONSTRUCTOR_MEMBER constructorMember = parseTreeFactory.createConstructorMember(context,
                stackConstructor.getModifiers(), stackConstructor.getName(), stackConstructor.getNameContext(),
                stackConstructor.getParameters(), stackConstructor.getList());

        final ConstructorMemberSetter<CONSTRUCTOR_MEMBER> constructorMemberSetter = get();

        constructorMemberSetter.addConstructorMember(constructorMember);

        logExit(context);
    }

    @Override
    public final void onClassMethodStart(int startContext) {

        final Context context = getStartContext(startContext);
        
        logEnter(context);

        final StackClassMethod<STATEMENT, PARAMETER, TYPE_REFERENCE, CLASS_METHOD_MODIFIER_HOLDER> method
            = new StackClassMethod<>(logger);

        push(method);

        // VARSCOPE pushVariableScope();

        logExit(context);
    }

    @Override
    public final void onMethodSignatureParameterEnd(int startContext, Context endContext) {

        final Context context = getEndContext(startContext, endContext);
        
        logEnter(context);

        final StackParameterSignature<VARIABLE_MODIFIER_HOLDER, TYPE_REFERENCE> stackParameterSignature = pop();

        final CallableStackEntry<STATEMENT, PARAMETER, TYPE_REFERENCE> stackCallable = get();

        final PARAMETER parameter = parseTreeFactory.createParameter(context,
                stackParameterSignature.getTypeReference(), stackParameterSignature.getName(),
                stackParameterSignature.getNameContext(), stackParameterSignature.isVarArgs());

        stackCallable.addParameter(parameter);

        // VARSCOPE
        // final VARIABLE_DECLARATION variableDeclaration = makeVariableDeclaration(context, stackParameterSignature);

        // variableScopes.get().add(stackParameterSignature.getName(), variableDeclaration);

        logExit(context);
    }
    
    @Override
    public final void onClassMethodEnd(int startContext, Context endContext) {

        final Context context = getEndContext(startContext, endContext);
        
        logEnter(context);

        // VARSCOPE popVariableScope();

        final StackClassMethod<STATEMENT, PARAMETER, TYPE_REFERENCE, CLASS_METHOD_MODIFIER_HOLDER> method = pop();

        final ClassMethodMemberSetter<CLASS_METHOD_MEMBER> methodMemberSetter = get();
        
        final CLASS_METHOD_MEMBER methodMember = parseTreeFactory.createClassMethodMember(
                context,
                method.getModifiers(),
                method.getReturnType(),
                method.getName(),
                method.getNameContext(),
                method.getParameters(),
                method.getList());

        methodMemberSetter.addMethod(methodMember);

        logExit(context);
    }
    
    @Override
    public final void onInterfaceMethodStart(int startContext) {
        
        final Context context = getStartContext(startContext);

        logEnter(context);

        final StackInterfaceMethod<STATEMENT, PARAMETER, TYPE_REFERENCE, INTERFACE_METHOD_MODIFIER_HOLDER> method = new StackInterfaceMethod<>(logger);

        push(method);

        // VARSCOPE pushVariableScope();

        logExit(context);
    }

    @Override
    public final void onInterfaceMethodEnd(int startContext, Context endContext) {

        final Context context = getEndContext(startContext, endContext);
        
        logEnter(context);

        // VARSCOPE popVariableScope();

        final StackInterfaceMethod<STATEMENT, PARAMETER, TYPE_REFERENCE, INTERFACE_METHOD_MODIFIER_HOLDER> method = pop();

        final InterfaceMethodMemberSetter<INTERFACE_METHOD_MEMBER> methodMemberSetter = get();
        

        final INTERFACE_METHOD_MEMBER methodMember = parseTreeFactory.createInterfaceMethodMember(
                context,
                method.getModifiers(),
                method.getReturnType(),
                method.getName(),
                method.getNameContext(),
                method.getParameters(),
                method.getList());
        
        methodMemberSetter.addMethod(methodMember);

        logExit(context);
    }

    @Override
    public final void onVariableReference(int leafContext, long name) {

        final Context context = getLeafContext(leafContext);
        
        logEnter(context);

        final VariableReferenceSetter<VARIABLE_REFERENCE> variableReferenceSetter = get();

        // VARSCOPE
        // final VARIABLE_DECLARATION declaration = findVariableDeclaration(stringSource.asString(name));

        // if (declaration == null) {
        //     throw new CompileException(context, "No variable declared for name " + name);
        // }

        // final SIMPLE_VARIABLE_REFERENCE variableReference = parseTreeFactory.createSimpleVariableReference(context, declaration);

        // variableReferenceSetter.setVariableReference(variableReference);
        
        variableReferenceSetter.setVariableReference(parseTreeFactory.createNameReference(context, stringSource.asString(name)));

        logExit(context);
    }

    @Override
    public final void onMethodInvocationStart(
            int startContext,
            MethodInvocationType type,
            ScopedName classTypeName,
            int classTypeNameContext,
            ReferenceType referenceType,
            long methodName,
            int methodNameContext) {
        
        final Context context = getStartContext(startContext);

        logEnter(context);

        TYPE_REFERENCE classType = null;
        
        if (classTypeName != null && referenceType == ReferenceType.NAME) {
            classType = parseTreeFactory.createResolveLaterTypeReference(
                    getOtherContext(classTypeNameContext),
                    classTypeName,
                    referenceType);
            
            // VARSCOPE
            //if (variableScopesContain(classTypeName.getName())) {
                // Likely a scoped variable, eg. variable.invokeMethod() instead
                // of Class.invokeStaticMethod()

           //     if (type == MethodInvocationType.NAMED_CLASS_STATIC) {
           //         type = MethodInvocationType.VARIABLE_REFERENCE;
           //         classType = null;
           //     }
           // }
        }

        push(new StackMethodInvocation<>(
                logger,
                type,
                classType,
                stringSource.asString(methodName),
                getOtherContext(methodNameContext)));

        logExit(context);
    }

    @Override
    public final void onLambdaExpressionStart(int startContext) {
        
        final Context context = getStartContext(startContext);

        logEnter(context);

        push(new StackLambdaExpression<>(logger));

        // VARSCOPE pushVariableScope();

        logExit(context);
    }

    @Override
    public final void onLambdaExpressionEnd(int startContext, Context endContext) {
        
        final Context context = getEndContext(startContext, endContext);

        logEnter(context);

        final StackLambdaExpression<EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT> stackLambdaExpression = pop();

        final LAMBDA_EXPRESSION_PARAMETERS parameters;

        if (stackLambdaExpression.getSingleParameter() != null) {
            parameters = parseTreeFactory.createLambdaExpressionParameters(stackLambdaExpression.getSingleParameterContext(),
                    stackLambdaExpression.getSingleParameter());
        } else if (stackLambdaExpression.getInferredParameterList() != null) {
            parameters = parseTreeFactory.createLambdaExpressionParameters(stackLambdaExpression.getInferredParametersContext(),
                    stackLambdaExpression.getInferredParameterList());
        } else {
            throw new UnsupportedOperationException();
        }

        final LAMBDA_EXPRESSION lambdaExpression;

        final EXPRESSION expression = makeExpressionOrNull(context, stackLambdaExpression);

        if (expression != null) {
            lambdaExpression = parseTreeFactory.createSingleLambdaExpression(context, parameters, expression);
        } else {
            lambdaExpression = parseTreeFactory.createBlockLambdaExpression(context, parameters, stackLambdaExpression.getStatements());
        }

        final ExpressionSetter<EXPRESSION> expressionSetter = get();

        expressionSetter.addExpression(lambdaExpression);

        // VARSCOPE popVariableScope();

        logExit(context);
    }

    @Override
    public void onVariableDeclarationStatementEnd(int startContext, Context endContext) {
        
        final Context context = getEndContext(startContext, endContext);

        logEnter(context);

        final StackVariableDeclarationList<TYPE_REFERENCE, EXPRESSION, VARIABLE_MODIFIER_HOLDER> variableDeclaration = pop();

        final StatementSetter<STATEMENT> statementSetter = get();

        final List<INITIALIZER_VARIABLE_DECLARATION_ELEMENT> elements = variableDeclaration.getList().stream()
                .map(e -> createInitializer(e))
                .collect(Collectors.toList());
        
        final VARIABLE_DECLARATION_STATEMENT statement = parseTreeFactory.createVariableDeclarationStatement(
                context,
                variableDeclaration.getModifiers(),
                variableDeclaration.getTypeReference(),
                elements);

        // variableDeclaration.getList().forEach(e -> {
            
        //    final VARIABLE_DECLARATION var = parseTreeFactory.createVariableDeclaration(
        //            context,
        //            variableDeclaration.getModifiers(),
        //            variableDeclaration.getTypeReference(),
        //            e.getVarName(),
        //            e.getNumDims());
        //    
        //    variableScopes.get().add(e.getVarName(), var);
        // });

        statementSetter.addStatement(statement);

        logExit(context);
    }

    @Override
    public final void onIteratorForStatementStart(int startContext) {
        
        final Context context = getStartContext(startContext);

        logEnter(context);

        push(new StackIteratorForStatement<>(logger));

        // VARSCOPE pushVariableScope();

        logExit(context);
    }

    @Override
    public final void onIteratorForTestEnd(int startContext, Context endContext) {

        final Context context = getEndContext(startContext, endContext);
        
        logEnter(context);

        // VARSCOPE
        // final StackIteratorForStatement<VARIABLE_MODIFIER_HOLDER, TYPE_REFERENCE, EXPRESSION, PRIMARY, VARIABLE_REFERENCE, STATEMENT> stackIteratorForStatement = get();

        // Must add variable declarations to scope so that can be found further
        // down in parsing
        // final VARIABLE_DECLARATION variableDeclaration = makeVariableDeclaration(context, stackIteratorForStatement);

        // variableScopes.get().add(stackIteratorForStatement.getName(), variableDeclaration);

        logExit(context);
    }

    @Override
    public final void onIteratorForStatementEnd(int startContext, Context endContext) {

        final Context context = getEndContext(startContext, endContext);
        
        logEnter(context);

        // VARSCOPE popVariableScope();

        final StackIteratorForStatement<VARIABLE_MODIFIER_HOLDER, TYPE_REFERENCE, EXPRESSION, VARIABLE_REFERENCE, PRIMARY, STATEMENT> stackIteratorForStatement = pop();

        final ITERATOR_FOR_STATEMENT statement = parseTreeFactory.createIteratorForStatement(
                context,
                stackIteratorForStatement.getModifiers(),
                stackIteratorForStatement.getTypeReference(),
                stackIteratorForStatement.getName(),
                stackIteratorForStatement.getNameContext(),
                stackIteratorForStatement.getNumDims(),
                stackIteratorForStatement.getExpression(),
                stackIteratorForStatement.getStatements());

        final StatementSetter<STATEMENT> statementSetter = get();

        statementSetter.addStatement(statement);

        logExit(context);
    }

    @Override
    public final void onTryWithResourcesStatementStart(int startContext) {

        final Context context = getStartContext(startContext);
        
        logEnter(context);

        push(new StackTryWithResourcesStatement<>(logger));

        // VARSCOPE pushVariableScope(); // for the variables in resources

        logExit(context);
    }

    @Override
    public final void onResourceEnd(int startContext, Context endContext) {
        
        final Context context = getEndContext(startContext, endContext);

        logEnter(context);

        final StackResource<VARIABLE_MODIFIER_HOLDER, TYPE_REFERENCE, EXPRESSION, PRIMARY> stackResource = pop();

        final RESOURCE resource = parseTreeFactory.createResource(
                context,
                stackResource.getModifiers(),
                stackResource.getTypeReference(),
                stackResource.getName(),
                stackResource.getNameContext(),
                stackResource.getNumDims(),
                stackResource.getInitializer());

        // VARSCOPE
        // variableScopes.get().add(
        //        stackResource.getName(),
        //        parseTreeFactory.createVariableDeclaration(
        //                context,
        //                stackResource.getModifiers(),
        //                stackResource.getTypeReference(),
        //                stackResource.getName(),
        //                stackResource.getNumDims()));

        final StackResourceList<RESOURCE> stackResourceList = get();

        stackResourceList.add(resource);

        logExit(context);
    }

    @Override
    public final void onTryWithResourcesEnd(int startContext, Context endContext) {
        
        final Context context = getEndContext(startContext, endContext);

        logEnter(context);

        // VARSCOPE popVariableScope();

        final StackTryWithResourcesStatement<STATEMENT, CATCH_BLOCK, RESOURCE> stackTryWithResourcesStatement = pop();

        final TRY_WITH_RESOURCES statement = parseTreeFactory.createTryWithResourcesStatement(
                context,
                stackTryWithResourcesStatement.getResources(),
                stackTryWithResourcesStatement.getTryBlock().getList(),
                stackTryWithResourcesStatement.getCatchBlocks(),
                stackTryWithResourcesStatement.getFinallyBlock().getList());

        final StatementSetter<STATEMENT> statementSetter = get();

        statementSetter.addStatement(statement);

        logExit(context);
    }

    @Override
    public final void onIfStatementStart(int startContext, long ifKeyword, int ifKeywordContext) {
        
        final Context context = getStartContext(startContext);
        
        logEnter(context);
        
        push(new StackIfElseIfElse<>(getLogger()));
        push(new StackIfConditionBlock<>(
                getLogger(),
                context,
                stringSource.asString(ifKeyword),
                getOtherContext(ifKeywordContext)));
        
        // VARSCOPE pushVariableScope();
        
        logExit(context);
    }

    @Override
    public final void onIfStatementInitialBlockEnd(int startContext, Context endContext) {

        final Context context = getEndContext(startContext, endContext);
        
        logEnter(context);
        
        popAndAddIfConditionBlock(context);

        // VARSCOPE popVariableScope();
        
        logExit(context);
    }

    @Override
    public final void onElseIfStatementStart(
            int startContext,
            long elseKeyword, int elseKeywordContext,
            long ifKeyword, int ifKeywordContext) {
        
        final Context context = getStartContext(startContext);

        logEnter(context);
        
        push(new StackElseIfConditionBlock<>(
                getLogger(),
                context,
                stringSource.asString(elseKeyword),
                getOtherContext(elseKeywordContext),
                stringSource.asString(ifKeyword),
                getOtherContext(ifKeywordContext)));
        
        // pushVariableScope();
        
        logExit(context);
    }

    @Override
    public final void onElseIfStatementEnd(int startContext, Context endContext) {
        
        final Context context = getEndContext(startContext, endContext);

        logEnter(context);

        popAndAddElseIfConditionBlock(context);
        
        // popVariableScope();
        
        logExit(context);
    }

    @Override
    public final void onElseStatementEnd(int startContext, Context endContext) {
        
        final Context context = getEndContext(startContext, endContext);

        logEnter(context);
        
        final StackElseBlock<STATEMENT> stackBlock = pop();

        // popVariableScope();

        final StackIfElseIfElse<KEYWORD, CONDITION_BLOCK, BLOCK> ifElseIfElse = get();
        
        ifElseIfElse.setElseBlock(
                stackBlock.getKeyword() != null
                    ? parseTreeFactory.createKeyword(stackBlock.getKeywordContext(), stackBlock.getKeyword())
                    : null,
                parseTreeFactory.createBlock(context, stackBlock.getList()));
        
        logExit(context);
    }

    @Override
    public VariableDeclaration createVariableDeclaration(
            Context context,
            List<VariableModifierHolder> modifiers,
            TypeReference type,
            String name,
            int numDims) {
        
        return new VariableDeclaration(new VariableModifiers(modifiers), type, new VarName(name), numDims);
    }

    @Override
    public final VariableReference onSimpleVariableReference(SimpleVariableReference variableReference, T param) {

        return new SimpleVariableReference(
                variableReference.getContext(),
                mapVariableDeclaration(variableReference.getDeclaration(), param));
    }

    public final VariableDeclaration mapVariableDeclaration(VariableDeclaration variableDeclaration) {
        throw new UnsupportedOperationException();
    }

    */
}
