import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;


public class Shine extends HorizontalMove {

	public Shine(LinkedList<Nozzle> path) {
		super(path);
		// TODO Auto-generated constructor stub
	}

	public void draw() {
		for(Dot dot : dotList) {
			PGraphics pg = dot.current.sysA;
			if(dot.lifetime>0){
				pg.beginDraw();
				pg.noStroke();
				pg.colorMode(PConstants.HSB, 360, 100, 100);
				pg.fill(0, 100, 100, dot.lifetime);
				pg.noStroke();
				pg.rect((int)dot.x,(int)dot.y,1,5);
				pg.endDraw();
			}
		}
	}

}
