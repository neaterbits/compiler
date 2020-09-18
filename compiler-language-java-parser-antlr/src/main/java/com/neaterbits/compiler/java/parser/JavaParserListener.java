package com.neaterbits.compiler.java.parser;

import java.util.List;

import org.antlr.v4.runtime.Token;

import com.neaterbits.compiler.antlr4.Antlr4;
import com.neaterbits.compiler.antlr4.ModelParserListener;
import com.neaterbits.compiler.parser.listener.common.IterativeParseTreeListener;
import com.neaterbits.compiler.types.ReferenceType;
import com.neaterbits.compiler.types.block.ConstructorInvocation;
import com.neaterbits.compiler.types.method.MethodInvocationType;
import com.neaterbits.compiler.types.operator.IncrementDecrement;
import com.neaterbits.compiler.types.operator.Operator;
import com.neaterbits.compiler.types.statement.ASTMutability;
import com.neaterbits.compiler.types.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.types.typedefinition.ClassMethodVisibility;
import com.neaterbits.compiler.types.typedefinition.ClassVisibility;

import com.neaterbits.compiler.types.typedefinition.InterfaceMethodVisibility;
import com.neaterbits.compiler.types.typedefinition.InterfaceVisibility;import com.neaterbits.compiler.types.typedefinition.ConstructorVisibility;
import com.neaterbits.compiler.types.typedefinition.FieldVisibility;
import com.neaterbits.compiler.types.typedefinition.Subclassing;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ContextNamePart;
import com.neaterbits.compiler.util.FullContext;
import com.neaterbits.compiler.util.ImmutableFullContext;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.Strings;
import com.neaterbits.compiler.util.name.Names;
import com.neaterbits.compiler.util.parse.FieldAccessType;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.io.strings.OffsetLengthStringRef;
import com.neaterbits.util.io.strings.StringSource;

import static com.neaterbits.compiler.antlr4.AntlrStringRefs.stringRef;

/**
 * Listener for the Java grammars
 */

public class JavaParserListener<COMPILATION_UNIT> implements ModelParserListener<COMPILATION_UNIT> {

	// Delegate to make sure make all special handling here

	private final StringSource stringSource;
	private final ParseLogger logger;
	private final String file;
	
	// Cache packagename
	private String packageName;

	private COMPILATION_UNIT compilationUnit;

	// Workaround for Java grammar oddities where 'else if' is specified like
	// ifThenElseStatement [ ifThenElseStatement ]
	// so we need to detect whether an if directly follows an else and merge those into
	// the if-then-else structures made by this framework
	// before we pass them to the delegate

	// We use delegation instead of subclassing to make sure that we handle all methods in this class

	private final IterativeParseTreeListener<COMPILATION_UNIT> delegate;
	private final StatementsStack statementsStack;

	private void printStack(String statement) {
		logger.println("stack at " + statement + " " + statementsStack);
	}
	
	private int writeStartContext(Context context) {
	    
	    return delegate.writeContext(context);
	}

    private int writeLeafContext(Context context) {

        return delegate.writeContext(context);
    }

    private int writeOtherContext(Context context) {

        return delegate.writeContext(context);
    }

    private int writeEndContext(Context context) {

        return delegate.writeContext(context);
    }

	public JavaParserListener(
	        StringSource stringSource,
	        ParseLogger logger,
	        String file,
	        IterativeParseTreeListener<COMPILATION_UNIT> delegate) {
		this.stringSource = stringSource;
		this.logger = logger;
		this.file = file;
		this.delegate = delegate;
		this.statementsStack = new StatementsStack();
	}

	@Override
	public COMPILATION_UNIT getResult() {
		return compilationUnit;
	}

	public void onPackageDeclaration(Context context, long packageKeyword, Context packageKeywordContext, long name, FullContext nameContext) {
		this.packageName = stringSource.asString(name);
		
		delegate.onNamespaceStart(writeStartContext(context), packageKeyword, writeOtherContext(packageKeywordContext));
		
		final String [] parts = Strings.split(packageName, '.');
		
		int indexInName = 0;
		
		for (String part : parts) {
		    
		    final int partLength = part.length();
		    
		    final FullContext partContext = new ImmutableFullContext(
		            nameContext.getFile(),
		            nameContext.getStartLine(),
		            indexInName + nameContext.getStartPosInLine(),
		            indexInName + nameContext.getStartOffset(),
		            nameContext.getEndLine(),
		            indexInName + partLength + nameContext.getStartPosInLine(),
		            indexInName + partLength + nameContext.getStartOffset(),
		            part);
		    
		    final long partRef = OffsetLengthStringRef.encode(indexInName, partLength);
		    
		    delegate.onNamespacePart(writeLeafContext(partContext), partRef);
		    
		    indexInName += partLength;
		}
	}

	public void onCompilationUnitStart(Context context) {
		delegate.onCompilationUnitStart(writeStartContext(context));
	}
	
	public final void onImportStart(Context context, long importKeyword, Context importKeywordContext, long staticKeyword, Context staticKeywordContext) {
		delegate.onImportStart(
		        writeStartContext(context),
		        importKeyword,
		        writeOtherContext(importKeywordContext),
		        staticKeyword,
		        writeOtherContext(staticKeywordContext));
	}

	public final void onImportIdentifier(Context context, long identifier) {
		delegate.onImportName(writeOtherContext(context), identifier);
	}
	
	public final void onImportEnd(Context context, boolean ondemand) {
		delegate.onImportEnd(writeEndContext(context), context, ondemand);
	}

