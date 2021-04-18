package dev.nimbler.build.language.java.jdk;

import java.io.File;
import java.util.List;

import org.jutils.StringUtils;

class ClassPathHelper {
    
    static void addClassPathOption(
            List<String> dst,
            List<File> files) {

        if (!files.isEmpty()) {
            dst.add("-cp");
    
            dst.add(StringUtils.join(files, ':', File::getPath));
        }
    }
}
