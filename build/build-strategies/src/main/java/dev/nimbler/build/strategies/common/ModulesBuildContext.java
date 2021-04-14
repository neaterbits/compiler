package dev.nimbler.build.strategies.common;

import dev.nimbler.build.common.language.BuildableLanguage;
import dev.nimbler.build.model.BuildRoot;
import dev.nimbler.build.types.compile.Compiler;
import dev.nimbler.build.types.compile.FileDependencyMap;

public final class ModulesBuildContext extends TaskBuilderContext {
	private final Compiler compiler;
	private final FileDependencyMap fileDependencyMap;
	
	public ModulesBuildContext(
			BuildRoot buildRoot,
			
			BuildableLanguage language,

			Compiler compiler,
			
			FileDependencyMap fileDependencyMap) {
		
		super(buildRoot, language);
		
		this.compiler = compiler;
		this.fileDependencyMap = fileDependencyMap;
	}

	public ModulesBuildContext(ModulesBuildContext context) {
		super(context);
		
		this.compiler = context.compiler;
		this.fileDependencyMap = context.fileDependencyMap;
	}

    public Compiler getCompiler() {
        return compiler;
    }
}
