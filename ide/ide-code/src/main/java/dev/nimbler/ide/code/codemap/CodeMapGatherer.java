package dev.nimbler.ide.code.codemap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.jutils.Strings;
import org.jutils.concurrency.scheduling.AsyncExecutor;

import dev.nimbler.build.common.language.CompileableLanguage;
import dev.nimbler.build.model.runtimeenvironment.RuntimeEnvironment;
import dev.nimbler.build.types.TypeToDependencyFile;
import dev.nimbler.build.types.compile.FileCompilation;
import dev.nimbler.build.types.resource.LibraryResourcePath;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.build.types.resource.compile.CompiledModuleFileResourcePath;
import dev.nimbler.exe.vm.bytecode.loader.HashTypeMap;
import dev.nimbler.exe.vm.bytecode.loader.LoadClassHelper;
import dev.nimbler.exe.vm.bytecode.loader.LoadClassParameters;
import dev.nimbler.exe.vm.bytecode.loader.HashTypeMap.CreateType;
import dev.nimbler.exe.vm.bytecode.loader.HashTypeMap.LoadType;
import dev.nimbler.ide.common.model.codemap.CodeMapModel;
import dev.nimbler.ide.common.model.codemap.TypeSuggestion;
import dev.nimbler.ide.common.model.codemap.TypeSuggestions;
import dev.nimbler.ide.common.model.common.InformationGatherer;
import dev.nimbler.language.bytecode.common.ClassByteCodeWithTypeSource;
import dev.nimbler.language.bytecode.common.ClassBytecode;
import dev.nimbler.language.bytecode.common.ClassFileException;
import dev.nimbler.language.codemap.CodeMap.TypeResult;
import dev.nimbler.language.codemap.compiler.CompilerCodeMap;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.typesources.DependencyFile;
import dev.nimbler.language.common.typesources.TypeSource;
import dev.nimbler.language.common.typesources.TypeSources;
import dev.nimbler.language.common.typesources.libs.ClassLibs;

public final class CodeMapGatherer extends InformationGatherer implements CodeMapModel {

	private final CompileableLanguage language;
	private final Projects projects;
	
	private final TypeToDependencyFile moduleAndLibraryToDependencyFile;
	
	private final Map<RuntimeEnvironment, TypeToDependencyFile> systemTypeToDependencyFileByRuntimeEnvironment;
	
	private final HashTypeMap<ClassInfo> typeMap;
	private final CompilerCodeMap codeMap;

	private final List<TypeSuggestionFinder> typeSuggestionFinders;

	private final CreateType<ClassInfo> createType;
	
	private boolean codeScanComplete;
	
	public CodeMapGatherer(
			AsyncExecutor asyncExecutor,
			CompileableLanguage language,
			Projects projects,
			CompilerCodeMap codeMap) {

		Objects.requireNonNull(asyncExecutor);
		Objects.requireNonNull(language);
		Objects.requireNonNull(projects);

		this.language = language;
		this.projects = projects;
		
		this.moduleAndLibraryToDependencyFile = new TypeToDependencyFile();
		this.systemTypeToDependencyFileByRuntimeEnvironment = new HashMap<>();
		
		this.typeMap = new HashTypeMap<>(ClassInfo::getTypeNo, ClassInfo::getTypeSource);
		this.codeMap = codeMap;
		
		final TypeSuggestionFinder typeMapSuggestionFinder = new TypeSuggestionFinder() {
			@Override
			boolean findSuggestions(TypeNameMatcher matcher, Map<TypeName, TypeSuggestion> dst) {
				
				typeMap.forEachKeyValueSynchronized((typeName, classInfo) -> {
					
				});
				
				return codeScanComplete;
			}
			
			@Override
			boolean hasSourceCode() {
				// Might have source code if is project type
				return true;
			}

			@Override
			boolean canRetrieveTypeVariant() {
				return true;
			}

			@Override
			boolean hasType(TypeName typeName, TypeSources typeSources) {
				return typeMap.hasType(typeName, typeSources);
			}
		};
		
		final TypeSuggestionFinder dependencyFileSuggestionFinder = new TypeSuggestionFinder() {
			
			@Override
			boolean findSuggestions(TypeNameMatcher matcher, Map<TypeName, TypeSuggestion> dst) {

				moduleAndLibraryToDependencyFile.forEachKeyValue((typeName, file) -> {
					
					final String namespaceString = language.getNamespaceString(typeName);
					
					final TypeName match = matcher.matches(typeName, null, namespaceString, typeName.getName());
					
					if (match != null) {
						dst.put(typeName, new TypeSuggestionImpl(
								null,
								namespaceString,
								typeName.getName(),
								language.getBinaryName(typeName),
								null));
					}
					
				});
				
				return false;
			}
			
			@Override
			boolean hasSourceCode() {
				return false;
			}

			@Override
			boolean canRetrieveTypeVariant() {
				return false;
			}

			@Override
			boolean hasType(TypeName typeName, TypeSources typeSources) {
				final DependencyFile dependencyFile
				    = moduleAndLibraryToDependencyFile.getDependencyFileFor(typeName);
				
				return dependencyFile != null
						? typeSources.isSet(dependencyFile.getTypeSource())
						: false;
			}
		};
		
		this.typeSuggestionFinders = Arrays.asList(
				typeMapSuggestionFinder,
				dependencyFileSuggestionFinder,
				new SourceFileScannerTypeSuggestionFinder(projects, language));
		
		this.createType = (typeName, typeSource, typeNo, classByteCode) -> new ClassInfo(
				typeNo,
				typeName,
				typeSource,
				language.getNamespaceString(typeName),
				language.getBinaryName(typeName),
				null,
				classByteCode);
	}
	
