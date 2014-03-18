/*
 * Created by Marius Hoggenm�ller on 04.02.14
 * Copyright (c) 2014 Marius Hoggenm�ller. All rights reserved.
 * 
 */


import java.awt.Color;
import java.util.AbstractList;
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
	float checkbox_array[] = {0,0,0,0,0,0,0,0};
	boolean activeArray [] = {false, false, false, false, false, false, false, false};
	
	//Arduino Communication
	static final String ARDUINO_DEVICE = "/dev/tty.usbmodemda121";
	static int lf = 10; // Linefeed in ASCII
	String myString = null; // Serial Output String
	Serial myPort; // Serial port you are using

	//Sensors
	private ArrayList<Sensor> sensorList = new ArrayList<Sensor>();
	private static int SENSORS = 5;

	//Animation Stuff
	private PGraphics pg, pg2;

	private int counter2;


	private float y=0;

	private ArrayList<hsvGradient> hsv1 = new ArrayList<hsvGradient>();

	private int startHue;
	
	//DrawPathApplication
	private ArrayList<DrawPath> dpList= new ArrayList<DrawPath>();
	int max_path = 1;
	
	//Shine
	private static int SHINE_HOR_MAX = 1;
	private static int SHINE_VERT_MAX = 8;
	//private ArrayList<HorizontalShine> horizontalShineList = new ArrayList<HorizontalShine>();

	private LinkedList<Nozzle> nozzlePath;

	int h=0;
	
	private GSMovie m;

	private ArrayList<discoDot> discoDotList = new ArrayList<discoDot>();
	
	double k = 1.0;

	private RandomLampManager rlm, rlm2;

	private ArrayList<HorizontalMove> horizontalMoveList = new ArrayList<HorizontalMove>();

	private ColorFade cfYellow;

	private ColorFadeList cfList = new ColorFadeList(this);

	private NozzleLayer nLayer;

	private PGraphics layerGraphics;
	
	double x=0.1;
	double acc=0.05;

	private ColorFade cfFlower;

	private int z = 0;

	private ColorFade lampFade;

	private ColorFade sTube;
	
	private ArrayList<SimpleTube> sTubeList = new ArrayList<SimpleTube>();

	private ColorFade sTube2;

	private ColorFadeList colorTubeList = new ColorFadeList(this);

	private ColorFade hueFade;

	private NozzleLayer lampLayer;

	private NozzleLayer flowerLayer;

	private PGraphics flowerLayerGraphics;

	private ColorFade pushColor;

	private boolean up = true;

	private int alpha = 50;

	private int value1;

	private int value2;

	private boolean alphaUP = true;

	private ArrayList<Pendulum> pendulumList = new ArrayList<Pendulum>();

	private ArrayList<Strobo> stroboList = new ArrayList<Strobo>();

	private int[] flashArray = new int[5];

	private int flashTimer;

	private boolean flashActive;

	private ControlP5 cp5Sensor;
	private ArrayList<CheckBox> checkBoxSensorList = new ArrayList<CheckBox>();
	private float[][] checkBoxSensorArray= {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0}};
    private boolean flashActiveArray[] = {true,true,true,true,true};
    private boolean wiActiveArray[] = {true,true,true,true,true};
    
    private ArrayList<Effect> effectList = new ArrayList<Effect>();
    
    private ArrayList<Node> nodeList = new ArrayList<Node>();

	private ArrayList<Lamp> lampList = new ArrayList<Lamp>();

	private ColorFade backGround;
	
	private int[] Farbe = new int[3]; 
    
	//Initiate as Application
	public static void main(String args[]) {
	    PApplet.main(new String[] { "--present", "ProcessingMain" });
	  }
	
	public void setup() {
		
		size(1200,750);
	
		initArduino();
		  
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
		
		cp5.addButton("SETUP")
			.setPosition(1050, 10)
			.setSize(20,15)
		;
		
		checkbox = cp5.addCheckBox("checkBox").setPosition(220, 10)
				.setColorForeground(color(120)).setColorActive(color(200))
				.setColorLabel(color(0,0,255)).setSize(15, 15).setItemsPerRow(7)
				.setSpacingColumn(45).setSpacingRow(20).addItem("Tube", 0)
				.addItem("Pathos", 50).addItem("Shine", 100).addItem("Lamp", 150)
				.addItem("Flower", 200).addItem("HueFade", 250).addItem("Pendulum", 300).addItem("Push", 350);		

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
		
		nodeList.add(node1);
		nodeList.add(node2);
		nodeList.add(node3);
		nodeList.add(node4);
		nodeList.add(node5);
		nodeList.add(node6);
		nodeList.add(node7);
		
		scp = new Pavillon(this, IP, PORT, CONTROLLER_ID);
		scp.add(node1, node2, node3, node4, node5, node6, node7);
		scp.setAdj();
		scp.start();
		
		cp5Sensor = new ControlP5(this);
		
		for(int i=0; i<SENSORS; i++){
			sensorList.add(new Sensor(this, i+1, 1050, 50+i*130));
			String name = "checkBoxSensor"+i;
			String nameF = "Flash"+i;
			String nameW = "Wi"+i;
			checkBoxSensorList.add(cp5Sensor.addCheckBox(name).setPosition(1050, 55+i*130)
					.setColorForeground(color(120)).setColorActive(color(200))
					.setColorLabel(color(0,0,0)).setSize(15, 15).setItemsPerRow(7)
					.setSpacingColumn(45).setSpacingRow(15).addItem(nameF, 0)
					.addItem(nameW, 50));
		
		}
				
		
		
		counter2=0;
								
		startHue = 120;

		for(Nozzle n : scp.nozzleList){
			  hsv1.add(new hsvGradient(this, n, startHue-2*n.id, 120, 120));
		}
		
		//m = new GSMovie(this, "ko.avi");
		//m.loop();
		
		
		setupPathosLight();
		setupYellowBlue();
		setupHueBackground();		
		setUpLamp();
		setupFlower();
		setupSimpleTube();
		setupPush();
		setupBackGroundContrast();
		setupCompTube();
		
		//frameRate(10);
		cfList.start();
		
		nozzlePath = createPath(4,3,2,1,0);
		HLayer nLayer = new HLayer(this, scp, nozzlePath);
		stroboList.add(new Strobo(this, nLayer, 25));
		
	}
		
	//SETUP ARDUINO
	public void initArduino() {
		for (int i = 0; i < Serial.list().length; i++) {
			System.out.println("Device " + i + " " + Serial.list()[i]);
		}

		try {
			myPort = new Serial(this, "/dev/tty.usbmodemfa121", 14400);
			myPort.clear();
		} catch (Exception e) {
			System.out.println("Serial konnte nicht initialisiert werden");
		}
	}
	
	//DRAW SIMPLETUBE
	public void drawPendulum(){
	for(Iterator<Pendulum> pTIterator = pendulumList .iterator(); pTIterator.hasNext();){
		  Pendulum p = pTIterator.next();
		  
		  p.draw();
		  
		  if(p.isDead()){
			  //System.out.println("DEAD");
			  pTIterator.remove();
		  }
	  }
	  
	  while(pendulumList.size()<1){
		  nozzlePath = createPath(4,3,2,1,0);
		  //nozzlePath = createRowPath(0,65);
		  HLayer nLayer = new HLayer(this, scp, nozzlePath);
		  pendulumList.add(new Pendulum(nLayer));
	  }
	}
	
	//DRAW SIMPLETUBE
	public void drawLamp(){
		for(Iterator<Lamp> lampIterator = lampList  .iterator(); lampIterator.hasNext();){
			  Lamp l = lampIterator.next();
			  
			  l.draw();
			  
			  if(l.isDead()){
				  //System.out.println("DEAD");
				  lampIterator.remove();
			  }
		  }
		  
		  while(lampList.size()<5){
			  nozzlePath = createNodePath(nodeList.get((int)random(0,7)));
			  HLayer nLayer = new HLayer(this, scp, nozzlePath);
			  lampList.add(new Lamp(this, nLayer, cfList, (int) random(1000,1000)));
		  }
		}
		
	//SETUP PUSH
	public void setupPush(){
		pushColor = new ColorFade(this, 360, 100, 100);
		pushColor.hueFade(140, 10000);
		cfList.addColorFade(pushColor);
	}
	
	//DRAW PUSH
	public void drawPush(){
		for(Nozzle n : scp.nozzleList){
			PGraphics pg = n.sysA;
			pg.beginDraw();
			pg.colorMode(HSB,360,100,100,100);
			pg.noStroke();
			pg.fill(pushColor.hue-n.id, pushColor.saturation, 100-alpha);
			//pg.rect(0, y-1, pg.width, 1);
			pg.fill(pushColor.hue-n.id, pushColor.saturation, alpha);
			pg.rect(0, y, pg.width, 1);
			pg.endDraw();
		}
		
		if(up){
		if(alphaUP ){
		if(y==0){
		alpha+=13;
		}else if(y==1){
		alpha+=11;
		}else if(y==2){
		alpha+=9;
		}else if(y==3){
		alpha+=7;
		}else if(y==4){
		alpha+=3;
		}
		}else{
		if(y==0){
		alpha-=13;
		}else if(y==1){
		alpha-=11;
		}else if(y==2){
		alpha-=9;
		}else if(y==3){
		alpha-=7;
		}else if(y==4){
		alpha-=3;
		}
		}
		}else{
		if(alphaUP ){
		if(y==0){
		alpha+=13;
		}else if(y==1){
		alpha+=11;
		}else if(y==2){
		alpha+=9;
		}else if(y==3){
		alpha+=7;
		}else if(y==4){
		alpha+=3;
		}
		}else{
		if(y==0){
		alpha-=13;
		}else if(y==1){
		alpha-=11;
		}else if(y==2){
		alpha-=9;
		}else if(y==3){
		alpha-=7;
		}else if(y==4){
		alpha-=3;
		}
		}	
		}
		if(alpha>=100){
			alphaUP = false;
		}
		//if(frameCount%10==0){
		if(!alphaUP && alpha<=80){
			alphaUP = true;
			alpha=20;
		if(up){
			y+=1;
		}else{
			y-=1;
		}
		}
		//}
		if(y<=0){
			up = true;
			value1=100;
			value2=0;
		}
		if(y>=4){
			up=false;
			value1=0;
			value2=100;
		}
	}
	
	//SETUP FLOWER
	public void setupFlower(){
		cfFlower = new ColorFade(this, 220, 100, 100);
		cfFlower.hueFade(180, 2000);
		cfList.addColorFade(cfFlower);
		nozzlePath = createPath(7,6,5,4,3,2,1,0);
		flowerLayer = new NozzleLayer(this, scp, nozzlePath);
		flowerLayerGraphics = flowerLayer.getLayer();
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
		int timer = (int) random(10,10);
		for(discoDot d : discoDotList ) {
			if(frameCount%timer==0){
			d.update();
			}
			d.draw();
		}
		
	}
		
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

		  while(horizontalMoveList.size()<0){
			  nozzlePath = createPath(7,6,5,4,3,2,1,0);
			  //nozzlePath = createRandomPath();
			  horizontalMoveList.add(new Shine(this, nozzlePath));  
			  horizontalMoveList.add(new Glitter(this, nozzlePath)); 
		  }
	}
	
	//SETUP SIMPLETUBE
	public void setupSimpleTube(){
		sTube = new ColorFade(this, 0, 100, 100);
		sTube.hueFade(h+50, 1000);
		
		sTube2 = new ColorFade(this, 0, 100, 50);
		sTube2.alphaFade(40, 1000);
		
		colorTubeList.start();
		
		colorTubeList.addColorFade(sTube);
		colorTubeList.addColorFade(sTube2);
	}
	
	//DRAW SIMPLETUBE
	public void drawSimpleTube(){
	for(Iterator<SimpleTube> sTIterator = sTubeList.iterator(); sTIterator.hasNext();){
		  SimpleTube sT = sTIterator.next();
		  
		  sT.draw();
		  
		  if(sT.isDead()){
			  //System.out.println("DEAD");
			  sTIterator.remove();
		  }
	  }
	  
	  while(sTubeList.size()<1){
		  nozzlePath = createRowPath(0,65);
		  //nozzlePath = createRandomPath(0,8,58,65);
		  NozzleLayer nLayer = new NozzleLayer(this, scp, nozzlePath);
		  sTubeList.add(new SimpleTube(this, nLayer, colorTubeList.get((int)random(0,2)), (int)random(20,100), 1+Math.random()*0.5));
	  }
	}
	
	//SETUP COMP SIMPLETUBE
		public void setupCompTube(){
			float[] hsb = Color.RGBtoHSB(abs(Farbe[0]-255), abs(Farbe[1]-255), abs(Farbe[2]-255), null);
			float hue = hsb[0]*360;          // .58333
			float saturation = hsb[1]*100;   // .66667
			float brightness = hsb[2]*100;   // .6
			
			sTube = new ColorFade(this, (int)hue, (int)saturation, (int)brightness);
			//sTube.hueFade((int)hue+50, 1000);
			
			sTube2 = new ColorFade(this, (int)hue, (int)10, (int)brightness);
			//sTube.hueFade(h+50, 1000);
			
			
			cfList.addColorFade(sTube);
			cfList.addColorFade(sTube2);
		}
		
		//DRAW COMP SIMPLETUBE
		public void drawCompSimpleTube(){
		for(Iterator<SimpleTube> sTIterator = sTubeList.iterator(); sTIterator.hasNext();){
			  SimpleTube sT = sTIterator.next();
			  
			  sT.draw();
			  
			  if(sT.isDead()){
				  //System.out.println("DEAD");
				  sTIterator.remove();
			  }
		  }
		  
		  while(sTubeList.size()<1){
			  //nozzlePath = createRandomPath(0,8,58,65);
			  nozzlePath = createPath(7,6,5,4,3,2,1,0);
			  NozzleLayer nLayer = new NozzleLayer(this, scp, nozzlePath);
			  sTubeList.add(new SimpleTube(this, nLayer, sTube, (int)random(50,150), 1+Math.random()*0.5));
			  nozzlePath = createPath(0,1,2,3,4,5,6,7);
			  nLayer = new NozzleLayer(this, scp, nozzlePath);
			  sTubeList.add(new SimpleTube(this, nLayer, sTube2, (int)random(50,150), 1+Math.random()*0.5));
		  }
		}
	
	//SETUP HUEBACKGROUND
	public void setupHueBackground(){
		hueFade = new ColorFade(this, 240, 90, 90);
		hueFade.hueFade(150, 5000);
		hueFade.brightnessFade(40, 10000);
		cfList.addColorFade(hueFade);
	}
	
	public void drawHueBackground(){
		for(Nozzle n : scp.nozzleList){
			PGraphics pg = n.sysA;
			pg.beginDraw();
			pg.colorMode(HSB, 360, 100, 100);
			pg.noStroke();
			pg.fill(hueFade.hue-n.id, hueFade.saturation, hueFade.brightness);
			pg.rect(0, 0, pg.width, pg.height);
			pg.endDraw();
		}
	}
	
	public void setupBackGroundContrast(){
		Farbe[0] = (int) random(0,255);
		Farbe[1] = (int) random(0,255);
		Farbe[2] = (int) random(0,255);
		float[] hsb = Color.RGBtoHSB(Farbe[0], Farbe[1], Farbe[2], null);
		float hue = hsb[0]*360;          // .58333
		float saturation = hsb[1]*100;   // .66667
		float brightness = hsb[2]*100;   // .6
		System.out.println(hue+" "+saturation+" "+brightness+" ");
		backGround = new ColorFade(this, (int)hue, (int)saturation, (int)brightness);
		backGround.hueFade((int)hue+30, 3000);
		backGround.brightnessFade(50, 3000);
		cfList.addColorFade(backGround);
	}
	
	public void drawBackGroundContrast(){
		
		for(Nozzle n : scp.nozzleList){
			PGraphics pg = n.sysB;
			pg.beginDraw();
			pg.colorMode(HSB, 360, 100, 100);
			pg.noStroke();
			pg.fill(backGround.hue+(int)(0.5*n.id), backGround.saturation, backGround.brightness);
			pg.rect(0, 0, pg.width, pg.height);
			pg.endDraw();
		}
		
		/*nozzlePath = createNodePath(node1);
		NozzleLayer nLayer = new NozzleLayer(this, scp, nozzlePath);
		PGraphics pg = nLayer.getLayer();
		pg.beginDraw();
		pg.colorMode(HSB, 360, 100, 100);
		pg.noStroke();
		pg.fill(backGround.hue, backGround.saturation, backGround.brightness);
		pg.rect(0, 0, pg.width, pg.height);
		pg.endDraw();
		
		nLayer.add();
		nozzlePath = createNodePath(node2);
		nLayer = new NozzleLayer(this, scp, nozzlePath);
		pg = nLayer.getLayer();
		pg.beginDraw();
		pg.colorMode(HSB, 360, 100, 100);
		pg.noStroke();
		pg.fill(backGround.hue+5, backGround.saturation, backGround.brightness);
		pg.rect(0, 0, pg.width, pg.height);
		pg.endDraw();
		nLayer.add();
		
		nLayer.add();
		nozzlePath = createNodePath(node3);
		nLayer = new NozzleLayer(this, scp, nozzlePath);
		pg = nLayer.getLayer();
		pg.beginDraw();
		pg.colorMode(HSB, 360, 100, 100);
		pg.noStroke();
		pg.fill(backGround.hue+10, backGround.saturation, backGround.brightness);
		pg.rect(0, 0, pg.width, pg.height);
		pg.endDraw();
		nLayer.add();
		
		nLayer.add();
		nozzlePath = createNodePath(node4);
		nLayer = new NozzleLayer(this, scp, nozzlePath);
		pg = nLayer.getLayer();
		pg.beginDraw();
		pg.colorMode(HSB, 360, 100, 100);
		pg.noStroke();
		pg.fill(backGround.hue+15, backGround.saturation, backGround.brightness);
		pg.rect(0, 0, pg.width, pg.height);
		pg.endDraw();
		nLayer.add();
		
		nLayer.add();
		nozzlePath = createNodePath(node5);
		nLayer = new NozzleLayer(this, scp, nozzlePath);
		pg = nLayer.getLayer();
		pg.beginDraw();
		pg.colorMode(HSB, 360, 100, 100);
		pg.noStroke();
		pg.fill(backGround.hue+20, backGround.saturation, backGround.brightness);
		pg.rect(0, 0, pg.width, pg.height);
		pg.endDraw();
		nLayer.add();
		
		nLayer.add();
		nozzlePath = createNodePath(node6);
		nLayer = new NozzleLayer(this, scp, nozzlePath);
		pg = nLayer.getLayer();
		pg.beginDraw();
		pg.colorMode(HSB, 360, 100, 100);
		pg.noStroke();
		pg.fill(backGround.hue+25, backGround.saturation, backGround.brightness);
		pg.rect(0, 0, pg.width, pg.height);
		pg.endDraw();
		nLayer.add();
		
		nLayer.add();
		nozzlePath = createNodePath(node7);
		nLayer = new NozzleLayer(this, scp, nozzlePath);
		pg = nLayer.getLayer();
		pg.beginDraw();
		pg.colorMode(HSB, 360, 100, 100);
		pg.noStroke();
		pg.fill(backGround.hue+30, backGround.saturation, backGround.brightness);
		pg.rect(0, 0, pg.width, pg.height);
		pg.endDraw();
		nLayer.add();*/
	}
	
	//SETUP LAMP
	public void setUpLamp() {
		nozzlePath = createPath(7,6,5,4,3,2,1,0);
		lampLayer = new NozzleLayer(this, scp, nozzlePath);
		layerGraphics = lampLayer.getLayer();
		int h = 0;
		lampFade = new ColorFade(this, h, 100, 0);
		lampFade.hueFade(h+50, 1000, 2);
		lampFade.brightnessFade(100, 1000, 2);
		cfList.addColorFade(lampFade);
	}
	
	//DRAW LAMO
	public void NodeLamp() {
		layerGraphics.beginDraw();
		layerGraphics.colorMode(HSB,360,100,100);
		layerGraphics.noStroke();
		layerGraphics.fill(lampFade.hue, lampFade.saturation, lampFade.brightness);
		layerGraphics.rect(0, 0, layerGraphics.width, layerGraphics.height);
		layerGraphics.endDraw();
		lampLayer.add();
	}
	
	// Called every time a new frame is available to read
	public void movieEvent(GSMovie movie) {
	    movie.read();
	  }

	public void discoDots() {
		int timer = (int) random(1,1);
		for(discoDot d : discoDotList ) {
			if(frameCount%timer==0){
			d.update();
			}
			d.draw();
		}
	}

	public void draw() {
		
		//System.out.println(cfList.colorFadeList.size());
		//colorMode(HSB,360,100,100);
		//background(lampFade.hue,lampFade.saturation,lampFade.brightness);
		
		if(abs(millis()-flashTimer)>=20 && flashActive){
			int minValue = flashArray[0];  
			int minPos = 0;
			  for(int i=1;i<flashArray.length;i++){  
			    if(flashArray[i] < minValue){  
			      minValue = flashArray[i];
			      minPos = i;
			    }  
			  }  
			for(int i=0; i<flashArray.length; i++){
				flashArray[i] = 1023;
			}
			flashActive = false;
			System.out.println("FLASH mit "+(minPos+1)+" "+minValue);
			sensorList.get(minPos).setFlash();
		}
		
		for(Sensor s : sensorList){
			//System.out.println("SENSOR: "+s.getID()+" STATE: "+s.getState());
			if(s.getState()){
				if(activeArray[6]){
				nozzlePath = createNodePath(nodeList.get(s.getID()-1));
				HLayer nLayer = new HLayer(this, scp, nozzlePath);
				effectList.add(new Pendulum(nLayer));
				s.setState(false);
				}else if(activeArray[3]){
				nozzlePath = createNodePath(nodeList.get(s.getID()-1));
				HLayer nLayer = new HLayer(this, scp, nozzlePath);
				lampList.add(new Lamp(this, nLayer, cfList, (int)random(300,3000)));
				s.setState(false);
				}else{
				nozzlePath = createNodePath(nodeList.get(s.getID()-1));
				HLayer nLayer = new HLayer(this, scp, nozzlePath);
				effectList.add(new Strobo(this, nLayer, 25));
				s.setState(false);
				}
			}
		}
	

		//if(frameCount%50<250){
		scp.dimm(30);
		//}else{
		//scp.clearSysA();
		//}
		if(frameCount%200==0){
			setupBackGroundContrast();
			setupCompTube();
		}
		drawBackGroundContrast();

		
		//scp.clearSysA();

		  if(activeArray[5]){
			  drawHueBackground();
		  }
		  if(activeArray[0]){
			  drawSimpleTube();
			  //drawCompSimpleTube();
		  }
		  if(activeArray[1]){
			  drawPathosLight();
		  }
		  if(activeArray[2]){
			  drawShine();
		  }
		  if(activeArray[3]){
			  drawLamp();
		  }
		  if(activeArray[4]){
			  openFlower();
		  }
		  if(activeArray[6]){
			  drawPendulum();
		  }
		  if(activeArray[7]){
			  drawPush();
		  }
		  
		  //drawYellowBlue();
		  		  
		  //drawLamp();
		  
		  if(frameCount%100==0){
		  System.out.println(frameRate);
		  }	 
		 
		  
		  /*for(Sensor s : sensorList){
			  System.out.println(s.toString());
		  }*/

		  /*for(NozzleLayer nL : nLayerList){
			  nL.add();
		  }*/
		  
		  //drawPush();
		  //drawPendulum();
		  
		  /*layerGraphics.beginDraw();
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
				  if(acc<0.2){
					  acc +=0.02;
				  } else {
					  acc = 0.05;
				  }
			  }
			 
			  
			  nLayer.add();*/
			  
			  
			  
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
		 		 
		  
		  //nLayer.add();
		  		  
		  //Draw on GUI  
		  
		  
		  for(Iterator<Effect> effectIterator = effectList.iterator(); effectIterator.hasNext();){
			  Effect e = effectIterator.next();
			  
			  e.draw();
			  
			  if(e.isDead()){
				  //System.out.println("DEAD");
				  effectIterator.remove();
			  }
		  }
		  
		  
		  
		  /*for(Effect e : effectList){
			  System.out.println("GO EFFEKT LIST");
				e.draw();
				
		  }*/
		  
		  colorMode(RGB);
		  background(255);
		  		  
		  node1.drawOnGui(10, 50);
		  node2.drawOnGui(150, 50);
		  node3.drawOnGui(300, 50);
		  node4.drawOnGui(450, 50);
		  node5.drawOnGui(600, 50);
		  node6.drawOnGui(750, 50);
		  node7.drawOnGui(900, 50);
		  
		  for(Sensor s : sensorList){
			  s.drawOnGui();
		  }
		  
		  fill(0);
		  text("Created by Marius Hoggenm�ller on 04.02.14. Copyright (c) 2014 Marius Hoggenm�ller, LMU Munich. All rights reserved.", 10, 740);

	}
	
	

	public void openFlower(){
		flowerLayerGraphics.beginDraw();
		flowerLayerGraphics.clear();
		flowerLayerGraphics.colorMode(HSB,360,100,100);
		flowerLayerGraphics.fill(cfFlower.hue, cfFlower.saturation, cfFlower.brightness, 255);
		flowerLayerGraphics.noStroke();
		flowerLayerGraphics.rect(0, y, 100, -5);
		  
		flowerLayerGraphics.endDraw();
		  
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
		 
		  
		  flowerLayer.add();
	}
	
	public void easyColor() {
		
		/*scp.clearSysA();

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
	  }*/
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
	
	public LinkedList<Nozzle> createRowPath(int i, int j) {
		LinkedList<Nozzle> path = new LinkedList<Nozzle>();
		if(i>j){
			for(int z=i; z>j; z--){
				path.add(scp.nozzleList.get(z));
			}
		}else{
			for(int z=i; z<j; z++){
				path.add(scp.nozzleList.get(z));
			}
		}
		return path;
	}
	
	public LinkedList<Nozzle> createNodePath(Node node){
		LinkedList<Nozzle> path = new LinkedList<Nozzle>();
		for(Nozzle n : node.nozzleList){
			path.add(n);
		}
		return path;
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
			System.out.println(myString);
			if (myString != null) {
				String[] spl1 = split(myString, '\n');
				String[] spl2 = split(spl1[0], '/');
				//System.out.println(spl1[0]);

				if(spl2[0].compareTo("FLASH")==0){
					System.out.println("GO: "+spl2[1]);
					spl2 = split(spl2[1], '-');
					int id = Integer.parseInt(spl2[0]);
					spl2 = split(spl2[1], ',');
					int value = Integer.parseInt(spl2[0]);
					/*if(horizontalMoveList.size()<1){
						nozzlePath = createPath(7,6,5,4,3,2,1,0);
						//nozzlePath = createRandomPath();
						horizontalMoveList.add(new Shine(this, nozzlePath));  
						horizontalMoveList.add(new Glitter(this, nozzlePath)); 
					}*/
					/*if(stroboList.size()<1){
						nozzlePath = createPath(7,6,5,4,3,2,1,0);
						HLayer nLayer = new HLayer(this, scp, nozzlePath);
						stroboList.add(new Strobo(this, nLayer, 25));  
					}
					if(pendulumList.size()<1){
						nozzlePath = createPath(4,3,2,1,0);
						HLayer nLayer = new HLayer(this, scp, nozzlePath);
						pendulumList.add(new Pendulum(nLayer));
					}*/
					//System.out.println("FLASH mit "+id+" value: "+value);
					flashArray [id-1]=value;
					flashActive = true;
					flashTimer = millis();
				}
				if(spl2[0].compareTo("WII")==0){
					System.out.println("GO");
					spl2 = split(spl2[1], '-');
					int id = Integer.parseInt(spl2[0]);
					spl2 = split(spl2[1], ':');
					int[] posX = new int[spl2.length];
					int[] posY = new int[spl2.length];
					String output = "WII mit "+id+" ";
					for (int i = 0; i < spl2.length; i++) {
						String[] spl3 = split(spl2[i], ',');
						if (spl2.length >= 2) {
							posX[i] = parseWithDefault(spl3[0], 0);
							posY[i] = parseWithDefault(spl3[1], 0);
						}
						output += posX[i]+":"+posY[i]+",";
					}
					System.out.println("OUTPUT: "+output);
					sensorList.get(id-1).setWi(posX, posY); 
					/*if(horizontalMoveList.size()<1){
					nozzlePath = createPath(7,6,5,4,3,2,1,0);
					  //nozzlePath = createRandomPath();
					  horizontalMoveList.add(new Shine(this, nozzlePath));  
					  horizontalMoveList.add(new Glitter(this, nozzlePath)); 
					}*/
					/*if(stroboList.size()<1){
						nozzlePath = createPath(7,6,5,4,3,2,1,0);
						HLayer nLayer = new HLayer(this, scp, nozzlePath);
						stroboList.add(new Strobo(this, nLayer, 25));  
					}
					if(pendulumList.size()<1){
						nozzlePath = createPath(4,3,2,1,0);
						HLayer nLayer = new HLayer(this, scp, nozzlePath);
						pendulumList.add(new Pendulum(nLayer));
					}*/
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
		
		if (theEvent.isFrom(checkBoxSensorList.get(0))) {
			if(checkBoxSensorArray[0][0] != checkBoxSensorList.get(0).getArrayValue(0)){
				flashActiveArray[0] =! flashActiveArray[0];
				System.out.println("SENSOR0"+checkBoxSensorList.get(0).getArrayValue(0));
				sendArduinoFlash(myPort);
			} else if(checkBoxSensorArray[0][1] != checkBoxSensorList.get(0).getArrayValue(1)){
				wiActiveArray[0] =! wiActiveArray[0];
				System.out.println("SENSOR0"+checkBoxSensorList.get(0).getArrayValue(1));
				sendArduinoWi(myPort);
			}
			checkBoxSensorArray[0] = checkBoxSensorList.get(0).getArrayValue();
		} else if (theEvent.isFrom(checkBoxSensorList.get(1))) {
			if(checkBoxSensorArray[1][0] != checkBoxSensorList.get(1).getArrayValue(0)){
				flashActiveArray[1] =! flashActiveArray[1];
				System.out.println("SENSOR1"+checkBoxSensorList.get(1).getArrayValue(0));
				sendArduinoFlash(myPort);
			} else if(checkBoxSensorArray[1][1] != checkBoxSensorList.get(1).getArrayValue(1)){
				wiActiveArray[1] =! wiActiveArray[1];
				System.out.println("SENSOR0"+checkBoxSensorList.get(1).getArrayValue(1));
				sendArduinoWi(myPort);
			}
			checkBoxSensorArray[1] = checkBoxSensorList.get(1).getArrayValue();
		} else if (theEvent.isFrom(checkBoxSensorList.get(2))) {
			if(checkBoxSensorArray[2][0] != checkBoxSensorList.get(2).getArrayValue(0)){
				flashActiveArray[2] =! flashActiveArray[2];
				System.out.println("SENSOR2"+checkBoxSensorList.get(2).getArrayValue(0));
				sendArduinoFlash(myPort);
			} else if(checkBoxSensorArray[2][1] != checkBoxSensorList.get(2).getArrayValue(1)){
				wiActiveArray[2] =! wiActiveArray[2];
				System.out.println("SENSOR0"+checkBoxSensorList.get(2).getArrayValue(1));
				sendArduinoWi(myPort);
			}
			checkBoxSensorArray[2] = checkBoxSensorList.get(2).getArrayValue();
		} else if (theEvent.isFrom(checkBoxSensorList.get(3))) {
			if(checkBoxSensorArray[3][0] != checkBoxSensorList.get(3).getArrayValue(0)){
				flashActiveArray[3] =! flashActiveArray[3];
				System.out.println("SENSOR3"+checkBoxSensorList.get(3).getArrayValue(0));
				sendArduinoFlash(myPort);
			} else if(checkBoxSensorArray[3][1] != checkBoxSensorList.get(3).getArrayValue(1)){
				wiActiveArray[3] =! wiActiveArray[3];
				System.out.println("SENSOR0"+checkBoxSensorList.get(3).getArrayValue(1));
				sendArduinoWi(myPort);
			}
			checkBoxSensorArray[3] = checkBoxSensorList.get(3).getArrayValue();
		} else if (theEvent.isFrom(checkBoxSensorList.get(4))) {
			if(checkBoxSensorArray[4][0] != checkBoxSensorList.get(4).getArrayValue(0)){
				flashActiveArray[4] =! flashActiveArray[4];
				System.out.println("SENSOR4"+checkBoxSensorList.get(4).getArrayValue(0));
				sendArduinoFlash(myPort);
			} else if(checkBoxSensorArray[4][1] != checkBoxSensorList.get(4).getArrayValue(1)){
				wiActiveArray[4] =! wiActiveArray[4];
				System.out.println("SENSOR0"+checkBoxSensorList.get(4).getArrayValue(1));
				sendArduinoWi(myPort);
			}
			checkBoxSensorArray[4] = checkBoxSensorList.get(4).getArrayValue();
		}
		
		if(theEvent.getGroup().getName() == "checkBox") {
		    print("got an event from "+theEvent.getName()+"\t");
		    //System.out.println(theEvent.getGroup().getArrayValue().length);
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
		    	System.out.println("BUTTON4");
		    } else if(checkbox_array[4] != theEvent.getGroup().getArrayValue(4)) {
		    	activeArray[4] =! activeArray[4];
		    	System.out.println("BUTTON5");
		    } else if(checkbox_array[5] != theEvent.getGroup().getArrayValue(5)) {
		    	activeArray[5] =! activeArray[5];
		    	System.out.println("BUTTON6");
		    } else if(checkbox_array[6] != theEvent.getGroup().getArrayValue(6)) {
		    	activeArray[6] =! activeArray[6];
		    	System.out.println("BUTTON7");
		    } else if(checkbox_array[7] != theEvent.getGroup().getArrayValue(7)) {
		    	activeArray[7] =! activeArray[7];
		    	System.out.println("BUTTON8");
		    }
		    
		}
		//println("\t "+theEvent.getValue());
		//System.out.println("ACTIVE_ARRAY: "+activeArray[0]+" "+activeArray[1]);
		checkbox_array = checkbox.getArrayValue();
		scp.clearSysA();
		scp.clearSysB();
	
		//System.out.println(theEvent.getGroup().getName());
		
	
	}
	
	public void sendArduinoFlash(Serial myPort){
		System.out.println("UPDATE FLASH TO ARDUINO");
		String flash = "UPDATE-FLASH:";
		for(int i=0; i<flashActiveArray.length; i++){
			if(flashActiveArray[i]==true){
				flash += "true,";
			}else{
				flash += "false,";
			}
		}
		flash += '\n';
		myPort.write(flash);
		System.out.println(flash);
	}
	
	public void sendArduinoWi(Serial myPort){
		System.out.println("UPDATE WI TO ARDUINO");
		String wi = "UPDATE-WI:";
		for(int i=0; i<wiActiveArray.length; i++){
			if(wiActiveArray[i]==true){
				wi += "true,";
			}else{
				wi += "false,";
			}
		}
		wi += '\n';
		myPort.write(wi);
		System.out.println(wi);
	}
	
	public void sendArduinoSetup(Serial myPort){
		System.out.println("UPDATE SETUP TO ARDUINO");
		String setup = "UPDATE-SETUP:SETUP\n";
		myPort.write(setup);
		System.out.println(setup);
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
	
	public void SETUP(int theValue) {
		sendArduinoSetup(myPort);
	}

}