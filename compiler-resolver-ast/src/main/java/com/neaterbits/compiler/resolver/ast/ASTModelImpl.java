package com.neaterbits.compiler.resolver.ast;

import java.util.Objects;

import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.block.ClassMethod;
import com.neaterbits.compiler.ast.block.Parameter;
import com.neaterbits.compiler.ast.expression.MethodInvocationExpression;
import com.neaterbits.compiler.ast.statement.ASTMutability;
import com.neaterbits.compiler.ast.statement.FieldTransient;
import com.neaterbits.compiler.ast.statement.FieldVolatile;
import com.neaterbits.compiler.ast.type.complex.ClassType;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.ast.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodModifiers;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodStatic;
import com.neaterbits.compiler.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.ast.typedefinition.FieldModifier;
import com.neaterbits.compiler.ast.typedefinition.FieldModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.FieldStatic;
import com.neaterbits.compiler.ast.typedefinition.FieldVisibility;
import com.neaterbits.compiler.ast.typedefinition.Subclassing;
import com.neaterbits.compiler.ast.typereference.BuiltinTypeReference;
import com.neaterbits.compiler.ast.typereference.ComplexTypeReference;
import com.neaterbits.compiler.ast.typereference.LibraryTypeReference;
import com.neaterbits.compiler.ast.typereference.ResolveLaterTypeReference;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.resolver.ASTFieldVisitor;
import com.neaterbits.compiler.resolver.ASTMethodVisitor;
import com.neaterbits.compiler.resolver.ASTTypesModel;
import com.neaterbits.compiler.resolver.ReferenceType;
import com.neaterbits.compiler.resolver.UpdateOnResolve;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.TypeResolveMode;
import com.neaterbits.compiler.util.model.FieldModifiers;
import com.neaterbits.compiler.util.model.MethodVariant;
import com.neaterbits.compiler.util.model.Mutability;
import com.neaterbits.compiler.util.model.Visibility;

public class ASTModelImpl implements ASTTypesModel<CompilationUnit, BuiltinType, ComplexType<?, ?, ?>, TypeName> {

	private final FieldModifiers dataFieldDefaultModifiers;
	
	public ASTModelImpl() {
		this(new FieldModifiers(false, Visibility.NAMESPACE, Mutability.MUTABLE, false, false));
	}
	
	public ASTModelImpl(FieldModifiers dataFieldDefaultModifiers) {

		Objects.requireNonNull(dataFieldDefaultModifiers);
		
		this.dataFieldDefaultModifiers = dataFieldDefaultModifiers;
	}

	@Override
	public ScopedName getBuiltinTypeScopedName(BuiltinType builtinType) {
		return builtinType.getCompleteName().toScopedName();
	}

	@Override
	public String getBuiltinTypeNameString(BuiltinType builtinType) {
		return builtinType.getName().getName();
	}

	@Override
	public TypeName getBuiltinTypeName(BuiltinType builtinType) {
		return builtinType.getCompleteName().toTypeName();
	}

	@Override
	public ScopedName getLibraryTypeScopedName(TypeName libraryType) {
		return libraryType.toScopedName();
	}

	@Override
	public TypeName getLibraryTypeName(TypeName libraryType) {
		return libraryType;
	}

	@Override
	public ResolvedTypeDependency makeResolvedTypeDependency(
			TypeName completeName,
			ReferenceType referenceType,
			TypeResolveMode typeResolveMode,
			TypeVariant typeVariant,
			CompiledTypeDependency compiledTypeDependency) {
		
		return new ResolvedTypeDependency(
				completeName,
				referenceType,
				compiledTypeDependency.getTypeReferenceElementRef(),
				typeResolveMode,
				typeVariant,
				compiledTypeDependency.getUpdateOnResolve(),
				compiledTypeDependency.getUpdateOnResolveElementRef());
	}

