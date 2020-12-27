package com.neaterbits.compiler.resolver.build;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.strategies.compilemodules.AllModulesBuildContext;
import com.neaterbits.build.strategies.compilemodules.AllModulesCompiler;
import com.neaterbits.build.strategies.compilemodules.CompileModule;
import com.neaterbits.build.strategies.compilemodules.ResolveModule;
import com.neaterbits.build.strategies.compilemodules.ResolvedModule;
import com.neaterbits.build.strategies.compilemodules.TargetBuilderAllModules;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.compiler.codemap.compiler.SynchronizedCompilerCodeMap;
import com.neaterbits.compiler.model.common.LanguageSpec;
import com.neaterbits.compiler.model.common.passes.CompilerModel;
import com.neaterbits.compiler.resolver.ResolveError;
import com.neaterbits.compiler.resolver.build.strategies.compilemodules.AllModulesCompilerImpl;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.compiler.util.parse.Parser;
import com.neaterbits.structuredlog.binary.logging.LogContext;
import com.neaterbits.util.Value;
import com.neaterbits.util.concurrency.dependencyresolution.executor.TargetBuildResult;
import com.neaterbits.util.concurrency.scheduling.QueueAsyncExecutor;
import com.neaterbits.util.parse.ParserException;

public final class LanguageCompiler<COMPILATION_UNIT, PARSED_FILE extends ParsedFile> {

    private final Parser<COMPILATION_UNIT> parser;
    private final CompilerModel<COMPILATION_UNIT, PARSED_FILE> compilerModel;
    private final CompilerOptions options;
    
	public LanguageCompiler(
	        Parser<COMPILATION_UNIT> parser,
	        CompilerModel<COMPILATION_UNIT, PARSED_FILE> compilerModel,
	        CompilerOptions options) {
		
	    Objects.requireNonNull(parser);
	    Objects.requireNonNull(compilerModel);
	    Objects.requireNonNull(options);
	    
	    this.parser = parser;
	    this.compilerModel = compilerModel;
	    this.options = options;
	}

	public ResolvedModule<PARSED_FILE, ResolveError> compile(
	        CompileModule module,
	        IntCompilerCodeMap codeMap) throws IOException, ParserException {
	    
	    Objects.requireNonNull(module);
	    
	    return compile(Arrays.asList(module), codeMap).get(0);
	}

    public List<ResolvedModule<PARSED_FILE, ResolveError>>
    compile(List<CompileModule> inputs, IntCompilerCodeMap intCodeMap) {
		
		Objects.requireNonNull(inputs);
		
		if (inputs.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		final SynchronizedCompilerCodeMap codeMap = new SynchronizedCompilerCodeMap(intCodeMap);

		final TargetBuilderAllModules<PARSED_FILE, SynchronizedCompilerCodeMap, ResolveError> targetBuilderAllModules
		        = new TargetBuilderAllModules<>();
		
		final AllModulesCompiler<PARSED_FILE, SynchronizedCompilerCodeMap, ResolveError> allModulesCompiler
		    = new AllModulesCompilerImpl<>(parser, compilerModel, options);
		
		final AllModulesBuildContext<PARSED_FILE, SynchronizedCompilerCodeMap, ResolveError> context
		    = new AllModulesBuildContext<>(allModulesCompiler, inputs, codeMap);
		
		final QueueAsyncExecutor executor = new QueueAsyncExecutor(false);
		
		final Value<TargetBuildResult> buildResult = new Value<>();
		
		targetBuilderAllModules.execute(
		        new LogContext(),
		        context,
		        TargetBuilderAllModules.RESOLVED_TARGET,
		        null,
		        executor,
		        result -> { buildResult.set(result); });
		
		executor.runQueuedResultRunnables();
		
		final TargetBuildResult result = buildResult.get();
		
		if (result == null) {
		    throw new IllegalStateException();
		}

		// Must get all outputs for all ResolveModule targets
		final List<ResolvedModule<PARSED_FILE, ResolveError>> resolvedModules
		    = new ArrayList<>(inputs.size());
		
		for (CompileModule compileModule : inputs) {

		    final ResolveModule resolveModule = TargetBuilderAllModules.makeResolveModule(compileModule);

		    @SuppressWarnings("unchecked")
            final ResolvedModule<PARSED_FILE, ResolveError> resolvedModule
                = result.getTargetActionResult(resolveModule, ResolvedModule.class);
		
		    if (resolvedModule == null) {
		        throw new IllegalStateException();
		    }
		    
		    resolvedModules.add(resolvedModule);
		}
		
		return resolvedModules;
	}

    public static void addBuiltinTypesToCodeMap(LanguageSpec languageSpec, CompilerCodeMap codeMap) {
        
        for (TypeName builtinTypeName : languageSpec.getBuiltinTypes()) {

            final int typeNo = codeMap.addType(TypeVariant.BUILTIN);

            codeMap.addTypeMapping(builtinTypeName, typeNo);
        }
    }
}
