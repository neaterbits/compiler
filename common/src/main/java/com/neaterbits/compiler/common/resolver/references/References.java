package com.neaterbits.compiler.common.resolver.references;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.loader.ResolvedFile;
import com.neaterbits.compiler.common.loader.ResolvedType;
import com.neaterbits.compiler.common.loader.TypeSpec;
import com.neaterbits.compiler.common.resolver.CodeMap;

public final class References extends BaseReferences implements CodeMap {
	
	private final Map<ScopedName, Integer> typesByScopedName;
	
	private final FileReferences<ResolvedFile> fileReferences;
	private final TypeHierarchy<ResolvedType> typeHierarchy;
	private final TypeReferences<ResolvedType> typeReferences;
	
	public References() {
		
		this.typesByScopedName = new HashMap<>();
		
		this.fileReferences = new FileReferences<>();
		this.typeHierarchy 	= new TypeHierarchy<>();
		this.typeReferences = new TypeReferences<>();
	}

	public int addFile(ResolvedFile file) {
		return fileReferences.addFile(file);
	}
	
	public int addType(int fileNo, ResolvedType type) {
		
		final int [] extendsFromEncoded;
		
		final List<TypeSpec> extendsFrom = type.getExtendsFrom() != null
				? type.getExtendsFrom().stream().map(t -> new TypeSpec(t.getScopedName(), t.getTypeVariant())).collect(Collectors.toList())
				: null;
		
		if (extendsFrom != null) {
			extendsFromEncoded = new int[extendsFrom.size()];

			int i = 0;
			
			for (TypeSpec typeSpec : extendsFrom) {
				extendsFromEncoded[i ++] = encodeType(getTypeNo(typeSpec), typeSpec.getTypeVariant());
			}
		}
		else {
			extendsFromEncoded = null;
		}
		
		final int typeNo = typeHierarchy.addType(fileNo, type, extendsFromEncoded);
		
		typesByScopedName.put(type.getSpec().getScopedName(), typeNo);
		
		return typeNo;
	}

	public void addFieldReferences(int fromType, int ... toTypes) {
		typeReferences.addFieldReferences(fromType, toTypes);
	}

	public void addMethodReferences(int fromType, int ... toTypes) {
		typeReferences.addMethodReferences(fromType, toTypes);
	}
	
	public ResolvedType getType(ScopedName scopedName) {
		
		Objects.requireNonNull(scopedName);

		final Integer typeNo = typesByScopedName.get(scopedName);

		return typeNo != null ? typeHierarchy.getType(decodeTypeNo(typeNo)) : null;
	}


	@Override
	public ResolvedType getClassExtendsFrom(TypeSpec classType) {
		
		final int typeNo = getTypeNo(classType);

		final List<ResolvedType> compiledTypes = typeHierarchy.getExtendsFrom(typeNo, BaseReferences::isClass);
		
		if (compiledTypes.size() > 1) {
			throw new IllegalStateException("Extending from more than one class");
		}
		
		return compiledTypes.isEmpty() ? null : compiledTypes.get(0);
	}
	
	@Override
	public Collection<ResolvedType> getInterfacesImplement(TypeSpec classType) {

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ResolvedType> getInterfacesExtendFrom(TypeSpec interfaceType) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private int getTypeNo(TypeSpec type) {
		return decodeTypeNo(getTypeNoEncoded(type));
	}
	
	private int getTypeNoEncoded(TypeSpec type) {
		Objects.requireNonNull(type);

		final Integer typeNo = typesByScopedName.get(type.getScopedName());
		
		if (typeNo == null) {
			throw new IllegalArgumentException("Unknown type " + type);
		}

		return typeNo;
	}

	@Override
	public List<ResolvedType> getDirectSubtypes(TypeSpec type) {
		
		final int typeNo = getTypeNo(type);

		final int numSubtypes = typeHierarchy.getNumExtendedBy(typeNo);

		final List<ResolvedType> result;
		
		if (numSubtypes == 0) {
			result = Collections.emptyList();
		}
		else {
			result = new ArrayList<>(numSubtypes);

			for (int i = 0; i < numSubtypes; ++ i) {
				final int subtypeEncoded = typeHierarchy.getExtendedByTypeNoEncoded(typeNo, i);

				final ResolvedType subtype = typeHierarchy.getType(decodeTypeNo(subtypeEncoded));
				
				if (subtype == null) {
					throw new IllegalStateException();
				}
				
				result.add(subtype);
			}
		}

		return result;
	}

	@Override
	public List<ResolvedType> getAllSubtypes(TypeSpec type) {

		final List<ResolvedType> allTypes = new ArrayList<>();
		
		final Collection<ResolvedType> directSubtypes = getDirectSubtypes(type);
		
		allTypes.addAll(directSubtypes);
		
		getAllSubtypes(directSubtypes, allTypes);
		
		final Set<TypeSpec> found = new HashSet<>();
		
		final Iterator<ResolvedType> iterator = allTypes.iterator();
		
		while (iterator.hasNext()) {
			final ResolvedType subType = iterator.next();
			
			if (found.contains(subType.getSpec())) {
				iterator.remove();
			}
			else {
				found.add(subType.getSpec());
			}
		}

		return allTypes;
	}
	
	private void getAllSubtypes(Collection<ResolvedType> types, Collection<ResolvedType> allTypes) {
		
		for (ResolvedType type : types) {
			final Collection<ResolvedType> subTypes = getDirectSubtypes(type.getSpec());
			
			allTypes.addAll(subTypes);
			
			getAllSubtypes(subTypes, allTypes);
		}
	}
}
