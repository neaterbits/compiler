package dev.nimbler.ide.changehistory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * File storage that caches in-memory
 *  
 */
final class InMemoryFiles extends InMemoryCloseable {

    private final Map<Path, byte []> storage;
    
    InMemoryFiles() {
        this.storage = new HashMap<>();
    }
    
    OutputStream addFile(Path filePath) throws IOException {
        
        Objects.requireNonNull(filePath);

        checkClosed();

        return new ByteArrayOutputStream() {

            @Override
            public void write(int b) {
                
                checkClosed();

                super.write(b);
            }

            @Override
            public void write(byte[] b, int off, int len) {
                
                checkClosed();

                super.write(b, off, len);
            }

            @Override
            public void writeBytes(byte[] b) {

                checkClosed();

                super.writeBytes(b);
            }

            @Override
            public void close() throws IOException {
                
                super.close();
                
                final byte [] data = toByteArray();

                storage.put(filePath, data);
            }
        };
    }

    InputStream retrieveFile(Path filePath) throws IOException {

        Objects.requireNonNull(filePath);
        
        final byte [] data = storage.get(filePath);
        
        return data != null
                ? new ByteArrayInputStream(data)
                : null;
    }

    @Override
    public void close() throws Exception {

        try {
            super.close();
        }
        finally {
            storage.clear();
        }
    }
}
