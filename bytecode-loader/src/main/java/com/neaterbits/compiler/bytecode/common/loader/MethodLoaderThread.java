package com.neaterbits.compiler.bytecode.common.loader;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.types.ClassLibs;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.bytecode.common.BytecodeFormat;
import com.neaterbits.compiler.bytecode.common.ClassBytecode;
import com.neaterbits.compiler.bytecode.common.ClassFileException;
import com.neaterbits.compiler.bytecode.common.MethodClassReferenceScanner;
import com.neaterbits.compiler.bytecode.common.MethodType;
import com.neaterbits.compiler.codemap.CodeMap;
import com.neaterbits.compiler.codemap.VTableScratchArea;
import com.neaterbits.compiler.codemap.CodeMap.MethodRefStatus;
import com.neaterbits.compiler.types.MethodVariant;
import com.neaterbits.compiler.util.FieldType;

final class MethodLoaderThread<CLASS, METHOD> extends Thread {

	private static final int SCAN_DEPTH = 5;

	private final BytecodeLoader<CLASS, METHOD> bytecodeLoader;
	private final LoadMethodQueues<METHOD> loadQueues;
	private final BytecodeFormat bytecodeFormat;
	private final ClassLibs classLibs;
	private final BytecodeCompiler<CLASS, METHOD> bytecodeCompiler;

	private final LoaderMaps loaderMaps;
	private final ThreadsafeArray<LoadedMethod> scannedMethodsArray;

	private final MethodClassReferenceScanner<LoadedMethod> methodScanner;
	
	private final VTableScratchArea scratchArea;

	MethodLoaderThread(
			BytecodeLoader<CLASS, METHOD> bytecodeLoader,
			LoadMethodQueues<METHOD> loadQueues,
			BytecodeFormat bytecodeFormat,
			ClassLibs classLibs,
			BytecodeCompiler<CLASS, METHOD> bytecodeCompiler,
			LoaderMaps loaderMaps,
			ThreadsafeArray<LoadedMethod> scannedMethodsArray) {
		
		Objects.requireNonNull(bytecodeLoader);
		Objects.requireNonNull(loadQueues);
		Objects.requireNonNull(bytecodeFormat);
		Objects.requireNonNull(classLibs);
		Objects.requireNonNull(bytecodeCompiler);
		Objects.requireNonNull(loaderMaps);
		Objects.requireNonNull(scannedMethodsArray);
		
		this.bytecodeLoader = bytecodeLoader;
		this.loadQueues = loadQueues;
		this.bytecodeFormat = bytecodeFormat;
		this.classLibs = classLibs;
		this.bytecodeCompiler = bytecodeCompiler;
		this.loaderMaps = loaderMaps;
		this.scannedMethodsArray = scannedMethodsArray;

		this.scratchArea = new VTableScratchArea();
		
		this.methodScanner = new MethodClassReferenceScanner<LoadedMethod>() {
			
			@Override
			public void onMethodInvocation(LoadedMethod param, TypeName typeName, String methodName, FieldType returnType,
					List<FieldType> parameterTypes, MethodType methodType) {

				
				param.addInvokedMethod(new InvokedMethod(typeName, methodName, returnType, parameterTypes, methodType));
			}
			
			@Override
			public void onInstanceCreation(LoadedMethod param, TypeName typeName) {
				param.addInstantiatedType(typeName);
			}
		};
	}
	
	@Override
	public void run() {
		loadThreadMain();
	}

