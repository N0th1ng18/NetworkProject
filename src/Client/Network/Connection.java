package Client.Network;
import java.net.InetAddress;
import Client.Entities.Object;

import Server.Entities.ServerObject;
import Tools.Input;
import Tools.Timer;

public class Connection {
	//ID
	private int ID;
	
	//Connection Attributes
	private String name;
	private InetAddress address;
	private int port;
	Timer connectionTimer;
	private boolean updated;
	
	//Lobby Attributes
	private int ready; //0-false, 1-true
	
	//Game
	public Object player;
	
	//Client Connection
	public Connection(int ID, String name) {
		this.ID = ID;
		this.name = name;
		connectionTimer = new Timer();
		ready = 0;
		player = new Object();
	}

	
	

	
	
	
	//Getters and Setters
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		player.setName(name);
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public double getTimeSeconds() {
		return connectionTimer.getSeconds();
	}

	public void startTimer() {
		connectionTimer.start();
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public void setReady(int ready) {
		this.ready  = ready;
	}
	
	public int getReady() {
		return ready;
	}
	
}
