package dev.nimbler.compiler.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;
import org.jutils.Bits;

public class BitsTest {

	@Test
	public void testGetNumBitsForStoringMaxValue() {

		assertThat(Bits.getNumBitsForStoringMaxValue(1)).isEqualTo(1);
		assertThat(Bits.getNumBitsForStoringMaxValue(2)).isEqualTo(2);
		assertThat(Bits.getNumBitsForStoringMaxValue(3)).isEqualTo(2);
		assertThat(Bits.getNumBitsForStoringMaxValue(4)).isEqualTo(3);
		assertThat(Bits.getNumBitsForStoringMaxValue(5)).isEqualTo(3);
		assertThat(Bits.getNumBitsForStoringMaxValue(6)).isEqualTo(3);
		assertThat(Bits.getNumBitsForStoringMaxValue(7)).isEqualTo(3);
		assertThat(Bits.getNumBitsForStoringMaxValue(8)).isEqualTo(4);
		assertThat(Bits.getNumBitsForStoringMaxValue(65535)).isEqualTo(16);
		assertThat(Bits.getNumBitsForStoringMaxValue(65536)).isEqualTo(17);
		assertThat(Bits.getNumBitsForStoringMaxValue(1L << 62)).isEqualTo(63);
		
		try {
			Bits.getNumBitsForStoringMaxValue(1L << 63);
			
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testMask() {
		assertThat(Bits.maskForNumBits(0)).isEqualTo(0);
		assertThat(Bits.maskForNumBits(1)).isEqualTo(1);
		assertThat(Bits.maskForNumBits(2)).isEqualTo(3);
		assertThat(Bits.maskForNumBits(3)).isEqualTo(7);
		assertThat(Bits.maskForNumBits(4)).isEqualTo(15);
		assertThat(Bits.maskForNumBits(5)).isEqualTo(31);
		assertThat(Bits.maskForNumBits(6)).isEqualTo(63);
		assertThat(Bits.maskForNumBits(7)).isEqualTo(127);
		assertThat(Bits.maskForNumBits(8)).isEqualTo(255);
		assertThat(Bits.maskForNumBits(63)).isEqualTo(Long.MAX_VALUE);
		assertThat(Bits.maskForNumBits(64)).isEqualTo(0xFFFFFFFFFFFFFFFFL);

		try {
			Bits.maskForNumBits(-1);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		try {
			Bits.maskForNumBits(65);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
	}
	
	@Test
	public void testIntMask() {
		assertThat(Bits.intMaskForNumBits(0)).isEqualTo(0);
		assertThat(Bits.intMaskForNumBits(1)).isEqualTo(1);
		assertThat(Bits.intMaskForNumBits(2)).isEqualTo(3);
		assertThat(Bits.intMaskForNumBits(3)).isEqualTo(7);
		assertThat(Bits.intMaskForNumBits(4)).isEqualTo(15);
		assertThat(Bits.intMaskForNumBits(5)).isEqualTo(31);
		assertThat(Bits.intMaskForNumBits(6)).isEqualTo(63);
		assertThat(Bits.intMaskForNumBits(7)).isEqualTo(127);
		assertThat(Bits.intMaskForNumBits(8)).isEqualTo(255);
		assertThat(Bits.intMaskForNumBits(31)).isEqualTo(Integer.MAX_VALUE);
		assertThat(Bits.intMaskForNumBits(32)).isEqualTo((int)0xFFFFFFFFL);

		try {
			Bits.intMaskForNumBits(-1);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		try {
			Bits.intMaskForNumBits(33);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
	}
}
