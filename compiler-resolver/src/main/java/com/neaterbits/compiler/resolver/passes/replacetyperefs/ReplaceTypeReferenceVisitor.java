package com.neaterbits.compiler.resolver.passes.replacetyperefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.types.ScopedName;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.build.types.TypesMap;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.model.common.CompilationUnitModel;
import com.neaterbits.compiler.model.common.TypeReferenceVisitor;
import com.neaterbits.compiler.model.common.util.ScopedNameResolver;
import com.neaterbits.compiler.resolver.ResolveError;
import com.neaterbits.compiler.resolver.UnknownReferenceError;

final class ReplaceTypeReferenceVisitor<COMPILATION_UNIT> implements TypeReferenceVisitor<COMPILATION_UNIT> {

    private final CompilationUnitModel<COMPILATION_UNIT> compilationUnitModel;
    private final CompilerCodeMap codeMap;

    private final TypesMap<TypeName> compiledTypesMap;
    private final List<ResolveError> errors;

    private final List<String> currentNamespace;

    ReplaceTypeReferenceVisitor(
            CompilationUnitModel<COMPILATION_UNIT> compilationUnitModel,
            CompilerCodeMap codeMap,
            TypesMap<TypeName> compiledTypesMap,
            List<ResolveError> errors) {

        Objects.requireNonNull(compilationUnitModel);
        Objects.requireNonNull(codeMap);
        Objects.requireNonNull(compiledTypesMap);
        Objects.requireNonNull(errors);

        this.compilationUnitModel = compilationUnitModel;
        this.codeMap = codeMap;
        this.compiledTypesMap = compiledTypesMap;
        this.errors = errors;

        this.currentNamespace = new ArrayList<>();
    }

    @Override
    public void onNamespaceStart() {
        
        if (!currentNamespace.isEmpty()) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void onNamespacePart(CharSequence part) {

        currentNamespace.add(part.toString());
    }

    @Override
    public void onNamespaceEnd() {

        currentNamespace.clear();
    }

    @Override
    public void onResolvedTypeReference(COMPILATION_UNIT compilationUnit, int parseTreeElement, int typeNo) {

        // Already resolved reference so skip
    }

    @Override
    public void onNonScopedTypeReference(COMPILATION_UNIT compilationUnit, int parseTreeElement, CharSequence name) {

        replaceTypeReference(
                compilationUnit,
                parseTreeElement,
                ScopedName.makeScopedName(name.toString()));
    }

    @Override
    public void onScopedTypeReference(COMPILATION_UNIT compilationUnit, int parseTreeElement, ScopedName scopedName) {

        replaceTypeReference(compilationUnit, parseTreeElement, scopedName);
    }

    private void replaceTypeReference(COMPILATION_UNIT compilationUnit, int parseTreeElement, ScopedName scopedName) {

        final TypeName typeName = ScopedNameResolver.resolveScopedName(
                scopedName,
                compilationUnit,
                compilationUnitModel,
                currentNamespace,
                compiledTypesMap);
        
        System.out.println("## replace from " + compiledTypesMap);

        if (typeName == null) {
            unresolvedReference(scopedName);
        }
        else {
            replaceTypeReference(compilationUnit, parseTreeElement, typeName);
        }
    }

    private void replaceTypeReference(COMPILATION_UNIT compilationUnit, int parseTreeElement, TypeName typeName) {

        Objects.requireNonNull(typeName);

        final int typeNo = codeMap.getTypeNoByTypeName(typeName);

        if (typeNo < 0) {
            unresolvedReference(typeName);
        }
        else {
            
            // Non-scoped reference, replace with type
            compilationUnitModel.replaceTypeReference(compilationUnit, parseTreeElement, typeNo, typeName);
        }
    }

    private void unresolvedReference(ScopedName scopedName) {

        System.out.println("## unresolved ScopedName " + scopedName);
        
        errors.add(new UnknownReferenceError("No matching class for name " + scopedName.getDebugString()));
    }

    private void unresolvedReference(TypeName typeName) {

        errors.add(new UnknownReferenceError("Matching class not found in code map for name "
                + typeName.toDebugString()));
    }
}
