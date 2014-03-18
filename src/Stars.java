import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;


public class Stars implements Effect {


	private PApplet p;
	private LinkedList<Nozzle> path;
	private boolean dead = false;
	private Layer nozzleLayer;
	private PGraphics pg;
	private ArrayList<GlitterParticle> glitterList;

	public Stars(PApplet p, Layer nozzleLayer) {
		this.p = p;
		this.nozzleLayer = nozzleLayer;
		pg = nozzleLayer.getLayer();
		glitterList = new ArrayList<GlitterParticle>();
		
		for(int i=0; i<30; i++){
			glitterList.add(new GlitterParticle(p,100));
		}

	}

	public void draw() {
		
		pg.beginDraw();
		pg.clear();
		pg.noStroke();
		pg.colorMode(PConstants.HSB, 360, 100, 100);
		for(Iterator<GlitterParticle> glitterIterator = glitterList.iterator(); glitterIterator.hasNext();){
			GlitterParticle glitter = glitterIterator.next();
			pg.stroke(0, 0, 100, glitter.lifetime);
			pg.strokeWeight(1);
			glitter.update();	  
			if(glitter.lifetime>0){
				pg.point(p.random(glitter.x-20, glitter.x), p.random(0, pg.height));
			} else {
				//System.out.println("DEAD");
				glitterIterator.remove();
			}
		  }
		pg.endDraw();
		nozzleLayer.add();
		
		if(glitterList.isEmpty()){
			dead = true;
		}
	}

	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		return dead;
	}
	
	class GlitterParticle{
		
		public float x;
		public int lifetime;

		public GlitterParticle(PApplet p, int x){
			x=(int)p.random(-x,0);
			lifetime=(int) p.random(100,255);
		}
		
		public void update(){
			lifetime --;
			x++;
		}
	}

}
