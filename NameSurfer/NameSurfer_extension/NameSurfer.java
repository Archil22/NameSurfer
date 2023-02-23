/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
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
	private JComboBox namesList;
	private JButton delete;
	private JButton keepOnlyOne;
	private JButton saveImage;
	private JTextField fileName;
	private JButton table;

/* Method: init() */
/**
 * This method has the responsibility for reading in the data base
 * and initializing the interactors at the bottom of the window.
 */
	public void init() {
	    // You fill this in, along with any helper methods //
		DB=new NameSurferDataBase(NameSurferConstants.NAMES_DATA_FILE);
		JLabel nameLabel = new JLabel("Name");
		textField = new JTextField(20);
		Graph = new JButton("Graph");
		Clear = new JButton("Clear");
		namesList=new JComboBox();
		delete = new JButton("Delete");
		keepOnlyOne=new JButton("KeepOnlyOne");
		saveImage = new JButton("save Image as PNG");
		fileName = new JTextField(20);
		table = new JButton("show Table");
		add(table,SOUTH);
		add(nameLabel,SOUTH);
		add(textField,SOUTH);
		add(Graph,SOUTH);
		add(Clear,SOUTH);
		add(namesList,NORTH);
		add(delete,NORTH);
		add(keepOnlyOne,NORTH);
		add(saveImage,NORTH);
		add(fileName,NORTH);
		addActionListeners();
		textField.addActionListener(this);
		fileName.addActionListener(this);
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
			drawGraph();
		}else if(e.getSource()==Clear){
			canvas.clear();
			namesList.removeAllItems();
		} else if (e.getSource()==delete) {
			deleteGraph();
		}else if (e.getSource()==keepOnlyOne){
			filterGraph();
		}else if(e.getSource()==saveImage){
			canvas.save(fileName);
		} else if (e.getSource()==fileName) {
			fileName.setText("");
		}else if(e.getSource()==table){
			showTable();
		}
	}

	private void showTable() {
		String str = (String) namesList.getSelectedItem();
		if(str!=null){
			new table(DB.findEntry(str));
		}
	}

	private void filterGraph() {
		String keepVal = (String) namesList.getSelectedItem();
		if(keepVal!=null){
			namesList.removeAllItems();
			namesList.addItem(keepVal);
			canvas.filter(DB.findEntry(keepVal));
		}
	}

	private void deleteGraph() {
		String name = (String) namesList.getSelectedItem();
		if(name!=null){
			canvas.delete(DB.findEntry(name));
			namesList.removeItem(name);
		}
	}

	private void drawGraph() {
		String name = textField.getText();
		NameSurferEntry nameEntry = DB.findEntry(name);
		if(nameEntry!=null && canvas.addEntry(nameEntry)){
			namesList.addItem(name);
		}
		textField.setText("");
	}
}