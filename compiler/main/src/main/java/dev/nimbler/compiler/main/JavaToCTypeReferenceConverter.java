package dev.nimbler.compiler.main;

import dev.nimbler.compiler.ast.objects.type.BaseType;
import dev.nimbler.compiler.ast.objects.type.TypeDefType;
import dev.nimbler.compiler.ast.objects.type.primitive.ScalarType;
import dev.nimbler.compiler.ast.objects.typereference.ScalarTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.TypeDefTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.convert.ootofunction.BaseTypeReferenceConverter;

final class JavaToCTypeReferenceConverter<T extends MappingJavaToCConverterState<T>> extends BaseTypeReferenceConverter<T> {

	@Override
	public TypeReference onScalarTypeReference(ScalarTypeReference typeReference, T param) {

		final BaseType convertedType = convertBuiltinType(typeReference, param);

		final TypeReference converted;

		if (convertedType instanceof ScalarType) {
			converted = new ScalarTypeReference(typeReference.getContext(), -1, ((ScalarType) convertedType).getTypeName());
		}
		else if (convertedType instanceof TypeDefType) {

			final TypeDefType typeDefType = (TypeDefType)convertedType;

			converted = new TypeDefTypeReference(
					typeReference.getContext(),
					-1,
					typeDefType.getTypeName(),
					null);
		}
		else {
			throw new UnsupportedOperationException();
		}

		return converted;
	}
}
