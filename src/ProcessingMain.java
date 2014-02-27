/*
 * Created by Marius Hoggenmüller on 04.02.14
 * Copyright (c) 2014 Marius Hoggenmüller. All rights reserved.
 * 
 */


import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import codeanticode.gsvideo.GSMovie;
import controlP5.CheckBox;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.ControllerGroup;
import controlP5.Textfield;
import processing.core.*;
import processing.serial.*;

public class ProcessingMain extends PApplet {
	
	private static final long serialVersionUID = 1L;
	
	//Network settings
	private final String IP = "224.1.1.1";
	private final int PORT = 5026;
	private final int[] CONTROLLER_ID = {4, 1};
	
	//Init Nozzles, Node, Sculpture Objects
	private final int NODE1_LEDS[] = {75,60,75,60,90,75,60,75};
	private final int NODE2_LEDS[] = {75,75,75,75,60,60,75,75,60,90};
	private final int NODE3_LEDS[] = {75,75,75,60,90,75,75,60,75,75,75};
	private final int NODE4_LEDS[] = {60,75,60,75,60,75,75,60};
	private final int NODE5_LEDS[] = {60,90,75,60,75,75,75,90,75,75,60};
	private final int NODE6_LEDS[] = {75,75,45,90,60,60,90,75,60};
	private final int NODE7_LEDS[] = {75,75,75,75,60,60,60,60,90};
		
	Node node1, node2, node3, node4, node5, node6, node7;

	Pavillon scp;

	//Variables for GUI
	ControlP5 cp5;
	private CheckBox checkbox;
	float checkbox_array[] = {0,0,0,0};
	boolean activeArray [] = {false, false, false, false};
	
	//Arduino Communication
	static final String ARDUINO_DEVICE = "/dev/tty.usbmodemda121";
	static int lf = 10; // Linefeed in ASCII
	String myString = null; // Serial Output String
	Serial myPort; // Serial port you are using

	//Animation Stuff
	private PGraphics pg, pg2;

	private int counter1;
	private int counter2;

	private int counter3;

	private float y=0;

	private boolean next;

	private LinkedList<Nozzle> path;

	private PGraphics pg_last;

	private int color;

	private ColorPoint cp;

	private DrawPath dp;

	private int color1;

	private int color2;

	private ArrayList<hsvGradient> hsv1 = new ArrayList<hsvGradient>();

	private int startHue;
	
	//DrawPathApplication
	private ArrayList<DrawPath> dpList= new ArrayList<DrawPath>();
	private ArrayList<ColorPoint> cpList= new ArrayList<ColorPoint>();
	int max_path = 1;
	
	//Shine
	private static int SHINE_HOR_MAX = 1;
	private static int SHINE_VERT_MAX = 8;
	private ArrayList<HorizontalShine> horizontalShineList = new ArrayList<HorizontalShine>();
	private ArrayList<VerticalShine> verticalShineList = new ArrayList<VerticalShine>();

	private LinkedList<Nozzle> nozzlePath;

	private ColorFade colorFade1, colorFade2, colorFade3, colorFade4, colorFade5, colorFade6, colorFade7, colorFade8, colorFade9, colorFade10, colorFade11;

	int h=0;

	private ColorFadeList cfl;
	
	private GSMovie m;

	private boolean flash;

	private ArrayList<discoDot> discoDotList = new ArrayList<discoDot>();
	
	double k = 1.0;

	private RandomLampManager rlm, rlm2;

	private HorizontalMove horMove;

	private ArrayList<HorizontalMove> horizontalMoveList = new ArrayList<HorizontalMove>();

	private ColorFade cfYellow;

	private ColorFadeList cfList = new ColorFadeList(this);

	private NozzleLayer nLayer;

	private PGraphics layerGraphics;
	
	double x=0.1;
	double acc=0.05;

	private long ns;

	private float xn;

	private float yn;

	private float xs;

	private double zn;

	private ColorFade cfFlower;

	private int z = 0;

	private ColorFade lampFade;;
	  