	public void onClassStart(Context context, long classKeyword, Context classKeywordContext, long name, Context nameContext) {

	    delegate.onTypeDefinitionStart(writeStartContext(context));

	    delegate.onClassStart(
		        writeStartContext(context),
		        classKeyword,
		        writeOtherContext(classKeywordContext),
		        name,
		        writeOtherContext(nameContext));
	}
	
	public void onVisibilityClassModifier(Context context, ClassVisibility visibility) {
		delegate.onVisibilityClassModifier(writeLeafContext(context), visibility);
	}
	
	public void onSubclassingModifier(Context context, Subclassing subclassing) {
		delegate.onSubclassingModifier(writeLeafContext(context), subclassing);
	}
	
	public void onStaticClassModifier(Context context) {
		delegate.onStaticClassModifier(writeLeafContext(context));
	}
	
	public void onStrictfpClassModifier(Context context) {
		delegate.onStrictfpClassModifier(writeLeafContext(context));
	}
	
	public void onClassExtends(Context context, long extendsKeyword, Context extendsKeywordContext) {
		delegate.onClassExtendsStart(writeStartContext(context), extendsKeyword, writeOtherContext(extendsKeywordContext));
	}
	
	public void onClassImplementsStart(Context context, long implementsKeyword, Context implementsKeywordContext) {
		delegate.onImplementsStart(writeStartContext(context), implementsKeyword, writeOtherContext(implementsKeywordContext));
	}

    public void onClassImplementsNamePart(Context context, long identifier) {
        delegate.onImplementsNamePart(writeOtherContext(context), identifier);
    }
	
	public void onClassEnd(Context context) {
		delegate.onClassEnd(writeEndContext(context), context);

		delegate.onTypeDefinitionEnd(writeEndContext(context), context);
	}
	
	public void onAnonymousClassStart(Context context) {
		delegate.onAnonymousClassStart(writeStartContext(context));
	}
	
	public void onAnonymousClassEnd(Context context) {
		delegate.onAnonymousClassEnd(writeEndContext(context), context);
	}

	public void onStaticInitializerStart(Context context) {
		
		statementsStack.push();
		
		delegate.onStaticInitializerStart(writeStartContext(context));
	}
	
	public void onStaticInitializerEnd(Context context) {
		delegate.onStaticInitializerEnd(writeEndContext(context), context);
		
		statementsStack.pop();
	}
	
	public void onConstructorStart(Context context) {
		
		statementsStack.push();
		
		delegate.onConstructorStart(writeStartContext(context));
	}

	public void onConstructorVisibilityModifier(Context context, ConstructorVisibility visibility) {
		delegate.onConstructorVisibilityModifier(writeLeafContext(context), visibility);
	}
	
	public void onConstructorName(Context context, long constructorName) {
		delegate.onConstructorName(writeLeafContext(context), constructorName);
	}
	
	public void onConstructorInvocationStart(Context context, ConstructorInvocation type) {
		delegate.onConstructorInvocationStart(writeStartContext(context), type);
	}
	
	public void onConstructorInvocationEnd(Context context) {
		delegate.onConstructorInvocationEnd(writeEndContext(context), context);
	}

	public void onConstructorEnd(Context context) {
		delegate.onConstructorEnd(writeEndContext(context), context);
		
		statementsStack.pop();
	}
	
	public void onClassMethodStart(Context context) {
		
		statementsStack.push();
		
		delegate.onClassMethodStart(writeStartContext(context));
	}
	
	public void onMethodReturnTypeStart(Context context) {
		delegate.onMethodReturnTypeStart(writeStartContext(context));
	}
	
	public void onMethodReturnTypeEnd(Context context) {
		delegate.onMethodReturnTypeEnd(writeEndContext(context), context);
	}

	public void onMethodName(Context context, long methodName) {
		delegate.onMethodName(writeLeafContext(context), methodName);
	}

	public void onMethodSignatureParametersStart(Context context) {
		delegate.onMethodSignatureParametersStart(writeStartContext(context));
	}

	public void onMethodSignatureParameterStart(Context context, boolean varArgs) {
		delegate.onMethodSignatureParameterStart(writeStartContext(context), varArgs);
	}

	public void onMethodSignatureParameterEnd(Context context) {
		delegate.onMethodSignatureParameterEnd(writeEndContext(context), context);
	}

	public void onMethodSignatureParametersEnd(Context context) {
		delegate.onMethodSignatureParametersEnd(writeEndContext(context), context);
	}

	public void onVisibilityClassMethodModifier(Context context, ClassMethodVisibility visibility) {
		delegate.onVisibilityClassMethodModifier(writeLeafContext(context), visibility);
	}
	
	public void onOverrideClassMehodModifier(Context context, ClassMethodOverride methodOverride) {
		delegate.onOverrideClassMethodModifier(writeLeafContext(context), methodOverride);
	}
	
	public void onStaticClassMethodModifier(Context context) {
		delegate.onStaticClassMethodModifier(writeLeafContext(context));
	}

	public void onStrictfpClassMethodModifier(Context context) {
		delegate.onStrictfpClassMethodModifier(writeLeafContext(context));
	}
	
	public void onSynchronizedClassMethodModifier(Context context) {
		delegate.onSynchronizedClassMethodModifier(writeLeafContext(context));
	}

	public void onNativeClassMethodModifier(Context context) {
		delegate.onNativeClassMethodModifier(writeLeafContext(context));
	}
	
	public void onClassMethodEnd(Context context) {
		delegate.onClassMethodEnd(writeEndContext(context), context);
		
		statementsStack.pop();
	}

	public void onInterfaceStart(Context context, long interfaceKeyword, Context interfaceKeywordContext, long name, Context nameContext) {
		delegate.onInterfaceStart(
		        writeStartContext(context),
		        interfaceKeyword,
		        writeOtherContext(interfaceKeywordContext),
		        name,
		        writeOtherContext(nameContext));
	}
	
