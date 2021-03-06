package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.List;
import java.util.Map;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.maven.common.model.MavenDependency;
import dev.nimbler.build.buildsystem.maven.common.parse.StackEntity;
import dev.nimbler.build.buildsystem.maven.project.model.MavenBuild;
import dev.nimbler.build.buildsystem.maven.project.model.MavenCiManagement;
import dev.nimbler.build.buildsystem.maven.project.model.MavenDependencyManagement;
import dev.nimbler.build.buildsystem.maven.project.model.MavenDistributionManagement;
import dev.nimbler.build.buildsystem.maven.project.model.MavenIssueManagement;
import dev.nimbler.build.buildsystem.maven.project.model.MavenMailingList;
import dev.nimbler.build.buildsystem.maven.project.model.MavenOrganization;
import dev.nimbler.build.buildsystem.maven.project.model.MavenParent;
import dev.nimbler.build.buildsystem.maven.project.model.MavenPluginRepository;
import dev.nimbler.build.buildsystem.maven.project.model.MavenProfile;
import dev.nimbler.build.buildsystem.maven.project.model.MavenReporting;
import dev.nimbler.build.buildsystem.maven.project.model.MavenRepository;
import dev.nimbler.build.buildsystem.maven.project.model.MavenScm;

final class StackProject
        extends StackEntity
        implements NameSetter, UrlSetter, CommonSetter, PropertiesSetter, DistributionManagementSetter {

	private String name;

	private String description;
	
	private String url;
	
	private MavenParent parent;
	
	private Map<String, String> properties;
	
	private final StackCommon common;
	
    private MavenOrganization organization;
    
	private MavenIssueManagement issueManagement;
	
	private MavenCiManagement ciManagement;
	
	private List<MavenMailingList> mailingLists;
	
	private MavenScm scm;
	
	private List<MavenProfile> profiles;
	
	StackProject(Context context) {
		super(context);
		
		this.common = new StackCommon();
	}

	String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    String getUrl() {
        return url;
    }

	@Override
    public void setUrl(String url) {
        this.url = url;
    }

    public MavenParent getParent() {
		return parent;
	}

	public void setParent(MavenParent parent) {
		this.parent = parent;
	}

	Map<String, String> getProperties() {
        return properties;
    }

	@Override
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    StackCommon getCommon() {
        return common;
    }

    @Override
    public void setBuild(MavenBuild build) {
        
        common.setBuild(build);
    }

    @Override
    public void setReporting(MavenReporting reporting) {

        common.setReporting(reporting);
    }

    @Override
    public void setModules(List<String> modules) {

        common.setModules(modules);
    }

    @Override
    public void setRepositories(List<MavenRepository> repositories) {

        common.setRepositories(repositories);
    }

    @Override
    public void setPluginRepositories(List<MavenPluginRepository> pluginRepositories) {

        common.setPluginRepositories(pluginRepositories);
    }

    @Override
    public void setDependencyManagement(MavenDependencyManagement dependencyManagement) {
        common.setDependencyManagement(dependencyManagement);
    }

    @Override
    public void setDependencies(List<MavenDependency> dependencies) {

        common.setDependencies(dependencies);
    }

    MavenOrganization getOrganization() {
        return organization;
    }

    void setOrganization(MavenOrganization organization) {
        this.organization = organization;
    }

    MavenIssueManagement getIssueManagement() {
        return issueManagement;
    }

    void setIssueManagement(MavenIssueManagement issueManagement) {
        this.issueManagement = issueManagement;
    }

    MavenCiManagement getCiManagement() {
        return ciManagement;
    }

    void setCiManagement(MavenCiManagement ciManagement) {
        this.ciManagement = ciManagement;
    }

    List<MavenMailingList> getMailingLists() {
        return mailingLists;
    }

    void setMailingLists(List<MavenMailingList> mailingLists) {
        this.mailingLists = mailingLists;
    }

    MavenScm getScm() {
        return scm;
    }

    void setScm(MavenScm scm) {
        this.scm = scm;
    }

    @Override
    public void setDistributionManagement(MavenDistributionManagement distributionManagement) {
        common.setDistributionManagement(distributionManagement);
    }

    List<MavenProfile> getProfiles() {
        return profiles;
    }

    void setProfiles(List<MavenProfile> profiles) {
        this.profiles = profiles;
    }
}