	//Initiate as Application
	public static void main(String args[]) {
	    PApplet.main(new String[] { "--present", "ProcessingMain" });
	  }

	public void setup() {
		
		size(1200,800);
		
		//initArduino();

		  /*String portName = Serial.list()[5];
		  for(int i=0; i<Serial.list().length; i++) {
		    System.out.println(Serial.list()[i]);
		  }
		  myPort = new Serial(this, portName, 9600);*/
		  
		//frameRate(10);
		
		//Init GUI with Textfields, Buttons
		cp5 = new ControlP5(this);

		cp5.addTextfield("IP")
		   .setPosition(10,10)
		   .setSize(50,15)
		   .setColor(color(255,255,255))
		   .setColorCaptionLabel(color(0,0,0))
		   .setText(IP);
		;
		
		cp5.addTextfield("PORT")
		   .setPosition(70,10)
		   .setSize(30,15)
		   .setColor(color(255,255,255))
		   .setColorCaptionLabel(color(0,0,0))
		   .setText(Integer.toString(PORT));
		;
		
		cp5.addTextfield("ID-1")
		   .setPosition(110,10)
		   .setSize(20,15)
		   .setColor(color(255,255,255))
		   .setColorCaptionLabel(color(0,0,0))
		   .setText(Integer.toString(CONTROLLER_ID[0]));
		;
		
		cp5.addTextfield("ID-2")
		   .setPosition(140,10)
		   .setSize(20,15)
		   .setColor(color(255,255,255))
		   .setColorCaptionLabel(color(0,0,0))
		   .setText(Integer.toString(CONTROLLER_ID[1]));
		;
		
		cp5.addButton("OK")
		   .setPosition(170,10)
		   .setSize(20,15)
		;
		
		checkbox = cp5.addCheckBox("checkBox").setPosition(220, 10)
				.setColorForeground(color(120)).setColorActive(color(200))
				.setColorLabel(color(0)).setSize(15, 15).setItemsPerRow(7)
				.setSpacingColumn(45).setSpacingRow(20).addItem("Pathos", 0)
				.addItem("Shine", 50).addItem("Flower", 100).addItem("Lamp", 150);
		

		pg = createGraphics(12, 5);
		pg2 = createGraphics(12, 5);

		
		node1 = new Node(this);
		node2 = new Node(this);
		node3 = new Node(this);
		node4 = new Node(this);
		node5 = new Node(this);
		node6 = new Node(this);
		node7 = new Node(this);

		node1.add(NODE1_LEDS);
		node2.add(NODE2_LEDS);
		node3.add(NODE3_LEDS);
		node4.add(NODE4_LEDS);
		node5.add(NODE5_LEDS);
		node6.add(NODE6_LEDS);
		node7.add(NODE7_LEDS);
		
		scp = new Pavillon(this, IP, PORT, CONTROLLER_ID);
		scp.add(node1, node2, node3, node4, node5, node6, node7);
		scp.setAdj();
		
		counter1=0;
		counter2=0;
		
		next=true;
		path = scp.breadthFirstSearch(scp.nozzleList.get(0), scp.nozzleList.get(19));
		
		pg_last = createGraphics(12,5);
		
		cp = new ColorPoint(this, color);
		dp = new DrawPath(path, cp);
		
		
		startHue = 120;

		for(Nozzle n : scp.nozzleList){
			  hsv1.add(new hsvGradient(this, n, startHue-2*n.id, 120, 120));

		}

		//color1 = (int) random(0,360);
		  //color2 = (int) random(0,360);
		  		
		scp.start();
		
		//Horizontal Shine
		/*for(int i=0; i<SHINE_MAX; i++) {
		colorMode(HSB, 360, 100, 100);
		color = color(50, 100, 100);
		nozzlePath = createRandomPath();
		horizontalShineList.add(new HorizontalShine(this, nozzlePath, color,(int)random(1,4)));
		//horizontalShineList.get(i).setUpShine();;
		}*/
		
		//Vertical Shine
		/*for(int i=0; i<SHINE_MAX; i++) {
		colorMode(HSB, 360, 100, 100);
		color = color(50, 0, 100);
		nozzlePath = createPath(i);
		verticalShineList.add(new VerticalShine(this, nozzlePath, color,1));
		//verticalShineList.get(i).setUpShine();;
		}*/
		
		cfFlower = new ColorFade(this, 220, 100, 100);
		cfFlower.hueFade(180, 2000);
		//cfFlower.brightnessFade(80, 1000);
		cfList.addColorFade(cfFlower);
		
		
		int h = 0;
		lampFade = new ColorFade(this, h, 100, 0);
		lampFade.hueFade(h+50, 1000);
		lampFade.brightnessFade(100, 1000);
		cfList.addColorFade(lampFade);


		/*colorFade1 = new ColorFade(this, 360, 100, 100);
		colorFade1.hueFade(0, 100000);
		colorFade1.brightnessFade(80, 1000);
		//colorFade1.start();
		
		colorFade2 = new ColorFade(this, 360, 100, 100);
		colorFade2.hueFade(0, 100000);
		colorFade2.brightnessFade(60, 1000);
		//colorFade2.start();
		
		colorFade3 = new ColorFade(this, 360, 100, 100);
		colorFade3.hueFade(0, 100000);
		colorFade3.brightnessFade(40, 1000);
		//colorFade3.start();
		
		colorFade4 = new ColorFade(this, 360, 100, 100);
		colorFade4.hueFade(0, 100000);
		colorFade4.brightnessFade(20, 1000);
		//colorFade4.start();
		
		colorFade5 = new ColorFade(this, 360, 100, 100);
		colorFade5.hueFade(0, 100000);
		colorFade5.brightnessFade(0, 1000);
		//colorFade5.start();
		
		colorFade6 = new ColorFade(this, 360, 100, 0);
		colorFade6.hueFade(0, 100000);
		colorFade6.brightnessFade(100, 1000);
		//colorFade6.start();
		
		colorFade7 = new ColorFade(this, 360, 100, 20);
		colorFade7.hueFade(0, 100000);
		colorFade7.brightnessFade(100, 1000);
		//colorFade7.start();
		
		colorFade8 = new ColorFade(this, 360, 100, 40);
		colorFade8.hueFade(0, 100000);
		colorFade8.brightnessFade(100, 1000);
		//colorFade8.start();
		
		colorFade9 = new ColorFade(this, 360, 100, 60);
		colorFade9.hueFade(0, 100000);
		colorFade9.brightnessFade(100, 1000);
		//colorFade9.start();
		
		colorFade10 = new ColorFade(this, 360, 100, 80);
		colorFade10.hueFade(0, 100000);
		colorFade10.brightnessFade(100, 1000);
		//colorFade10.start();
		
		
		colorFade11 = new ColorFade(this, 0, 100, 0);
		colorFade11.hueFade(50, 2000);
		colorFade11.brightnessFade(100, 2000);
		
		
		cfl = new ColorFadeList(this);
		cfl.addColorFade(colorFade1);
		cfl.addColorFade(colorFade2);
		cfl.addColorFade(colorFade3);
		cfl.addColorFade(colorFade4);
		cfl.addColorFade(colorFade5);
		cfl.addColorFade(colorFade6);
		cfl.addColorFade(colorFade7);
		cfl.addColorFade(colorFade8);
		//cfl.addColorFade(colorFade9);
		//cfl.addColorFade(colorFade10);
		cfl.addColorFade(colorFade11);
		cfl.start();
		
		//m = new GSMovie(this, "ko.avi");
		//m.loop();
		
		flash = true;
		
		ColorFade cf = new ColorFade(this, 180, 50, 30);
		cf.brightnessFade(60, 1000);
		cf.start();*/
		
		cfList.start();

		
		setupPathosLight();
		
		nozzlePath = createPath(0,1,2,3,4,5,6,7);
		horizontalMoveList.add(new Shine(this, nozzlePath));
		horizontalMoveList.add(new Glitter(this, nozzlePath));
		
		setupYellowBlue();

		nLayer = new NozzleLayer(this, scp, nozzlePath);
		layerGraphics = nLayer.getLayer();
	}
	
