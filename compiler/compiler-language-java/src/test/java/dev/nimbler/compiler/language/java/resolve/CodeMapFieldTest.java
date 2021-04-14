package dev.nimbler.compiler.language.java.resolve;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.util.parse.ParserException;

import dev.nimbler.compiler.language.java.JavaUtil;
import dev.nimbler.compiler.language.java.TestFile;
import dev.nimbler.compiler.language.java.compile.BaseCompilerTest;
import dev.nimbler.compiler.language.java.compile.CompiledAndMappedFiles;
import dev.nimbler.language.codemap.FieldInfo;
import dev.nimbler.language.common.types.Mutability;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.types.Visibility;

public class CodeMapFieldTest extends BaseCompilerTest {

	@Test
	public void testFields() throws IOException, ParserException {

		final String source =
				
				"package com.test;\n"
			+   "class FieldsTest {\n"
			+   " static volatile byte byteField;\n"
			+   " private final transient int integerField;\n"
			+   "}\n";

		
		final TestFile spec = new TestFile("FieldsTest.java", source);
		
		final CompiledAndMappedFiles compiledAndMapped = compileAndMap(spec, new TestResolvedTypes());
		
		final TypeName type = JavaUtil.parseToTypeName("com.test.FieldsTest");
		
		final FieldInfo byteFieldInfo = compiledAndMapped.getFieldInfo(type, "byteField");

		final int byteTypeNo = compiledAndMapped.getTypeNo(JavaUtil.parseToTypeName("byte"));
		
		assertThat(byteFieldInfo).isNotNull();
		assertThat(byteFieldInfo.getFieldNo()).isGreaterThanOrEqualTo(0);
		assertThat(byteFieldInfo.isStatic()).isTrue();
		assertThat(byteFieldInfo.getVisibility()).isEqualTo(Visibility.NAMESPACE);
		assertThat(byteFieldInfo.getMutability()).isEqualTo(Mutability.MUTABLE);
		assertThat(byteFieldInfo.isVolatile()).isTrue();
		assertThat(byteFieldInfo.isTransient()).isFalse();
		assertThat(byteFieldInfo.getFieldType()).isEqualTo(byteTypeNo);

		final FieldInfo integerFieldInfo = compiledAndMapped.getFieldInfo(type, "integerField");

		final int intTypeNo = compiledAndMapped.getTypeNo(JavaUtil.parseToTypeName("int"));

		assertThat(integerFieldInfo).isNotNull();
		assertThat(integerFieldInfo.getFieldNo()).isGreaterThanOrEqualTo(0);
		assertThat(integerFieldInfo.isStatic()).isFalse();
		assertThat(integerFieldInfo.getVisibility()).isEqualTo(Visibility.PRIVATE);
		assertThat(integerFieldInfo.getMutability()).isEqualTo(Mutability.VALUE_OR_REF_IMMUTABLE);
		assertThat(integerFieldInfo.isVolatile()).isFalse();
		assertThat(integerFieldInfo.isTransient()).isTrue();
		assertThat(integerFieldInfo.getFieldType()).isEqualTo(intTypeNo);
	}
}
