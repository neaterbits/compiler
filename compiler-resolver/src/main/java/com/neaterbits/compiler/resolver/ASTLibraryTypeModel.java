package com.neaterbits.compiler.resolver;

import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;

public interface ASTLibraryTypeModel<LIBRARYTYPE> {

	ScopedName getLibraryTypeScopedName(LIBRARYTYPE libraryType);

	TypeName getLibraryTypeName(LIBRARYTYPE libraryType);

}
