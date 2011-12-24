package com.minnozz.midi2chord;

import java.util.ArrayList;

public class Chord {
	private String name;
	private ArrayList<Note> notes;

	public Chord(String name, ArrayList<Note> notes) {
		this.name = name;
		this.notes = notes;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Note> getNotes() {
		return notes;
	}
}
