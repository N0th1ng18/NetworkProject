package Server.Network;
import java.net.InetAddress;

import Server.Entities.ServerObject;
import Tools.Input;
import Tools.Timer;

public class Connection_Server {
	//ID
	private int ID;
	
	//Connection Attributes
	private String name;
	private InetAddress address;
	private int port;
	Timer connectionTimer;
	private boolean updated;
	
	//Lobby Attributes
	private int color = 0; //0-red, 1-blue, 2-green
	private int ready; //0-false, 1-true
	
	//Game
	public ServerObject player;
	
	
	
	//Server Connection
	public Connection_Server(InetAddress address, int port, String name) {
		this.name = name;
		this.address = address;
		this.port = port;
		connectionTimer = new Timer();
		this.color = 0;
		ready = 0;
	}
	
	//Client Connection
	public Connection_Server(int ID, String name) {
		this.ID = ID;
		this.name = name;
		connectionTimer = new Timer();
		this.color = 0;
		ready = 0;
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

	public double getConnectionTime() {
		return connectionTimer.getSeconds();
	}

	public void startTimer() {
		connectionTimer.start();
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
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
