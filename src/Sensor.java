import java.util.ArrayList;

import processing.core.PApplet;


public class Sensor {
	
	private PApplet p;
	private int id;
	private boolean flash;
	private int timerFlash;
	private int timerWi;
	private int posX[] = {1023,1023,1023,1023};
	private int posY[] = {1023,1023,1023,1023};

	public Sensor(PApplet p, int id){
		this.p = p;
		this.id = id;
	}
		
	public void drawOnGui(int x, int y){
		p.fill(0);
		p.text("Sensor :"+id, x, y);
		p.rect(x, y+5, 100, 78);	
		if(flash){
			p.fill(255);
			p.ellipse(x+45, y+45, 10, 10);
		}
		if(PApplet.abs(p.millis()-timerFlash)>=1000){
			flash = false;
		}
		if(PApplet.abs(p.millis()-timerWi)>=1000){
			for(int i=0; i<4; i++){
				posX[i] = 1023;
				posY[i] = 1023;
			}
		}
		for(int i=0; i<4; i++){
			if(posX[i]!=1023 | posY[i]!=1023){
				p.fill(255);
				p.noStroke();
				p.ellipse((int)(posX[i]/10)+x, (int)(posY[i]/10)+y+5, 5, 5);
			}
		}
	}
	
	public void setFlash(){
		flash = true;
		timerFlash = p.millis();
	}
	
	public void setWi(int posX[], int posY[]){
		this.posX=posX;
		this.posY=posY;
		timerWi = p.millis();
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
