package dev.nimbler.ide.core.tasks;

import java.util.Set;
import java.util.stream.Collectors;

import org.jutils.concurrency.dependencyresolution.spec.TargetBuilderSpec;
import org.jutils.concurrency.dependencyresolution.spec.builder.TargetBuilder;
import org.jutils.concurrency.scheduling.Constraint;

import dev.nimbler.build.model.runtimeenvironment.RuntimeEnvironment;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.language.common.types.TypeName;

public final class TargetBuilderAddSystemLibraryTypeNamesToCodeMap extends TargetBuilderSpec<InitialScanContext> {

	@Override
	protected void buildSpec(TargetBuilder<InitialScanContext> targetBuilder) {
		
		targetBuilder
			.addTarget(
			        "systemlibrarytypes",
			        "system_libraries_typenames_to_codemap",
			        "scan_system_libraries_for_typenames",
			        "System library type names")
			
			.withPrerequisites("System library files")
			
			.fromIterating(context -> context.getBuildRoot().getModules().stream()
			        .filter(module -> !context.getCodeMapGatherer().hasSystemLibraryFileTypes(module))
			        .collect(Collectors.toList()))
			
            .buildBy(st -> st.addInfoSubTarget(
                    ProjectModuleResourcePath.class,
                    "module_system_libraries_typenames_to_codemap",
                    "scan_module_system_libraries_for_typenames",
                    module -> module.getModuleId().toString(),
                    module -> "System library type names for " + module.getName())
                    
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
    					"system_library_typenames",
    					"scan_system_library_for_typenames",
    					file -> file.getTo().getFile().getName(),
    					file -> "System library type names from " + file.getTo().getFile().getPath())
    				.action(Constraint.IO, (context, target, parameters) -> {
    				    
    				    if (context.getCodeMapGatherer().hasSystemLibraryFileTypes(target.getFrom())) {
    				        throw new IllegalStateException();
    				    }
    					
    				    final RuntimeEnvironment runtimeEnvironment
    				        = context.getBuildRoot().getRuntimeEnvironment(target.getFrom());
    				    
    					final Set<TypeName> types
    					    = runtimeEnvironment.getTypesFromSystemLibraryFile(target.getTo());
    
    					context.getCodeMapGatherer().addSystemLibraryFileTypes(
        					        target.getFrom(),
        					        target.getTo().getFile(),
        					        types);
    					
    					return null;
    				})));
		
	}
}
