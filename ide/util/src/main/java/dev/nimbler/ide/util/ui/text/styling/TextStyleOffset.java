package dev.nimbler.ide.util.ui.text.styling;


public final class TextStyleOffset {

	public static final TextColor SELECTED_BG_COLOR = new TextColor(0x60, 0x60, 0xD0);
	
	private final long start;
	private final long length;
	private final TextColor color;
	private final TextColor bgColor;
	private final TextStyles styles;
	
	public TextStyleOffset(long start, long length, TextColor color) {
		this(start, length, color, null, null);
	}

	public TextStyleOffset(long start, long length, TextColor color, TextColor bgColor, TextStyles styles) {

		this.start = start;
		this.length = length;
		this.color = color;
		this.bgColor = bgColor;
		this.styles = styles;
	}

	public long getStart() {
		return start;
	}

	public long getLength() {
		return length;
	}

	public TextColor getColor() {
		return color;
	}
	
	public TextColor getBgColor() {
		return bgColor;
	}

	public TextStyles getStyles() {
		return styles;
	}

	@Override
	public String toString() {
		return "TextStyleOffset [start=" + start + ", length=" + length + ", color=" + color + "]";
	}
}
