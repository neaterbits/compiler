package dev.nimbler.ide.component.common;

import java.io.IOException;

public interface ComponentIDEAccess extends ConfigurationAccess {

	void writeAndOpenFile(
			String projectName,
			String sourceFolder,
			String [] namespacePath,
			String fileName,
			String text) throws IOException;

	boolean isValidSourceFolder(String projectName, String sourceFolder);
	
	// File getRootPath();
}
