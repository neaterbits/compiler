package dev.nimbler.ide.code.tasks;

import org.junit.Test;
import org.jutils.concurrency.scheduling.AsyncExecutor;
import org.jutils.concurrency.scheduling.Constraint;
import org.jutils.concurrency.scheduling.ScheduleFunction;
import org.jutils.concurrency.scheduling.ScheduleListener;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;

import dev.nimbler.ide.common.tasks.TaskName;

public class TaskManagerImplTest {

	@Test
	public void testRun() {
	
		final AsyncExecutor asyncExecutor = Mockito.mock(AsyncExecutor.class);
		
		final TaskManager taskManager = new TaskManagerImpl(asyncExecutor, Runnable::run);

		final Object param1 = new Object();
		final ScheduleFunction<Object, Object> function1 = param -> null;
		
		taskManager.scheduleTask(
				"task1",
				"Task 1",
				Constraint.IO,
				param1,
				function1,
				(param, result) -> { });

		final Object param2 = new Object();
		final ScheduleFunction<Object, Object> function2 = param -> null;
		
		taskManager.scheduleTask(
				"task2",
				"Task 2",
				Constraint.IO,
				param2,
				function2,
				(param, result) -> { });
		
		@SuppressWarnings("unchecked")
		final ArgumentCaptor<ScheduleListener<Object, Object>> listenerCaptor1
			= ArgumentCaptor.forClass(ScheduleListener.class);
		
		Mockito.verify(asyncExecutor).schedule(
				eq(Constraint.IO),
				same(param1),
				same(function1),
				listenerCaptor1.capture());

		@SuppressWarnings("unchecked")
		final ArgumentCaptor<ScheduleListener<Object, Object>> listenerCaptor2
			= ArgumentCaptor.forClass(ScheduleListener.class);

		Mockito.verify(asyncExecutor).schedule(
				eq(Constraint.IO),
				same(param2),
				same(function2),
				listenerCaptor2.capture());

		Mockito.verifyNoMoreInteractions(asyncExecutor);
		
		final ScheduleListener<Object, Object> listener1 = listenerCaptor1.getValue();
		final ScheduleListener<Object, Object> listener2 = listenerCaptor2.getValue();
		
		listener1.onScheduleResult(param1, function1.perform(param1));
		
		Mockito.verifyNoMoreInteractions(asyncExecutor);
		
		listener2.onScheduleResult(param2, function2.perform(param2));

		Mockito.verifyNoMoreInteractions(asyncExecutor);
	}

	@Test
	public void testRunAfter() {
	
		final AsyncExecutor asyncExecutor = Mockito.mock(AsyncExecutor.class);
		
		final TaskManager taskManager = new TaskManagerImpl(asyncExecutor, Runnable::run);

		final Object param1 = new Object();
		final ScheduleFunction<Object, Object> function1 = param -> null;
		
		final TaskName task1 = taskManager.scheduleTask(
				"task1",
				"Task 1",
				Constraint.IO,
				param1,
				function1,
				(param, result) -> { });

		final Object param2 = new Object();
		final ScheduleFunction<Object, Object> function2 = param -> null;
		
		taskManager.scheduleTask(
				"task2",
				"Task 2",
				Constraint.IO,
				param2,
				function2,
				(param, result) -> { },
				task1);
		
		@SuppressWarnings("unchecked")
		final ArgumentCaptor<ScheduleListener<Object, Object>> listenerCaptor
			= ArgumentCaptor.forClass(ScheduleListener.class);
		
		Mockito.verify(asyncExecutor).schedule(
				eq(Constraint.IO),
				same(param1),
				same(function1),
				listenerCaptor.capture());
		
		Mockito.verifyNoMoreInteractions(asyncExecutor);
		
		final ScheduleListener<Object, Object> listener1 = listenerCaptor.getValue();
		
		listener1.onScheduleResult(param1, function1.perform(param1));

		Mockito.verify(asyncExecutor).schedule(
				eq(Constraint.IO),
				same(param2),
				same(function2),
				listenerCaptor.capture());
		
		Mockito.verifyNoMoreInteractions(asyncExecutor);
		
		final ScheduleListener<Object, Object> listener2 = listenerCaptor.getValue();
		
		listener2.onScheduleResult(param2, function2.perform(param2));

		Mockito.verifyNoMoreInteractions(asyncExecutor);
	}
}
