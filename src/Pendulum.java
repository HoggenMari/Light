import processing.core.PConstants;
import processing.core.PGraphics;


public class Pendulum {
	
	double speed = 1;
	int timer = 0;
	int width = 50;
	int x = 0;
	int y = 0;
	private NozzleLayer nozzleLayer;
	private PGraphics pg;
	private boolean dead = false;
	private boolean value = false;
	
	public Pendulum(NozzleLayer nozzleLayer){
		this.nozzleLayer = nozzleLayer;
		pg = nozzleLayer.getLayer();
	}
	
	public void draw(){
		pg.beginDraw();
		pg.clear();
		pg.colorMode(PConstants.HSB, 360, 100, 100,255);
		pg.noStroke();
		if(value){
		for(int i=0; i<pg.width-x; i++){
		pg.fill(i,100,100,pg.width-x+50);
		pg.rect((((pg.width/2)-(pg.width-x)/2))+i, y, 1, width);
		}
		}else{
		for(int i=0; i<pg.width-x; i++){
		pg.fill(pg.width-x-i,100,100,pg.width-x+50);
		pg.rect((((pg.width/2)-(pg.width-x)/2))+i, y, 1, width);
		}	
		}
		nozzleLayer.add();
		speed = speed + 0.5;
		
		x = x + (int)speed;

		  // if we've hit the floor... 
		  if (x > pg.width) {
		    // set the position to be on the floor
		    x = pg.width; 
		    // and make the y speed 90% of what it was, 
		    // but in the opposite direction
		    speed = speed * -0.9;
		    System.out.println("GO1");
		    value = !value;
		  } 
		
		timer++;
		if(timer==500){
			dead = true;
		}
	}
	
	public boolean isDead(){
		return dead;
	}

}