	//SETUP ARDUINO
	public void initArduino() {
		for (int i = 0; i < Serial.list().length; i++) {
			System.out.println("Device " + i + " " + Serial.list()[i]);
		}

		try {
			myPort = new Serial(this, Serial.list()[4], 9600);
			myPort.clear();
		} catch (Exception e) {
			System.out.println("Serial konnte nicht initialisiert werden");
		}
	}
	
	//SETUP PATHOSLIGHT
	public void setupPathosLight() {
		//create DiscoDots
		for(Nozzle n : scp.nozzleList) {
			discoDotList.add(new discoDot(this, n));
			}
		//create LampManager
		rlm = new RandomLampManager(this, scp, 3, 12);
		rlm2 = new RandomLampManager(this, scp, 3, 12);
	}
	
	//DRAW PATHOSLIGHT
	public void drawPathosLight() {
		//clear Old Frame
		//scp.clearSysA();
		//scp.dimm(80);
		//LampManager
				rlm.draw();
				rlm2.draw();
		
		//DiscoDots
		int timer = (int) random(15,15);
		for(discoDot d : discoDotList ) {
			if(frameCount%timer==0){
			d.update();
			}
			d.draw();
		}
		
	}
	
	//SETUP SHINE
	
	
	//DRAW SHINE
	public void drawShine() {
		for(Iterator<HorizontalMove> hMIterator = horizontalMoveList.iterator(); hMIterator.hasNext();){
			  HorizontalMove hM = hMIterator.next();
			  
			  hM.update();
			  hM.draw();
			  
			  if(hM.isDead()){
				  hMIterator.remove();
			  }
		  }

		  while(horizontalMoveList.size()<1){
			  nozzlePath = createPath(7,6,5,4,3,2,1,0);
			  //nozzlePath = createRandomPath();
			  horizontalMoveList.add(new Shine(this, nozzlePath));  
			  horizontalMoveList.add(new Glitter(this, nozzlePath)); 
		  }
	}
	
