package com.neaterbits.compiler.java;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.neaterbits.build.strategies.compilemodules.CompileModule;
import com.neaterbits.build.types.ModuleId;
import com.neaterbits.build.types.language.Language;
import com.neaterbits.build.types.resource.ModuleResource;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.SourceFileHolderResourcePath;
import com.neaterbits.build.types.resource.SourceFileResource;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResource;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;
import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.compiler.java.resolve.TestResolvedTypes;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.NameFileSpec;
import com.neaterbits.util.IOUtils;
import com.neaterbits.util.parse.ParserException;

public class CompileFileCollector<COMPILATION_UNIT> {
    
    @FunctionalInterface
    public interface Compiler<COMPILED_AND_MAPPED_FILES> {
        
        COMPILED_AND_MAPPED_FILES compile(
                    CompileModule parseInputs,
                    IntCompilerCodeMap codeMap) throws IOException, ParserException;
    }

	private static class CompileFile {
		private final String text;
		
		CompileFile(String text) {
			this.text = text;
		}
	}
	
	private final Compiler<CodeMapCompiledAndMappedFiles<COMPILATION_UNIT>> compiler;
	
	private final List<CompileFile> files;

	public CompileFileCollector(Compiler<CodeMapCompiledAndMappedFiles<COMPILATION_UNIT>> compiler) {
	    
	    Objects.requireNonNull(compiler);
	    
	    this.compiler = compiler;
	    
		this.files = new ArrayList<>();
	}
	
	public CompileFileCollector<COMPILATION_UNIT> add(FileSpec name, String text) {

		files.add(new CompileFile(text));
		
		return this;
	}

	public CompileFileCollector<COMPILATION_UNIT> add(String name, String text) {

		return add(new NameFileSpec(name), text);
	}

	public CompiledAndMappedFiles compile(TestResolvedTypes resolvedTypes) throws IOException, ParserException {
		return compile(resolvedTypes, new IntCompilerCodeMap());
	}

	public CodeMapCompiledAndMappedFiles<COMPILATION_UNIT>
		compile(TestResolvedTypes resolvedTypes, IntCompilerCodeMap codeMap)
		        throws IOException, ParserException {
		    
		final List<File> f = new ArrayList<>(files.size());
		
		for (CompileFile compileFile : files) {

		    final File file = File.createTempFile("sourcefile", "test");

		    file.deleteOnExit();

		    IOUtils.write(file, compileFile.text);
		    
		    f.add(file);
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
		        Charset.defaultCharset(),
		        f.stream()
		            .map(path -> new SourceFileResourcePath(
		                    sourceHolderResourcePath,
		                    new SourceFileResource(path)))
		            .collect(Collectors.toList()),
		        Collections.emptyList(),
		        Collections.emptyList());
		
		return compiler.compile(compileModule, codeMap);
	}
}
