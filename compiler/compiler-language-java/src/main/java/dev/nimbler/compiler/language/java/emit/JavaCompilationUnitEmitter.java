package dev.nimbler.compiler.language.java.emit;

import com.neaterbits.util.Strings;

import dev.nimbler.compiler.ast.objects.Namespace;
import dev.nimbler.compiler.ast.objects.block.ClassMethod;
import dev.nimbler.compiler.ast.objects.block.Constructor;
import dev.nimbler.compiler.ast.objects.block.StaticInitializer;
import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.ast.objects.statement.Statement;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDataFieldMember;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassMethodMember;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassMethodModifierHolder;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassModifiers;
import dev.nimbler.compiler.ast.objects.typedefinition.ConstructorMember;
import dev.nimbler.compiler.ast.objects.typedefinition.ConstructorModifierHolder;
import dev.nimbler.compiler.ast.objects.typedefinition.EnumConstantDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.EnumDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.FieldModifierHolder;
import dev.nimbler.compiler.ast.objects.typedefinition.FieldModifiers;
import dev.nimbler.compiler.ast.objects.typedefinition.InnerClassMember;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceMethod;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceMethodMember;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceMethodModifierHolder;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.variables.InitializerVariableDeclarationElement;
import dev.nimbler.compiler.ast.objects.variables.VariableDeclarationElement;
import dev.nimbler.compiler.emit.EmitterState;
import dev.nimbler.compiler.emit.base.BaseOOCompilationUnitEmitter;
import dev.nimbler.compiler.types.statement.ASTMutability;
import dev.nimbler.compiler.types.typedefinition.ClassMethodModifierVisitor;
import dev.nimbler.compiler.types.typedefinition.ClassMethodNative;
import dev.nimbler.compiler.types.typedefinition.ClassMethodOverride;
import dev.nimbler.compiler.types.typedefinition.ClassMethodStatic;
import dev.nimbler.compiler.types.typedefinition.ClassMethodStrictfp;
import dev.nimbler.compiler.types.typedefinition.ClassMethodSynchronized;
import dev.nimbler.compiler.types.typedefinition.ClassMethodVisibility;
import dev.nimbler.compiler.types.typedefinition.ClassModifier;
import dev.nimbler.compiler.types.typedefinition.ClassModifierVisitor;
import dev.nimbler.compiler.types.typedefinition.ClassStatic;
import dev.nimbler.compiler.types.typedefinition.ClassStrictfp;
import dev.nimbler.compiler.types.typedefinition.ClassVisibility;
import dev.nimbler.compiler.types.typedefinition.ConstructorModifierVisitor;
import dev.nimbler.compiler.types.typedefinition.ConstructorVisibility;
import dev.nimbler.compiler.types.typedefinition.FieldModifierVisitor;
import dev.nimbler.compiler.types.typedefinition.FieldStatic;
import dev.nimbler.compiler.types.typedefinition.FieldTransient;
import dev.nimbler.compiler.types.typedefinition.FieldVisibility;
import dev.nimbler.compiler.types.typedefinition.FieldVolatile;
import dev.nimbler.compiler.types.typedefinition.InterfaceAbstract;
import dev.nimbler.compiler.types.typedefinition.InterfaceMethodAbstract;
import dev.nimbler.compiler.types.typedefinition.InterfaceMethodDefault;
import dev.nimbler.compiler.types.typedefinition.InterfaceMethodModifierVisitor;
import dev.nimbler.compiler.types.typedefinition.InterfaceMethodStatic;
import dev.nimbler.compiler.types.typedefinition.InterfaceMethodStrictfp;
import dev.nimbler.compiler.types.typedefinition.InterfaceMethodVisibility;
import dev.nimbler.compiler.types.typedefinition.InterfaceModifier;
import dev.nimbler.compiler.types.typedefinition.InterfaceModifierVisitor;
import dev.nimbler.compiler.types.typedefinition.InterfaceStatic;
import dev.nimbler.compiler.types.typedefinition.InterfaceStrictfp;
import dev.nimbler.compiler.types.typedefinition.InterfaceVisibility;
import dev.nimbler.compiler.types.typedefinition.Subclassing;
import dev.nimbler.compiler.util.Base;
import dev.nimbler.language.common.types.TypeName;

