package dev.nimbler.ide.core.tasks;


import java.util.Objects;

import dev.nimbler.build.common.language.CompileableLanguage;
import dev.nimbler.build.model.BuildRoot;
import dev.nimbler.build.model.runtimeenvironment.RuntimeEnvironment;
import dev.nimbler.build.strategies.common.TaskBuilderContext;
import dev.nimbler.ide.core.model.codemap.CodeMapGatherer;

public final class InitialScanContext extends TaskBuilderContext {

	private final CompileableLanguage compileableLanguage;
	private final CodeMapGatherer codeMapGatherer;
	
	public InitialScanContext(
	        BuildRoot buildRoot,
	        CompileableLanguage language,
	        CodeMapGatherer codeMapGatherer) {
	    
		super(buildRoot, language);
	
		Objects.requireNonNull(codeMapGatherer);
		
		this.compileableLanguage = language;
		this.codeMapGatherer = codeMapGatherer;
	}

	public InitialScanContext(
	        TaskBuilderContext context,
	        CompileableLanguage language,
	        RuntimeEnvironment runtimeEnvironment,
	        CodeMapGatherer codeMapGatherer) {
	    
		super(context);

        Objects.requireNonNull(runtimeEnvironment);
		Objects.requireNonNull(codeMapGatherer);
		
		this.compileableLanguage = language;
		this.codeMapGatherer = codeMapGatherer;
	}

	public CompileableLanguage getCompileableLanguage() {
		return compileableLanguage;
	}

    public CodeMapGatherer getCodeMapGatherer() {
		return codeMapGatherer;
	}
}
