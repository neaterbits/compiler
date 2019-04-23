package com.neaterbits.compiler.resolver.ast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.Module;
import com.neaterbits.compiler.ast.Program;
import com.neaterbits.compiler.ast.parser.ASTParsedFile;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.resolver.ResolveFilesResult;
import com.neaterbits.compiler.resolver.ast.model.ObjectProgramModel;
import com.neaterbits.compiler.resolver.passes.ResolveTypeDependenciesPass;
import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.ImportsModel;
import com.neaterbits.compiler.util.model.ResolvedTypes;
import com.neaterbits.compiler.util.modules.ModuleId;
import com.neaterbits.compiler.util.modules.ModuleSpec;
import com.neaterbits.compiler.util.modules.SourceModuleSpec;
import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.compiler.util.parse.ParseError;
import com.neaterbits.compiler.util.parse.Parser;

public class BuildAndResolve {

	@FunctionalInterface
	public interface GetInputStream<I> {
		InputStream getInputStream(I input) throws IOException;
	}
	
	public static <INPUT> BuildAndResolveResult parseAndResolve(
			Parser<CompilationUnit> parser,
			Collection<INPUT> inputs,
			GetInputStream<INPUT> getInputStream,
			Function<INPUT, FileSpec> getFileSpec,
			Consumer<ASTParsedFile> onParsedFile,
			ObjectProgramModel programModel,
			Collection<BuiltinType> builtinTypes,
			ResolvedTypes resolvedTypes) throws IOException {

		final List<ASTParsedFile> parsedFiles = new ArrayList<>(inputs.size());
		final List<CompiledFile<ComplexType<?, ?, ?>, CompilationUnit>> allFiles = new ArrayList<>(inputs.size());

		for (INPUT input : inputs) {
			
			try (InputStream inputStream = getInputStream.getInputStream(input)) {
			
				final FileSpec fileSpec = getFileSpec.apply(input);
				
				final ASTParsedFile parsedFile = BuildAndResolve.parseFile(parser, inputStream, fileSpec, resolvedTypes);
				
				parsedFiles.add(parsedFile);
				
				onParsedFile.accept(parsedFile);
				
				allFiles.add(ProgramLoader.makeCompiledFile(parsedFile));
			}
		}
		
		final ResolveFilesResult<BuiltinType, ComplexType<?, ?, ?>, TypeName> resolveFilesResult
				= BuildAndResolve.resolveParsedFiles(allFiles, programModel, builtinTypes, resolvedTypes);
		
		return new BuildAndResolveResult(parsedFiles, resolveFilesResult);
	}
	
	public static ASTParsedFile parseFile(
			Parser<CompilationUnit> parser,
			InputStream inputStream,
			FileSpec fileSpec,
			ResolvedTypes resolvedTypes) throws IOException {
		
		final List<ParseError> errors = new ArrayList<>();
		
		final CompilationUnit compilationUnit = parser.parse(inputStream, errors, fileSpec.getParseContextName(), null);
		
		if (compilationUnit == null) {
			throw new IllegalStateException();
		}
		
		final List<CompileError> compileErrors = errors.stream().map(error -> (CompileError)error).collect(Collectors.toList());
		
		final ASTParsedFile parsedFile = new ASTParsedFile(
				fileSpec,
				compileErrors,
				null,
				compilationUnit);
		
		return parsedFile;
	}
	
	public static <T> void resolveParsedModule(
			T modulePath,
			List<T> dependencies,
			Function<T, ModuleId> getModuleId,
			Function<T, File> getFile,
			Collection<ASTParsedFile> compilationUnits,
			ObjectProgramModel programModel,
			Collection<BuiltinType> builtinTypes,
			ResolvedTypes resolvedTypes) {
		
		final ModuleSpec moduleSpec = new SourceModuleSpec(
				getModuleId.apply(modulePath),
				dependencies.stream()
					.map(dependency -> new SourceModuleSpec(getModuleId.apply(dependency), null, getFile.apply(dependency)))
					.collect(Collectors.toList()),
				getFile.apply(modulePath));
		
		final Module module = new Module(moduleSpec, compilationUnits);
		
		final Program program = new Program(module);

		final Collection<CompiledFile<ComplexType<?, ?, ?>, CompilationUnit>> allFiles = ProgramLoader.getCompiledFiles(program);
		
		resolveParsedFiles(allFiles, programModel, builtinTypes, resolvedTypes);
	}

	public static ResolveFilesResult<BuiltinType, ComplexType<?, ?, ?>, TypeName> resolveParsedFiles(
			
			Collection<CompiledFile<ComplexType<?, ?, ?>, CompilationUnit>> allFiles,
			ImportsModel<CompilationUnit> importsModel,
			Collection<BuiltinType> builtinTypes,
			ResolvedTypes resolvedTypes) {
		
		return ResolveTypeDependenciesPass.resolveParsedFiles(allFiles, importsModel, builtinTypes, resolvedTypes::lookup, new ASTModelImpl());
	}

}