	// Called every time a new frame is available to read
	public void movieEvent(GSMovie movie) {
	    movie.read();
	  }

	public void discoDots() {
		int timer = (int) random(5,5);
		for(discoDot d : discoDotList ) {
			if(frameCount%timer==0){
			d.update();
			}
			d.draw();
		}
	}

	public void draw() {
		
		//colorMode(HSB,360,100,100);
		//background(lampFade.hue,lampFade.saturation,lampFade.brightness);
		  scp.clearSysA();

		  if(activeArray[0]){
			  drawPathosLight();
		  }
		  if(activeArray[1]){
			  drawShine();
		  }
		  if(activeArray[2]){
			  openFlower();
		  }
		  if(activeArray[3]){
			  NodeLamp();
		  }

		  drawYellowBlue();
		  
		  

		  layerGraphics.beginDraw();
		  layerGraphics.clear();
		  layerGraphics.colorMode(RGB);
		  
		  
		  
		  
		  
		  
		  
		  layerGraphics.beginDraw();
			layerGraphics.clear();
			layerGraphics.colorMode(HSB,360,100,100);
			  layerGraphics.fill(cfFlower.hue, cfFlower.saturation, cfFlower.brightness, 255);
			  layerGraphics.noStroke();
			  layerGraphics.rect(0, y, 100, -1);
			  
			  layerGraphics.fill(cfFlower.hue, cfFlower.saturation, cfFlower.brightness, 100);
			  layerGraphics.rect(0, y, 100, -2);
			  layerGraphics.fill(cfFlower.hue, cfFlower.saturation, cfFlower.brightness, 50);
			  layerGraphics.rect(0, y, 100, -3);

			  layerGraphics.endDraw();
			  
			  if(y<5) {
				  y += 0.1;
			  } else {
				  z ++;
				  if(z>1){
				  y = 0;
				  z=0;
				  cfFlower.hue=(int) random(0,60);
				  }
				  /*if(acc<0.2){
					  acc +=0.02;
				  } else {
					  acc = 0.05;
				  }*/
			  }
			 
			  
			  nLayer.add();
			  
			  
			  
		  /*layerGraphics.fill(255, 100, 50);
		  layerGraphics.noStroke();
		  layerGraphics.rect((int)x, 0, 1, 5);
		  layerGraphics.rect((int)x-5, 0, 1, 5);
		  layerGraphics.rect((int)x-10, 0, 1, 5);
		  layerGraphics.rect((int)x-15, 0, 1, 5);
		  layerGraphics.fill(255, 225);
		  layerGraphics.rect((int)x-1, 0, 1, 5);
		  layerGraphics.rect((int)x-6, 0, 1, 5);
		  layerGraphics.rect((int)x-11, 0, 1, 5);
		  layerGraphics.rect((int)x-16, 0, 1, 5);
		  layerGraphics.fill(255, 200);
		  layerGraphics.rect((int)x-2, 0, 1, 5);
		  layerGraphics.rect((int)x-7, 0, 1, 5);
		  layerGraphics.rect((int)x-12, 0, 1, 5);
		  layerGraphics.rect((int)x-17, 0, 1, 5);
		  layerGraphics.fill(255, 175);
		  layerGraphics.rect((int)x-3, 0, 1, 5);
		  layerGraphics.rect((int)x-8, 0, 1, 5);
		  layerGraphics.rect((int)x-13, 0, 1, 5);
		  layerGraphics.rect((int)x-18, 0, 1, 5);
		  layerGraphics.fill(255, 150);
		  layerGraphics.rect((int)x-4, 0, 1, 5);
		  layerGraphics.rect((int)x-9, 0, 1, 5);
		  layerGraphics.rect((int)x-14, 0, 1, 5);
		  layerGraphics.rect((int)x-19, 0, 1, 5);
		  layerGraphics.fill(255, 125);
		  layerGraphics.rect((int)x-5, 0, 1, 5);
		  layerGraphics.fill(255, 100);
		  layerGraphics.rect((int)x-6, 0, 1, 5);
		  layerGraphics.fill(255, 75);
		  layerGraphics.rect((int)x-7, 0, 1, 5);*/
		  
		  /*for(int j=0; j<1000; j++) {
		  for(int i=0; i<3; i++){
			  layerGraphics.noStroke();
			  layerGraphics.fill(255, 100, 50, 255-80*i);
			  layerGraphics.rect(100-(int)x-i-j*10, 0, 1, 5);
		  }
		  }*/
		  
		  
		  
		  
		  
		  /*for(int i=0; i<50; i++){
			  layerGraphics.noStroke();
			  layerGraphics.fill(255, 100, 50, 100-5*i);
			  layerGraphics.rect((int)x-i, 0, 1, 1);
			  layerGraphics.fill(255, 100, 50, 170-5*i);
			  layerGraphics.rect((int)x-i, 1, 1, 1);
			  layerGraphics.fill(255, 180, 50, 255-5*i);
			  layerGraphics.rect((int)x-i, 2, 1, 1);
			  layerGraphics.fill(255, 100, 50, 170-5*i);
			  layerGraphics.rect((int)x-i, 3, 1, 1);
			  layerGraphics.fill(255, 100, 50, 100-5*i);
			  layerGraphics.rect((int)x-i, 4, 1, 1);
		  }
		  
		  
		  if(x<layerGraphics.width) {
			  x = x*1.02+0.5;
		  }else{
			  x=0;
		  }*/
		 		 
		  
		  nLayer.add();
		  		  
		  //Draw on GUI  
		  node1.drawOnGui(10, 50);
		  node2.drawOnGui(150, 50);
		  node3.drawOnGui(300, 50);
		  node4.drawOnGui(450, 50);
		  node5.drawOnGui(600, 50);
		  node6.drawOnGui(750, 50);
		  node7.drawOnGui(900, 50);

	}
	
