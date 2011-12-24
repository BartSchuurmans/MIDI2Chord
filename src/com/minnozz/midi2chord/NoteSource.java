package com.minnozz.midi2chord;

import java.util.ArrayList;

public abstract class NoteSource {
	private ArrayList<NoteListener> noteListeners;
	protected boolean connected;
	protected ArrayList<Note> currentState;

	public NoteSource() {
		noteListeners = new ArrayList<NoteListener>();
		connected = false;
		currentState = new ArrayList<Note>();
	}

	public boolean connect() {
		if(connected) {
			return true;
		}

		if(!_connect()) {
			return false;
		}

		synchronized(noteListeners) {
			for(NoteListener noteListener : noteListeners) {
				noteListener.onConnect();
				sendUpdate(noteListener, currentState);
			}
			connected = true;
		}
		return true;
	}

	protected abstract boolean _connect();

	public void disconnect() {
		if(!connected) {
			return;
		}

		_disconnect();

		synchronized(noteListeners) {
			for(NoteListener noteListener : noteListeners) {
				noteListener.onDisconnect();
			}
			connected = false;
		}
	}

	protected abstract void _disconnect();

	public void registerNoteListener(NoteListener noteListener) {
		synchronized(noteListeners) {
			if(!noteListeners.contains(noteListener)) {
				noteListeners.add(noteListener);
				if(connected) {
					noteListener.onConnect();
					sendUpdate(noteListener, currentState);
				}
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
		noteListener.onUpdate(notes);
	}
}
