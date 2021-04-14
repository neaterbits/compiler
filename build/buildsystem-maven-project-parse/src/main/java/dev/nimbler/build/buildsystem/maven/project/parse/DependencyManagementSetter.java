package dev.nimbler.build.buildsystem.maven.project.parse;

import dev.nimbler.build.buildsystem.maven.project.model.MavenDependencyManagement;

interface DependencyManagementSetter {

    void setDependencyManagement(MavenDependencyManagement dependencyManagement);
}
