package dev.nimbler.compiler.model.common.passes;

import java.util.List;
import java.util.Objects;

@Deprecated
public class CompilerBuilderIntermediate<INPUT, INITIAL_INPUT> {

	private final List<Pass<?, ?>> passes;

	CompilerBuilderIntermediate(List<Pass<?, ?>> passes) {

		Objects.requireNonNull(passes);
		
		this.passes = passes;
	}
	
	@SuppressWarnings("unchecked")
	public <OUTPUT> CompilerBuilderIntermediate<OUTPUT, INITIAL_INPUT> addPass(SinglePass<INPUT, OUTPUT> pass) {

		Objects.requireNonNull(pass);

		passes.add(pass);
		
		return (CompilerBuilderIntermediate<OUTPUT, INITIAL_INPUT>)this;
	}

	@SuppressWarnings("unchecked")
	public <OUTPUT> CompilerBuilderIntermediate<OUTPUT, INITIAL_INPUT> addSingleToMultiPass(SingleToMultiPass<INPUT, OUTPUT> pass) {

		Objects.requireNonNull(pass);

		passes.add(pass);
		
		return (CompilerBuilderIntermediate<OUTPUT, INITIAL_INPUT>)this;
	}

	@SuppressWarnings("unchecked")
	public <OUTPUT> CompilerBuilderIntermediate<OUTPUT, INITIAL_INPUT> addMultiPass(MultiPass<INPUT, OUTPUT> pass) {

		Objects.requireNonNull(pass);

		passes.add(pass);
		
		return (CompilerBuilderIntermediate<OUTPUT, INITIAL_INPUT>)this;
	}

	public LanguageCompilerPasses<INITIAL_INPUT, INPUT> build() {
		return new LanguageCompilerPasses<>(passes);
	}
}
