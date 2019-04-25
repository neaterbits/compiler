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
import com.neaterbits.compiler.ast.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodModifiers;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodStatic;
import com.neaterbits.compiler.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.ast.typedefinition.ComplexTypeDefinition;
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
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.TypeResolveMode;
import com.neaterbits.compiler.util.model.ASTFieldVisitor;
import com.neaterbits.compiler.util.model.ASTMethodVisitor;
import com.neaterbits.compiler.util.model.ASTTypesModel;
import com.neaterbits.compiler.util.model.BuiltinTypeRef;
import com.neaterbits.compiler.util.model.FieldModifiers;
import com.neaterbits.compiler.util.model.LibraryTypeRef;
import com.neaterbits.compiler.util.model.MethodVariant;
import com.neaterbits.compiler.util.model.Mutability;
import com.neaterbits.compiler.util.model.UpdateOnResolve;
import com.neaterbits.compiler.util.model.UserDefinedTypeRef;
import com.neaterbits.compiler.util.model.Visibility;

public final class ASTModelImpl implements ASTTypesModel<CompilationUnit> {

	private final FieldModifiers dataFieldDefaultModifiers;
	
	public ASTModelImpl() {
		this(new FieldModifiers(false, Visibility.NAMESPACE, Mutability.MUTABLE, false, false));
	}
	
	public ASTModelImpl(FieldModifiers dataFieldDefaultModifiers) {

		Objects.requireNonNull(dataFieldDefaultModifiers);
		
		this.dataFieldDefaultModifiers = dataFieldDefaultModifiers;
	}

	@Override
	public void updateOnResolve(CompilationUnit compilationUnit, UpdateOnResolve mode, int elementParseTreeRef, UserDefinedTypeRef type, TypeResolveMode typeResolveMode) {
		
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
	public void replaceWithUserDefinedType(CompilationUnit compilationUnit, int typeReferenceParseTreeRef, UserDefinedTypeRef userType) {
		
		final BaseASTElement element = compilationUnit.getElementFromParseTreeRef(typeReferenceParseTreeRef);
		
		if (!(element instanceof ResolveLaterTypeReference)) {
			throw new IllegalStateException();
		}
		
		element.replaceWith(new ComplexTypeReference(element.getContext(), userType.getTypeName()));
	}

	@Override
	public void replaceWithBuiltinType(CompilationUnit compilationUnit, int typeReferenceParseTreeRef, BuiltinTypeRef builtinType) {

		final BaseASTElement element = compilationUnit.getElementFromParseTreeRef(typeReferenceParseTreeRef);

		if (!(element instanceof ResolveLaterTypeReference)) {
			throw new IllegalStateException();
		}

		element.replaceWith(new BuiltinTypeReference(element.getContext(), builtinType.getTypeName(), builtinType.isScalar()));
	}

	@Override
	public void replaceWithLibraryType(CompilationUnit compilationUnit, int typeReferenceParseTreeRef, LibraryTypeRef libraryType) {

		final BaseASTElement element = compilationUnit.getElementFromParseTreeRef(typeReferenceParseTreeRef);

		if (!(element instanceof ResolveLaterTypeReference)) {
			throw new IllegalStateException();
		}
		
		element.replaceWith(new LibraryTypeReference(element.getContext(), libraryType.getTypeName()));
	}

	@Override
	public int getNumMethods(CompilationUnit compilationUnit, UserDefinedTypeRef userDefinedType) {

		Objects.requireNonNull(compilationUnit);
		Objects.requireNonNull(userDefinedType);
		
		int numMethods = 0;
		
		final ComplexTypeDefinition<?, ?> complexType = (ComplexTypeDefinition<?, ?>)compilationUnit.getElementFromParseTreeRef(userDefinedType.getParseTreeRef());
		
		if (complexType != null && complexType.getMembers() != null) {
		
			for (ComplexMemberDefinition member : complexType.getMembers()) {
				if (member.isMethod()) {
					++ numMethods;
				}
			}
		}
		
		return numMethods;
	}

	@Override
	public void iterateClassMembers(CompilationUnit compilationUnit, UserDefinedTypeRef userDefinedType, ASTFieldVisitor fieldVisitor, ASTMethodVisitor methodVisitor) {

		final ClassDefinition complexType = (ClassDefinition)compilationUnit.getElementFromParseTreeRef(userDefinedType.getParseTreeRef());
		
		iterateClassMembers(complexType, fieldVisitor, methodVisitor);
	}

	private void iterateClassMembers(ClassDefinition complexType, ASTFieldVisitor fieldVisitor, ASTMethodVisitor methodVisitor) {
		
		final Subclassing subclassing = complexType.getModifiers().getModifier(Subclassing.class);
		
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