public class JavaCompilationUnitEmitter extends BaseOOCompilationUnitEmitter<EmitterState> {

	private static final JavaStatementEmitter STATEMENT_EMITTER = new JavaStatementEmitter();	
	private static final JavaExpressionEmitter EXPRESSION_EMITTER = new JavaExpressionEmitter();
	
    private final void emitType(TypeReference typeReference, EmitterState state) {
        
        final TypeName typeName = typeReference.getTypeName();
        
        typeName.join('.', state::append);
    }

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
            case ABSTRACT:  s = "abstract"; break;
            case FINAL:     s = "final";    break;

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
            case PUBLIC:                    s = "public";       break;
            case NAMESPACE_AND_SUBCLASSES:  s = "protected";    break;
            case PRIVATE:                   s = "private";      break;

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
    
    private static final FieldModifierVisitor<Void, String> CLASSFIELDMODIFIER_TO_NAME = new FieldModifierVisitor<Void, String>() {
        
        
        @Override
        public String onFieldVisibility(FieldVisibility fieldVisibility, Void param) {

            final String s;
            
            switch (fieldVisibility.getVisibility()) {
            case PUBLIC:                    s = "public";       break;
            case NAMESPACE_AND_SUBCLASSES:  s = "protected";    break;
            case PRIVATE:                   s = "private";      break;

            default:
                throw new UnsupportedOperationException("Unknown field visibility " + fieldVisibility);
            }
            
            return s;
        }

        @Override
        public String onFieldMutability(ASTMutability fieldMutability, Void param) {
            
            final String s;
            
            switch (fieldMutability.getMutability()) {
            case VALUE_OR_REF_IMMUTABLE:
                s = "final";
                break;
                
            default:
                throw new UnsupportedOperationException("Unknown field mutability " + fieldMutability);
            }
            
            return s;
        }

        @Override
        public String onStatic(FieldStatic fieldStatic, Void param) {

            return "static";
        }

        @Override
        public String onTransient(FieldTransient fieldTransient, Void param) {

            return "transient";
        }

        @Override
        public String onVolatile(FieldVolatile fieldVolatile, Void param) {

            return "volatile";
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

    private void emitFieldModifiers(FieldModifiers fieldModifiers, EmitterState param) {
        final ASTList<? extends FieldModifierHolder> modifiers = fieldModifiers.getModifiers();
        
        emitList(param, modifiers, " ", modifier -> modifier.getModifier().visit(CLASSFIELDMODIFIER_TO_NAME, null));
        
        if (!modifiers.isEmpty()) {
            param.append(' ');
        }
    }
    
    private void emitVarNameDeclaration(VariableDeclarationElement declaration, EmitterState state) {
        
        state.append(declaration.getNameString());
        
        if (declaration.getNumDims() > 0) {

            state.append('[');

            state.append(declaration.getNumDims(), Base.DECIMAL);
            
            state.append(']');
        }
    }
    
    private void emitInitializer(InitializerVariableDeclarationElement initializerElement, EmitterState state) {
        
        emitVarNameDeclaration(initializerElement, state);
        
        if (initializerElement.getInitializer() != null) {
            
            state.append(' ');

            emitExpression(initializerElement.getInitializer(), state);
        }
    }
	
	@Override
	public Void onClassDataFieldMember(ClassDataFieldMember field, EmitterState param) {
	    
	    emitFieldModifiers(field.getModifiers(), param);
	    
	    emitType(field.getType(), param);
	    
	    emitListTo(param, field.getInitializers(), ",", initializer -> emitInitializer(initializer, param));
	    
	    return null;
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
