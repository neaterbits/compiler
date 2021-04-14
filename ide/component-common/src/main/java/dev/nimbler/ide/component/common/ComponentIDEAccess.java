package dev.nimbler.ide.component.common;

import java.io.File;
import java.io.IOException;

public interface ComponentIDEAccess {

	void writeAndOpenFile(
			String projectName,
			String sourceFolder,
			String [] namespacePath,
			String fileName,
			String text) throws IOException;

	boolean isValidSourceFolder(String projectName, String sourceFolder);
	
	File getRootPath();
}