	public void onVisibilityInterfaceModifier(Context context, InterfaceVisibility visibility) {
		delegate.onVisibilityInterfaceModifier(writeLeafContext(context), visibility);
	}

	public void onAbstractInterfaceModifier(Context context) {
		delegate.onStaticInterfaceModifier(writeLeafContext(context));
	}
	
	public void onStaticInterfaceModifier(Context context) {
		delegate.onStaticInterfaceModifier(writeLeafContext(context));
	}
	
	public void onStrictfpInterfaceModifier(Context context) {
		delegate.onStrictfpInterfaceModifier(writeLeafContext(context));
	}
	
	public void onInterfaceExtends(Context context, ScopedName interfaceName) {
		delegate.onInterfaceExtends(writeOtherContext(context), interfaceName);
	}
	
	public void onInterfaceEnd(Context context) {
		delegate.onInterfaceEnd(writeEndContext(context), context);
	}

	public void onInterfaceMethodStart(Context context) {
		
		statementsStack.push();
		
		delegate.onInterfaceMethodStart(writeStartContext(context));
	}

	public void onVisibilityInterfaceMethodModifier(Context context, InterfaceMethodVisibility visibility) {
		delegate.onVisibilityInterfaceMethodModifier(writeLeafContext(context), visibility);
	}
	
	public void onAbstractInterfaceMethodModifier(Context context) {
		delegate.onAbstractInterfaceMethodModifier(writeLeafContext(context));
	}
	
	public void onDefaultInterfaceMethodModifier(Context context) {
		delegate.onDefaultInterfaceMethodModifier(writeLeafContext(context));
	}
	
	public void onStaticInterfaceMethodModifier(Context context) {
		delegate.onStaticInterfaceMethodModifier(writeLeafContext(context));
	}

	public void onStrictfpInterfaceMethodModifier(Context context) {
		delegate.onStrictfpInterfaceMethodModifier(writeLeafContext(context));
	}
	
	public void onInterfaceMethodEnd(Context context) {
		delegate.onInterfaceMethodEnd(writeEndContext(context), context);
		
		statementsStack.pop();
	}
	
	public void onEnumStart(Context context, long enumKeyword, Context enumKeywordContext, long name, Context nameContext) {
		statementsStack.push();
		
		delegate.onEnumStart(
		        writeStartContext(context),
		        enumKeyword,
		        writeOtherContext(enumKeywordContext),
		        name,
		        writeOtherContext(nameContext));
	}

    public void onEnumImplementsStart(Context context, long implementsKeyword, Context implementsKeywordContext) {
        delegate.onImplementsStart(writeStartContext(context), implementsKeyword, writeOtherContext(implementsKeywordContext));
    }

    public void onEnumImplementsNamePart(Context context, long identifier) {
        delegate.onImplementsNamePart(writeOtherContext(context), identifier);
    }
	
	public void onEnumConstantStart(Context context, long name) {
		delegate.onEnumConstantStart(writeStartContext(context), name);
	}
	
	public void onEnumConstantEnd(Context context) {
		delegate.onEnumConstantEnd(writeEndContext(context), context);
	}
	
	public void onEnumEnd(Context context) {
		
		delegate.onEnumEnd(writeEndContext(context), context);

		statementsStack.pop();
	}
	
	public final void onFieldDeclarationStart(Context context) {
		delegate.onFieldDeclarationStart(writeStartContext(context));
	}

	public void onVisibilityFieldModifier(Context context, FieldVisibility visibility) {
		delegate.onVisibilityFieldModifier(writeLeafContext(context), visibility);
	}
	
	public void onStaticFieldModifier(Context context) {
		delegate.onStaticFieldModifier(writeLeafContext(context));
	}
	
	public void onMutabilityFieldModifier(Context context, ASTMutability mutability) {
		delegate.onMutabilityFieldModifier(writeLeafContext(context), mutability);
	}
	
	public void onTransientFieldModifier(Context context) {
		delegate.onTransientFieldModifier(writeLeafContext(context));
	}
	
	public void onVolatileFieldModifier(Context context) {
		delegate.onVolatileFieldModifier(writeLeafContext(context));
	}

	public final void onFieldDeclarationEnd(Context context) {
		delegate.onFieldDeclarationEnd(writeEndContext(context), context);
	}

	
	public void onNestedExpressionStart(Context context) {
		delegate.onNestedExpressionStart(writeStartContext(context));
	}
	
	public void onNestedExpressionEnd(Context context) {
		delegate.onNestedExpressionEnd(writeEndContext(context), context);
	}

	public void onEnterAssignmentExpression(Context context) {
		delegate.onEnterAssignmentExpression(writeStartContext(context));
	}
	
	public void onNameReference(Context context, long name) {
		delegate.onNameReference(writeLeafContext(context), name);
	}

	public void onVariableReference(Context context, long name) {
		delegate.onVariableReference(writeLeafContext(context), name);
	}

	public void onEnterAssignmentLHS(Context context) {
		delegate.onEnterAssignmentLHS(writeStartContext(context));
	}
	
	public void onExitAssignmentLHS(Context context) {
		delegate.onExitAssignmentLHS(writeEndContext(context), context);
	}
	
	public void onExitAssignmentExpression(Context context) {
		delegate.onExitAssignmentExpression(writeEndContext(context), context);
	}

	public void onExpressionBinaryOperator(Context context, Operator operator) {
		delegate.onExpressionBinaryOperator(writeLeafContext(context), operator);
	}

	public void onIncrementDecrementExpressionStart(Context context, IncrementDecrement operator) {
		delegate.onIncrementDecrementExpressionStart(writeStartContext(context), operator);
	}

