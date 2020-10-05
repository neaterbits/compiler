package com.neaterbits.compiler.java.bytecode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.bytecode.common.DependencyFile;
import com.neaterbits.compiler.util.model.TypeSource;

public abstract class BaseZipFileClassLib<FILE extends ZipFile, ENTRY extends ZipEntry> extends JavaClassLib {

    @FunctionalInterface
    interface CreateZip<FILE> {
        
        FILE create(File path) throws IOException;
    }
    
    private final File path;
    private final FILE zipFile;
    
    protected abstract ENTRY getEntry(FILE file, String path);
    
    protected abstract InputStream getInputStream(FILE file, ENTRY entry) throws IOException;
    
    BaseZipFileClassLib(File path, CreateZip<FILE> createFile) throws IOException {
        this.path = path;
        this.zipFile = createFile.create(path);
    }

    @Override
    final boolean hasClassName(TypeName className) {
        return getEntry(zipFile, toPath(className)) != null;
    }

    @Override
    final InputStream openClassFile(TypeName className) throws IOException {
        
        Objects.requireNonNull(className);
        
        final String path = toPath(className);
        
        final ENTRY zipEntry = getEntry(zipFile, path);
        
        if (zipEntry == null) {
            throw new IllegalStateException("No jar entry for " + path + "/" + className.toDebugString());
        }
        
        return getInputStream(zipFile, zipEntry);
    }

    @Override
    final DependencyFile getDependencyFile(TypeName className) {
        return new DependencyFile(path, TypeSource.LIBRARY);
    }

    @Override
    final List<DependencyFile> getFiles() {
        return Arrays.asList(new DependencyFile(path, TypeSource.LIBRARY));
    }

}
