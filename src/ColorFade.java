import processing.core.PApplet;


public class ColorFade {

	PApplet p;
	int hue;
	int saturation;
	int brightness;
	boolean running = false;
	
	boolean loop = false;
	
	int saturationSavedTime;
	int saturationTotalTime;
	int saturationEnd;
	int saturationStart;
	int saturationAdd;
	int saturationLoop = 0;
	int saturationLoopMax = Integer.MAX_VALUE;
	boolean activeSaturation = false;
	
	int brightnessSavedTime;
	int brightnessTotalTime;
	int brightnessEnd;
	int brightnessStart;
	int brightnessAdd;
	int brightnessLoop = 0;
	int brightnessLoopMax = Integer.MAX_VALUE;
	boolean activeBrightness = false;

	int hueSavedTime;
	int hueTotalTime;
	int hueEnd;
	int hueStart;
	int hueAdd;
	int hueLoop = 0;
	int hueLoopMax = Integer.MAX_VALUE;
	boolean activeHue = false;
	
	public ColorFade(PApplet p, int hueStart, int saturationStart, int brightnessStart) {
		this.p = p;
		this.hue = hueStart;
		this.hueStart = hueStart;
		this.saturation = saturationStart;
		this.saturationStart = saturationStart;
		this.brightness = brightnessStart;
		this.brightnessStart = brightnessStart;
		
	}
	
	public void hueFade(int hueEnd, int duration) {
		this.hueEnd = hueEnd;
		int diff = Math.abs(hue-hueEnd);
		if(diff!=0) {
		this.hueTotalTime = duration/diff;
		if(hueEnd < hue) {
			hueAdd = -1;
		}else{ 
			hueAdd = 1;
		}
		//System.out.println(diff+" "+hueTotalTime);	
		activeHue = true;
		}
	}
	
	public void hueFade(int hueEnd, int duration, int hueLoopMax) {
		loop = true;
		this.hueEnd = hueEnd;
		this.hueLoopMax = hueLoopMax;
		int diff = Math.abs(hue-hueEnd);
		if(diff!=0) {
		this.hueTotalTime = duration/diff;
		if(hueEnd < hue) {
			hueAdd = -1;
		}else{ 
			hueAdd = 1;
		}
		//System.out.println(diff+" "+hueTotalTime);	
		activeHue = true;
		}
	}
	
	public void saturationFade(int saturationEnd, int duration) {
		this.saturationEnd = saturationEnd;
		int diff = Math.abs(saturation-saturationEnd);
		if(diff!=0) {
		this.saturationTotalTime = duration/diff;
		if(saturationEnd < saturation) {
			saturationAdd = -1;
		}else{ 
			saturationAdd = 1;
		}
		//System.out.println(diff+" "+saturationTotalTime);	
		activeSaturation = true;
		}
	}
	
	public void saturationFade(int saturationEnd, int duration, int saturationLoopMax) {
		loop = true;
		this.saturationLoopMax = saturationLoopMax;
		this.saturationEnd = saturationEnd;
		int diff = Math.abs(saturation-saturationEnd);
		if(diff!=0) {
		this.saturationTotalTime = duration/diff;
		if(saturationEnd < saturation) {
			saturationAdd = -1;
		}else{ 
			saturationAdd = 1;
		}
		//System.out.println(diff+" "+saturationTotalTime);	
		activeSaturation = true;
		}
	}
	
	public void brightnessFade(int brightnessEnd, int duration) {
		this.brightnessEnd = brightnessEnd;
		int diff = Math.abs(brightness-brightnessEnd);
		this.brightnessTotalTime = duration/(int)diff;
		if(brightnessEnd < brightness) {
			brightnessAdd = -1;
		}else{ 
			brightnessAdd = 1;
		}
		//System.out.println(diff+" "+brightnessTotalTime);	
		activeBrightness = true;
	}
	
	public void brightnessFade(int brightnessEnd, int duration, int brightnessLoopMax) {
		loop = true;
		this.brightnessEnd = brightnessEnd;
		this.brightnessLoopMax = brightnessLoopMax;
		int diff = Math.abs(brightness-brightnessEnd);
		this.brightnessTotalTime = duration/(int)diff;
		if(brightnessEnd < brightness) {
			brightnessAdd = -1;
		}else{ 
			brightnessAdd = 1;
		}
		//System.out.println(diff+" "+brightnessTotalTime);	
		activeBrightness = true;
	}
}
