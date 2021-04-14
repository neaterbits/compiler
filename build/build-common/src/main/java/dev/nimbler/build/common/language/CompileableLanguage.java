package dev.nimbler.build.common.language;


import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.language.common.types.TypeName;

public interface CompileableLanguage extends BuildableLanguage {

	TypeName getTypeName(SourceFileResourcePath sourceFile);

	TypeName getTypeName(String namespace, String name);

	String getNamespaceString(TypeName typeName);

	String getCompleteNameString(TypeName typeName);

	String getBinaryName(TypeName typeName);

}
