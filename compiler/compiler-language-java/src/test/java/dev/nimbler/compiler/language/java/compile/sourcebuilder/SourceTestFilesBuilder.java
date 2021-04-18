package dev.nimbler.compiler.language.java.compile.sourcebuilder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.jutils.parse.ParserException;

import dev.nimbler.build.strategies.compilemodules.ParsedWithCachedRefs;
import dev.nimbler.compiler.language.java.compile.CodeMapCompiledAndMappedFiles;
import dev.nimbler.compiler.language.java.compile.CompileFileCollector.CompileFile;
import dev.nimbler.compiler.resolver.ResolveError;
import dev.nimbler.compiler.resolver.build.CompileSource;
import dev.nimbler.compiler.resolver.build.CompilerOptions;
import dev.nimbler.compiler.resolver.build.ResolvedSourceModule;
import dev.nimbler.compiler.resolver.build.SourceBuilder;
import dev.nimbler.compiler.resolver.build.SourceModule;
import dev.nimbler.compiler.resolver.util.CompilerLanguage;
import dev.nimbler.compiler.util.FileSpec;
import dev.nimbler.compiler.util.FileSystemFileSpec;
import dev.nimbler.compiler.util.parse.ParsedFile;
import dev.nimbler.compiler.language.java.compile.CompiledAndResolvedFile;
import dev.nimbler.compiler.language.java.compile.CompiledAndResolvedFileImpl;
import dev.nimbler.compiler.language.java.compile.TestFilesBuilder;
import dev.nimbler.language.codemap.FieldInfo;
import dev.nimbler.language.codemap.compiler.IntCompilerCodeMap;
import dev.nimbler.language.common.types.TypeName;

public final class SourceTestFilesBuilder<COMPILATION_UNIT, PARSED_FILE extends ParsedFile>
    extends TestFilesBuilder<COMPILATION_UNIT, PARSED_FILE> {

    @Override
    protected CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> compileFiles(
            List<CompileFile> files,
            IntCompilerCodeMap codeMap,
            CompilerLanguage<COMPILATION_UNIT, PARSED_FILE> compilerLanguage)
                    throws IOException, ParserException {

        final SourceBuilder<COMPILATION_UNIT, PARSED_FILE> sourceBuilder
            = new SourceBuilder<>(
                    compilerLanguage.getLanguageSpec(),
                    compilerLanguage.getModel(),
                    new CompilerOptions(true));
        
        final List<CompileSource> compileSources = files.stream()
                .map(file -> new CompileSource(file.getText(), file.getName()))
                .collect(Collectors.toList());
        
        final SourceModule sourceModule = new SourceModule(
                                                compileSources,
                                                Charset.defaultCharset(),
                                                Collections.emptyList(),
                                                Collections.emptyList());
        
        final ResolvedSourceModule<PARSED_FILE> resolvedModule
            = sourceBuilder.compile(
                    sourceModule,
                    codeMap,
                    compileSource -> createFile(compileSource.getFileName()));
        
        return new CodeMapCompiledAndMappedFiles<COMPILATION_UNIT>() {

            @Override
            public Integer getTypeNo(TypeName typeName) {
                return codeMap.getTypeNoByTypeName(typeName);
            }

            @Override
            public FieldInfo getFieldInfo(TypeName typeName, String fieldName) {
                return getFieldInfo(codeMap, typeName, fieldName);
            }

            @Override
            public int getSourceFileNo(String name) {

                final ParsedWithCachedRefs<PARSED_FILE, ResolveError> parsed
                    = resolvedModule.getParsed(createFileSpec(name));
                
                return parsed.getCodeMapFileNo();
            }

            @Override
            public CompiledAndResolvedFile getFile(String name) {

                final ParsedWithCachedRefs<PARSED_FILE, ResolveError> parsed
                    = resolvedModule.getParsed(createFileSpec(name));
                
                return new CompiledAndResolvedFileImpl<COMPILATION_UNIT, PARSED_FILE>(parsed);
            }

            @Override
            public COMPILATION_UNIT getCompilationUnit(String name) {
                
                final ParsedWithCachedRefs<PARSED_FILE, ResolveError> parsed
                    = resolvedModule.getParsed(createFileSpec(name));

                return parsed.getParsedFile().getCompilationUnit();
            }
        };
    }
    
    private static File createFile(String name) {
        return new File(name);
    }
    
    private static FileSpec createFileSpec(String name) {
        
        return new FileSystemFileSpec(createFile(name));
    }
}
