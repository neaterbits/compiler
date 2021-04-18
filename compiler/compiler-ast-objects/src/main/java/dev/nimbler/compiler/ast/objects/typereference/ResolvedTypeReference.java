package dev.nimbler.compiler.ast.objects.typereference;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.types.ParseTreeElement;

public abstract class ResolvedTypeReference extends TypeReference {

    private final int typeNo;

	protected ResolvedTypeReference(Context context, int typeNo) {
		super(context);

		if (typeNo < 0) {
		    throw new IllegalArgumentException();
		}

		this.typeNo = typeNo;
	}
	
	protected ResolvedTypeReference(ResolvedTypeReference other) {
	    this(other.getContext(), other.typeNo);
	}
	
	public abstract ResolvedTypeReference makeCopy();

    public final int getTypeNo() {
        return typeNo;
    }

    @Override
    public final ParseTreeElement getParseTreeElement() {

        return ParseTreeElement.RESOLVED_TYPE_REFERENCE;
    }
}
