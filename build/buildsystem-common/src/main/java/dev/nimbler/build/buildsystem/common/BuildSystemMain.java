package dev.nimbler.build.buildsystem.common;

import java.io.File;
import java.util.List;
import java.util.function.Function;

import org.jutils.concurrency.dependencyresolution.executor.TargetBuildResult;
import org.jutils.concurrency.dependencyresolution.executor.logger.TargetExecutorLogger;
import org.jutils.concurrency.dependencyresolution.spec.TargetBuilderSpec;
import org.jutils.concurrency.scheduling.QueueAsyncExecutor;
import org.jutils.concurrency.scheduling.task.TaskContext;

import org.jutils.structuredlog.binary.logging.LogContext;

import dev.nimbler.build.types.Build;
import dev.nimbler.build.types.ModuleId;

public final class BuildSystemMain {
    
    @FunctionalInterface
    public interface OnBuildResult {
        
        void onResult(TargetBuildResult result, LogContext logContext);
    }
    
    public static boolean performBuild(
            BuildSystems buildSystems,
            String projectDirString,
            String [] args,
            Function<LogContext, TargetExecutorLogger> targetExecutorLogger,
            OnBuildResult onBuildResult) throws ScanException, ArgumentException {

        final File projectDir = new File(projectDirString);

        boolean buildPerformed = false;
        
        if (projectDir.isDirectory() && projectDir.canRead()) {

            final BuildSystem buildSystem = buildSystems.findBuildSystem(projectDir);

            if (buildSystem != null) {
                
                build(buildSystem, projectDir, args, targetExecutorLogger, onBuildResult);

                buildPerformed = true;
            }
        }

        return buildPerformed;
    }

    public static <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY, REPOSITORY, CONTEXT extends TaskContext>
    void build(
            BuildSystem buildSystem,
            File projectDir,
            String [] buildSystemArgs,
            Function<LogContext, TargetExecutorLogger> targetExecutorLogger,
            OnBuildResult onBuildResult) throws ScanException, ArgumentException {
        
        final BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY, REPOSITORY> buildSystemRoot
            = buildSystem.scan(projectDir, null);
        
        final BuildSpecifier<CONTEXT> buildSpecifier = buildSystem.getBuildSpecifier();

        final List<Build<CONTEXT>> builds = buildSpecifier.specifyBuild(buildSystemArgs);
        
        final CONTEXT context = buildSpecifier.createTaskContext(buildSystemRoot);
        
        for (Build<CONTEXT> build : builds) {
            build(build.getTargetBuilder(), context, build.getTargetName(), targetExecutorLogger, onBuildResult);
        }
    }
    
    private static <CONTEXT extends TaskContext>
    void build(
            TargetBuilderSpec<CONTEXT> targetBuilderSpec,
            CONTEXT context,
            String targetName,
            Function<LogContext, TargetExecutorLogger> targetExecutorLogger,
            OnBuildResult onBuildResult) {
        
        final LogContext logContext = new LogContext();

        final QueueAsyncExecutor asyncExecutor = new QueueAsyncExecutor(false);

        targetBuilderSpec.execute(
                logContext,
                context,
                targetName,
                targetExecutorLogger.apply(logContext),
                asyncExecutor,
                result -> {
                    
                    if (onBuildResult != null) {
                        onBuildResult.onResult(result, logContext);
                    }
                });
    }
}
