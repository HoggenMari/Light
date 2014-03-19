import processing.core.PApplet;
import ijeoma.motion.Motion;
import ijeoma.motion.tween.Tween;


public class Breath {
	
	private PApplet p;
	private Tween t;

	public Breath(PApplet p){
		this.p = p;
		Motion.setup(p);
		
		t = new Tween(300)
	    .addColor(this, "c1", p.color(60, 0, 120))
	    .addColor(this, "c2", p.color(60, 0, 120))
	    .addColor(this, "c3", p.color(60, 0, 120))
	    .addColor(this, "c4", p.color(60, 0, 120))
	    .addColor(this, "c5", p.color(60, 0, 120))
	    .play(); 
	}

}
