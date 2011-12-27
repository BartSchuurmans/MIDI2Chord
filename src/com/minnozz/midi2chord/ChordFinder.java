package com.minnozz.midi2chord;

import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.Comparator;

public class ChordFinder {
	final private static LinkedHashMap<Semitones, String> TYPES;
	static {
		TYPES = new LinkedHashMap<Semitones, String>();
		//										1/8				2/9				3/10	4/11			5/12			6/13			7/14		Name
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	false,	true,	false,	false,	true,	false,	false,	false,	false}),	"");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	false,	true,	false,	false,	true,	false,	false,	true,	false}),	"7");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	true,	false,	false,	false,	true,	false,	false,	false,	false}),	"m");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	true,	false,	false,	false,	true,	false,	false,	true,	false}),	"m7");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	false,	true,	false,	false,	true,	false,	false,	false,	true}),		"maj7");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	false,	false,	true,	false,	true,	false,	false,	false,	false}),	"sus4");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	false,	false,	true,	false,	true,	false,	false,	true,	false}),	"7sus4");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	false,	true,	false,	false,	true,	false,	true,	false,	false}),	"6");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	true,	false,	false,	false,	true,	false,	true,	false,	false}),	"m6");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	true,	false,	true,	false,	false,	true,	false,	false,	true,	false}),	"9");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	true,	true,	false,	false,	false,	true,	false,	false,	true,	false}),	"m9");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	true,	false,	true,	false,	false,	true,	false,	false,	false,	true}),		"maj9");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	true,	false,	true,	false,	false,	true,	false,	true,	false,	false}),	"6/9");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	true,	true,	false,	false,	true,	false,	false,	true,	false}),	"7+9");
		TYPES.put(new Semitones(new boolean[]{	true,	true,	false,	false,	true,	false,	false,	true,	false,	false,	true,	false}),	"7-9");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	false,	true,	false,	false,	false,	true,	false,	false,	false}),	"aug");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	false,	true,	false,	false,	false,	true,	false,	true,	false}),	"7+5");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	true,	false,	false,	true,	false,	false,	true,	false,	false}),	"dim");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	false,	true,	false,	true,	false,	false,	false,	false,	false}),	"-5");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	false,	true,	false,	true,	false,	false,	false,	true,	false}),	"7-5");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	true,	false,	true,	false,	true,	false,	false,	false,	true,	false}),	"9-5");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	true,	false,	true,	true,	false,	true,	false,	false,	true,	false}),	"11");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	true,	false,	true,	true,	false,	true,	false,	true,	true,	false}),	"13");
	};

	final private static ArrayList<String> TYPE_ORDER;
	static {
		TYPE_ORDER = new ArrayList<String>(TYPES.values());
	};

	public ArrayList<Chord> find(ArrayList<Note> notes) {
		ArrayList<Chord> options = new ArrayList<Chord>();

		// Avoid using the same note in different octives twice
		ArrayList<String> used = new ArrayList<String>();

		// Try all notes as fundamental
		for(int i = 0; i < notes.size(); i++) {
			Note fundamental = notes.get(i);

			if(used.contains(fundamental.getName())) {
				continue;
			}
			used.add(fundamental.getName());

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

		// Sort options by likelihood (defined by the order in the TYPES list)
		Collections.sort(options, new Comparator<Chord>() {
			public int compare(Chord c1, Chord c2) {
				return (TYPE_ORDER.indexOf(c1.getType()) - TYPE_ORDER.indexOf(c2.getType()));
			}
		});

		return options;
	}


	private String getType(Semitones semitones) {
		for(Semitones type : TYPES.keySet()) {
			if(type.equals(semitones)) {
				return TYPES.get(type);
			}
		}
		return null;
	}
}
