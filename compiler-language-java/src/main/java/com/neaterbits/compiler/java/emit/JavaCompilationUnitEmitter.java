package com.neaterbits.compiler.java.emit;

import com.neaterbits.compiler.ast.objects.Namespace;
import com.neaterbits.compiler.ast.objects.block.ClassMethod;
import com.neaterbits.compiler.ast.objects.block.Constructor;
import com.neaterbits.compiler.ast.objects.block.StaticInitializer;
import com.neaterbits.compiler.ast.objects.expression.Expression;
import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.ast.objects.statement.Statement;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassMethodModifierHolder;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassModifiers;
import com.neaterbits.compiler.ast.objects.typedefinition.ConstructorMember;
import com.neaterbits.compiler.ast.objects.typedefinition.ConstructorModifierHolder;
import com.neaterbits.compiler.ast.objects.typedefinition.EnumConstantDefinition;
import com.neaterbits.compiler.ast.objects.typedefinition.EnumDefinition;
import com.neaterbits.compiler.ast.objects.typedefinition.InnerClassMember;
import com.neaterbits.compiler.ast.objects.typedefinition.InterfaceDefinition;
import com.neaterbits.compiler.ast.objects.typedefinition.InterfaceMethod;
import com.neaterbits.compiler.ast.objects.typedefinition.InterfaceMethodMember;
import com.neaterbits.compiler.ast.objects.typedefinition.InterfaceMethodModifierHolder;
import com.neaterbits.compiler.emit.EmitterState;
import com.neaterbits.compiler.emit.base.BaseOOCompilationUnitEmitter;
import com.neaterbits.compiler.util.Strings;
import com.neaterbits.compiler.util.typedefinition.ClassMethodModifierVisitor;
import com.neaterbits.compiler.util.typedefinition.ClassMethodNative;
import com.neaterbits.compiler.util.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.util.typedefinition.ClassMethodStatic;
import com.neaterbits.compiler.util.typedefinition.ClassMethodStrictfp;
import com.neaterbits.compiler.util.typedefinition.ClassMethodSynchronized;
import com.neaterbits.compiler.util.typedefinition.ClassMethodVisibility;
import com.neaterbits.compiler.util.typedefinition.ClassModifier;
import com.neaterbits.compiler.util.typedefinition.ClassModifierVisitor;
import com.neaterbits.compiler.util.typedefinition.ClassStatic;
import com.neaterbits.compiler.util.typedefinition.ClassStrictfp;
import com.neaterbits.compiler.util.typedefinition.ClassVisibility;
import com.neaterbits.compiler.util.typedefinition.ConstructorModifierVisitor;
import com.neaterbits.compiler.util.typedefinition.ConstructorVisibility;
import com.neaterbits.compiler.util.typedefinition.InterfaceAbstract;
import com.neaterbits.compiler.util.typedefinition.InterfaceMethodAbstract;
import com.neaterbits.compiler.util.typedefinition.InterfaceMethodDefault;
import com.neaterbits.compiler.util.typedefinition.InterfaceMethodModifierVisitor;
import com.neaterbits.compiler.util.typedefinition.InterfaceMethodStatic;
import com.neaterbits.compiler.util.typedefinition.InterfaceMethodStrictfp;
import com.neaterbits.compiler.util.typedefinition.InterfaceMethodVisibility;
import com.neaterbits.compiler.util.typedefinition.InterfaceModifier;
import com.neaterbits.compiler.util.typedefinition.InterfaceModifierVisitor;
import com.neaterbits.compiler.util.typedefinition.InterfaceStatic;
import com.neaterbits.compiler.util.typedefinition.InterfaceStrictfp;
import com.neaterbits.compiler.util.typedefinition.InterfaceVisibility;
import com.neaterbits.compiler.util.typedefinition.Subclassing;

public class JavaCompilationUnitEmitter extends BaseOOCompilationUnitEmitter<EmitterState> {

	private static final JavaStatementEmitter STATEMENT_EMITTER = new JavaStatementEmitter();
	
	private static final JavaExpressionEmitter EXPRESSION_EMITTER = new JavaExpressionEmitter();
	
	@Override
	protected void emitStatement(Statement statement, EmitterState state) {
		statement.visit(STATEMENT_EMITTER, state);
	}
	
