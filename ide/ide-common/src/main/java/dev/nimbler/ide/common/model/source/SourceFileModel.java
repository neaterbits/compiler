package dev.nimbler.ide.common.model.source;

import java.util.ArrayList;
import java.util.List;

import dev.nimbler.compiler.model.common.ISourceToken;
import dev.nimbler.compiler.model.common.IType;
import dev.nimbler.compiler.model.common.SourceTokenVisitor;
import dev.nimbler.compiler.model.common.TypeMemberVisitor;
import dev.nimbler.compiler.model.common.VariableScope;
import dev.nimbler.compiler.util.parse.CompileError;

public interface SourceFileModel {

	void iterate(SourceTokenVisitor visitor, boolean visitPlaceholderElements);

	void iterate(long offset, long length, SourceTokenVisitor visitor, boolean visitPlaceholderElements);

	ISourceToken getSourceTokenAt(long offset);

	void iterateTypeMembers(TypeMemberVisitor typeMemberVisitor);

	IType getVariableType(ISourceToken token);

	List<CompileError> getParserErrors();

	VariableScope getVariableScope(ISourceToken token);
	
	public static ISourceTokenProperties getProperties(ISourceToken token) {

		final List<SourceElementFlag> flags = new ArrayList<>();

		switch (token.getTokenType()) {
		
		case KEYWORD:
			break;
			
		case CLASS_DECLARATION_NAME:
		case INTERFACE_DECLARATION_NAME:
			flags.add(SourceElementFlag.RENAMEABLE);
			flags.add(SourceElementFlag.MOVEABLE);
			break;
			
		case METHOD_DECLARATION_NAME:
			flags.add(SourceElementFlag.RENAMEABLE);
			break;
			
		case CLASS_REFERENCE_NAME:
			flags.add(SourceElementFlag.RENAMEABLE);
			break;
			
		case METHOD_REFERENCE_NAME:
			flags.add(SourceElementFlag.RENAMEABLE);
			break;
			
		case VARIABLE_REFERENCE:
			flags.add(SourceElementFlag.RENAMEABLE);
			break;

		case INSTANCE_VARIABLE_DECLARATION_NAME:
		case STATIC_VARIABLE_DECLARATION_NAME:
		case CALL_PARAMETER_DECLARATION_NAME:
		case LOCAL_VARIABLE_DECLARATION_NAME:
			flags.add(SourceElementFlag.RENAMEABLE);
			break;
			
		case NAMESPACE_DECLARATION_NAME:
			break;

		case IMPORT_NAME:
			break;
			
		case BUILTIN_TYPE_NAME:
			break;
			
		case THIS_REFERENCE:
			break;
			
		case CHARACTER_LITERAL:
		case STRING_LITERAL:
		case INTEGER_LITERAL:
		case BOOLEAN_LITERAL:
		case NULL_LITERAL:
			break;
			
		case ENUM_CONSTANT:
			break;

		case UNKNOWN:
			break;

		}

		return new SourceTokenProperties(new SourceElementMask(flags));
	}
}
