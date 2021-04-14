package com.neaterbits.ide.core.model.codemap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.build.common.language.CompileableLanguage;
import com.neaterbits.build.common.tasks.util.SourceFileScanner;
import com.neaterbits.build.common.tasks.util.SourceFileScanner.Namespace;
import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.build.types.resource.NamespaceResourcePath;
import com.neaterbits.build.types.resource.SourceFileResource;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;
import com.neaterbits.compiler.util.model.TypeSources;
import com.neaterbits.ide.common.model.codemap.TypeSuggestion;

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
