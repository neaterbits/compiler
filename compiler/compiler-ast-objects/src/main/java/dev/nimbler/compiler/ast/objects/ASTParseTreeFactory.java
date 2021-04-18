package dev.nimbler.compiler.ast.objects;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.annotation.Annotation;
import dev.nimbler.compiler.ast.objects.annotation.AnnotationElement;
import dev.nimbler.compiler.ast.objects.block.Block;
import dev.nimbler.compiler.ast.objects.block.ClassMethod;
import dev.nimbler.compiler.ast.objects.block.Constructor;
import dev.nimbler.compiler.ast.objects.block.ConstructorInvocationStatement;
import dev.nimbler.compiler.ast.objects.block.MethodName;
import dev.nimbler.compiler.ast.objects.block.Parameter;
import dev.nimbler.compiler.ast.objects.block.ParameterModifierHolder;
import dev.nimbler.compiler.ast.objects.block.ParameterModifiers;
import dev.nimbler.compiler.ast.objects.block.ParameterName;
import dev.nimbler.compiler.ast.objects.block.StaticInitializer;
import dev.nimbler.compiler.ast.objects.expression.ArrayAccessExpression;
import dev.nimbler.compiler.ast.objects.expression.ArrayCreationExpression;
import dev.nimbler.compiler.ast.objects.expression.AssignmentExpression;
import dev.nimbler.compiler.ast.objects.expression.BlockLambdaExpression;
import dev.nimbler.compiler.ast.objects.expression.CastExpression;
import dev.nimbler.compiler.ast.objects.expression.ClassInstanceCreationExpression;
import dev.nimbler.compiler.ast.objects.expression.ConditionalExpression;
import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.expression.ExpressionList;
import dev.nimbler.compiler.ast.objects.expression.FieldAccess;
import dev.nimbler.compiler.ast.objects.expression.LambdaExpression;
import dev.nimbler.compiler.ast.objects.expression.LambdaExpressionParameters;
import dev.nimbler.compiler.ast.objects.expression.NestedExpression;
import dev.nimbler.compiler.ast.objects.expression.ParameterList;
import dev.nimbler.compiler.ast.objects.expression.PrimaryList;
import dev.nimbler.compiler.ast.objects.expression.Resource;
import dev.nimbler.compiler.ast.objects.expression.SingleLambdaExpression;
import dev.nimbler.compiler.ast.objects.expression.ThisPrimary;
import dev.nimbler.compiler.ast.objects.expression.UnresolvedMethodInvocationExpression;
import dev.nimbler.compiler.ast.objects.expression.arithemetic.unary.UnaryExpression;
import dev.nimbler.compiler.ast.objects.expression.literal.BooleanLiteral;
import dev.nimbler.compiler.ast.objects.expression.literal.CharacterLiteral;
import dev.nimbler.compiler.ast.objects.expression.literal.FloatingPointLiteral;
import dev.nimbler.compiler.ast.objects.expression.literal.IntegerLiteral;
import dev.nimbler.compiler.ast.objects.expression.literal.Literal;
import dev.nimbler.compiler.ast.objects.expression.literal.NullLiteral;
import dev.nimbler.compiler.ast.objects.expression.literal.Primary;
import dev.nimbler.compiler.ast.objects.expression.literal.StringLiteral;
import dev.nimbler.compiler.ast.objects.expression.literal.UnresolvedClassExpression;
import dev.nimbler.compiler.ast.objects.expression.literal.UnresolvedNamePrimary;
import dev.nimbler.compiler.ast.objects.generics.NamedGenericTypeParameter;
import dev.nimbler.compiler.ast.objects.generics.ReferenceTypeArgument;
import dev.nimbler.compiler.ast.objects.generics.TypeArgument;
import dev.nimbler.compiler.ast.objects.generics.TypeBound;
import dev.nimbler.compiler.ast.objects.generics.WildcardTypeArgument;
import dev.nimbler.compiler.ast.objects.statement.AssignmentStatement;
import dev.nimbler.compiler.ast.objects.statement.BreakStatement;
import dev.nimbler.compiler.ast.objects.statement.CatchBlock;
import dev.nimbler.compiler.ast.objects.statement.ConditionBlock;
import dev.nimbler.compiler.ast.objects.statement.ConstantSwitchCaseLabel;
import dev.nimbler.compiler.ast.objects.statement.DefaultSwitchCaseLabel;
import dev.nimbler.compiler.ast.objects.statement.DoWhileStatement;
import dev.nimbler.compiler.ast.objects.statement.ElseBlock;
import dev.nimbler.compiler.ast.objects.statement.ElseIfConditionBlock;
import dev.nimbler.compiler.ast.objects.statement.EnumConstant;
import dev.nimbler.compiler.ast.objects.statement.EnumSwitchCaseLabel;
import dev.nimbler.compiler.ast.objects.statement.ExpressionStatement;
import dev.nimbler.compiler.ast.objects.statement.ForInit;
import dev.nimbler.compiler.ast.objects.statement.ForStatement;
import dev.nimbler.compiler.ast.objects.statement.ForUpdateExpressionList;
import dev.nimbler.compiler.ast.objects.statement.IfConditionBlock;
import dev.nimbler.compiler.ast.objects.statement.IfElseIfElseStatement;
import dev.nimbler.compiler.ast.objects.statement.IteratorForStatement;
import dev.nimbler.compiler.ast.objects.statement.ReturnStatement;
import dev.nimbler.compiler.ast.objects.statement.Statement;
import dev.nimbler.compiler.ast.objects.statement.SwitchCaseGroup;
import dev.nimbler.compiler.ast.objects.statement.SwitchCaseLabel;
import dev.nimbler.compiler.ast.objects.statement.SwitchCaseStatement;
import dev.nimbler.compiler.ast.objects.statement.ThrowStatement;
import dev.nimbler.compiler.ast.objects.statement.TryCatchFinallyStatement;
import dev.nimbler.compiler.ast.objects.statement.TryWithResourcesStatement;
import dev.nimbler.compiler.ast.objects.statement.VariableDeclarationStatement;
import dev.nimbler.compiler.ast.objects.statement.WhileStatement;
import dev.nimbler.compiler.ast.objects.type.primitive.BooleanType;
import dev.nimbler.compiler.ast.objects.type.primitive.BuiltinType;
import dev.nimbler.compiler.ast.objects.type.primitive.Char16Type;
import dev.nimbler.compiler.ast.objects.type.primitive.FloatingPointType;
import dev.nimbler.compiler.ast.objects.type.primitive.IntegerType;
import dev.nimbler.compiler.ast.objects.type.primitive.NumericType;
import dev.nimbler.compiler.ast.objects.type.primitive.StringType;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDataFieldMember;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDeclarationName;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassMethodMember;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassMethodModifierHolder;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassMethodModifiers;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassModifierHolder;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassModifiers;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassOrInterfaceName;
import dev.nimbler.compiler.ast.objects.typedefinition.ComplexMemberDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.ConstructorMember;
import dev.nimbler.compiler.ast.objects.typedefinition.ConstructorModifierHolder;
import dev.nimbler.compiler.ast.objects.typedefinition.ConstructorModifiers;
import dev.nimbler.compiler.ast.objects.typedefinition.ConstructorName;
import dev.nimbler.compiler.ast.objects.typedefinition.EnumConstantDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.EnumConstantName;
import dev.nimbler.compiler.ast.objects.typedefinition.EnumDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.FieldModifierHolder;
import dev.nimbler.compiler.ast.objects.typedefinition.FieldModifiers;
import dev.nimbler.compiler.ast.objects.typedefinition.FieldName;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceDeclarationName;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceMethod;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceMethodMember;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceMethodModifierHolder;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceMethodModifiers;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceModifierHolder;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceModifiers;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceName;
import dev.nimbler.compiler.ast.objects.typedefinition.VariableModifierHolder;
import dev.nimbler.compiler.ast.objects.typedefinition.VariableModifiers;
import dev.nimbler.compiler.ast.objects.typereference.ScalarTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.typereference.UnresolvedTypeReference;
import dev.nimbler.compiler.ast.objects.variables.ArrayAccessReference;
import dev.nimbler.compiler.ast.objects.variables.InitializerVariableDeclarationElement;
import dev.nimbler.compiler.ast.objects.variables.ModifiersVariableDeclarationElement;
import dev.nimbler.compiler.ast.objects.variables.NameReference;
import dev.nimbler.compiler.ast.objects.variables.PrimaryListVariableReference;
import dev.nimbler.compiler.ast.objects.variables.VarName;
import dev.nimbler.compiler.ast.objects.variables.VarNameDeclaration;
import dev.nimbler.compiler.ast.objects.variables.VariableReference;
import dev.nimbler.compiler.parser.listener.stackbased.ParseTreeFactory;
import dev.nimbler.compiler.types.ReferenceType;
import dev.nimbler.compiler.types.block.ConstructorInvocation;
import dev.nimbler.compiler.types.operator.Operator;
import dev.nimbler.compiler.types.operator.UnaryOperator;
import dev.nimbler.compiler.types.typedefinition.ClassMethodModifier;
import dev.nimbler.compiler.types.typedefinition.ClassModifier;
import dev.nimbler.compiler.types.typedefinition.ConstructorModifier;
import dev.nimbler.compiler.types.typedefinition.FieldModifier;
import dev.nimbler.compiler.types.typedefinition.InterfaceMethodModifier;
import dev.nimbler.compiler.types.typedefinition.InterfaceModifier;
import dev.nimbler.compiler.types.typedefinition.TypeBoundType;
import dev.nimbler.compiler.types.typedefinition.VariableModifier;
import dev.nimbler.compiler.util.Base;
import dev.nimbler.compiler.util.FullContextProvider;
import dev.nimbler.compiler.util.name.ClassName;
import dev.nimbler.compiler.util.name.NamespaceReference;
import dev.nimbler.compiler.util.parse.FieldAccessType;
import dev.nimbler.language.common.types.ScopedName;
import dev.nimbler.language.common.types.TypeName;