	public void onIncrementDecrementExpressionEnd(Context context) {
		delegate.onIncrementDecrementExpressionEnd(writeEndContext(context), context);
	}
	
	// Primary
	public void onPrimaryStart(Context context) {
		delegate.onPrimaryStart(writeStartContext(context));
	}

	public void onArrayAccessStart(Context context) {
		delegate.onArrayAccessStart(writeStartContext(context));
	}
	
	public void onArrayIndexStart(Context context) {
		delegate.onArrayIndexStart(writeStartContext(context));
	}
	
	public void onArrayIndexEnd(Context context) {
		delegate.onArrayIndexEnd(writeEndContext(context), context);
	}
	
	public void onArrayAccessEnd(Context context) {
		delegate.onArrayAccessEnd(writeEndContext(context), context);
	}

	public void onFieldAccess(Context context, FieldAccessType fieldAccessType, ScopedName typeName, ReferenceType referenceType, long fieldName, Context fieldNameContext) {
		delegate.onFieldAccess(
		        writeOtherContext(context),
		        fieldAccessType,
		        typeName,
		        referenceType,
		        fieldName,
		        writeOtherContext(fieldNameContext));
	}

	public void onCastExpressionStart(Context context) {
		delegate.onCastExpressionStart(writeStartContext(context));
	}
	
	public void onCastExpressionEnd(Context context) {
		delegate.onCastExpressionEnd(writeEndContext(context), context);
	}

	public void onThisPrimary(Context context) {
		delegate.onThisPrimary(writeLeafContext(context));
	}

	public void onPrimaryEnd(Context context) {
		delegate.onPrimaryEnd(writeEndContext(context), context);
	}

	public void onConditionalExpressionStart(Context context) {
		delegate.onConditionalExpressionStart(writeStartContext(context));
	}
	
	public void onConditionalExpressionPart1Start(Context context) {
		delegate.onConditionalExpressionPart1Start(writeStartContext(context));
	}
	
	public void onConditionalExpressionPart1End(Context context) {
		delegate.onConditionalExpressionPart1End(writeEndContext(context), context);
	}

	public void onConditionalExpressionPart2Start(Context context) {
		delegate.onConditionalExpressionPart2Start(writeStartContext(context));
	}
	
	public void onConditionalExpressionPart2End(Context context) {
		delegate.onConditionalExpressionPart2End(writeEndContext(context), context);
	}

	public void onConditionalExpressionPart3Start(Context context) {
		delegate.onConditionalExpressionPart3Start(writeStartContext(context));
	}
	
	public void onConditionalExpressionPart3End(Context context) {
		delegate.onConditionalExpressionPart3End(writeEndContext(context), context);
	}
	
	public void onConditionalExpressionEnd(Context context) {
		delegate.onConditionalExpressionEnd(writeEndContext(context), context);
	}
	
	// Literals
	public void onJavaIntegerLiteral(Context context, String literal) {

		final JavaInteger javaInteger = JavaParserUtil.parseIntegerLiteral(literal);
		
		final int bits = javaInteger.getBits();
		
		delegate.onIntegerLiteral(
				writeLeafContext(context),
				javaInteger.getValue(),
				javaInteger.getBase(),
				true,
				bits);
	}

	public void onJavaFloatingPointLiteral(Context context, String literal) {
		throw new UnsupportedOperationException();
	}

	public void onJavaBooleanLiteral(Context context, String literal) {

		final boolean value;

		switch (literal) {
		case "true":
			value = true;
			break;

		case "false":
			value = false;
			break;

		default:
			throw new IllegalStateException("Not a boolean literal: " + literal);
		}

		delegate.onBooleanLiteral(writeLeafContext(context), value);
	}
	
	public void onJavaCharacterLiteral(Context context, String literal) {
		
		if (literal.length() < 3) {
			throw new IllegalStateException("Not a Character literal");
		}

		final String s = literal.substring(1, literal.length() - 1);
		
		delegate.onCharacterLiteral(writeLeafContext(context), s.charAt(0));
	}
	
	public void onJavaStringLiteral(Context context, long literal) {
		
		final int length = OffsetLengthStringRef.decodeLength(literal);
		
		if (length < 2) {
			throw new IllegalStateException("Not a String literal");
		}

		delegate.onStringLiteral(writeLeafContext(context), OffsetLengthStringRef.substring(literal, 1, length - 1));
	}
	
	public void onJavaNullLiteral(Context context, String literal) {
		
		if (!literal.equals("null")) {
			throw new IllegalStateException("not value null");
		}
		
		delegate.onNullLiteral(writeLeafContext(context));
	}
	
	public void onJavaClassInstanceCreationExpressionStart(Context context) {
		delegate.onClassInstanceCreationExpressionStart(writeStartContext(context));
	}

	public void onJavaClassInstanceCreationConstructorName(Context context, long name) {

		delegate.onClassInstanceCreationTypeAndConstructorName(writeLeafContext(context), name);
	}

	private Names makeNames(List<ContextNamePart> parts) {
	    
	    final int [] contexts = new int[parts.size()];
	    
	    for (int i = 0; i < parts.size(); ++ i) {
	        contexts[i] = writeOtherContext(parts.get(i).getContext());
	    }
	    
	    return new Names() {
            
            @Override
            public long getStringAt(int index) {
                return parts.get(index).getName();
            }
            
            @Override
            public int getContextAt(int index) {
                return contexts[index];
            }
            
            @Override
            public int count() {
                return parts.size();
            }
        };
	}
	
	public void onJavaClassInstanceCreationConstructorName(Context context, List<ContextNamePart> parts) {

        delegate.onClassInstanceCreationTypeAndConstructorName(writeLeafContext(context), makeNames(parts));
    }