	public void NodeLamp() {
		layerGraphics.beginDraw();
		layerGraphics.colorMode(HSB,360,100,100);
		layerGraphics.noStroke();
		layerGraphics.fill(lampFade.hue, lampFade.saturation, lampFade.brightness);
		layerGraphics.rect(0, 0, layerGraphics.width, layerGraphics.height);
		layerGraphics.endDraw();
		nLayer.add();
	}

	public void openFlower(){
		layerGraphics.beginDraw();
		layerGraphics.clear();
		layerGraphics.colorMode(HSB,360,100,100);
		  layerGraphics.fill(cfFlower.hue, cfFlower.saturation, cfFlower.brightness, 255);
		  layerGraphics.noStroke();
		  layerGraphics.rect(0, y, 100, -5);
		  
		  layerGraphics.endDraw();
		  
		  if(y<5) {
			  y += 0.4;
		  } else {
			  z ++;
			  if(z>100){
			  y = 0;
			  z=0;
			  cfFlower.hue=(int) random(0,360);
			  }
			  /*if(acc<0.2){
				  acc +=0.02;
			  } else {
				  acc = 0.05;
			  }*/
		  }
		 
		  
		  nLayer.add();
	}
	
	public void easyColor() {
		
		scp.clearSysA();

		for(Iterator<HorizontalShine> shIterator = horizontalShineList.iterator(); shIterator.hasNext();){
		  HorizontalShine sh = shIterator.next();
		  
		  sh.updateShine();
		  sh.drawShine();
		  
		  if(sh.isDead()){
			  shIterator.remove();
		  }
	  }
	  
	  while(horizontalShineList.size()<SHINE_HOR_MAX){
		  nozzlePath = createPath(7,6,5,4,3,2,1,0);
		  //nozzlePath = createRandomPath(0, 17, 57, 65);
		  colorMode(HSB, 360, 100, 100);
		  color = color((int)random(0,20), 100, 100);
		  horizontalShineList.add(new HorizontalShine(this, nozzlePath, color, (int) random(2,2)));  
	  }
	}
	
