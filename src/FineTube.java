import java.util.LinkedList;

import de.looksgood.ani.Ani;
import de.looksgood.ani.AniSequence;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;


public class FineTube implements Effect{

	private PApplet p;
	private NozzleLayer nozzleLayer;
	private ColorFade cf;
	private PGraphics pg;

	private float x;
	private int a1;
	private int m1;
	private int e1;
	private int current;
	private int speed;
	private int timer;
	private boolean dead = false;
	private LinkedList<Nozzle> nozzlePath;
	private PGraphics pg2;
	private NozzleLayer nLayer;
	private TopGlow tp;
	private boolean first = false;

	public FineTube(PApplet p, NozzleLayer nozzleLayer, ColorFade cf){
		this.p = p;
		this.nozzleLayer = nozzleLayer;
		this.cf = cf;
		
		a1 = 5;
		m1 = 10;
		e1 = 5;
		
		x=-(2*a1+m1+e1);
		
		pg = nozzleLayer.getLayer();
		
		speed=90;
		
		timer = p.millis();
		
		Ani.init(p);
		Ani.to(this, (float) 1.0, "speed", 50, Ani.CIRC_IN);

		  
		
		//p.frameRate(20);

	}
	
	public FineTube(PApplet p, Pavillon scp, LinkedList<Nozzle> nozzlePath, NozzleLayer nozzleLayer, ColorFade cf){
		this.p = p;
		this.nozzleLayer = nozzleLayer;
		this.cf = cf;
		this.nozzlePath = nozzlePath;
		
		a1 = 5;
		m1 = 10;
		e1 = 5;
		
		x=-(2*a1+m1+e1);
		
		pg = nozzleLayer.getLayer();
		LinkedList<Nozzle> lastNozzle = new LinkedList<Nozzle>();
		lastNozzle.add(nozzlePath.getLast()); 
		nLayer = new NozzleLayer(p, scp, lastNozzle);
		pg2 = nLayer.getLayer();

		
		speed=90;
		
		timer = p.millis();
		
		Ani.init(p);
		Ani.to(this, (float) 1.0, "speed", 50, Ani.CIRC_IN);

		  
		
		//p.frameRate(20);

	}

	@Override
	public void draw() {
		
		System.out.println("SPEED: "+speed);
		
		pg.beginDraw();
		pg.colorMode(PConstants.HSB,360,255,255,255);
		pg.noStroke();
		pg.clear();
		pg.smooth();
		
		
		
		
		

		int amount = cf.alpha/a1;

		for(int i=0; i<a1; i++){
			pg.fill(cf.hue, 255, 255, i*amount);
			pg.rect(x+i, 0, 1, pg.height);
		}
				
			pg.fill(cf.hue, 255, 255, a1*amount);
			pg.rect(x+a1, 0, m1, pg.height);
		
		int e1 = 20;
		
		amount = cf.alpha/e1;
		for(int i=0; i<e1; i++){
			pg.fill(cf.hue, 255, 255, cf.alpha-i*amount);
			pg.rect(x+i+a1+m1, 0, 1, pg.height);
		}
		pg.endDraw();
		nozzleLayer.add();
		
		current = p.millis();
		if(current-timer>speed){
		x++;
		timer=current;
		}

		if(x>pg.width+20){
			dead = true;
		}
		
	}
	
	public void draw2() {
		
		System.out.println("SPEED: "+speed);
		
		pg.beginDraw();
		pg.colorMode(PConstants.HSB,360,255,255,255);
		pg.noStroke();
		pg.clear();
		pg.smooth();
		
		
		
		
		

		int amount = cf.alpha/a1;

		for(int i=0; i<a1; i++){
			pg.fill(cf.hue, 255, 255, i*amount);
			pg.rect(x+i, 0, 1, pg.height);
		}
				
			pg.fill(cf.hue, 255, 255, a1*amount);
			pg.rect(x+a1, 0, m1, pg.height);
		
		int e1 = 20;
		
		amount = cf.alpha/e1;
		for(int i=0; i<e1; i++){
			pg.fill(cf.hue, 255, 255, cf.alpha-i*amount);
			pg.rect(x+i+a1+m1, 0, 1, pg.height);
		}
		pg.endDraw();
		nozzleLayer.add();
		
		current = p.millis();
		if(current-timer>speed){
		x++;
		timer=current;
		}

		if(x>pg.width+20){
			if(first ){
			tp = new TopGlow(p, nozzleLayer, 0, 200);
			first = false;
			}else{
			tp.draw();
			}
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