	public void onJavaClassInstanceCreationExpressionEnd(Context context) {
		delegate.onClassInstanceCreationExpressionEnd(writeEndContext(context), context);
	}
	
	public void onMethodInvocationStart(
			Context context,
			MethodInvocationType type,
			Names names,
			ReferenceType referenceType,
			long methodName,
			Context methodNameContext) {

		delegate.onMethodInvocationStart(
		        writeStartContext(context),
		        type,
		        methodName,
		        writeOtherContext(methodNameContext));
	}
	
	public void onMethodInvocationEnd(Context context) {
		delegate.onMethodInvocationEnd(writeEndContext(context), context);
	}
	
	public void onParametersStart(Context context) {
		delegate.onParametersStart(writeStartContext(context));
	}
	
	public void onParameterStart(Context context) {
		delegate.onParameterStart(writeStartContext(context));
	}
	
	public void onParameterEnd(Context context) {
		delegate.onParameterEnd(writeEndContext(context), context);
	}
	
	public void onParametersEnd(Context context) {
		delegate.onParametersEnd(writeEndContext(context), context);
	}
	
	public void onArrayCreationExpressionStart(Context context, ScopedName typeName, ReferenceType referenceType, int numDims) {
		delegate.onArrayCreationExpressionStart(
		        writeStartContext(context),
		        typeName,
		        referenceType,
		        numDims);
	}
	
	public void onDimExpressionStart(Context context) {
		delegate.onDimExpressionStart(writeStartContext(context));
	}
	
	public void onDimExpressionEnd(Context context) {
		delegate.onDimExpressionEnd(writeEndContext(context), context);
	}

	public void onArrayCreationExpressionEnd(Context context) {
		delegate.onArrayCreationExpressionEnd(writeEndContext(context), context);
	}
	
	public void onClassExpression(Context context, long className, int numArrayDims) {
		delegate.onClassExpression(writeLeafContext(context), className, numArrayDims);
	}
	
	
	public void onLambdaExpressionStart(Context context) {
		delegate.onLambdaExpressionStart(writeStartContext(context));
	}
	
	public void onSingleLambdaParameter(Context context, long varName, Context varNameContext) {
		delegate.onSingleLambdaParameter(writeLeafContext(context), varName);
	}
	
	public void onFormalLambdaParameterListStart(Context context) {
		delegate.onFormalLambdaParameterListStart(writeStartContext(context));
	}

	public void onFormalLambdaParameterListEnd(Context context) {
		delegate.onFormalLambdaParameterListEnd(writeEndContext(context), context);
	}
	
    public void onInferredLambdaParameterList(Context context, List<String> varNames, Context varNamesContext) {
		delegate.onInferredLambdaParameterList(writeOtherContext(context), varNames, writeOtherContext(varNamesContext));
	}
	
	public void onLambdaBodyStart(Context context) {
		delegate.onLambdaBodyStart(writeStartContext(context));
	}
	
	public void onLambdaBodyEnd(Context context) {
		delegate.onLambdaBodyEnd(writeEndContext(context), context);
	}
	
	public void onLambdaExpressionEnd(Context context) {
		delegate.onLambdaExpressionEnd(writeEndContext(context), context);
	}
	
	public COMPILATION_UNIT onCompilationUnitEnd(Context context) {
		
		// Trigger namespace end here since namespace contains code
		// to suppert eg. C# namespace { }, namespace { }
		delegate.onNameSpaceEnd(writeEndContext(context), context);
		
		final COMPILATION_UNIT compilationUnit = (COMPILATION_UNIT)delegate.onCompilationUnitEnd(writeEndContext(context), context);
		
		this.compilationUnit = compilationUnit;
		
		return compilationUnit;
	}

	public void onMutabilityVariableModifier(Context context, ASTMutability mutability) {
		delegate.onMutabilityVariableModifier(writeLeafContext(context), mutability);
	}
	
	public void onVariableDeclarationStatementStart(Context context) {
		delegate.onVariableDeclarationStatementStart(writeStartContext(context));
	}
	
	public void onVariableDeclarationStatementEnd(Context context) {
		delegate.onVariableDeclarationStatementEnd(writeEndContext(context), context);
	}
	
	public void onVariableDeclaratorStart(Context context) {
		delegate.onVariableDeclaratorStart(writeStartContext(context));
	}
	
	public void onVariableDeclaratorEnd(Context context) {
		delegate.onVariableDeclaratorEnd(writeEndContext(context), context);
	}

	public void onVariableName(Context context, long name, int numDims) {
		delegate.onVariableName(writeLeafContext(context), name, numDims);
	}

	public void onJavaPrimitiveType(Context context, JavaPrimitiveType type) {
	    
        throw new UnsupportedOperationException("Not maintained for now");
	    
		// delegate.onTypeReference(context, type.getScopedName(), ReferenceType.SCALAR);
	}
	
	public void onJavaClassOrInterfaceReferenceType(Context context, ScopedName typeName) {

	    throw new UnsupportedOperationException("Not maintained for now");

		// delegate.onTypeReference(context, typeName, ReferenceType.REFERENCE);
	}
	
	public void onJavaTypeVariableReferenceType(Context context, ScopedName typeName, ReferenceType referenceType) {

	    throw new UnsupportedOperationException("Not maintained for now");

		// delegate.onTypeReference(context, typeName, referenceType);
	}
	
