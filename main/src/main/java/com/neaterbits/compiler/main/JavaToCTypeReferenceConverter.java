package com.neaterbits.compiler.main;

import com.neaterbits.compiler.common.BuiltinTypeReference;
import com.neaterbits.compiler.common.TypeDefTypeReference;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.type.TypeDefType;
import com.neaterbits.compiler.common.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.common.convert.ootofunction.BaseTypeReferenceConverter;

final class JavaToCTypeReferenceConverter<T extends MappingJavaToCConverterState<T>> extends BaseTypeReferenceConverter<T> {

	@Override
	public TypeReference onBuiltinTypeReference(BuiltinTypeReference typeReference, T param) {

		final BaseType convertedType = convertType(typeReference.getType(), param);
		
		final TypeReference converted;
		
		if (convertedType instanceof BuiltinType) {
			converted = new BuiltinTypeReference(
					typeReference.getContext(),
					(BuiltinType)convertedType);
		}
		else if (convertedType instanceof TypeDefType) {
			converted = new TypeDefTypeReference(
					typeReference.getContext(),
					(TypeDefType)convertedType);
		}
		else {
			throw new UnsupportedOperationException();
		}
		
		return converted;
	}
}
