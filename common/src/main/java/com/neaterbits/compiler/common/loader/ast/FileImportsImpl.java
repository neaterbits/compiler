package com.neaterbits.compiler.common.loader.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.Import;
import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.ast.typedefinition.ClassOrInterfaceName;
import com.neaterbits.compiler.common.loader.FileImports;
import com.neaterbits.compiler.common.parser.ParsedFile;

final class FileImportsImpl implements FileImports {

	private final ParsedFile parsedFile;
	
	FileImportsImpl(ParsedFile parsedFile) {
		Objects.requireNonNull(parsedFile);

		this.parsedFile = parsedFile;
	}
	
	private ASTList<Import> getImports() {
		return parsedFile.getParsed().getImports();
	}
	
	private static ScopedName makeScopedName(NamespaceReference namespace, ClassOrInterfaceName className) {
		
		Objects.requireNonNull(namespace);
		Objects.requireNonNull(className);
		
		final String [] namespaceParts = namespace.getParts();
		
		return ScopedName.makeScopedName(namespaceParts, namespaceParts.length, className.getName());
	}

	
	@Override
	public List<ScopedName> getAllNameCombinations(ScopedName scopedName) {
		
		final List<ScopedName> result;
		
		if (scopedName.getScope() != null && !scopedName.getScope().isEmpty()) {
			throw new IllegalArgumentException("Already scoped");
		}
		else {
			
			final ASTList<Import> imports = getImports();
			
			result = new ArrayList<>(imports.size());
			
			for (Import typeImport : imports) {
				
				if (typeImport.getTypeName() != null && typeImport.getTypeName().getName().equals(scopedName.getName())) {
					result.add(makeScopedName(typeImport.getNamespace(), typeImport.getTypeName()));
				}
				else if (typeImport.getNamespaceOrTypeName() != null) {

					final String [] packageOrType = typeImport.getNamespaceOrTypeName();
					
					result.add(ScopedName.makeScopedName(packageOrType, packageOrType.length - 1, packageOrType[packageOrType.length - 1]));
					result.add(ScopedName.makeScopedName(packageOrType, packageOrType.length, scopedName.getName()));
				}
				else if (typeImport.isMethodImport()) {
					
				}
				else if (typeImport.getTypeName() != null && !typeImport.getTypeName().getName().equals(scopedName.getName())) {
					// different classname
				}
				else {
					throw new UnsupportedOperationException("Uknown import type " + typeImport);
				}
			}
		}

		return result;
	}
}
