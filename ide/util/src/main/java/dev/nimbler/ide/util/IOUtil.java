package dev.nimbler.ide.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class IOUtil {

	public static String readAll(File file) throws IOException {
		
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		final byte [] buf = new byte[10000];
		
		try (FileInputStream inputStream = new FileInputStream(file)) {
			
			for (;;) {
				final int bytesRead = inputStream.read(buf);
				
				if (bytesRead < 0) {
					break;
				}
				
				baos.write(buf, 0, bytesRead);
			}
		}
		
		return new String(baos.toByteArray());
	}
	
	public static void writeAll(File file, String text) throws IOException {
		
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			outputStream.write(text.getBytes());
		}
	}
}
