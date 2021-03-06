package dev.nimbler.compiler.c.emit;

import dev.nimbler.compiler.ast.objects.CompilationCode;
import dev.nimbler.compiler.ast.objects.block.Function;
import dev.nimbler.compiler.ast.objects.statement.Statement;
import dev.nimbler.compiler.ast.objects.typedefinition.ComplexMemberDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.EnumConstantDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.EnumDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.StructDataFieldMember;
import dev.nimbler.compiler.ast.objects.typedefinition.StructDefinition;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.emit.EmitterState;
import dev.nimbler.compiler.emit.base.BaseProceduralProgramEmitter;

public class CCompilationUnitEmitter extends BaseProceduralProgramEmitter<EmitterState> {

	private static final CStatementEmitter STATEMENT_EMITTER = new CStatementEmitter(); 
	
	private static final CTypeEmitter TYPE_EMITTER = new CTypeEmitter();
	
	private void emitType(TypeReference typeReference, EmitterState param) {
		getType(typeReference).visit(TYPE_EMITTER, param);
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
		
		param.append(' ').append(field.getNames().get(0).getName());
		
		param.append(';').newline();
		
		return null;
	}
}
