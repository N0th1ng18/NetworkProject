package Client;

import Tools.Input;

public class RawData {
	
	//Basic Attributes
	private String name;
	
	//Lobby Attributes
	private int color; //0-red, 1-blue, 2-green
	private int ready; //0-false, 1-true
	
	//Game Attributes
	public Input input;
	
	
	public RawData(String name) {
		this.name = name;
		this.color = 0;
		input = new Input();
		ready = 0;
	}
	
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getReady() {
		return ready;
	}

	public void setReady(int ready) {
		this.ready = ready;
	}
	
	
	
	
}
