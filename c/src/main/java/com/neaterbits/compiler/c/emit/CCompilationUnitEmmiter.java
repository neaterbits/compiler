package com.neaterbits.compiler.c.emit;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.block.Function;
import com.neaterbits.compiler.common.ast.block.Parameter;
import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.ast.typedefinition.StructDataFieldMember;
import com.neaterbits.compiler.common.ast.typedefinition.StructDefinition;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.base.BaseProceduralProgramEmitter;

public class CCompilationUnitEmmiter extends BaseProceduralProgramEmitter<EmitterState> {

	private static final CStatementEmitter STATEMENT_EMITTER = new CStatementEmitter(); 
	
	private static final CTypeEmitter TYPE_EMITTER = new CTypeEmitter();
	
	private void emitType(TypeReference typeReference, EmitterState param) {
		typeReference.getType().visit(TYPE_EMITTER, param);
	}
	
	@Override
	protected void emitStatement(Statement statement, EmitterState state) {
		statement.visit(STATEMENT_EMITTER, state);
	}

	@Override
	public Void onFunction(Function function, EmitterState param) {

		if (function.getQualifiers().isFileLocal()) {
			param.append("static ");
		
			emitType(function.getReturnType(), param);
			
			param.append(' ');

			param.append(function.getName().getName());
			
			param.append('(');
			
			for (int i = 0; i < function.getParameters().size(); ++ i) {
				if (i > 0) {
					param.append(", ");
				}
				
				final Parameter parameter = function.getParameters().get(i);
				
				emitType(parameter.getType(), param);
				
				param.append(' ').append(parameter.getName());
			}

			param.append(") {");
			
			emitIndentedBlock(function.getBlock(), param);
			
			param.append('}').newline();
		}
		
		return null;
	}

	@Override
	public Void onStructDefinition(StructDefinition structDefinition, EmitterState param) {

		return null;
	}

	@Override
	public Void onStructDataFieldMember(StructDataFieldMember field, EmitterState param) {
		
		return null;
	}
}
