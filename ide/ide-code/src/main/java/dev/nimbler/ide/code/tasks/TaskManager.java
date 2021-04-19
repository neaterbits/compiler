package dev.nimbler.ide.code.tasks;

import java.util.function.Supplier;

import org.jutils.concurrency.dependencyresolution.spec.TargetBuilderSpec;
import org.jutils.concurrency.scheduling.Constraint;
import org.jutils.concurrency.scheduling.ScheduleFunction;
import org.jutils.concurrency.scheduling.ScheduleListener;
import org.jutils.concurrency.scheduling.task.TaskContext;
import org.jutils.threads.ForwardResultToCaller;

import dev.nimbler.ide.common.tasks.TaskName;

public interface TaskManager extends ForwardResultToCaller {

	<T, R> TaskName scheduleTask(
			String name,
			String description,
			Constraint constraint,
			T param,
			ScheduleFunction<T, R> function,
			ScheduleListener<T, R> listener,
			TaskName ... runAfter);

	<CONTEXT extends TaskContext> TaskName scheduleTargets(
			String name,
			String description,
			TargetBuilderSpec<CONTEXT> spec,
			String targetName,
			Supplier<CONTEXT> context,
			TaskName ... runAfter);
}
