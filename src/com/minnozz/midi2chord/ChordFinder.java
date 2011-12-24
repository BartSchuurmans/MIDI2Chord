package com.minnozz.midi2chord;

import java.util.ArrayList;

public class ChordFinder {
	public ArrayList<Chord> find(ArrayList<Note> notes) {
		ArrayList<Chord> options = new ArrayList<Chord>();

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
