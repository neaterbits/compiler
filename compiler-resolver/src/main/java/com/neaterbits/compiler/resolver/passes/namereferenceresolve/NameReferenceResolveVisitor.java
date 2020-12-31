package com.neaterbits.compiler.resolver.passes.namereferenceresolve;

import java.util.List;
import java.util.Objects;

import com.neaterbits.build.types.ScopedName;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.build.types.TypesMap;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.model.common.CompilationUnitModel;
import com.neaterbits.compiler.model.common.UnresolvedScopesListener;
import com.neaterbits.compiler.model.common.util.ScopedNameResolver;
import com.neaterbits.compiler.resolver.ResolveError;
import com.neaterbits.compiler.resolver.UnknownReferenceError;
import com.neaterbits.compiler.resolver.passes.BaseVariableDeclaratorVisitor;
import com.neaterbits.compiler.resolver.passes.ScopeVariableDeclaration;
import com.neaterbits.compiler.resolver.passes.namereferenceresolve.PrimaryListEntryPart.PartType;
import com.neaterbits.compiler.types.FieldInfo;

final class NameReferenceResolveVisitor<COMPILATION_UNIT>
        extends BaseVariableDeclaratorVisitor<ScopeVariableDeclaration>
        implements UnresolvedScopesListener {

    private final CompilationUnitModel<COMPILATION_UNIT> compilationUnitModel;
    private final CompilerCodeMap codeMap;
    private final TypesMap<TypeName> typesMap;
    private final COMPILATION_UNIT compilationUnit;
    private final List<ResolveError> errors;
    
    private final Primaries primaries;
    
    NameReferenceResolveVisitor(
            CompilationUnitModel<COMPILATION_UNIT> compilationUnitModel,
            CompilerCodeMap codeMap,
            TypesMap<TypeName> typesMap,
            COMPILATION_UNIT compilationUnit,
            List<ResolveError> errors) {
        
        super(codeMap);
        
        Objects.requireNonNull(compilationUnitModel);
        Objects.requireNonNull(codeMap);
        Objects.requireNonNull(typesMap);
        Objects.requireNonNull(compilationUnit);
        Objects.requireNonNull(errors);
        
        this.compilationUnitModel = compilationUnitModel;
        this.codeMap = codeMap;
        this.typesMap = typesMap;
        this.compilationUnit = compilationUnit;
        this.errors = errors;

        this.primaries = new Primaries();
    }

    @Override
    protected ScopeVariableDeclaration createVariableDeclaration(
            int variableDeclarationParseTreeRef,
            TypeName type,
            int typeReferenceParseTreeRef) {

        return new ScopeVariableDeclaration(type, typeReferenceParseTreeRef);
    }

    @Override
    public <T> T onPrimaryListStart(T primaryList, int primaryListParseTreeRef, int size) {

        primaries.push(new PrimaryListEntry(primaryListParseTreeRef));
        
        return null;
    }

    @Override
    public <T> void onPrimaryListEnd(T primaryList, int primaryListParseTreeRef) {

        primaries.pop();
    }

    private FieldInfo getField(String name, PrimaryListEntry entry) {
        
        final TypeName lastType = entry.getLastType();

        // See if this is a field within the type
        final int typeNo = codeMap.getTypeNoByTypeName(lastType);
        
        final FieldInfo field = codeMap.getFieldInfo(typeNo, name);
        
        return field;
    }
    
    private void addField(String name, PrimaryListEntry entry, int namePrimaryParseTreeRef) {
        
        // Field
        final FieldInfo field = getField(name, entry);

        if (field != null) {
            // Replace with field access
            compilationUnitModel.replaceNamePrimaryWithFieldAccess(
                    compilationUnit,
                    namePrimaryParseTreeRef,
                    entry.getLastTypeParseTreeRef(),
                    name);
        }
        else {
            errors.add(new UnknownReferenceError("Unknown field " + name));
        }
    }

    @Override
    public void onUnresolvedNamePrimary(int namePrimaryParseTreeRef, String name) {

        final PrimaryListEntry entry = primaries.getEntry();
        
        if (entry.isEmpty()) {
            
            // Initial entry, e.g. a in a.b.c
            final ScopeVariableDeclaration declaration = resolveVariableDeclaration(name);
            
            if (declaration != null) {

                entry.addType(
                        PartType.NAME,
                        declaration.getType(),
                        declaration.getTypeReferenceParseTreeRef());
                
                compilationUnitModel.replaceNamePrimaryWithNameReference(
                        compilationUnit,
                        namePrimaryParseTreeRef,
                        name);
            }
            else {

                // Might be class name, e.g. static reference, resolve and replace
                final ScopedName scopedName = ScopedName.makeScopedName(name);
                
                final TypeName typeName = ScopedNameResolver.resolveScopedName(
                        scopedName,
                        compilationUnit,
                        compilationUnitModel,
                        getCurrentNamespace(),
                        typesMap);
                
                if (typeName != null) {
                    // static reference e.g. class part of Class.field or Class.field.method()
                    entry.addType(
                            PartType.STATIC_CLASS,
                            typeName,
                            namePrimaryParseTreeRef);
                }
                else {
                    // Unknown reference
                    errors.add(new UnknownReferenceError("Unknown reference " + name));
                }
            }
        }
        else {
            
            // Field, e.g. b in a.b.c, must be relative to last type
            
            switch (entry.getLastPartType()) {
            case NAME:
                if (entry.size() != 1) {
                    throw new IllegalStateException();
                }

                addField(name, entry, namePrimaryParseTreeRef);
                break;
                
            case STATIC_CLASS:
                // Static field or inner class
                if (entry.size() != 1) {
                    throw new IllegalStateException();
                }

                final FieldInfo fieldInfo = getField(name, entry);
                if (fieldInfo != null) {
                    
                    entry.addType(
                            PartType.STATIC_FIELD_ACCESS,
                            entry.getLastType(),
                            entry.getLastTypeParseTreeRef());
                    
                    compilationUnitModel.replaceNamePrimaryWithStaticReference(
                                    compilationUnit,
                                    namePrimaryParseTreeRef,
                                    entry.getLastTypeParseTreeRef(),
                                    name);
                }
                else {
                    // Could be an inner class
                    throw new UnsupportedOperationException();
                }
                break;

                
            case FIELD:
                // Field or method
                addField(name, entry, namePrimaryParseTreeRef);
                break;
                
            default:
                throw new IllegalStateException();
            }
        }
    }
}
