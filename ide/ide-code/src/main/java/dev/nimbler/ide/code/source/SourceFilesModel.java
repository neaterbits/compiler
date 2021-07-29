package dev.nimbler.ide.code.source;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import org.jutils.concurrency.scheduling.Constraint;

import dev.nimbler.build.types.resource.ModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.compiler.model.common.ResolvedTypes;
import dev.nimbler.ide.code.tasks.TaskManager;
import dev.nimbler.ide.common.codeaccess.SourceFileInfo;
import dev.nimbler.ide.common.codeaccess.types.LanguageName;
import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.ide.component.common.language.LanguageComponent;
import dev.nimbler.ide.component.common.language.Languages;
import dev.nimbler.ide.util.ui.text.Text;
import dev.nimbler.language.codemap.compiler.CompilerCodeMap;

public final class SourceFilesModel {

	private final TaskManager taskManager;
	private final Languages languages;
	private final ResolvedTypes resolvedTypes;
	private final CompilerCodeMap codeMap;

	private static class ParsedSourceFile {

	    @SuppressWarnings("unused")
        private final SourceFileInfo sourceFileInfo;
	    private final SourceFileModel sourceFileModel;

	    ParsedSourceFile(SourceFileInfo sourceFileInfo, SourceFileModel sourceFileModel) {
	        
	        Objects.requireNonNull(sourceFileInfo);
	        Objects.requireNonNull(sourceFileModel);
	        
            this.sourceFileInfo = sourceFileInfo;
            this.sourceFileModel = sourceFileModel;
        }
	}
	
	private final Map<SourceFileResourcePath, ParsedSourceFile> parsedSourceFiles;
	
	public SourceFilesModel(TaskManager taskManager, Languages languages, ResolvedTypes resolvedTypes, CompilerCodeMap codeMap) {
		
		Objects.requireNonNull(taskManager);
		Objects.requireNonNull(languages);
		Objects.requireNonNull(resolvedTypes);
		
		this.taskManager = taskManager;
		this.languages = languages;
		this.resolvedTypes = resolvedTypes;
		this.codeMap = codeMap;
		this.parsedSourceFiles = new HashMap<>();
	}
	
	public SourceFileModel getSourceFileModel(SourceFileResourcePath sourceFileResourcePath) {
	    
	    Objects.requireNonNull(sourceFileResourcePath);
	    
	    final ParsedSourceFile parsedSourceFile = parsedSourceFiles.get(sourceFileResourcePath);
	
	    return parsedSourceFile != null
	            ? parsedSourceFile.sourceFileModel
                : null;
	}

	public void parseModuleOnStartup(
			ModuleResourcePath modulePath,
			List<ModuleResourcePath> dependencies,
			List<SourceFileResourcePath> sourceFiles, LanguageName language) throws IOException {
		
		final LanguageComponent languageComponent = languages.getLanguageComponent(language);
		
		final Map<SourceFileResourcePath, SourceFileModel> sourceFileModels
				= languageComponent.getParseableLanguage().parseModule(modulePath, dependencies, sourceFiles, resolvedTypes, codeMap);
		
		for (Map.Entry<SourceFileResourcePath, SourceFileModel> entry : sourceFileModels.entrySet()) {
			
 			final SourceFileInfo sourceFileInfo = new SourceFileInfo(entry.getKey(), language /* , languageComponent, resolvedTypes */);
			final ParsedSourceFile parsedSourceFile = new ParsedSourceFile(sourceFileInfo, entry.getValue());

			parsedSourceFiles.put(sourceFileInfo.getPath(), parsedSourceFile);
		}
	}
	
	public void parseOnChange(SourceFileInfo sourceFile, Text text, ResolvedTypes resolvedTypes, Consumer<SourceFileModel> onUpdatedModel) {

		// Called from IDE so schedule asynchronously
		taskManager.scheduleTask(
				"parse",
				"Parse file " + sourceFile.getFile().getName(),
				Constraint.CPU,
				sourceFile,
				file -> {
					
					final LanguageComponent languageComponent = languages.getLanguageComponent(file.getLanguage());
					
					final SourceFileModel sourceFileModel  = languageComponent.getParseableLanguage().parseAndResolveChangedFile(
							sourceFile.getPath(),
							text.asString(),
							resolvedTypes,
							codeMap);

					return sourceFileModel;
				},
				(file, sourceFileModel) -> {

		            final ParsedSourceFile parsedSourceFile = new ParsedSourceFile(sourceFile, sourceFileModel);

					parsedSourceFiles.put(sourceFile.getPath(), parsedSourceFile);
					
					onUpdatedModel.accept(sourceFileModel);
				});
		
	}
}
