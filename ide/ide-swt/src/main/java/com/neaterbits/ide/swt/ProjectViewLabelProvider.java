package com.neaterbits.ide.swt;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import com.neaterbits.build.types.resource.ModuleResource;
import com.neaterbits.build.types.resource.NamespaceResourcePath;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.build.types.resource.Resource;
import com.neaterbits.build.types.resource.ResourcePath;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.build.types.resource.SourceFolderResourcePath;

final class ProjectViewLabelProvider extends LabelProvider {

    private final Image projectImage = new Image(null, new Image(null, loadIcon("if_project_59298.png"))
			.getImageData().scaledTo(16, 16));
	private final Image sourceFolderImage = new Image(null, loadIcon("if_Noun_Project_100Icon_10px_grid-26_296917.png"));
	private final Image namespaceImage = new Image(null, loadIcon("if_icon-139-package_314837.png"));
	private final Image sourceFileImage = new Image(null, loadIcon("if_icon-78-document-file-java_315699.png"));

	private static ImageData loadIcon(String name) {
	    
	    final ImageData imageData;
	    
	    try (InputStream inputStream = ProjectViewLabelProvider.class.getResourceAsStream("/icons/" + name)) {
	        
	        if (inputStream == null) {
	            throw new IllegalStateException("No icon image file for " + name);
	        }
	        
	        imageData = new ImageData(inputStream);
	    } catch (IOException ex) {
	        throw new IllegalStateException(ex);
        }

	    return imageData;
	}
	
	@Override
	public void dispose() {
		projectImage.dispose();
		sourceFolderImage.dispose();
		namespaceImage.dispose();
		sourceFileImage.dispose();
	}

	@Override
	public String getText(Object element) {
		
		final String text;
		
		if (element instanceof ProjectModuleResourcePath) {
			
			final ProjectModuleResourcePath moduleResourcePath = (ProjectModuleResourcePath)element;
			
			final ModuleResource moduleResource = (ModuleResource)moduleResourcePath.getLast();
			
			text = moduleResource.getName();
		}
		else if (element instanceof ResourcePath) {
			
			final ResourcePath resourcePath = (ResourcePath)element;
			
			final Resource resource = resourcePath.getLast();
			
			text = resource.getName();
		}
		else {
			text = element.toString();
		}

		return text;
	}

	@Override
	public Image getImage(Object element) {
		
		final Image image;
		
		if (element instanceof ProjectModuleResourcePath) {
			image = projectImage;
		}
		else if (element instanceof SourceFolderResourcePath) {
			image = sourceFolderImage;
		}
		else if (element instanceof NamespaceResourcePath) {
			image = namespaceImage;
		}
		else if (element instanceof SourceFileResourcePath) {
			image = sourceFileImage;
		}
		else {
			image = null;
		}
		
		return image;
	}
}
