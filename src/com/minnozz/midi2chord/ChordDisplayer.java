package com.minnozz.midi2chord;

import java.util.ArrayList;

public abstract class ChordDisplayer extends Thread {
	public void run() {
		// Override if needed
	}

	public abstract void display(ArrayList<Chord> options, ArrayList<Note> notes);
}
