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
	private NozzleLayer nozzleLayer;
	private PGraphics pg;
	private ArrayList<GlitterParticle> glitterList;
	private int color;

	public Stars(PApplet p, NozzleLayer nozzleLayer, int color) {
		this.p = p;
		this.nozzleLayer = nozzleLayer;
		this.color = color;
		pg = nozzleLayer.getLayer();
		glitterList = new ArrayList<GlitterParticle>();
		
		for(int i=0; i<30; i++){
			glitterList.add(new GlitterParticle(p,pg.width));
		}

	}

	public void draw() {
		
		pg.beginDraw();
		pg.clear();
		pg.noStroke();
		pg.colorMode(PConstants.HSB, 360, 100, 100);
		for(Iterator<GlitterParticle> glitterIterator = glitterList.iterator(); glitterIterator.hasNext();){
			GlitterParticle glitter = glitterIterator.next();
			pg.stroke(color, glitter.lifetime);
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

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean fadeBack() {
		// TODO Auto-generated method stub
		return false;
	}

}
