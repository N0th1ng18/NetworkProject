package Client;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.PortUnreachableException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javax.swing.JFrame;
import Client.Entities.Entities;
import Client.Network.ClientGameState;
import Client.Network.ClientProtocol;
import Client.Network.ClientSocketListener;
import Client.UI.LobbyScreen;
import Client.UI.UIManager;
import Server.Server;
import Server.Network.SocketData;
import Client.Entities.Object;
import Tools.Images;
import Tools.IpChecker;
import Tools.Maps;
import Tools.Timer;

public class Client extends GameLoop{
	
	/*
	 * Pixel
	 * 
	 * 		Created by Nichlas Frey
	 * 		Intro to Computer Networks Project
	 * 		COSC 2327
	 * 
	 * Pixel is a game with online support through a client-server setup.
	 * 		It uses the built in Graphics2D Class in Java for rendering.
	 * 		It uses UDP for the communication process.
	 * 
	 * How it works:
	 * 		
	 * 		Client: 	Client can host a server. 
	 * 
	 * 
	 * 				*Human Inputs (w,a,s,d) -> Server						(OUT-TO-SERVER		*Sends data on Client Thread.)
	 * 				*ServerGameState -> Render to Screen. 					(IN-FROM-SERVER		*Receives data on seperate Listening Thread.)
	 * 		
	 * 		Server:		  
	 * 
	 * 
	 * 				*Inputs from Clients -> Update ServerGameState			(IN-FROM-CLIENTS	*Receives data with seperate Listening Thread.)
	 * 				*ServerGameState -> ALL Clients.						(OUT-TO-CLIENTS		*Sends data on Server Thread.)
	 * 				
	 * 
	 * Pixel SOURCE CODE SETUP
	 * 		-Client
	 * 			*Client							Client's main class that creates the Client's window and defines Update() and Render()							####IMPORTANT####	
	 * 												for the Client Application.  If hosting a server, manages the creation and 
	 * 												termination of the server.
	 * 			*GameLoop						-Manages the math behind the Client side game loop.  The loop has a Fixed time step (60 updates per second)
	 * 												and an UNFIXED render()(potentially unlimited frames per second).
	 * 			*KeyInput						-Manages Key Presses.
	 * 			*MouseInput						-Manages Mouse actions and movements.
	 * 			*RawData						-A structure that is used to store the client's input.
	 * 			-Entities
	 * 				*Entities					-Class that manages the creation, deletion, update, and rendering of "things" 
	 * 												(objects, buttons, labels, cameras, maps,...) in the game.
	 * 				*Animation					-Class that manages the creation and playing of an animation.
	 * 				*Button						-Class that defines a button.
	 * 				*Camera						-Class that defines the camera ("What the screen is looking at").
	 * 				*Label						-Class that defines a Label. ("Screen text").
	 * 				*Map						-Class that defines the map to play on.
	 * 				*Object						-Class to defines an object. Players are objects.
	 * 				*ScoreBoard					-Class that defines the scoreboard.
	 * 			-Network
	 * 				*ClientProtocol				-IMPORTANT - Manages the communication between the Client and the Server on the Client side.					####IMPORTANT####	
	 * 				*ClientGameState			-The ClientGameState is what the server updates so the Client can render that information.
	 * 				*ClientPacket				-The Client structure for managing and creating a packet to send to the server.  Supports
	 * 												"send when changed" function to reduce packet size and increase networking performance.
	 * 				*ClientSocketListener		-A seperate thread that handles incomming messages from the Server to the Client. (Listening Thread)
	 * 				*Connection					-Defines the attributes of a connection on the Client.
	 * 				*Connections				-Structure to mantain all connections.
	 * 			-UI
	 * 				*UIManager					-Manages the switching of different screens.
	 * 				*MenuScreen					-------------------------------------------------------
	 * 				*SettingScreen
	 * 				*MultiplayerScreen
	 * 				*HostServerScreen				The Different Screens that can be switched to.
	 * 				*LobbyScreen
	 * 				*WinnerScreen
	 * 				*LoadingScreen				-------------------------------------------------------
	 * 		-Server
	 * 				*Server						-Main Server class that runs and manages the game. (Only Update() no Render())									####IMPORTANT####		
	 * 				*ServerLoop					-Manages the math behind the Server side game loop.  The loop has a Fixed time step (60 updates per second)
	 * 												and an UNFIXED render()(potentially unlimited frames per second).  Render is not used for the Server.
	 * 												The Server only Updates().
	 * 				-Entities
	 * 					*ServerEntities			-Class that manages the creation, deletion, update, and rendering of "things" 
	 * 												(objects, buttons, labels, cameras, maps,...) in the game on the Server side.
	 * 					*Animation				-Class that manages and defines an animation on the server side.
	 * 					*Button					-Server version of Button class.
	 * 					*Label					-Server version of Label class.
	 * 					*Map_Server				-Server version of Map class.
	 * 					*ServerObject			-Server version of Object Class. Players are objects.
	 * 				-Network
	 * 					*ServerProtocol			-IMPORTANT - Manages the communication between the Client and Server on the Server side.						####IMPORTANT####	
	 * 					*ServerGameState		-The state of the game.  This is sent to the clients to update their gameState to be rendered.
	 * 					*Connection_Server		-Server version of the Connection class.
	 * 					*Connections_Server		-Server version of the Connections class.
	 * 					*ServerSocketListener	-Seperate thread that receives messages from all clients. (Listening Thread)
	 * 					*SocketData				-Small structure to organize Socket data.
	 * 		-Tools
	 * 				*Images						-Class to import all images in the game
	 * 				*Input						-Class to define basic key ints (acts like booleans)
	 * 				*IpChecker					-Class to look up external address to the server
	 * 				*Maps						-Class that imports images and defines a map. (Currently only an image)
	 * 				*Timer						-Class to use a timer. (useful for anything that is timed)
	 * 				*Vec2						-Vector class to define a vector(x, y)
	 * 		-Sprites
	 * 
	 * 
	 */
	
