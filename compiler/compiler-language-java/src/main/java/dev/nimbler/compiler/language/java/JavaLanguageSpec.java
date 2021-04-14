package dev.nimbler.compiler.language.java;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import dev.nimbler.compiler.model.common.LanguageSpec;
import dev.nimbler.compiler.parser.listener.common.CreateParserListener;
import dev.nimbler.compiler.types.imports.TypeImport;
import dev.nimbler.compiler.util.parse.Parser;
import dev.nimbler.language.common.types.FieldModifiers;
import dev.nimbler.language.common.types.Mutability;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.types.Visibility;

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

    @Override
    public <COMPILATION_UNIT> Parser<COMPILATION_UNIT> createParser(
                                CreateParserListener<COMPILATION_UNIT> createListener) {

        return new JavaRecursiveParser<>(createListener);
    }
}
