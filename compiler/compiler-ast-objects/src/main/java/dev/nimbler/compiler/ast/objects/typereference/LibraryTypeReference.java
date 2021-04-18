package dev.nimbler.compiler.ast.objects.typereference;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.language.common.types.TypeName;

public class LibraryTypeReference extends ResolvedNamedTypeReference {

	public LibraryTypeReference(Context context, int typeNo, TypeName typeName) {
		super(context, typeNo, typeName);
	}

	private LibraryTypeReference(LibraryTypeReference other) {
	    super(other);
	}
	
	@Override
    public ResolvedTypeReference makeCopy() {
        return new LibraryTypeReference(this);
    }

    @Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

	}
}
