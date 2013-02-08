package com.minnozz.midi2chord;

import java.lang.Math;
import java.lang.Comparable;

public class Note implements Comparable<Note> {
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

	public int getSemitonesAbove(Note fundamental) {
		int semitonesAbove = (getNoteNumber() - fundamental.getNoteNumber()) % 12;
		while(semitonesAbove < 0) {
			semitonesAbove += 12;
		}
		return semitonesAbove;
	}

	public boolean equals(Object obj) {
		if(obj instanceof Note) {
			return equals((Note)obj);
		}
		return false;
	}

	private boolean equals(Note other) {
		return (getNoteNumber() == other.getNoteNumber());
	}

	public int compareTo(Note other) {
		if(equals(other)) {
			return 0;
		}
		return noteNumber > other.getNoteNumber() ? 1 : -1;
	}

	public static String concat(java.util.ArrayList<Note> notes) {
		java.lang.StringBuilder sb = new java.lang.StringBuilder();
		for(Note note : notes) {
			if(sb.length() == 0) {
				sb.append(note.getNameWithOctive());
			} else {
				sb.append(" "+ note.getNameWithOctive());
			}
		}
		return sb.toString();
	}
}
