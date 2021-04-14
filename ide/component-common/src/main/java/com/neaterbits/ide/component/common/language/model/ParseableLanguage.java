package com.neaterbits.ide.component.common.language.model;

import com.neaterbits.build.types.resource.ModuleResourcePath;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.model.common.ResolvedTypes;
import com.neaterbits.ide.common.model.source.SourceFileModel;

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
