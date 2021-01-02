package com.neaterbits.compiler.ast.objects.typereference;

import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.parse.context.Context;

public abstract class ResolvedTypeReference extends TypeReference {

    private final int typeNo;

	protected ResolvedTypeReference(Context context, int typeNo) {
		super(context);

		if (typeNo < 0) {
		    throw new IllegalArgumentException();
		}

		this.typeNo = typeNo;
	}

    public final int getTypeNo() {
        return typeNo;
    }

    @Override
    public final ParseTreeElement getParseTreeElement() {

        return ParseTreeElement.RESOLVED_TYPE_REFERENCE;
    }
}
