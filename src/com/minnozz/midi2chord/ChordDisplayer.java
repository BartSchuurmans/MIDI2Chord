package com.minnozz.midi2chord;

import java.util.ArrayList;

public abstract class ChordDisplayer extends Thread {
	protected MIDI2ChordApp app;

	public ChordDisplayer(MIDI2ChordApp app) {
		this.app = app;
	}

	public void run() {
		// Override if needed
	}

	public abstract void display(ArrayList<Chord> options, ArrayList<Note> notes);
}
