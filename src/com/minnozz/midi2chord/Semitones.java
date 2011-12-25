package com.minnozz.midi2chord;

public class Semitones {
	private boolean[] present;

	public Semitones() {
		this(new boolean[] {false, false, false, false, false, false, false, false, false, false, false, false});
	}

	public Semitones(boolean[] present) {
		this.present = present;
	}

	public void setPresent(int index) {
		present[index] = true;
	}

	public boolean isPresent(int index) {
		return present[index];
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Semitones) {
			return equals((Semitones)obj);
		}
		return false;
	}

	public boolean equals(Semitones semitones) {
		for(int i = 0; i < present.length; i++) {
			if(isPresent(i) != semitones.isPresent(i)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		for(int i = 0; i < present.length; i++) {
			sb.append(present[i] ? " true" : " false");
		}
		return sb.toString();
	}
}
