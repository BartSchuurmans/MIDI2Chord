package com.minnozz.midi2chord;

import java.lang.StringBuilder;
import java.util.ArrayList;

public class ChordFinder {
	public ArrayList<Chord> find(ArrayList<Note> notes) {
		ArrayList<Chord> options = new ArrayList<Chord>();

		StringBuilder notesString = new StringBuilder();
		for(Note note : notes) {
			notesString.append(" "+ note.getNameWithOctive());
		}
		System.out.println("Notes: "+ notesString.toString());

		// TODO

		return options;
	}

	public Chord findMostLikely(ArrayList<Note> notes) {
		ArrayList<Chord> options = find(notes);

		if(options.isEmpty()) {
			return null;
		}

		return options.get(0);
	}
}
