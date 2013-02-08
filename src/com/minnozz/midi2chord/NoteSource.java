package com.minnozz.midi2chord;

import java.util.ArrayList;

public abstract class NoteSource extends Thread {
	private ArrayList<NoteListener> noteListeners;
	protected ArrayList<Note> currentNotes;

	public NoteSource() {
		noteListeners = new ArrayList<NoteListener>();
		currentNotes = new ArrayList<Note>();
	}

	public void run() {
		if(!connect()) {
			// XXX: Ignore
		}
	}

	protected abstract boolean connect();
	protected abstract void disconnect();
	public abstract ArrayList<SubNoteSource> getSubNoteSources();
	public abstract void setSubNoteSource(SubNoteSource subNoteSource);

	public void registerNoteListener(NoteListener noteListener) {
		synchronized(noteListeners) {
			if(!noteListeners.contains(noteListener)) {
				noteListeners.add(noteListener);
				sendUpdate(noteListener, currentNotes);
			}
		}
	}

	protected void broadcastUpdate(ArrayList<Note> notes) {
		synchronized(noteListeners) {
			System.out.println("Broadcasting update: "+ Note.concat(notes));

			for(NoteListener noteListener : noteListeners) {
				sendUpdate(noteListener, notes);
			}
			currentNotes = notes;
		}
	}

	private void sendUpdate(NoteListener noteListener, ArrayList<Note> notes) {
		ArrayList<Note> copy = (ArrayList<Note>)notes.clone();
		noteListener.onUpdate(copy);
	}
}
