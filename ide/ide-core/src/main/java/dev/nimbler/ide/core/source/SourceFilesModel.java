package dev.nimbler.ide.core.source;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import com.neaterbits.util.concurrency.scheduling.Constraint;

import dev.nimbler.build.types.resource.ModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.compiler.model.common.ResolvedTypes;
import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.ide.common.scheduling.IDEScheduler;
import dev.nimbler.ide.component.common.language.LanguageComponent;
import dev.nimbler.ide.component.common.language.LanguageName;
import dev.nimbler.ide.component.common.language.Languages;
import dev.nimbler.ide.util.ui.text.Text;
import dev.nimbler.language.codemap.compiler.CompilerCodeMap;

public final class SourceFilesModel {

	private final IDEScheduler scheduler;
	private final Languages languages;
	private final ResolvedTypes resolvedTypes;
	private final CompilerCodeMap codeMap;
	
	private final Map<SourceFileInfo, SourceFileModel> parsedSourceFiles;
	
	public SourceFilesModel(IDEScheduler scheduler, Languages languages, ResolvedTypes resolvedTypes, CompilerCodeMap codeMap) {
		
		Objects.requireNonNull(scheduler);
		Objects.requireNonNull(languages);
		Objects.requireNonNull(resolvedTypes);
		
		this.scheduler = scheduler;
		this.languages = languages;
		this.resolvedTypes = resolvedTypes;
		this.codeMap = codeMap;
		this.parsedSourceFiles = new HashMap<>();
	}

	public void parseModuleOnStartup(
			ModuleResourcePath modulePath,
			List<ModuleResourcePath> dependencies,
			List<SourceFileResourcePath> sourceFiles, LanguageName language) throws IOException {
		
		final LanguageComponent languageComponent = languages.getLanguageComponent(language);
		
		final Map<SourceFileResourcePath, SourceFileModel> sourceFileModels
				= languageComponent.getParseableLanguage().parseModule(modulePath, dependencies, sourceFiles, resolvedTypes, codeMap);

		
		for (Map.Entry<SourceFileResourcePath, SourceFileModel> entry : sourceFileModels.entrySet()) {
			
			final SourceFileInfo sourceFileInfo = new SourceFileInfo(entry.getKey(), languageComponent, resolvedTypes);
			
			parsedSourceFiles.put(sourceFileInfo, entry.getValue());
		}
	}
	
	public void parseOnChange(SourceFileInfo sourceFile, Text text, Consumer<SourceFileModel> onUpdatedModel) {

		// Called from IDE so schedule asynchronously
		scheduler.scheduleTask(
				"parse",
				"Parse file " + sourceFile.getFile().getName(),
				Constraint.CPU,
				sourceFile,
				file -> {
					final SourceFileModel sourceFileModel  = file.getLanguage().getParseableLanguage().parseAndResolveChangedFile(
							sourceFile.getPath(),
							text.asString(),
							sourceFile.getResolvedTypes(),
							codeMap);

					return sourceFileModel;
				},
				(file, sourceFileModel) -> {

					parsedSourceFiles.put(sourceFile, sourceFileModel);
					
					onUpdatedModel.accept(sourceFileModel);
				});
		
	}
}
