package com.minnozz.midi2chord;

import java.util.ArrayList;
import javax.sound.midi.*;

public class MIDINoteSource extends NoteSource {
	private Transmitter transmitter;
	private MidiDevice device;
	private ArrayList<Note> currentNotes;

	@Override
	public ArrayList<SubNoteSource> getSubNoteSources() {
		ArrayList<SubNoteSource> subNoteSources = new ArrayList<SubNoteSource>();

		try {
			for(MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
				MidiDevice device = MidiSystem.getMidiDevice(info);
				try {
					transmitter = device.getTransmitter();
				} catch(MidiUnavailableException e) {
					// Device has no transmitter
					continue;
				}

				subNoteSources.add(new SubNoteSource(device, info.getName() +" ("+ info.getDescription() +", "+ info.getVendor() +")"));
			}
		} catch(MidiUnavailableException e) {
			// No MIDI devices available
		}

		return subNoteSources;
	}

	@Override
	public void setSubNoteSource(SubNoteSource subNoteSource) {
		if(transmitter != null) {
			transmitter.close();
		}
		if(device != null) {
			device.close();
		}

		if(subNoteSource.getIdentifier() == null) {
			// No device
			return;
		}

		try {
			device = (MidiDevice)subNoteSource.getIdentifier();
			transmitter = device.getTransmitter();
			device.open();
			transmitter.setReceiver(new MIDINoteSourceReceiver());

			System.out.println("Changed sub note source to "+ subNoteSource.getDisplayName());
		} catch(MidiUnavailableException e) {
			// XXX: Ignore

			System.out.println("Error changing sub note source to "+ subNoteSource.getDisplayName() +": "+ e.getMessage());
		}
	}

	@Override
	protected boolean connect() {
		currentNotes = new ArrayList<Note>();
		return true;
	}

	@Override
	protected void disconnect() {
		if(transmitter != null) {
			transmitter.close();
			transmitter = null;
		}
		if(device != null) {
			device.close();
			device = null;
		}
	}

	private class MIDINoteSourceReceiver implements Receiver {
		public void close() {
			System.out.println("Closing MIDI note receiver");
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
