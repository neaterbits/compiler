package com.neaterbits.compiler.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.neaterbits.compiler.codemap.MethodInfo;
import com.neaterbits.compiler.codemap.MethodMapCache;
import com.neaterbits.compiler.codemap.MethodVariant;
import com.neaterbits.compiler.codemap.StaticMethodOverrideMap;
import com.neaterbits.compiler.codemap.TypeReferences;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.compiler.resolver.types.ResolvedType;

public final class ResolvedCodeMapImpl<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> {
	
	private final IntCompilerCodeMap codeMap;
	
	private final TypeReferences<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> typeReferences;
	
	public ResolvedCodeMapImpl() {

		this.codeMap = new IntCompilerCodeMap(new StaticMethodOverrideMap());
		this.typeReferences = new TypeReferences<>();
	}

	public int addFile(String file, int [] types) {
		return codeMap.addFile(file, types);
	}
	
	int addType(TypeVariant typeVariant, int numMethods, int [] thisExtendsFromClasses, int [] thisExtendsFromInterfaces) {
		
		final int typeNo = codeMap.addType(typeVariant, thisExtendsFromClasses, thisExtendsFromInterfaces);
		
		codeMap.setMethodCount(typeNo, numMethods);
		
		return typeNo;
	}

	TypeVariant getTypeVariant(int typeNo) {
		return codeMap.getTypeVariantForType(typeNo);
	}
	
	public int addMethod(int typeNo, String name, int [] parameterTypes, MethodVariant methodVariant, int indexInType) {
		return codeMap.addOrGetMethod(typeNo, name, methodVariant, -1, parameterTypes, indexInType);
	}
	
	public void computeMethodExtends(int typeNo) {
		codeMap.computeMethodExtends(typeNo);
	}

	public void addFieldReferences(int fromType, int ... toTypes) {
		typeReferences.addFieldReferences(fromType, toTypes);
	}

	public void addMethodReferences(int fromType, int ... toTypes) {
		typeReferences.addMethodReferences(fromType, toTypes);
	}

	int getClassThisExtendsFrom(int typeNo) {
		return codeMap.getExtendsFromSingleSuperClass(typeNo);
	}

	int [] getDirectExtendingThis(int typeNo) {
		return codeMap.getTypesDirectlyExtendingThis(typeNo);
	}

	int [] getAllExtendingThis(int typeNo) {

		final List<Integer> allTypes = new ArrayList<>();
		
		final int [] directSubtypes = getDirectExtendingThis(typeNo);
		
		addAll(allTypes, directSubtypes);
		
		getAllSubtypes(directSubtypes, allTypes);
		
		// Use separate set to find duplicates so that returns in order
		final Set<Integer> found = new HashSet<>();
		
		final Iterator<Integer> iterator = allTypes.iterator();
		
		while (iterator.hasNext()) {
			final Integer subType = iterator.next();
			
			if (found.contains(subType)) {
				iterator.remove();
			}
			else {
				found.add(subType);
			}
		}
		
		final int [] result = new int[allTypes.size()];

		int dstIdx = 0;
		
		for (Integer foundTypeNo : allTypes) {
			result[dstIdx ++] = foundTypeNo;
		}
		
		return result;
	}
	
	MethodInfo getMethodInfo(int typeNo, String methodName, int [] parameterTypes, MethodMapCache methodMapCache) {
		return codeMap.getMethodInfo(typeNo, methodName, parameterTypes);
	}
	
	private void getAllSubtypes(int [] types, Collection<Integer> allTypes) {
		
		for (int type : types) {
			final int [] subTypes = getDirectExtendingThis(type);
			
			addAll(allTypes, subTypes);
			
			getAllSubtypes(subTypes, allTypes);
		}
	}
	
	private static void addAll(Collection<Integer> collection, int [] array) {
		for (int i : array) {
			collection.add(i);
		}
	}
}
