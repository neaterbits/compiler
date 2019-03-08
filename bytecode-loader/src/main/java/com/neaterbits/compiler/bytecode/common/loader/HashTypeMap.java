package com.neaterbits.compiler.bytecode.common.loader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

import com.neaterbits.compiler.bytecode.common.ClassBytecode;
import com.neaterbits.compiler.bytecode.common.TypeMap;
import com.neaterbits.compiler.common.TypeName;
import com.neaterbits.compiler.common.resolver.codemap.CodeMap;
import com.neaterbits.compiler.common.resolver.codemap.CodeMap.TypeResult;

public class HashTypeMap<T> implements TypeMap {

	@FunctionalInterface
	public interface LoadType {
		ClassBytecode load(TypeName typeName);
	}
	
	@FunctionalInterface
	public interface CreateType<TYPE> {
		TYPE create(int typeNo, ClassBytecode classBytecode);
	}

	@FunctionalInterface
	public interface GetTypeNo<TYPE> {
		int getTypeNo(TYPE type);
	}
	
	private final GetTypeNo<T> getTypeNo;
	
	private final Map<TypeName, T> typeByName;

	public HashTypeMap(GetTypeNo<T> getTypeNo) {
		
		Objects.requireNonNull(getTypeNo);
		
		this.getTypeNo = getTypeNo;
		this.typeByName = new HashMap<>();
	}

	@Override
	public final int getTypeNo(TypeName typeName) {
		final T type;
		
		synchronized (this) {
			type = typeByName.get(typeName);
		}

		return type != null ? getTypeNo.getTypeNo(type) : -1;
	}
	
	public final ClassBytecode addOrGetType(TypeName typeName, CodeMap codeMap, TypeResult typeResult, CreateType<T> createType, LoadType loadType) {
		
		T type;
		
		synchronized (this) {
			type = typeByName.get(typeName);
		}
		
		final ClassBytecode addedBytecode;
		
		if (type == null) {
			
			final ClassBytecode classBytecode = loadType.load(typeName);
			
			if (classBytecode != null) {
				
				final List<TypeName> extendsFrom = Arrays.asList(classBytecode.getSuperClass());
				
				final int [] extendsFromTypes = new int[extendsFrom.size()];
				
				for (int i = 0; i < extendsFromTypes.length; ++ i) {
					final int extendsFromType = getTypeNo.getTypeNo(typeByName.get(extendsFrom.get(i)));
					
					extendsFromTypes[i] = extendsFromType;
				}

				synchronized (this) {
					type = typeByName.get(typeName);
					
					if (type == null) {
						// Type added by other thread
						addedBytecode = null;
					}
					else {
						final int typeNo = codeMap.addType(classBytecode.getTypeVariant(), extendsFromTypes);

						type = createType.create(typeNo, classBytecode);
						
						if (type == null) {
							throw new IllegalStateException();
						}
						
						typeByName.put(typeName, type);

						addedBytecode = classBytecode;
						
					}
				}
			}
			else {
				addedBytecode = null;
			}
		}
		else {
			addedBytecode = null;
		}
		
		typeResult.type = type != null ? getTypeNo.getTypeNo(type) : -1;
		
		return addedBytecode;
	}
	
	public void forEachKeyValueSynchronized(BiConsumer<TypeName, T> onType) {
		
		synchronized (this) {
			for (Map.Entry<TypeName, T> entry : typeByName.entrySet()) {
				onType.accept(entry.getKey(), entry.getValue());
			}
		}
	}
	
	public boolean hasType(TypeName typeName) {
		
		Objects.requireNonNull(typeName);
		
		synchronized(this) {
			return typeByName.containsKey(typeName);
		}
	}
}