	public LinkedList<Nozzle> createRandomPath(int r1_start, int r1_end, int r2_start, int r2_end) {
	LinkedList<Nozzle> randomPath = new LinkedList<Nozzle>();
	do{
      int r1=0;
	  int r2=0;
	  do{
	    r1 = (int)random(r1_start,r1_end);
	    r2 = (int)random(r2_start,r2_end);
	    //System.out.println("randomPath.size: "+randomPath.size());
	  }while(r1==r2 | r1>r2);
	  randomPath = scp.breadthFirstSearch(scp.nozzleList.get(r1), scp.nozzleList.get(r2));
	}while(randomPath.size()<5);
	return randomPath;
	}
	
	public LinkedList<Nozzle> createClosedPath() {
		LinkedList<Nozzle> randomPath = new LinkedList<Nozzle>();
		do{
	      int r1=0;
		  int r2=0;
		  do{
		    r1 = (int)random(0,16);
		    r2 = (int)random(0,65);
		    //System.out.println("randomPath.size: "+randomPath.size());
		  }while(r1==r2 | r1>r2);
		  randomPath = scp.breadthFirstSearch(scp.nozzleList.get(r1), scp.nozzleList.get(r2));
		}while(randomPath.size()<5);
		return randomPath;
		}
	
	public LinkedList<Nozzle> createPath(int...i) {
		LinkedList<Nozzle> path = new LinkedList<Nozzle>();
		for(int f : i){
		path.add(scp.nozzleList.get(f));
		}
		return path;
	}

