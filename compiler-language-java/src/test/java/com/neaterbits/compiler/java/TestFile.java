package com.neaterbits.compiler.java;

import java.util.Objects;

public final class TestFile {

	private final String name;
	private final String text;

	public TestFile(String name, String text) {

		Objects.requireNonNull(name);
		Objects.requireNonNull(text);
		
		this.name = name;
		this.text = text;
	}
	
	public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		TestFile other = (TestFile) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NameFileSpec [name=" + name + "]";
	}
}
