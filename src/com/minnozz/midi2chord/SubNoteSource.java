package com.minnozz.midi2chord;

public class SubNoteSource {
	private Object identifier;
	private String displayName;

	public SubNoteSource(Object identifier, String displayName) {
		this.identifier = identifier;
		this.displayName = displayName;
	}

	public Object getIdentifier() {
		return identifier;
	}

	public String getDisplayName() {
		return displayName;
	}
}
