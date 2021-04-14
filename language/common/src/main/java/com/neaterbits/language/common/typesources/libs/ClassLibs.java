package com.neaterbits.language.common.typesources.libs;

import java.util.List;

import com.neaterbits.language.common.types.TypeName;
import com.neaterbits.language.common.typesources.DependencyFile;

public interface ClassLibs {

	DependencyFile getDependencyFileFor(TypeName typeName);

	List<DependencyFile> getFiles();
}
