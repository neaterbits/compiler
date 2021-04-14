package dev.nimbler.build.buildsystem.maven.project.parse;

import dev.nimbler.build.buildsystem.maven.common.parse.DependenciesSetter;

interface CommonSetter 
    extends ModulesSetter,
            BuildSetter,
            ReportingSetter,
            RepositoriesSetter,
            PluginRepositoriesSetter,
            DependencyManagementSetter,
            DependenciesSetter {
    
}
