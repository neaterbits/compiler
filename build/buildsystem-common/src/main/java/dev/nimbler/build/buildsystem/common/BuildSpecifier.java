package dev.nimbler.build.buildsystem.common;

import java.util.List;

import com.neaterbits.util.concurrency.scheduling.task.TaskContext;

import dev.nimbler.build.types.Build;

public interface BuildSpecifier<CONTEXT extends TaskContext> {

    List<Build<CONTEXT>> specifyBuild(String [] args) throws ArgumentException;

    CONTEXT createTaskContext(BuildSystemRootScan buildSystemRoot);
}
