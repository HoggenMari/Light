import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;


public class TubeAnimation {
	
	private ArrayList<ArrayList<SimpleTube>> sTubeList = new ArrayList<ArrayList<SimpleTube>>();
	private ArrayList<Effect> effectList = new ArrayList<Effect>();
	private PApplet p;
	private Pavillon scp;
	private ColorFadeList cfList;
	private ColorFade glow;
	private ColorFade backGround;
	private int hue = 0, saturation = 90, brightness = 100;
	private boolean colorChange = false;
	private boolean in = false;


	public TubeAnimation(PApplet p, Pavillon scp, ColorFadeList cfList){
		this.p = p;
		this.scp = scp;
		this.cfList = cfList;
		
		for(int i=0; i<7; i++){
			sTubeList.add(new ArrayList<SimpleTube>());
		}
	}
	
	public void setupMode1(){
		glow = new ColorFade(p, 0, 0, 100);
		glow.brightnessFade(30, 2000);
		cfList.addColorFade(glow);
		
		backGround = new ColorFade(p, 260, 100, 70);
		backGround.brightnessFade(40, 2000);
		cfList.addColorFade(backGround);
	}
	
	public void setupMode2(){
		glow = new ColorFade(p, 0, 0, 100);
		glow.brightnessFade(0, 2000);
		cfList.addColorFade(glow);
		
		backGround = new ColorFade(p, 260, 100, 100);
		backGround.saturationFade(70, 5000);
		cfList.addColorFade(backGround);
	}
	
	public void draw(){
		
		backGround();
		tube();
		
		//glow();
		
		if(p.frameCount%300==0){
			colorChange = true;
			//hue = (int) p.random(0,360);
		}
		
		//colorChange();
	}
	
	private void tube(){
		
		for(int i=0; i<sTubeList.size(); i++){
		for(Iterator<SimpleTube> sTIterator = sTubeList.get(i).iterator(); sTIterator.hasNext();){
			  SimpleTube sT = sTIterator.next();
			  sT.draw();
			  if(sT.isDead()){
				  //System.out.println("DEAD");
				  sTIterator.remove();
			  }
		}
		}
		
		int TubesGesamt = 0;
		for(int i=0; i<sTubeList.size(); i++){
			TubesGesamt =+ sTubeList.get(i).size();
		}
		
		while(TubesGesamt<1){	
			ColorFade sTube = new ColorFade(p, (int) ((int)hue+(p.random(-50,50))), (int)saturation, (int)brightness);
			sTube.hueFade((int)hue+20, 3000);
			cfList.addColorFade(sTube);
			
			int zf = (int)p.random(0,7);
			if(sTubeList.get(zf).size()==0){
			LinkedList<Nozzle> nozzlePath = scp.createNodePath(scp.nodeList.get(zf));
			if(p.frameCount%1==1){
			nozzlePath = scp.createRandomPath(8*zf,8*zf+8,58,65);
			}else if(p.frameCount%1==0){
			nozzlePath = scp.createNodePath(scp.nodeList.get(zf));
			}
			NozzleLayer nLayer = new NozzleLayer(p, scp, nozzlePath);
			sTubeList.get(zf).add(new SimpleTube(p, nLayer, sTube, (int)p.random(50,80), 1+Math.random()*0.5));
			}
			
			TubesGesamt = 0;
			for(int i=0; i<sTubeList.size(); i++){
				TubesGesamt =+ sTubeList.get(i).size();
			}
			
			
		}
	}

	private void backGround(){
		
		for(int i=0; i<scp.nodeList.size(); i++){
			for(int j=0; j<scp.nodeList.get(i).nozzleList.size(); j++){
				LinkedList<Nozzle> nozzlePath = scp.createNodePath(scp.nodeList.get(i));
				NozzleLayer nLayer = new NozzleLayer(p, scp, nozzlePath);
				PGraphics pg = scp.nodeList.get(i).nozzleList.get(j).sysB;
				pg.beginDraw();
				pg.colorMode(PConstants.HSB, 360, 100, 100);
				pg.noStroke();
				pg.fill(backGround.hue, backGround.saturation, backGround.brightness);
				pg.rect(0, 0, pg.width, pg.height);
				pg.endDraw();
			}
		}
		
	}
	
	
	public void glow(){
		for(int i=0; i<scp.nodeList.size(); i++){
			for(int j=0; j<scp.nodeList.get(i).nozzleList.size(); j++){
				LinkedList<Nozzle> nozzlePath = scp.createNodePath(scp.nodeList.get(i));
				NozzleLayer nLayer = new NozzleLayer(p, scp, nozzlePath);
				PGraphics pg = scp.nodeList.get(i).nozzleList.get(j).sysB;
				pg.beginDraw();
				pg.colorMode(PConstants.HSB, 360, 100, 100);
				pg.noStroke();
				int invert=0;
				if(i%2==0){
					invert = 130;
				}
				pg.fill(glow.hue, glow.saturation, Math.abs(invert-glow.brightness));
				pg.rect(0, 0, pg.width, pg.height);
				pg.endDraw();
			}
		}
	}

}
