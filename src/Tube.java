import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;


public class Tube {

	private PApplet p;
	private PGraphics pg;
	private ColorFade cf;
	private int length;
	double x=0;
	private NozzleLayer nozzleLayer;
	private boolean dead;
	private double speed=0;
	private int color;
	
	public Tube(PApplet p, NozzleLayer nozzleLayer, int color, int length, double speed) {
		this.p = p;
		this.nozzleLayer = nozzleLayer;
		this.cf = cf;
		this.color = color;
		this.length = length;
		this.speed  = speed;
		pg = nozzleLayer.getLayer();
		x=0;
	}
	
	public void draw() {
		pg.beginDraw();
		pg.colorMode(PConstants.HSB, 360, 100, 100);
		pg.clear();
		pg.noStroke();
		double alpha = 255.0/length;
		pg.fill(color, 20);
		pg.rect((int)x, 0, 1, 5);
		pg.fill(color, 50);
		pg.rect((int)x-1, 0, 1, 5);
		pg.fill(color, 80);
		pg.rect((int)x-2, 0, 1, 5);
		pg.fill(color, 100);
		pg.rect((int)x-3, 0, 1, 5);
		pg.fill(color, 200);
		pg.rect((int)x-4, 0, 1, 5);
		for(int i=5; i<length; i+=1){
			pg.fill(color, (int) (255-alpha*i));
			pg.rect((int)x-i, 0, 1, 5);
		}
		pg.endDraw();
		nozzleLayer.add();
		x += speed;
		
		if(x-length>pg.width){
			dead = true;
		}
	}
	
	
	public void reverseDraw() {
		pg.beginDraw();
		pg.colorMode(PConstants.HSB, 360, 100, 100);
		pg.clear();
		pg.noStroke();
		double alpha = 255.0/length;
		for(int i=length; i>0; i--){
			pg.fill(cf.hue, cf.saturation, cf.brightness, (int) (255-alpha*i));
			pg.rect((int)x+i, 0, 1, 5);
		}
		pg.endDraw();
		nozzleLayer.add();
		x -= 1;
		
		if(x+length<0){
			dead = true;
		}
	}
	
	public void draw2() {
		pg.beginDraw();
		pg.colorMode(PConstants.HSB, 360, 100, 100);
		pg.clear();
		pg.noStroke();
		double alpha = 255.0/length;
		for(int i=0; i<length; i++){
			pg.fill(cf.hue, cf.saturation, cf.brightness, (int) (255-alpha*i));
			pg.rect((int)x-i, 0, 1, 1);
			pg.rect((int)x-i, 2, 1, 1);
			pg.rect((int)x-i, 4, 1, 1);
		}
		pg.endDraw();
		nozzleLayer.add();
		x += 1;
		
		if(x>pg.width){
			dead = true;
		}
	}
	
	public boolean isDead() {
		return dead;
	}
}
