package dev.nimbler.compiler.resolver.passes;

import java.util.Objects;

import org.jutils.ArrayStack;
import org.jutils.Stack;

import dev.nimbler.language.codemap.compiler.CompilerCodeMap;
import dev.nimbler.language.common.types.TypeName;

public abstract class BaseVariableDeclaratorVisitor<T extends ScopeVariableDeclaration>
        extends BaseScopesVisitor<T> {

    private final CompilerCodeMap codeMap;
    
    // Cache type until each of the names, eg. int i, j, k;
    // where int type is part of statement while i, j and k are declarators
    private final Stack<PrimaryType> variableDeclaration;

    protected BaseVariableDeclaratorVisitor(CompilerCodeMap codeMap) {
    
        Objects.requireNonNull(codeMap);

        this.codeMap = codeMap;
        
        this.variableDeclaration = new ArrayStack<>();
    }
    
    @Override
    public final void onScopeVariableDeclarationStatementStart(
            int variableDeclarationStatementParseTreeRef,
            int typeNo,
            int typeReferenceParseTreeRef) {
        
        final TypeName typeName = codeMap.getTypeName(typeNo);
        
        Objects.requireNonNull(typeName);

        variableDeclaration.push(new PrimaryType(typeName, typeReferenceParseTreeRef));
    }
    
    protected abstract T createVariableDeclaration(
            int variableDeclarationParseTreeRef,
            TypeName type,
            int typeReferenceParseTreeRef);

    @Override
    public final void onScopeVariableDeclarator(int variableDeclaratorParseTreeRef, String name) {

        Objects.requireNonNull(name);

        final VariableScope<T> scope = getCurrentScope();

        final PrimaryType primaryType = variableDeclaration.get();
        
        scope.addVariableDeclaration(
                variableDeclaratorParseTreeRef,
                name,
                createVariableDeclaration(
                        variableDeclaratorParseTreeRef,
                        primaryType.getTypeName(),
                        primaryType.getTypeReferenceParseTreeRef()));
    }

    @Override 
    public final void onScopeVariableDeclarationStatementEnd(int variableDeclarationStatementParseTreeRef) {

        variableDeclaration.pop();
    }
}
