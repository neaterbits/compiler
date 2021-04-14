package dev.nimbler.compiler.model.common;

import java.util.List;

import dev.nimbler.compiler.types.imports.TypeImport;

public abstract class BaseImportsModel<COMPILATION_UNIT>
    implements ImportsModel<COMPILATION_UNIT> {

    private final List<TypeImport> implicitImports;
    
    public BaseImportsModel(List<TypeImport> implicitImports) {
        this.implicitImports = implicitImports;
    }

    @Override
    public final List<TypeImport> getImplicitImports() {
        return implicitImports;
    }
}
