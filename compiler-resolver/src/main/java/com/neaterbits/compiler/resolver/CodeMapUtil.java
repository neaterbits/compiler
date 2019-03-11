package com.neaterbits.compiler.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.util.TypeName;

final class CodeMapUtil extends ResolveUtil {

	static <BUILTINTYPE, COMPLEXTYPE> 
		ResolvedTypeCodeMapImpl<BUILTINTYPE, COMPLEXTYPE> makeCodeMap(
				List<ResolvedFile<BUILTINTYPE, COMPLEXTYPE>> resolvedFiles,
				Collection<BUILTINTYPE> builtinTypes,
				List<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> typesInDependencyOrder,
				ASTModel<BUILTINTYPE, COMPLEXTYPE> astModel) {
		
		final ResolvedTypeCodeMapImpl<BUILTINTYPE, COMPLEXTYPE> codeMap = new ResolvedTypeCodeMapImpl<>(
				new ResolvedCodeMapImpl<>(),
				builtinTypes,
				astModel);
		
		final Map<TypeName, ResolvedType<BUILTINTYPE, COMPLEXTYPE>> resolvedTypesByName = new HashMap<>();
		
		for (ResolvedFile<BUILTINTYPE, COMPLEXTYPE> resolvedFile : resolvedFiles) {
			
			forEachResolvedTypeNested(resolvedFile.getTypes(), type -> {
				resolvedTypesByName.put(type.getTypeName(), type);
			});
		}
		
		// Verify that all dependencies are resolved
		for (ResolvedFile<BUILTINTYPE, COMPLEXTYPE> resolvedFile : resolvedFiles) {

			forEachResolvedTypeNested(resolvedFile.getTypes(), type -> {
				
				if (type.getExtendsFrom() != null) {
					for (ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE> typeDependency : type.getExtendsFrom()) {
						if (!resolvedTypesByName.containsKey(typeDependency.getCompleteName())) {
							throw new IllegalStateException("Cannot find type " + typeDependency.getCompleteName());
						}
					}
				}
				
			});
		}
		
		final Set<TypeName> toAdd = new HashSet<>(resolvedTypesByName.keySet());
		
		
		// Since types extend from other types, we can only add those were we have added all base classes
		while (!toAdd.isEmpty()) {
			
			final Iterator<TypeName> iterator = toAdd.iterator();
			
			while (iterator.hasNext()) {

				final TypeName completeName = iterator.next();
				
				if (codeMap.hasType(completeName)) {
					throw new IllegalStateException();
				}
	
				final ResolvedType<BUILTINTYPE, COMPLEXTYPE> type = resolvedTypesByName.get(completeName);
				
				boolean allExtendsFromAdded = true;
				
				if (type.getExtendsFrom() != null) {
					for (ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE> typeDependency : type.getExtendsFrom()) {
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
			
		for (ResolvedFile<BUILTINTYPE, COMPLEXTYPE> resolvedFile : resolvedFiles) {
			forEachResolvedTypeNested(resolvedFile.getTypes(), type -> {
				typeNosList.add(codeMap.getTypeNo(type.getTypeName()));
			});
			
			final int [] typeNos = new int[typeNosList.size()];
			
			for (int i = 0; i < typeNosList.size(); ++ i) {
				typeNos[i] = typeNosList.get(i);
			}
			
			codeMap.addFile(resolvedFile, typeNos);
			
			typeNosList.clear();
		}
		
		final MethodsResolver<BUILTINTYPE, COMPLEXTYPE> methodsResolver = new MethodsResolver<>(codeMap, astModel);

		methodsResolver.resolveMethodsForAllTypes(resolvedFiles);
		
		return codeMap;
	}

	
	
}