	public void drawPathApplication() {
		if(dpList.size()<max_path){
			int r1=0;
			int r2=0;
			do{
			r1 = (int)random(0,0);
			r2 = (int)random(20,20);
			}while(r1==r2);
			LinkedList<Nozzle> randomPath = scp.breadthFirstSearch(scp.nozzleList.get(r1), scp.nozzleList.get(r2));
			ColorPoint cp = new ColorPoint(this, (int) random(180,240));
			dpList.add(new DrawPath(randomPath, cp));
		}
		
		for(Iterator<DrawPath> dpIterator = dpList.iterator(); dpIterator.hasNext();){
		//for(DrawPath dp : dpList){
			DrawPath dp = dpIterator.next();
			dp.update();
			dp.draw();
			
			if(dp.isDead()){
				  System.out.println("GO HERE");
				  dpIterator.remove();
				  //path = scp.breadthFirstSearch(scp.nozzleList.get(0), scp.nozzleList.get(19));
				  //cp = new ColorPoint(this, color);
				  //dp = new DrawPath(path, cp);  
			}
		}
	}
	
	public void setupYellowBlue() {
		cfYellow = new ColorFade(this, 270, 100, 100);
		cfYellow.saturationFade(0, 5000);
		cfYellow.brightnessFade(30, 2000);
		cfList.addColorFade(cfYellow);
	}
	
	public void drawYellowBlue() {
		for(Nozzle nozzle : scp.nozzleList) {
			PGraphics pg = nozzle.sysB;
			pg.beginDraw();
			for(int iy=0; iy<pg.height; iy++){
				pg.colorMode(HSB, 360, 100, 100);	
				pg.fill(cfYellow.hue-5*iy,cfYellow.saturation,cfYellow.brightness);
				pg.noStroke();
				pg.rect(0, iy, pg.width, 1);
		   }
			pg.endDraw();
		}
		//scp.setColor(cfYellow.hue, cfYellow.saturation, cfYellow.brightness);
	}
	
	public void yellowCold(){
		//Animate SystemB
	  	  counter2 = -400+((frameCount%800)*1);
	  	  int color1=60;
	  	  int color2=180;
	  	  int dimm=0; //(frameCount%300)/2;
		  if(counter2<=0){
		  for(Nozzle nozzle : scp.nozzleList){
			  pg2 = nozzle.sysA;
			  pg2.beginDraw();
			  pg2.colorMode(RGB);
			  pg2.background(255);
			  if(counter2<=-200){
				  //System.out.println("T1: "+(350-(counter2+400)));
				  for(int iy=0; iy<pg2.height; iy++){
						pg2.colorMode(HSB);	
						pg2.fill(color1-5*iy,200-(counter2+400),255,255);
						pg2.noStroke();
						pg2.rect(0, iy, pg.width, 1);
				   }
			  //pg2.background(counter2,0,0);
			  }else {
				  //System.out.println("T2: "+(50+counter2));
				  for(int iy=0; iy<pg2.height; iy++){
						pg2.colorMode(HSB);	
						pg2.fill(color2+5*iy,200+counter2,255,255);
						pg2.noStroke();
						pg2.rect(0, iy, pg.width, 1);
						pg2.colorMode(RGB);	
						pg2.fill(255,255,255,200-counter2);
						pg2.noStroke();
						pg2.rect(0, iy, pg.width, 1);
				   }
			  }
			  pg2.colorMode(RGB);
			  pg2.fill(0,0,0,dimm);
			  pg2.noStroke();
			  pg2.rect(0, 0, pg.width, pg.height);
			  pg2.endDraw();
			  
			  /*for(int ix=0; ix<pg2.width; ix++){
			  pg2.beginDraw();
			  pg2.colorMode(RGB);
			  pg2.fill(0,0,0,255);
			  pg2.rect(ix,y,1,1);
			  pg2.endDraw();
			  }*/
			  
		  }
		  }else{
		  //counter2 = (frameCount%100)*4;
		  for(Nozzle nozzle : scp.nozzleList){
			  pg2 = nozzle.sysA;
			  pg2.beginDraw();
			  pg2.colorMode(RGB);
			  pg2.background(0);
			  if(counter2<200){
				  //System.out.println("T3: "+(50-counter2));
				  for(int iy=0; iy<pg2.height; iy++){
						pg2.colorMode(HSB);	
						pg2.fill(color2+5*iy,200-counter2,255,255);
						pg2.noStroke();
						pg2.rect(0, iy, pg.width, 1);
						pg2.colorMode(RGB);	
						pg2.fill(255,255,255,200-counter2);
						pg2.noStroke();
						pg2.rect(0, iy, pg.width, 1);
				   }
			  //pg2.background(counter2,0,0);
			  }else {
				  //System.out.println("T4: "+(-50+counter2));
				  for(int iy=0; iy<pg2.height; iy++){
						pg2.colorMode(HSB);	
						pg2.fill(color1-5*iy,-200+counter2,255,255);
						pg2.noStroke();
						pg2.rect(0, iy, pg.width, 1);
				   }
			  }
			  pg2.colorMode(RGB);
			  pg2.fill(0,0,0,0);
			  pg2.noStroke();
			  pg2.rect(0, 0, pg.width, pg.height);
			  pg2.endDraw();
			  
		  }
		  
		 }
	  scp.dimm(100);
	}

