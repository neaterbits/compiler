package com.neaterbits.compiler.java.bytecode;

import java.io.IOException;
import java.io.InputStream;

import com.neaterbits.compiler.bytecode.common.DependencyFile;
import com.neaterbits.compiler.common.TypeName;

abstract class JavaClassLib {

	abstract boolean hasClassName(TypeName className);
	
	abstract InputStream openClassFile(TypeName className) throws IOException;

	abstract DependencyFile getDependencyFile(TypeName className);
	
	static String toPath(TypeName className) {

		final StringBuilder sb = new StringBuilder();
		
		final String [] namespaceParts = className.getNamespace();
		
		for (int i = 0; i < namespaceParts.length; ++ i) {
			sb.append(namespaceParts[i]);
			sb.append('/');
		}

		if (className.getOuterTypes() != null) {
			for (String outerType : className.getOuterTypes()) {
				sb.append(outerType);
				sb.append('$');
			}
		}
		
		sb.append(className.getName()).append(".class");
		
		return sb.toString();
	}
}
