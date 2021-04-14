package com.neaterbits.exe.vm.bytecode.loader;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

final class LoadMethodQueues<METHOD> {

	private final LinkedList<LoadMethodRequest> loadRequestQueue;
	private final LinkedList<LoadMethodResponse<METHOD>> loadResponseQueue;

	LoadMethodQueues() {
		this.loadRequestQueue = new LinkedList<>();
		this.loadResponseQueue = new LinkedList<>();
	}
	
	void addLoadMethodRequest(LoadMethodRequest loadMethodRequest) {

		Objects.requireNonNull(loadMethodRequest);
		
		synchronized (loadRequestQueue) {
			loadRequestQueue.add(loadMethodRequest);
		}
	}
	
	void addLoadMethodResponse(LoadMethodResponse<METHOD> loadMethodResponse) {
		
		Objects.requireNonNull(loadMethodResponse);
		
		synchronized (loadResponseQueue) {
			
			for (LoadMethodResponse<METHOD> response : loadResponseQueue) {
				if (response.getRequestInfo().getThreadNo() == loadMethodResponse.getRequestInfo().getThreadNo()) {
					throw new IllegalStateException();
				}
			}
			
			loadResponseQueue.add(loadMethodResponse);
		}
	}
	
	LoadMethodResponse<METHOD> getInitialMethodResponse() {
		
		final LoadMethodResponse<METHOD> loadMethodResponse;
		
		synchronized (loadResponseQueue) {
			loadMethodResponse = loadResponseQueue.removeFirst();
			
			if (loadMethodResponse == null) {
				throw new IllegalStateException();
			}
			
			if (!loadResponseQueue.isEmpty()) {
				throw new IllegalStateException();
			}
		}
	
		return loadMethodResponse;
	}
	
	LoadMethodResponse<METHOD> getThreadMethodResponse(int threadNo) {
		
		LoadMethodResponse<METHOD> loadMethodResponse = null;
		
		synchronized (loadResponseQueue) {
			
			final Iterator<LoadMethodResponse<METHOD>> iter = loadResponseQueue.iterator();
			
			while (iter.hasNext()) {
				final LoadMethodResponse<METHOD> response = iter.next();
				
				if (response.getRequestInfo().getThreadNo() == threadNo) {
					if (loadMethodResponse != null) {
						throw new IllegalStateException();
					}
					
					loadMethodResponse = response;
				}
			}

			if (loadMethodResponse == null) {
				throw new IllegalStateException();
			}
		}
	
		return loadMethodResponse;
	}
	
	LoadMethodRequest getNextMethodRequest(RunningState runningState) {
		
		LoadMethodRequest loadMethodRequest;
		
		synchronized (loadRequestQueue) {
			
			do {
				loadMethodRequest = loadRequestQueue.removeFirst();
				
				if (loadMethodRequest == null) {
					try {
						loadRequestQueue.wait();
					} catch (InterruptedException ex) {
					}
				}
			} while (runningState.isRunning() && loadMethodRequest == null);
		}

		return loadMethodRequest;
	}

	boolean hasLoadRequestsForThread(int threadNo) {
		
		synchronized (loadRequestQueue) {
			for (LoadMethodRequest loadMethodRequest : loadRequestQueue) {
				
				final ScanLoadMethodRequest scanLoadMethodRequest = (ScanLoadMethodRequest)loadMethodRequest;
				
				if (scanLoadMethodRequest.getTriggeringLoadRequestInfo().getThreadNo() == threadNo) {
					return true;
				}
			}
		}
	
		return false;
	}
}
