package com.neaterbits.compiler.java.emit;

import com.neaterbits.compiler.common.ast.Namespace;
import com.neaterbits.compiler.common.ast.block.Method;
import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.ClassModifier;
import com.neaterbits.compiler.common.ast.typedefinition.ClassModifierVisitor;
import com.neaterbits.compiler.common.ast.typedefinition.ClassStatic;
import com.neaterbits.compiler.common.ast.typedefinition.ClassStrictfp;
import com.neaterbits.compiler.common.ast.typedefinition.ClassVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.InnerClassMember;
import com.neaterbits.compiler.common.ast.typedefinition.MethodMember;
import com.neaterbits.compiler.common.ast.typedefinition.MethodModifierHolder;
import com.neaterbits.compiler.common.ast.typedefinition.MethodModifierVisitor;
import com.neaterbits.compiler.common.ast.typedefinition.MethodNative;
import com.neaterbits.compiler.common.ast.typedefinition.MethodOverride;
import com.neaterbits.compiler.common.ast.typedefinition.MethodStatic;
import com.neaterbits.compiler.common.ast.typedefinition.MethodStrictfp;
import com.neaterbits.compiler.common.ast.typedefinition.MethodSynchronized;
import com.neaterbits.compiler.common.ast.typedefinition.MethodVisibility;
import com.neaterbits.compiler.common.ast.typedefinition.Subclassing;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.base.BaseOOCompilationUnitEmitter;

public class JavaCompilationUnitEmitter extends BaseOOCompilationUnitEmitter<EmitterState> {

	private static final JavaStatementEmitter STATEMENT_EMITTER = new JavaStatementEmitter();
	
	@Override
	protected void emitStatement(Statement statement, EmitterState state) {
		statement.visit(STATEMENT_EMITTER, state);
	}

	private static final ClassModifierVisitor<Void, String> CLASSMODIFIER_TO_NAME = new ClassModifierVisitor<Void, String>() {
		
		@Override
		public String onSubclassing(Subclassing subclassing, Void param) {
			
			final String s;
			
			switch (subclassing) {
			case ABSTRACT: 	s = "abstract"; break;
			case FINAL:		s = "final";	break;

			default:
				throw new UnsupportedOperationException("Unknown subclassing " + subclassing);
			}
			
			return s;
		}
		
		@Override
		public String onStrictFp(ClassStrictfp classStrictfp, Void param) {
			return "strictfp";
		}
		
		@Override
		public String onStatic(ClassStatic classStatic, Void param) {
			return "static";
		}
		
		@Override
		public String onClassVisibility(ClassVisibility visibility, Void param) {
			final String s;
			
			switch (visibility) {
			case PUBLIC: 	s = "public"; 		break;
			case NAMESPACE:	s = "protected";	break;
			case PRIVATE: 	s = "private"; 		break;

			default:
				throw new UnsupportedOperationException("Unknown class visibility " + visibility);
			}
			
			return s;
		}
	};

	private static final MethodModifierVisitor<Void, String> METHODMODIFIER_TO_NAME = new MethodModifierVisitor<Void, String>() {
		
		@Override
		public String onOverride(MethodOverride methodOverride, Void param) {
			
			final String s;
			
			switch (methodOverride) {
			case ABSTRACT: 	s = "abstract"; break;
			case FINAL:		s = "final";	break;

			default:
				throw new UnsupportedOperationException("Unknown subclassing " + methodOverride);
			}
			
			return s;
		}
		
		@Override
		public String onStrictFp(MethodStrictfp methodStrictfp, Void param) {
			return "strictfp";
		}
		
		@Override
		public String onStatic(MethodStatic methodStatic, Void param) {
			return "static";
		}
		
		@Override
		public String onVisibility(MethodVisibility visibility, Void param) {
			final String s;
			
			switch (visibility) {
			case PUBLIC: 					s = "public"; 		break;
			case NAMESPACE_AND_SUBCLASSES:	s = "protected";	break;
			case PRIVATE: 					s = "private"; 		break;

			default:
				throw new UnsupportedOperationException("Unknown class visibility " + visibility);
			}
			
			return s;
		}

		@Override
		public String onSynchronized(MethodSynchronized methodSynchronized, Void param) {
			return "synchronized";
		}

		@Override
		public String onNative(MethodNative methodNative, Void param) {
			return "native";
		}
	};

	@Override
	public Void onClassDefinition(ClassDefinition classDefinition, EmitterState param) {

		final ASTList<? extends ClassModifier> modifiers = classDefinition.getModifiers().getModifiers();
		
		emitList(param, modifiers, " ", modifier -> modifier.visit(CLASSMODIFIER_TO_NAME, null));
		
		if (!modifiers.isEmpty()) {
			param.append(' ');
		}
		
		param.append("class ").append(classDefinition.getName().getName()).append(" {").newline();
		
		param.addIndent();
		
		emitCode(classDefinition.getMembers(), param);

		param.subIndent();
		
		param.append('}').newline();
		
		return null;
	}

	@Override
	public Void onMethod(Method method, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onMethodMember(MethodMember methodMember, EmitterState param) {
		
		final ASTList<MethodModifierHolder> modifiers = methodMember.getModifiers().getModifiers();
		
		emitList(param, modifiers, " ", modifier -> modifier.visit(METHODMODIFIER_TO_NAME, null));
		
		if (!modifiers.isEmpty()) {
			param.append(' ');
		}

		final Method method = methodMember.getMethod();

		param.append(method.getName().getName()).append('(');
		
		param.append(") {").newline();
		
		System.out.println("## emitting method at " + method.getContext());
		
		emitBlock(method.getBlock(), param);
		
		param.append('}').newline();
		
		return null;
	}

	@Override
	public Void onClassDataFieldMember(ClassDataFieldMember field, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onNamespace(Namespace namespace, EmitterState param) {
		param.append("package ").append(namespace.getName()).append(';').newline();
		
		emitCode(namespace.getLines(), param);

		return null;
	}

	@Override
	public Void onInnerClassMember(InnerClassMember field, EmitterState param) {
		throw new UnsupportedOperationException();
	}
}
