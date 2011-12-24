package com.minnozz.midi2chord;

import java.util.ArrayList;

public interface NoteListener {
	public void onConnect();
	public void onDisconnect();
	public void onUpdate(ArrayList<Note> notes);
}