	@Override
	public void updateOnResolve(CompilationUnit compilationUnit, UpdateOnResolve mode, int elementParseTreeRef, ComplexType<?, ?, ?> type, TypeResolveMode typeResolveMode) {
		
		switch (mode) {
		case METHOD_INVOCATION_EXPRESSION:
			
			final MethodInvocationExpression methodInvocationExpression = (MethodInvocationExpression)compilationUnit.getElementFromParseTreeRef(elementParseTreeRef);
			final ResolveLaterTypeReference resolveLaterTypeReference = (ResolveLaterTypeReference)methodInvocationExpression.getClassType();
			
			final ScopedName toResolve = resolveLaterTypeReference.getScopedName();
			
			MethodInvocationExpressionResolver.updateOnResolve(toResolve, type, typeResolveMode, methodInvocationExpression);
			break;
			
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public void replaceWithComplexType(CompilationUnit compilationUnit, int typeReferenceParseTreeRef, ComplexType<?, ?, ?> complexType) {
		
		final BaseASTElement element = compilationUnit.getElementFromParseTreeRef(typeReferenceParseTreeRef);
		
		if (!(element instanceof ResolveLaterTypeReference)) {
			throw new IllegalStateException();
		}
		
		element.replaceWith(new ComplexTypeReference(element.getContext(), complexType));
	}

	@Override
	public void replaceWithBuiltinType(CompilationUnit compilationUnit, int typeReferenceParseTreeRef, BuiltinType builtinType) {

		final BaseASTElement element = compilationUnit.getElementFromParseTreeRef(typeReferenceParseTreeRef);

		if (!(element instanceof ResolveLaterTypeReference)) {
			throw new IllegalStateException();
		}

		element.replaceWith(new BuiltinTypeReference(element.getContext(), builtinType));
	}

	@Override
	public void replaceWithLibraryType(CompilationUnit compilationUnit, int typeReferenceParseTreeRef, TypeName libraryType) {

		final BaseASTElement element = compilationUnit.getElementFromParseTreeRef(typeReferenceParseTreeRef);

		if (!(element instanceof ResolveLaterTypeReference)) {
			throw new IllegalStateException();
		}
		
		element.replaceWith(new LibraryTypeReference(element.getContext(), libraryType));
	}

	@Override
	public int getNumMethods(ComplexType<?, ?, ?> complextype) {
		int numMethods = 0;
		
		if (complextype != null && complextype.getMembers() != null) {
		
			for (ComplexMemberDefinition member : complextype.getMembers()) {
				if (member.isMethod()) {
					++ numMethods;
				}
			}
		}
		
		return numMethods;
	}

	@Override
	public void iterateClassMembers(ComplexType<?, ?, ?> complexType, ASTFieldVisitor fieldVisitor, ASTMethodVisitor methodVisitor) {
		iterateClassMembers((ClassType)complexType, fieldVisitor, methodVisitor);
	}

	private void iterateClassMembers(ClassType complexType, ASTFieldVisitor fieldVisitor, ASTMethodVisitor methodVisitor) {
		
		final Subclassing subclassing = complexType.getDefinition().getModifiers().getModifier(Subclassing.class);
		
		int fieldIdx = 0;
		int methodIdx = 0;
		
		for (ComplexMemberDefinition memberDefinition : complexType.getMembers()) {
			
			if (memberDefinition instanceof ClassDataFieldMember) {
				
				final ClassDataFieldMember classDataFieldMember = (ClassDataFieldMember)memberDefinition;

				visitDataField(classDataFieldMember, fieldIdx, fieldVisitor);
				
				fieldIdx ++;
			}
			else if (memberDefinition instanceof ClassMethodMember) {

				final ClassMethodMember classMethodMember = (ClassMethodMember)memberDefinition;
				
				final MethodVariant methodVariant = findMethodVariant(classMethodMember, subclassing);
				
				visitClassMethod(classMethodMember, methodVariant, methodIdx, methodVisitor);
				
				++ methodIdx;
			}
		}
	}
	
	private void visitDataField(ClassDataFieldMember dataField, int indexInType, ASTFieldVisitor visitor) {
		
		boolean isStatic = dataFieldDefaultModifiers.isStatic();
		boolean isTransient = dataFieldDefaultModifiers.isTransient();
		boolean isVolatile = dataFieldDefaultModifiers.isVolatile();
		Visibility visibility = dataFieldDefaultModifiers.getVisibility();
		Mutability mutability = dataFieldDefaultModifiers.getMutability();
		
		for (FieldModifierHolder fieldModifierHolder : dataField.getModifiers().getModifiers()) {
			
			final FieldModifier fieldModifier = fieldModifierHolder.getModifier();
			
			if (fieldModifier instanceof FieldStatic) {
				isStatic = true;
			}
			else if (fieldModifier instanceof FieldTransient) {
				isTransient = true;
			}
			else if (fieldModifier instanceof FieldVolatile) {
				isVolatile = true;
			}
			else if (fieldModifier instanceof FieldVisibility) {
				visibility = ((FieldVisibility)fieldModifier).getVisibility();
			}
			else if (fieldModifier instanceof ASTMutability) {
				mutability = ((ASTMutability)fieldModifier).getMutability();
			}
			else {
				throw new UnsupportedOperationException();
			}
		}

		visitor.onField(
				dataField.getNameString(),
				dataField.getType().getTypeName(),
				0,
				isStatic,
				visibility,
				mutability,
				isVolatile,
				isTransient,
				indexInType);
	}
	
	private MethodVariant findMethodVariant(ClassMethodMember classMethodMember, Subclassing subclassing) {

		final ClassMethodModifiers modifiers = classMethodMember.getModifiers();

		final MethodVariant methodVariant;
		
		if (modifiers.hasModifier(ClassMethodStatic.class)) {
			methodVariant = MethodVariant.STATIC;
		}
		else {
			final ClassMethodOverride methodOverride = modifiers.getModifier(ClassMethodOverride.class);
			
			if (methodOverride != null) {
				if (methodOverride == ClassMethodOverride.ABSTRACT) {
					methodVariant = MethodVariant.ABSTRACT;
				}
				else if (methodOverride == ClassMethodOverride.FINAL) {
					methodVariant = MethodVariant.FINAL_IMPLEMENTATION;
				}
				else {
					throw new UnsupportedOperationException();
				}
			}
			else {
				methodVariant = subclassing != null && subclassing == Subclassing.FINAL
						? MethodVariant.FINAL_IMPLEMENTATION
						: MethodVariant.OVERRIDABLE_IMPLEMENTATION;
			}
		}
		
		return methodVariant;
	}
	
	private void visitClassMethod(ClassMethodMember classMethodMember, MethodVariant methodVariant, int indexInType, ASTMethodVisitor visitor) {

		final ClassMethod classMethod = classMethodMember.getMethod();
		
		final TypeName [] parameterTypes = new TypeName[classMethod.getParameters().size()];
		
		// final int [] parameterTypes = new int[classMethod.getParameters().size()]; 
		
		int i = 0;
		
		for (Parameter parameter : classMethod.getParameters()) {
			final TypeReference namedType = parameter.getType();
			
			parameterTypes[i ++] = namedType.getTypeName();
		}
		
		final TypeReference returnType = classMethod.getReturnType();
		
		visitor.onMethod(
				classMethod.getName().getName(),
				methodVariant,
				returnType.getTypeName(),
				parameterTypes,
				indexInType);
	}
}
