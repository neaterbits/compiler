package dev.nimbler.ide.core.tasks;

import java.util.Set;
import java.util.stream.Collectors;

import org.jutils.concurrency.dependencyresolution.spec.TargetBuilderSpec;
import org.jutils.concurrency.dependencyresolution.spec.builder.TargetBuilder;
import org.jutils.concurrency.scheduling.Constraint;

import dev.nimbler.build.buildsystem.common.Scope;
import dev.nimbler.build.model.BuildRoot.DependencySelector;
import dev.nimbler.build.model.runtimeenvironment.RuntimeEnvironment;
import dev.nimbler.build.types.resource.LibraryResourcePath;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.language.common.types.TypeName;

// Add type names from eg. jar file index
public final class TargetBuilderAddLibraryTypesToCodeMap extends TargetBuilderSpec<InitialScanContext> {

	private static final DependencySelector DEPENDENCY_SELECTOR
				= (scope, optional) -> scope != Scope.TEST;
	
	@Override
	protected void buildSpec(TargetBuilder<InitialScanContext> targetBuilder) {

		targetBuilder.addTarget(
		        "librariescodemap",
		        "libraries_codemap",
		        "build_codemap",
		        "External libraries code map")
			.withPrerequisites("modules")
			.fromIterating(InitialScanContext::getModules)
			
			.buildBy(st -> st.addInfoSubTarget(
						ProjectModuleResourcePath.class,
						"module",
						"module_scan",
						module -> module.getName(),
						module -> "Scan module " + module.getName())
					.withPrerequisites("Scan any found libraries")
					.fromIterating(
					        null,
					        (context, module) ->
					        	context.getBuildRoot().getTransitiveLibraryDependenciesForProjectModule(
					                                module,
					                                DEPENDENCY_SELECTOR).stream()
					            .map(dep -> new LibraryDep(module, dep))
					            .collect(Collectors.toList()))
					
					.buildBy(subTarget -> {
						subTarget.addFileSubTarget(
								LibraryDep.class,
								"library_dependency" ,
								"gather_library_dependency",
								LibraryResourcePath.class,
								(context, dependency) -> dependency.getTo().getModulePath(),
								LibraryResourcePath::getFile,
								
								dependency -> "Project dependency "
								               + dependency.getTo().getModulePath().getLast().getName())
						
						.action(Constraint.IO, (context, target, parameters) -> {
							
						    final RuntimeEnvironment runtimeEnvironment
						        = context.getBuildRoot().getRuntimeEnvironment(target.getFrom());
						    
							final LibraryResourcePath libraryResourcePath = target.getTo().getModulePath();
							
							final Set<TypeName> types
							    = runtimeEnvironment.getTypesFromLibraryFile(libraryResourcePath);
							
							context.getCodeMapGatherer().addLibraryFileTypes(libraryResourcePath, types);
							
							return null;
						});
					}));
		
	}
}