	private void loadThreadMain() {
		
		final CodeMap.TypeResult typeResult = new CodeMap.TypeResult();
		
		while (bytecodeLoader.isRunning()) {

			final LoadMethodRequest loadMethodRequest = loadQueues.getNextMethodRequest(bytecodeLoader::isRunning);
			
			if (loadMethodRequest != null) {
				
				if (loadMethodRequest instanceof NameLoadMethodRequest) {
					
					final NameLoadMethodRequest request = (NameLoadMethodRequest)loadMethodRequest;
					
					final TypeName typeName = request.getTypeName();
	
					ClassBytecode addedClass = null;
					try {
						addedClass = LoadClassHelper.loadClass(typeName, typeResult, bytecodeFormat, classLibs, loaderMaps);
					} catch (IOException | ClassFileException ex) {
						ex.printStackTrace();
					}
					
					final int type = typeResult.type;
					
					final ClassBytecode classBytecode;
					
					if (addedClass != null) {
						// This was a new type so add method to the type map
						// Threadsafe since typeAdded is true only for one thread
						classBytecode = addedClass;
						
						bytecodeCompiler.initializeClass(type, classBytecode, scratchArea);
					}
					else {
						classBytecode = loaderMaps.loadedClasses.getBytecode(type);
					}
					
					final int requestedMethodIdx = classBytecode.getMethodIndex(request.getMethodName(), request.getParameterTypes());
					
					if (requestedMethodIdx != -1) {
						processRequestedMethod(
								request,
								type,
								classBytecode,
								requestedMethodIdx);
					}
					
				}
				else if (loadMethodRequest instanceof ExecutionPathLoadMethodRequest) {
					
					final ExecutionPathLoadMethodRequest executionPathRequest = (ExecutionPathLoadMethodRequest)loadMethodRequest;
					
					final int methodNo = executionPathRequest.getMethodNo();
					
					final int type = loaderMaps.codeMap.getTypeForMethod(methodNo);
					final int methodIdx = loaderMaps.codeMap.getIndexForMethod(methodNo);

					final TriggeringLoadMethodRequestInfo triggeringRequestInfo = new TriggeringLoadMethodRequestInfo(
							executionPathRequest.getThreadNo(),
							executionPathRequest.getNotifyObject(),
							type,
							methodNo,
							methodIdx);
					
					final int numAddedToQueue = loadCalledMethods(triggeringRequestInfo, methodNo);
					
					if (numAddedToQueue == 0) {
						checkReturnLoadResponse(triggeringRequestInfo);
					}
				}
				else {
					throw new UnsupportedOperationException();
				}
			}
		}
	}

	
	private void processRequestedMethod(
			NameLoadMethodRequest request,
			int type,
			ClassBytecode classBytecode,
			int requestedMethodIdx) {
		
		final FieldType returnTypeName = classBytecode.getMethodReturnType(requestedMethodIdx);
		
		final int methodNo = loaderMaps.codeMap.addOrGetMethod(
				type,
				request.getMethodName(),
				classBytecode.getMethodVariant(requestedMethodIdx),
				loaderMaps.getReturnType(returnTypeName),
				loaderMaps.getParameters(request.getParameterTypes()),
				requestedMethodIdx);


		final TriggeringLoadMethodRequestInfo triggeringRequestInfo
			= getTriggeringRequestInfo(request, type, methodNo, requestedMethodIdx);

		if (request.getCallingMethod() != -1) {
			loaderMaps.codeMap.addMethodCall(request.getCallingMethod(), methodNo);
		}

		ExtendingTypesHelper.addAnyExtendingMethods(classBytecode, type, methodNo, requestedMethodIdx, request, loaderMaps);
		
		scanMethodTypes(classBytecode, requestedMethodIdx, methodNo);
		
		final int numAddedToLoadQueue = loadCalledMethods(triggeringRequestInfo, methodNo);
		
		if (numAddedToLoadQueue == 0) {
			checkReturnLoadResponse(triggeringRequestInfo);
		}
	}
	
