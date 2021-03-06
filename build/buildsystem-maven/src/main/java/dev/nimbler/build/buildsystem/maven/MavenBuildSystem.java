package dev.nimbler.build.buildsystem.maven;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.jutils.concurrency.scheduling.task.TaskContext;
import org.w3c.dom.Document;

import dev.nimbler.build.buildsystem.common.BuildSpecifier;
import dev.nimbler.build.buildsystem.common.BuildSystem;
import dev.nimbler.build.buildsystem.common.BuildSystemRoot;
import dev.nimbler.build.buildsystem.common.ScanException;
import dev.nimbler.build.buildsystem.common.http.HTTPDownloader;
import dev.nimbler.build.buildsystem.maven.common.model.MavenDependency;
import dev.nimbler.build.buildsystem.maven.common.model.MavenModuleId;
import dev.nimbler.build.buildsystem.maven.effective.DocumentModule;
import dev.nimbler.build.buildsystem.maven.effective.EffectivePOMReader;
import dev.nimbler.build.buildsystem.maven.model.MavenFileDependency;
import dev.nimbler.build.buildsystem.maven.plugins.MavenPluginInfo;
import dev.nimbler.build.buildsystem.maven.plugins.MavenPluginsEnvironment;
import dev.nimbler.build.buildsystem.maven.plugins.PluginsEnvironmentProvider;
import dev.nimbler.build.buildsystem.maven.plugins.PluginsEnvironmentProvider.Result;
import dev.nimbler.build.buildsystem.maven.plugins.access.MavenPluginsAccess;
import dev.nimbler.build.buildsystem.maven.plugins.access.RepositoryMavenPluginsAccess;
import dev.nimbler.build.buildsystem.maven.project.model.MavenProject;
import dev.nimbler.build.buildsystem.maven.project.model.xml.MavenXMLProject;
import dev.nimbler.build.buildsystem.maven.project.parse.PomProjectParser;
import dev.nimbler.build.buildsystem.maven.project.parse.PomTreeParser;
import dev.nimbler.build.buildsystem.maven.repositoryaccess.MavenRepositoryAccess;
import dev.nimbler.build.buildsystem.maven.targets.MavenBuildSpecifier;
import dev.nimbler.build.buildsystem.maven.xml.XMLReaderException;
import dev.nimbler.build.buildsystem.maven.xml.XMLReaderFactory;
import dev.nimbler.build.buildsystem.maven.xml.stream.JavaxXMLStreamReaderFactory;
import dev.nimbler.build.types.ModuleId;

public final class MavenBuildSystem implements BuildSystem {

    private final MavenPluginsAccess pluginsAccess;

    private final PluginsEnvironmentProvider pluginsEnvironmentProvider;
    
    private final MavenRepositoryAccess repositoryAccess;
    private final PomProjectParser<Document> pomProjectParser;

    public MavenBuildSystem() {
        this(MavenRepositoryAccess.baseMavenRepositoryPath());
    }

    public MavenBuildSystem(Path mavenRepositoryDir) {
        this(
                new URLMavenRepositoryAccess(
                        mavenRepositoryDir,
                        HTTPDownloader.create()));
    }
    
	MavenBuildSystem(MavenRepositoryAccess repositoryAccess) {
	    this(
	            new RepositoryMavenPluginsAccess(repositoryAccess),
                MavenBuildSystem::loadPluginsEnvironment,
	            repositoryAccess,
	            PomTreeParser::readModule);
    }

	MavenBuildSystem(
	        MavenPluginsAccess pluginsAccess,
	        MavenPluginsEnvironment pluginsEnvironment,
	        MavenRepositoryAccess repositoryAccess,
	        PomProjectParser<Document> pomProjectParser) {
	    
	    this(
	            pluginsAccess,
	            (pluginDeps, dep, access)
                    -> new PluginsEnvironmentProvider.Result(pluginsEnvironment, URLClassLoader.newInstance(new URL [] { })),
                repositoryAccess,
                pomProjectParser);
	}

    MavenBuildSystem(
            MavenPluginsAccess pluginsAccess,
            PluginsEnvironmentProvider pluginsEnvironmentProvider,
            MavenRepositoryAccess repositoryAccess,
            PomProjectParser<Document> pomProjectParser) {
        
        Objects.requireNonNull(pluginsAccess);
	    Objects.requireNonNull(pluginsEnvironmentProvider);
        Objects.requireNonNull(repositoryAccess);
        Objects.requireNonNull(pomProjectParser);
        
        this.pluginsAccess = pluginsAccess;
        this.pluginsEnvironmentProvider = pluginsEnvironmentProvider;
        this.repositoryAccess = repositoryAccess;

        this.pomProjectParser = pomProjectParser;
    }

