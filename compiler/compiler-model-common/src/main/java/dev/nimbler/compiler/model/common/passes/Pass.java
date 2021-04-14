package dev.nimbler.compiler.model.common.passes;

import java.io.IOException;

import com.neaterbits.util.parse.ParserException;

public abstract class Pass<INPUT, OUTPUT> {

	public abstract OUTPUT execute(INPUT input) throws IOException, ParserException;
	
}
