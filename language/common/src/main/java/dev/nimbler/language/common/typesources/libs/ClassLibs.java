package dev.nimbler.language.common.typesources.libs;

import java.util.List;

import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.typesources.DependencyFile;

public interface ClassLibs {

	DependencyFile getDependencyFileFor(TypeName typeName);

	List<DependencyFile> getFiles();
}
