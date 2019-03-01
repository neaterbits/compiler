package com.neaterbits.compiler.c.emit;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.CompilationCode;
import com.neaterbits.compiler.common.ast.block.Function;
import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.EnumConstantDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.EnumDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.StructDataFieldMember;
import com.neaterbits.compiler.common.ast.typedefinition.StructDefinition;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.base.BaseProceduralProgramEmitter;

public class CCompilationUnitEmitter extends BaseProceduralProgramEmitter<EmitterState> {

	private static final CStatementEmitter STATEMENT_EMITTER = new CStatementEmitter(); 
	
	private static final CTypeEmitter TYPE_EMITTER = new CTypeEmitter();
	
	private void emitType(TypeReference typeReference, EmitterState param) {
		typeReference.getType().visit(TYPE_EMITTER, param);
	}
	
	private void emitCode(CompilationCode code, EmitterState param) {
		code.visit(this, param);
	}
	
	@Override
	protected void emitStatement(Statement statement, EmitterState state) {
		statement.visit(STATEMENT_EMITTER, state);
	}

	@Override
	public Void onFunction(Function function, EmitterState param) {

		if (function.getQualifiers().isFileLocal()) {
			param.append("static ");
		}
		
		emitType(function.getReturnType(), param);
		
		param.append(' ');

		param.append(function.getName().getName());
		
		param.append('(');
		
		function.getParameters().foreachWithIndex((parameter, i) -> {
			if (i > 0) {
				param.append(", ");
			}
			
			emitType(parameter.getType(), param);
			
			param.append(' ').append(parameter.getName().getName());
		});

		param.append(") {").newline();
		
		emitIndentedBlock(function.getBlock(), param);
		
		param.append('}').newline();
		
		return null;
	}

	@Override
	public Void onStructDefinition(StructDefinition structDefinition, EmitterState param) {

		param.append("struct ").append(structDefinition.getNameString()).append(" {").newline();
		
		param.addIndent();

		for (ComplexMemberDefinition memberDefinition : structDefinition.getMembers()) {
			
			System.out.println("## emit field " + memberDefinition);
			
			emitCode(memberDefinition, param);
		}
		
		param.subIndent();

		param.append("};").newline();

		return null;
	}
	

	@Override
	public Void onEnumDefinition(EnumDefinition enumDefinition, EmitterState param) {

		param.append("enum");
		
		if (enumDefinition.getName() != null) {
			param.append(' ').append(enumDefinition.getNameString());
		}
		
		param.append(" {").newline();
		
		param.addIndent();
		
		enumDefinition.getConstants().foreachWithIndex((constant, index) -> {

			if (index > 0) {
				param.append(',').newline();
			}
			
			param.append(constant.getName().getName());
		});
		
		param.append(';').newline();
		
		param.subIndent();
		
		param.append('}').newline();
		
		return null;
	}

	@Override
	public Void onEnumConstantDefinition(EnumConstantDefinition enumConstantDefinition, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onStructDataFieldMember(StructDataFieldMember field, EmitterState param) {
		
		emitType(field.getType(), param);
		
		param.append(' ').append(field.getName().getName());
		
		param.append(';').newline();
		
		return null;
	}
}
