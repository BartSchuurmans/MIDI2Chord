package com.minnozz.midi2chord;

import java.util.ArrayList;
import java.util.Date;

public class MIDI2ChordApp implements Runnable {
	private NoteSource source;
	private ChordFinder chordFinder;
	private ChordDisplayer displayer;

	public MIDI2ChordApp(NoteSource source, ChordDisplayer displayer) {
		this.source = source;
		this.displayer = displayer;
	}

	private static NoteSource createNoteSource(String type) {
		if(type.equals("midi")) {
			return new MIDINoteSource();
		}
		throw new IllegalArgumentException();
	}

	private static ChordDisplayer createChordDisplayer(String type) {
		if(type.equals("cli")) {
			return new CLIChordDisplayer();
		}
		throw new IllegalArgumentException();
	}

	public void run() {
		chordFinder = new ChordFinder();

		source.registerNoteListener(new NoteListener() {
			public void onConnect() {
				System.out.println("Note listener connected");
			}

			public void onDisconnect() {
				System.out.println("Note listener disconnected");
			}

			public void onUpdate(ArrayList<Note> notes) {
				Chord chord = chordFinder.findMostLikely(notes);
				displayer.display(chord);
			}
		});

		source.run();
	}

	public static void main(String[] args) {
		if(args.length != 2) {
			usage();
		}

		NoteSource source;
		ChordDisplayer displayer;
		try {
			source = createNoteSource(args[0]);
			displayer = createChordDisplayer(args[1]);

			new MIDI2ChordApp(source, displayer).run();
		} catch(IllegalArgumentException e) {
			System.out.println("Error: "+ e.getMessage());
			usage();
		}
	}

	private static void usage() {
		System.out.println("Usage: MIDI2ChordApp <noteSourceType> <chordDisplayerType>");
		System.exit(1);
	}
}
