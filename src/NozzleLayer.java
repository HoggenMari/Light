import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;


public class NozzleLayer {
	
	private LinkedList<Nozzle> nozzlePath;
	private PGraphics pg;
	private int totalWidth;
	private PApplet p;
	private Pavillon scp;

	public NozzleLayer(PApplet p, Pavillon scp, LinkedList<Nozzle> nozzlePath) {
		this.p = p;
		this.scp = scp;
		this.nozzlePath = nozzlePath;
	}
	
	public PGraphics getLayer() {
		for(Nozzle n : nozzlePath) {
			totalWidth += n.sysA.width;
		}
		pg = p.createGraphics(totalWidth,5);
		return pg;
	}
	
	public void add() {
		int currentX = 0;
		for(int i=nozzlePath.size()-1; i>-1; i--) {
			Nozzle n = nozzlePath.get(i);
			PGraphics p = scp.nozzleList.get(n.id).sysA;
			PImage img = pg.get(currentX, 0, currentX+p.width, 5);
			p.beginDraw();
			p.image(img,0,0);
			p.endDraw();
			currentX += p.width;
		}
	}

}