	private static final long serialVersionUID = 7592609184714604614L;

	//Client
	public static RawData rawData;
	public static int tick;
	public static ClientGameState gameState;
	public static volatile int clientID = -1;
	public static int MAX_NAME_LENGTH = 18;
	public static int connectionStatus;
	/*  ConnectionStatus
	 *  case  0: Not Connected
	 *  case  1: Connection Timeout
	 *  case  2: Connected
	 */
	//-----------------------------------------------
	
	//JFrame
	public static JFrame frame;
	public static int gWidth = 1400;
	public static int gHeight = 720;
	public static int sWidth;
	public static int sHeight;
	//-----------------------------------------------
	
	//Input
	public static KeyInput keyInput = new KeyInput();
	public static MouseInput mouseInput = new MouseInput();
	//-----------------------------------------------
	
	/*Pixels -> 0xAARRGGBB*/
	private BufferedImage image = new BufferedImage(gWidth, gHeight, BufferedImage.TYPE_INT_RGB);
	//-----------------------------------------------

	//Defaults
	public static String name = "Player";
	public static String serverPassword = "password";
	public static String IP = "";
	//-----------------------------------------------
	
	//Server
	public static Server server;
	public static Thread serverThread;
	public static boolean serverIsRunning;
	public static Timer serverTimeoutTimer;
	public static final double SERVER_TIMEOUT = 5.0;
	public static Timer packetsPerSecondTimer;
	public static final double PACKETS_PER_SECOND = 30.0;
	public static final double PACKETS_PER_SECOND_TIME = 1.0 / PACKETS_PER_SECOND;
	//-----------------------------------------------
	
	//Socket Listener
	public static ClientSocketListener socketListener;
	public static Thread sLThread;
	public static boolean sLThreadIsRunning;
	//-----------------------------------------------
	