public class ASTParseTreeFactory implements ParseTreeFactory<
	Keyword,
	Name,
	NameList,
	TypeReference,
	CompilationUnit,
	Import,
	CompilationCode,
	Namespace,
	ComplexMemberDefinition,
	StaticInitializer,
	Annotation,
	AnnotationElement,
	ClassModifierHolder,
	TypeArgument,
	ReferenceTypeArgument,
	WildcardTypeArgument,
	TypeBound,
	NamedGenericTypeParameter,
	ClassDefinition,
	ConstructorMember,
	ConstructorModifierHolder,
	ConstructorName,
	Constructor,
	ConstructorInvocationStatement,
	ClassMethodMember,
	ClassMethodModifierHolder,
	ClassDataFieldMember,
	FieldModifierHolder,
	InterfaceModifierHolder,
	InterfaceDefinition,
	InterfaceMethodMember,
	InterfaceMethodModifierHolder,
	EnumDefinition,
	EnumConstantDefinition,
	VariableModifierHolder,
	Block,
	Statement,
	Expression,
	Primary,
	VariableReference,
	Literal,
	ExpressionList,
	AssignmentExpression,
	CastExpression,
	ConditionalExpression,
	UnaryExpression,
	LambdaExpression,
	SingleLambdaExpression,
	BlockLambdaExpression,
	LambdaExpressionParameters,
	PrimaryList,
	UnresolvedNamePrimary,
	NestedExpression,
	FieldAccess,
	ThisPrimary,
	ClassInstanceCreationExpression,
	UnresolvedMethodInvocationExpression,
	ArrayCreationExpression,
	ArrayAccessExpression,
	UnresolvedClassExpression,
	NameReference,
	IntegerLiteral,
	FloatingPointLiteral,
	BooleanLiteral,
	CharacterLiteral,
	StringLiteral,
	NullLiteral,

	ParameterModifierHolder,
	Parameter,

	InitializerVariableDeclarationElement,
	VariableDeclarationStatement,

	ExpressionStatement,
	AssignmentStatement,

	ForInit,
	ForStatement,

	IteratorForStatement,

	WhileStatement,
	DoWhileStatement,

	Resource,
	CatchBlock,
	TryCatchFinallyStatement,
	TryWithResourcesStatement,

	ReturnStatement,
	ThrowStatement,

	IfElseIfElseStatement,
	ConditionBlock,
    IfConditionBlock,
    ElseIfConditionBlock,
    ElseBlock,

	SwitchCaseLabel,
	ConstantSwitchCaseLabel,
	EnumConstant,
	EnumSwitchCaseLabel,
	DefaultSwitchCaseLabel,
	SwitchCaseGroup,
	SwitchCaseStatement,
	BreakStatement> {

	private final Collection<BuiltinType> builtinTypes;
	private final GetBuiltinTypeNo getBuiltinTypeNo;

	@FunctionalInterface
	public interface GetBuiltinTypeNo {

	    int getTypeNo(TypeName typeName);
	}

	public ASTParseTreeFactory(Collection<BuiltinType> builtinTypes, GetBuiltinTypeNo getBuiltinTypeNo) {

		Objects.requireNonNull(builtinTypes);

		this.builtinTypes = builtinTypes;
		this.getBuiltinTypeNo = getBuiltinTypeNo;
	}

	@Override
	public CompilationUnit createCompilationUnit(
	        Context context,
	        List<Import> imports,
	        List<CompilationCode> code,
	        FullContextProvider fullContextProvider) {
	    
		return new CompilationUnit(context, imports, code, fullContextProvider);
	}

	private static String [] getIdentifierArray(List<Name> identifiers, int length) {

		if (length <= 0) {
			throw new IllegalArgumentException();
		}

		final String [] parts = new String[length];

		for (int i = 0; i < length; ++ i) {
			parts[i] = identifiers.get(i).getText();
		}

		return parts;
	}

	private static NamespaceReference getNamespaceReference(List<Name> identifiers, int length) {

		return new NamespaceReference(getIdentifierArray(identifiers, length));
	}

	@Override
	public Import createImport(
			Context context,
			Keyword importKeyword,
			Keyword staticKeyword,
			List<Name> identifiers,
			boolean ondemand) {

		final ImportName importName;

		if (ondemand) {

			if (staticKeyword != null) {

				if (identifiers.size() <= 1) {
					throw new IllegalArgumentException();
				}

				final NamespaceReference namespaceOrTypeName = getNamespaceReference(identifiers, identifiers.size() - 1);
				final String classOrInterfaceName = identifiers.get(identifiers.size() - 1).getText();

				importName = new ImportName(context, namespaceOrTypeName, new ClassOrInterfaceName(classOrInterfaceName), null);
			}
			else {
				importName = new ImportName(context, getIdentifierArray(identifiers, identifiers.size()));
			}
		}
		else {

			if (staticKeyword != null) {

				if (identifiers.size() <= 2) {
					throw new IllegalArgumentException();
				}

				final NamespaceReference namespaceOrTypeName = getNamespaceReference(identifiers, identifiers.size() - 2);
				final String classOrInterfaceName = identifiers.get(identifiers.size() - 2).getText();

				final Name methodName = identifiers.get(identifiers.size() - 1);

				importName = new ImportName(
						context,
						namespaceOrTypeName,
						new ClassOrInterfaceName(classOrInterfaceName),
						new MethodName(methodName.getContext(), methodName.getText()));

			}
			else {
				final NamespaceReference namespaceOrTypeName = getNamespaceReference(identifiers, identifiers.size() - 1);
				final String classOrInterfaceName = identifiers.get(identifiers.size() - 1).getText();

				importName = new ImportName(
						context,
						namespaceOrTypeName,
						new ClassOrInterfaceName(classOrInterfaceName));
			}
		}

		return new Import(context, importKeyword, importName);
	}

	@Override
	public Keyword createKeyword(Context context, String name) {
		return new Keyword(context, name);
	}

	@Override
	public Name createName(Context context, String name) {
		return new Name(context, name);
	}

	@Override
    public NameList createNameList(Context context, List<Name> names) {

	    return new NameList(context, names);
    }

    @Override
    public TypeReference createScalarTypeReference(Context context, String name) {

        final TypeName typeName = new TypeName(null, null, name);
        
        final int typeNo = getBuiltinTypeNo.getTypeNo(typeName);

        return new ScalarTypeReference(context, typeNo, typeName);
    }

    @Override
    public TypeReference createUnresolvedTypeReference(Context context, ScopedName name,
            Collection<TypeArgument> genericTypeParameters, ReferenceType type) {

        return new UnresolvedTypeReference(context, name, genericTypeParameters, type);
    }

	@Override
	public Namespace createNamespace(Context context, Keyword namespaceKeyword,
			String[] parts, Context nameContext, List<CompilationCode> codeLines) {

		return new Namespace(
				context,
				namespaceKeyword,
				parts,
				nameContext,
				new CompilationCodeLines(context, codeLines));
	}

	@Override
	public ClassModifierHolder createClassModifierHolder(Context context, ClassModifier classModifier) {
		return new ClassModifierHolder(context, classModifier);
	}

	@Override
    public ReferenceTypeArgument createReferenceTypeArgument(Context context, TypeReference typeReference) {

	    return new ReferenceTypeArgument(context, typeReference);
    }

    @Override
    public WildcardTypeArgument createWildcardTypeArgument(Context context, List<TypeBound> bounds) {

        return new WildcardTypeArgument(context, bounds);
    }

    @Override
    public TypeBound createTypeBound(Context context, TypeBoundType type, TypeReference typeReference) {

        return new TypeBound(context, type, typeReference);
    }

    @Override
    public NamedGenericTypeParameter createNamedTypeParameter(Context context, Name name, List<TypeBound> bounds) {

        return new NamedGenericTypeParameter(context, name, bounds);
    }

    @Override
	public ClassDefinition createClassDefinition(Context context, List<Annotation> annotations, List<ClassModifierHolder> modifiers,
			Keyword classKeyword, String name, Context nameContext,
			List<NamedGenericTypeParameter> genericTypes,
			Keyword extendsKeyword, List<TypeReference> extendsClasses,
			List<TypeReference> implementsInterfaces,
			List<ComplexMemberDefinition> members) {

		return new ClassDefinition(
				context,
				new ClassModifiers(annotations, modifiers),
				classKeyword,
				new ClassDeclarationName(nameContext, new ClassName(name)),
				genericTypes,
				extendsKeyword,
				extendsClasses,
				implementsInterfaces,
				members);
	}


	@Override
	public ConstructorInvocationStatement createConstructorInvocationStatement(Context context,
			ConstructorInvocation type, Expression expression, List<Expression> parameters) {

		final ParameterList parameterList = new ParameterList(
				parameters != null
					? parameters
					: Collections.emptyList());

		final ConstructorInvocationStatement statement = new ConstructorInvocationStatement(
				context,
				type,
				expression,
				parameterList);

		return statement;
	}

	@Override
	public ConstructorMember createConstructorMember(
	        Context context,
	        List<Annotation> annotations, List<ConstructorModifierHolder> modifiers,
			String name, Context nameContext,
			List<Parameter> parameters,
            List<TypeReference> thrownExceptions,
			List<Statement> statements) {

		final Constructor constructor = new Constructor(
				context,
				new ConstructorName(nameContext, name),
				parameters,
				thrownExceptions,
				new Block(context, statements));

		final ConstructorMember constructorMember = new ConstructorMember(
				context,
				new ConstructorModifiers(annotations, modifiers),
				constructor);

		return constructorMember;
	}

	@Override
	public StaticInitializer createStaticInitializer(Context context, List<Statement> statements) {
		return new StaticInitializer(context, new Block(context, statements));
	}

	@Override
    public AnnotationElement createAnnotationElementFromExpression(Context context, Name name, Expression value) {
        return new AnnotationElement(context, name, value);
    }

    @Override
    public AnnotationElement createAnnotationElementFromAnnotation(Context context, Name name, Annotation annotation) {
        return new AnnotationElement(context, name, annotation);
    }

    @Override
    public AnnotationElement createAnnotationElementFromElements(Context context, Name name, List<AnnotationElement> elements) {
        return new AnnotationElement(context, name, elements);
    }

    @Override
    public Annotation createAnnotation(Context context, ScopedName typeName, List<AnnotationElement> elements) {
        return new Annotation(context, typeName, elements);
    }

    @Override
	public ConstructorModifierHolder createConstructorModifierHolder(Context context, ConstructorModifier modifier) {
		return new ConstructorModifierHolder(context, modifier);
	}

	@Override
	public ConstructorName createConstructorName(Context context, String name) {
		return new ConstructorName(context, name);
	}

	@Override
	public ClassMethodMember createClassMethodMember(
	        Context context,
	        List<Annotation> annotations, List<ClassMethodModifierHolder> modifiers,
	        List<NamedGenericTypeParameter> genericTypes,
			TypeReference returnType, String name, Context nameContext,List<Parameter> parameters,
			List<TypeReference> thrownExceptions,
			List<Statement> block) {

		final ClassMethod classMethod = new ClassMethod(
				context,
				genericTypes,
				returnType,
				name,
				nameContext,
				parameters,
				thrownExceptions,
				new Block(context, block));

		final ClassMethodMember methodMember = new ClassMethodMember(
				context,
				new ClassMethodModifiers(annotations, modifiers),
				classMethod);

		return methodMember;
	}


	@Override
	public ClassMethodModifierHolder createClassMethodModifierHolder(Context context, ClassMethodModifier modifier) {
		return new ClassMethodModifierHolder(context, modifier);
	}

	@Override
	public ClassDataFieldMember createClassFieldMember(Context context, List<Annotation> annotations, List<FieldModifierHolder> modifiers,
			TypeReference type, List<InitializerVariableDeclarationElement> initializers) {

		return new ClassDataFieldMember(
				context,
				new FieldModifiers(annotations, modifiers),
				type,
				initializers);
	}

	@Override
	public FieldModifierHolder createFieldModifierHolder(Context context, FieldModifier modifier) {
		return new FieldModifierHolder(context, modifier);
	}

	@Override
	public InterfaceModifierHolder createInterfaceModifierHolder(Context context, InterfaceModifier modifier) {
		return new InterfaceModifierHolder(context, modifier);
	}

	@Override
	public InterfaceDefinition createInterfaceDefinition(Context context, List<Annotation> annotations, List<InterfaceModifierHolder> modifiers,
			Keyword interfaceKeyword, String name, Context nameContext, Keyword extendsKeyword,
			List<TypeReference> extendsInterfaces, List<ComplexMemberDefinition> members) {

		final InterfaceModifiers interfaceModifiers = new InterfaceModifiers(annotations, modifiers);

		final InterfaceDefinition classDefinition = new InterfaceDefinition(
				context,
				interfaceModifiers,
				interfaceKeyword,
				new InterfaceDeclarationName(nameContext, new InterfaceName(name)),
				extendsInterfaces,
				members);

		return classDefinition;
	}

	@Override
	public InterfaceMethodMember createInterfaceMethodMember(
			Context context,
			List<Annotation> annotations,
			List<InterfaceMethodModifierHolder> modifiers,
			TypeReference returnType,
			String name,
			Context nameContext,
			List<Parameter> parameters,
			List<Statement> block) {

		final InterfaceMethod method = new InterfaceMethod(context, returnType, name, nameContext, parameters);

		final InterfaceMethodMember methodMember = new InterfaceMethodMember(context,
				new InterfaceMethodModifiers(annotations, modifiers), method);

		return methodMember;
	}

	@Override
	public InterfaceMethodModifierHolder createInterfaceMethodModifierHolder(
			Context context,
			InterfaceMethodModifier modifier) {

		return new InterfaceMethodModifierHolder(context, modifier);
	}


	@Override
	public EnumDefinition createEnumDefinition(Context context, List<Annotation> annotations, List<ClassModifierHolder> modifiers,
			Keyword enumKeyword, String name, Context nameContext, Keyword implementsKeyword,
			List<TypeReference> implementsInterfaces, List<EnumConstantDefinition> enumConstants,
			List<ComplexMemberDefinition> members) {

		final EnumDefinition enumDefinition = new EnumDefinition(
				context,
				new ClassModifiers(annotations, modifiers),
				enumKeyword,
				new ClassDeclarationName(nameContext, new ClassName(name)),
				implementsInterfaces, enumConstants, members);

		return enumDefinition;
	}


	@Override
	public EnumConstantDefinition createEnumConstantDefinition(Context context, String name, List<Expression> parameters,
			List<ComplexMemberDefinition> members) {

		return new EnumConstantDefinition(
				context,
				new EnumConstantName(name),
				parameters != null ? new ParameterList(parameters) : null,
				members);
	}

	@Override
	public Block createBlock(Context context, List<Statement> statements) {
		return new Block(context, statements);
	}

	@Override
	public Parameter createParameter(
	        Context context,
	        List<Annotation> annotations, List<ParameterModifierHolder> modifiers,
	        TypeReference type,
	        String name, Context nameContext,
	        boolean varArgs) {

		return new Parameter(
		        nameContext,
		        new ParameterModifiers(annotations, modifiers),
		        type,
		        new ParameterName(nameContext, name),
		        varArgs);
	}

	@Override
	public Context getExpressionContext(Expression expression) {
		return expression.getContext();
	}

	@Override
	public ExpressionList createExpressionList(Context context, List<Operator> operators, List<Expression> expressions) {
		return new ExpressionList(context, operators, expressions);
	}

	@Override
	public AssignmentExpression createAssignmentExpression(Context context, VariableReference lhs, Expression rhs) {
		return new AssignmentExpression(context, lhs, rhs);
	}

	@Override
	public NestedExpression createNestedExpression(Context context, Expression nestedExpression) {
		return new NestedExpression(context, nestedExpression);
	}

	@Override
	public CastExpression createCastExpression(Context context, TypeReference castType, Expression expression) {
		return new CastExpression(context, castType, expression);
	}

	@Override
	public ConditionalExpression createConditionalExpression(Context context, Expression part1, Expression part2, Expression part3) {
		return new ConditionalExpression(context, part1, part2, part3);
	}

	@Override
    public UnaryExpression createUnaryExpression(Context context, UnaryOperator operator, Expression expression) {
	    return new UnaryExpression(context, operator, expression);
    }

	@Override
	public LambdaExpressionParameters createLambdaExpressionParameters(Context context, String singleParameter) {
		return new LambdaExpressionParameters(context, singleParameter);
	}

	@Override
	public LambdaExpressionParameters createLambdaExpressionParameters(Context context, List<String> inferredParameters) {
		return new LambdaExpressionParameters(context, inferredParameters);
	}

	@Override
	public SingleLambdaExpression createSingleLambdaExpression(Context context, LambdaExpressionParameters parameters, Expression expression) {
		return new SingleLambdaExpression(context, parameters, expression);
	}

	@Override
	public BlockLambdaExpression createBlockLambdaExpression(Context context, LambdaExpressionParameters parameters, List<Statement> statements) {
		return new BlockLambdaExpression(context, parameters, new Block(context, statements));
	}

	@Override
	public NameReference createNameReference(Context context, String name) {
		return new NameReference(context, name);
	}

	@Override
	public PrimaryList createPrimaryList(Context context, List<Primary> list) {
		return new PrimaryList(context, list);
	}

	@Override
    public UnresolvedNamePrimary createNamePrimary(Context context, String name) {
        return new UnresolvedNamePrimary(context, name);
    }

    @Override
	public ThisPrimary createThisPrimary(Context context, TypeReference classType) {
		return new ThisPrimary(context, classType);
	}

	@Override
	public FieldAccess createFieldAccess(Context context, FieldAccessType type, TypeReference classType, String fieldName) {

		return new FieldAccess(context, type, classType, new FieldName(fieldName));
	}

	@Override
	public ClassInstanceCreationExpression createClassInstanceCreationExpression(Context context, TypeReference type,
			ConstructorName name, List<Expression> parameters, List<ClassMethodMember> anonymousClassMethods) {

		return new ClassInstanceCreationExpression(context, type, name, new ParameterList(parameters), anonymousClassMethods);
	}

    @Override
	public UnresolvedMethodInvocationExpression createUnresolvedMethodInvocationExpression(
	        Context context,
	        // MethodInvocationType type,
			// TypeReference classType,
			// Expression object,
			String methodName,
			Context methodNameContext,
			List<Expression> parameters) {

		return new UnresolvedMethodInvocationExpression(
				context,
				// type,
				// classType,
				// object,
				new MethodName(methodNameContext, methodName),
				new ParameterList(parameters));
	}

	@Override
	public ArrayCreationExpression createArrayCreationExpression(
			Context context,
			TypeReference type,
			List<Expression> dimExpressions,
			int numDims) {

		return new ArrayCreationExpression(context, type, dimExpressions, numDims);
	}

	@Deprecated
	@Override
	public UnresolvedClassExpression createClassExpression(Context context, String className, int numArrayDims) {
		return new UnresolvedClassExpression(context, new ClassOrInterfaceName(className), numArrayDims);
	}

	@Override
	public ArrayAccessExpression createArrayAccessExpression(Context context, Primary primary, Expression expression) {
		return new ArrayAccessExpression(context, primary, expression);
	}

	@SuppressWarnings("unchecked")
	private <T extends BuiltinType> T getType(Class<T> typeClass) {
		return (T)builtinTypes.stream()
				.filter(type -> type.getClass().equals(typeClass))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

	@SuppressWarnings("unchecked")
	private <T extends NumericType> T getNumericalType(boolean isInteger, int bits) {
		return (T)builtinTypes.stream()
				.filter(type -> type instanceof NumericType)
				.map(type -> (NumericType)type)
				.filter(type -> type.isInteger() == isInteger && type.getNumBits() == bits)
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

	@Override
	public IntegerLiteral createIntegerLiteral(Context context, long value, Base base, boolean signed, int bits) {

	    final IntegerType type = getNumericalType(true, bits);
	    final int typeNo = getBuiltinTypeNo.getTypeNo(type.getTypeName());

		return new IntegerLiteral(context, value, base, signed, bits, type, typeNo);
	}

	@Override
	public FloatingPointLiteral createFloatingPointLiteral(Context context, BigDecimal value, Base base, int bits) {

	    final FloatingPointType type = getNumericalType(false, bits);
	    final int typeNo = getBuiltinTypeNo.getTypeNo(type.getTypeName());

		return new FloatingPointLiteral(context, value, base, bits, type, typeNo);
	}

	@Override
	public BooleanLiteral createBooleanLiteral(Context context, boolean value) {

	    final BooleanType type = getType(BooleanType.class);
	    final int typeNo = getBuiltinTypeNo.getTypeNo(type.getTypeName());

		return new BooleanLiteral(context, value, type, typeNo);
	}

	@Override
	public CharacterLiteral createCharacterLiteral(Context context, char value) {

	    final Char16Type type = getType(Char16Type.class);
	    final int typeNo = getBuiltinTypeNo.getTypeNo(type.getTypeName());

		return new CharacterLiteral(context, value, type, typeNo);
	}

	@Override
	public StringLiteral createStringLiteral(Context context, String value) {

	    final StringType type = getType(StringType.class);
	    final int typeNo = getBuiltinTypeNo.getTypeNo(type.getTypeName());

		return new StringLiteral(context, value, type, typeNo);
	}

	@Override
	public NullLiteral createNullLiteral(Context context) {
		return new NullLiteral(context);
	}

	@Override
	public VariableModifierHolder createVariableModifierHolder(Context context, VariableModifier modifier) {
		return new VariableModifierHolder(context, modifier);
	}

	@Override
	public InitializerVariableDeclarationElement createInitializerVariableDeclarationElement(Context context,
			String varName, Context varNameContext, int numDims, Expression initializer) {

		return new InitializerVariableDeclarationElement(
		        varNameContext,
		        new VarNameDeclaration(varNameContext, varName),
		        numDims,
		        initializer);
	}

	@Override
	public VariableDeclarationStatement createVariableDeclarationStatement(Context context,
			List<Annotation> annotations, List<VariableModifierHolder> modifiers,
			TypeReference typeReference,
			List<InitializerVariableDeclarationElement> elements) {
		return new VariableDeclarationStatement(context, new VariableModifiers(annotations, modifiers), typeReference, elements);
	}

	@Override
	public ExpressionStatement createExpressionStatement(Context context, Expression expression) {
		return new ExpressionStatement(context, expression);
	}

	@Override
    public AssignmentStatement createAssignmentStatement(Context context, AssignmentExpression assignmentExpression) {
        return new AssignmentStatement(context, assignmentExpression);
    }

    @Override
	public ForInit createForInit(Context context, VariableDeclarationStatement initializer) {
		return new ForInit(context, initializer);
	}

	@Override
	public ForInit createForInit(Context context, List<Expression> expressionList) {
		return new ForInit(context, new ForUpdateExpressionList(context, expressionList));
	}

	@Override
	public ForStatement createForStatement(Context context, Keyword keyword, ForInit forInit, Expression condition,
			List<Expression> forUpdateExpressionList, List<Statement> statements) {

		return new ForStatement(
		        context,
		        keyword,
		        forInit,
		        condition,
		        forUpdateExpressionList != null
		            ? new ForUpdateExpressionList(context, forUpdateExpressionList)
                    : null,
		        new Block(context, statements));
	}

	@Override
	public IteratorForStatement createIteratorForStatement(
	        Context context,
	        List<Annotation> annotations, List<VariableModifierHolder> modifiers,
			TypeReference type, String varName, Context varNameContext, int numDims, Expression expression,
			List<Statement> statements) {

		return new IteratorForStatement(
				varNameContext,
				new ModifiersVariableDeclarationElement(
						context,
						new VariableModifiers(annotations, modifiers),
						type,
						new VarNameDeclaration(varNameContext, varName),
						numDims),
				expression,
				new Block(context, statements));
	}

	@Override
	public WhileStatement createWhileStatement(Context context, Expression condition, List<Statement> statements) {
		return new WhileStatement(context, condition, new Block(context, statements));
	}

	@Override
	public DoWhileStatement createDoWhileStatement(Context context, Expression expression, List<Statement> statements) {
		return new DoWhileStatement(context, expression, new Block(context, statements));
	}

	@Override
	public Resource createResource(Context context, List<Annotation> annotations, List<VariableModifierHolder> modifiers, TypeReference type,
			String varName, Context varNameContext, int numDims, Expression initializer) {

		return new Resource(
		        context,
				new VariableModifiers(annotations, modifiers),
                type,
				new VarNameDeclaration(varNameContext, varName),
				numDims,
				initializer);
	}

	@Override
	public CatchBlock createCatchBlock(Context context, List<TypeReference> exceptionTypes, String exceptionVarName, List<Statement> statements) {
		return new CatchBlock(context, exceptionTypes, new VarName(exceptionVarName), new Block(context, statements));
	}

	@Override
	public TryCatchFinallyStatement createTryCatchFinallyStatement(Context context, List<Statement> tryBlock,
			List<CatchBlock> catchBlocks, List<Statement> finallyBlock) {
		return new TryCatchFinallyStatement(
		        context,
		        new Block(context, tryBlock),
		        catchBlocks,
		        finallyBlock != null
		            ? new Block(context, finallyBlock)
                    : null);
	}

	@Override
	public TryWithResourcesStatement createTryWithResourcesStatement(Context context, List<Resource> resources,
			List<Statement> tryBlock, List<CatchBlock> catchBlocks, List<Statement> finallyBlock) {
		return new TryWithResourcesStatement(
		        context,
		        resources,
		        new Block(context, tryBlock),
		        catchBlocks,
		        finallyBlock != null
		            ? new Block(context, finallyBlock)
                    : null);
	}

	@Override
	public ReturnStatement createReturnStatement(Context context, Expression expression) {
		return new ReturnStatement(context, expression);
	}

	@Override
	public ThrowStatement createThrowStatement(Context context, Expression expression) {
		return new ThrowStatement(context, expression);
	}

	@Override
	public IfElseIfElseStatement createIfElseIfElseStatement(Context context, List<ConditionBlock> conditions,
			Keyword elseKeyword, Block elseBlock) {
		return new IfElseIfElseStatement(context, conditions, elseKeyword, elseBlock);
	}

    @Override
    public IfConditionBlock createIfConditionBlock(Context context, Keyword ifKeyword,
            Expression condition, Block block) {
        return new IfConditionBlock(context, ifKeyword, condition, block);
    }

    @Override
    public ElseIfConditionBlock createElseIfConditionBlock(Context context, Keyword elseIfKeyword,
            Expression condition, Block block) {
        return new ElseIfConditionBlock(context, elseIfKeyword, condition, block);
    }

    @Override
	public ConstantSwitchCaseLabel createConstantSwitchCaseLabel(Context context, Keyword keyword, Expression constant) {
		return new ConstantSwitchCaseLabel(context, keyword, constant);
	}

	@Override
	public EnumConstant createEnumConstant(Context context, String enumConstant) {
		return new EnumConstant(context, enumConstant);
	}

	@Override
	public EnumSwitchCaseLabel createEnumSwitchCaseLabel(Context context, Keyword keyword, EnumConstant enumConstant) {
		return new EnumSwitchCaseLabel(context, keyword, enumConstant);
	}

	@Override
	public DefaultSwitchCaseLabel createDefaultSwitchCaseLabel(Context context, Keyword keyword) {
		return new DefaultSwitchCaseLabel(context, keyword);
	}

	@Override
	public SwitchCaseGroup createSwitchCaseGroup(Context context, List<SwitchCaseLabel> labels, Block block) {
		return new SwitchCaseGroup(context, labels, block);
	}

	@Override
	public SwitchCaseStatement createSwitchCaseStatement(Context context, Keyword keyword, Expression expression,
			List<SwitchCaseGroup> groups) {
		return new SwitchCaseStatement(context, keyword, expression, groups);
	}

	@Override
	public BreakStatement createBreakStatement(Context context, Keyword keyword, String label) {
		return new BreakStatement(context, keyword, label);
	}

	@Override
	public VariableReference makeVariableReference(Context context, List<Primary> primaries) {

		final VariableReference variableReference;

		if (primaries.isEmpty()) {
			throw new IllegalStateException("No primaries");
		}
		else if (primaries.size() == 1) {
			final Primary primary = primaries.get(0);

			if (primary instanceof VariableReference) {
				variableReference = (VariableReference)primary;
			}
			else if (primary instanceof ArrayAccessExpression) {
				variableReference = new ArrayAccessReference(context, (ArrayAccessExpression)primary);
			}
			else {
				throw new UnsupportedOperationException("Unknown primary type " + primary.getClass().getSimpleName());
			}
		}
		else {
			variableReference = new PrimaryListVariableReference(context, new PrimaryList(context, primaries));
		}

		return variableReference;
	}
}
