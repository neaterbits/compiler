package dev.nimbler.ide.core.tasks;

import java.util.stream.Collectors;

import org.jutils.concurrency.dependencyresolution.spec.TargetBuilderSpec;
import org.jutils.concurrency.dependencyresolution.spec.builder.TargetBuilder;
import org.jutils.concurrency.scheduling.Constraint;

import dev.nimbler.build.types.resource.ProjectModuleResourcePath;

public final class TargetBuilderAddSystemLibraryTypesToCodeMap extends TargetBuilderSpec<InitialScanContext> {

	@Override
	protected void buildSpec(TargetBuilder<InitialScanContext> targetBuilder) {

		targetBuilder
			.addTarget(
			        "systemlibrarytypes",
			        "system_libraries_types_to_codemap",
			        "scan_system_libraries_for_types",
			        "System library type names")

			.withPrerequisites("System library files")
			
			// System libraries may be different for different modules
			.fromIterating(context -> context.getModules())
			
			.buildBy(st -> st.addInfoSubTarget(
			        ProjectModuleResourcePath.class,
			        "module_system_libraries_types_to_codemap",
                    "scan_module_system_libraries_for_types",
			        module -> module.getModuleId().toString(),
			        module -> "System library types for " + module.getName())
			        
		        .withPrerequisites("Module system libraries")
			
    			.fromIterating(Constraint.CPU, (context, module) -> context
    			                                .getBuildRoot()
    			                                .getRuntimeEnvironment(module)
    			                                .getSystemLibraries()
    			                                .getFiles()
    			                                .stream()
    			                                .map(dep -> new SystemLibraryDep(module, dep))
    			                                .collect(Collectors.toList()))
    			
    			.buildBy(stf -> stf
    				.addInfoSubTarget(
    					SystemLibraryDep.class,
                        "system_library_types_to_codemap",
                        "scan_system_library_for_types",
    					file -> file.getTo().getFile().getName(),
    					file -> "System library type names from " + file.getTo().getFile().getPath())
    				
    				.action(Constraint.IO, (context, target, parameters) -> {
    				
    					context.getCodeMapGatherer().addSystemLibraryFile(
    					        target.getFrom(),
    					        target.getTo());
    					
    					return null;
    				})));
		
	}
}