	//Socket
	static DatagramSocket socket;
	public static int port = 9090;
	public static int packetSize = 256;
	private static byte[] sendData = new byte[packetSize];
	private static BlockingQueue<SocketData> blockingQueue;
	private int BLOCKING_QUEUE_MAX_SIZE = 20;
	//-----------------------------------------------
	
	//Font
	private static Font font = new Font("TimesRoman", Font.PLAIN, 12);
	//-----------------------------------------------
	
	//Render Details
	public static AlphaComposite defaultTrans = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
	
	public static void main(String[] args) {
		Client client = new Client();
		client.init();
		client.loop();
		client.terminate();
	}

	
	private void init() {
		
		//Input
		addKeyListener(keyInput);
		addMouseListener(mouseInput);
	
		rawData = new RawData(name);
		gameState = new ClientGameState();
		
		blockingQueue = new ArrayBlockingQueue<SocketData>(BLOCKING_QUEUE_MAX_SIZE);
		
		try {
			IP = InetAddress.getLocalHost().getHostAddress();
			LobbyScreen.externalIP = IpChecker.getIp();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		//Images
		Images.load();
		
		//Map
		Maps.load();
		
		//Entities
		Entities.init();
		
		//MenuScreen
		connectionStatus = 0;
		//Menu Screen
		UIManager.init(0);
		UIManager.goTo(UIManager.MENU_SCREEN);
		
		//Screen Dimensions
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		sWidth = (int)screen.getWidth();
		sHeight = (int)screen.getHeight();
		
		//Canvas size
		setMinimumSize(new Dimension(gWidth,gHeight));
		setMaximumSize(new Dimension(gWidth,gHeight));
		setPreferredSize(new Dimension(gWidth,gHeight));
		
		
		//Jframe
		frame = new JFrame();
		frame.setTitle("PIXEL");
		frame.setSize(gWidth, gHeight);
		frame.setLocation((sWidth/2) - (gWidth/2), (sHeight/2) - (gHeight/2));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				terminate();
			   }
		});
		frame.setLayout(new BorderLayout());
		frame.add(this, BorderLayout.CENTER);
		frame.setResizable(false);
		this.setFocusable(true);
		frame.setFocusable(true);
		frame.setVisible(true);
		
		//Tick
		tick = 0;
		
	}

	@Override
	void update(double time, double dt) {
		switch(UIManager.getScreen()) {
		case 4:
			tick++;
			break;
		}
		
		//Inputs
		MouseInput.update();
		KeyInput.update();
		
		
		if(socket != null && !socket.isClosed()) {
			//Send To Server
			if(packetsPerSecondTimer != null && packetsPerSecondTimer.getSeconds() >= PACKETS_PER_SECOND_TIME) {
				updateServer();
				packetsPerSecondTimer.start();
			}
			
			//Receive From SocketListener
			while(!blockingQueue.isEmpty()) {
				ClientProtocol.processPacket(blockingQueue.poll());
			}
			

			checkServerTimout();
		}
		
		
		

		
		//Entities
		Entities.update(time, dt);
		
		//System.out.println(Entities.objectCount);
		/*
		String s = gameState.toString();
		if(s.compareTo("") != 0)
			System.out.println(gameState.toString());
		*/

	}

	@Override
	void render(double alpha, double dt) {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2 = (Graphics2D)g;
	    g2.setRenderingHint(RenderingHints.KEY_RENDERING,
	    					RenderingHints.VALUE_RENDER_QUALITY);
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	    					RenderingHints.VALUE_ANTIALIAS_ON);
		/*****DRAW HERE***************/
	    g2.drawImage(image, 0, 0, gWidth, gHeight, null);
	    
	    
		Entities.render(g2, alpha, dt);


		/*
		 * FPS
		 */
		if(showFPS == true) {
			g2.setColor(Color.GREEN);
			g2.setFont(font);
			g2.drawString(FPS+"",0, 10);
			g2.drawString(UPDATES+"",0, 22);
		}
		/*****************************/
		g2.dispose();
		g.dispose();
		bs.show();
	}
	
	
	private void updateServer() {
		byte[] outputBuffer;
		DatagramPacket sendPacket = null;
		
		switch(UIManager.getScreen()) {
		case 3://Lobby
			
			outputBuffer = ClientProtocol.getBytes_Lobby_Packet(clientID);
			try {
				sendPacket = new DatagramPacket(outputBuffer, outputBuffer.length, InetAddress.getByName(IP), port);
			} catch (UnknownHostException e1) {
				System.out.println("Client/updateServer: UnknownHostException");
				break;
			}
	
			try {
				if(!socket.isClosed())
					socket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case 4://Game
			
			outputBuffer = ClientProtocol.getBytes_Game_Packet(clientID);
			
			try {
				sendPacket = new DatagramPacket(outputBuffer, outputBuffer.length, InetAddress.getByName(IP), port);
			} catch (UnknownHostException e1) {
				System.out.println("Client/updateServer: UnknownHostException");
				break;
			}
	
			try {
				if(!socket.isClosed())
					socket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case 5://GameOver
			outputBuffer = ClientProtocol.getBytes_GameOver_Packet(clientID);
			
			try {
				sendPacket = new DatagramPacket(outputBuffer, outputBuffer.length, InetAddress.getByName(IP), port);
			} catch (UnknownHostException e1) {
				System.out.println("Client/updateServer: UnknownHostException");
				break;
			}
	
			try {
				if(!socket.isClosed())
					socket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
		
	}
	
	private void checkServerTimout() {
		if(serverTimeoutTimer != null && serverTimeoutTimer.getSeconds() >= SERVER_TIMEOUT){
			serverTimeoutTimer = null;
			terminateSocketListener();
			terminateServer();
			for(int i=0; i < gameState.connections.getNumConnections(); i++) {
				Entities.removeObject(gameState.connections.get(i).player);
			}
			gameState.connections.removeConnections();
			Client.connectionStatus = 3;
			UIManager.goTo(UIManager.LOADING_SCREEN);
		}
	} 
	
	
	public static void openSocket() {
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("Client/openSocket(): Failed to Construct and Bind Socket to Port");
		}
	}
	
	public static void requestToJoinServer(InetAddress address) {
		//Spawn new Thread that opens socket and listens on port
		Client.socketListener = new ClientSocketListener(socket, blockingQueue, packetSize);
		Client.sLThread = new Thread(Client.socketListener);
		Client.sLThread.start();
		Client.sLThreadIsRunning = true;
		
		sendData = ClientProtocol.getBytes_ConnectionRequest_Packet(name, serverPassword);
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
		try {
			socket.send(sendPacket);
		} catch (PortUnreachableException e) {
			System.out.println("Client/joinServer: Failed to Send on Socket due to an PortUnreachableException");
		} catch (IOException e) {
			System.out.println("Client/joinServer: Failed to Send on Socket due to an IOException");
		}
		gameState.connectionRequestTimer.start();
		
	}
	
	public static void disconnectFromServer(InetAddress address){
		
		byte[] outputBuffer = ClientProtocol.getBytes_Disconnect_Packet(clientID);
		DatagramPacket sendPacket = null;
		sendPacket = new DatagramPacket(outputBuffer, outputBuffer.length, address, port);

		try {
			if(!socket.isClosed())
				socket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void terminateSocketListener() {
		if(sLThreadIsRunning == true && sLThread.isAlive()) {
			socketListener.stop();
			sLThreadIsRunning = false;
			if(socket != null)
				socket.close();
			try {
				socketListener.returnLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void spawnServer() {
		Client.server = new Server(port, serverPassword);
		Client.serverThread = new Thread(server);
		Client.serverThread.start();
		Client.serverIsRunning = true;
	}
	
	public static void terminateServer() {
		if(serverIsRunning == true && serverThread.isAlive()) {
			server.stop();
			serverIsRunning = false;
			
			try {
				server.returnLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void terminate() {
		terminateSocketListener();
		terminateServer();

		System.out.println("Client Terminated");
		System.exit(0);
	}
}
