package com.neaterbits.compiler.common.model;

import java.io.File;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.Program;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDeclarationName;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceDeclarationName;
import com.neaterbits.compiler.common.parser.ParsedFile;

public final class ObjectProgramModel implements ProgramModel<Program, ParsedFile> {

	private final Program program;
	
	public ObjectProgramModel(Program program) {

		Objects.requireNonNull(program);
		
		this.program = program;
	}

	@Override
	public ParsedFile getSourceFile(File path) {
		
		return (ParsedFile)program.findElement(element -> {
			
			if (element instanceof ParsedFile) {
				final ParsedFile parsedFile = (ParsedFile)element;
				
				if (parsedFile.getFile().getPath().getAbsoluteFile().equals(path.getAbsoluteFile())) {
					return true;
				}
			}
			
			return false;
		});
		
		
	}

	@Override
	public SourceToken getTokenAt(ParsedFile sourceFile, long offset) {

		final BaseASTElement found = sourceFile.findElement(element -> {
			
			final Context context = element.getContext();
			
			return offset >= context.getStartPos() && offset <= context.getEndPos();
		});
		
		return found != null ? makeSourceToken(found) : null;
	}
	
	private static SourceToken makeSourceToken(BaseASTElement element) {
		
		Objects.requireNonNull(element);

		final SourceTokenType sourceTokenType;
		
		if (element instanceof ClassDeclarationName) {
			sourceTokenType = SourceTokenType.CLASS_DECLARATION_NAME;
		}
		else if (element instanceof InterfaceDeclarationName) {
			sourceTokenType = SourceTokenType.INTERFACE_DECLARATION_NAME;
		}
		else {
			
			sourceTokenType = SourceTokenType.UNKNOWN;
			
		}
		
		final Context context = element.getContext();

		return new SourceToken(
				sourceTokenType,
				context.getStartPos(),
				context.getText().length());

	}
}
