package com.neaterbits.compiler.common.loader;

import java.util.Collection;

import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.type.FullTypeName;
import com.neaterbits.compiler.common.ast.type.complex.ComplexType;
import com.neaterbits.compiler.common.ast.typedefinition.ComplexMemberDefinition;

public interface ResolvedType extends TypeInfo {

	TypeSpec getSpec();
	
	FileSpec getFile();
	
	default NamespaceReference getNamespace() {
		return getType() != null ? getType().getNamespace() : null;
	}
	
	default FullTypeName getFullTypeName() {
		return getType() != null ? getType().getFullTypeName() : null;
	}
	
	ComplexType<?> getType();
	
	default int getNumMethods() {
		
		int numMethods = 0;
		
		if (getType() != null && getType().getMembers() != null) {
		
			for (ComplexMemberDefinition member : getType().getMembers()) {
				if (member.isMethod()) {
					++ numMethods;
				}
			}
		}
		
		return numMethods;
	}
	
	
	Collection<ResolvedType> getNestedTypes();

	Collection<ResolvedTypeDependency> getExtendsFrom();
	
	Collection<ResolvedTypeDependency> getDependencies();

}
