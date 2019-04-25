package com.neaterbits.compiler.main;

import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.ast.type.TypeDefType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.ast.type.primitive.ScalarType;
import com.neaterbits.compiler.ast.typereference.BuiltinTypeReference;
import com.neaterbits.compiler.ast.typereference.TypeDefTypeReference;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.convert.ootofunction.BaseTypeReferenceConverter;

final class JavaToCTypeReferenceConverter<T extends MappingJavaToCConverterState<T>> extends BaseTypeReferenceConverter<T> {

	@Override
	public TypeReference onBuiltinTypeReference(BuiltinTypeReference typeReference, T param) {

		final BaseType convertedType = convertBuiltinType(typeReference, param);
		
		final TypeReference converted;
		
		if (convertedType instanceof BuiltinType) {
			converted = new BuiltinTypeReference(
					typeReference.getContext(),
					((BuiltinType) convertedType).getTypeName(),
					convertedType instanceof ScalarType);
		}
		else if (convertedType instanceof TypeDefType) {
			
			final TypeDefType typeDefType = (TypeDefType)convertedType;
			
			converted = new TypeDefTypeReference(
					typeReference.getContext(),
					typeDefType.getTypeName(),
					null);
		}
		else {
			throw new UnsupportedOperationException();
		}
		
		return converted;
	}
}
