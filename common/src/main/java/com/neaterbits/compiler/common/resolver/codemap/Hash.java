package com.neaterbits.compiler.common.resolver.codemap;

import java.util.Arrays;

final class Hash {

	interface GetCompareValue {

		long getKey(long mapValue);
		
		long getValue(long mapValue);
		
		long getDefaultValue();
		
		long makeMapValue(long key, long value);
	}
	
	private static int hash(long key, int size) {
		return (int)(key % size);
	}
	
	private static int hashIndex(long [] hashMap, long key) {
		final int hash = hash(key, hashMap.length - 1) + 1;

		return hash;
	}
	
	static long [] makeHashMap(int size, long undefValue) {
		
		final long [] hashMap = new long[size + 1];
		
		if (undefValue != 0L) {
			Arrays.fill(hashMap, undefValue);
			
			hashMap[0] = 0L;
		}
		
		return hashMap;
	}
	
	static long [] hashStore(long [] hashMap, long key, long value, long undefValue, GetCompareValue values) {

		final long numEntries = hashMap[0];
		
		if (numEntries > hashMap.length * 0.75) {

			final long [] rehash = makeHashMap(hashMap.length * 3, undefValue);

			for (int i = 1; i < hashMap.length; ++ i) {
				final long mapValue = hashMap[i];

				if (mapValue != undefValue) {
					
					
					storeKeyValue(
							rehash,
							values.getKey(mapValue),
							values.getValue(mapValue),
							undefValue,
							values);
				}
			}
			
			hashMap = rehash;
		}
		
		storeKeyValue(hashMap, key, value, undefValue, values);
		
		return hashMap;
	}
		
	private static void storeKeyValue(long [] hashMap, long key, long value, long undefValue, GetCompareValue values) {

		final int hashIndex = hashIndex(hashMap, key);

		final long mapValueToStore = values.makeMapValue(key, value);

		final long initialMapValue = hashMap[hashIndex];
		
		if (initialMapValue == undefValue) {
			hashMap[hashIndex] = mapValueToStore;
		}
		else {
			if (values.getKey(initialMapValue) == key) {
				throw new IllegalStateException(String.format("Key already stored: %x", key));
			}
			
			if (!storeAfterIndex(hashMap, hashIndex, key, undefValue, mapValueToStore, values)) {
				storeBeforeIndex(hashMap, hashIndex, key, undefValue, mapValueToStore, values);
			}
			
		}

		++ hashMap[0];
	}
	
	private static boolean storeAfterIndex(
			long [] hashMap,
			int hashIndex,
			long key,
			long undefValue,
			long mapValueToStore,
			GetCompareValue values) {

		for (int i = hashIndex + 1; i < hashMap.length; ++ i) {
			final long mapValue = hashMap[i];

			if (mapValue == undefValue) {
				hashMap[i] = mapValueToStore;
				return true;
			}

			if (values.getKey(mapValue) == key) {
				throw new IllegalStateException();
			}
		}
		
		
		return false;
	}
	
	private static void storeBeforeIndex(
			long [] hashMap,
			int hashIndex,
			long key,
			long undefValue,
			long mapValueToStore,
			GetCompareValue values) {
		
		for (int i = 1; i < hashIndex; ++ i) {
			final long mapValue = hashMap[i];

			if (mapValue == undefValue) {
				hashMap[i] = mapValueToStore;
				break;
			}

			if (values.getKey(mapValue) == key) {
				throw new IllegalStateException();
			}
		}
	}
	
	static long hashGet(long [] hashMap, long key, long undefValue, GetCompareValue values) {
		
		final int hashIndex = hashIndex(hashMap, key);
		
		final long initialMapValue = hashMap[hashIndex];

		if (initialMapValue == undefValue) {
			return values.getDefaultValue();
		}
		else if (values.getKey(initialMapValue) == key) {
			return values.getValue(initialMapValue);
		}
		else {

			for (int i = hashIndex + 1; i < hashMap.length; ++ i) {
				final long mapValue = hashMap[i];
				
				if (mapValue == undefValue) {
					throw new IllegalStateException();
				}
				
				if (values.getKey(mapValue) == key) {
					return values.getValue(mapValue);
				}
			}
			
			for (int i = 1; i < hashIndex; ++ i) {
				final long mapValue = hashMap[i];
				
				if (mapValue == undefValue) {
					throw new IllegalStateException();
				}
				
				if (values.getKey(mapValue) == key) {
					return values.getValue(mapValue);
				}
			}
		}
		
		return values.getDefaultValue();
	}
	
	static String toString(long [] hashMap, long undefValue) {
		
		final StringBuilder sb = new StringBuilder();
		
		sb.append('[');
		
		for (int i = 0; i < hashMap.length; ++ i) {
			if (hashMap[i] != undefValue) {
				
				if (sb.length() > 1) {
					sb.append(", ");
				}
				
				sb.append(String.format("%d 0x%08x", i, hashMap[i]));
			}
		}

		sb.append(']');

		return sb.toString();
	}
}
