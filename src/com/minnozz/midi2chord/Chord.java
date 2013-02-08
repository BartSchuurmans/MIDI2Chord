package com.minnozz.midi2chord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Chord {
	private String fundamental;
	private String type;
	private String bass;
	private ArrayList<Note> notes;

	public Chord(String fundamental, String type, ArrayList<Note> notes) {
		this.fundamental = fundamental;
		this.type = type;
		this.notes = notes;

		Collections.sort(this.notes);

		bass = notes.get(0).getName();
	}

	public String getFundamental() {
		return fundamental;
	}

	public String getType() {
		return type;
	}

	public String getBass() {
		return bass;
	}

	public boolean isInversion() {
		return !fundamental.equals(bass);
	}

	public String getName() {
		if(isInversion()) {
			return fundamental + type +"/"+ bass;
		} else {
			return fundamental + type;
		}
	}

	public boolean equals(Object obj) {
		if(obj instanceof Chord) {
			return equals((Chord)obj);
		}
		return false;
	}

	private boolean equals(Chord other) {
		return (fundamental.equals(other.getFundamental())) && (type.equals(other.getType())) && (bass.equals(other.getBass()));
	}
}