	//ARDUINO SERIAL EVENT
	public void serialEvent(Serial myPort) {
		try {
			myString = myPort.readStringUntil(lf);
			if (myString != null) {
				String[] spl1 = split(myString, '\n');
				spl1 = split(spl1[0], ':');

				for (int i = 0; i < spl1.length; i++) {
					String[] spl2 = split(spl1[i], ',');
					if (spl2.length >= 2) {
						int posX = parseWithDefault(spl2[0], 0);
						int posY = parseWithDefault(spl2[1], 0);
						System.out.println(posX);
						System.out.println(posY);
					}
				}
			}
		} catch (Exception e) {
			println("Initialization exception");
		}
	}
	
	// Auxiliary function for parsing Arduino Data
	public static int parseWithDefault(String number, int defaultVal) {
		try {
			return Integer.parseInt(number);
		} catch (NumberFormatException e) {
			return defaultVal;
		}
	}
	
	
	@SuppressWarnings("deprecation")
	public void controlEvent(ControlEvent theEvent) {
		if(theEvent.getGroup().getName() == "checkBox") {
		    print("got an event from "+theEvent.getName()+"\t");
		    System.out.println(theEvent.getGroup().getArrayValue().length);
		    if(checkbox_array[0] != theEvent.getGroup().getArrayValue(0)) {
		    	activeArray[0] =! activeArray[0];
		    	System.out.println("BUTTON1");
		    } else if(checkbox_array[1] != theEvent.getGroup().getArrayValue(1)) {
		    	activeArray[1] =! activeArray[1];
		    	System.out.println("BUTTON2");
		    } else if(checkbox_array[2] != theEvent.getGroup().getArrayValue(2)) {
		    	activeArray[2] =! activeArray[2];
		    	System.out.println("BUTTON3");
		    } else if(checkbox_array[3] != theEvent.getGroup().getArrayValue(3)) {
		    	activeArray[3] =! activeArray[3];
		    	System.out.println("BUTTON3");
		    }
		    
		}
		    println("\t "+theEvent.getValue());
		    System.out.println("ACTIVE_ARRAY: "+activeArray[0]+" "+activeArray[1]);
		    checkbox_array = checkbox.getArrayValue();
		    scp.clearSysA();
		    scp.clearSysB();
	}

		// function buttonA will receive changes from 
		// controller with name buttonA
	public void OK(int theValue) {
		  println("a button event from buttonA: "+theValue);
		  scp.setIP_ADRESS(cp5.get(Textfield.class,"IP").getText());
		  scp.setPORT(Integer.parseInt(cp5.get(Textfield.class,"PORT").getText()));
		  int[] id = {Integer.parseInt(cp5.get(Textfield.class,"ID-1").getText()),
				      Integer.parseInt(cp5.get(Textfield.class,"ID-2").getText())};
		  scp.setID(id);
	}

}