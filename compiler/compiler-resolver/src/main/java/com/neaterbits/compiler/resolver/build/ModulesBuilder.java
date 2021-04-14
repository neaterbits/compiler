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
import com.neaterbits.compiler.model.common.LanguageSpec;
import com.neaterbits.compiler.model.common.passes.CompilerModel;
import com.neaterbits.compiler.resolver.ResolveError;
import com.neaterbits.compiler.resolver.build.strategies.compilemodules.AllModulesCompilerImpl;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.language.codemap.compiler.CompilerCodeMap;
import com.neaterbits.language.codemap.compiler.SynchronizedCompilerCodeMap;
import com.neaterbits.structuredlog.binary.logging.LogContext;
import com.neaterbits.util.Value;
import com.neaterbits.util.concurrency.dependencyresolution.executor.TargetBuildResult;
import com.neaterbits.util.concurrency.scheduling.QueueAsyncExecutor;
import com.neaterbits.util.parse.ParserException;

public final class ModulesBuilder<COMPILATION_UNIT, PARSED_FILE extends ParsedFile>
        extends BaseBuilder<COMPILATION_UNIT, PARSED_FILE> {

	public ModulesBuilder(
	        LanguageSpec languageSpec,
	        CompilerModel<COMPILATION_UNIT, PARSED_FILE> compilerModel,
	        CompilerOptions options) {
	    
	    super(languageSpec, compilerModel, options);
	}

	public ResolvedModule<PARSED_FILE, ResolveError> compile(
	        CompileModule module,
	        CompilerCodeMap codeMap) throws IOException, ParserException {
	    
	    Objects.requireNonNull(module);
	    
	    return compile(Arrays.asList(module), codeMap).get(0);
	}

    public List<ResolvedModule<PARSED_FILE, ResolveError>>
    compile(List<CompileModule> inputs, CompilerCodeMap intCodeMap) {
		
		Objects.requireNonNull(inputs);
		
		if (inputs.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		final SynchronizedCompilerCodeMap codeMap = new SynchronizedCompilerCodeMap(intCodeMap);

		final TargetBuilderAllModules<PARSED_FILE, SynchronizedCompilerCodeMap, ResolveError> targetBuilderAllModules
		        = new TargetBuilderAllModules<>();
		
		final AllModulesCompiler<PARSED_FILE, SynchronizedCompilerCodeMap, ResolveError> allModulesCompiler
		    = new AllModulesCompilerImpl<>(getParser(), getCompilerModel(), getOptions());
		
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
}