	private Context updateElseIfContext(FullContext context, Token elseToken, String file) {

		final FullContext elseContext = Antlr4.context(elseToken, file);
		
		final Context updatedContext;
		
		if (elseContext.getStartOffset() < context.getStartOffset()) {
			updatedContext = new ImmutableFullContext(
					context.getFile(),
					elseContext.getStartLine(),
					elseContext.getStartPosInLine(),
					elseContext.getStartOffset(),
					context.getEndLine(),
					context.getEndPosInLine(),
					context.getEndOffset(),
					context.getText());
		}
		else {
			updatedContext = context;
		}
		
		if (elseContext.getStartOffset() != updatedContext.getStartOffset()) {
			throw new IllegalStateException("elseContext " + elseContext.getStartOffset() + "/" + updatedContext.getStartOffset());
		}
		
		return updatedContext;
	}
	
	public void onJavaIfThenStatementStart(FullContext context, Token keywordToken) {
		
		if (logger != null) {
			printStack("javaIfThenStatementStart");
		}

		final JavaStatementHolder ifOrElseStatement = getIfOrElseStatement(0);

		if (ifOrElseStatement != null && ifOrElseStatement.getStatement() == JavaStatement.ELSE) {
			
			statementsStack.pop();

			final Token elseToken = ifOrElseStatement.getKeywordToken(0);
			final Context elseContext = Antlr4.context(elseToken, file);
			
			final Context keywordContext = Antlr4.context(keywordToken, file);

			delegate.onElseIfStatementStart(
					writeStartContext(updateElseIfContext(context, elseToken, file)),
					stringRef(elseToken),
					writeOtherContext(elseContext),
					stringRef(keywordToken),
					writeOtherContext(keywordContext));
		}
		else {
			delegate.onIfStatementStart(
			        writeStartContext(context),
			        stringRef(keywordToken),
			        writeOtherContext(Antlr4.context(keywordToken, file)));
		}

		statementsStack.add(JavaStatement.IF_THEN, keywordToken);
		statementsStack.push();

		if (logger != null) {
			printStack("javaIfThenStatementStart - exit");
		}
	}

	public void onJavaIfThenStatementEnd(Context context) {
		
		if (logger != null) {
			printStack("javaIfThenStatementEnd");
		}

		final JavaStatementHolder ifOrElseStatement = getIfOrElseStatement(0);
		
		if (ifOrElseStatement.getStatement() == JavaStatement.ELSE) {
			delegate.onElseIfStatementEnd(writeEndContext(context), context);
		}
		else {
			delegate.onIfStatementInitialBlockEnd(writeEndContext(context), context);
		}

		delegate.onEndIfStatement(writeEndContext(context), context);
		statementsStack.pop();

		if (logger != null) {
			printStack("javaIfThenStatementEnd after pop");
		}
	}

	public void onJavaIfThenElseStatementStart(
			FullContext context,
			Token ifKeyword,
			Token elseKeyword) {

		if (logger != null) {
			printStack("javaIfThenElseStatementStart");
		}

		final JavaStatementHolder ifOrElseStatement = getIfOrElseStatement(0);
		
		if (ifOrElseStatement != null && ifOrElseStatement.getStatement() == JavaStatement.ELSE) {
			// Previous was if-statement so this is else-statement
			statementsStack.pop();

			final Token elseToken = ifOrElseStatement.getKeywordToken(0);
			
			final Context elseContext = Antlr4.context(elseToken, file);
			final Context ifKeywordContext = Antlr4.context(ifKeyword, file);
			
			delegate.onElseIfStatementStart(
					writeStartContext(updateElseIfContext(context, elseToken, file)),
					stringRef(elseContext),
					writeOtherContext(elseContext),
					stringRef(ifKeywordContext),
					writeOtherContext(ifKeywordContext));
		}
		else {
			delegate.onIfStatementStart(
			        writeStartContext(context),
			        stringRef(ifKeyword),
			        writeOtherContext(Antlr4.context(ifKeyword, file)));
		}

		statementsStack.add(JavaStatement.IF_THEN_ELSE_START, ifKeyword, elseKeyword);
		statementsStack.push();
	}

	public void onJavaIfThenElseStatementEnd(Context context) {

		if (logger != null) {
			printStack("javaIfThenElseStatementEnd");
		}
	}
	
	final JavaStatementHolder getIfOrElseStatement(int i) {

		return 	   statementsStack.size() > 1
				&& statementsStack.getSizeOfFrame(1) > 0 + i

			? statementsStack.getLastFromFrame(1, i)
			: null;
	}

	private void onAnyNonIfStatementStart(Context context) {

		final JavaStatementHolder ifOrElseStatement = getIfOrElseStatement(0);

		if (ifOrElseStatement != null && statementsStack.getSizeOfFrame(0) == 0) {
			
			switch (ifOrElseStatement.getStatement()) {
			case IF_THEN_ELSE_START:
				// We are at block1 of if <block1> else <block2>
				break;

			case ELSE:
				// We are at block2 of if <block1> else <block2>
				delegate.onElseStatementStart(
						writeStartContext(context),
						stringRef(ifOrElseStatement.getKeywordToken(0)),
						writeOtherContext(Antlr4.context(ifOrElseStatement.getKeywordToken(0), file)));
				break;
				
			default:
				break;
			}
		}
	}
	