	private static TriggeringLoadMethodRequestInfo getTriggeringRequestInfo(
			NameLoadMethodRequest request,
			int type,
			int methodNo,
			int requestedMethodIdx) {

		final TriggeringLoadMethodRequestInfo triggeringRequestInfo;
		
		if (request instanceof InitialLoadMethodRequest) {
			
			final InitialLoadMethodRequest initialRequest = (InitialLoadMethodRequest)request;
			
			triggeringRequestInfo = new TriggeringLoadMethodRequestInfo(
					initialRequest.getThreadNo(),
					initialRequest.getNotifyObject(),
					type,
					methodNo,
					requestedMethodIdx);
			
		}
		else if (request instanceof ScanLoadMethodRequest) {

			final ScanLoadMethodRequest scanRequest = (ScanLoadMethodRequest)request;

			triggeringRequestInfo = scanRequest.getTriggeringLoadRequestInfo();
		}
		else {
			throw new UnsupportedOperationException();
		}

		return triggeringRequestInfo;
	}
	
	private void checkReturnLoadResponse(TriggeringLoadMethodRequestInfo triggeringRequestInfo) {

		if (!loadQueues.hasLoadRequestsForThread(triggeringRequestInfo.getThreadNo())) {
			
			final ClassBytecode triggeringClass = loaderMaps.loadedClasses.getBytecode(triggeringRequestInfo.getType());
			
			final int methodIdx = triggeringRequestInfo.getMethodIdx();
			final MethodVariant methodVariant = triggeringClass.getMethodVariant(methodIdx);
			final byte[] methodBytecode = triggeringClass.getMethodBytecode(methodIdx);
			
			final METHOD compiledMethod = bytecodeCompiler.compileMethod(
					triggeringRequestInfo.getType(),
					methodIdx,
					methodVariant,
					methodBytecode);
			
			final LoadMethodResponse<METHOD> loadMethodResponse = new LoadMethodResponse<>(
					triggeringRequestInfo,
					compiledMethod);
			
			loadQueues.addLoadMethodResponse(loadMethodResponse);
			
			triggeringRequestInfo.getNotifyObject().notify();
		}
	}

	private void scanMethodTypes(ClassBytecode classBytecode, int methodIdx, int methodNo) {
		
		final LoadedMethod scannedMethod = new LoadedMethod();
		
		classBytecode.scanMethodClassReferences(methodIdx, methodScanner, scannedMethod);

		scannedMethodsArray.set(methodNo, scannedMethod);
	}
	
	private int loadCalledMethods(TriggeringLoadMethodRequestInfo triggeringRequestInfo, int fromMethodNo) {
		
		final int numAddedToLoadQueue = loaderMaps.codeMap.recurseCallGraph(
				fromMethodNo,
				(referencedMethodNo, depth) -> {
			
					return processCalledMethod(fromMethodNo, triggeringRequestInfo, referencedMethodNo, depth);
		});
		
		return numAddedToLoadQueue;
	}
	
	private MethodRefStatus processCalledMethod(int fromMethodNo, TriggeringLoadMethodRequestInfo triggeringRequestInfo, int referencedMethodNo, int depth) {
		if (depth > SCAN_DEPTH) {
			throw new IllegalStateException();
		}
		
		final LoadedMethod loadedMethod = scannedMethodsArray.get(referencedMethodNo);
		
		final MethodRefStatus status;
		
		if (loadedMethod == null) {
			throw new IllegalStateException();
		}
		else if (depth == SCAN_DEPTH) {
			// Skip, break out of this branch of recursion
			status = MethodRefStatus.SKIPPED_BREAK;
		}
		else {
			final boolean processed = loadedMethod.isProcessed();
		
			if (!processed) {
				
				for (InvokedMethod invokedMethod : loadedMethod.getInvokedMethods()) {
					
					final ScanLoadMethodRequest loadMethodRequest = new ScanLoadMethodRequest(
							fromMethodNo,
							invokedMethod.getTypeName(),
							invokedMethod.getMethodName(),
							invokedMethod.getParameterTypes(),
							triggeringRequestInfo);
					
					loadQueues.addLoadMethodRequest(loadMethodRequest);
				}
				
				status = MethodRefStatus.PROCESSED_CONTINUE;
			}
			else {
				status = MethodRefStatus.SKIPPED_CONTINUE;
			}
		}
		
		return status;
	}
}
