package com.minnozz.midi2chord;

import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Collections;

public class ChordFinder {
	final private static HashMap<Semitones, String> TYPES = new HashMap<Semitones, String>() {
		{
			//								1				2				3		4				5				6				7			Name
			put(new Semitones(new boolean[]{true,	false,	false,	false,	true,	false,	false,	true,	false,	false,	false,	false}),	"");
			put(new Semitones(new boolean[]{true,	false,	false,	false,	true,	false,	false,	true,	false,	false,	true,	false}),	"7");
			put(new Semitones(new boolean[]{true,	false,	false,	true,	false,	false,	false,	true,	false,	false,	false,	false}),	"m");
			put(new Semitones(new boolean[]{true,	false,	false,	true,	false,	false,	false,	true,	false,	false,	true,	false}),	"m7");
			put(new Semitones(new boolean[]{true,	false,	false,	false,	true,	false,	false,	true,	false,	false,	false,	true}),		"maj7");
			put(new Semitones(new boolean[]{true,	false,	false,	false,	false,	true,	false,	true,	false,	false,	false,	false}),	"sus4");
			put(new Semitones(new boolean[]{true,	false,	false,	false,	false,	true,	false,	true,	false,	false,	true,	false}),	"7sus4");
			put(new Semitones(new boolean[]{true,	false,	false,	false,	true,	false,	false,	true,	false,	true,	false,	false}),	"6");
			put(new Semitones(new boolean[]{true,	false,	false,	true,	false,	false,	false,	true,	false,	true,	false,	false}),	"m6");
		}
	};

	public ArrayList<Chord> find(ArrayList<Note> notes) {
		ArrayList<Chord> options = new ArrayList<Chord>();

		/*
		StringBuilder notesString = new StringBuilder();
		for(Note note : notes) {
			notesString.append(" "+ note.getNameWithOctive());
		}
		System.out.println("Notes: "+ notesString.toString());
		*/

		// Try all notes as fundamental
		for(int i = 0; i < notes.size(); i++) {
			Note fundamental = notes.get(i);

			Semitones semitones = new Semitones();
			semitones.setPresent(0);

			for(Note note : notes) {
				if(note.equals(fundamental)) {
					continue;
				}

				int semitonesAbove = note.getSemitonesAbove(fundamental);
				semitones.setPresent(semitonesAbove);
			}

			String type = getType(semitones);
			if(type != null) {
				Chord chord = new Chord(fundamental.getName(), type, notes);
				if(!options.contains(chord)) {
					options.add(chord);
				}
			}
		}

		return options;
	}

	public Chord findMostLikely(ArrayList<Note> notes) {
		ArrayList<Chord> options = find(notes);

		if(options.isEmpty()) {
			return null;
		}

		// TODO: Sort by likelihood

		return options.get(0);
	}

	private String getType(Semitones semitones) {
		// XXX Broken
		for(Semitones type : TYPES.keySet()) {
			if(type.equals(semitones)) {
				return TYPES.get(type);
			}
		}
		return null;
	}
}
