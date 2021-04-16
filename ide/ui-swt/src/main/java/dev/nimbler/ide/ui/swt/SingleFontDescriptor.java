package dev.nimbler.ide.ui.swt;

import org.eclipse.jface.resource.DeviceResourceException;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

final class SingleFontDescriptor extends FontDescriptor {

	private final FontData fontData;
	
	SingleFontDescriptor(String name, int height, int style) {
		this.fontData = new FontData(name, height, style);
	}

	@Override
	public Font createFont(Device device) throws DeviceResourceException {
		return new Font(device, fontData);
	}

	@Override
	public void destroyFont(Font previouslyCreatedFont) {
		previouslyCreatedFont.dispose();
	}
}
