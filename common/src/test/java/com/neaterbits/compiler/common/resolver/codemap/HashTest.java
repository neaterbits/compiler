package com.neaterbits.compiler.common.resolver.codemap;

import org.junit.Test;

import com.neaterbits.compiler.common.resolver.codemap.Hash.GetCompareValue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class HashTest {

	private static final long HASH_UNDEF = 0xFFFFFFFFFFFFFFFFL;

	private static final GetCompareValue getCompareValue = new GetCompareValue() {
		@Override
		public long makeMapValue(long key, long value) {
			final long mapValue = key << 32 | value;
			
			return mapValue;
		}
		
		@Override
		public long getValue(long mapValue) {
			final long value = mapValue & 0x00000000FFFFFFFFL;
			
			return value;
		}
		
		@Override
		public long getKey(long mapValue) {
			return mapValue >>> 32;
		}
		
		@Override
		public long getDefaultValue() {
			return HASH_UNDEF;
		}
	};
	
	@Test
	public void testHash() {

		final long [] hashMap = Hash.makeHashMap(10000, HASH_UNDEF);

		assertThat(Hash.hashStore(hashMap, 1234, 2345, HASH_UNDEF, getCompareValue)).isSameAs(hashMap);
		
		try {
			Hash.hashStore(hashMap, 1234, 2345, HASH_UNDEF, getCompareValue);

			fail("Expected exception");
		}
		catch (IllegalStateException ex) {
			
		}

		assertThat(Hash.hashGet(hashMap, 2345, HASH_UNDEF, getCompareValue)).isEqualTo(HASH_UNDEF);
		assertThat(Hash.hashGet(hashMap, 3456, HASH_UNDEF, getCompareValue)).isEqualTo(HASH_UNDEF);
		
		assertThat(Hash.hashGet(hashMap, 1234, HASH_UNDEF, getCompareValue)).isEqualTo(2345);
	}

	@Test
	public void testHashingFromMiddle() {

		final long [] hashMap = Hash.makeHashMap(10, HASH_UNDEF);

		assertThat(Hash.hashStore(hashMap, 3456, 2345, HASH_UNDEF, getCompareValue)).isSameAs(hashMap);

		long [] updatedHashMap = hashMap;
		
		for (int i = 5; i < 10; ++ i) {
			final int value = 100000 - i;
			
			updatedHashMap = Hash.hashStore(updatedHashMap, i, value, HASH_UNDEF, getCompareValue);

			assertThat(Hash.hashGet(updatedHashMap, i, HASH_UNDEF, getCompareValue)).isEqualTo(value);
		}
		
		for (int i = 0; i < 5; ++ i) {
			final int value = 100000 - i;
			
			updatedHashMap = Hash.hashStore(updatedHashMap, i, value, HASH_UNDEF, getCompareValue);

			assertThat(Hash.hashGet(updatedHashMap, i, HASH_UNDEF, getCompareValue)).isEqualTo(value);
		}
		
		
		assertThat(updatedHashMap != hashMap).isTrue();
	}

	@Test
	public void testRehashing() {

		final long [] hashMap = Hash.makeHashMap(10, HASH_UNDEF);

		assertThat(Hash.hashStore(hashMap, 1234, 2345, HASH_UNDEF, getCompareValue)).isSameAs(hashMap);

		long [] updatedHashMap = hashMap;
		
		for (int i = 0; i != 1234 && i < 20; ++ i) {
			final int value = 100000 - i;
			
			updatedHashMap = Hash.hashStore(updatedHashMap, i, value, HASH_UNDEF, getCompareValue);

			assertThat(Hash.hashGet(updatedHashMap, i, HASH_UNDEF, getCompareValue)).isEqualTo(value);
		}
		
		assertThat(updatedHashMap != hashMap).isTrue();
	}

}