	private void emitExpression(Expression expression, EmitterState state) {
		expression.visit(EXPRESSION_EMITTER, state);
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
	
	private static final ConstructorModifierVisitor<Void, String> CONSTRUCTORMODIFIER_TO_NAME = new ConstructorModifierVisitor<Void, String>() {

		@Override
		public String onVisibility(ConstructorVisibility visibility, Void param) {
			
			final String s;
			
			switch (visibility) {
			case PUBLIC: s = "public"; break;
			case NAMESPACE_AND_SUBCLASSES: s = "protected"; break;
			case PRIVATE: s = "private"; break;
			
			default:
				throw new UnsupportedOperationException("Unknown constructor visibility " + visibility);
			}
			
			return s;
		}
	}; 

	private static final ClassMethodModifierVisitor<Void, String> CLASSMETHODMODIFIER_TO_NAME = new ClassMethodModifierVisitor<Void, String>() {
		
		@Override
		public String onOverride(ClassMethodOverride methodOverride, Void param) {
			
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
		public String onStrictFp(ClassMethodStrictfp methodStrictfp, Void param) {
			return "strictfp";
		}
		
		@Override
		public String onStatic(ClassMethodStatic methodStatic, Void param) {
			return "static";
		}
		
		@Override
		public String onVisibility(ClassMethodVisibility visibility, Void param) {
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
		public String onSynchronized(ClassMethodSynchronized methodSynchronized, Void param) {
			return "synchronized";
		}

		@Override
		public String onNative(ClassMethodNative methodNative, Void param) {
			return "native";
		}
	};

	private static final InterfaceModifierVisitor<Void, String> INTERFACEMODIFIER_TO_NAME = new InterfaceModifierVisitor<Void, String>() {
		
		@Override
		public String onVisibility(InterfaceVisibility visibility, Void param) {

			final String s;
			
			switch (visibility) {
			case PUBLIC: s = "public"; break;
			case NAMESPACE: s = "protected"; break;
			case PRIVATE: s = "private"; break;
			
			default:
				throw new UnsupportedOperationException("Unknown interface visibility " + visibility);
			}
			
			return s;
		}
		
		@Override
		public String onStrictfp(InterfaceStrictfp interfaceStrictfp, Void param) {
			return "strictfp";
		}
		
		@Override
		public String onStatic(InterfaceStatic interfaceStatic, Void param) {
			return "static";
		}
		
		@Override
		public String onAbstract(InterfaceAbstract interfaceAbstract, Void param) {
			return "abstract";
		}
	};
	
	private static final InterfaceMethodModifierVisitor<Void, String> INTERFACEMETHODMODIFIER_TO_NAME = new InterfaceMethodModifierVisitor<Void, String>() {
		
		@Override
		public String onVisibility(InterfaceMethodVisibility visibility, Void param) {
			
			final String s;
			
			switch (visibility) {
			case PUBLIC: s = "public"; break;
			
			default:
				throw new UnsupportedOperationException("Unknown interface method visibility " + visibility);
			}
			
			return s;
		}
		
		@Override
		public String onStrictfp(InterfaceMethodStrictfp methodStrictfp, Void param) {
			return "strictfp";
		}
		
		@Override
		public String onStatic(InterfaceMethodStatic methodStatic, Void param) {
			return "static";
		}
		
		@Override
		public String onDefault(InterfaceMethodDefault methodDefault, Void param) {
			return "default";
		}
		
		@Override
		public String onAbstract(InterfaceMethodAbstract methodAbstract, Void param) {
			return "abstract";
		}
	};
	
	private void emitClassModifiers(ClassModifiers classModifiers, EmitterState param) {
		final ASTList<? extends ClassModifier> modifiers = classModifiers.getModifiers();
		
		emitList(param, modifiers, " ", modifier -> modifier.visit(CLASSMODIFIER_TO_NAME, null));
		
		if (!modifiers.isEmpty()) {
			param.append(' ');
		}
	}
	
	@Override
	public Void onClassDefinition(ClassDefinition classDefinition, EmitterState param) {

		emitClassModifiers(classDefinition.getModifiers(), param);
		
		param.append("class ").append(classDefinition.getNameString()).append(" {").newline();
		
		param.addIndent();
		
		emitCode(classDefinition.getMembers(), param);

		param.subIndent();
		
		param.append('}').newline();
		
		return null;
	}
	
	@Override
	public Void onEnumDefinition(EnumDefinition enumDefinition, EmitterState param) {

		emitClassModifiers(enumDefinition.getModifiers(), param);
		
		param.append("enum ").append(enumDefinition.getNameString()).append(" {").newline();
		
		param.addIndent();
		
		param.subIndent();
		
		param.append('}');
		
		return null;
	}


	@Override
	public Void onEnumConstantDefinition(EnumConstantDefinition enumConstantDefinition, EmitterState param) {

		param.append(enumConstantDefinition.getName().getName());
		
		if (enumConstantDefinition.getParameters() != null) {
			param.append('(');
			
			emitListTo(param, enumConstantDefinition.getParameters().getList(), ", ", parameter -> emitExpression(parameter, param));
			
			param.append(')');
		}
		
		return null;
	}


	@Override
	public Void onStaticInitializer(StaticInitializer initializer, EmitterState param) {

		param.append("static {").newline();
		
		emitIndentedBlock(initializer.getBlock(), param);
		
		param.append('}');

		return null;
	}

	@Override
	public Void onConstructor(Constructor constructor, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onConstructorMember(ConstructorMember constructorMember, EmitterState param) {
		
		final ASTList<ConstructorModifierHolder> modifiers = constructorMember.getModifiers().getModifiers();
		
		emitList(param, modifiers, " ", modifier -> modifier.visit(CONSTRUCTORMODIFIER_TO_NAME, null));
		
		if (!modifiers.isEmpty()) {
			param.append(' ');
		}

		final Constructor constructor = constructorMember.getConstructor();

		param.append(constructor.getName().getName()).append('(');
		
		param.append(") {").newline();
		
		emitBlock(constructor.getBlock(), param);
		
		param.append('}').newline();

		return null;
	}


	@Override
	public Void onClassMethod(ClassMethod method, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onClassMethodMember(ClassMethodMember methodMember, EmitterState param) {
		
		final ASTList<ClassMethodModifierHolder> modifiers = methodMember.getModifiers().getModifiers();
		
		emitList(param, modifiers, " ", modifier -> modifier.visit(CLASSMETHODMODIFIER_TO_NAME, null));
		
		if (!modifiers.isEmpty()) {
			param.append(' ');
		}

		final ClassMethod method = methodMember.getMethod();

		param.append(method.getName().getName()).append('(');
		
		param.append(") {").newline();
		
		emitIndentedBlock(method.getBlock(), param);
		
		param.append('}').newline();
		
		return null;
	}

	@Override
	public Void onClassDataFieldMember(ClassDataFieldMember field, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onInterfaceDefinition(InterfaceDefinition interfaceDefinition, EmitterState param) {
		final ASTList<? extends InterfaceModifier> modifiers = interfaceDefinition.getModifiers().getModifiers();
		
		emitList(param, modifiers, " ", modifier -> modifier.visit(INTERFACEMODIFIER_TO_NAME, null));
		
		if (!modifiers.isEmpty()) {
			param.append(' ');
		}
		
		param.append("interface ").append(interfaceDefinition.getNameString()).append(" {").newline();
		
		param.addIndent();
		
		emitCode(interfaceDefinition.getMembers(), param);

		param.subIndent();
		
		param.append('}').newline();
		
		return null;
	}

	@Override
	public Void onInterfaceMethod(InterfaceMethod method, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onInterfaceMethodMember(InterfaceMethodMember methodMember, EmitterState param) {
		final ASTList<InterfaceMethodModifierHolder> modifiers = methodMember.getModifiers().getModifiers();
		
		emitList(param, modifiers, " ", modifier -> modifier.visit(INTERFACEMETHODMODIFIER_TO_NAME, null));
		
		if (!modifiers.isEmpty()) {
			param.append(' ');
		}

		final InterfaceMethod method = methodMember.getMethod();

		param.append(method.getName().getName()).append('(');
		
		if (modifiers.contains(h -> h.getModifier() instanceof InterfaceMethodStatic)) {
			throw new UnsupportedOperationException();
		} else if (modifiers.contains(h -> h.getModifier() instanceof InterfaceMethodDefault)) {
			throw new UnsupportedOperationException();
		}
		else {
			param.append(");").newline();
		}

		return null;
	}

	@Override
	public Void onNamespace(Namespace namespace, EmitterState param) {
		
		final String namespaceName = Strings.join(namespace.getParts(), '.');
		
		param.append("package ").append(namespaceName).append(';').newline();
		
		emitCode(namespace.getLines(), param);

		return null;
	}

	@Override
	public Void onInnerClassMember(InnerClassMember field, EmitterState param) {
		throw new UnsupportedOperationException();
	}
}
