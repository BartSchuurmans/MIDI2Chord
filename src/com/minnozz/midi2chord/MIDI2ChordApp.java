package com.minnozz.midi2chord;

import java.util.ArrayList;
import java.util.Date;

public class MIDI2ChordApp implements Runnable {
	private static enum NoteSourceType { MIDI };
	private static enum ChordDisplayerType { CLI, GUI };

	private NoteSourceType noteSourceType;
	private ChordDisplayerType chordDisplayerType;

	private NoteSource noteSource;
	private ChordFinder chordFinder;
	private ChordDisplayer chordDisplayer;

	public MIDI2ChordApp(NoteSourceType noteSourceType, ChordDisplayerType chordDisplayerType) {
		this.noteSourceType = noteSourceType;
		this.chordDisplayerType = chordDisplayerType;
	}

	private void createNoteSource() {
		switch(noteSourceType) {
			case MIDI:
				noteSource = new MIDINoteSource();
				break;
		}
	}

	private void createChordFinder() {
		chordFinder = new ChordFinder();
	}

	private void createChordDisplayer() {
		switch(chordDisplayerType) {
			case CLI:
				chordDisplayer = new CLIChordDisplayer(this);
				break;
			case GUI:
				chordDisplayer = new GUIChordDisplayer(this);
				break;
		}
	}

	private void createNoteListener() {
		noteSource.registerNoteListener(new NoteListener() {
			public void onUpdate(ArrayList<Note> notes) {
				ArrayList<Chord> options = chordFinder.find(notes);
				chordDisplayer.display(options, notes);
			}
		});
	}

	public void run() {
		createNoteSource();
		createChordFinder();
		createChordDisplayer();
		createNoteListener();

		chordDisplayer.run();
		noteSource.run();
	}

	public ArrayList<SubNoteSource> getSubNoteSources() {
		return noteSource.getSubNoteSources();
	}

	public void setSubNoteSource(SubNoteSource subNoteSource) {
		noteSource.setSubNoteSource(subNoteSource);
	}

	public static void main(String[] args) {
		// Defaults
		NoteSourceType noteSourceType = NoteSourceType.MIDI;
		ChordDisplayerType chordDisplayerType = ChordDisplayerType.GUI;

		// Parse command line arguments
		for(int i = 0; i < args.length; i++) {
			String arg = args[i];

			if(arg.equals("--cli")) {
				chordDisplayerType = ChordDisplayerType.CLI;
			} else {
				throw new IllegalArgumentException("Invalid argument: "+ arg);
			}
		}

		new MIDI2ChordApp(noteSourceType, chordDisplayerType).run();
	}
}
