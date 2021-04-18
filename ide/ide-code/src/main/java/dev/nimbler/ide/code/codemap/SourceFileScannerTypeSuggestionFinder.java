package dev.nimbler.ide.code.codemap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dev.nimbler.build.common.language.CompileableLanguage;
import dev.nimbler.build.common.tasks.util.SourceFileScanner;
import dev.nimbler.build.common.tasks.util.SourceFileScanner.Namespace;
import dev.nimbler.build.model.BuildRoot;
import dev.nimbler.build.types.resource.NamespaceResourcePath;
import dev.nimbler.build.types.resource.SourceFileResource;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.build.types.resource.SourceFolderResourcePath;
import dev.nimbler.ide.common.model.codemap.TypeSuggestion;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.typesources.TypeSources;

final class SourceFileScannerTypeSuggestionFinder extends TypeSuggestionFinder {

	private final BuildRoot buildRoot;
	private final CompileableLanguage language;

	SourceFileScannerTypeSuggestionFinder(BuildRoot buildRoot, CompileableLanguage language) {

		Objects.requireNonNull(buildRoot);
		Objects.requireNonNull(language);
		
		this.buildRoot = buildRoot;
		this.language = language;
	}
	
	@Override
	boolean canRetrieveTypeVariant() {
		return false;
	}

	@Override
	boolean hasSourceCode() {
		return true;
	}

	@Override
	boolean findSuggestions(TypeNameMatcher typeNameMatcher, Map<TypeName, TypeSuggestion> dst) {

		buildRoot.forEachSourceFolder(sourceFolder -> {
				findSuggestions(sourceFolder, typeNameMatcher, dst);

				return null;
		});
		
		
		return false;
	}
	
	@Override
	boolean hasType(TypeName typeName, TypeSources typeSources) {
		return false;
	}

	private void findSuggestions(SourceFolderResourcePath sourceFolder, TypeNameMatcher typeNameMatcher, Map<TypeName, TypeSuggestion> suggestions) {

		final List<SourceFileResourcePath> sourceFiles = new ArrayList<>();
		
		SourceFileScanner.findSourceFiles(
				sourceFolder,
				(folder, file) -> {
									
				final Namespace namespace = SourceFileScanner.getNamespaceResource(folder.getFile(), file);
				
				final NamespaceResourcePath namespaceResourcePath = new NamespaceResourcePath(sourceFolder, namespace.getNamespace());
				
					return new SourceFileResourcePath(namespaceResourcePath, new SourceFileResource(file));
				},
				sourceFiles);

		
		for (SourceFileResourcePath sourceFile : sourceFiles) {
			
			final String namespace = sourceFile.getNamespacePath().getNamespaceResource().getName().replace(File.separatorChar, '.');
	
			final String fileName = sourceFile.getName();

			final int suffixIndex = fileName.lastIndexOf(".java");
			
			if (suffixIndex > 0) {
			
				final String name = fileName.substring(0, suffixIndex);
			
				final TypeName typeName = typeNameMatcher.matches(null, sourceFile, namespace, name);
				
				if (typeName != null) {
				
					final TypeSuggestion suggestion = new TypeSuggestionImpl(
							null,
							namespace,
							name,
							language.getBinaryName(typeName),
							sourceFile);
					
					suggestions.put(typeName, suggestion);
				}
			}
		}
	}
}
