package com.neaterbits.compiler.bytecode.common.loader;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

import com.neaterbits.compiler.bytecode.common.ClassBytecode;
import com.neaterbits.compiler.bytecode.common.ClassFileException;
import com.neaterbits.compiler.bytecode.common.TypeMap;
import com.neaterbits.compiler.common.TypeName;
import com.neaterbits.compiler.common.resolver.codemap.CodeMap;
import com.neaterbits.compiler.common.resolver.codemap.CodeMap.TypeResult;

public class HashTypeMap<T> implements TypeMap {

	private static final Boolean DEBUG = false;
	
	@FunctionalInterface
	public interface LoadType {
		ClassBytecode load(TypeName typeName) throws IOException, ClassFileException;
	}
	
	@FunctionalInterface
	public interface CreateType<TYPE> {
		TYPE create(TypeName typeName, int typeNo, ClassBytecode classBytecode);
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
	
	public final ClassBytecode addOrGetType(
			TypeName typeName,
			CodeMap codeMap,
			boolean baseClassesAlreadyLoaded,
			TypeResult typeResult,
			CreateType<T> createType,
			LoadType loadType) throws IOException, ClassFileException {
		
		if (DEBUG) {
			System.out.println("## addOrGetType " + typeName.toDebugString());
		}
		
		T type;
		
		synchronized (this) {
			type = typeByName.get(typeName);
		}
		
		final ClassBytecode addedBytecode;
		
		if (type == null) {
			
			final ClassBytecode classByteCode = loadType.load(typeName);
			
			if (classByteCode != null) {

				final int [] extendsFromTypes;
				
				if (baseClassesAlreadyLoaded && classByteCode.getSuperClass() != null) {

					final List<TypeName> extendsFrom = Arrays.asList(classByteCode.getSuperClass());
					
					extendsFromTypes = new int[extendsFrom.size()];
				
					for (int i = 0; i < extendsFromTypes.length; ++ i) {
						
						final TypeName extendsFromName = extendsFrom.get(i);
						
						final T extendsFromType = typeByName.get(extendsFromName);

						if (DEBUG) {
							System.out.println("## get type for " + typeName.toDebugString() + " with type " + extendsFromType);
						}
						
						final int extendsFromTypeNo = getTypeNo.getTypeNo(extendsFromType);
						
						extendsFromTypes[i] = extendsFromTypeNo;
					}
				}
				else {
					extendsFromTypes = null;
				}

				synchronized (this) {
					type = typeByName.get(typeName);
					
					if (type != null) {
						// Type added by other thread
						addedBytecode = null;
					}
					else {
						final int typeNo = codeMap.addType(classByteCode.getTypeVariant(), extendsFromTypes);

						type = createType.create(typeName, typeNo, classByteCode);
						
						if (type == null) {
							throw new IllegalStateException();
						}
						
						typeByName.put(typeName, type);

						addedBytecode = classByteCode;
						
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

		if (DEBUG) {
			System.out.println("## done addOrGetType " + typeName.toDebugString() + " with " + typeResult.type);
		}

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
