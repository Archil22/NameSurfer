/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implement the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

public class NameSurfer extends Program implements NameSurferConstants {
	private JTextField textField;
	private JButton Graph;
	private JButton Clear;
	private NameSurferDataBase DB;//saves information read from names-data.txt as HashMap of String and NameSurferEntry class
	private NameSurferGraph canvas;

/* Method: init() */
/**
 * This method has the responsibility for reading in the database
 * and initializing the interactors at the bottom of the window.
 */
	public void init() {
	    // You fill this in, along with any helper methods //
		DB=new NameSurferDataBase(NameSurferConstants.NAMES_DATA_FILE);
		JLabel nameLabel = new JLabel("Name");
		textField = new JTextField(20);
		Graph = new JButton("Graph");
		Clear = new JButton("Clear");
		add(nameLabel,SOUTH);
		add(textField,SOUTH);
		add(Graph,SOUTH);
		add(Clear,SOUTH);
		addActionListeners();
		textField.addActionListener(this);
		canvas=new NameSurferGraph();
		add(canvas,CENTER);
	}

/* Method: actionPerformed(e) */
/**
 * This class is responsible for detecting when the buttons are
 * clicked, so you will have to define a method to respond to
 * button actions.
 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==Graph||e.getSource()==textField){
			String name = textField.getText();
			NameSurferEntry nameEntry = DB.findEntry(name);
			if(nameEntry!=null){
				canvas.addEntry(nameEntry);
			}
			textField.setText("");
		}else if(e.getSource()==Clear){
			canvas.clear();
		}
	}
}