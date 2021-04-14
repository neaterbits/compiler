package com.neaterbits.compiler.java.compile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.java.TestFile;
import com.neaterbits.compiler.java.resolve.TestResolvedTypes;
import com.neaterbits.language.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.language.common.types.TypeName;
import com.neaterbits.language.common.types.TypeVariant;
import com.neaterbits.util.parse.ParserException;

public class CompileFileCollector<COMPILATION_UNIT> {
    
    @FunctionalInterface
    public interface Compiler<COMPILED_AND_MAPPED_FILES> {
        
        COMPILED_AND_MAPPED_FILES compile(
                    List<CompileFile> parseInputs,
                    IntCompilerCodeMap codeMap) throws IOException, ParserException;
    }

	public static class CompileFile {

	    private final String name;
		private final String text;
		
		private CompileFile(String name, String text) {
		    this.name = name;
			this.text = text;
		}

        public String getName() {
            return name;
        }

        public String getText() {
            return text;
        }
	}
	
	private final Compiler<CodeMapCompiledAndMappedFiles<COMPILATION_UNIT>> compiler;
	
	private final List<CompileFile> files;

	public CompileFileCollector(Compiler<CodeMapCompiledAndMappedFiles<COMPILATION_UNIT>> compiler) {
	    
	    Objects.requireNonNull(compiler);
	    
	    this.compiler = compiler;
	    
		this.files = new ArrayList<>();
	}

	public CompileFileCollector<COMPILATION_UNIT> add(TestFile testFile) {

	    return add(testFile.getName(), testFile.getText());
	}

	public CompileFileCollector<COMPILATION_UNIT> add(String name, String text) {

		files.add(new CompileFile(name, text));
		
		return this;
	}

	public CompiledAndMappedFiles compile(TestResolvedTypes resolvedTypes) throws IOException, ParserException {
		return compile(resolvedTypes, new IntCompilerCodeMap());
	}

	public CodeMapCompiledAndMappedFiles<COMPILATION_UNIT>
		compile(TestResolvedTypes resolvedTypes, IntCompilerCodeMap codeMap)
		        throws IOException, ParserException {

        for (TypeName typeName : resolvedTypes.getTypeNames()) {

            final int typeNo = codeMap.addType(TypeVariant.CLASS);

            codeMap.addTypeMapping(typeName, typeNo);
        }

		return compiler.compile(files, codeMap);
	}
}
