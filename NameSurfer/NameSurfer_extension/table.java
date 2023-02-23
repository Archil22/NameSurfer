import acm.graphics.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.RenderedImage;
import java.io.File;
import java.lang.reflect.Field;

public class table extends JFrame implements ComponentListener, MouseListener {
    private GCanvas canvas;
    private NameSurferEntry entry;
    private int fontSize;
    private GImage download;
    table(NameSurferEntry entry){
        canvas = new GCanvas();
        setVisible(true);
        setSize(NameSurferConstants.APPLICATION_WIDTH,NameSurferConstants.APPLICATION_HEIGHT);
        this.entry=entry;
        add(canvas);
        addComponentListener(this);
    }

    //draws nameLabel,rectangles for decades and rates and adds download icon
    //this function is called every time,when window is resized
    //add mouseListener to download GImage
    public void drawTable(){
        canvas.removeAll();
        drawName();
        drawRects();
        drawDownloadIcon();
        download.addMouseListener(this);
    }

    private void drawDownloadIcon() {
        download = new GImage("download.png");
        download.setSize(canvas.getWidth()/10.0,canvas.getHeight()/10.0);
        canvas.add(download,canvas.getWidth()-download.getWidth()-50,canvas.getHeight()-download.getHeight()-50);
    }

    private void drawLabels(String text,GRect rect) {
        GLabel label = new GLabel(text);
        label.setFont(label.getFont()+"-"+fontSize);
        GPoint rectLocation = rect.getLocation();
        double X = rectLocation.getX()+((rect.getWidth()-label.getWidth())/2);
        double Y = rectLocation.getY()+((rect.getHeight()-label.getAscent())/2)+label.getAscent();
        canvas.add(label,X,Y);
    }


    //draws 22 rectangles in the center of the canvas
    private void drawRects() {
        for(int y=0;y<2;y++){
            for(int x=0;x<11;x++){
                double rectSide = (double) canvas.getWidth()/11;
                GRect rect = new GRect(rectSide, rectSide);
                rect.setColor(Color.BLACK);
                canvas.add(rect,x*rectSide,(canvas.getHeight()/2.0-rectSide)+y*rectSide);
                if(y==0){
                    drawLabels(""+(1900+x*10),rect);
                }else {
                    drawLabels(""+entry.getRank((1900+x*10)),rect);
                }
            }
        }

    }

    private void drawName() {
        GImage check = new GImage("check.png");
        check.setSize(canvas.getWidth()/20.0,canvas.getHeight()/20.0);
        canvas.add(check,50,50);
        GLabel name = new GLabel(entry.getName());
        fontSize=(int) ((getWidth()/(double)NameSurferConstants.APPLICATION_WIDTH)*17);
        name.setFont(name.getFont()+"-"+fontSize);
        canvas.add(name,70+check.getWidth(),50+name.getAscent());
    }

    //as you click on the download GImage, this function uses Java reflection to save canvas as png on fileSystem
    public void mouseClicked(MouseEvent e){
        Class canvasClass = canvas.getClass();
        try {
            Field f = canvasClass.getDeclaredField("offscreenImage");
            f.setAccessible(true);
            Image img = (Image) f.get(canvas);
            ImageIO.write((RenderedImage) img,"png",new File("./"+entry.getName()+"_table"+".png"));
        }catch (Exception exc){
            throw new RuntimeException(exc);
        }
    }
    public void mousePressed(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}

    public void componentHidden(ComponentEvent e) { }
    public void componentMoved(ComponentEvent e) { }
    public void componentResized(ComponentEvent e) { drawTable(); }
    public void componentShown(ComponentEvent e) { }
}