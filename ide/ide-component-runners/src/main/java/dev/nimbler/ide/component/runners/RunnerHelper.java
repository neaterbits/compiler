package dev.nimbler.ide.component.runners;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

import org.jutils.StringUtils;
import org.jutils.process.ProcessRunner;

import dev.nimbler.build.common.language.BuildableLanguage;
import dev.nimbler.build.model.runtimeenvironment.RuntimeEnvironment;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.build.types.resource.compile.CompiledFileResourcePath;
import dev.nimbler.build.types.resource.compile.TargetDirectoryResourcePath;
import dev.nimbler.ide.common.codeaccess.CodeAccess;
import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.ide.common.ui.actions.ActionAppParameters;
import dev.nimbler.ide.common.ui.actions.ActionExeParameters;
import dev.nimbler.ide.common.ui.actions.ActionSourceFileParameters;
import dev.nimbler.ide.component.common.runner.RunnableLanguage;
import dev.nimbler.ide.component.common.runner.RunnerComponent;
import dev.nimbler.ide.component.runners.model.RunConfiguration;
import dev.nimbler.ide.component.runners.model.RunnersConfiguration;
import dev.nimbler.language.bytecode.common.ClassBytecode;
import dev.nimbler.language.bytecode.common.ClassFileException;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.typesources.TypeSource;

public class RunnerHelper {
    
    public static RunConfiguration run(
            RunnerComponent runnerComponent,
            SourceFileResourcePath sourceFile,
            ActionExeParameters parameters,
            RunnersConfiguration runnersConfiguration) {
        
        final RunConfiguration existingRunConfiguration = findConfiguration(sourceFile, runnersConfiguration);
        
        final RunConfiguration runConfiguration;
        
        final RunConfiguration defaultRunConfiguration;
        
        if (existingRunConfiguration != null) {

            runConfiguration = existingRunConfiguration;
            
            defaultRunConfiguration = null;
        }
        else {
            final TypeName mainClass = getBytecodeFileRunnableClass(sourceFile, runnerComponent, parameters);
            
            defaultRunConfiguration = new RunConfiguration();
            
            defaultRunConfiguration.setRunnerComponent(runnerComponent.getClass().getName());
            
            defaultRunConfiguration.setName(mainClass.getName());
            defaultRunConfiguration.setMainClass(mainClass);
            defaultRunConfiguration.setSourceFile(sourceFile);

            runConfiguration = defaultRunConfiguration;
        }
        
        run(runnerComponent, runConfiguration, parameters);

        return defaultRunConfiguration;
    }
    
    public static void run(
            RunnerComponent runnerComponent,
            RunConfiguration runConfiguration,
            ActionExeParameters parameters) {

        final SourceFileModel sourceFileModel = parameters.getSourceFileModel(runConfiguration.getSourceFile());

        final String[] programArguments = runConfiguration.getProgramArguments() != null
                ? StringUtils.tokenize(runConfiguration.getProgramArguments())
                : null;
        
        final String [] vmArguments = runConfiguration.getVmArguments() != null
                ? StringUtils.tokenize(runConfiguration.getVmArguments())
                : null;

        final String [] commandLine = runnerComponent.getCommandLineForRunning(
                runConfiguration.getSourceFile(),
                sourceFileModel,
                runConfiguration.getMainClass(),
                programArguments,
                vmArguments,
                parameters.getCodeAccess());
        
        try {
            ProcessRunner.run(
                    commandLine,
                    stdoutText -> { System.out.println("stdout: " + stdoutText); }, // outputComponent.output(ProcessSource.STDOUT, stdoutText),
                    stderrText -> { System.err.println("stderr: " + stderrText); }, // outputComponent.output(ProcessSource.STDERR, stderrText),
                    (exitStatus, join) -> join.run(),
                    parameters.getForwardResultToCaller());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static RunConfiguration findConfiguration(
            SourceFileResourcePath sourceFile,
            RunnersConfiguration configurations) {

        Objects.requireNonNull(configurations);
        Objects.requireNonNull(configurations.getRunConfigurations());
        
        final RunConfiguration sourceFileConfiguration
            = configurations.getRunConfigurations().stream()
                .filter(runConfiguration -> runConfiguration.getSourceFile().equals(sourceFile))
                .findFirst()
                .orElse(null);
        
        return sourceFileConfiguration;
    }

    private static TypeName getBytecodeFileRunnableClass(
            SourceFileResourcePath sourceFile,
            RunnerComponent runnerComponent,
            ActionExeParameters parameters) {
        
        return processByteCode(
                sourceFile,
                parameters,
                parameters.getCodeAccess(),
                runnerComponent::getRunnableType,
                null);
    }

    public static boolean isClassBytecodeFileRunnable(
            SourceFileResourcePath sourceFile,
            RunnerComponent runnerComponent,
            ActionAppParameters parameters) {
        
        return processByteCode(
                sourceFile,
                parameters,
                parameters.getCodeAccess(),
                runnerComponent::isRunnable,
                Boolean.FALSE);
    }
    
    @FunctionalInterface
    public interface BytecodeProcessor<T> {
        
        T process(
                CompiledFileResourcePath compiledFileResourcePath,
                ClassBytecode bytecode,
                RunnableLanguage runnableLanguage);
    }
    
    private static <T> T processByteCode(
            SourceFileResourcePath sourceFile,
            ActionSourceFileParameters parameters,
            CodeAccess codeAccess,
            BytecodeProcessor<T> bytecodeProcessor,
            T defaultValue) {
        
        Objects.requireNonNull(sourceFile);

        final BuildableLanguage buildableLanguage
            = parameters.getSourceFileLanguage(BuildableLanguage.class, sourceFile);

        final RunnableLanguage runnableLanguage
            = parameters.getSourceFileLanguage(RunnableLanguage.class, sourceFile);

        final ProjectModuleResourcePath module = sourceFile.getModule();
        final TargetDirectoryResourcePath targetPath = codeAccess.getTargetDirectory(module);

        final CompiledFileResourcePath compiledFileResourcePath
            = buildableLanguage.getCompiledFilePath(targetPath, sourceFile);
        
        final File file = compiledFileResourcePath.getFile();
        
        T result;

        if (file.exists() && file.isFile() && file.canRead()) {

            final RuntimeEnvironment runtimeEnvironment = codeAccess.getRuntimeEnvironment(module);

            final ClassBytecode bytecode;

            try (FileInputStream inputStream = new FileInputStream(file)) {
                bytecode = runtimeEnvironment.getBytecodeFormat().loadClassBytecode(inputStream,
                        TypeSource.COMPILED_PROJECT_MODULE);

                result = bytecodeProcessor.process(compiledFileResourcePath, bytecode, runnableLanguage);
                
            } catch (IOException | ClassFileException ex) {
                
                ex.printStackTrace();

                result = defaultValue;
            }
        }
        else {
            result = defaultValue;
        }
        
        return result;
    }
}
