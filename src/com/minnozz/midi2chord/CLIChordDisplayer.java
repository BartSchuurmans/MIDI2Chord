package com.minnozz.midi2chord;

public class CLIChordDisplayer extends ChordDisplayer {
	@Override
	public void display(Chord chord) {
		if(chord != null) {
			System.out.println("Current chord: "+ chord.getName());
		}
	}
}
