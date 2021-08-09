package dev.nimbler.ide.changehistory.filestorage.zip;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.jutils.IOUtils;

public class ZipFileReaderWriterTest {

    @Test
    public void testReadWrite() throws Exception {
        
        final File file = File.createTempFile("zipfilereaderwriter", ".zip");

        file.deleteOnExit();

        try {
            final Path path = Path.of("the", "file", "path");
            
            final String testString = "testString";

            try (ZipFileWriter writer = new ZipFileWriter(file.toPath())) {
                
                try (OutputStream stream = writer.addFile(path)) {
                    stream.write(testString.getBytes());
                }
            }
            
            final byte [] data;

            try (ZipFileReader reader = new ZipFileReader(file.toPath())) {

                try (InputStream stream = reader.readFile(path)) {
                    
                    data = IOUtils.readAll(stream);
                }
            }
            
            final String readString = new String(data);
            
            assertThat(readString).isEqualTo(testString);
            
        }
        finally {
            file.delete();
        }
    }
}
