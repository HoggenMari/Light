import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import de.looksgood.ani.*;

public class TopGlow implements Effect{

	
	private PApplet p;
	private NozzleLayer nozzleLayer;
	private PGraphics pg;
	private int startGlow;
	float alpha;
	private AniSequence seq;
	private boolean dead;

	public TopGlow(PApplet p, NozzleLayer nozzleLayer, int startGlow, int maxGlow){
	  System.out.println("KONSTRUKTOR");
	  this.p = p;
	  this.nozzleLayer = nozzleLayer;
	  this.alpha = startGlow;
	  pg = nozzleLayer.getLayer();
	  
	  Ani.init(p);
	  
	  seq = new AniSequence(p);
	  seq.beginSequence();
		
	  seq.add(Ani.to(this, (float) 2.0, "alpha", 255, Ani.CIRC_IN));
	  seq.add(Ani.to(this, (float) 10.0, "alpha", 0, Ani.CIRC_OUT));

	  seq.endSequence();
	  
	  seq.start();
	  
	  //Ani.to(p, (float) 1.0, "alpha", 255, Ani.QUINT_IN);
		  
	}
	
	@Override
	public void draw() {
		// TODO Auto-generated method stub
		pg.beginDraw();
		pg.clear();
		pg.colorMode(PConstants.HSB, 360,255,255, 255);
		System.out.println(alpha);
		pg.fill(0,0,255,alpha);
		pg.noStroke();
		pg.rect(0, 4, pg.width, 1);
		pg.endDraw();
		nozzleLayer.add();
		
		if(seq.isEnded()){
			dead = true;
		}
		
	}

	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		return dead;
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
