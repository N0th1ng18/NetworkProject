package Server;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import Client.Entities.Entities;
import Server.Entities.ServerEntities;
import Server.Entities.ServerObject;
import Server.Network.ServerGameState;
import Server.Network.ServerProtocol;
import Server.Network.ServerSocketListener;
import Server.Network.SocketData;
import Tools.IpChecker;
import Tools.Timer;

public class Server extends ServerLoop implements Runnable {
	
	private static final long serialVersionUID = -8018690593753158968L;

	//Game Settings
	public static double START_TIME = 5.0;
	
	//Exit 
	public volatile CountDownLatch openSocketLatch;
	public volatile int socketStatus;
	public volatile CountDownLatch returnLatch;

	//GameState
	public static ServerGameState gameState;
	private static final double CONNECTION_TIMEOUT = 5.0;
	
	//Socket
	private DatagramSocket socket;
	private int port;
	private String password;
	private int packetSize = 256;
	
	//Host Information
	private String hostName;
	private String internal_IP;
	private String external_IP;
	
	//Socket Listener
	private ServerSocketListener socketListener;
	private Thread ssLThread;
	private static boolean ssLThreadIsRunning;
	private BlockingQueue<SocketData> blockingQueue;
	private int BLOCKING_QUEUE_MAX_SIZE = 20;
	
	//Tick
	public static int tick;
	
	//GameOver Timer
	private Timer gameOver = new Timer();
	private boolean gameOverBool = true;
	
	
	public Server(int port, String password) {
		this.port = port;
		this.password = password;
		openSocketLatch = new CountDownLatch(1);
		returnLatch = new CountDownLatch(1);
		gameState = new ServerGameState();
		blockingQueue = new ArrayBlockingQueue<SocketData>(BLOCKING_QUEUE_MAX_SIZE);
		tick = 0;
	}
	
	@Override
	public void run() {
		openSocket();
		if(socketStatus == 0) {
			startSocketListener();
			loop();
		}
		terminate();
	}

	@Override
	void update(double time, double dt) {
		
		//Get Incoming Packets
		while(!blockingQueue.isEmpty()) {
			ServerProtocol.processPacket(blockingQueue.poll(), password, socket);
		}
		
		switch(gameState.getScreenType()) {
		case 3://Lobby
			
			if(readyCheck()) {
				unReadyPlayers();
				initGame();
				gameState.setScreenType(4);
			}
			break;
		case 4://Game
			tick++;
			ServerEntities.update(time, dt);
			
			if(finishCheck() == true && gameOverBool == true) {
				gameOverBool = false;
				gameOver.start();
			}
			
			if(finishCheck() == true && gameOver.getSeconds() >= 1.0) {
				gameState.setScreenType(5);
			}
			break;
		case 5://Game Over
			if(gameOver.getSeconds() >= 8.0) {
				resetPlayers();
				gameOverBool = true;
				gameState.setScreenType(3);
			}
			break;
		}
		
		//Update Entities
		updateClients();
		
		removeTimedOutClients();
		
		//System.out.println(ServerEntities.objectCount);
		
		/*
		String s = gameState.toString();
		if(s.compareTo("") != 0)
			System.out.println(gameState.toString());
		*/
	}

	@Override
	void render(double alpha) {
		
		
	}
	
	
	
	
	
	
	
	
	
