package com.neaterbits.ide.util.ui.text.styling;

import java.util.Collection;

import com.neaterbits.ide.util.ui.text.Text;

public interface TextStylingModel {

	Collection<TextStyleOffset> getLineStyleOffsets(long startPos, long length, Text lineText);
	
}
