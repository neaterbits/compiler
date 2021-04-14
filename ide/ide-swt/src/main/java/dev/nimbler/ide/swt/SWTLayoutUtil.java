package dev.nimbler.ide.swt;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;

class SWTLayoutUtil {
	
	static FillLayout makeFillLayout(int type, int marginWidth, int marginHeight, int spacing) {
		
		final FillLayout fillLayout = new FillLayout(type);
		
		fillLayout.marginWidth = marginWidth;
		fillLayout.marginHeight = marginHeight;
		fillLayout.spacing = spacing;
		
		return fillLayout;
	}

	static RowLayout makeRowLayout(int type, int marginWidth, int marginHeight, int spacing) {
		
		final RowLayout rowLayout = new RowLayout(type);
		
		rowLayout.marginWidth = marginWidth;
		rowLayout.marginHeight = marginHeight;
		rowLayout.spacing = spacing;
		
		return rowLayout;
	}

	static GridLayout makeGridLayout(int columns, int marginWidth, int marginHeight, int spacing) {
		
		final GridLayout gridLayout = new GridLayout(columns, false);
		
		gridLayout.marginWidth = marginWidth;
		gridLayout.marginHeight = marginHeight;
		gridLayout.horizontalSpacing = spacing;
		gridLayout.verticalSpacing = spacing;
		
		return gridLayout;
	}
}
