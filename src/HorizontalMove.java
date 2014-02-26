import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class HorizontalMove {

	protected ArrayList<Dot> dotList = new ArrayList<Dot>();
	private int DOT_LENGTH = 50;

	public HorizontalMove(LinkedList<Nozzle> path) {
		int lifeSizeFactor = 255/(DOT_LENGTH-1);
		for(int i=0; i<DOT_LENGTH; i++) {
			dotList.add(new Dot(-1-i, 0, 255-i*lifeSizeFactor, path));
		}
	}
	
	public void update() {
		for(Iterator<Dot> dotIterator = dotList.iterator(); dotIterator.hasNext();){
			Dot dot = dotIterator.next();

				if(dot.x>=dot.current.sysA.width){
					//System.out.println("Bla"+ld.clone.toString());
					//ld.current = ld.clone.removeLast();
					dot.x = 0;
					dot.next();
				}else{
					dot.x += 1;
					//dot.y += 1;	
				}
				
				if(dot.clone.size()==0 && dot.x>=dot.current.sysA.width){
					dotIterator.remove();
				}
				
				//System.out.println("Position X:"+ld.x);	
			}
	}
	
}