	@Override
	public TypeSuggestions findSuggestions(String searchText, boolean onlyTypesWithSourceCode) {

		final String searchTextLowerCase = searchText.toLowerCase();
		
		final TypeNameMatcher typeNameMatcher = (typeNameIfKnown, sourceFileResourcePath, namespace, name) -> {
		
			final TypeName result;
			
			if (onlyTypesWithSourceCode && sourceFileResourcePath == null) {
				result = null;
			}
			else {
				result = Strings.startsWithToFindLowerCase(name, searchTextLowerCase)
					? typeNameIfKnown != null
							? typeNameIfKnown
							: getTypeName(sourceFileResourcePath, namespace, name)
					: null;
			}
			
			return result;
		};
		
		// Map instead of List for distinct type suggestions
		final Map<TypeName, TypeSuggestion> suggestions = new HashMap<>(10000);
		
		boolean completeResult = false;
		
		for (TypeSuggestionFinder typeSuggestionFinder : typeSuggestionFinders) {

			if (onlyTypesWithSourceCode && !typeSuggestionFinder.hasSourceCode()) {
				continue;
			}
			
			completeResult = typeSuggestionFinder.findSuggestions(typeNameMatcher, suggestions);
			
			if (completeResult) {
				break;
			}
		}
		
		final List<TypeSuggestion> suggestionsList = new ArrayList<>(suggestions.values());
		
		Collections.sort(suggestionsList, (t1, t2) -> t1.getName().compareTo(t2.getName()));

		return new TypeSuggestions(suggestionsList, completeResult);
	}
	
	@Override
	public boolean hasType(TypeName typeName, TypeSources typeSources) {
		
		for (TypeSuggestionFinder typeSuggestionFinder : typeSuggestionFinders) {
			if (typeSuggestionFinder.hasType(typeName, typeSources)) {
				return true;
			}
		}
		
		return false;
	}

	private TypeName getTypeName(SourceFileResourcePath sourceFileResourcePath, String namespace, String name) {

		final TypeName typeName;
		
		if (sourceFileResourcePath != null) {
			typeName = language.getTypeName(sourceFileResourcePath);
		}
		else {
			typeName = language.getTypeName(namespace, name);
		}
		
		return typeName;
	}

	public void addCompiledModuleFileTypes(CompiledModuleFileResourcePath module, Set<TypeName> types) {

		moduleAndLibraryToDependencyFile.mergeModuleDependencyTypes(new DependencyFile(module.getFile(), TypeSource.LIBRARY), types);
	}
	
	public void addLibraryFileTypes(LibraryResourcePath module, Set<TypeName> types) {

	    moduleAndLibraryToDependencyFile.mergeLibraryDependencyTypesIfNotPresent(new DependencyFile(module.getFile(), TypeSource.LIBRARY), types);
	}

	public boolean hasSystemLibraryFileTypes(ProjectModuleResourcePath from) {
	    
        Objects.requireNonNull(from);

        final RuntimeEnvironment runtimeEnvironment = projects.getRuntimeEnvironment(from);

        return systemTypeToDependencyFileByRuntimeEnvironment.containsKey(runtimeEnvironment);
	}

	public void addSystemLibraryFileTypes(ProjectModuleResourcePath from, File libraryFile, Set<TypeName> types) {

	    Objects.requireNonNull(from);
	    Objects.requireNonNull(libraryFile);
	    Objects.requireNonNull(types);

	    final RuntimeEnvironment runtimeEnvironment = projects.getRuntimeEnvironment(from);
	    
	    TypeToDependencyFile typeToDependencyFile
	        = systemTypeToDependencyFileByRuntimeEnvironment.get(runtimeEnvironment);
	    
	    if (typeToDependencyFile == null) {
	        
	        typeToDependencyFile = new TypeToDependencyFile();

	        systemTypeToDependencyFileByRuntimeEnvironment.put(runtimeEnvironment, typeToDependencyFile);
	    }

		typeToDependencyFile.mergeLibraryDependencyTypesIfNotPresent(new DependencyFile(libraryFile, TypeSource.LIBRARY), types);
	}

