package com.neaterbits.compiler.java.resolve;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.compiler.java.BaseCompilerTest;
import com.neaterbits.compiler.java.JavaUtil;
import com.neaterbits.compiler.util.NameFileSpec;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.CompiledAndMappedFiles;
import com.neaterbits.compiler.util.model.FieldInfo;
import com.neaterbits.compiler.util.model.Mutability;
import com.neaterbits.compiler.util.model.Visibility;

public class CodeMapFieldTest extends BaseCompilerTest {

	@Test
	public void testFields() throws IOException {

		final String source =
				
				"package com.test;\n"
			+   "class FieldsTest {\n"
			+   " static volatile byte byteField;\n"
			+   " private final transient int integerField;\n"
			+   "}\n";

		
		final NameFileSpec spec = new NameFileSpec("FieldsTest.java");
		
		final CompiledAndMappedFiles compiledAndMapped = compileAndMap(spec, source, new TestResolvedTypes());
		
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
