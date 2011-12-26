package com.minnozz.midi2chord;

import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;

public class GUIChordDisplayer extends ChordDisplayer {
	private JLabel bigChord;

	@Override
	public void run() {
		JFrame frame = new JFrame("MIDI2Chord");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container contentPane = frame.getContentPane();
		GroupLayout layout = new GroupLayout(contentPane);
		layout.setAutoCreateContainerGaps(true);
		contentPane.setLayout(layout);

		// Create display areas
		bigChord = new JLabel("-", SwingConstants.CENTER);
		bigChord.setSize(200, 200);

		layout.setHorizontalGroup(
			layout.createParallelGroup()
			.addComponent(bigChord)
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
			.addComponent(bigChord)
		);

		frame.pack();

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((screen.width / 2) - (frame.getWidth() / 2), (screen.height / 2) - (frame.getHeight() / 2));

		frame.setVisible(true);
	}

	@Override
	public void display(ArrayList<Chord> options, ArrayList<Note> notes) {
		if(options.isEmpty()) {
			bigChord.setText("-");
		} else {
			for(Chord chord : options) {
				bigChord.setText(chord.getName());
			}
		}
	}
}
