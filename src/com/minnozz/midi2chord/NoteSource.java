package com.minnozz.midi2chord;

import java.util.ArrayList;

public abstract class NoteSource extends Thread {
	private ArrayList<NoteListener> noteListeners;
	protected boolean connected;
	protected ArrayList<Note> currentState;

	public NoteSource() {
		noteListeners = new ArrayList<NoteListener>();
		currentState = new ArrayList<Note>();
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
				sendUpdate(noteListener, currentState);
			}
		}
	}

	protected void broadcastUpdate(ArrayList<Note> notes) {
		synchronized(noteListeners) {
			for(NoteListener noteListener : noteListeners) {
				sendUpdate(noteListener, notes);
			}
			currentState = notes;
		}
	}

	private void sendUpdate(NoteListener noteListener, ArrayList<Note> notes) {
		ArrayList<Note> copy = (ArrayList<Note>)notes.clone();
		noteListener.onUpdate(copy);
	}
}
