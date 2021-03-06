package dev.nimbler.build.strategies.compileeachmodule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jutils.concurrency.dependencyresolution.spec.TargetBuilderSpec;
import org.jutils.concurrency.dependencyresolution.spec.builder.ActionLog;
import org.jutils.concurrency.dependencyresolution.spec.builder.CommandLineActionLog;
import org.jutils.concurrency.dependencyresolution.spec.builder.TargetBuilder;
import org.jutils.concurrency.scheduling.Constraint;

import dev.nimbler.build.common.compile.BuildException;
import dev.nimbler.build.strategies.common.ModulesBuildContext;
import dev.nimbler.build.strategies.common.PrerequisitesBuilderExternalDependencies;
import dev.nimbler.build.strategies.common.PrerequisitesBuilderModuleCompileList;
import dev.nimbler.build.strategies.common.PrerequisitesBuilderProjectDependencies;
import dev.nimbler.build.types.compile.Compiler;
import dev.nimbler.build.types.compile.CompilerStatus;
import dev.nimbler.build.types.compile.ExternalModuleDependencyList;
import dev.nimbler.build.types.compile.ModuleCompileList;
import dev.nimbler.build.types.compile.ProjectModuleDependencyList;
import dev.nimbler.build.types.dependencies.LibraryDependency;
import dev.nimbler.build.types.dependencies.ProjectDependency;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.compile.CompiledModuleFileResourcePath;

public class TargetBuilderModules extends TargetBuilderSpec<ModulesBuildContext> {

	public static final String TARGET_COMPILEALL = "compileall";
	
	@Override
	protected void buildSpec(TargetBuilder<ModulesBuildContext> targetBuilder) {
		
		targetBuilder.addTarget(TARGET_COMPILEALL, "modules", "compile", "Compile all modules")
			.withPrerequisites("Modules")
			.fromIterating(context -> context.getModules())
			.buildBy(subTarget-> subTarget
					
				.addFileSubTarget(
				        ProjectModuleResourcePath.class,
                        "module",
                        "compile",
						CompiledModuleFileResourcePath.class,
						(context, module) -> context.getCompiledModuleFile(module),
						CompiledModuleFileResourcePath::getFile,
						module -> "Compile module  " + module.getName())

					// collect dependencies in list for later
				/*
					.withPrerequisites("Module dependencies list")
						.makingProduct(ModuleDependencyList.class)
						.fromItemType(Dependency.class)
						.fromIterating(null, ModuleBuilderUtil::transitiveProjectDependencies)
						.collectToProduct((module, dependencyList) -> new ModuleDependencyList(module, dependencyList))
				*/
					// add targets for local module dependencies
					.withPrerequisites(new PrerequisitesBuilderProjectDependencies())

					// for downloading external dependencies
					.withPrerequisites(new PrerequisitesBuilderExternalDependencies<>())
					
					// must collect info on classes to compile into a list
					// so can run compiler onto multiple files
					.withPrerequisites(new PrerequisitesBuilderModuleCompileList())
					
					.action(Constraint.CPU, (context, target, actionParameters) -> {
						
						final ModuleCompileList moduleCompileList = actionParameters.getCollectedProduct(
								target,
								ModuleCompileList.class);
						
						final ExternalModuleDependencyList externalDependencyList = actionParameters.getCollectedProduct(
								target,
								ExternalModuleDependencyList.class);

						final ProjectModuleDependencyList projectDependencyList = actionParameters.getCollectedProduct(
								target,
								ProjectModuleDependencyList.class);
						
						/*
						System.out.println("## module compile list " + moduleCompileList);
						
						System.out.println("## external dependency list " + externalDependencyList + " for " + target);

						System.out.println("## project dependency list " + projectDependencyList);
						*/

						if (moduleCompileList == null) {
							throw new IllegalStateException();
						}

						/* May be null for pom files without dependencies
						if (externalDependencyList == null) {
							throw new IllegalStateException();
						}
						*/

						final ActionLog actionLog;
						
						if (!moduleCompileList.getSourceFiles().isEmpty()) {
						
							final File targetDirectory = context.getTargetDirectory(target).getFile();
	
							actionLog = compileSourceFiles(context.getCompiler(), moduleCompileList, targetDirectory, projectDependencyList, externalDependencyList);
						}
						else {
							actionLog = null;
						}
						
						return actionLog;
					})
			);
		
	}

	
	private static ActionLog compileSourceFiles(
			Compiler compiler,
			ModuleCompileList moduleCompileList,
			File targetDirectory,
			ProjectModuleDependencyList projectModuleDependencyList,
			ExternalModuleDependencyList externalDependencyList) throws IOException, BuildException {

		if (moduleCompileList == null) {
			throw new IllegalStateException();
		}

		final List<File> dependencies = new ArrayList<>(
				  projectModuleDependencyList.getDependencies().size()
				+ (externalDependencyList != null ? externalDependencyList.getDependencies().size() : 0));

		projectModuleDependencyList.getDependencies().stream()
			.map(ProjectDependency::getCompiledModuleFile)
			.forEach(dependencies::add);

		if (externalDependencyList != null) {
			externalDependencyList.getDependencies().stream()
				.map(LibraryDependency::getCompiledModuleFile)
				.forEach(dependencies::add);
		}
		
		final CompilerStatus status = compiler.compile(

				moduleCompileList.getSourceFiles().stream()
					.flatMap(sourceFiles -> sourceFiles.getFileCompilations().stream())
					.map(fileCompilation -> fileCompilation.getSourcePath())
					.collect(Collectors.toList()),
					
				targetDirectory,
				dependencies);

		if (!status.executedOk()) {
			throw new BuildException(status.getIssues());
		}
		else {
			return new CommandLineActionLog(status.getCommandLine(), status.getExitCode());
		}
	}
}
