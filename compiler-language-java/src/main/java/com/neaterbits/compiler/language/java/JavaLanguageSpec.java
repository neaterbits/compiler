package com.neaterbits.compiler.language.java;

import java.util.List;

import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.resolver.LanguageSpec;
import com.neaterbits.compiler.resolver.passes.AddTypesAndMembersToCodeMapPass;
import com.neaterbits.compiler.resolver.passes.CodeMapCompiledAndMappedFiles;
import com.neaterbits.compiler.resolver.passes.FindTypeDependenciesPass;
import com.neaterbits.compiler.resolver.passes.LibraryTypes;
import com.neaterbits.compiler.resolver.passes.ReplaceResolvedTypeReferencesPass;
import com.neaterbits.compiler.resolver.passes.ResolveTypeDependenciesPass;
import com.neaterbits.compiler.resolver.passes.namereferenceresolve.NameReferenceResolvePass;
import com.neaterbits.compiler.util.CastFullContextProvider;
import com.neaterbits.compiler.util.FullContextProvider;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.imports.TypeImport;
import com.neaterbits.compiler.util.model.FieldModifiers;
import com.neaterbits.compiler.util.model.LibraryTypeRef;
import com.neaterbits.compiler.util.model.Mutability;
import com.neaterbits.compiler.util.model.ResolvedTypes;
import com.neaterbits.compiler.util.model.TypeSources;
import com.neaterbits.compiler.util.model.Visibility;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.compiler.util.passes.CompilerBuilderIntermediate;
import com.neaterbits.compiler.util.passes.CompilerModel;
import com.neaterbits.compiler.util.passes.FileParsePassInput;
import com.neaterbits.compiler.util.passes.LanguageCompiler;

public final class JavaLanguageSpec implements LanguageSpec {

    public static final JavaLanguageSpec INSTANCE = new JavaLanguageSpec();

    private static final FullContextProvider FULL_CONTEXT_PROVIDER = CastFullContextProvider.INSTANCE;

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
    public <COMPILATION_UNIT, PARSED_FILE extends ParsedFile>
               CompilerBuilderIntermediate<PARSED_FILE, FileParsePassInput<COMPILATION_UNIT>>
                    buildCompilerParsePass(CompilerModel<COMPILATION_UNIT, PARSED_FILE> model) {


        return buildCompilerParsePass(model, FULL_CONTEXT_PROVIDER);
    }

    @Override
    public <COMPILATION_UNIT, PARSED_FILE extends ParsedFile>
            LanguageCompiler<FileParsePassInput<COMPILATION_UNIT>, CodeMapCompiledAndMappedFiles<COMPILATION_UNIT>>
                makeCompilerPasses(
                        CompilerModel<COMPILATION_UNIT, PARSED_FILE> model,
                        ResolvedTypes resolvedTypes,
                        CompilerCodeMap codeMap) {
        
        // final JavaProgramModel compilationUnitModel = new JavaProgramModel(FULL_CONTEXT_PROVIDER);
        
        // final CompilationUnitModel<CompilationUnit> model = compilationUnitModel;
        
        final LibraryTypes libraryTypes = scopedName -> {
            final TypeName typeName = resolvedTypes.lookup(scopedName, TypeSources.LIBRARY);
            
            return typeName != null ? new LibraryTypeRef(typeName) : null;
        };
        
        return buildCompilerParsePass(model)
        
                .addSingleToMultiPass(new FindTypeDependenciesPass<COMPILATION_UNIT, PARSED_FILE>())
                
                .addMultiPass(new ResolveTypeDependenciesPass<COMPILATION_UNIT, PARSED_FILE>(
                            model.getImportsModel(),
                            JavaTypes.getBuiltinTypeRefs(),
                            libraryTypes,
                            model.getResolveTypesModel()))
                
                .addMultiPass(new ReplaceResolvedTypeReferencesPass<>(libraryTypes, model.getResolveTypesModel()))
                .addMultiPass(new AddTypesAndMembersToCodeMapPass<>(codeMap, model.getResolveTypesModel()))
                .addMultiPass(new NameReferenceResolvePass<>(model.getCompilationUnitModel()))
                
                .build();
    }
}
