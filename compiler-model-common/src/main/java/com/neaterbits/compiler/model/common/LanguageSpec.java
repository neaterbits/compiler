package com.neaterbits.compiler.model.common;

import java.util.Collection;
import java.util.List;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.parser.listener.common.CreateParserListener;
import com.neaterbits.compiler.types.FieldModifiers;
import com.neaterbits.compiler.types.imports.TypeImport;
import com.neaterbits.compiler.util.parse.Parser;

public interface LanguageSpec {
    
    List<TypeImport> getImplicitImports();
    
    FieldModifiers getDefaultModifiers();
    
    Collection<TypeName> getBuiltinTypes();

    <COMPILATION_UNIT> Parser<COMPILATION_UNIT> createParser(
            CreateParserListener<COMPILATION_UNIT> createListener);

}
