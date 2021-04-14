package com.neaterbits.exe.vm.bytecode.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import com.neaterbits.exe.vm.bytecode.loader.codemap.LoaderCodeMap;
import com.neaterbits.language.bytecode.common.BytecodeFormat;
import com.neaterbits.language.common.types.FieldType;
import com.neaterbits.language.common.types.TypeName;
import com.neaterbits.language.common.typesources.libs.ClassLibs;

public class BytecodeLoader<CLASS, METHOD> {

	private static final int NUM_THREADS = 3;
	
	private volatile boolean isRunning;
	
	private final IntegerTypeMap typeNameMap;
	private final LoadedClassesCache loadedClasses;
	
	private final LoadMethodQueues<METHOD> loadQueues;
	
	private final List<MethodLoaderThread<CLASS, METHOD>> threads;
	private final ThreadsafeArray<LoadedMethod> scannedMethodsArray;
	
	
	public BytecodeLoader(BytecodeFormat bytecodeFormat, ClassLibs classLibs, BytecodeCompiler<CLASS, METHOD> bytecodeCompiler, LoaderCodeMap codeMap) {

		Objects.requireNonNull(classLibs);
		Objects.requireNonNull(bytecodeCompiler);
		Objects.requireNonNull(codeMap);
		
		this.loadedClasses = new LoadedClassesCache();
		
		this.loadQueues = new LoadMethodQueues<>();
		this.threads = new ArrayList<>(NUM_THREADS);
		
		this.scannedMethodsArray = new ThreadsafeArray<>(new LoadedMethod[10000]);
		
		this.typeNameMap = new IntegerTypeMap();
		
		final LoaderMaps loaderMaps = new LoaderMaps(
				codeMap,
				typeNameMap,
				loadedClasses);
		
		for (int i = 0; i < NUM_THREADS; ++ i) {
			final MethodLoaderThread<CLASS, METHOD> thread = new MethodLoaderThread<>(
					this,
					loadQueues,
					bytecodeFormat,
					classLibs,
					bytecodeCompiler,
					loaderMaps,
					scannedMethodsArray);

			threads.add(thread);
			
			thread.start();
		}
	
		this.isRunning = true;
	}
	
	boolean isRunning() {
		return isRunning;
	}
	
	public METHOD loadInitialMethod(ThreadData thread, TypeName className, String methodName, List<FieldType> parameterTypes) {

		final int type = typeNameMap.getTypeNo(className);
		
		final METHOD method;
		
		if (type >= 0) {
			throw new IllegalStateException();
		}
		else {
			final InitialLoadMethodRequest loadMethodRequest = new InitialLoadMethodRequest(
					className,
					methodName,
					parameterTypes,
					thread.getThreadNo(),
					thread);
			
			loadQueues.addLoadMethodRequest(loadMethodRequest);
			
			final LoadMethodResponse<METHOD> loadMethodResponse = waitLoadMethodResponse(thread, loadQueues::getInitialMethodResponse);
			
			method = loadMethodResponse.getCompiledMethod();
		}
		
		return method;
	}
	
	@SuppressWarnings("unchecked")
	public METHOD loadMethodFromExecutionPath(ThreadData thread, int methodNo) {
		
		final LoadedMethod loadedMethod = scannedMethodsArray.get(methodNo);
		
		final METHOD method;
		
		if (loadedMethod.getCompiledMethod() != null) {
			// Compiled by preloader or by other thread
			method = (METHOD)loadedMethod.getCompiledMethod();
		}
		else {
			// Add to queue for loading by preloader
			final ExecutionPathLoadMethodRequest loadMethodRequest = new ExecutionPathLoadMethodRequest(
					methodNo,
					thread.getThreadNo(),
					thread);
			
			loadQueues.addLoadMethodRequest(loadMethodRequest);
			
			final LoadMethodResponse<METHOD> loadMethodResponse = waitLoadMethodResponse(
					thread,
					() -> loadQueues.getThreadMethodResponse(thread.getThreadNo()));

			method = loadMethodResponse.getCompiledMethod();
		}
		
		return method;
	}
	
	private LoadMethodResponse<METHOD> waitLoadMethodResponse(ThreadData thread, Supplier<LoadMethodResponse<METHOD>> getFromQueue) {
		LoadMethodResponse<METHOD> loadMethodResponse;

		synchronized (thread) {
			for (;;) {
				try {
					thread.wait();

					loadMethodResponse = getFromQueue.get();

					break;
				} catch (InterruptedException ex) {
					throw new IllegalStateException(ex);
				}
			}
		}
		
		return loadMethodResponse;
	}
}
