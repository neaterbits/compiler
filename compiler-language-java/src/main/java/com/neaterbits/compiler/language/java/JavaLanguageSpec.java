package com.neaterbits.compiler.language.java;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.model.common.LanguageSpec;
import com.neaterbits.compiler.types.FieldModifiers;
import com.neaterbits.compiler.types.Mutability;
import com.neaterbits.compiler.types.Visibility;
import com.neaterbits.compiler.types.imports.TypeImport;

public final class JavaLanguageSpec implements LanguageSpec {

    public static final JavaLanguageSpec INSTANCE = new JavaLanguageSpec();
    
    private static final Collection<TypeName> BUILTIN_TYPES
            = JavaTypes.getBuiltinTypes().stream()
                    .map(type -> type.getTypeName())
                    .collect(Collectors.toUnmodifiableList());

    private JavaLanguageSpec() {

    }

    @Override
    public List<TypeImport> getImplicitImports() {

        return JavaTypes.getImplicitImports();
    }

    @Override
    public FieldModifiers getDefaultModifiers() {

        return new FieldModifiers(
                false,
                Visibility.NAMESPACE,
                Mutability.MUTABLE,
                false,
                false);
    }

    @Override
    public Collection<TypeName> getBuiltinTypes() {
        
        return BUILTIN_TYPES;
    }
}
