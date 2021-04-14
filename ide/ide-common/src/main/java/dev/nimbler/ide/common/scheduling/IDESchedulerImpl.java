package dev.nimbler.ide.common.scheduling;

import java.util.Objects;

import com.neaterbits.util.concurrency.scheduling.AsyncExecutor;
import com.neaterbits.util.concurrency.scheduling.Constraint;
import com.neaterbits.util.concurrency.scheduling.ScheduleFunction;
import com.neaterbits.util.concurrency.scheduling.ScheduleListener;

public class IDESchedulerImpl implements IDEScheduler {

	private final AsyncExecutor asyncExecutor;

	public IDESchedulerImpl(AsyncExecutor asyncExecutor) {

		Objects.requireNonNull(asyncExecutor);
		
		this.asyncExecutor = asyncExecutor;
	}

	@Override
	public <T, R> void scheduleTask(String name, String description, Constraint constraint,
			T param, ScheduleFunction<T, R> function, ScheduleListener<T, R> listener) {

		asyncExecutor.schedule(constraint, param, function, listener);
		
	}
}
