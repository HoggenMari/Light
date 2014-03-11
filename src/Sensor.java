import java.util.ArrayList;

import processing.core.PApplet;


public class Sensor {
	
	private PApplet p;
	private int id;
	public ArrayList<SensorEvent> eventList = new ArrayList<SensorEvent>();

	public Sensor(PApplet p, int id){
		this.p = p;
		this.id = id;
	}
		
	public void drawOnGui(int x, int y){
		p.fill(0);
		p.text("Sensor :"+id, x, y);
		p.rect(x, y+5, 100, 100);	
	}
	
	public void addEvent(int type){
		eventList.add(new SensorEvent(type));
	}
	
	public String toString(){
		String event = "EVENTS: ";
		for(SensorEvent e : eventList){
			event+=e.toString();
		}
		return event;
	}

}
