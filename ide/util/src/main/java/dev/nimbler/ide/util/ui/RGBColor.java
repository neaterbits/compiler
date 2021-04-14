package dev.nimbler.ide.util.ui;

public class RGBColor {

	private final int r;
	private final int g;
	private final int b;
	
	public RGBColor(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public final int getR() {
		return r;
	}

	public final int getG() {
		return g;
	}

	public final int getB() {
		return b;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + b;
		result = prime * result + g;
		result = prime * result + r;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RGBColor other = (RGBColor) obj;
		if (b != other.b)
			return false;
		if (g != other.g)
			return false;
		if (r != other.r)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Color [r=" + r + ", g=" + g + ", b=" + b + "]";
	}
}
