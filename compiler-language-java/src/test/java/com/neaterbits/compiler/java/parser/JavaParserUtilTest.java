package com.neaterbits.compiler.java.parser;

import org.junit.Test;

import com.neaterbits.compiler.util.Base;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaParserUtilTest {

	@Test
	public void testParseIntegerLiteral() {
		assertThat(JavaParserUtil.parseIntegerLiteral("0")).isEqualTo(new JavaInteger(0L, Base.DECIMAL, 32));
		assertThat(JavaParserUtil.parseIntegerLiteral("0l")).isEqualTo(new JavaInteger(0L, Base.DECIMAL, 64));
		assertThat(JavaParserUtil.parseIntegerLiteral("0L")).isEqualTo(new JavaInteger(0L, Base.DECIMAL, 64));

		assertThat(JavaParserUtil.parseIntegerLiteral("0123")).isEqualTo(new JavaInteger(83L, Base.OCTAL, 32));

		assertThat(JavaParserUtil.parseIntegerLiteral("0x7f")).isEqualTo(new JavaInteger(127L, Base.HEX, 32));
		assertThat(JavaParserUtil.parseIntegerLiteral("0X7F")).isEqualTo(new JavaInteger(127L, Base.HEX, 32));
		assertThat(JavaParserUtil.parseIntegerLiteral("0X7Fl")).isEqualTo(new JavaInteger(127L, Base.HEX, 64));
		assertThat(JavaParserUtil.parseIntegerLiteral("0X7FL")).isEqualTo(new JavaInteger(127L, Base.HEX, 64));

		assertThat(JavaParserUtil.parseIntegerLiteral("123")).isEqualTo(new JavaInteger(123L, Base.DECIMAL, 32));
		assertThat(JavaParserUtil.parseIntegerLiteral("-123")).isEqualTo(new JavaInteger(-123L, Base.DECIMAL, 32));
		assertThat(JavaParserUtil.parseIntegerLiteral("123l")).isEqualTo(new JavaInteger(123L, Base.DECIMAL, 64));
		assertThat(JavaParserUtil.parseIntegerLiteral("-123l")).isEqualTo(new JavaInteger(-123L, Base.DECIMAL, 64));
	}
}