    private static Result loadPluginsEnvironment(
            MavenPluginInfo pluginInfo,
            MavenDependency executePluginDependency,
            MavenRepositoryAccess repositoryAccess) {

        final List<URL> urls;
        try {
            urls = makeClassLoaderURLs(pluginInfo, executePluginDependency, repositoryAccess);
        } catch (XMLReaderException | IOException ex) {
            throw new IllegalStateException(ex);
        }
        
        final URLClassLoader classLoader = makeClassLoader(urls);
        
        final MavenPluginsEnvironment pluginsEnvironment;

        // final ClassLoader existingClassLoader = Thread.currentThread().getContextClassLoader();
        
        // Thread.currentThread().setContextClassLoader(classLoader);
        
        try {
            final Class<MavenPluginsEnvironment> cl;
    
            try {
                @SuppressWarnings("unchecked")
                final Class<MavenPluginsEnvironment> c
                    = (Class<MavenPluginsEnvironment>)classLoader.loadClass(
                            "com.neaterbits.build.buildsystem.maven.plugins.execute.MavenPluginsEnvironmentImpl");
                
                cl = c;
            } catch (ClassNotFoundException ex) {
                throw new IllegalStateException(ex);
            }

            final Constructor<MavenPluginsEnvironment> ct;
            try {
                ct = cl.getConstructor();
            } catch (NoSuchMethodException | SecurityException ex) {
                throw new IllegalStateException(ex);
            }
            
            try {
                pluginsEnvironment = ct.newInstance();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException ex) {
                
                throw new IllegalStateException(ex);
            }
        }
        finally {
            // Thread.currentThread().setContextClassLoader(existingClassLoader);
        }

        return new Result(pluginsEnvironment, classLoader);
    }

    private static List<URL> makeClassLoaderURLs(
            MavenPluginInfo pluginInfo,
            MavenDependency executePluginDependency,
            MavenRepositoryAccess repositoryAccess) throws XMLReaderException, IOException {
        
        final List<URL> urls = new ArrayList<>(pluginInfo.getAllDependencies().size() + 1);
        
        System.out.println("## all deps " + pluginInfo.getAllDependencies());
        
        if (pluginInfo.getPluginJarFile() != null) {
            urls.add(makeJarUrl(pluginInfo.getPluginJarFile()));
        }
        
        for (MavenFileDependency fileDependency : pluginInfo.getAllDependencies()) {
            urls.add(makeJarUrl(fileDependency.getJarFile()));
        }
        
        if (executePluginDependency != null) {

            final File jarFile = repositoryAccess.repositoryJarFile(
                                                        executePluginDependency.getModuleId(),
                                                        executePluginDependency.getClassifier());

            urls.add(makeJarUrl(jarFile));

            final File pomFile = repositoryAccess.repositoryExternalPomFile(executePluginDependency.getModuleId());
                
            final MavenXMLProject<?> pom = PomTreeParser.readModule(pomFile, new JavaxXMLStreamReaderFactory());
            
            final MavenProject project = pom.getProject();
            
            if (project.getCommon().getDependencies() != null) {
    
                for (MavenDependency otherDependency : project.getCommon().getDependencies()) {
                    
                    if ("provided".equals(otherDependency.getScope())) {
                        
                        final File file = repositoryAccess.repositoryJarFile(
                                                                otherDependency.getModuleId(),
                                                                otherDependency.getClassifier());
                        
                        urls.add(makeJarUrl(file));
                    }
                }
            }
        }

        return urls;
    }
    
    private static URLClassLoader makeClassLoader(List<URL> urls) {
        
        final URLClassLoader classLoader = URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]));
        
        return classLoader;
    }

    private static URL makeJarUrl(File jarFile) {

        final URL url;

        try {
            url = new URL("jar:file:" + jarFile.getAbsolutePath() + "!/");
        } catch (MalformedURLException ex) {
            throw new IllegalStateException(ex);
        }

        return url;
    }
    
    @Override
	public boolean isBuildSystemFor(File rootDirectory) {

		final File pomFile = new File(rootDirectory, "pom.xml");

		return pomFile.exists() && pomFile.isFile() && pomFile.canRead();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <MODULE_ID extends ModuleId, PROJECT, DEPENDENCY, REPOSITORY>
	BuildSystemRoot<MODULE_ID, PROJECT, DEPENDENCY, REPOSITORY> scan(
			File rootDirectory,
			BuildSystemScanListener listener) throws ScanException {

	    final EffectivePOMReader effectivePOMReader = new EffectivePOMReader(false);
	    
		final List<MavenXMLProject<Document>> mavenXMLProjects;

		final XMLReaderFactory<Document> xmlReaderFactory = effectivePOMReader.getXMLReaderFactory();
		
		try {
			
			final Consumer<MavenXMLProject<Document>> readListener =
					listener != null
						? mavenXMLProject -> {
						
							final MavenProject project = mavenXMLProject.getProject();
							
							listener.onModuleFound(
								EffectivePOMReader.getProjectModuleId(project),
								project.getRootDirectory(),
								project.getParentModuleId());
						}
						: null;
			
			mavenXMLProjects = MavenModulesReader.readModules(
					rootDirectory,
					xmlReaderFactory,
					readListener);
			
		} catch (XMLReaderException | IOException ex) {
			throw new ScanException("Failed to scan project", ex);
		}

        final List<DocumentModule<Document>> modules = mavenXMLProjects.stream()
                .map(p -> {
                    
                    final MavenModuleId moduleId = EffectivePOMReader.getProjectModuleId(p.getProject());
                    
                    return new DocumentModule<>(moduleId, p);
                })
                .collect(Collectors.toUnmodifiableList());

        final List<MavenProject> mavenProjects = effectivePOMReader.computeEffectiveProjects(modules);

		return (BuildSystemRoot)new MavenBuildRoot(
		        mavenProjects,
		        xmlReaderFactory,
		        pomProjectParser,
		        new MavenProjectsAccessImpl(),
		        pluginsAccess,
                pluginsEnvironmentProvider,
		        repositoryAccess,
		        effectivePOMReader);
	}

    @SuppressWarnings("unchecked")
    @Override
    public <CONTEXT extends TaskContext> BuildSpecifier<CONTEXT> getBuildSpecifier() {
        
        return (BuildSpecifier<CONTEXT>)new MavenBuildSpecifier();
    }
}
