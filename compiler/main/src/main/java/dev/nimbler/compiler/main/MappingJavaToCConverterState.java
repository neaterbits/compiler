package dev.nimbler.compiler.main;

import org.jutils.Strings;

import dev.nimbler.compiler.ast.objects.FieldNameDeclaration;
import dev.nimbler.compiler.ast.objects.block.FunctionName;
import dev.nimbler.compiler.ast.objects.block.MethodName;
import dev.nimbler.compiler.ast.objects.type.CompleteName;
import dev.nimbler.compiler.ast.objects.type.primitive.IntType;
import dev.nimbler.compiler.ast.objects.typedefinition.StructName;
import dev.nimbler.compiler.convert.Converters;
import dev.nimbler.compiler.convert.OOToProceduralConverterState;
import dev.nimbler.compiler.language.java.JavaTypes;
import dev.nimbler.compiler.util.name.BaseTypeName;
import dev.nimbler.language.codemap.compiler.CompilerCodeMap;

public class MappingJavaToCConverterState<T extends MappingJavaToCConverterState<T>> extends OOToProceduralConverterState<T> {

	public MappingJavaToCConverterState(Converters<T> converters, CompilerCodeMap codeMap) {

		super(converters, codeMap);
	}

	private static String className(CompleteName type) {
		final StringBuilder sb = new StringBuilder();

		className(type, sb);

		return sb.toString();
	}

	private static void className(CompleteName type, StringBuilder sb) {

		sb.append(Strings.join(type.getNamespace().getParts(), '_'));

		if (type.getOuterTypes() != null) {
			for (BaseTypeName typeName : type.getOuterTypes()) {
				sb.append('_').append(typeName.getName());
			}
		}

		if (sb.length() > 0) {
			sb.append('_');
		}

		sb.append(type.getName().getName());
	}

	@Override
	public final FunctionName methodToFunctionName(CompleteName type, MethodName methodName) {

		final StringBuilder sb = new StringBuilder();

		className(type, sb);

		sb.append('_').append(methodName.getName());

		return new FunctionName(methodName.getContext(), sb.toString());
	}

	@Override
	public final StructName classToStructName(CompleteName completeName) {
		return new StructName(className(completeName));
	}

	@Override
	public final FieldNameDeclaration getVTableBaseFieldName(CompleteName type) {
		return new FieldNameDeclaration(null, "base_" + className(type));
	}

	@Override
	public final FieldNameDeclaration getVTableFunctionFieldName(MethodName methodName) {
		return new FieldNameDeclaration(methodName.getContext(), methodName.getName());
	}

	@Override
	public final String getClassStaticMembersArrayName() {
		return "class_static_members";
	}

	@Override
	public String getClassStaticVTableArrayName() {
		return "class_vtables";
	}

	@Override
	public final IntType getIntType() {
		return JavaTypes.INT_TYPE;
	}
}
