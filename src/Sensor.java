import processing.core.PApplet;


public class Sensor {
	
	private PApplet p;
	private int id;

	public Sensor(PApplet p, int id){
		this.p = p;
		this.id = id;
	}
		
	public void drawOnGui(int x, int y){
		p.fill(0);
		p.text("Sensor :"+id, x, y);
		p.rect(x, y+5, 100, 100);
		
	}

}