	public void addSystemLibraryFile(ProjectModuleResourcePath from, DependencyFile file) {
	    
	    Objects.requireNonNull(from);
	    
	    final RuntimeEnvironment runtimeEnvironment = projects.getRuntimeEnvironment(from);
		
		final long start = System.currentTimeMillis();

		Set<TypeName> typeNames = null;
		
		try {
			typeNames = runtimeEnvironment.getTypesFromSystemLibraryFile(file);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		final ClassLibs systemLibraries = runtimeEnvironment.getSystemLibraries();
		
		if (typeNames != null) {
			for (TypeName typeName : typeNames) {
				try {
					final boolean addedType = loadClassAndBaseClassesAndAddToCodeMap(
					                                            from,
					                                            systemLibraries,
					                                            typeName);
					
					if (!addedType) {
						System.out.println("## already added " + typeName.toDebugString());
					}
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		System.out.println("Loading files took " + ((System.currentTimeMillis() - start) / 1000));
	}
	
	public CodeMapModel getModel() {
		return this;
	}
	
	public void addClassFile(
	        ProjectModuleResourcePath from,
	        FileCompilation fileCompilation) throws IOException, ClassFileException {
		
		Objects.requireNonNull(fileCompilation);
		
        Objects.requireNonNull(from);
        
        final RuntimeEnvironment runtimeEnvironment = projects.getRuntimeEnvironment(from);
		
		final TypeName typeName = language.getTypeName(fileCompilation.getSourcePath());
		
		final File classFile = fileCompilation.getCompiledFile();
		
		try (FileInputStream inputStream = new FileInputStream(classFile)) {

			final TypeSource typeSource = TypeSource.COMPILED_PROJECT_MODULE;
			
			loadAndAddToCodeMap(typeName, typeSource, type -> {
				ClassByteCodeWithTypeSource classBytecode = null;
				
				try {
					classBytecode = runtimeEnvironment
					                    .getBytecodeFormat()
					                    .loadClassBytecode(inputStream, typeSource);
				} catch (IOException | ClassFileException ex) {
					ex.printStackTrace();
				}
				catch (Exception ex) {
					System.out.print("## error while reading bytecode " + classFile.getPath());
					ex.printStackTrace();
				}
				
				return classBytecode;
			});
		}
		catch (IOException ex) {
			System.out.println("## error while reading " + classFile.getPath());
			ex.printStackTrace();
		}
	}

	// load classfile from local library
	void loadAndAddToCodeMap(
	        ProjectModuleResourcePath from,
	        TypeName typeName,
	        TypeSource typeSource) throws IOException, ClassFileException {
		
        Objects.requireNonNull(from);
        
        final RuntimeEnvironment runtimeEnvironment = projects.getRuntimeEnvironment(from);
	    
		loadAndAddToCodeMap(typeName, typeSource, type -> {
					
					ClassByteCodeWithTypeSource classBytecode = null;
					
					try {
						classBytecode = runtimeEnvironment
						                        .getBytecodeFormat()
						                        .loadClassBytecode(moduleAndLibraryToDependencyFile, type);
						
						if (classBytecode == null) {
						    
						    final TypeToDependencyFile systemTypeToDependencyFile
						        = systemTypeToDependencyFileByRuntimeEnvironment.get(runtimeEnvironment);
						    
						    if (systemTypeToDependencyFile != null) {

						        classBytecode = runtimeEnvironment
                                        .getBytecodeFormat()
                                        .loadClassBytecode(systemTypeToDependencyFile, type);
						    }
						}
					} catch (IOException | ClassFileException ex) {
						ex.printStackTrace();
					}
					
					return classBytecode;
				});

	}

	private boolean loadClassAndBaseClassesAndAddToCodeMap(
	        ProjectModuleResourcePath from,
	        ClassLibs classLibs,
	        TypeName typeName) throws IOException, ClassFileException {

	    Objects.requireNonNull(from);
        
        final RuntimeEnvironment runtimeEnvironment = projects.getRuntimeEnvironment(from);
		
		final LoadClassParameters<File, ClassInfo, Void> parameters = new LoadClassParameters<>(
				typeMap,
				codeMap,
				createType,
				null,
				type -> runtimeEnvironment.getBytecodeFormat().loadClassBytecode(classLibs, type));
		
		final ClassBytecode addedByteCode = LoadClassHelper.loadClassAndBaseTypesAndAddToCodeMap(typeName, new TypeResult(), parameters);
		
		return addedByteCode != null;
	}
	
	private void loadAndAddToCodeMap(TypeName typeName, TypeSource typeSource, LoadType loadType) throws IOException, ClassFileException {
		
		Objects.requireNonNull(typeName);
		
		final TypeResult typeResult = new TypeResult();
		
		final ClassBytecode bytecode = typeMap.addOrGetType(
				typeName,
				codeMap,
				false,
				typeResult,
				createType,
				loadType);
		
		// System.out.println("## added type " + language.getCompleteNameString(typeName) + " with typeNo " + typeResult.type);
		
		if (bytecode != null) {
			final int methodCount = bytecode.getMethodCount();
		
			synchronized (typeMap) {
				codeMap.setMethodCount(typeResult.type, methodCount);
			}
		}
	}
}
