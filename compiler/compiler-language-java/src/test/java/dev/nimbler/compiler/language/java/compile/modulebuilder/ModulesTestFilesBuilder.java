package dev.nimbler.compiler.language.java.compile.modulebuilder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jutils.IOUtils;
import org.jutils.parse.ParserException;

import dev.nimbler.build.strategies.compilemodules.CompileModule;
import dev.nimbler.build.strategies.compilemodules.ParsedWithCachedRefs;
import dev.nimbler.build.strategies.compilemodules.ResolvedModule;
import dev.nimbler.build.types.ModuleId;
import dev.nimbler.build.types.language.Language;
import dev.nimbler.build.types.resource.ModuleResource;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFileHolderResourcePath;
import dev.nimbler.build.types.resource.SourceFileResource;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResource;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;
import dev.nimbler.compiler.language.java.compile.CodeMapCompiledAndMappedFiles;
import dev.nimbler.compiler.language.java.compile.CompileFileCollector.CompileFile;
import dev.nimbler.compiler.model.common.passes.ParsedFiles;
import dev.nimbler.compiler.resolver.ResolveError;
import dev.nimbler.compiler.resolver.build.CompilerOptions;
import dev.nimbler.compiler.resolver.build.ModulesBuilder;
import dev.nimbler.compiler.resolver.util.CompilerLanguage;
import dev.nimbler.compiler.util.FileSpec;
import dev.nimbler.compiler.util.FileSystemFileSpec;
import dev.nimbler.compiler.util.parse.ParsedFile;
import dev.nimbler.compiler.language.java.compile.CompiledAndResolvedFile;
import dev.nimbler.compiler.language.java.compile.CompiledAndResolvedFileImpl;
import dev.nimbler.compiler.language.java.compile.TestFilesBuilder;
import dev.nimbler.language.codemap.compiler.IntCompilerCodeMap;

public final class ModulesTestFilesBuilder<COMPILATION_UNIT, PARSED_FILE extends ParsedFile>
    extends TestFilesBuilder<COMPILATION_UNIT, PARSED_FILE> {

    @Override
    protected CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> compileFiles(
            List<CompileFile> files,
            IntCompilerCodeMap codeMap,
            CompilerLanguage<COMPILATION_UNIT, PARSED_FILE> compilerLanguage)
    
            throws IOException, ParserException {
        
        Objects.requireNonNull(files);
        Objects.requireNonNull(codeMap);
        
        final Map<String, FileSpec> fileSpecMap = new HashMap<>(files.size());
        final Map<FileSpec, String> fileToName = new HashMap<>(files.size());
        
        final CompileModule toCompile = makeCompileModule(files, fileSpecMap, fileToName);
        
        final ModulesBuilder<COMPILATION_UNIT, PARSED_FILE> compiler
                = new ModulesBuilder<>(
                        compilerLanguage.getLanguageSpec(),
                        compilerLanguage.getModel(),
                        new CompilerOptions(true));
        
        final ResolvedModule<PARSED_FILE, ResolveError> resolved
            = compiler.compile(toCompile, codeMap);
        
        if (resolved == null) {
            throw new IllegalStateException();
        }
        
        final Map<String, Integer> sourceFileNos = new HashMap<>();
        
        final List<ParsedWithCachedRefs<PARSED_FILE, ResolveError>> moduleParsed
            = resolved.getParsedModule().getParsed();

        final List<PARSED_FILE> list = new ArrayList<>(moduleParsed.size());

        for (ParsedWithCachedRefs<PARSED_FILE, ResolveError> p : moduleParsed) {

            list.add(p.getParsedFile());
            
            final String fileName = fileToName.get(p.getParsedFile().getFileSpec());

            if (fileName == null) {
                throw new IllegalStateException();
            }

            sourceFileNos.put(fileName, p.getCodeMapFileNo());
        }
        
        final ParsedFiles<PARSED_FILE> parsedFiles = new ParsedFiles<>(list);
        
        final ModuleMappedFiles<PARSED_FILE, COMPILATION_UNIT> mappedFiles
            = new ModuleMappedFiles<PARSED_FILE, COMPILATION_UNIT>(parsedFiles, sourceFileNos, codeMap) {
              
                @Override
                public CompiledAndResolvedFile getFile(String name) {
                    
                    final FileSpec fileSystemFileSpec = fileSpecMap.get(name);
                    
                    final ParsedWithCachedRefs<PARSED_FILE, ResolveError> parsed = moduleParsed.stream()
                            .filter(p -> p.getParsedFile().getFileSpec().equals(fileSystemFileSpec))
                            .findFirst()
                            .get();
                    
                    return new CompiledAndResolvedFileImpl<>(parsed);
                }
            };
        
        return mappedFiles;
    }

    private CompileModule makeCompileModule(
            List<CompileFile> files,
            Map<String, FileSpec> fileSpecMap,
            Map<FileSpec, String> fileToName) throws IOException {
        
        final List<File> f = new ArrayList<>(files.size());
        
        for (CompileFile compileFile : files) {

            final File file = File.createTempFile("sourcefile", "test");

            file.deleteOnExit();

            IOUtils.write(file, compileFile.getText());
            
            f.add(file);
            
            final FileSystemFileSpec fileSpec = new FileSystemFileSpec(file);
            final String fileName = compileFile.getName();
            
            fileSpecMap.put(fileName, fileSpec);
            fileToName.put(fileSpec, fileName);
        }
        
        final File directory = f.get(0).getParentFile();

        final ModuleId moduleId = new ModuleId("test-module");

        final ProjectModuleResourcePath projectModuleResourcePath
            = new ProjectModuleResourcePath(
                    Arrays.asList(
                            new ModuleResource(moduleId, directory)));
        
        final SourceFolderResource sourceFolderResource
            = new SourceFolderResource(directory, directory.getName(), Language.JAVA);
        
        final SourceFileHolderResourcePath sourceHolderResourcePath
            = new SourceFolderResourcePath(
                    projectModuleResourcePath,
                    sourceFolderResource);
        
        final CompileModule compileModule = new CompileModule(
                projectModuleResourcePath,
                f.stream()
                    .map(path -> new SourceFileResourcePath(
                            sourceHolderResourcePath,
                            new SourceFileResource(path)))
                    .collect(Collectors.toList()),
                Charset.defaultCharset(),
                Collections.emptyList(),
                Collections.emptyList());
        
        return compileModule;
    }
}
