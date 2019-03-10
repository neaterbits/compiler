package com.neaterbits.compiler.codemap;

import static com.neaterbits.compiler.codemap.Encode.decodeTypeNo;
import static com.neaterbits.compiler.codemap.Encode.encodeType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.neaterbits.compiler.codemap.BaseCodeMap;
import com.neaterbits.compiler.codemap.Encode;
import com.neaterbits.compiler.codemap.FileReferences;
import com.neaterbits.compiler.codemap.MethodInfo;
import com.neaterbits.compiler.codemap.MethodMap;
import com.neaterbits.compiler.codemap.MethodMapCache;
import com.neaterbits.compiler.codemap.MethodVariant;
import com.neaterbits.compiler.codemap.StaticMethodOverrideMap;
import com.neaterbits.compiler.codemap.TypeHierarchy;
import com.neaterbits.compiler.codemap.TypeReferences;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.resolver.loader.ResolvedFile;
import com.neaterbits.compiler.resolver.loader.ResolvedType;

public final class ResolvedCodeMapImpl extends BaseCodeMap {
	
	private final FileReferences<ResolvedFile> fileReferences;
	private final TypeHierarchy typeHierarchy;
	private final MethodMap methodMap;
	private final StaticMethodOverrideMap methodOverrideMap;
	private final TypeReferences<ResolvedType> typeReferences;
	
	public ResolvedCodeMapImpl() {
		this.fileReferences = new FileReferences<>();
		this.typeHierarchy 	= new TypeHierarchy();
		this.methodMap = new MethodMap();
		this.methodOverrideMap = new StaticMethodOverrideMap();
		this.typeReferences = new TypeReferences<>();
	}

	public int addFile(int [] types) {
		return fileReferences.addFile(types);
	}
	
	int addType(TypeVariant typeVariant, int numMethods, int [] thisExtendsFromClasses, int [] thisExtendsFromInterfaces) {
		
		final int typeNo = typeHierarchy.addType(
				typeVariant,
				thisExtendsFromClasses != null ? typeHierarchy.encodeTypeVariant(thisExtendsFromClasses) : null,
				thisExtendsFromInterfaces != null ? typeHierarchy.encodeTypeVariant(thisExtendsFromInterfaces) : null
				);
		
		methodMap.allocateMethods(typeNo, numMethods);
		
		return typeNo;
	}
	
	private int getEncodedTypeNo(int typeNo) {
		return encodeType(typeNo, typeHierarchy.getTypeVariantForType(typeNo));
	}

	TypeVariant getTypeVariant(int typeNo) {
		return typeHierarchy.getTypeVariantForType(typeNo);
	}
	
	public int addMethod(int typeNo, String name, int [] parameterTypes, MethodVariant methodVariant, int indexInType, MethodMapCache methodMapCache) {
		
		return methodMap.addMethod(
				typeNo,
				typeHierarchy.getTypeVariantForType(typeNo),
				name,
				parameterTypes,
				methodVariant,
				indexInType,
				methodMapCache);
	}
	
	public void computeMethodExtends(int typeNo) {
		
		final int encodedTypeNo = getEncodedTypeNo(typeNo);
		
		final int [] extendedByEncoded = typeHierarchy.getTypesExtendingThisEncoded(typeNo);
		
		if (extendedByEncoded != null) {
			methodOverrideMap.addTypeExtendsTypes(encodedTypeNo, extendedByEncoded, methodMap);
		}
	}

	public void addFieldReferences(int fromType, int ... toTypes) {
		typeReferences.addFieldReferences(fromType, toTypes);
	}

	public void addMethodReferences(int fromType, int ... toTypes) {
		typeReferences.addMethodReferences(fromType, toTypes);
	}
	

	int getClassThisExtendsFrom(int typeNo) {
		
		final int [] types = typeHierarchy.getTypesThisExtendsFrom(typeNo, Encode::isClass);

		final int result;
		
		if (types == null) {
			result = -1;
		}
		else {
		
			if (types.length > 1) {
				throw new IllegalStateException("Extending from more than one class");
			}
			
			return types.length == 0 ? -1 : types[0];
		}
		
		return result;
	}
	
	private static final int [] EMPTY_ARRAY = new int[0];

	int [] getDirectSubtypes(int typeNo) {

		final int numSubtypes = typeHierarchy.getNumExtendingThis(typeNo);
		final int [] result;

		if (numSubtypes == 0) {
			result = EMPTY_ARRAY;
		}
		else {
			result = new int[numSubtypes];

			for (int i = 0; i < numSubtypes; ++ i) {
				final int subtypeEncoded = typeHierarchy.getTypesExtendingThisEncoded(typeNo, i);

				result[i] = decodeTypeNo(subtypeEncoded);
			}
		}

		return result;
	}

	int [] getAllSubtypes(int typeNo) {

		final List<Integer> allTypes = new ArrayList<>();
		
		final int [] directSubtypes = getDirectSubtypes(typeNo);
		
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
		return methodMap.getMethodInfo(typeNo, methodName, parameterTypes, methodMapCache);
	}
	
	private void getAllSubtypes(int [] types, Collection<Integer> allTypes) {
		
		for (int type : types) {
			final int [] subTypes = getDirectSubtypes(type);
			
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
