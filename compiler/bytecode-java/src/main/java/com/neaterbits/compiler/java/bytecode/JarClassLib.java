package com.neaterbits.compiler.java.bytecode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

final class JarClassLib extends BaseZipFileClassLib<JarFile, JarEntry> {

	JarClassLib(File path) throws IOException {
	    super(path, JarFile::new);
	}

	@Override
    protected JarEntry getEntry(JarFile file, String path) {
        return file.getJarEntry(path);
    }

    @Override
    protected InputStream getInputStream(JarFile file, JarEntry entry) throws IOException {
        return file.getInputStream(entry);
    }
}
