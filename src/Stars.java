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
		
		for(int i=0; i<20; i++){
			glitterList.add(new GlitterParticle(p,pg.width));
		}

	}

	public void draw() {
		
		for(Iterator<GlitterParticle> glitterIterator = glitterList.iterator(); glitterIterator.hasNext();){
			GlitterParticle glitter = glitterIterator.next();
			  
			if(glitter.lifetime>0){
				pg.beginDraw();
				pg.clear();
				pg.noStroke();
				pg.colorMode(PConstants.HSB, 360, 100, 100);
				pg.stroke(0, 0, 100, glitter.lifetime);
				pg.strokeWeight(1);
				//int ldx = (int) p.random(dot.x-20, dot.x);
				//pg.line(ldx, ld.y, ldx, 5);
				glitter.update();
				pg.point(p.random(glitter.x-20, glitter.x), p.random(0, pg.height));
				pg.endDraw();
				nozzleLayer.add();
			} else {
				//System.out.println("DEAD");
				glitterIterator.remove();
			}
		  }
		
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
			x=(int)p.random(x,0);
			lifetime=(int) p.random(100,255);
		}
		
		public void update(){
			lifetime --;
			x++;
		}
	}

}
