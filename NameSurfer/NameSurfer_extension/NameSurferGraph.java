/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */

import acm.graphics.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas
	implements NameSurferConstants, ComponentListener {
	private final HashMap<NameSurferEntry,Integer> entryList;
	private int fontSize=12;

	/**
	* Creates a new NameSurferGraph object that displays the data.
	 * initialises entryList,that save information about already added entryClasses
	*/
	public NameSurferGraph() {
		addComponentListener(this);
		entryList = new HashMap<>();

		//	 You fill in the rest //
	}
	
	/**
	* Clears the list of name surfer entries stored inside this class.
	*/
	public void clear() {
		//	 You fill this in //
		entryList.clear();
		update();
	}
	
	/* Method: addEntry(entry) */
	/**
	* Adds a new NameSurferEntry to the list of entries on the display.
	* Note that this method does not actually draw the graph, but
	* simply stores the entry; the graph is drawn by calling update.
	*/
	public boolean addEntry(NameSurferEntry entry) {
		if(!isHere(entry)){
			entryList.put(entry, entryList.size() % 4);
			drawEntry(entry,generateColor(entryList.get(entry)));
			return true;
		}else {
			return false;
		}
	}

	//checks if entry is already added or not
	private boolean isHere(NameSurferEntry entry) {
		return entryList.containsKey(entry);
	}


	//generates color for entryClass
	private Color generateColor(int rem) {
		switch (rem){
			case 0:return Color.BLACK;
			case 1:return Color.RED;
			case 2:return Color.BLUE;
			case 3:return Color.YELLOW;
		}
		return Color.BLACK;
	}

	//draws Glines,GLables,all information about entry on canvas
	private void drawEntry(NameSurferEntry entry,Color c){
		double gapBeetwenLines= (double) getWidth()/NDECADES;
		double unitOfOneRate = (double)(getHeight()-(2*GRAPH_MARGIN_SIZE))/MAX_RANK;
		GPoint leftPoint = new GPoint(0,generateY(START_DECADE,entry,unitOfOneRate));
		addLabel(entry.getName(),entry.getRank(START_DECADE),leftPoint,c);
		for(int i=1;i<NDECADES;i++){
			int rank = entry.getRank(START_DECADE+i*10);
			GPoint rightPoint = new GPoint(i*gapBeetwenLines,generateY(START_DECADE+i*10,entry,unitOfOneRate));
			addLabel(entry.getName(),rank,rightPoint,c);
			GLine line = new GLine(leftPoint.getX(),leftPoint.getY(),rightPoint.getX(),rightPoint.getY());
			line.setColor(c);
			add(line);
			leftPoint=rightPoint;
		}
	}


	//adds Glable on canvas
	private void addLabel(String name,int Rank,GPoint rankPoint,Color c) {
		GLabel label;
		if(Rank==0){
			label = new GLabel(name+" *");
		}else {
			label = new GLabel(name+" "+Rank);
		}
		label.setColor(c);
		label.setFont(label.getFont()+"-"+fontSize);
		add(label,rankPoint.getX()+5,rankPoint.getY()-5);
	}

	//Generates Y coordinate on vertical line for entry's specific rank
	private double generateY(int decade,NameSurferEntry entry,double unit){
		int rank = entry.getRank(decade);
		double y;
		if(rank==0){
			y=getHeight()-GRAPH_MARGIN_SIZE;
		}
		else{
			y=GRAPH_MARGIN_SIZE+((rank-1)*unit);
		}
		return y;
	}
	
	/**
	* Updates the display image by deleting all the graphical objects
	* from the canvas and then reassembling the display according to
	* the list of entries. Your application must call update after
	* calling either clear or addEntry; update is also called whenever
	* the size of the canvas changes.
	*/
	public void update() {
		fontSize=(int) ((getWidth()/(double)APPLICATION_WIDTH)*12);
		double gapBetweenLines = (double) getWidth()/NDECADES;
		removeAll();
		drawBorders();
		drawVerticalLines(gapBetweenLines);
		drawDecadeLabels(gapBetweenLines);
		drawEntries();
	}



	//draw graphs of entryList items
	private void drawEntries() {
		for (NameSurferEntry key:entryList.keySet()){
			drawEntry(key,generateColor(entryList.get(key)));
		}
	}

	//draws two Horizontal lines at the top and Bottom
	private void drawBorders(){
		GLine upperBorder = new GLine(0,GRAPH_MARGIN_SIZE,getWidth(),GRAPH_MARGIN_SIZE);
		GLine lowerBorder = new GLine(0,getHeight()-GRAPH_MARGIN_SIZE,getWidth(),getHeight()-GRAPH_MARGIN_SIZE);
		add(upperBorder);
		add(lowerBorder);
	}

	//draws vertical lines
	private void drawVerticalLines(double gapBetweenLines){
		for(int i=0;i<NDECADES;i++){
			double xVal = gapBetweenLines*i;
			GLine vertivalLine = new GLine(xVal,0,xVal,getHeight());
			add(vertivalLine);
		}
	}


	//draws labels for every decade at the bottom of the canvas
	private void drawDecadeLabels(double gapBetweenLabels){
		for(int i=0;i<NDECADES;i++){
			double xVal = (gapBetweenLabels*i)+2;
			GLabel label = new GLabel(""+(START_DECADE+10*i));
			label.setFont(label.getFont()+"-"+fontSize);
			add(label,xVal,getHeight()-2);
		}
	}

	//removes entry class from canvas and entryList
	public void delete(NameSurferEntry entry) {
		entryList.remove(entry);
		update();
	}

	//removes all entries,but one
	public void filter(NameSurferEntry entry) {
		int key = entryList.get(entry);
		entryList.clear();
		entryList.put(entry,key);
		update();
	}

	//reads GCanvas private 'offscreenImage' Field using java reflection and save image as PNG in FileSystem
	public void save(JTextField fileName) {
		if(fileName.getText().equals("")){
			fileName.setText("Enter The Name");
		}else {
			Class canvasClass = this.getClass().getSuperclass();
			try {
				Field f = canvasClass.getDeclaredField("offscreenImage");
				f.setAccessible(true);
				Image img = (Image) f.get(this);
				ImageIO.write((RenderedImage) img,"png",new File("./"+fileName.getText()+".png"));
				fileName.setText("");
			}catch (Exception e){
				throw new RuntimeException(e);
			}
		}
	}

	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentResized(ComponentEvent e) { update(); }
	public void componentShown(ComponentEvent e) { }
}
