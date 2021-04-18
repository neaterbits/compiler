package dev.nimbler.ide.common.scheduling;

import org.jutils.concurrency.scheduling.Constraint;
import org.jutils.concurrency.scheduling.ScheduleFunction;
import org.jutils.concurrency.scheduling.ScheduleListener;

public interface IDEScheduler {

	<T, R>void scheduleTask(
			String name,
			String description,
			Constraint constraint,
			T param,
			ScheduleFunction<T, R> function,
			ScheduleListener<T, R> listener);
	
}
