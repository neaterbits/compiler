package com.neaterbits.compiler.util.model;


public interface CompilationUnitModel<COMPILATION_UNIT>
		extends ImportsModel<COMPILATION_UNIT>,
		        SourceTokenModel<COMPILATION_UNIT>,
		        ParseTreeModel<COMPILATION_UNIT> {

}
