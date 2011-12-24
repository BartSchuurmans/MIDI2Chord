package com.minnozz.midi2chord;

import java.util.ArrayList;
import javax.sound.midi.*;

public class MIDINoteSource extends NoteSource {
	private Transmitter transmitter;
	private ArrayList<Note> currentNotes;

	@Override
	protected boolean _connect() {
		System.out.println("Connecting MIDI note source");

		try {
			transmitter = MidiSystem.getTransmitter();	// Default MIDI source
		} catch(MidiUnavailableException e) {
			System.out.println(e.getMessage());
			return false;
		}

		currentNotes = new ArrayList<Note>();
		transmitter.setReceiver(new MIDINoteSourceReceiver());

		return true;
	}

	@Override
	protected void _disconnect() {
		if(transmitter != null) {
			transmitter.close();
			transmitter = null;
		}
	}

	private class MIDINoteSourceReceiver implements Receiver {
		public void close() {
			// Ignore
		}

		public void send(MidiMessage m, long timeStamp) {
			if(m instanceof ShortMessage) {
				ShortMessage message = (ShortMessage)m;

				switch(message.getStatus()) {
					case ShortMessage.NOTE_ON:
						onNoteOn(message.getData1());
						break;
					case ShortMessage.NOTE_OFF:
						onNoteOff(message.getData1());
						break;
				}
			}
		}
	}

	private void onNoteOn(int noteNumber) {
		Note note = new Note(noteNumber);

		synchronized(currentNotes) {
			if(!currentNotes.contains(note)) {
				currentNotes.add(note);
				broadcastUpdate(currentNotes);
			}
		}
	}

	private void onNoteOff(int noteNumber) {
		Note note = new Note(noteNumber);

		synchronized(currentNotes) {
			if(currentNotes.contains(note)) {
				currentNotes.remove(note);
				broadcastUpdate(currentNotes);
			}
		}
	}
}
