package dev.nimbler.build.strategies.common;

import java.util.stream.Collectors;

import com.neaterbits.util.concurrency.dependencyresolution.spec.PrerequisitesBuilderSpec;
import com.neaterbits.util.concurrency.dependencyresolution.spec.builder.PrerequisitesBuilder;

import dev.nimbler.build.types.compile.ProjectModuleDependencyList;
import dev.nimbler.build.types.dependencies.ProjectDependency;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.compile.CompiledModuleFileResourcePath;

public final class PrerequisitesBuilderProjectDependencies extends PrerequisitesBuilderSpec<ModulesBuildContext, ProjectModuleResourcePath> {

	@Override
	public void buildSpec(PrerequisitesBuilder<ModulesBuildContext, ProjectModuleResourcePath> builder) {

		builder
			.withPrerequisites("Project dependencies")
			.makingProduct(ProjectModuleDependencyList.class)
			.fromItemType(ProjectDependency.class)
			
			.fromIterating(null, (context, dependency) -> StrategiesBuilderUtil.transitiveProjectDependencies(context, dependency).stream()
					.map(projectDependency -> projectDependency.getModulePath())
					.collect(Collectors.toList()))
			
			.buildBy(st -> {
				st.addFileSubTarget(
						ProjectModuleResourcePath.class,
						"module",
						"collect",
						CompiledModuleFileResourcePath.class,
						(context, resourcePath) -> context.getBuildRoot().getCompiledModuleFile(resourcePath),
						CompiledModuleFileResourcePath::getFile,
						projectResourcePath -> "Project dependency " + projectResourcePath.getLast().getName());
			})
			.collectSubTargetsToProduct((module, dependencies) -> {
				
				System.out.println("## collect project dependencies " + module + " " + dependencies);
				
				final ProjectModuleDependencyList list = new ProjectModuleDependencyList(
					module,
					dependencies);
						
				return list;
				});

	}
}