	private void updateClients() {
		//Update Connections
			switch(gameState.getScreenType()) {
			case 3://Lobby
				for(int i=0; i < gameState.connections.getNumConnections(); i++) {
					
					byte[] outputBuffer = ServerProtocol.getBytes_Lobby_Packet(gameState.connections.get(i).getID());
					DatagramPacket sendPacket = new DatagramPacket(outputBuffer, outputBuffer.length, gameState.connections.get(i).getAddress(), gameState.connections.get(i).getPort());
			
					try {
						socket.send(sendPacket);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				break;
			case 4://Game
				for(int i=0; i < gameState.connections.getNumConnections(); i++) {
					
					byte[] outputBuffer = ServerProtocol.getBytes_Game_Packet(gameState.connections.get(i).getID());
					DatagramPacket sendPacket = new DatagramPacket(outputBuffer, outputBuffer.length, gameState.connections.get(i).getAddress(), gameState.connections.get(i).getPort());
			
					try {
						socket.send(sendPacket);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				break;
				
			case 5://GameOver
				for(int i=0; i < gameState.connections.getNumConnections(); i++) {
					
					byte[] outputBuffer = ServerProtocol.getBytes_GameOver_Packet(gameState.connections.get(i).getID());
					DatagramPacket sendPacket = new DatagramPacket(outputBuffer, outputBuffer.length, gameState.connections.get(i).getAddress(), gameState.connections.get(i).getPort());
			
					try {
						socket.send(sendPacket);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
	}
	
	private void removeTimedOutClients() {
		for(int i=0; i < gameState.connections.getNumConnections(); i++) {
			if(gameState.connections.get(i).getConnectionTime() >= CONNECTION_TIMEOUT) {
				ServerEntities.removeObject(gameState.connections.get(i).player);
				gameState.connections.removeConnection(i);
			}
		}
	}
	
	private boolean readyCheck() {
		boolean everyoneIsReady = true;
		for(int i=0; i < gameState.connections.getNumConnections(); i++) {
			if(gameState.connections.get(i).getReady() == 0) {
				everyoneIsReady = false;
			}
		}
		
		return everyoneIsReady;
	}
	
	private boolean finishCheck() {
		boolean everyoneFinished = true;
		for(int i=0; i < gameState.connections.getNumConnections(); i++) {
			if(gameState.connections.get(i).player.finishTime < 0) {
				everyoneFinished = false;
			}
		}
		
		return everyoneFinished;
	}
	
	private void unReadyPlayers() {
		for(int i=0; i < gameState.connections.getNumConnections(); i++) {
			gameState.connections.get(i).setReady(0);
		}
	}
	
	private void resetPlayers() {
		for(int i=0; i < gameState.connections.getNumConnections(); i++) {
			//Spawn point - make them not touch
			gameState.connections.get(i).player = null;
		}
		ServerEntities.removeAll();
	}
	
	private void initGame() {
		//GameTime
		ServerLoop.time = 0.0;
		//Entities
		ServerEntities.init();
		//Create Client Player Objects
		for(int i=0; i < gameState.connections.getNumConnections(); i++) {
			//Spawn point - make them not touch
			gameState.connections.get(i).player = new ServerObject(36f, 125f);
			ServerEntities.addObject(gameState.connections.get(i).player);
		}
	}
	
	
	private void openSocket() {
		socketStatus = 0;
		
		try {
			socket = new DatagramSocket(port);
			
			hostName = InetAddress.getLocalHost().getHostName();
			internal_IP = InetAddress.getLocalHost().getHostAddress();
			external_IP = IpChecker.getIp();
			
			System.out.println("Server: Host Name:		" + hostName);
			System.out.println("Server: Internal Address:	" + ""+internal_IP + ":" + port);
			System.out.println("Server: External Address:	" + external_IP + ":" + port);
			
		} catch (SocketException e) {
			System.out.println("Server/openSocket(): Failed to listen on port: " + port);
			socketStatus = 1;
			stop();
		}  catch (UnknownHostException e) {
			System.out.println("Server/openSocket(): Local host name could not be resolved into an address.");
			socketStatus = 2;
			stop();
		} catch (Exception e) {
			System.out.println("Server/openSocket(): Exception");
			socketStatus = 3;
			stop();
		}
		
		openSocketLatch.countDown();
	}
	
	private void closeSocket() {
		if(socket != null)
			socket.close();
	}
	
	private void disconnectClients() {
		for(int i=0; i < gameState.connections.getNumConnections(); i++) {
			
			byte[] outputBuffer = ServerProtocol.getBytes_Disconnect_Packet();
			DatagramPacket sendPacket = new DatagramPacket(outputBuffer, outputBuffer.length, gameState.connections.get(i).getAddress(), gameState.connections.get(i).getPort());
	
			try {
				socket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void startSocketListener() {
		socketListener = new ServerSocketListener(socket, blockingQueue, packetSize);
		ssLThread = new Thread(socketListener);
		ssLThread.start();
		ssLThreadIsRunning = true;
	}
	
	public void terminateServerSocketListener() {
		if(ssLThreadIsRunning == true && ssLThread.isAlive()) {
			socketListener.stop();
			ssLThreadIsRunning = false;
			
			try {
				socketListener.returnLatch.await();
			} catch (InterruptedException e) {
				System.out.println("Failed to Terminate SocketListener");
			}
		}
	}
	
	public void stop() {
		running = false;
	}
	
	private void terminate() {
		disconnectClients();
		terminateServerSocketListener();
		closeSocket();
		System.out.println("Server Terminated");
		returnLatch.countDown();
	}

}