	private void onAnyNonIfStatementEnd(Context context) {

		final JavaStatementHolder ifOrElseStatement = getIfOrElseStatement(0);

		if (ifOrElseStatement != null && statementsStack.getSizeOfFrame(0) == 1) {

			switch (ifOrElseStatement.getStatement()) {
			case IF_THEN_ELSE_START:
				
				final JavaStatementHolder prevStatement = getIfOrElseStatement(1);
				
				if (prevStatement != null && prevStatement.getStatement() == JavaStatement.ELSE) {
					delegate.onElseIfStatementEnd(writeEndContext(context), context);
				}
				else {
					// Initial if-statement
					delegate.onIfStatementInitialBlockEnd(writeEndContext(context), context);
				}
				
				// We are at block1 of if <block1> else <block2>
				
				final Token elseToken = ifOrElseStatement.getKeywordToken(1);
				
				statementsStack.pop();
				statementsStack.add(JavaStatement.ELSE, elseToken);
				
				// else-block
				statementsStack.push();
				break;
				
			case ELSE:
				// We are at block2 of if <block1> else <block2>
				delegate.onElseStatementEnd(writeEndContext(context), context);
				
				delegate.onEndIfStatement(writeEndContext(context), context);
				
				statementsStack.pop();
				break;
				
			default:
				break;
			}
		}
	}
	
	
	public void onJavaBlockStart(Context context) {
		
		if (logger != null) {
			printStack("javaBlockStart");
		}
		
		onAnyNonIfStatementStart(context);
		
		if (statementsStack.size() > 1) {
			statementsStack.add(JavaStatement.BLOCK);
		}
		
		if (statementsStack.size() > 1 && statementsStack.getSizeOfFrame(1) > 0) {
			
			switch (statementsStack.getLastFromFrame(1).getStatement()) {
				
			case ENHANCED_FOR:
				delegate.onIteratorForTestEnd(writeEndContext(context), context);
				break;
				
			case TRY_WITH_RESOURCES:
				break;

			default:
				break;
			}
		}
		else {
			// Do nothing, blocks are handled separately in parser
		}

		if (statementsStack.size() > 1) {
			statementsStack.push();
		}

		if (logger != null) {
			printStack("onJavaBlockStart - exit");
		}
	}
	
	public void onJavaBlockEnd(Context context) {

		if (logger != null) {
			printStack("onJavaBlockEnd");
		}

		if (statementsStack.size() > 2) {
			statementsStack.pop();
		}
		
		onAnyNonIfStatementEnd(context);
		
		
		if (statementsStack.size() > 1 && statementsStack.getSizeOfFrame(1) > 0) {

			switch (statementsStack.getLastFromFrame(1).getStatement()) {
				
			case TRY_CATCH:
			case TRY_CATCH_FINALLY:
			case TRY_WITH_RESOURCES:
				delegate.onTryBlockEnd(writeEndContext(context), context);
				
				statementsStack.pop();
				break;
				
			default:
				break;
			}
		}
		else {
			// Do nothing, blocks are handled separately in parser
		}
		
		if (logger != null) {
			printStack("onJavaBlockEnd - exit");
		}
	}

	public void onJavaAssertStatementStart(Context context) {
		
		statementsStack.add(JavaStatement.ASSERT);
	}

	public void onJavaAssertStatementEnd(Context context) {
		
	}

	public void onJavaExpressionStatementStart(Context context) {
		
		statementsStack.add(JavaStatement.EXPRESSION);
		
		delegate.onExpressionStatementStart(writeStartContext(context));
	}

	public void onJavaExpressionStatementEnd(Context context) {
		delegate.onExpressionStatementEnd(writeEndContext(context), context);
	}

	public void onSwitchStatementStart(Context context, long keyword, Context keywordContext) {

		statementsStack.add(JavaStatement.SWITCH);

		delegate.onSwitchStatementStart(writeStartContext(context), keyword, writeOtherContext(keywordContext));
	}

	public void onJavaSwitchBlockStart(Context context) {
		delegate.onJavaSwitchBlockStart(writeStartContext(context));
	}
	
	public void onJavaSwitchBlockStatementGroupStart(Context context) {
		delegate.onJavaSwitchBlockStatementGroupStart(writeStartContext(context));
	}

	public void onSwitchLabelsStart(Context context) {
		delegate.onSwitchLabelsStart(writeStartContext(context));
	}
	
	public void onSwitchLabelsEnd(Context context) {
		delegate.onSwitchLabelsEnd(writeEndContext(context), context);
	}

	public void onJavaSwitchBlockStatementGroupEnd(Context context) {
		delegate.onJavaSwitchBlockStatementGroupEnd(writeEndContext(context), context);
	}

	public void onConstantSwitchLabelStart(Context context, long keyword, Context keywordContext) {
		delegate.onConstantSwitchLabelStart(
		        writeStartContext(context),
		        keyword,
		        writeOtherContext(keywordContext));
	}
	
	public void onConstantSwitchLabelEnd(Context context) {
		delegate.onConstantSwitchLabelEnd(writeEndContext(context), context);
	}
	
	public void onEnumSwitchLabel(Context context, long keyword, Context keywordContext, long constantName, Context constantNameContext) {
		delegate.onEnumSwitchLabel(
		        writeOtherContext(context),
		        keyword, writeOtherContext(keywordContext),
		        constantName,
		        writeOtherContext(constantNameContext));
	}
	
	public void onDefaultSwitchLabel(Context context, long keyword, Context keywordContext) {
		delegate.onDefaultSwitchLabel(writeLeafContext(context), keyword);
	}
	
	public void onJavaSwitchBlockEnd(Context context) {
		delegate.onJavaSwitchBlockEnd(writeEndContext(context), context);
	}
	
	public void onSwitchStatementEnd(Context context) {
		delegate.onSwitchStatementEnd(writeEndContext(context), context);
	}
	
	public void onBreakStatement(Context context, long keyword, Context keywordContext, long label, Context labelContext) {
		delegate.onBreakStatement(
		        writeStartContext(context),
		        keyword,
		        writeOtherContext(keywordContext),
		        label,
		        writeOtherContext(labelContext),
		        context);
	}
	
	public void onWhileStatementStart(Context context, long whileKeyword, Context whileKeywordContext) {
		delegate.onWhileStatementStart(writeStartContext(context), whileKeyword, writeOtherContext(whileKeywordContext));
	}
	
