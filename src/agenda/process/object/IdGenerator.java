package agenda.process.object;

import java.util.Random;

public class IdGenerator {
	static private Random rand = new Random();
	
	static public long getId(){
		long id1 = rand.nextInt(1000000);
		long id2 = System.currentTimeMillis()*1000000;
		long id = id1 + id2;
		return id;
		
	}
}
