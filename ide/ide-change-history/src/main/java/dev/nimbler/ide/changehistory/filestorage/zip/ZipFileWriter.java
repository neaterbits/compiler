package dev.nimbler.ide.changehistory.filestorage.zip;

import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.jutils.PathUtil;

final class ZipFileWriter implements AutoCloseable {

    private final ZipOutputStream zipOutputStream;
    
    ZipFileWriter(Path outputFile) throws IOException {

        this.zipOutputStream = new ZipOutputStream(new FileOutputStream(outputFile.toFile()));
    }
    
    OutputStream addFile(Path path) throws IOException {
    
        final ZipEntry zipEntry = new ZipEntry(PathUtil.getForwardSlashPathString(path));
        
        zipOutputStream.putNextEntry(zipEntry);
        
        return new FilterOutputStream(zipOutputStream) {

            @Override
            public void close() throws IOException {
                
                // intercept close
            }
        };
    }

    @Override
    public void close() throws Exception {
        
        zipOutputStream.close();
    }
}
