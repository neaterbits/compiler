
package com.neaterbits.compiler.resolver;

import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;

public interface ASTBuiltinTypeModel<BUILTINTYPE> {

	ScopedName getBuiltinTypeScopedName(BUILTINTYPE builtinType);
	
	String getBuiltinTypeNameString(BUILTINTYPE builtinType);
	
	TypeName getBuiltinTypeName(BUILTINTYPE builtinType);
}
