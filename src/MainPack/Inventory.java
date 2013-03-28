package MainPack;

public class Inventory {

	public enum InvState{ INGAME, INVENTORY }
	InvState state;
	public Inventory(){
		state = InvState.INGAME;
	}
	
	public void draw(){
		switch(state){
		case INGAME:
			
			break;
		case INVENTORY:
			
			break;
		}
	}
}
