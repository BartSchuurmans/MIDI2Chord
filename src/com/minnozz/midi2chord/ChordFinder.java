package com.minnozz.midi2chord;

import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Collections;

public class ChordFinder {
	final private static HashMap<Semitones, String> TYPES = new HashMap<Semitones, String>() {
		{
			//								1/8				2/9				3/10	4/11			5/12			6/13			7/14		Name
			put(new Semitones(new boolean[]{true,	false,	false,	false,	true,	false,	false,	true,	false,	false,	false,	false}),	"");
			put(new Semitones(new boolean[]{true,	false,	false,	false,	true,	false,	false,	true,	false,	false,	true,	false}),	"7");
			put(new Semitones(new boolean[]{true,	false,	false,	true,	false,	false,	false,	true,	false,	false,	false,	false}),	"m");
			put(new Semitones(new boolean[]{true,	false,	false,	true,	false,	false,	false,	true,	false,	false,	true,	false}),	"m7");
			put(new Semitones(new boolean[]{true,	false,	false,	false,	true,	false,	false,	true,	false,	false,	false,	true}),		"maj7");
			put(new Semitones(new boolean[]{true,	false,	false,	false,	false,	true,	false,	true,	false,	false,	false,	false}),	"sus4");
			put(new Semitones(new boolean[]{true,	false,	false,	false,	false,	true,	false,	true,	false,	false,	true,	false}),	"7sus4");
			put(new Semitones(new boolean[]{true,	false,	false,	false,	true,	false,	false,	true,	false,	true,	false,	false}),	"6");
			put(new Semitones(new boolean[]{true,	false,	false,	true,	false,	false,	false,	true,	false,	true,	false,	false}),	"m6");
			put(new Semitones(new boolean[]{true,	false,	true,	false,	true,	false,	false,	true,	false,	false,	true,	false}),	"9");
			put(new Semitones(new boolean[]{true,	false,	true,	true,	false,	false,	false,	true,	false,	false,	true,	false}),	"m9");
			put(new Semitones(new boolean[]{true,	false,	true,	false,	true,	false,	false,	true,	false,	false,	false,	true}),		"maj9");
			put(new Semitones(new boolean[]{true,	false,	true,	false,	true,	false,	false,	true,	false,	true,	false,	false}),	"6/9");
			put(new Semitones(new boolean[]{true,	false,	false,	true,	true,	false,	false,	true,	false,	false,	true,	false}),	"7+9");
			put(new Semitones(new boolean[]{true,	true,	false,	false,	true,	false,	false,	true,	false,	false,	true,	false}),	"7-9");
			put(new Semitones(new boolean[]{true,	false,	false,	false,	true,	false,	false,	false,	true,	false,	false,	false}),	"aug");
			put(new Semitones(new boolean[]{true,	false,	false,	false,	true,	false,	false,	false,	true,	false,	true,	false}),	"7+5");
			put(new Semitones(new boolean[]{true,	false,	false,	true,	false,	false,	true,	false,	false,	true,	false,	false}),	"dim");
			put(new Semitones(new boolean[]{true,	false,	false,	false,	true,	false,	true,	false,	false,	false,	false,	false}),	"-5");
			put(new Semitones(new boolean[]{true,	false,	false,	false,	true,	false,	true,	false,	false,	false,	true,	false}),	"7-5");
			put(new Semitones(new boolean[]{true,	false,	true,	false,	true,	false,	true,	false,	false,	false,	true,	false}),	"9-5");
			put(new Semitones(new boolean[]{true,	false,	true,	false,	true,	true,	false,	true,	false,	false,	true,	false}),	"11");
			put(new Semitones(new boolean[]{true,	false,	true,	false,	true,	true,	false,	true,	false,	true,	true,	false}),	"13");
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
