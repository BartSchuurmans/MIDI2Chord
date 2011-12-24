package com.minnozz.midi2chord;

import java.lang.Math;

public class Note {
	final public static String[] NOTES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

	private int noteNumber;

	public Note(int noteNumber) {
		this.noteNumber = noteNumber;
	}

	public static String getName(int noteNumber) {
		return NOTES[noteNumber % 12];
	}

	public static int getOctive(int noteNumber) {
		return (int)(Math.floor(noteNumber / 12) - 1);
	}

	public int getNoteNumber() {
		return noteNumber;
	}

	public String getName() {
		return getName(noteNumber);
	}

	public int getOctive() {
		return getOctive(noteNumber);
	}

	public String getNameWithOctive() {
		return getName() + getOctive();
	}

	public boolean equals(Object obj) {
		if(obj instanceof Note) {
			return equals((Note)obj);
		}
		return false;
	}

	public boolean equals(Note note) {
		return (getNoteNumber() == note.getNoteNumber());
	}
}
