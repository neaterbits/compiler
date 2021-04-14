package com.neaterbits.build.common.language;


import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.language.common.types.TypeName;

public interface CompileableLanguage extends BuildableLanguage {

	TypeName getTypeName(SourceFileResourcePath sourceFile);

	TypeName getTypeName(String namespace, String name);

	String getNamespaceString(TypeName typeName);

	String getCompleteNameString(TypeName typeName);

	String getBinaryName(TypeName typeName);

}
