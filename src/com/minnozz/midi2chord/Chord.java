package com.minnozz.midi2chord;

import java.util.ArrayList;

public class Chord {
	private String fundamental;
	private String type;
	private ArrayList<Note> notes;

	public Chord(String fundamental, String type, ArrayList<Note> notes) {
		this.fundamental = fundamental;
		this.type = type;
		this.notes = notes;
	}

	public String getName() {
		return fundamental + type;
	}

	public String getType() {
		return type;
	}
}
