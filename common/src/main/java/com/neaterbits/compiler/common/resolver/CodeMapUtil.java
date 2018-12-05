package com.neaterbits.compiler.common.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.common.loader.ResolvedFile;
import com.neaterbits.compiler.common.loader.ResolvedType;
import com.neaterbits.compiler.common.loader.ResolvedTypeDependency;
import com.neaterbits.compiler.common.resolver.codemap.ResolvedCodeMapImpl;
import com.neaterbits.compiler.common.resolver.codemap.ResolvedTypeCodeMapImpl;

final class CodeMapUtil extends ResolveUtil {

	static ResolvedTypeCodeMapImpl makeCodeMap(List<ResolvedFile> resolvedFiles, Collection<? extends BuiltinType> builtinTypes, List<ResolvedType> typesInDependencyOrder) {
		
		final ResolvedTypeCodeMapImpl codeMap = new ResolvedTypeCodeMapImpl(new ResolvedCodeMapImpl(), builtinTypes);
		
		final Map<CompleteName, ResolvedType> resolvedTypesByName = new HashMap<>();
		
		for (ResolvedFile resolvedFile : resolvedFiles) {
			forEachResolvedTypeNested(resolvedFile.getTypes(), type -> resolvedTypesByName.put(type.getCompleteName(), type));
		}
		
		// Verify that all dependencies are resolved
		for (ResolvedFile resolvedFile : resolvedFiles) {

			forEachResolvedTypeNested(resolvedFile.getTypes(), type -> {
				
				if (type.getExtendsFrom() != null) {
					for (ResolvedTypeDependency typeDependency : type.getExtendsFrom()) {
						if (!resolvedTypesByName.containsKey(typeDependency.getCompleteName())) {
							throw new IllegalStateException("Cannot find type " + typeDependency.getCompleteName());
						}
					}
				}
				
			});
		}
		
		final Set<CompleteName> toAdd = new HashSet<>(resolvedTypesByName.keySet());
		
		
		// Since types extend from other types, we can only add those were we have added all base classes
		while (!toAdd.isEmpty()) {
			
			final Iterator<CompleteName> iterator = toAdd.iterator();
			
			while (iterator.hasNext()) {

				final CompleteName completeName = iterator.next();
				
				if (codeMap.hasType(completeName)) {
					throw new IllegalStateException();
				}
	
				final ResolvedType type = resolvedTypesByName.get(completeName);
				
				boolean allExtendsFromAdded = true;
				
				if (type.getExtendsFrom() != null) {
					for (ResolvedTypeDependency typeDependency : type.getExtendsFrom()) {
						if (!codeMap.hasType(typeDependency.getCompleteName())) {
							allExtendsFromAdded = false;
							break;
						}
					}
				}
				
				if (allExtendsFromAdded) {
					codeMap.addType(type);
					
					iterator.remove();

					if (typesInDependencyOrder != null) {
						typesInDependencyOrder.add(type);
					}
				}
			}
		}
		
		final List<Integer> typeNosList = new ArrayList<>();
			
		for (ResolvedFile resolvedFile : resolvedFiles) {
			forEachResolvedTypeNested(resolvedFile.getTypes(), type -> {
				typeNosList.add(codeMap.getTypeNo(type.getCompleteName()));
			});
			
			final int [] typeNos = new int[typeNosList.size()];
			
			for (int i = 0; i < typeNosList.size(); ++ i) {
				typeNos[i] = typeNosList.get(i);
			}
			
			codeMap.addFile(resolvedFile, typeNos);
			
			typeNosList.clear();
		}
		
		final MethodsResolver methodsResolver = new MethodsResolver(codeMap);

		methodsResolver.resolveMethodsForAllTypes(resolvedFiles);
		
		return codeMap;
	}

	
	
}
