package com.minnozz.midi2chord;

import java.util.ArrayList;

public class CLIChordDisplayer extends ChordDisplayer {
	@Override
	public void display(ArrayList<Chord> options, ArrayList<Note> notes) {
		if(!options.isEmpty()) {
			StringBuilder optionsString = new StringBuilder();
			for(Chord chord : options) {
				optionsString.append(" "+ chord.getName());
			}
			System.out.println("Current chord:"+ optionsString.toString());
		}
	}
}
