package com.neaterbits.ide.common.model.source;

import java.util.ArrayList;
import java.util.List;

import com.neaterbits.compiler.model.common.ISourceToken;
import com.neaterbits.compiler.model.common.IType;
import com.neaterbits.compiler.model.common.SourceTokenVisitor;
import com.neaterbits.compiler.model.common.TypeMemberVisitor;
import com.neaterbits.compiler.model.common.VariableScope;
import com.neaterbits.compiler.util.parse.CompileError;

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
