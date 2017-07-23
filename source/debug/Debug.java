package debug;

import java.util.ArrayList;

import structs.GameCharacter;

public class Debug implements Runnable{

	ArrayList<GameCharacter> characters;
	
	
	public Debug(ArrayList<GameCharacter> characters){
		this.characters = characters;
	}
	@Override
	public void run() {
		while(true){
			System.out.println(characters.size());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
