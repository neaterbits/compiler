package com.neaterbits.compiler.util.passes;

import java.io.IOException;

public abstract class Pass<INPUT, OUTPUT> {

	public abstract OUTPUT execute(INPUT input) throws IOException;
	
}
