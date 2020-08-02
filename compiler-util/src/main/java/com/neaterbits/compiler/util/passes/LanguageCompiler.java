package com.neaterbits.compiler.util.passes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.ParserException;

public class LanguageCompiler<INPUT, OUTPUT> {

	private final List<Pass<?, ?>> passes;

	LanguageCompiler(List<Pass<?, ?>> passes) {
		
		Objects.requireNonNull(passes);
		
		this.passes = passes;
	}

	public OUTPUT compile(FilePassInput input) throws IOException, ParserException {
		return compile(Arrays.asList(input));
	}
	
	public OUTPUT compile(Collection<? extends FilePassInput> inputs) throws IOException, ParserException {
		
		Objects.requireNonNull(inputs);
		
		if (inputs.isEmpty()) {
			throw new IllegalArgumentException();
		}

		Object curInput = new MultiFilePassInput(inputs);
		
		for (int i = 0; i < passes.size(); ++ i) {
			
			final Pass<?, ?> pass = passes.get(i);

			if (pass instanceof FilePass<?, ?>) {
				
				@SuppressWarnings({ "unchecked", "rawtypes" })
				final FilePass<FilePassInput, Object> filePass = (FilePass)pass;

				final MultiFilePassInput multiFilePassInput = (MultiFilePassInput)curInput;

				final List<Object> outputs = new ArrayList<>(multiFilePassInput.getCount());
				
				for (FilePassInput filePassInput : multiFilePassInput) {

					final Object result = filePass.execute(filePassInput);
					
					if (result == null) {
						throw new IllegalStateException("No result for " + filePassInput.getFile().getDebugName());
					}
					
					outputs.add(result);
				}
				
				curInput = outputs;
			}
			else if (pass instanceof MultiPass<?, ?>) {
				
				@SuppressWarnings({ "unchecked", "rawtypes" })
				final MultiPass<MultiInputOutput, MultiInputOutput> multiPass = (MultiPass)pass;
				
				final MultiInputOutput input = (MultiInputOutput)curInput;
				
				curInput = multiPass.execute(input);
			}
			else if (pass instanceof SingleToMultiPass<?, ?>) {
				
				@SuppressWarnings({ "unchecked", "rawtypes" })
				final SingleToMultiPass<Object, MultiInputOutput> multiPass
							= (SingleToMultiPass)pass;
				
				@SuppressWarnings("unchecked")
				final Collection<Object> input = (Collection<Object>)curInput;
				
				curInput = multiPass.execute(input);
			}
			else {
				throw new UnsupportedOperationException();
			}
			
			System.out.println("## got output " + curInput.getClass().getSimpleName() + " from pass " + pass.getClass().getSimpleName());
		}
		
		
		@SuppressWarnings("unchecked")
		final OUTPUT output = (OUTPUT)curInput;
		
		return output;
	}
}
