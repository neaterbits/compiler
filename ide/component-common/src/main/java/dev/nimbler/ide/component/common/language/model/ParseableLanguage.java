package dev.nimbler.ide.component.common.language.model;

import dev.nimbler.build.types.resource.ModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.compiler.model.common.ResolvedTypes;
import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.language.codemap.compiler.CompilerCodeMap;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ParseableLanguage {

	Map<SourceFileResourcePath, SourceFileModel> parseModule(
			ModuleResourcePath modulePath,
			List<ModuleResourcePath> dependencies,
			List<SourceFileResourcePath> files,
			ResolvedTypes resolvedTypes,
			CompilerCodeMap codeMap) throws IOException;
	
	SourceFileModel parseAndResolveChangedFile(
			SourceFileResourcePath sourceFilePath,
			String string,
			ResolvedTypes resolvedTypes,
			CompilerCodeMap codeMap);
	
}
