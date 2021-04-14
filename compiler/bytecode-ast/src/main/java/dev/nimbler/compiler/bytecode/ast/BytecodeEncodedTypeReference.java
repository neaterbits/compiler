package dev.nimbler.compiler.bytecode.ast;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.typereference.TypeReferenceVisitor;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.language.common.types.TypeName;

public final class BytecodeEncodedTypeReference extends TypeReference {

	private final String typeName;

	public BytecodeEncodedTypeReference(Context context, String typeName) {
		super(context);
	
		Objects.requireNonNull(typeName);
		
		this.typeName = typeName;
	}

	public String getEncodedTypeName() {
		return typeName;
	}

	/*
	@Override
	public BaseType getType() {
		throw new UnsupportedOperationException();
	}
	*/

	@Override
	public TypeName getTypeName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getDebugName() {
		return typeName;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.BYTECODE_ENCODED_TYPE_REFERENCE;
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

	}
}
