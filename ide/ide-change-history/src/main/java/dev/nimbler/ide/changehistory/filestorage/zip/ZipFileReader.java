package dev.nimbler.ide.changehistory.filestorage.zip;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jutils.PathUtil;

final class ZipFileReader implements AutoCloseable {

    private final ZipFile zipFile;
    
    ZipFileReader(Path outputFile) throws IOException {

        this.zipFile = new ZipFile(outputFile.toFile());
    }
    
    /**
     * Return {@link InputStream} for specified file, or null if not found
     * 
     * @param path the relative {@link Path} of the file in the ZIP file
     * 
     * @return an {@link InputStream} or null if not found
     * 
     * @throws IOException if failed to read
     */
    
    InputStream readFile(Path path) throws IOException {
        
        final ZipEntry zipEntry = zipFile.getEntry(PathUtil.getForwardSlashPathString(path));
        
        return zipEntry != null
                ? zipFile.getInputStream(zipEntry)
                : null;
    }

    @Override
    public void close() throws Exception {

        zipFile.close();
    }
}
