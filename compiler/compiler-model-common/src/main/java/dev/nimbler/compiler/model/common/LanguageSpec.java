package dev.nimbler.compiler.model.common;

import java.util.Collection;
import java.util.List;

import dev.nimbler.compiler.parser.listener.common.CreateParserListener;
import dev.nimbler.compiler.types.imports.TypeImport;
import dev.nimbler.compiler.util.parse.Parser;
import dev.nimbler.language.common.types.FieldModifiers;
import dev.nimbler.language.common.types.TypeName;

public interface LanguageSpec {
    
    List<TypeImport> getImplicitImports();
    
    FieldModifiers getDefaultModifiers();
    
    Collection<TypeName> getBuiltinTypes();

    <COMPILATION_UNIT> Parser<COMPILATION_UNIT> createParser(
            CreateParserListener<COMPILATION_UNIT> createListener);

}
