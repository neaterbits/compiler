package com.neaterbits.language.bytecode.java;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class JModClassLib extends BaseZipFileClassLib<ZipFile, ZipEntry> {

    JModClassLib(File path) throws IOException {
        super(path, ZipFile::new);
    }

    @Override
    protected ZipEntry getEntry(ZipFile file, String path) {
        return file.getEntry("classes/" + path);
    }

    @Override
    protected InputStream getInputStream(ZipFile file, ZipEntry entry) throws IOException {
        return file.getInputStream(entry);
    }
}
