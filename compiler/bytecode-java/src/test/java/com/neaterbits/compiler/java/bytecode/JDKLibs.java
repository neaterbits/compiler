package com.neaterbits.compiler.java.bytecode;

import java.io.File;

class JDKLibs {

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

    static File getJavaBaseLibraryPath() {
        
        File file = getRTJarPath();
        
        if (!file.exists()) {
            file = getJavaBaseJModPath();
        }

        return file;
    }
}
