package com.minnozz.midi2chord;

import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUIChordDisplayer extends ChordDisplayer {
	private JFrame frame;
	private JLabel bigChord;
	private JLabel alternative1;
	private JLabel alternative2;
	private JLabel alternative3;

	public GUIChordDisplayer(MIDI2ChordApp app) {
		super(app);

		createInterface();
	}

	private void createInterface() {
		frame = new JFrame("MIDI2Chord");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container contentPane = frame.getContentPane();
		GroupLayout layout = new GroupLayout(contentPane);
		contentPane.setLayout(layout);

		// Create fonts
		Font bigFont = new Font(Font.SANS_SERIF, Font.BOLD, 96);
		Font mediumFont = new Font(Font.SANS_SERIF, Font.BOLD, 32);

		// Create display areas
		bigChord = new JLabel("-");
		bigChord.setHorizontalAlignment(SwingConstants.CENTER);
		bigChord.setMinimumSize(new Dimension(450, 240));
		bigChord.setFont(bigFont);

		alternative1 = new JLabel("-");
		alternative1.setHorizontalAlignment(SwingConstants.CENTER);
		alternative1.setMinimumSize(new Dimension(150, 80));
		alternative1.setFont(mediumFont);

		alternative2 = new JLabel("-");
		alternative2.setHorizontalAlignment(SwingConstants.CENTER);
		alternative2.setMinimumSize(new Dimension(150, 80));
		alternative2.setFont(mediumFont);

		alternative3 = new JLabel("-");
		alternative3.setHorizontalAlignment(SwingConstants.CENTER);
		alternative3.setMinimumSize(new Dimension(150, 80));
		alternative3.setFont(mediumFont);

		JComboBox sourcePicker = new JComboBox(app.getSubNoteSources().toArray());
		sourcePicker.insertItemAt(new SubNoteSource(null, "Select a MIDI device..."), 0);
		sourcePicker.setSelectedIndex(0);
		sourcePicker.setEditable(false);
		sourcePicker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				SubNoteSource selectedSubNoteSource = (SubNoteSource)((JComboBox)event.getSource()).getSelectedItem();
				app.setSubNoteSource(selectedSubNoteSource);
			}
		});
		sourcePicker.setRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				return super.getListCellRendererComponent(list, ((SubNoteSource)value).getDisplayName(), index, isSelected, cellHasFocus);
			}
		});

		layout.setHorizontalGroup(
			layout.createParallelGroup()
				.addComponent(sourcePicker)
				.addGroup(layout.createSequentialGroup()
					.addComponent(bigChord)
					.addGroup(layout.createParallelGroup()
						.addComponent(alternative1)
						.addComponent(alternative2)
						.addComponent(alternative3)
					)
				)
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addComponent(sourcePicker)
				.addGroup(layout.createParallelGroup()
					.addComponent(bigChord)
					.addGroup(layout.createSequentialGroup()
						.addComponent(alternative1)
						.addComponent(alternative2)
						.addComponent(alternative3)
					)
				)
		);
	}

	@Override
	public void run() {
		frame.pack();

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((screen.width / 2) - (frame.getWidth() / 2), (screen.height / 2) - (frame.getHeight() / 2));

		frame.setVisible(true);
	}

	@Override
	public void display(ArrayList<Chord> options, ArrayList<Note> notes) {
		switch(options.size()) {
			case 0:
				bigChord.setText("-");
				alternative1.setText("-");
				alternative2.setText("-");
				alternative3.setText("-");
				break;
			case 1:
				bigChord.setText(options.get(0).getName());
				alternative1.setText("-");
				alternative2.setText("-");
				alternative3.setText("-");
				break;
			case 2:
				bigChord.setText(options.get(0).getName());
				alternative1.setText(options.get(1).getName());
				alternative2.setText("-");
				alternative3.setText("-");
				break;
			case 3:
				bigChord.setText(options.get(0).getName());
				alternative1.setText(options.get(1).getName());
				alternative2.setText(options.get(2).getName());
				alternative3.setText("-");
				break;
			default:
				bigChord.setText(options.get(0).getName());
				alternative1.setText(options.get(1).getName());
				alternative2.setText(options.get(2).getName());
				alternative3.setText(options.get(3).getName());
				break;
		}
	}
}
