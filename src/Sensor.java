import java.util.ArrayList;

import controlP5.CheckBox;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import processing.core.PApplet;


public class Sensor {
	
	private PApplet p;
	private int id;
	private boolean flash;
	private int timerFlash;
	private int timerWi;
	private int posX[] = {1023,1023,1023,1023};
	private int posY[] = {1023,1023,1023,1023};
	private boolean active = false;
	private int x;
	private int y;

	public Sensor(PApplet p, int id, int x, int y){
		this.p = p;
		this.id = id;
		this.x = x;
		this.y = y;

	}
		
	public void drawOnGui(){
		p.fill(0);
		p.text("Sensor :"+id, x, y);
		p.rect(x, y+25, 100, 78);	
		if(flash){
			p.fill(255);
			p.ellipse(x+45, y+45, 10, 10);
		}
		if(PApplet.abs(p.millis()-timerFlash)>=1000){
			flash = false;
			//active = false;
		}
		if(PApplet.abs(p.millis()-timerWi)>=1000){
			for(int i=0; i<4; i++){
				posX[i] = 1023;
				posY[i] = 1023;
			}
			//active = false;
		}
		for(int i=0; i<4; i++){
			if(posX[i]!=1023 | posY[i]!=1023){
				p.fill(255);
				p.noStroke();
				p.ellipse((int)(posX[i]/10)+x, (int)(posY[i]/10)+y+25, 5, 5);
			}
		}
	}
	
	public int getID(){
		return id;
	}
	
	public void setFlash(){
		flash = true;
		timerFlash = p.millis();
		active = true;
	}
	
	public boolean getState(){
		return active;
	}
	
	public void setState(boolean bool){
		active = bool;
	}
	
	public void setWi(int posX[], int posY[]){
		this.posX=posX;
		this.posY=posY;
		timerWi = p.millis();
		active = true;
	}
	
	public void addEvent(int type){
		//eventList.add(new SensorEvent(type));
	}
	
	public String toString(){
		String event = "EVENTS: ";
		/*for(SensorEvent e : eventList){
			event+=e.toString();
		}*/
		return event;
	}

}