	public void onWhileStatementEnd(Context context) {
		delegate.onWhileStatementEnd(writeEndContext(context), context);
	}
	
	public void onForStatementStart(Context context, long keyword, Context keywordContext) {
		
		statementsStack.add(JavaStatement.FOR);
		
		delegate.onForStatementStart(
		        writeStartContext(context),
		        keyword,
		        writeOtherContext(keywordContext));
	}
	
	public void onForInitStart(Context context) {
		
		delegate.onForInitStart(writeStartContext(context));
	}
	
	public void onForInitEnd(Context context) {
		
		delegate.onForInitEnd(writeEndContext(context), context);
	}
	
	public void onForUpdateStart(Context context) {
		
		delegate.onForUpdateStart(writeStartContext(context));
	}
	
	public void onForUpdateEnd(Context context) {
		
		delegate.onForUpdateEnd(writeEndContext(context), context);
	}
	
	public void onForStatementEnd(Context context) {
		
		delegate.onForStatementEnd(writeEndContext(context), context);
	}
	
	public void onIteratorForStatementStart(Context context, long forKeyword, Context forKeywordContext) {
		
		statementsStack.add(JavaStatement.ENHANCED_FOR);
		
		statementsStack.push();
		
		delegate.onIteratorForStatementStart(writeStartContext(context), forKeyword, writeLeafContext(forKeywordContext));
	}
	
	public void onIteratorForStatementEnd(Context context) {
		
		statementsStack.pop();
		
		delegate.onIteratorForStatementEnd(writeEndContext(context), context);
	}

	public void onDoWhileStatementStart(Context context) {
		
		statementsStack.add(JavaStatement.DOWHILE);
		
		statementsStack.push();
		
		delegate.onDoWhileStatementStart(writeStartContext(context));
	}
	
	public void onDoWhileStatementEnd(Context context) {
		
		delegate.onDoWhileStatementEnd(writeEndContext(context), context);
		
		statementsStack.pop();
	}
	
	public void onContinueStatement(Context context, String label) {
		
	}
	
	public void onReturnStatementStart(Context context, long keyword, Context keywordContext) {
		
		statementsStack.add(JavaStatement.RETURN);
		
		delegate.onReturnStatementStart(writeStartContext(context), keyword, writeOtherContext(keywordContext));
	}
	
	public void onReturnStatementEnd(Context context) {
		delegate.onReturnStatementEnd(writeEndContext(context), context);
	}
	
	public void onJavaSynchronizedStatementStart(Context context) {
		
	}
	
	public void onJavaSynchronizedStatementEnd(Context context) {
		
	}
	
	public void onThrowStatementStart(Context context, long keyword, Context keywordContext) {
		delegate.onThrowStatementStart(writeStartContext(context), keyword, writeLeafContext(keywordContext));
	}
	
	public void onThrowStatementEnd(Context context) {
		delegate.onThrowStatementEnd(writeEndContext(context), context);
	}

	public void onAnnotationStart(Context context) {
		delegate.onAnnotationStart(writeStartContext(context), null);
	}

	public void onAnnotationEnd(Context context) {
		delegate.onAnnotationEnd(writeEndContext(context), context);
	}

	public void onTryStatementStart(Context context, long tryKeyword, Context tryKeywordContext) {
		
		statementsStack.add(JavaStatement.TRY_CATCH);
		
		statementsStack.push();
		
		delegate.onTryStatementStart(writeStartContext(context), tryKeyword, writeOtherContext(tryKeywordContext));
	}

	public void onTryStatementEnd(Context context) {
		
		delegate.onTryStatementEnd(writeEndContext(context), context);
	}
	
	public void onJavaTryWithResourcesStart(Context context, long tryKeyword, Context tryKeywordContext) {
		
		statementsStack.add(JavaStatement.TRY_WITH_RESOURCES);

		statementsStack.push();

		delegate.onTryWithResourcesStatementStart(writeStartContext(context), tryKeyword, writeOtherContext(tryKeywordContext));
	}
	
	public void onJavaTryWithResourcesSpecificationStart(Context context) {
		delegate.onTryWithResourcesSpecificationStart(writeStartContext(context));
	}
	
	public void onJavaResourceStart(Context context) {
		delegate.onResourceStart(writeStartContext(context));
	}
	
	public void onJavaResourceEnd(Context context) {
		delegate.onResourceEnd(writeEndContext(context), context);
	}

	public void onJavaTryWithResourcesSpecificationEnd(Context context) {
		delegate.onTryWithResourcesSpecificationEnd(writeEndContext(context), context);
	}
	
	public void onJavaTryWithResourcesEnd(Context context) {
		
		statementsStack.pop();
		
		delegate.onTryWithResourcesEnd(writeEndContext(context), context);
	}
	
	public void onCatchStart(Context context, long keyword, Context keywordContext) {
		
		statementsStack.add(JavaStatement.CATCH);
		
		statementsStack.push();
		
		delegate.onCatchStart(writeStartContext(context), keyword, writeOtherContext(keywordContext));
	}

	public void onCatchEnd(Context context) {
		
		statementsStack.pop();
		
		delegate.onCatchEnd(writeEndContext(context), context);
	}

	public void onFinallyStart(Context context, long keyword, Context keywordContext) {
		
		statementsStack.add(JavaStatement.FINALLY);
		
		statementsStack.push();

		delegate.onFinallyStart(writeStartContext(context), keyword, writeOtherContext(keywordContext));
	}

	public void onFinallyEnd(Context context) {

		statementsStack.pop();
		
		delegate.onFinallyEnd(writeEndContext(context), context);
	}
}
