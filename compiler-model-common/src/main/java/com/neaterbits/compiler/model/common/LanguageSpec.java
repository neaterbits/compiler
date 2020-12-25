package com.neaterbits.compiler.model.common;

import java.util.Collection;
import java.util.List;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.types.FieldModifiers;
import com.neaterbits.compiler.types.imports.TypeImport;

public interface LanguageSpec {

    List<TypeImport> getImplicitImports();
    
    FieldModifiers getDefaultModifiers();
    
    Collection<TypeName> getBuiltinTypes();
    
}
