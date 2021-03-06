package dev.nimbler.exe.vm.bytecode.loader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

import dev.nimbler.language.bytecode.common.ClassByteCodeWithTypeSource;
import dev.nimbler.language.bytecode.common.ClassBytecode;
import dev.nimbler.language.bytecode.common.ClassFileException;
import dev.nimbler.language.bytecode.common.TypeMap;
import dev.nimbler.language.codemap.CodeMap;
import dev.nimbler.language.codemap.CodeMap.TypeResult;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.typesources.TypeSource;
import dev.nimbler.language.common.typesources.TypeSources;

public class HashTypeMap<T> implements TypeMap {

	private static final Boolean DEBUG = false;
	
	@FunctionalInterface
	public interface LoadType {
		ClassByteCodeWithTypeSource load(TypeName typeName) throws IOException, ClassFileException;
	}
	
	@FunctionalInterface
	public interface CreateType<TYPE> {
		TYPE create(TypeName typeName, TypeSource typeSource, int typeNo, ClassBytecode classBytecode);
	}

	@FunctionalInterface
	public interface GetTypeNo<TYPE> {
		int getTypeNo(TYPE type);
	}

	@FunctionalInterface
	public interface GetTypeSource<TYPE> {
		TypeSource getTypeSource(TYPE type);
	}

	private final GetTypeNo<T> getTypeNo;
	private final GetTypeSource<T> getTypeSource;
	
	private final Map<TypeName, T> typeByName;

	public HashTypeMap(GetTypeNo<T> getTypeNo, GetTypeSource<T> getTypeSource) {
		
		Objects.requireNonNull(getTypeNo);
		Objects.requireNonNull(getTypeSource);
		
		this.getTypeNo = getTypeNo;
		this.getTypeSource = getTypeSource;
		this.typeByName = new HashMap<>();
	}

	@Override
	public final int getTypeNo(TypeName typeName) {

		final T type = getType(typeName);
		
		return type != null ? getTypeNo.getTypeNo(type) : -1;
	}

	
	public final T getType(TypeName typeName) {
		final T type;
		
		synchronized (this) {
			type = typeByName.get(typeName);
		}

		return type;
	}
	
	public final ClassByteCodeWithTypeSource addOrGetType(
			TypeName typeName,
			CodeMap codeMap,
			boolean baseTypesAlreadyLoaded,
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
		
		final ClassByteCodeWithTypeSource addedBytecode;
		
		if (type == null) {
			
			final ClassByteCodeWithTypeSource classByteCode = loadType.load(typeName);
			
			if (classByteCode != null) {

				final int [] extendsFromClasses;
				final int [] extendsFromInterfaces;
				
				if (baseTypesAlreadyLoaded) {

					if (classByteCode.getSuperClass() != null) {
						final T extendsFromType = typeByName.get(classByteCode.getSuperClass());

						extendsFromClasses = new int [] { getTypeNo.getTypeNo(extendsFromType) };
					}
					else {
						extendsFromClasses = null;
					}
					
					if (classByteCode.getImplementedInterfacesCount() > 0) {

						extendsFromInterfaces = new int[classByteCode.getImplementedInterfacesCount()];
						
						for (int i = 0; i < classByteCode.getImplementedInterfacesCount(); ++ i) {

							final T extendsFromType = typeByName.get(classByteCode.getImplementedInterface(i));

							extendsFromInterfaces[i] = getTypeNo.getTypeNo(extendsFromType);
						}
					}
					else {
						extendsFromInterfaces = null;
					}
				}
				else {
					extendsFromClasses = null;
					extendsFromInterfaces = null;
				}

				synchronized (this) {
					type = typeByName.get(typeName);
					
					if (type != null) {
						// Type added by other thread
						addedBytecode = null;
					}
					else {
						final int typeNo = codeMap.addType(classByteCode.getTypeVariant(), extendsFromClasses, extendsFromInterfaces);

						if (DEBUG) {
							System.out.println("## addType " + typeName.toDebugString() + " " + typeNo
									+ " with superclass " 
									+ (classByteCode.getSuperClass() != null ? classByteCode.getSuperClass().toDebugString() : null));
						}

						type = createType.create(typeName, classByteCode.getTypeSource(), typeNo, classByteCode);
						
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

	public boolean hasType(TypeName typeName, TypeSources typeSources) {
		
		Objects.requireNonNull(typeName);
		
		synchronized(this) {
			final T type = typeByName.get(typeName);
					
			return type != null
					? typeSources.isSet(getTypeSource.getTypeSource(type))
					: false;
					
		}
	}
}
