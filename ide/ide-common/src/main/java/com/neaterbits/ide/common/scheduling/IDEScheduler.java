package com.neaterbits.ide.common.scheduling;

import com.neaterbits.util.concurrency.scheduling.Constraint;
import com.neaterbits.util.concurrency.scheduling.ScheduleFunction;
import com.neaterbits.util.concurrency.scheduling.ScheduleListener;

public interface IDEScheduler {

	<T, R>void scheduleTask(
			String name,
			String description,
			Constraint constraint,
			T param,
			ScheduleFunction<T, R> function,
			ScheduleListener<T, R> listener);
	
}
