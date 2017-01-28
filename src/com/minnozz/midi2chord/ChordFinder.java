package com.minnozz.midi2chord;

import java.lang.StringBuilder;
import java.util.Arrays;
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
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	false,	false,	false,	false,	true,	false,	false,	false,	false}),	"5");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	false,	true,	false,	false,	true,	false,	false,	true,	false}),	"7");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	true,	false,	false,	false,	true,	false,	false,	false,	false}),	"m");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	true,	false,	false,	false,	true,	false,	false,	true,	false}),	"m7");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	false,	true,	false,	false,	true,	false,	false,	false,	true}),		"maj7");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	true,	false,	false,	false,	true,	false,	false,	false,	true}),		"m(maj7)");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	false,	false,	true,	false,	true,	false,	false,	false,	false}),	"sus4");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	false,	false,	true,	false,	true,	false,	false,	true,	false}),	"7sus4");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	true,	false,	false,	false,	false,	true,	false,	false,	false,	false}),	"sus2");
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
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	true,	false,	false,	true,	false,	false,	false,	false,	false}),	"dim");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	true,	false,	false,	true,	false,	false,	true,	false,	false}),	"dim7");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	false,	true,	false,	true,	false,	false,	false,	false,	false}),	"-5");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	false,	false,	true,	false,	true,	false,	false,	false,	true,	false}),	"7-5");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	true,	false,	true,	false,	true,	false,	false,	false,	true,	false}),	"9-5");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	true,	false,	true,	true,	false,	true,	false,	false,	true,	false}),	"11");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	true,	false,	true,	true,	false,	true,	false,	true,	true,	false}),	"13");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	true,	false,	true,	false,	false,	true,	false,	false,	false,	false}),	"add9");
		TYPES.put(new Semitones(new boolean[]{	true,	false,	true,	true,	false,	false,	false,	true,	false,	false,	false,	false}),	"madd9");
	};

	final private static ArrayList<String> TYPE_ORDER;
	static {
		TYPE_ORDER = new ArrayList<String>(TYPES.values());
	};

	public ArrayList<Chord> find(ArrayList<Note> notes) {
		ArrayList<Chord> options = new ArrayList<Chord>();

		// Sort notes to avoid weird inconsistencies in chord order
		Collections.sort(notes);

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

		// Sort options by likelihood (defined by the order in the TYPES list)
		Collections.sort(options, new Comparator<Chord>() {
			public int compare(Chord c1, Chord c2) {
				int diff;
				// Chords that have the fundamental (are no inversion) as their root note are more likely
				if((diff = ((c1.isInversion() ? 1 : 0) - (c2.isInversion() ? 1 : 0))) != 0) {
					System.out.println(c1.getName() +" differs from "+ c2.getName() +" by inversion ("+ diff +")");
					return diff;
				}
				// Chords that are higher (lower index) in the TYPE_ORDER are more likely
				if((diff = (TYPE_ORDER.indexOf(c1.getType()) - TYPE_ORDER.indexOf(c2.getType()))) != 0) {
					System.out.println(c1.getName() +" differs from "+ c2.getName() +" by type order ("+ diff +")");
					return diff;
				}
				// Sort on fundamental to ensure a deterministic sort order
				if((diff = (Arrays.asList(Note.NOTES).indexOf(c2.getFundamental()) - Arrays.asList(Note.NOTES).indexOf(c1.getFundamental()))) != 0) {
					System.out.println(c1.getName() +" differs from "+ c2.getName() +" by fundamental ("+ diff +")");
					return diff;
				}
				System.out.println(c1.getName() +" and "+ c2.getName() +" are equal");
				return 0;
			}
		});

		java.lang.StringBuilder sb = new java.lang.StringBuilder("Sorted options:");
		for(Chord chord : options) {
			sb.append(" "+ chord.getName());
		}
		System.out.println(sb.toString());

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
