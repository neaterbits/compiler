package dev.nimbler.language.bytecode.java;

import java.io.File;
import java.util.Arrays;

import com.neaterbits.util.Strings;

import dev.nimbler.language.common.types.TypeName;

public class JDKLibs {

	public static TypeName parseClassName(String className) {

		final String [] parts = Strings.split(className, '.');
		
		return new TypeName(
				Arrays.copyOf(parts, parts.length - 1),
				null,
				parts[parts.length - 1]);
	}
	
    private static File getRTJarPath() {
        
        final String jreDir = System.getProperty("java.home");
        
        final File file = new File(jreDir + "/lib/rt.jar");

        return file;
    }
    
    private static File getJavaBaseJModPath() {
        
        final String jreDir = System.getProperty("java.home");
        
        final File file = new File(jreDir + "/jmods/java.base.jmod");

        return file;
    }

    public static File getJavaBaseLibraryPath() {
        
        File file = getRTJarPath();
        
        if (!file.exists()) {
            file = getJavaBaseJModPath();
        }

        return file;
    }
}
